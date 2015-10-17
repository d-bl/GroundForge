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
function startMarker (svg,id,color){
    svg.append('svg:defs').append('svg:marker')
        .attr('id', id)
        .attr('viewBox', '0 -5 10 10')
        .attr('markerWidth', 6)
        .attr('markerHeight', 8 )
        .attr('orient', 'auto')
      .append('svg:path')
        .attr('d', 'M0,0L10,0')
        .attr('stroke-width', 3)
        .attr('stroke', color)
}
function endMarker (svg,id,color){
    svg.append('svg:defs').append('svg:marker')
        .attr('id', id)
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
function showGraph(containerID, graph, colorpickerID) {
    var width = 800,
        height = 400,
        colorpicker = d3.select(colorpickerID)[0][0]

    // document creation

    var svg = d3.select(containerID).append("svg")
                .attr("width", width)
                .attr("height", height)

    // marker definitions

    startThread(svg)
    startPair(svg)
    startMarker(svg, 'start-green','#0f0')
    endMarker(svg, 'end-green','#0f0')
    startMarker(svg, 'start-red','#f00')
    endMarker(svg, 'end-red','#f00')
    startMarker(svg, 'start-white','#fff')
    endMarker(svg, 'end-white','#fff')

    // object creation and decoration

    var link = svg.selectAll(".link").data(graph.links).enter().append("line")
        .attr("class", function(d) { return d.thread ? "link thread" + d.thread : "link"})
        .style('marker-start', function(d) { if (d.start) return 'url(#start-'+d.start+')'})
        .style('marker-end', function(d) { if (d.end) return 'url(#end-'+d.end+')'})
        .style('opacity', function(d) { return d.border || d.toPin ? 0 : 1})

    var node = svg.selectAll(".node").data(graph.nodes).enter().append("circle")
        .attr("class", function(d) { return d.startOf ? ("node threadStart") : (d.pin ? ("node pin") : "node")})
        .attr("r", function(d) { return d.pin ? 4 : 6})
        .style('opacity', function(d) { return d.bobbin ? 0.5: (d.pin ? 0.2 : 0)})
    node.append("svg:title").text(function(d) { return d.title ? d.title : (d.pin || d.bobbin ? "" : (d.index + 1)) })

    // TODO tweak parametrs for (pin) behaviour once we have larger diagrams
    // https://github.com/mbostock/d3/wiki/Force-Layout#linkDistance
    // http://grokbase.com/t/gg/d3-js/1375rpwbdt/consistent-force-directed-graph-generation
    // http://grokbase.com/t/gg/d3-js/137xwsrbd4/how-to-dynamically-adjusting-linkdistance-and-linkstrength-to-help-reducing-crossing-edges
    var force = d3.layout.force()
        .nodes(graph.nodes)
        .links(graph.links)
        .size([width, height])
        .charge(-120)
        .linkDistance(5)
        .linkStrength(5)
        .start()

    // event listeners

    svg.selectAll(".threadStart").on('dblclick', function (d) {
        svg.selectAll("."+d.startOf).style('stroke', colorpicker.value)
    })
    svg.selectAll(".pin").on('dblclick', function (d) {
        d3.select(this).classed("fixed", d.fixed = false) 
    })
    force.drag().on("dragstart", function (d) {
        if (d.pin) d3.select(this).classed("fixed", d.fixed = true)
    })
    node.call(force.drag)

    // layout simulation step

    force.on("tick", function() {
        link.attr("x1", function(d) { return d.source.x })
            .attr("y1", function(d) { return d.source.y })
            .attr("x2", function(d) { return d.target.x })
            .attr("y2", function(d) { return d.target.y })

        node.attr("cx", function(d) { return d.x })
            .attr("cy", function(d) { return d.y })
    })
}