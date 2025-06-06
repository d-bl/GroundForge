const GF_svgP2T = {
    svgNS: "http://www.w3.org/2000/svg",
    gap: 8,

    newSVG(w, h) {
        const svg = document.createElementNS(this.svgNS, "svg");
        svg.setAttribute("width", w);
        svg.setAttribute("height", h);
        svg.setAttribute("xmlns", this.svgNS);

        // show full width of lines on the edge of the viewpBox
        svg.setAttribute("viewBox", "-4 -4 " + (w + 8) + " " + (h + 8));
        return svg;
    },

    newStitch: function (stitchInputValue, firstKissingPathNr, firstNodeNr, svgContainer) {
        let stitch = stitchInputValue
            .toLowerCase()
            .replace(/[^clrt]/g, '');

        const svgNS = svgContainer.namespaceURI;
        const containerHeight = svgContainer.getAttribute('height');
        const containerWidth = svgContainer.getAttribute('width');
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
                const n0 = svgContainer.getElementById(id0).classList[0];
                const n1 = svgContainer.getElementById(id1).classList[0];
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
            const leftNode = svgContainer.getElementById(leftNodeId);
            const rightNode = svgContainer.getElementById(rightNodeId);

            if (!leftNode || !rightNode) {
                console.error("One or both nodes not found");
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
        for (let pathNr = firstKissingPathNr; pathNr < 4 + firstKissingPathNr; pathNr++) {
            const x = (pathNr) * pathSpacing; // X-coordinate for the current path

            for (let nodeNr = 0; nodeNr < nrOfInitialPathNodes; nodeNr++) {
                const y = (nodeNr + 1) * nodeSpacing; // Y-coordinate for the current subnode
                drawCircle(x, y, nodeNr + '-' + pathNr);

                // Draw an edge to the node
                const line = drawLine(x, y);
                line.setAttribute("class", "kiss_" + pathNr);
                line.classList.add("kiss_" + (pathNr%2 ? 'odd' : 'even'));
                if (nodeNr > 0) addToEdgeMap(nodeNr - 1, pathNr, line, edgeStartMap);
                addToEdgeMap(nodeNr, pathNr, line, edgeEndMap);
            }
            // Draw an edge out of the last node
            const line = drawLine(x, (nrOfInitialPathNodes + 1) * nodeSpacing);
            line.setAttribute("class", "kiss_" + pathNr);
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
                svgContainer.getElementById(nodeId).remove();
            }
        });

        svgContainer.querySelectorAll("line").forEach(line => {
            const path = document.createElementNS(svgNS, "path");
            path.setAttribute("class", line.getAttribute("class"));
            path.setAttribute("d", calculatePathDefinition(line));
            line.replaceWith(path);
        });
        this.addThreadClasses(svgContainer, svgNS);
        return currentNodeNr;
    },

    newLegendStitch(stitchInputValue, colorCodeElement) {

        const threadSvg = this.newSVG(80, 120);
        this.newStitch(stitchInputValue, 0, 0, threadSvg);

        const colorCodeSvg = this.newSVG(27, 35);
        colorCodeElement.setAttribute("transform", "translate(13,17) scale(3)");
        colorCodeSvg.appendChild(colorCodeElement);

        const figcaption = document.createElement("figcaption");
        figcaption.append(colorCodeSvg, document.createTextNode(stitchInputValue.replace(/[^ctlr]/gi, '')));

        const figure = document.createElement("figure");
        figure.append(threadSvg, figcaption);

        document.body.appendChild(figure);
    },

    addThreadClasses(svg) {
        const threadStarts = {};
        const classToPath = {};
        svg.querySelectorAll("path").forEach(path => {
            const classes = Array.from(path.classList).filter(className => className.includes('_at_'));
            if (classes.length === 1 && classes[0].startsWith("ends_")) {
                threadStarts[classes[0]] = path;
            }
            classes.forEach(className => {
                classToPath[className] = path;
            });
        });
        const threadStartKeys = Object.keys(threadStarts);
        const nrOfThreads = threadStartKeys.length;
        for (let threadNr = 0; threadNr < nrOfThreads; threadNr++) {
            let currentClass = threadStartKeys[threadNr];
            let currentPath = threadStarts[currentClass];

            while (true) {
                currentPath.classList.add('thread_' + threadNr);
                // find next edge
                if (currentClass == null) {
                    break; // End of the thread
                }
                switch (currentClass.replace(/_at_.*/, "")) {
                    case "ends_left":
                        currentPath = classToPath[currentClass.replace("ends_left", "starts_right")];
                        break;
                    case "ends_right":
                        currentPath = classToPath[currentClass.replace("ends_right", "starts_left")];
                        break;
                    default:
                        currentPath = null;
                }
                if (currentPath == null) {
                    break; // End of the thread
                }
                currentClass = Array.from(currentPath.classList).find(className => className.startsWith('ends_'));
            }
        }
        // threads not twisted at all
        Array.from(svg.querySelectorAll("path"))
            .forEach(path => {
                if (!Array.from(path.classList).join('').includes("thread")) {
                    // use kissing_path number
                    path.classList.add("thread_" + path.classList[0].replace(/.*_/, ''));
                }
            });
    },

    appendUploadedSvg(svgInput) {
        document.querySelectorAll("svg,figure,hr,.note").forEach(el => el.remove());
        document.body.insertAdjacentHTML("beforeend", "<hr>");

        // the uploaded template element
        const template = svgInput.getElementById("cloned");

        // Calculate width and height from link elements
        const links = template.querySelectorAll(".link");
        const xVals = Array.from(links, el => +el.getAttribute("d").split(" ").pop().split(",")[0]);
        const yVals = Array.from(links, el => +el.getAttribute("d").split(",").pop());
        const w = Math.max(...xVals) * 3 + 12;
        const h = Math.max(...yVals) * 3 + 12;

        const svg = this.newSVG(w, h);
        template.setAttribute("transform", "scale(3)");
        const twistMarkDefinitions = svgInput.querySelector("defs");
        svg.append(twistMarkDefinitions, template);

        svg.querySelectorAll('.link').forEach(linkElement => {
            // replace the style attribute with presentation attribute (allows override with CSS)
            const marker = linkElement.style.getPropertyValue('marker-mid');
            if (marker?.startsWith("url")) linkElement.setAttribute("marker-mid", marker);
            linkElement.removeAttribute('style');
            linkElement.classList.forEach(c => {
                if (c.startsWith("kiss_")) {
                    linkElement.classList.add('kiss_' + ((c.replace("kiss_", "")*1)%2? 'odd' : 'even'));
                }
            })
        });

        document.body.appendChild(svg);

        // loop over pairs of tspan/g elements in #bdpqLegend (text and color code)
        const legend = svgInput.getElementById("bdpqLegend");
        const legendTexts = legend.querySelectorAll("tspan");
        const legendColors = legend.querySelectorAll("g");
        const n = Math.min(legendTexts.length, legendColors.length);
        for (let i = 0; i < n; i++) {
            this.newLegendStitch(legendTexts[i].textContent, legendColors[i]);
        }

        this.createDiagram(template, w, h);
    },

    createDiagram(templateElement, w, h) {

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
        diagramGroup.setAttribute("id", "original");
        let kissingPathNr = 0;
        let nodeNr = 0;
        const additionalTwists = twistIndex();

        templateElement.querySelectorAll("g").forEach(templateNode => {
            const [x, y] = templateNode.getAttribute("transform")
                .match(/translate\(([^)]+)\)/)[1]
                .split(",");
            const titles = templateNode.querySelectorAll("title");
            let stitchInputValue = titles.length === 0 ? 'ctc' : titles[0].textContent;
            if (additionalTwists[templateNode.id])
                stitchInputValue += additionalTwists[templateNode.id];

            // detour because getElementById (used in newStitch) does not exist for group elements
            // and querySelector("#\\0-1") did also not work
            // TODO adjust IDs into n_0_1 in uploadeded template
            const tmpSVG = this.newSVG(125, 80);

            const stitchGroup = document.createElementNS(this.svgNS, "g");
            const nrOfNodes = this.newStitch(stitchInputValue, 0, 0, tmpSVG);
            nodeNr += nrOfNodes;
            kissingPathNr += 4;
            stitchGroup.setAttribute("transform", `translate(${x * 5.7}, ${y * 5.6})`);
            tmpSVG.childNodes.forEach(child => {
                stitchGroup.appendChild(child.cloneNode(true));
            });
            diagramGroup.appendChild(stitchGroup);
        });

        const svg = this.newSVG((w * 6.5) + "", h * 3.5);
        svg.appendChild(diagramGroup);
        svg.insertAdjacentHTML("beforeend", `
            <use xlink:href="#original" id="clone_b" x="${w * 4}" transform="scale(0.7,0.7)"></use>
            <use xlink:href="#original" id="clone_d" x="${-w * 8.8}" y="0" transform="scale(-0.7,0.7)"></use>
            <use xlink:href="#original" id="clone_p" x="${w * 4}" y="${-h * 4.8}" transform="scale(0.7,-0.7)"></use>
            <use xlink:href="#original" id="clone_q" x="${-w * 8.8}" y="${-h * 4.8}" transform="scale(-0.7,-0.7)"></use>
        `);

        document.body.insertAdjacentHTML("beforeend", `
            <hr>
            <p class='note'>
            Under construction (added/moved stitches cause overlap, a thread may have multiple colors): 
            </p>
        `);
        document.body.appendChild(svg);
        // TODO connect threads (at least by color), enable download, squeeze pinched stitches
        //  see also https://d-bl.github.io/GroundForge-help/symmetry/#file-structure
    },

    async loadSVGFile(file) {
        if (!file) {
            alert("Failed to load file");
            return;
        }
        const svgContent = await file.text();
        const sanitized = DOMPurify.sanitize(svgContent, { USE_PROFILES: { svg: true, svgFilters: true } });
        return new DOMParser().parseFromString(sanitized, "image/svg+xml");
    },

    async init() {
        document.getElementById("upload").addEventListener("change", async event => {
            const file = event.target.files[0];
            this.appendUploadedSvg(await this.loadSVGFile(file));
        });

        try {
            const response = await fetch('demo.svg');
            const svgContent = await response.text();
            const file = new File([svgContent], "demo.svg", {type: "image/svg+xml"});
            this.appendUploadedSvg(await this.loadSVGFile(file));
        } catch (error) {
            console.error("Error loading the file:", error);
        }
    }
}