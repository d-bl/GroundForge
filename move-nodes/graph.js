var diagram = {}
diagram.shape = {}
diagram.shape.stitch = "M 6,0 A 6,6 0 0 1 0,6 6,6 0 0 1 -6,0 6,6 0 0 1 0,-6 6,6 0 0 1 6,0 Z" // larger circle
diagram.showGraph = function(args) {
    var container = d3.select(args.container)
              .append("svg")
                .attr("width", 600)
                .attr("height", 600)
                .attr("pointer-events", "all")
              .append('svg:g')
    var links = container.selectAll(".path").data(args.links).enter().append("svg:path")
        .attr("class", "link")
        .style('stroke', '#000')
        .style('fill', 'none')
    var nodes = container.selectAll(".node").data(args.nodes).enter().append("svg:path")
        .attr("d", diagram.shape.stitch)
        .attr("class", function(d) { return "node " + tr(d.title) })
        .style('fill', '#000000')
        .style('stroke', '#000000')
    var sim = d3.forceSimulation(args.nodes)
        .force("charge", d3.forceManyBody().strength(-50))
        .force("link", d3.forceLink(args.links).strength(1).distance(5).iterations(10))
        .alpha(0.0)
        .on("tick", simTicked)
    nodes.append("svg:title").text(function(d) { return tr(d.title) })
    nodes.call(d3.drag()
                   .on("start", dragstarted)
                   .on("drag", dragged)
                   .on("end", dragended)
    )
    function simTicked () {
        nodes.attr("transform", moveNode)
        links.attr("d", drawPath)
    }
    function drawPath(d) {
        var sX = d.source.x
        var sY = d.source.y
        var tX = d.target.x
        var tY = d.target.y
        return "M"+ sX + "," + sY + " " + tX + "," + tY
    }
    function moveNode (d) {
        d.fx = d.x;
        d.fy = d.y;
        return "translate(" + d.x + "," + d.y + ")"
    }
    function dragstarted(d) {
        if (!d3.event.active) sim.alphaTarget(0.05).restart();
    }
    function dragged(d) {
        d.x = d.fx = d3.event.x;
        d.y = d.fy = d3.event.y;
    }
    function dragended(d) {
        var cl = "."+tr(d.title)
        var sel = container.selectAll(cl)
        sel.attr("transform", moveNode)
    }
    function tr(title) {
        return (title ? title : "").replace("Pair ","pair").replace("ctc - ","")
    }
}
