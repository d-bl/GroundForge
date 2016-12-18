fullyTransparant = 0 // global to allow override while testing
var diagram = {}
diagram.start = function(svgDefs, id, shape){
    svgDefs.append('svg:marker')
        .attr('id', id)
        .attr('viewBox', '-7 -7 14 14')
        .attr('markerWidth', 12)
        .attr('markerHeight', 12 )
        .attr('orient', 'auto')
        .attr('markerUnits', 'userSpaceOnUse')
      .append('svg:path')
        .attr('d', shape)
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
diagram.shape.square = "M -6,-6 6,-6 6,6 -6,6 Z"
diagram.shape.diamond = "M -5,0 0,8 5,0 0,-8 Z"
diagram.shape.bobbin = "m 0,40 c -3.40759,0 -6.01351,3.60204 -1.63269,3.60204 l 0,19.82157 c -3.67432,-0.008 -1.7251,5.087 -1.32784,7.27458 0.76065,4.18864 1.01701,8.40176 0.3478,12.58551 -1.68869,10.55725 -2.31894,21.67593 1.25552,31.9161 0.2088,0.59819 0.68935,2.7631 1.40054,2.7636 0.71159,0 1.19169,-2.16521 1.40057,-2.7636 C 5.01838,104.95964 4.38954,93.84095 2.70085,83.2837 2.03164,79.09995 2.28656,74.88683 3.04721,70.69819 3.44447,68.51061 5.61865,63.44146 1.71951,63.42361 l 0,-19.82157 C 5.86853,43.60204 3.4855,39.99659 0,40 L 0,0"
diagram.markLinks = function(links) {
  links
    .style('marker-start', function(d) { if (d.start != "white") return 'url(#start-'+d.start+')' })
    .style('marker-end', function(d) { if (d.end != "white") return 'url(#end-'+d.end+')' })
    .style('marker-mid', function(d,i) { if (d.mid) return 'url(#twist-1)' })
}

diagram.showGraph = function(args) {
    var htmlContainer = d3.select(args.container)

    args.width = args.width ? args.width : 744
    args.height = args.height? args.height : 1052
    args.viewWidth = args.viewWidth ? args.viewWidth : (args.width / 2)
    args.viewHeight = args.viewHeight? args.viewHeight : (args.height / 2)

    // document creation
    var svgRoot = htmlContainer.append("svg")
                .attr("width", args.width)
                .attr("height", args.height)
                .attr("pointer-events", "all")

    // marker definitions

    var defs = svgRoot.append('svg:defs')
    diagram.start(defs, "start-thread", diagram.shape.square)
    diagram.start(defs, "start-pair", diagram.shape.diamond)
    diagram.twistMark(defs)
    diagram.markers(defs, 'green','#0f0')
    diagram.markers(defs, 'red','#f00')
    diagram.markers(defs, 'purple','#609')
    //diagram.markers(defs, 'white','#fff')

    var svgContainer = svgRoot.append('svg:g')

    // object creation and decoration

    var isThreadDiagram = args.nodes[0].title == 'thread 1'
    var isIE = document.documentURI == undefined // wrong feature check
    var isMobileMac = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream

    var links = svgContainer.selectAll(".path").data(args.links).enter().append("svg:path")
        .attr("class", function(d) { return d.thread ? "link thread" + d.thread : "link" })
        .style('opacity', function(d) { return d.border || d.toPin ? fullyTransparant : 1})
        .style('stroke', '#000')
        .style('fill', 'none')
    if (isThreadDiagram) links.style('stroke-width', '2px')
    if (!isIE && !isMobileMac) diagram.markLinks(links)

    var nodes = svgContainer.selectAll(".node").data(args.nodes).enter().append("svg:path")
        .attr("d", function(d) { return (d.bobbin ? diagram.shape.bobbin : d.pin ? diagram.shape.pin : diagram.shape.stitch)})
        .attr("class", function(d) { return "node " + (d.startOf ? "threadStart" : d.thread ? ("thread"+d.thread) : "")})
        .style('opacity', function(d) { return d.bobbin || d.pin ? 1 : fullyTransparant})
        .style('fill', '#000000')
        .style('stroke', function(d) { return d.pin ? 'none' : '#000000'})

    nodes.append("svg:title").text(function(d) { return d.title ? d.title : "" })

    var threadStarts = svgContainer.selectAll(".threadStart")
    if ( args.palette ) {
      var colors = args.palette.split(',')
      for(i=threadStarts.size() ; i >= 0 ; i--) {
        var n = (i - 1 + colors.length) % colors.length
        svgContainer.selectAll(".thread"+i)
          .style('stroke', colors[n])
          .style('fill', function(d) { return d.bobbin ? colors[n] : 'none'})
      }
    }

    // event listeners

    var colorpicker = (args.threadColor == undefined ? undefined : d3.select(args.threadColor).node())
    if (colorpicker) {
        threadStarts.on('click', function (d) {
            if (d3.event.defaultPrevented) return
            sim.alpha(0).stop()
            svgContainer.selectAll("."+d.startOf)
              .style('stroke', '#'+colorpicker.value)
              .style('fill', function(d) { return d.bobbin ? '#'+colorpicker.value : 'none' })
        })
    }

    var drawPath = function(d) {
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
    var moveNode = function(d) {
        return "translate(" + d.x + "," + d.y + ")"
    }

    // a higher speed for IE as marks only appear when the animation is finished
    var mod = isMobileMac ? 4 : isIE ? 3 : 2
    var step = 0
    var simTicked = function() {
                         if ( step++ > 2 && (step % mod) != 0) return // skip rendering
                         nodes.attr("transform", moveNode)
                         links.attr("d", drawPath)
                     }
    var simEnded = function(){
                        if (isIE || isMobileMac) diagram.markLinks(links)
                        if (args.onAnimationEnd) args.onAnimationEnd()
                    }
    function strength(link){ return link.weak ? 5 : 50 }
    var sim = d3.forceSimulation(args.nodes)
        .force("charge", d3.forceManyBody().strength(-1000))
        .force("link", d3.forceLink(args.links).strength(strength).distance(12).iterations(30))
        .force("center", d3.forceCenter(args.viewWidth / 2, args.viewHeight / 2))
        .alpha(0.0035)
        .on("tick", simTicked)
        .on("end", simEnded)

    // zooming and panning

    htmlContainer.call( d3.zoom().on("zoom", zoomed) )
    function zoomed() {
      svgContainer.attr("transform", d3.event.transform)
    }

    // dragging nodes

    nodes.call(d3.drag()
                   .on("start", dragstarted)
                   .on("drag", dragged)
                   .on("end", dragended))
    function dragstarted(d) {
      if (!d3.event.active) sim.alpha(0.005).restart()
      d.fx = d.x;
      d.fy = d.y;
    }
    function dragged(d) {
      d.fx = d3.event.x;
      d.fy = d3.event.y;
    }
    function dragended(d) {
      step = 0
      if (!d3.event.active) sim.alpha(0.005).restart()
      d.fx = null;
      d.fy = null;
    }
}
