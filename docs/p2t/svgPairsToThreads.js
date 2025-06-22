const GF_svgP2T = {
    svgNS: "http://www.w3.org/2000/svg",
    gap: 8,
    lastID: 0, // to be used for unique IDs of over all SVGs on the page

    newSVG: function (w, h) {
        const svg = document.createElementNS(this.svgNS, "svg");
        svg.setAttribute("width", w);
        svg.setAttribute("height", h);
        svg.setAttribute("xmlns", this.svgNS);

        // show full width of lines on the edge of the viewpBox
        svg.setAttribute("viewBox", "-4 -4 " + (w + 8) + " " + (h + 8));
        return svg;
    },

    findSvgDoc: function(svgContainer) {
        let svgDoc = svgContainer;
        while (svgDoc.ownerSVGElement) svgDoc = svgDoc.ownerSVGElement; // find the root SVG document
        return svgDoc;
    },

    newStitch: function (stitchInputValue, firstKissingPathNr, firstNodeNr, svgContainer, containerWidth, containerHeight) {
        let stitch = stitchInputValue
            .toLowerCase()
            .replace(/[^clrt]/g, '');

        const svgDoc = this.findSvgDoc(svgContainer);
        const svgNS = svgContainer.namespaceURI;
        const nrOfInitialPathNodes = stitch.length;
        const nodeSpacing = containerHeight / (nrOfInitialPathNodes + 1); // Vertical spacing between subnodes
        const pathSpacing = containerWidth / 3; // Horizontal spacing between paths
        const edgeStartMap = {}; // Index for edges starting at nodes
        const edgeEndMap = {}; // Index for edges ending at nodes

        function drawLine(x, y) {
            const line = document.createElementNS(svgNS, "line");
            line.setAttribute("x1", x);
            line.setAttribute("y1", (y - nodeSpacing) + '');
            line.setAttribute("x2", x);
            line.setAttribute("y2", y);
            svgContainer.appendChild(line);
            return line;
        }

        function calculatePathDefinition(line) {
            const x1 = line.getAttribute("x1") * 1;
            const y1 = line.getAttribute("y1") * 1;
            const x2 = line.getAttribute("x2") * 1;
            const y2 = line.getAttribute("y2") * 1;

            // Calculate the direction vector of the line segment
            let dx = x2 - x1;
            let dy = y2 - y1;
            const length = Math.sqrt(dx * dx + dy * dy);

            // make one end shorter for over/under effect
            dx = (dx / length) * GF_svgP2T.gap;
            dy = (dy / length) * GF_svgP2T.gap;

            const whiteStart = line.classList.contains("white_start");
            const whiteEnd = line.classList.contains("white_end");

            const classes = Array.from(line.classList).filter(className => className.includes('_at_'));
            if (classes.length > 1) {
                const id0 = classes[0].replace(/.*_/g, "");
                const id1 = classes[1].replace(/.*_/g, "");
                const n0 = svgDoc.getElementById(id0).classList[0];
                const n1 = svgDoc.getElementById(id1).classList[0];
                const startsLeft = classes[0].includes("starts_left");
                const startsRight = classes[0].includes("starts_right");
                // TODO Conditions seems to work. Which coincidence eliminates bends for twist with a cross in between?
                if (n0 === n1 && (startsLeft || startsRight)) {
                    // curved when straight would be on top of one another
                    const midX = (x1 + x2) / 2;
                    const midY = (y1 + y2) / 2;
                    const gapFraction = 0.4;
                    const controlPointDistance = 2 + gapFraction;
                    const controlPointDirection = startsLeft ? -1 : 1;
                    const controlPointX = midX + dy * controlPointDirection * controlPointDistance;
                    const controlPointY = midY - dx * controlPointDirection * controlPointDistance;
                    const startX = !whiteStart ? x1 : x1 + (controlPointX - x1) * gapFraction;
                    const startY = !whiteStart ? y1 : y1 + (controlPointY - y1) * gapFraction;
                    const endX = !whiteEnd ? x2 : x2 + (controlPointX - x2) * gapFraction;
                    const endY = !whiteEnd ? y2 : y2 + (controlPointY - y2) * gapFraction;
                    return `M ${startX} ${startY} Q ${controlPointX} ${controlPointY} ${endX} ${endY}`;
                }
            }
            // default: straight line
            const startX = !whiteStart ? x1 : x1 + dx;
            const startY = !whiteStart ? y1 : y1 + dy;
            const endX = !whiteEnd ? x2 : x2 - dx;
            const endY = !whiteEnd ? y2 : y2 - dy;
            return `M ${startX} ${startY} L ${endX} ${endY}`;
        }

        function drawCircle(x, y, id, description) {
            const circle = document.createElementNS(svgNS, "circle");
            circle.setAttribute("cx", x + '');
            circle.setAttribute("cy", y + '');
            circle.setAttribute("r", (GF_svgP2T.gap - 1) + '');
            circle.setAttribute("id", id);
            circle.setAttribute("class", (description ? description : '').replace(/.* /g, ''));
            const title = document.createElementNS(svgNS, "title");
            title.textContent = `${description} - ${id}`;
            circle.appendChild(title);
            svgContainer.appendChild(circle);
        }

        function mergeNodes(leftNodeId, rightNodeId, id, description) {
            const leftNode = svgDoc.getElementById(leftNodeId);
            const rightNode = svgDoc.getElementById(rightNodeId);

            if (!leftNode || !rightNode) {
                console.error(`One or both nodes not found ${leftNodeId}=${leftNode} or ${rightNodeId}=${rightNode} ${id}`);
                return;
            }

            // Get the coordinates of the left and right nodes
            const leftX = parseFloat(leftNode.getAttribute("cx"));
            const leftY = parseFloat(leftNode.getAttribute("cy"));
            const rightX = parseFloat(rightNode.getAttribute("cx"));
            const rightY = parseFloat(rightNode.getAttribute("cy"));

            const midX = (leftX + rightX) / 2;
            const midY = (leftY + rightY) / 2;
            drawCircle(midX, midY, id, description);

            function updateEdge(nodeId, edgeMap, attrX, attrY, classPrefix) {
                const edge = edgeMap[nodeId];
                if (edge) {
                    edge.setAttribute(attrX, midX);
                    edge.setAttribute(attrY, midY);
                    edge.classList.add(classPrefix + id);
                    delete edgeMap[nodeId];
                }
                return edge;
            }

            const end = "white_end";
            const st = "white_start";
            const isCross = description === "cross";
            updateEdge(leftNodeId, edgeStartMap, "x1", "y1", "starts_left_at_").classList.add(isCross ? st : end);
            updateEdge(leftNodeId, edgeEndMap, "x2", "y2", "ends_left_at_").classList.add(isCross ? st : end);
            updateEdge(rightNodeId, edgeStartMap, "x1", "y1", "starts_right_at_").classList.add(isCross ? end : st);
            updateEdge(rightNodeId, edgeEndMap, "x2", "y2", "ends_right_at_").classList.add(isCross ? end : st);

            leftNode.remove();
            rightNode.remove();
        }

        function addToEdgeMap(nodeMap, pathMap, line, edgeMap) {
            const startNodeId = `${nodeMap}-${pathMap}`;
            if (!edgeMap[startNodeId]) edgeMap[startNodeId] = [];
            edgeMap[startNodeId] = line;
        }

        // Create 4 paths each with the number of subnodes needed by the stitch
        for (let pathNr = 0; pathNr < 4; pathNr++) {
            const x = (pathNr) * pathSpacing; // X-coordinate for the current path

            for (let nodeNr = 0; nodeNr < nrOfInitialPathNodes; nodeNr++) {
                const y = (nodeNr + 1) * nodeSpacing; // Y-coordinate for the current subnode
                drawCircle(x, y, nodeNr + '-' + pathNr);

                // Draw an edge to the node
                const line = drawLine(x, y);
                line.setAttribute("class", "kiss_" + (pathNr + firstKissingPathNr));
                line.classList.add("kiss_" + (pathNr%2 ? 'odd' : 'even'));
                if (nodeNr > 0) addToEdgeMap(nodeNr - 1, pathNr, line, edgeStartMap);
                addToEdgeMap(nodeNr, pathNr, line, edgeEndMap);
            }
            // Draw an edge out of the last node
            const line = drawLine(x, (nrOfInitialPathNodes + 1) * nodeSpacing);
            line.setAttribute("class", "kiss_" + (pathNr + firstKissingPathNr));
            line.classList.add("kiss_" + (pathNr%2 ? 'odd' : 'even'));
            addToEdgeMap(nrOfInitialPathNodes - 1, pathNr, line, edgeStartMap);
        }

        // Make the paths kiss
        let currentNodeNr = firstNodeNr;
        for (let i = 0; i < nrOfInitialPathNodes; i++) {
            switch (stitch[i]) {
                case 'c':
                    mergeNodes(i + '-1', i + '-2', ++currentNodeNr, 'cross');
                    break;
                case 'l':
                    mergeNodes(i + '-0', i + '-1', ++currentNodeNr, 'left twist');
                    break;
                case 'r':
                    mergeNodes(i + '-2', i + '-3', ++currentNodeNr, 'right twist');
                    break;
                case 't':
                    mergeNodes(i + '-0', i + '-1', ++currentNodeNr, 'left twist');
                    mergeNodes(i + '-2', i + '-3', ++currentNodeNr, 'right twist');
                    break;
                default:
                    break;
            }
        }

        // Cleanup of nodes that did not merge, join their links
        Object.keys(edgeStartMap).forEach(nodeId => {
            if (edgeStartMap[nodeId] && edgeEndMap[nodeId]) {
                const outEdge = edgeStartMap[nodeId];
                const inEdge = edgeEndMap[nodeId];

                // Merge the edges by connecting the start of inEdge to the end of outEdge
                outEdge.setAttribute("x1", inEdge.getAttribute("x1"));
                outEdge.setAttribute("y1", inEdge.getAttribute("y1"));
                inEdge.classList.forEach(className => {
                    outEdge.classList.add(className);
                });
                svgContainer.removeChild(inEdge);
                delete edgeStartMap[nodeId];
                delete edgeEndMap[nodeId];
                svgDoc.getElementById(nodeId).remove();
            }
        });

        svgContainer.querySelectorAll("line").forEach(line => {
            const path = document.createElementNS(svgNS, "path");
            path.setAttribute("class", line.getAttribute("class"));
            path.setAttribute("d", calculatePathDefinition(line));
            line.replaceWith(path);
        });
        return currentNodeNr;
    },

    newLegendStitch: function (stitchInputValue, colorCodeElement) {

        const threadSvg = this.newSVG(80, 120);
        this.newStitch(stitchInputValue, 0, 0, threadSvg, 80, 120);

        const colorCodeSvg = this.newSVG(27, 35);
        colorCodeElement.setAttribute("transform", "translate(13,17) scale(3)");
        colorCodeSvg.appendChild(colorCodeElement);

        const figcaption = document.createElement("figcaption");
        figcaption.append(colorCodeSvg, document.createTextNode(stitchInputValue.replace(/[^ctlr]/gi, '')));

        const figure = document.createElement("figure");
        figure.append(threadSvg, figcaption);
        this.addThreadClasses(threadSvg);

        document.body.appendChild(figure);
    },

    addThreadClasses: function (svgContainer) {
        const svgDoc = this.findSvgDoc(svgContainer);
        const threadStarts = [];
        const startAtClassToPath = {};
        svgContainer.querySelectorAll("path").forEach(path => {
            const startClass = Array.from(path.classList).filter(className => className.startsWith('starts_'));
            if (startClass.length === 0) {
                threadStarts.push(path);
            }
            path.classList.forEach(className => {
                if( className.startsWith('starts_'))
                    startAtClassToPath[className] = path;
            });
        });
        for (let threadNr = 0; threadNr < threadStarts.length; threadNr++) {
            let currentPath = threadStarts[threadNr];
            while (currentPath) {
                currentPath.classList.add('thread_' + threadNr);
                const endsAtClasses = Array.from(currentPath.classList).filter(className => className.startsWith('ends_'))
                if (endsAtClasses?.length === 0) {
                    break; // End of the thread
                }
                // assign top thread to node to allow painting threads by clicking a node
                const endsAtClass = endsAtClasses[0];
                const targetNode = svgDoc.getElementById(endsAtClass?.replace(/.*_/, ''));
                if (targetNode) {
                    // TODO numbers of nodes should not be unique per SVG but per page, use (++this.lastID)
                    if (endsAtClass.startsWith('ends_left') && targetNode.classList.contains("cross")) {
                        targetNode.classList.add('thread_' + threadNr);
                    } else if (endsAtClass.startsWith('ends_right') && targetNode.classList.contains("twist")) {
                        targetNode.classList.add('thread_' + threadNr);
                    }
                }
                // find next segment
                const nextStartsAtClass = endsAtClass
                    .replace(/ends_left/, 'starts_right')
                    .replace(/ends_right/, 'starts_left');
                currentPath = startAtClassToPath[nextStartsAtClass];
            }
        }
    },

    coyModifiedTemplateToDoc: function (template, svgInput) {
        // Calculate width and height from link elements
        const links = template.querySelectorAll(".link");
        const xVals = Array.from(links, el => +el.getAttribute("d").split(" ").pop().split(",")[0]);
        const yVals = Array.from(links, el => +el.getAttribute("d").split(",").pop());
        const w = Math.max(...xVals) * 3 + 12;
        const h = Math.max(...yVals) * 3 + 12;

        const svg = this.newSVG(w, h);
        template.setAttribute("transform", "scale(3)");
        template.setAttribute("id", "templatePairs");
        const twistMarkDefinitions = svgInput.querySelector("defs");
        svg.append(twistMarkDefinitions, template);

        svg.querySelectorAll('.link').forEach(linkElement => {
            // replace the style attribute to allow override with CSS
            const marker = linkElement.style.getPropertyValue('marker-mid');
            if (marker?.startsWith("url")) linkElement.setAttribute("marker-mid", marker);
            linkElement.removeAttribute('style');
            linkElement.classList.forEach(kissClass => {
                if (kissClass.startsWith("kiss_")) {
                    const oddEven = (kissClass.replace("kiss_", "") * 1) % 2 ? 'odd' : 'even';
                    linkElement.classList.add('kiss_' + oddEven);
                    const atClasses = Array.from(linkElement.classList).filter(cls => cls.includes("_at_"));
                    atClasses.forEach(atClass => {
                        const nodeId = atClass.replace(/.*_/, '');
                        svg.getElementById(nodeId)?.classList.add(kissClass);
                    })
                    const [source, target] = atClasses[0].startsWith("starts_")
                        ? [atClasses[0], atClasses[1]]
                        : [atClasses[1], atClasses[0]];
                    const sourceNodeId = source.replace(/.*_/, '');
                    const targetNodeId = target.replace(/.*_/, '');
                    svg.getElementById(targetNodeId)?.classList.add('from_' + sourceNodeId);
                    svg.getElementById(sourceNodeId)?.classList.add('to_' + targetNodeId);
                }
            });
        });

        document.body.appendChild(svg);
        return {w, h};
    },

    addCaptionedLegendElementsToDoc: function (svgInput) {
        // loop over pairs of tspan/g elements in #bdpqLegend (text and color code)
        const legend = svgInput.getElementById("bdpqLegend");
        const legendTexts = legend.querySelectorAll("tspan");
        const legendColorCodes = legend.querySelectorAll("g");
        const n = Math.min(legendTexts.length, legendColorCodes.length);
        for (let i = 0; i < n; i++) {
            this.newLegendStitch(legendTexts[i].textContent, legendColorCodes[i]);
        }
    },

    addThreadDiagramToDoc: function (templateElement, w, h) {

        function leftOrRight(startAtId, twistedPair) {
            const siblings = templateElement.querySelectorAll(".starts_at_" + startAtId)
            switch (siblings.length) {
                case 2:
                    const sibling = Array.from(siblings).filter(el => el !== twistedPair)[0]
                    const kissingPath = Array.from(twistedPair.classList)
                        .filter(cls => cls.startsWith("kiss_"))[0];
                    const kissingPathOfSibling = Array.from(sibling.classList)
                        .filter(cls => cls.startsWith("kiss_"))[0];
                    return kissingPath < kissingPathOfSibling ? 'l' : 'r';
                case 1:
                    return (twistedPair.classList.contains("kiss_0")) ? 'r' : 'l';
                default:
                    return 'l';
            }
        }

        function twistIndex() {
            const index = {};

            Array.from(templateElement.querySelectorAll("path"))
                .forEach(pair => {
                    const classes = Array.from(pair.classList);
                    const startsAtClass = classes.find(cls => cls.startsWith("starts_at_"));
                    const midMarker = pair.getAttribute("marker-mid");
                    if (startsAtClass && midMarker) { // mid-marker values are supposed to be: url("#twist-<n>")
                        const nrOfTwists = midMarker.replace(/[^0-9]*/g, '');
                        const startAtId = startsAtClass.replace("starts_at_", "");
                        const twist = leftOrRight(startAtId, pair);
                        if (index[startAtId]) // TODO combine l+r into t
                            index[startAtId] += twist.repeat(nrOfTwists);
                        else
                            index[startAtId] = twist.repeat(nrOfTwists);
                    }
                });
            return index;
        }

        const diagramGroup = document.createElementNS(this.svgNS, "g");
        diagramGroup.setAttribute("id", "templateThreads");
        const svg = this.newSVG((w * 6.5) + "", h * 3.5);
        svg.appendChild(diagramGroup);

        document.body.insertAdjacentHTML("beforeend", `
            <hr>
            <p class='note'>
            Under construction (added/moved stitches cause overlap, larger templates may have black threads): 
            </p>
        `);
        document.body.appendChild(svg);

        svg.insertAdjacentHTML("beforeend", `
            <use xlink:href="#templateThreads" id="clone_b" x="${w * 4}" transform="scale(0.7,0.7)"></use>
            <use xlink:href="#templateThreads" id="clone_d" x="${-w * 8.8}" y="0" transform="scale(-0.7,0.7)"></use>
            <use xlink:href="#templateThreads" id="clone_p" x="${w * 4}" y="${-h * 4.8}" transform="scale(0.7,-0.7)"></use>
            <use xlink:href="#templateThreads" id="clone_q" x="${-w * 8.8}" y="${-h * 4.8}" transform="scale(-0.7,-0.7)"></use>
        `);

        let lastThreadNodeNr = 0;
        const additionalTwists = twistIndex();

        const pairNodeIdToThreadGroup = {};
        const notProessed = new Set(templateElement.querySelectorAll("g"));
        const processed = [];
        let toProcess = Array.from(notProessed).filter(node => !Array.from(node.classList).some(cls => cls.startsWith('from_')));

        function findFringes(fromStitches, fringeType) {
            return fromStitches
                .flatMap(stitch => Array.from(stitch.querySelectorAll('path'))
                    .filter(path => !Array.from(path.classList).join("").includes(fringeType)))
                .reduce((acc, tailEdge) => {
                    const kissClass = Array.from(tailEdge.classList).find(cl => /^kiss_\d+$/.test(cl));
                    if (kissClass) acc[kissClass] = tailEdge;
                    return acc;
                }, {});
        }

        function combineStraightPaths(tailEdge, startEdge) {
            function parseDef(path) {
                return path.getAttribute('d')
                    .match(/M\s*(-?\d+(?:\.\d+)?)\s*(-?\d+(?:\.\d+)?)\s*L\s*(-?\d+(?:\.\d+)?)\s*(-?\d+(?:\.\d+)?)/);
            }

            // edges are in different groups with different translations
            const t1 = parseTranslate(tailEdge.closest('g'));
            const t2 = parseTranslate(startEdge.closest('g'));
            const dx = t1[0] * 1 - t2[0] * 1;
            const dy = t1[1] * 1 - t2[1] * 1;

            const [t, tx1, ty1] = parseDef(tailEdge) || [];
            const [s, , , sx2, sy2] = parseDef(startEdge) || [];
            if (!t || !s) return;

            // Start at tailEdge's start, end at startEdge's end
            startEdge.setAttribute('d', `M ${tx1 * 1 + dx} ${ty1 * 1 + dy} L ${sx2 * 1} ${sy2 * 1}`);
            Array.from(tailEdge.classList)
                .filter(cls => cls.includes("start"))
                .forEach(cls => {
                    startEdge.classList.add(cls);
                });
            tailEdge.remove();
        }

        function wip(templateNode) {
            if(!templateNode) return;
            const classes = templateNode.classList;
            let logMsg = "";
            const [cx,cy] =parseTranslate(templateNode);
            const svgDoc = GF_svgP2T.findSvgDoc(templateNode);
            let points = "M 0,0";//`${x},${y} `;
            for (const cls of classes) {
                if(cls.startsWith("to_") || cls.startsWith("from_")) {
                    const id = cls.replace(/.*_/, "");
                    const [x, y] = parseTranslate(svgDoc.getElementById(id));
                    points += ` L ${(x-cx)*0.4},${(y-cy)*0.4}`;
                    logMsg += ` $id=(${x},${y})`;
                }
            }
            const framePath = document.createElementNS(GF_svgP2T.svgNS, "path");
            framePath.setAttribute("d", points);
            framePath.classList.add("frame");
            templateNode.appendChild(framePath);
        }

        function parseTranslate(templateNode) {
            const [x,y] = templateNode.getAttribute("transform")
                ?.replace('translate(', '')
                .replace(')', '')
                .split(",");
            return [x*1,y*1];// return as numbers
        }
        for(const templateNode of notProessed) {
            wip(templateNode);
        }

        while (toProcess.length > 0) {
            for(const templateNode of toProcess) {
                // collect instructions from stitch and twists on pairs leaving the stitch
                const title = templateNode.querySelectorAll("title");
                let stitchInputValue = title.length === 0 ? 'ctc' : title[0].textContent;
                if (additionalTwists[templateNode.id])
                    stitchInputValue += additionalTwists[templateNode.id];

                // create group for the stitch at the position dictated bij the template node
                const stitchGroup = document.createElementNS(this.svgNS, "g");
                diagramGroup.appendChild(stitchGroup);
                const [x, y] = parseTranslate(templateNode);
                stitchGroup.setAttribute("transform", `translate(${x * 5.7}, ${y * 5.6})`);
                pairNodeIdToThreadGroup[templateNode.id] = stitchGroup;
                const pairNodeClasses = Array.from(templateNode.classList);

                const pairKissClasses = pairNodeClasses.filter(cl => cl.startsWith("kiss_"));
                const firstPairKissNr = pairKissClasses.sort()[0]?.replace(/.*_/, '');
                const firstThreadKissingPathNr = firstPairKissNr * 2 + (firstPairKissNr === '0' && pairKissClasses.length === 1 ? 0 : 2);
                stitchGroup.classList.add("first_kiss_"+firstThreadKissingPathNr);
                lastThreadNodeNr = this.newStitch(stitchInputValue, firstThreadKissingPathNr, lastThreadNodeNr, stitchGroup, 110, 75);

                if (processed.length > 0) {
                    const fromStitches = pairNodeClasses
                        .filter(cls => cls.startsWith("from_"))
                        .map(fromClass => pairNodeIdToThreadGroup[fromClass.replace("from_", "")])
                        .sort((a, b) => a.classList[0] - b.classList[0]); // one class on stitch groups: first_kiss_<nr>
                    const kissNrToTailEdge = findFringes(fromStitches, 'ends_');
                    const kissNrToStartEdge = findFringes([stitchGroup], 'starts_');
                    let startKissClasses = Object.keys(kissNrToStartEdge);
                    if (pairKissClasses.length === 1) {
                        // connect only the inner two pairs
                        if (pairKissClasses[0] === "kiss_0") {
                            startKissClasses = ["kiss_2", "kiss_3"];
                        } else {
                            startKissClasses = startKissClasses
                                .map(cls => parseInt(cls.replace('kiss_', ''), 10))
                                .sort((a, b) => a - b)
                                .slice(0, 2)
                                .map(n => `kiss_${n}`);
                        }
                    }
                    for (const kissNr of Array.from(startKissClasses)) {
                        const tailEdge = kissNrToTailEdge[kissNr];
                        if (tailEdge) {
                            const startEdge = kissNrToStartEdge[kissNr];
                            combineStraightPaths(tailEdge, startEdge);
                        }
                    }
                }
                // iteration
                processed.push(templateNode);
                notProessed.delete(templateNode);
            }
            toProcess = Array.from(notProessed).filter(node => {
                const fromClasses = Array.from(node.classList).filter(cls => cls.startsWith('from_'));
                return fromClasses.every(fromCls =>
                    processed.some(procNode => procNode.id === fromCls.replace('from_', ''))
                );
            });
        }
        this.addThreadClasses(svg);
    },

    processUploadedSvg: function (svgInput) {
        document.querySelectorAll("svg,figure,hr,.note").forEach(el => el.remove());
        document.body.insertAdjacentHTML("beforeend", "<hr>");
        this.lastID = 0;

        // the uploaded template element
        const template = svgInput.getElementById("cloned");
        const {w, h} = this.coyModifiedTemplateToDoc(template, svgInput);
        this.addCaptionedLegendElementsToDoc(svgInput);
        this.addThreadDiagramToDoc(template, w, h);
    },

    async readSVGFile(file) {
        if (!file) {
            alert("Failed to readd file" + file.name);
            return;
        }
        const svgContent = await file.text();
        const sanitized = DOMPurify.sanitize(svgContent, { USE_PROFILES: { svg: true, svgFilters: true } });
        return new DOMParser().parseFromString(sanitized, "image/svg+xml");
    },

    async init() {
        document.getElementById("upload").addEventListener("change", async event => {
            const file = event.target.files[0];
            this.processUploadedSvg(await this.readSVGFile(file));
        });

        try {
            const response = await fetch('demo.svg');
            const svgContent = await response.text();
            const file = new File([svgContent], "demo.svg", {type: "image/svg+xml"});
            this.processUploadedSvg(await this.readSVGFile(file));
        } catch (error) {
            console.error("Error loading the file:", error);
        }
    }
}