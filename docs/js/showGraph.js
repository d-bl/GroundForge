function showGraph(containerId, diagram) {
// copied from API/thread.html, added moveToNW by js/print.js

    var container = d3.select(containerId)

    // dimensions for an A1
    var width = 2245
    var height = 3178

    var opacity = 0
    var stroke = 2

    var markers = true // use false for slow devices and IE-11, set them at onEnd
    container.html(DiagramSvg.render(diagram, stroke, markers, width, height, opacity))

    var nodeDefs = diagram.jsNodes()
    var linkDefs = diagram.jsLinks()//can't inline
    var links = container.selectAll(".link").data(linkDefs)
    var nodes = container.selectAll(".node").data(nodeDefs)
    function moveNode(jsNode) {
        return 'translate('+jsNode.x+','+jsNode.y+')'
    }
    function drawPath(jsLink) {
        var s = jsLink.source
        var t = jsLink.target
        var l = diagram.link(jsLink.index)
        return DiagramSvg.pathDescription(l, s.x, s.y, t.x, t.y)
    }
    var tickCounter = 0
    function onTick() {
        if (0 !=  (tickCounter++ % 5) ) return
        links.attr("d", drawPath);
        nodes.attr("transform", moveNode);
    }

    function moveToNW() {
        var x = nodeDefs.reduce(minX).x - 3
        var y = nodeDefs.reduce(minY).y - 3
        function moveNode(jsNode) { return 'translate('+(jsNode.x-x)+','+(jsNode.y-y)+')' }
        function drawPath(jsLink) {
            var s = jsLink.source
            var t = jsLink.target
            var l = diagram.link(jsLink.index)
            // priority for preventing code duplication over less independency
            return DiagramSvg.pathDescription(l, s.x - x, s.y-y, t.x - x, t.y -y)
        }
        links.attr("d", drawPath);
        nodes.attr("transform", moveNode);
    }
    function minX (min, node) { return min.x < node.x ? min : node }
    function minY (min, node) { return min.y < node.y ? min : node }

    function strength(link){ return link.withPin ? 10 : 50 }
    var forceLink = d3
        .forceLink(linkDefs)
        .strength(strength)
        .distance(12)
        .iterations(30)
    d3.forceSimulation(nodeDefs)
        .force("charge", d3.forceManyBody().strength(-1000))
        .force("link", forceLink)
        .force("center", d3.forceCenter(width/2, height/2))
        .alpha(0.0035)
        .on("tick", onTick)
        .on("end", moveToNW)
}
