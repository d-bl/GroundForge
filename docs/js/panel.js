const GF_panel = {
    a4: {width: 744, height: 1052},
    load(config) {
        const { caption, id, controls = [] } = config;
        const isArray = Array.isArray(controls);

        const cleanup = isArray && controls.includes('cleanup') ? `
            <a href="javascript:cleanUp(d3.select('#${id} svg'))" title="clean up"><img src="/GroundForge/images/broom.png" alt="broom stick"></a>
        ` : '';
        const diagram = isArray && controls.includes('diagram') ? `
            <a href="javascript:reload()"  title="reload"><img src="/GroundForge/images/wand.png" alt="wand"></a>
            <a href="javascript:nudgeDiagram(d3.select('#${id} svg'))" title="resume animation"><img src="/GroundForge/images/play.png" alt="resume"></a>
            <a href="javascript:download(d3.select('#${id} svg'))" title="download"><img src="/GroundForge/images/download.jpg" alt="nudge"></a>
        ` : '';
        const colorChooser = isArray && controls.includes('color') ? `
            <input type="color" id="${id}ColorChooser" name="threadColor" value="#ff0000">
        ` : '';
        const resize = isArray && controls.includes('resize') ? `
            <a href="javascript:maximize('#${id}')" title="maximize"><img src="/GroundForge/images/maximize.png" alt="maximize"></a>
            <a href="javascript:resetDimensions('#${id}')" title="reset to default"><img src="/GroundForge/images/reset-dimensions.png" alt="default"></a>
            <a href="javascript:minimize('#${id}')" title="minimize"><img src="/GroundForge/images/minimize.png" alt="minimize"></a>
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
    },
    primaryPairSVG(query){
        const config = TilesConfig(query)
        const zoom = 1.9
        return PairSvg.render(config.getItemMatrix, this.a4.width, this.a4.height, zoom)
    },
    primaryThreadSVG(query){
        const config = TilesConfig(query)
        const nodeTransparency = 0.05
        const strokeWidth = "2px"
        const markers = true // use false for slow devices and IE-11, set them at onEnd
        const pairDiagram = NewPairDiagram.create(config)
        const threadDiagram = ThreadDiagram.create(pairDiagram)
        return DiagramSvg.render(threadDiagram, strokeWidth, markers, this.a4.width, this.a4.height, nodeTransparency);
    }
}
