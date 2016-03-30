fullyTransparant = 0 // global to allow override while testing
function startThread (svgDefs){
    svgDefs.append('svg:marker')
        .attr('id', "start-thread")
        .attr('viewBox', '-7 -7 14 14')
        .attr('markerWidth', 12)
        .attr('markerHeight', 12 )
        .attr('orient', 'auto')
      .append('svg:path')
        .attr('d', d3.svg.symbol().type("square"))
        .attr('fill', "#000").style('opacity',0.5)
}
function startPair (svgDefs){
    svgDefs.append('svg:marker')
        .attr('id', "start-pair")
        .attr('viewBox', '-7 -7 14 14')
        .attr('markerWidth', 10)
        .attr('markerHeight', 12 )
        .attr('orient', 'auto')
      .append('svg:path')
        .attr('d', d3.svg.symbol().type("diamond"))
        .attr('fill', "#000").style('opacity',0.5)
}
function twistMark (svgDefs){
    svgDefs.append('svg:marker')
        .attr('id', "twist-1")
        .attr('viewBox', '-2 -2 4 4')
        .attr('markerWidth', 5)
        .attr('markerHeight', 5)
        .attr('orient', 'auto')
      .append('svg:path')
        .attr('d', 'M 0,6 0,-6')
        .attr('fill', "#000")
        .attr('stroke', "#000")
        .attr('stroke-width', "1px")
}
function markers (svgDefs,id,color){
    svgDefs.append('svg:marker')
        .attr('id', 'start-' + id)
        .attr('viewBox', '0 -5 10 10')
        .attr('markerWidth', 6)
        .attr('markerHeight', 8 )
        .attr('orient', 'auto')
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
      .append('svg:path')
        .attr('d', 'M0,0L10,0')
        .attr('stroke-width', 3)
        .attr('stroke', color)
}
function showGraph(args) {
    var colorpicker = (args.threadColor == undefined ? undefined : d3.select(args.threadColor)[0][0])

    // document creation

    var svgRoot = d3.select(args.container).append("svg")
                .attr("width", args.width)
                .attr("height", args.height)
                .attr("pointer-events", "all")

    // marker definitions

    var defs = svgRoot.append('svg:defs')
    startThread(defs)
    startPair(defs)
    twistMark(defs)
    markers(defs, 'green','#0f0')
    markers(defs, 'red','#f00')
    markers(defs, 'purple','#609')
    markers(defs, 'white','#fff')

    // zoom functionality

    var container = svgRoot.append('svg:g')
                .call(d3.behavior.zoom().scale(args.scale).on("zoom", redraw))
              .append('svg:g')
                .attr("transform", args.transform)

    container.append('svg:rect')
        .attr('width', args.width * (1/args.scale))
        .attr('height', args.height * (1/args.scale))
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
        .style('marker-start', function(d) { if (d.start && !isIE) return 'url(#start-'+d.start+')'})
        .style('marker-end', function(d) { if (d.end && !isIE) return 'url(#end-'+d.end+')'})
        .style('marker-mid', function(d,i) { if (d.mid) return 'url(#twist-1)' } )
        .style('opacity', function(d) { return d.border || d.toPin ? fullyTransparant : 1})
        .style('stroke', '#000')

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
        .alpha(0.02)

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

    // layout simulation step
    force.on("tick", function() {
        link.attr("d", function(d) {
          var sX = d.source.x
          var sY = d.source.y
          var tX = d.target.x
          var tY = d.target.y
          var mX = (d.source.x * 1 + d.target.x * 1) / 2
          var mY = (d.source.y * 1 + d.target.y * 1) / 2
          var s = " "
          if (d.left) {
            mX = sY - mY + mX
            my = mX - sY + mY
            s = "S"
          }
          if (d.right) {
            mX = tY - mY + mX
            my = mX - tY + mY
            s = "S"
          }
          return "M" + sX + "," + sY +
                 s   + mX + "," + mY +
                 " " + tX + "," + tY
        })

        node.attr("cx", function(d) { return d.x })
            .attr("cy", function(d) { return d.y })
    })
}
