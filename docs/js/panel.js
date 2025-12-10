const GF_panel = {
    svgSize: [ // portrait pages, size increases per droste step
        {width: 793.70079, height: 1122.5197}, // A4
        {width: 1122.5197, height: 1587.4016}, // A3
        {width: 1587.4016, height: 2245.3094} // A2
    ],
    panelSize: {width: '300px', height: '300px'}, // default panel size

    load(config) {
        const { caption, id, controls = [], size = this.panelSize } = config;
        const isArray = Array.isArray(controls);
        const sizeStr = JSON.stringify(size)
            .replace(/\n/g, "")
            .replace(/"/g, "'");

        const cleanup = isArray && controls.includes('cleanup') ? `
            <a href="javascript:GF_panel.cleanupStitches('${id}')" title="clean up"><img src="/GroundForge/images/broom.png" alt="broom"></a>
        ` : '';
        const diagram = isArray && controls.includes('diagram') ? `
            <a href="javascript:return false"  title="reload"><img src="/GroundForge/images/wand.png" alt="wand"></a>
            <a href="javascript:GF_panel.nudge('${id}')" title="resume animation"><img src="/GroundForge/images/play.png" alt="resume"></a>
            <a href="javascript:GF_panel.downloadSVG('${id}')" title="download"><img src="/GroundForge/images/download.jpg" alt="download"></a>
        ` : '';
        const colorChooser = isArray && controls.includes('color') ? `
            <input type="color" id="${id}ColorChooser" name="threadColor" value="#ff0000">
        ` : '';
        const hasColorChooser = isArray && controls.includes('color')
            ? "class='hasColorChooser'" : '';
        const resize = isArray && controls.includes('resize') ? `
            <a href="javascript:GF_panel.maximize('${id}')" title="maximize"><img src="/GroundForge/images/maximize.png" alt="maximize"></a>
            <a href="javascript:GF_panel.resetDimensions('${id}',${sizeStr})" title="reset to default"><img src="/GroundForge/images/reset-dimensions.png" alt="default"></a>
            <a href="javascript:GF_panel.minimize('${id}')" title="minimize"><img src="/GroundForge/images/minimize.png" alt="minimize"></a>
        ` : '';
        const figure = document.createElement('figure');
        figure.className = 'gf_panel';
        figure.innerHTML = `
            <figcaption class="gf_panel">
                ${caption.trim()}
                ${cleanup.trim()}
                ${diagram.trim()}
                ${colorChooser.trim()}
                ${resize.trim()}
            </figcaption>
            <div id="${id}" ${hasColorChooser}></div>
        `.trim();
        document.currentScript.parentNode.append(figure);
        this.resetDimensions(id, size);
    },
    downloadSVG(containerId, filename = "diagram.svg") {
        const el = document.querySelector('#'+ containerId + " svg");
        if (!el) return;

        const svgString = new XMLSerializer().serializeToString(el);
        const blob = new Blob([svgString], {type: "image/svg+xml"});
        const url = URL.createObjectURL(blob);

        const a = document.createElement("a");
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        setTimeout(() => {
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        }, 1000); // 1 second delay
    },
    maximize(containerId) {
        const el = document.getElementById(containerId);
        if (el) {
            el.style.width = "100%";
            el.style.height = "90vh";
        }
        return false;
    },
    minimize(containerId) {
        const el = document.getElementById(containerId);
        if (el) {
            el.style.width = "200px";
            el.style.height = "0px";
        }
        return false;
    },
    nudge(containerId) {
        nudgeDiagram(d3.select('#' + containerId).select("svg"));
    },
    resetDimensions(containerId, size = this.panelSize) {
        const el = document.getElementById(containerId);
        const {width, height} = {
            ...this.panelSize,
            ...(typeof size === 'object' && size !== null ? size : this.panelSize)
        };
        if (el) {
            el.style.width = width;
            el.style.height = height;
        }
        return false;
    },
    diagramSVG(namedArgs) {
        console.time('diagramSVG');
        const { id, type='pair', steps: steps = [], query, size = this.svgSize } = namedArgs;
        const {width, height} = {
            ...this.svgSize[steps.length < this.svgSize.length ? steps.length : this.svgSize.length],
            ...(typeof size === 'object' && size !== null ? size : {})
        };
        const config = TilesConfig(query)
        const isPrimaryPairDiagrem = type === 'pair' && steps.length === 0;
        let pairDiagram = NewPairDiagram.create(config);
        let threadDiagram = isPrimaryPairDiagrem ? null : ThreadDiagram.create(pairDiagram);
        for (let i = 0; i < steps.length; i++) {
            console.time("step "+i)
            pairDiagram = PairDiagram.create(steps[i], threadDiagram);
            if(type==='thread' || i+1 < steps.length) {
                threadDiagram = ThreadDiagram.create(pairDiagram)
            }
            console.timeEnd("step "+i)
            // Preserving the intermediate results between calls to diagramSVG may not be worth the bookkeeping trouble.
            // Sampling shows that execution time grows exponentially with the number of steps.
            // Nudging is more linear if not constant. The second step may take as much time as nudging.
        }
        const nodeTransparency = 0.05
        const markers = true // use false for slow devices and IE-11, set them at onEnd
        let svg;
        if (namedArgs.type === 'pair') {
            svg = DiagramSvg.render(pairDiagram, "1px", markers, width, height, nodeTransparency)
        } else {
            svg = DiagramSvg.render(threadDiagram, "2px", markers, width, height, nodeTransparency);
        }
        // TODO extract method of the part above?
        if (!id) return svg;
        const container = document.getElementById(id);
        container.innerHTML = svg;
        if (type==='thread' && container.classList.contains("hasColorChooser")) {
            document.querySelectorAll(`#${id} .node`).forEach(el => {
                el.addEventListener('click', function(event) {
                    GF_panel.clickedNode(event, id + 'ColorChooser');
                });
            });
            document.querySelectorAll(`#${id} .threadStart, #${id} .bobbin`).forEach(el => {
                el.addEventListener('click', function(event) {
                    GF_panel.clickedThread(event, id + 'ColorChooser');
                });
            });
        }
        console.timeEnd('diagramSVG');
        if (!isPrimaryPairDiagrem) {
            this.nudge(id);
        }
    },
    clickedThread(event, colorId) {
        const selectedClass = event.currentTarget.textContent.replace(" ", "");
        const color = document.getElementById(colorId).value;
        document.querySelectorAll('.' + selectedClass).forEach(el => {
            el.style.stroke = color;
            el.style.fill = color;
            el.style.opacity = "1";
        });
    },

    clickedNode(event, colorId) {
        const selectedClass = event.currentTarget.classList.toString().replace(/ *node */,'');
        if (selectedClass === "threadStart") return;
        const color = document.getElementById(colorId).value;
        document.querySelectorAll("." + selectedClass).forEach(el => {
            el.style.stroke = color;
            el.style.fill = color;
            el.style.opacity = "0.4";
        });
    },
    cleanupStitches(id) {
        const textValue = document.getElementById(id).value
        let id2instruction = {}
        let inputLines = textValue.split(/[\n,]/ );
        inputLines.forEach(function (line) {
            if(line.includes('=')) {
                let stitchValue = line.replace(/.*=/, "")
                let stitchIds = line.replace(/=[^=]+$/, "").split(/=/)
                stitchIds.forEach(function (stitchId) {
                    id2instruction[stitchId] = stitchValue
                })
            }
        })
        let invertedMap = {};

        for (const [key, value] of Object.entries(id2instruction)) {
            if (!invertedMap[value]) {
                invertedMap[value] = [];
            }
            invertedMap[value].push(key);
        }

        let outputLines= inputLines.filter(line => /^((twist=)|(cross=))?[^=]+$/.test(line));
        const keys = Object.keys(invertedMap).sort();
        for (const key of keys) {
            const values = invertedMap[key].sort();
            outputLines.push(values.join('=') + '=' + key)
        }
        document.getElementById(id).value = outputLines.join('\n')
        setLinks(2)
    }
}
