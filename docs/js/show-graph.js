/*
 Copyright 2017 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
fullyTransparent = 0 // global to allow override while testing
diagram = {}
diagram.showGraph = function(args) {

    var isThreadDiagram = args.nodes[0].title == 'thread 1'
    var isIE = document.documentURI == undefined // wrong feature check
    var isMobileMac = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream
    var markers = !isMobileMac && !isIE

    args.container.node().innerHTML = dibl.SVG().render(args.diagram, args.stroke, markers, 2481, 3507, fullyTransparent)

    var links = args.container.selectAll(".link").data(args.links)
    var nodes = args.container.selectAll(".node").data(args.nodes)

    var threadStarts = args.container.selectAll(".threadStart")
    if ( args.palette ) {
      var colors = args.palette.split(',')
      for(i=threadStarts.size() ; i >= 0 ; i--) {
        var n = (i - 1 + colors.length) % colors.length
        args.container.selectAll(".thread"+i)
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
            args.container.selectAll("."+d.startOf)
              .style('stroke', '#'+colorpicker.value)
              .style('fill', function(d) { return d.bobbin ? '#'+colorpicker.value : 'none' })
        })
    }
    function drawPath(jsLink) {
        var s = jsLink.source
        var t = jsLink.target
        var l = args.diagram.link(jsLink.index)
        return  dibl.SVG().pathDescription(l, s.x, s.y, t.x, t.y)
    }
    function moveNode(d) {
        return "translate(" + d.x + "," + d.y + ")"
    }

    // a higher speed for IE as marks only appear when the animation is finished
    var mod = isMobileMac ? 4 : isIE ? 3 : 2
    var step = 0
    var simTicked = navigator == "no-browser" ? function(){} : function() {
                         if ( step++ > 2 && (step % mod) != 0) return // skip rendering
                         nodes.attr("transform", moveNode)
                         links.attr("d", drawPath)
                     }
    function markStart(d) { if (d.start != "white") return 'url(#start-'+d.start+')' }
    function markEnd(d) { if (d.end != "white") return 'url(#end-'+d.end+')' }
    function markMid(d,i) { if (d.mid) return 'url(#twist-1)' }
    function simEnded() {
                        if (navigator == "no-browser") {
                            nodes.attr("transform", moveNode)
                            links.attr("d", drawPath)
                        }
                        if (!markers) links
                            .style('marker-start', markStart)
                            .style('marker-end', markEnd)
                            .style('marker-mid', markMid)
                        if (args.onAnimationEnd) args.onAnimationEnd()
                    }
    var cx = args.container.node().clientWidth / 2
    var cy = args.container.node().clientHeight / 2
    if (!cx) cx = 200
    if (!cy) cy = 100
    function strength(link){ return link.weak ? 5 : 50 }
    var sim = d3.forceSimulation(args.nodes)
        .force("charge", d3.forceManyBody().strength(-1000))
        .force("link", d3.forceLink(args.links).strength(strength).distance(12).iterations(30))
        .force("center", d3.forceCenter(cx, cy))
        .alpha(0.0035)
        .on("tick", simTicked)
        .on("end", simEnded)

    // zooming and panning

    args.container.call( d3.zoom().on("zoom", zoomed) )
    function zoomed() {
      args.container.select('g').attr("transform", d3.event.transform)
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
