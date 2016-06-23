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
diagram.shape = {}
diagram.shape.stitch = "M 6,0 A 6,6 0 0 1 0,6 6,6 0 0 1 -6,0 6,6 0 0 1 0,-6 6,6 0 0 1 6,0 Z" // larger circle
diagram.shape.pin = "M 4,0 A 4,4 0 0 1 0,4 4,4 0 0 1 -4,0 4,4 0 0 1 0,-4 4,4 0 0 1 4,0 Z" // smaller circle
diagram.shape.bobbin = "m 0,40.839856 c -3.4075867,0 -6.0135054,3.60204 -1.632691,3.60204 0,0 0.00279,0.290427 0.00515,1.041986 5.249e-4,0.166984 -0.4474921,0.356732 -0.4470421,0.571116 C -2.0733531,46.644639 0,46.807377 0,46.807377 c 0,0 -2.0747976,-0.07449 -2.0782161,0.839791 C -2.0816346,48.561451 0,48.558405 0,48.558405 c 0,0 -2.1134183,-0.621879 -2.1273613,0.82057 -0.00182,0.188085 0.4448321,0.399374 0.4428921,0.630466 -0.027732,3.313461 -0.067563,10.698355 -0.1242943,12.135284 -0.031037,0.78614 0.35211,1.57119 -0.7908199,2.22383 -2.4753801,1.3408 -0.42951,4.9472 -0.3608,7.1695 1.0825399,4.117205 1.0596899,8.408805 0.3478,12.585505 -1.64596,10.564 -2.2015401,21.6357 1.2555199,31.9161 0.3219301,0.5457 0.69042007,2.8026 1.40054007,2.7636 0.71047,0.04 1.07854993,-2.2177 1.40056993,-2.7636 3.45706,-10.2804 2.9029,-21.3521 1.25695,-31.9161 -0.7119,-4.1767 -0.73619,-8.4683 0.34636,-12.585505 0.0687,-2.2223 2.11603,-5.8287 -0.35934,-7.1695 C 1.295109,63.632725 1.6393472,62.818342 1.6152571,62.031956 1.5718248,60.614171 1.5542634,53.373027 1.5528838,50.132229 1.5527814,49.891641 2.0012902,49.6731 2.0013622,49.480624 2.001883,48.088031 0,48.558405 0,48.558405 c 0,0 2.0336469,0.308522 2.0460054,-0.600477 C 2.0583638,47.048929 0,46.807377 0,46.807377 c 0,0 2.1256409,0.112943 2.1256409,-0.473743 0,-0.488459 -0.4256971,-0.570197 -0.4181031,-0.896492 0.00759,-0.326294 0.012141,-0.995246 0.012141,-0.995246 4.1490227,0 1.7659954,-3.605449 -1.7196788,-3.60204 L 0,-0.04325 Z"
diagram.transform = function(d) {
    return "translate(" + d.x + "," + d.y + ")"
}
diagram.markLinks = function(links) {
  links
    .style('marker-start', function(d) { if (d.start != "white") return 'url(#start-'+d.start+')' })
    .style('marker-end', function(d) { if (d.end != "white") return 'url(#end-'+d.end+')' })
    .style('marker-mid', function(d,i) { if (d.mid) return 'url(#twist-1)' })
}
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
    var isMobileMac = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream

    var link = container.selectAll(".path").data(args.links).enter().append("svg:path")
        .attr("class", function(d) { return d.thread ? "link thread" + d.thread : "link"})
        .attr("id",function(d,i) { return "link_" + i; })
        .style('opacity', function(d) { return d.border || d.toPin ? fullyTransparant : 1})
        .style('stroke', '#000')
        .style('fill', 'none')
    if (!isIE) diagram.markLinks(link)

    var sh // an inline assignment prevent two lookups
    var node = container.selectAll(".node").data(args.nodes).enter().append("svg:path")
        .attr("d", function(d) { return (d.bobbin ? diagram.shape.bobbin : d.pin ? diagram.shape.pin : diagram.shape.stitch)})
        .attr("class", function(d) { return "node " + (d.startOf ? "threadStart" : d.thread ? ("thread"+d.thread) : "")})
        .style('opacity', function(d) { return d.bobbin || d.pin ? 1 : fullyTransparant})
        .style('fill', function(d) { return '#999999'})
        .style('stroke', function(d) { return d.pin ? 'none' : '#000000'})

     node.append("svg:title").text(function(d) { return d.title ? d.title : "" })
     var threadStarts = container.selectAll(".threadStart")

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

    if ( args.palette ) {
      var colors = args.palette.split(',')
      for(i=threadStarts[0].length ; i >= 0 ; i--) {
        var n = (i - 1 + colors.length) % colors.length
        container.selectAll(".thread"+i).style('stroke', colors[n])
      }
    }

    // event listeners

    threadStarts.on('click', function (d) {
        if (d3.event.defaultPrevented) return
        container.selectAll("."+d.startOf).style('stroke', '#'+colorpicker.value)
    })

    // a higher speed for IE as marks only appear when the animation is finished
    var mod = isMobileMac ? 10 : isIE ? 3 : 2
    var step = 0
    // layout simulation step
    force.on("tick", function() {
        if ( ((step++)%mod) != 0) return
        link.attr("d", diagram.path)
        node.attr("transform", diagram.transform)
    })
    force.on("end", function(){
      if (isIE) diagram.markLinks(link)
    })
}
