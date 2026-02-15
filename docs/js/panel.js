/**
 * GroundForge component generator.
 *
 * Requires: d3.v4.min.js - GroundForge-opt.js - nudgePairs.js (nudgeDiagram)
 * @namespace
 */
const GF_panel = {
    svgSize: [ // portrait pages, size increases per droste step
        {width: 793.70079, height: 1122.5197}, // A4
        {width: 1122.5197, height: 1587.4016}, // A3
        {width: 1587.4016, height: 2245.3094} // A2
    ],
    panelSize: {width: '300px', height: '300px'}, // default panel size

    /**
     * Creates the panel structure with a configurable set of controls in the caption.
     *
     *     <figure class="gf-panel"><figcaption>...</figcaption><div id="..."></div></figure>
     *
     * @param {Object} config - Configuration for the panel.
     * @param {string} config.caption - Plain text or HTML string generated at the start of the `figcaption`.
     * @param {string} config.id - The unique ID for the `div` element.
     * @param {string} [config.wandHref] - Optional URL for the reload (wand) icon, typically: `javascript:...`.
     *   Creates the controls _wand_ and _href_.
     * @param {Array<string>} [config.controls] - List of control names to include.
     *   Recognized values:
     *   - 'cleanup': Adds a cleanup (broom) button to clean up content.
     *        Intended for the specification panel on a droste page.
     *   - 'color': Adds a color chooser.
     *        looks and dialog may vary per browser.
     *        Its value is used to highlight threads or stitches in thread diagrams.
     *   - 'resize': Adds maximize, minimize, and reset size controls.
     * @param {Object} [config.size] - Optional size for the panel (e.g., `{width: '300px', height: '300px'}`).
     * @param {HTMLElement} [config.parent] - The parent element to which the panel will be appended.
     *   Default: `document.body`.
     */
    load(config) {
        const { caption, id, wandHref, controls = [], size = this.panelSize, parent } = config;
        const isArray = Array.isArray(controls);
        const sizeStr = JSON.stringify(size)
            .replace(/\n/g, "")
            .replace(/"/g, "'");

        const cleanup = isArray && controls.includes('cleanup') ? `
            <a href="javascript:GF_panel.cleanupStitches('${id}')" title="clean up"><img src="/GroundForge/images/broom.png" alt="broom"></a>
        ` : '';
        const hasColorChooser = isArray && controls.includes('color');
        const colorChooser = hasColorChooser ? `
            <input type="color" id="${id}ColorChooser" name="threadColor" value="#ff0000">
        ` : '';
        const type = hasColorChooser ? "thread" : "pair";
        const diagram = wandHref ? `
            <a href="${wandHref}"  title="reload"><img src="/GroundForge/images/wand.png" alt="wand"></a>
            <a href="javascript:GF_panel.nudge('${id}','${type}')" title="resume animation"><img src="/GroundForge/images/play.png" alt="resume"></a>
            <a href="javascript:GF_panel.downloadSVG('${id}')" title="download"><img src="/GroundForge/images/download.jpg" alt="download"></a>
        ` : '';
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
            <div id="${id}"></div>
        `.trim();
        parent.append(figure);
        this.resetDimensions(id, size);
    },

    /**
     * Downloads the SVG content of the specified container as a file.
     * @param containerId
     * @param filename
     */
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
    /**
     * Spreads nodes in a pair diagram to reduce overlaps.
     *
     * Called by default for all diagrams except for a primary pair diagram (no `steps`),
     * Also called by resume control which is also available for the primary pair diagram.
     *
     * Uses d3-force to apply forces to the nodes and links of a diagram.
     * Requires d3.js and DiagramSvg.linkPath function of GroundForge-opt.js. See also
     * - https://devdocs.io/d3~4/d3-force
     * - https://devdocs.io/d3~4/d3-selection
     *
     * Wrapper for nudgeDiagram to hide D3js for the caller.
     *
     * @param {string} containerId Id of an element containing an SVG with the following requirements.
     *   - Elements with class `node` and an `id` attribute
     *     - Must have attribute `transform="translate(x,y)"`
     *   - Elements with class `link` and an `id` attribute containing node IDs separated by `-`
     *     - Must have attribute `d` defining a path
     * @param {string} [type] Defaults to 'pair'. Other option is 'thread'.
     */
    nudge(containerId, type = 'thread') {
        console.log(`nudge called for container ${containerId} and diagram type ${type}`);
        nudgeDiagram(d3.select('#' + containerId).select("svg"), type);
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
    scrollIfTooLittleIsVisible(element) {
        const rect = element.getBoundingClientRect();
        const vh = window.innerHeight || document.documentElement.clientHeight;
        const visibleHeight = Math.max(0, Math.min(rect.bottom, vh) - Math.max(rect.top, 0));
        if (visibleHeight / rect.height < 0.3) {
            element.scrollIntoView({behavior: 'smooth', block: 'center'});
        }
    },
    /**
     * Generates an SVG diagram.
     *
     * @param {Object} namedArgs
     * @param {string} [namedArgs.id] - Optional ID of the container element to render the SVG into.
     * @param {string} [namedArgs.type='pair'] - Type of diagram: 'pair' or 'thread'.
     * @param {Array<string>} [namedArgs.steps=[]] - droste stitch definitions:
     *   for each element a pair diagram is created from the (previous) thread diagram.
     * @param {string} namedArgs.query - URL arguments.
     *   Note that the arguments _droste2_ and _droste3_ are the equivalent of _steps[0]_ and _steps[1]_ respectively.
     *   This method uses the values of the _steps_ option.
     *   The pages of GroundForge use the query parameters to pass a pattern
     *   back and forth between _pattern_, _stitches_ and _droste_.
     *   The query keys were numbered with 2nd and 3rd pair diagram in mind.
     * @param {Object} [namedArgs.size] - Size configuration for the SVG.
     *   Default (A4 similar to US letter, A3 similar to Tabloid or Ledger, A2) depends on the number of steps.
     * @returns {HTMLElement} SVG unless `id` is provided.
     *   To be used outside a panel context, no nudging is applied.
     */
    diagramSVG(namedArgs) {
        console.time('diagramSVG');
        const {
            id,
            type='pair',
            steps: steps = [],
            query,
            size = this.svgSize[2]
        } = namedArgs;
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
        const isPairDiagram = namedArgs.type === 'pair';
        let svg;
        if (isPairDiagram) {
            svg = DiagramSvg.render(pairDiagram, "1.7px", markers, width, height, nodeTransparency)
        } else {
            svg = DiagramSvg.render(threadDiagram, "4px", markers, width, height, nodeTransparency);
        }
        // TODO extract method of the part above?
        if (!id) return svg;
        const container = document.getElementById(id);
        container.innerHTML = svg;
        const svgElement = container.querySelectorAll("svg>g")[0];
        svgElement.setAttribute("transform", isPairDiagram ? "scale(1.3)" : "scale(0.5)");

        if (type==='pair') {
            document.querySelectorAll(`#${id} .node`).forEach(el => {
                el.removeAttribute('onclick')
            })
        } else {
            document.querySelectorAll(`#${id} .node`).forEach(el => {
                el.removeAttribute('onclick');
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
            this.nudge(id, type);
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
