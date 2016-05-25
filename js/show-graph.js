fullyTransparant = 0 // global to allow override while testing
var diagram = {}
diagram.startThread = function(svgDefs){
    svgDefs.append('svg:marker')
        .attr('id', "start-thread")
        .attr('viewBox', '-7 -7 14 14')
        .attr('markerWidth', 12)
        .attr('markerHeight', 12 )
        .attr('orient', 'auto')
        .attr('markerUnits', 'userSpaceOnUse')
      .append('svg:path')
        .attr('d', d3.svg.symbol().type("square"))
        .attr('fill', "#000").style('opacity',0.5)
}
diagram.startPair = function(svgDefs){
    svgDefs.append('svg:marker')
        .attr('id', "start-pair")
        .attr('viewBox', '-7 -7 14 14')
        .attr('markerWidth', 10)
        .attr('markerHeight', 12 )
        .attr('orient', 'auto')
        .attr('markerUnits', 'userSpaceOnUse')
      .append('svg:path')
        .attr('d', d3.svg.symbol().type("diamond"))
        .attr('fill', "#000").style('opacity',0.5)
}
diagram.twistMark = function(svgDefs){
    svgDefs.append('svg:marker')
        .attr('id', "twist-1")
        .attr('viewBox', '-2 -2 4 4')
        .attr('markerWidth', 5)
        .attr('markerHeight', 5)
        .attr('orient', 'auto')
        .attr('markerUnits', 'userSpaceOnUse')
      .append('svg:path')
        .attr('d', 'M 0,6 0,-6')
        .attr('fill', "#000")
        .attr('stroke', "#000")
        .attr('stroke-width', "1px")
}
diagram.markers = function(svgDefs,id,color){
    svgDefs.append('svg:marker')
        .attr('id', 'start-' + id)
        .attr('viewBox', '0 -5 10 10')
        .attr('markerWidth', 6)
        .attr('markerHeight', 8 )
        .attr('orient', 'auto')
        .attr('markerUnits', 'userSpaceOnUse')
      .append('svg:path')
        .attr('d', 'M0,0L10,0')
        .attr('stroke-width', 3)
        .attr('stroke', color)

    svgDefs.append('svg:marker')
        .attr('id', 'end-' + id)
        .attr('viewBox', '0 -5 10 10')
        .attr('refX', 10)
        .attr('markerWidth', 6)
        .attr('markerHeight', 8 )
        .attr('orient', 'auto')
        .attr('markerUnits', 'userSpaceOnUse')
      .append('svg:path')
        .attr('d', 'M0,0L10,0')
        .attr('stroke-width', 3)
        .attr('stroke', color)
}
diagram.dx = function(d) { return d.x }
diagram.dy = function(d) { return d.y }
diagram.path = function(d) {
    var sX = d.source.x
    var sY = d.source.y
    var tX = d.target.x
    var tY = d.target.y
    var dX = (tX - sX)
    var dY = (tY - sY)
    var mid = d.left ? ("S" + (sX - dY/3 + dX/3) + "," + (sY + dX/3 + dY/3)) :
              d.right ? ("S" + (sX + dY/3 + dX/3) + "," + (sY + dY/3 - dX/3)) :
                         " "
    if (d.end && d.end == "white")
        return "M"+ sX + "," + sY + mid + " " + (tX - dX/4) + "," + (tY - dY/4)
    if (d.start && d.start == "white")
        return "M"+ (sX + dX/4) + "," + (sY + dY/4) + mid + " " + tX + "," + tY
    if (d.mid)
        return "M"+ sX + "," + sY + " " + (sX + dX/2) + "," + (sY + dY/2) + " " + tX + "," + tY
    return "M"+ sX + "," + sY + " " + tX + "," + tY
}
diagram.showGraph = function(args) {
    var colorpicker = (args.threadColor == undefined ? undefined : d3.select(args.threadColor)[0][0])

    // document creation

    var svgRoot = d3.select(args.container).append("svg")
                .attr("width", args.width)
                .attr("height", args.height)
                .attr("pointer-events", "all")

    // marker definitions

    var defs = svgRoot.append('svg:defs')
    diagram.startThread(defs)
    diagram.startPair(defs)
    diagram.twistMark(defs)
    diagram.markers(defs, 'green','#0f0')
    diagram.markers(defs, 'red','#f00')
    diagram.markers(defs, 'purple','#609')
    //diagram.markers(defs, 'white','#fff')

    // zoom functionality

    var container = svgRoot.append('svg:g')
                .call(d3.behavior.zoom().scale(args.scale).on("zoom", redraw))
              .append('svg:g')
                .attr("transform", args.transform)

    container.append('svg:rect')
        .attr('width', args.width * (5/args.scale))
        .attr('height', args.height * (5/args.scale))
        .attr('fill', 'white')

    function redraw() {
      container.attr("transform",
          "translate(" + d3.event.translate + ")"
          + " scale(" + d3.event.scale + ")");
    }

    // object creation and decoration

    var isIE = document.documentURI == undefined // wrong feature check
    // problem: the marker property is supported by IE but breaks on moving links

    var link = container.selectAll(".path").data(args.links).enter().append("svg:path")
        .attr("class", function(d) { return d.thread ? "link thread" + d.thread : "link"})
        .attr("id",function(d,i) { return "link_" + i; })
        .style('marker-start', function(d) { if (d.start && d.start != "white" && !isIE) return 'url(#start-'+d.start+')'})
        .style('marker-end', function(d) { if (d.end && d.end != "white" && !isIE) return 'url(#end-'+d.end+')'})
        .style('marker-mid', function(d,i) { if (d.mid) return 'url(#twist-1)' } )
        .style('opacity', function(d) { return d.border || d.toPin ? fullyTransparant : 1})
        .style('stroke', '#000')
        .style('fill', 'none')

    var node = container.selectAll(".node").data(args.nodes).enter().append("circle")
        .attr("class", function(d) { return d.startOf ? ("node threadStart") : (d.pin ? ("node pin") : "node")})
        .attr("r", function(d) { return d.pin ? 4 : 6})
        .style('opacity', function(d) { return d.bobbin ? 0.5: (d.pin ? 0.2 : fullyTransparant)})
    node.append("svg:title").text(function(d) {
        return d.title ? d.title
        : d.pin ? "pin"
        : d.bobbin ? "bobbin"
        : d.startOf ? d.startOf.replace("thread","thread ")
        : ""
    })

    // configure layout

    var force = d3.layout.force()
        .nodes(args.nodes)
        .links(args.links)
        .size([args.width, args.height])
        .charge(-20)
        .linkDistance(10)
        .linkStrength(10)
        .start()
        .alpha(0.01)

    // event listeners

    container.selectAll(".threadStart").on('click', function (d) {
        if (d3.event.defaultPrevented) return
        container.selectAll("."+d.startOf).style('stroke', colorpicker.value)
    })
    var drag = force.drag().on("dragstart", function (d) {
        d3.event.sourceEvent.preventDefault();
        d3.event.sourceEvent.stopPropagation();
    })
    node.call(drag)

    var mod = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream ? 10 : 1
    var step = 0
    // layout simulation step
    force.on("tick", function() {
        if ( ((step++)%mod) != 0) return
        link.attr("d", diagram.path)
        node.attr("cx", diagram.dx)
            .attr("cy", diagram.dy)
    })
}
