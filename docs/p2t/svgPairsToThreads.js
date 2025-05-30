const GF_svgP2T = {
    svgNS: "http://www.w3.org/2000/svg",

    newSVG(w,h) {
        const threadSvg = document.createElementNS(GF_svgP2T.svgNS, "svg");
        threadSvg.setAttribute("width", w);
        threadSvg.setAttribute("height", h);
        threadSvg.setAttribute("xmlns", GF_svgP2T.svgNS);
        return threadSvg;
    },

    newStitch(stitchInputValue, firstKissingPathNr, firstNodeNr, svgContainer) {
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
            line.setAttribute("stroke", "black");
            line.setAttribute("stroke-width", "4");
            svgContainer.appendChild(line);
            return line;
        }

        function drawCircle(x, y, id, description) {
            const circle = document.createElementNS(svgNS, "circle");
            circle.setAttribute("cx", x + '');
            circle.setAttribute("cy", y + '');
            circle.setAttribute("r", "7");
            circle.setAttribute("fill", "black");
            circle.setAttribute("opacity", "0.15");
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
            updateEdge(leftNodeId, edgeStartMap, "x1", "y1", "starts_left_at_").classList.add(isCross ? end : st);
            updateEdge(leftNodeId, edgeEndMap, "x2", "y2", "ends_left_at_").classList.add(isCross ? end : st);
            updateEdge(rightNodeId, edgeStartMap, "x1", "y1", "starts_right_at_").classList.add(isCross ? st : end);
            updateEdge(rightNodeId, edgeEndMap, "x2", "y2", "ends_right_at_").classList.add(isCross ? st : end);

            leftNode.remove();
            rightNode.remove();
        }

        function addToEdgeMap(nodeMap, pathMap, line, edgeMap) {
            const startNodeId = `${nodeMap}-${pathMap}`;
            if (!edgeMap[startNodeId]) edgeMap[startNodeId] = [];
            edgeMap[startNodeId] = line;
        }

        // Create 4 paths each with the number of subnodes needed by the stitch
        const kissingPathColors = ['red', 'blue', 'red', 'blue']; // for debugging purposes
        for (let pathNr = firstKissingPathNr; pathNr < 4 + firstKissingPathNr; pathNr++) {
            const x = (pathNr) * pathSpacing; // X-coordinate for the current path

            for (let nodeNr = 0; nodeNr < nrOfInitialPathNodes; nodeNr++) {
                const y = (nodeNr + 1) * nodeSpacing; // Y-coordinate for the current subnode
                drawCircle(x, y, nodeNr + '-' + pathNr);

                // Draw an edge to the node
                const line = drawLine(x, y);
                line.setAttribute("class", "kissing_path_" + pathNr);
                line.setAttribute("stroke", kissingPathColors[pathNr % 4]);
                if (nodeNr > 0) addToEdgeMap(nodeNr - 1, pathNr, line, edgeStartMap);
                addToEdgeMap(nodeNr, pathNr, line, edgeEndMap);
            }
            // Draw an edge out of the last node
            const line = drawLine(x, (nrOfInitialPathNodes + 1) * nodeSpacing);
            line.setAttribute("class", "kissing_path_" + pathNr);
            addToEdgeMap(nrOfInitialPathNodes - 1, pathNr, line, edgeStartMap);
            line.setAttribute("stroke", kissingPathColors[pathNr]);
        }

        // Make the paths kiss
        let currentNodeNr = firstNodeNr
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
                    break
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

        // Replace all line elements with path elements for short ends and bends
        svgContainer.querySelectorAll("line").forEach(line => {
            const path = document.createElementNS(svgNS, "path");
            path.setAttribute("stroke", line.getAttribute("stroke"));
            path.setAttribute("stroke-width", line.getAttribute("stroke-width"));
            path.setAttribute("opacity", line.getAttribute("opacity"));
            path.setAttribute("fill", "none");
            path.setAttribute("class", line.getAttribute("class"));

            const x1 = line.getAttribute("x1") * 1;
            const y1 = line.getAttribute("y1") * 1;
            const x2 = line.getAttribute("x2") * 1;
            const y2 = line.getAttribute("y2") * 1;

            // Calculate the direction vector of the line segment
            let dx = (parseFloat(x2) - parseFloat(x1));
            let dy = (parseFloat(y2) - parseFloat(y1));
            const length = Math.sqrt(dx * dx + dy * dy);
            dx = dx / length;
            dy = dy / length;

            // calculate the shortened ends
            const whiteStart = line.classList.contains("white_start");
            const whiteEnd = line.classList.contains("white_end");
            const gap = 8;
            const x1s = !whiteStart ? x1 : x1 + (dx * gap);
            const y1s = !whiteStart ? y1 : y1 + (dy * gap);
            const x2s = !whiteEnd ? x2 : x2 - (dx * gap);
            const y2s = !whiteEnd ? y2 : y2 - (dy * gap);
            path.setAttribute("d", `M ${x1s} ${y1s} L ${x2s} ${y2s}`);

            // Override d to bend the line if it is a repetition of the same type of stitch
            const classes = Array.from(line.classList).filter(className => className.includes('_at_'));
            if (classes.length > 1) {
                const id0 = classes[0].replace(/.*_/g, "");
                const id1 = classes[1].replace(/.*_/g, "");
                const n0 = svgContainer.getElementById(id0).classList[0]
                const n1 = svgContainer.getElementById(id1).classList[0]
                const startsLeft = classes[0].includes("starts_left");
                const startsRight = classes[0].includes("starts_right");
                // TODO Conditions seems to work. Which coincidence eliminates bends for twist with a cross in between?
                if (n0 === n1 && (startsLeft || startsRight)) {
                    // Calculate the midpoint of the line segment
                    const mx = (parseFloat(x1) + parseFloat(x2)) / 2;
                    const my = (parseFloat(y1) + parseFloat(y2)) / 2;

                    // Rotate the direction vector to get the perpendicular vector for the control point
                    const px = dy * 20; // Perpendicular x (scaled by 20)
                    const py = dx * 20;  // Perpendicular y (scaled by 20)

                    // note the signs after mx/my
                    const fraction = 0.4;
                    if (startsLeft) {
                        const x1s = !whiteStart ? x1 : x1 + (mx - px - x1) * fraction;
                        const y1s = !whiteStart ? y1 : y1 + (my + py - y1) * fraction;
                        const x2s = !whiteEnd ? x2 : x2 + (mx - px - x2) * fraction;
                        const y2s = !whiteEnd ? y2 : y2 + (my + py - y2) * fraction;
                        path.setAttribute("d", `M ${x1s} ${y1s} Q ${(mx - px)} ${(my + py)} ${x2s} ${y2s}`);
                    } else {
                        const x1s = !whiteStart ? x1 : x1 + (mx + px - x1) * fraction;
                        const y1s = !whiteStart ? y1 : y1 + (my - py - y1) * fraction;
                        const x2s = !whiteEnd ? x2 : x2 + (mx + px - x2) * fraction;
                        const y2s = !whiteEnd ? y2 : y2 + (my - py - y2) * fraction;
                        path.setAttribute("d", `M ${x1s} ${y1s} Q ${(mx + px)} ${(my - py)} ${x2s} ${y2s}`);
                    }
                }
            }

            line.replaceWith(path);
        });
        GF_svgP2T.addThreadClasses(svgContainer, svgNS);
        return currentNodeNr;
    },

    newLegendStitch(stitchInputValue, colorCodeElement) {

        const threadSvg = GF_svgP2T.newSVG("80","120");
        const colorCodeSvg =GF_svgP2T.newSVG("27","35");
        colorCodeSvg.appendChild(colorCodeElement)
        colorCodeElement.setAttribute("transform", "translate(13,17) scale(3)")
        document.body.insertAdjacentHTML("beforeend", `<span>${stitchInputValue.replace(/[^ctlr]/gi, '')}: </span>`);
        document.body.appendChild(colorCodeSvg);
        document.body.appendChild(threadSvg);
        // hint: add a temporary invisible box (fill and stroke "none") when passing in an empty group
        GF_svgP2T.newStitch(stitchInputValue, 0, 0, threadSvg);
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
            })
        })
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
                    path.classList.add("thread_" + path.classList[0].replace(/.*_/,''));
                }
           });
    },

    appendUploadedSvg(svgInput) {

        const twistMarkDefs = svgInput.querySelector("defs");
        const templateElement = svgInput.getElementById("cloned");
        const linkElements = Array.from(templateElement.querySelectorAll(".link"));
        const yValues = linkElements.map(element => element.getAttribute("d").replace(/.*,/g, '') * 1);
        const xValues = linkElements.map(element => element.getAttribute("d").replace(/.* /g, '').replace(/,.*/g, '') * 1);
        const legendElement = svgInput.getElementById("bdpqLegend");
        const legendTextEntries = legendElement.querySelectorAll("tspan");
        const legendColorCodeEntries = legendElement.querySelectorAll("g");
        const w = Math.max(...xValues) * 3 + 12 + '';
        const h = Math.max(...yValues) * 3 + 12 + '';
        const svg = GF_svgP2T.newSVG(w, h);
        templateElement.setAttribute("transform", "scale(3)");
        svg.appendChild(twistMarkDefs);
        svg.appendChild(templateElement);
        svg.querySelectorAll('.link').forEach(element => {
            element.setAttribute("stroke-width", "2");
            element.setAttribute("stroke", "#bbbbbb");
            const marker = element.getAttribute('style').replace(/.*marker-mid: */, "").replace(/;.*/, '');
            if (marker.startsWith("url")) element.setAttribute("marker-mid", marker);
            element.removeAttribute('style');
        });
        document.body.appendChild(svg);
        const nrOfLegendEntries = Math.min(legendTextEntries.length, legendColorCodeEntries.length);
        for (let i = 0; i < nrOfLegendEntries; i++) {
            GF_svgP2T.newLegendStitch(legendTextEntries[i].textContent, legendColorCodeEntries[i]);
        }
        GF_svgP2T.createDiagram(templateElement,w,h);
    },

    createDiagram(templateElement, w, h) {
        document.body.insertAdjacentHTML("beforeend", "<hr><p>Under construction (pinched/moved stitches cause overlap, a thread may have multiple colors): </p>");

        function leftOrRight(startAtId, twistedPair) {
            const siblings = templateElement.querySelectorAll(".starts_at_" + startAtId)
            switch (siblings.length) {
                case 2:
                    const sibling = Array.from(siblings).filter(el => el !==twistedPair)[0]
                    const kissingPath = Array.from(twistedPair.classList)
                        .filter(cls => cls.startsWith("kiss_"))[0];
                    const kissingPathOfSibling = Array.from(sibling.classList)
                        .filter(cls => cls.startsWith("kiss_"))[0];
                    return  kissingPath < kissingPathOfSibling ? 'l' : 'r';
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
                    if (startsAtClass && midMarker) {
                        const nrOfTwists = midMarker.replace(/.*-/,"" ).replace(/[^0-9]*$/, '');
                        const startAtId = startsAtClass.replace("starts_at_","");
                        const twist = leftOrRight(startAtId, pair);
                        if (index[startAtId]) // TODO combine l+r into t
                            index[startAtId] += twist.repeat(nrOfTwists);
                        else
                            index[startAtId] = twist.repeat(nrOfTwists);

                    }
                 });
            return index;
        }

        const additionalTwists = twistIndex();

        const svg = GF_svgP2T.newSVG((w*6.5)+"", h*3.5);
        document.body.appendChild(svg);
        const bigG = document.createElementNS(GF_svgP2T.svgNS, "g");
        bigG.setAttribute("id", "original");
        svg.appendChild(bigG);
        let kissingPathNr = 0;
        let nodeNr = 0;
        templateElement.querySelectorAll("g").forEach(element => {
            const [x, y] = element.getAttribute("transform")
                .match(/translate\(([^)]+)\)/)[1]
                .split(",");
            const titles = element.querySelectorAll("title");
            let stitchInputValue = 'ctc';
            if (titles.length !== 0) {
                stitchInputValue = titles[0].textContent;
                // otherwise a stitch has been added without explicitly assigning actions
            }
            if(additionalTwists[element.id])
                stitchInputValue += additionalTwists[element.id];
            const tmpSVG = GF_svgP2T.newSVG(125, 80);
            const g = document.createElementNS(GF_svgP2T.svgNS, "g");
            const nrOfNodes = GF_svgP2T.newStitch(stitchInputValue, 0, 0, tmpSVG);
            nodeNr += nrOfNodes;
            kissingPathNr += 4;
            g.setAttribute("transform", `translate(${ x*5.7 }, ${ y*5.6 })`);
            tmpSVG.childNodes.forEach(child => {
                g.appendChild(child.cloneNode(true));
            });
            bigG.appendChild(g);
        })
        svg.insertAdjacentHTML("beforeend", `<use xlink:href="#original" id="clone_b" x="${w*4}" transform="scale(0.7,0.7)"></use>`);
        svg.insertAdjacentHTML("beforeend", `<use xlink:href="#original" id="clone_d" x="${-w*8.8}" y="0" transform="scale(-0.7,0.7)"></use>`);
        svg.insertAdjacentHTML("beforeend", `<use xlink:href="#original" id="clone_p" x="${w*4}" y="${-h*4.8}" transform="scale(0.7,-0.7)"></use>`);
        svg.insertAdjacentHTML("beforeend", `<use xlink:href="#original" id="clone_q" x="${-w*8.8}" y="${-h*4.8}" transform="scale(-0.7,-0.7)"></use>`);
        // TODO connect threads (at least by color), enable download, squeeze pinched stitches
        //  see also https://d-bl.github.io/GroundForge-help/symmetry/#file-structure
    },

    loadSVGFile(event) {
        // Retrieve the first (and only!) File from the FileList object
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                // cleanup of previous upload TODO more specific selectors for integration on symmetry page
                document.querySelectorAll("svg,span")
                    .forEach(svg => svg.remove())
                const svgContent = DOMPurify.sanitize(
                    e.target.result,
                    {USE_PROFILES: {svg: true, svgFilters: true}}
                );
                const parsedSvg = new DOMParser().parseFromString(svgContent, "image/svg+xml");
                GF_svgP2T.appendUploadedSvg(parsedSvg);
            };
            reader.readAsText(file);
        } else {
            alert("Failed to load file");
        }
    },
    init(){
        document.getElementById("upload").addEventListener("change", GF_svgP2T.loadSVGFile);
        // default file upload for quick testing purposes
        fetch('demo.svg')
            .then(response => response.text())
            .then(svgContent => {
                const file = new File([svgContent], "demo.svg", { type: "image/svg+xml" });

                const dataTransfer = new DataTransfer();
                dataTransfer.items.add(file);

                const event = new Event("change");
                Object.defineProperty(event, "target", {
                    value: { files: dataTransfer.files },
                    writable: false
                });

                GF_svgP2T.loadSVGFile(event);
            })
            .catch(error => console.error("Error loading the file:", error));
    }
}
