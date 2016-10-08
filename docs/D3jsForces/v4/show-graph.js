var diagram = {}
diagram.shape = {}
diagram.shape.stitch = "M 6,0 A 6,6 0 0 1 0,6 6,6 0 0 1 -6,0 6,6 0 0 1 0,-6 6,6 0 0 1 6,0 Z" // larger circle
diagram.showGraph = function(args) {
    var svgRoot = d3.select(args.container).append("svg")
                .attr("width", 600)
                .attr("height", 600)
                .attr("pointer-events", "all")
    var container = svgRoot.append('svg:g')
    var links = container.selectAll(".path").data(args.links).enter().append("svg:path")
        .attr("class", "link")
        .style('stroke', '#000')
        .style('fill', 'none')
    var nodes = container.selectAll(".node").data(args.nodes).enter().append("svg:path")
        .attr("d", diagram.shape.stitch)
        .attr("class", "node")
        .style('fill', '#000000')
        .style('stroke', 'none')
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
    var count = 0
    var simTicked = function() {
                         nodes.attr("transform", moveNode)
                         links.attr("d", drawPath)
                         count++
                     }
    d3.forceSimulation(args.nodes)
        .force("charge", d3.forceManyBody().strength(-10))
        .force("link", d3.forceLink(args.links).strength(1).distance(10).iterations(15))
        .force("center", d3.forceCenter(220,130))
        .alpha(0.0035)
        .on("tick", simTicked)
        .on("end", function(){console.log(count + " ticks")})
}
