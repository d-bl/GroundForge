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
            <a href="javascript:cleanUp(d3.select('#${id} svg'))" title="clean up"><img src="/GroundForge/images/broom.png" alt="broom stick"></a>
        ` : '';
        const diagram = isArray && controls.includes('diagram') ? `
            <a href="javascript:reload()"  title="reload"><img src="/GroundForge/images/wand.png" alt="wand"></a>
            <a href="javascript:nudgeDiagram(d3.select('#${id} svg'))" title="resume animation"><img src="/GroundForge/images/play.png" alt="resume"></a>
            <a href="javascript:GF_panel.downloadSVG('${id}')" title="download"><img src="/GroundForge/images/download.jpg" alt="download"></a>
        ` : '';
        const colorChooser = isArray && controls.includes('color') ? `
            <input type="color" id="${id}ColorChooser" name="threadColor" value="#ff0000">
        ` : '';
        const resize = isArray && controls.includes('resize') ? `
            <a href="javascript:GF_panel.maximize('${id}')" title="maximize"><img src="/GroundForge/images/maximize.png" alt="maximize"></a>
            <a href="javascript:GF_panel.resetDimensions('${id}',${sizeStr})" title="reset to default"><img src="/GroundForge/images/reset-dimensions.png" alt="default"></a>
            <a href="javascript:GF_panel.minimize('${id}')" title="minimize"><img src="/GroundForge/images/minimize.png" alt="minimize"></a>
        ` : '';
        const figure = document.createElement('figure');
        figure.innerHTML = `
            <figcaption>
                ${caption.trim()}
                ${cleanup.trim()}
                ${diagram.trim()}
                ${colorChooser.trim()}
                ${resize.trim()}
            </figcaption>
            <div id="${id}"></div>
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
        const { type='pair', step = 0, query, size = this.svgSize } = namedArgs;
        const {width, height} = {
            ...this.svgSize[step < this.svgSize.length ? step : this.svgSize.length],
            ...(typeof size === 'object' && size !== null ? size : {})
        };
        const config = TilesConfig(query)
        if (type === 'pair' && step === 0) {
            const zoom = 1.9
            x = PairSvg.render(config.getItemMatrix, width, height, zoom)
            return x;
        }
        const pairDiagram = NewPairDiagram.create(config)
        const threadDiagram = ThreadDiagram.create(pairDiagram)
        // implement droste steps
        const nodeTransparency = 0.05
        const strokeWidth = "2px"
        const markers = true // use false for slow devices and IE-11, set them at onEnd
        return DiagramSvg.render(threadDiagram, strokeWidth, markers, width, height, nodeTransparency);
    }
}
