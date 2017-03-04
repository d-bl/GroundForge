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
fullyTransparant = 0 // global to allow override while testing
diagram = {}
diagram.showGraph = function(args) {
    var htmlContainer = d3.select(args.container)

    args.width = args.width ? args.width : 744
    args.height = args.height? args.height : 1052
    args.viewWidth = args.viewWidth ? args.viewWidth : (args.width / 2)
    args.viewHeight = args.viewHeight? args.viewHeight : (args.height / 2)

    // document creation
    var svgRoot = htmlContainer.append("svg")
                .attr("id", "svg2")
                .attr("version", "1.1")
                .attr("width", args.width)
                .attr("height", args.height)
                .attr("pointer-events", "all")
                .attr("xmlns", "http://www.w3.org/2000/svg")
                .attr("xmlns:svg", "http://www.w3.org/2000/svg")
                .attr("xmlns:xlink", "http://www.w3.org/1999/xlink")

    svgRoot.append('svg:defs').node().innerHTML = dibl.SVG().markerDefinitions
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

    function markLinks() {
      links
        .style('marker-start', function(d) { if (d.start != "white") return 'url(#start-'+d.start+')' })
        .style('marker-end', function(d) { if (d.end != "white") return 'url(#end-'+d.end+')' })
        .style('marker-mid', function(d,i) { if (d.mid) return 'url(#twist-1)' })
    }
    if (isThreadDiagram) links.style('stroke-width', '2px')
    if (!isIE && !isMobileMac) markLinks()

    var bobbinShape = dibl.SVG().bobbin
    var pinShape = dibl.SVG().circle(4)
    var stitchShape = dibl.SVG().circle(6)
    var nodes = svgContainer.selectAll(".node").data(args.nodes).enter().append("svg:path")
        .attr("d", function(d) { return (d.bobbin ? bobbinShape : d.pin ? pinShape : stitchShape)})
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
            diagram.sim.alpha(0).stop()
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
    var simTicked = navigator == "no-browser" ? function(){} : function() {
                         if ( step++ > 2 && (step % mod) != 0) return // skip rendering
                         nodes.attr("transform", moveNode)
                         links.attr("d", drawPath)
                     }
    var simEnded = function(){
                        if (navigator == "no-browser") {
                            nodes.attr("transform", moveNode)
                            links.attr("d", drawPath)
                        }
                        if (isIE || isMobileMac) markLinks()
                        if (args.onAnimationEnd) args.onAnimationEnd()
                    }
    function strength(link){ return link.weak ? 5 : 50 }
    diagram.sim = d3.forceSimulation(args.nodes)
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
      if (!d3.event.active) diagram.sim.alpha(0.005).restart()
      d.fx = d.x;
      d.fy = d.y;
    }
    function dragged(d) {
      d.fx = d3.event.x;
      d.fy = d3.event.y;
    }
    function dragended(d) {
      step = 0
      if (!d3.event.active) diagram.sim.alpha(0.005).restart()
      d.fx = null;
      d.fy = null;
    }
}
