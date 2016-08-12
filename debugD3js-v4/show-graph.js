var diagram = {}
diagram.shape = {}
diagram.shape.stitch = "M 6,0 A 6,6 0 0 1 0,6 6,6 0 0 1 -6,0 6,6 0 0 1 0,-6 6,6 0 0 1 6,0 Z" // larger circle
diagram.showGraph = function(args) {
    var m = args.links.length
    for (i = 0; i < m; ++i) {
      // workaround to prevent undefined in
      // https://github.com/d3/d3-force/blob/e6ac2336/src/link.js#L56-L61
      var link = args.links[i]
      link.source = args.nodes[link.source];
      link.target = args.nodes[link.target];
    }
    var svgRoot = d3.select(args.container).append("svg")
                .attr("width", 600)
                .attr("height", 600)
                .attr("pointer-events", "all")
    var container = svgRoot.append('svg:g').append('svg:g')
    var links = container.selectAll(".path").data(args.links).enter().append("svg:path")
        .attr("class", "link")
        .style('stroke', '#000')
        .style('fill', 'none')
    var nodes = container.selectAll(".node").data(args.nodes).enter().append("svg:path")
        .attr("d", diagram.shape.stitch)
        .attr("class", "node")
        .style('fill', '#000000')
        .style('stroke', '#000000')
    var drawPath = function(d) {
        var sX = d.source.x
        var sY = d.source.y
        var tX = d.target.x
        var tY = d.target.y
        return "M"+ sX + "," + sY + " " + tX + "," + tY
    }
    var moveNode = function(d) {
        return "translate(" + d.x + "," + d.y + ")"
    }
    var simTicked = function() {
                         nodes.attr("transform", moveNode)
                         links.attr("d", drawPath)
                     }
    var sim = d3.forceSimulation(args.nodes)
        .force("charge", d3.forceManyBody().strength(-2))
        .force("link", d3.forceLink(links).strength(1).distance(5).iterations(10))
        .on("tick", simTicked)
}
