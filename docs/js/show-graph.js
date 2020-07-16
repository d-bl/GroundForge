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

    args.container.node().innerHTML = D3jsSVG.render(args.diagram, args.stroke, markers, 2481, 3507, fullyTransparent)
    var svgDoc = args.container.select('svg')
    var svgGroup = args.container.select('g')
    var cx = args.container.node().clientWidth / 2
    var cy = args.container.node().clientHeight / 2
    if (!cx) cx = 200
    if (!cy) cy = 100

    var containerID = args.container.attr("id")
    var controls = d3.select("#" + containerID + "controls")
    controls.node().innerHTML = ""
    controls.append("a")
        .attr("target","_blank")
        .attr("href","#fullscreen")
        .attr("title","fullscreen")
        .attr("onblur","this.href='#fullscreen'")
        .attr("onmousedown","setDownloadContent(this,'" + containerID + "')")
      .append("img","")
        .attr("src","/GroundForge/images/fullscreen.jpg")
    controls.append("a")
        .attr("download",containerID + "-diagram.svg")
        .attr("onblur","this.href='#download'")
        .attr("target","_blank")
        .attr("href","#download")
        .attr("title","download")
        .attr("onmousedown","setDownloadContent(this,'" + containerID  + "')")
      .append("img","")
        .attr("src","/GroundForge/images/download.jpg")

    // zooming and panning

    var zoom =  d3.zoom().on("zoom", function() {
      svgGroup.attr("transform", d3.event.transform)
    })
    function zoomIn() {
      var t = d3.zoomTransform(svgDoc.node())
      t.k *= 1.2
      if (t.k) zoom.transform(svgDoc, t)
    }
    function zoomOut() {
      var t = d3.zoomTransform(svgDoc.node())
      t.k /= 1.2
      zoom.transform(svgDoc, t)
    }
    svgDoc.call( zoom )
    controls
      .append("a")
        .attr("href","#zoom-in")
        .attr("title","zoom in")
        .attr("onclick","return false")
      .append("img","").attr("src","/GroundForge/images/zoom-in.jpg").on("click", zoomIn)
    controls
      .append("a")
        .attr("href","#zoom-out")
        .attr("title","zoom out")
        .attr("onclick","return false")
      .append("img","").attr("src","/GroundForge/images/zoom-out.jpg").on("click", zoomOut)

    // bind SVG with data

    var links = args.container.selectAll(".link").data(args.links)
    var nodes = args.container.selectAll(".node").data(args.nodes)

    if ( args.palette ) {
      var nrOfThreads = args.container.selectAll(".threadStart").size()
      var colors = args.palette.split(',')
      for(i=0 ; i<nrOfThreads ; i++) {
        var n = (i - 1 + colors.length) % colors.length
        args.container.selectAll(".thread"+i)
          .style('stroke', colors[n])
          .style('fill', function(d) { return d.bobbin ? colors[n] : 'none'})
      }
    }

    function drawPath(jsLink) {
        var s = jsLink.source
        var t = jsLink.target
        var l = args.diagram.link(jsLink.index)
        return  D3jsSVG.pathDescription(l, s.x, s.y, t.x, t.y)
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
    function markStart(d) { if (d.start) return 'url(#start-'+d.start+')' }
    function markEnd(d) { if (d.end) return 'url(#end-'+d.end+')' }
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
    function strength(link){ return link.weak ? link.withPin ? 40 : 0.5 : 50 }
    var sim = d3.forceSimulation(args.nodes)
        .force("charge", d3.forceManyBody().strength(-1000))
        .force("link", d3.forceLink(args.links).strength(strength).distance(12).iterations(30))
        .force("center", d3.forceCenter(cx, cy))
        .alpha(0.0035)
        .on("tick", simTicked)
        .on("end", simEnded)

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
