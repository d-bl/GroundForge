fullyTransparant = 0 // global to allow override while testing
function startThread (svg){
    svg.append('svg:defs').append('svg:marker')
        .attr('id', "start-thread")
        .attr('viewBox', '-7 -7 14 14')
        .attr('markerWidth', 12)
        .attr('markerHeight', 12 )
        .attr('orient', 'auto')
      .append('svg:path')
        .attr('d', d3.svg.symbol().type("square"))
        .attr('fill', "#000").style('opacity',0.5)
}
function startPair (svg){
    svg.append('svg:defs').append('svg:marker')
        .attr('id', "start-pair")
        .attr('viewBox', '-7 -7 14 14')
        .attr('markerWidth', 10)
        .attr('markerHeight', 12 )
        .attr('orient', 'auto')
      .append('svg:path')
        .attr('d', d3.svg.symbol().type("diamond"))
        .attr('fill', "#000").style('opacity',0.5)
}
function twistMark (svg){
    svg.append('svg:defs').append('svg:marker')
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
function markers (svg,id,color){
    svg.append('svg:defs').append('svg:marker')
        .attr('id', 'start-' + id)
        .attr('viewBox', '0 -5 10 10')
        .attr('markerWidth', 6)
        .attr('markerHeight', 8 )
        .attr('orient', 'auto')
      .append('svg:path')
        .attr('d', 'M0,0L10,0')
        .attr('stroke-width', 3)
        .attr('stroke', color)

    svg.append('svg:defs').append('svg:marker')
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

    var svg = d3.select(args.container).append("svg")
                .attr("width", args.width)
                .attr("height", args.height)

    // marker definitions

    startThread(svg)
    startPair(svg)
    twistMark(svg)
    markers(svg, 'green','#0f0')
    markers(svg, 'red','#f00')
    markers(svg, 'purple','#609')
    markers(svg, 'white','#fff')

    // object creation and decoration

    var isIE = document.documentURI == undefined // wrong feature check
    // problem: the marker property is supported by IE but breaks on moving links
    var link = svg.selectAll(".path").data(args.links).enter().append("svg:path")
        .attr("class", function(d) { return d.thread ? "link thread" + d.thread : "link"})
        .attr("id",function(d,i) { return "link_" + i; })
        .style('marker-start', function(d) { if (d.start && !isIE) return 'url(#start-'+d.start+')'})
        .style('marker-end', function(d) { if (d.end && !isIE) return 'url(#end-'+d.end+')'})
        .style('marker-mid', function(d,i) { if (d.mid) return 'url(#twist-1)' } )
        .style('opacity', function(d) { return d.border || d.toPin ? fullyTransparant : 1})
        .style('stroke', '#000')

    var node = svg.selectAll(".node").data(args.nodes).enter().append("circle")
        .attr("class", function(d) { return d.startOf ? ("node threadStart") : (d.pin ? ("node pin") : "node")})
        .attr("r", function(d) { return d.pin ? 4 : 6})
        .style('opacity', function(d) { return d.bobbin ? 0.5: (d.pin ? 0.2 : fullyTransparant)})
    node.append("svg:title").text(function(d) { 
        return d.title ? d.title 
        : d.pin ? "pin: drag to fix, click to release"
        : d.bobbin ? "bobbin"
        : d.startOf ? d.startOf.replace("thread","thread ")
        : ""
    })

    // configure layout

    var force = d3.layout.force()
        .nodes(args.nodes)
        .links(args.links)
        .size([args.width, args.height])
        .charge(-120)
        .linkDistance(5)
        .linkStrength(5)
        .start()

    // event listeners

    svg.selectAll(".threadStart").on('click', function (d) {
        if (d3.event.defaultPrevented) return
        svg.selectAll("."+d.startOf).style('stroke', colorpicker.value)
    })
    svg.selectAll(".pin").on('click', function (d) {
        if (d3.event.defaultPrevented) return
        d3.select(this).classed("fixed", d.fixed = false) 
    })
    force.drag().on("dragstart", function (d) {
        if (!d.pin) return
        d3.select(this).classed("fixed", d.fixed = true)
    })
    // zooming made dragging less fluent, so not implemented
    node.call(force.drag)

    // layout simulation step

    force.on("tick", function() {
        link.attr("d", function(d) {
          var middleX = (d.source.x * 1 + d.target.x * 1) / 2,
              middleY = (d.source.y * 1 + d.target.y * 1) / 2
          return "M" + d.source.x + "," + d.source.y +
                 " " + middleX    + "," + middleY +
                 " " + d.target.x + "," + d.target.y
        })

        node.attr("cx", function(d) { return d.x })
            .attr("cy", function(d) { return d.y })
    })
}