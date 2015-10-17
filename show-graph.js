function startThread (svg){
    svg.append('svg:defs').append('svg:marker')
        .attr('id', "start-thread")
        .attr('viewBox', '-7 -7 14 14')
        .attr('markerWidth', 12)
        .attr('markerHeight', 12 )
        .attr('orient', 'auto')
      .append('svg:path')
        .attr('d', d3.svg.symbol().type("square"))
        .attr('fill', "#999");
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
        .attr('fill', "#999");
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
        .attr('stroke', color);
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
        .attr('stroke', color);
}
function showGraph(container, graph, colorpickerID) {
    var width = 800,
        height = 400
        colorpicker = d3.select(colorpickerID)[0][0];

    var force = d3.layout.force()
        .charge(-120)
        .linkDistance(5)
        .linkStrength(5)
        .size([width, height]);

    var svg = d3.select(container).append("svg")
        .attr("width", width)
        .attr("height", height);

    force.nodes(graph.nodes).links(graph.links).start();

    startThread(svg)
    startPair(svg)
    startMarker(svg, 'start-green','#0f0')
    endMarker(svg, 'end-green','#0f0')
    startMarker(svg, 'start-red','#f00')
    endMarker(svg, 'end-red','#f00')
    startMarker(svg, 'start-white','#fff')
    endMarker(svg, 'end-white','#fff')
    var link = svg.selectAll(".link").data(graph.links).enter().append("line")
        .attr("class", function(d) { return d.thread ? "link thread" + d.thread : "link"})
        .style('marker-start', function(d) {return 'url(#start-'+d.start+')';})
        .style('marker-end', function(d) {return 'url(#end-'+d.end+')';})
        .style('opacity', function(d) { return d.invisible? 0: 1})

    var node = svg.selectAll(".node").data(graph.nodes).enter().append("circle")
        .attr("class", "node")
        .attr("r", 6)
        .style('opacity', function(d) { return d.bobbin ? 1: 0})
        .on('dblclick', function(d) { if (d.startOf) svg.selectAll("."+d.startOf).style('stroke', colorpicker.value) })
        .call(force.drag);

    node.append("svg:title").text(function(d) { return d.title ? d.title : (d.index + 1); })

    force.on("tick", function() {
        link.attr("x1", function(d) { return d.source.x; })
            .attr("y1", function(d) { return d.source.y; })
            .attr("x2", function(d) { return d.target.x; })
            .attr("y2", function(d) { return d.target.y; });

        node.attr("cx", function(d) { return d.x; })
            .attr("cy", function(d) { return d.y; });
    });
}