var diagram = {}
diagram.showGraph = function(args) {
    var svgRoot = d3.select(args.container).append("svg")
                .attr("width", 600)
                .attr("height", 600)
    var links = svgRoot
        .append('svg:g').selectAll(".path").data(args.links).enter()
        .append("svg:path").style('stroke', '#000')
    var drawPath = function(d) {
        var sX = d.source.x
        var sY = d.source.y
        var tX = d.target.x
        var tY = d.target.y
        return "M"+ sX + "," + sY + " " + tX + "," + tY
    }
    var count = 0
    d3.forceSimulation(args.nodes)
        .force("charge", d3.forceManyBody().strength(-1000))
        .force("link", d3.forceLink(args.links).strength(50).distance(12).iterations(30))
        .force("center", d3.forceCenter(300, 300))
        .alpha(0.0035)
        .on("tick", function() { links.attr("d", drawPath); count++ })
        .on("end", function() { console.log(count + " ticks")})
}
