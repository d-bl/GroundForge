const GF_panel = {
    svgSize: {width: 744, height: 1052}, // A1
    panelSize: {width: 250, height: 250},
    load(config) {
        const { caption, id, controls = [] } = config;
        const isArray = Array.isArray(controls);

        const cleanup = isArray && controls.includes('cleanup') ? `
            <a href="javascript:cleanUp(d3.select('#${id} svg'))" title="clean up"><img src="/GroundForge/images/broom.png" alt="broom stick"></a>
        ` : '';
        const diagram = isArray && controls.includes('diagram') ? `
            <a href="javascript:reload()"  title="reload"><img src="/GroundForge/images/wand.png" alt="wand"></a>
            <a href="javascript:nudgeDiagram(d3.select('#${id} svg'))" title="resume animation"><img src="/GroundForge/images/play.png" alt="resume"></a>
            <a href="javascript:GF_panel.downloadSVG('${id}')" title="download"><img src="/GroundForge/images/download.png" alt="download"></a>
        ` : '';
        const colorChooser = isArray && controls.includes('color') ? `
            <input type="color" id="${id}ColorChooser" name="threadColor" value="#ff0000">
        ` : '';
        const resize = isArray && controls.includes('resize') ? `
            <a href="javascript:GF_panel.maximize('${id}')" title="maximize"><img src="/GroundForge/images/maximize.png" alt="maximize"></a>
            <a href="javascript:GF_panel.resetDimensions('${id}')" title="reset to default"><img src="/GroundForge/images/reset-dimensions.png" alt="default"></a>
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
        this.resetDimensions(id)
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
            el.style.width = this.panelSize.width;
            el.style.height = "0";
        }
        return false;
    },
    resetDimensions(containerId) {
        const el = document.getElementById(containerId);
        if (el) {
            el.style.width = this.panelSize.width;
            el.style.height = this.panelSize.height;
        }
        return false;
    },
    primaryPairSVG(query){
        const config = TilesConfig(query)
        const zoom = 1.9
        return PairSvg.render(config.getItemMatrix, this.svgSize.width, this.svgSize.height, zoom)
    },
    primaryThreadSVG(query){
        const config = TilesConfig(query)
        const nodeTransparency = 0.05
        const strokeWidth = "2px"
        const markers = true // use false for slow devices and IE-11, set them at onEnd
        const pairDiagram = NewPairDiagram.create(config)
        const threadDiagram = ThreadDiagram.create(pairDiagram)
        return DiagramSvg.render(threadDiagram, strokeWidth, markers, this.svgSize.width, this.svgSize.height, nodeTransparency);
    }
}
