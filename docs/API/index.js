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
function load() {

  var matrix = "586-,-789,2111,-4-4"
  var patterns = new dibl.SheetSVG(2, "height='90mm' width='100mm'")
  patterns.add(matrix, "checker")
  document.getElementById("sheet").innerHTML = (patterns.toSvgDoc().trim())

  var q = "patchWidth=9&patchHeight=12" +
          "&footside=" + matrix +
          "&tile=" + matrix +
          "&footsideStitch=-&tileStitch=ctc" +
          "&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4"
  var config = TilesConfig.create(q)
  var pairDiagram = NewPairDiagram.create(config)
  var threadDiagram = dibl.ThreadDiagram.create(pairDiagram)
  showGraph(d3.select('#pairs'), pairDiagram, "1px")
  showGraph(d3.select('#threads'), threadDiagram, "2px")
}
function showGraph(container, diagram, stroke) {
    var svg = dibl.D3jsSVG
    var markers = true // use false for slow devices and IE-11, set them at onEnd
    container.node().innerHTML = svg.render(diagram, stroke, markers, 400, 400)
    var nodeDefs = diagram.jsNodes()
    var linkDefs = diagram.jsLinks()//can't inline
    var links = container.selectAll(".link").data(linkDefs)
    var nodes = container.selectAll(".node").data(nodeDefs)
    function moveNode(jsNode) {
        return 'translate('+jsNode.x+','+jsNode.y+')'
    }
    function drawPath(jsLink) {
        var s = jsLink.source
        var t = jsLink.target
        var l = diagram.link(jsLink.index)
        return svg.pathDescription(l, s.x, s.y, t.x, t.y)
    }
    function onTick() {
        links.attr("d", drawPath);
        nodes.attr("transform", moveNode);
    }
    // read 'weak' as 'invisible'
    function strength(link){ return link.weak ? link.withPin ? 40 : 10 : 50 }
    var forceLink = d3
      .forceLink(linkDefs)
      .strength(strength)
      .distance(12)
      .iterations(30)
    d3.forceSimulation(nodeDefs)
      .force("charge", d3.forceManyBody().strength(-1000))
      .force("link", forceLink)
      .force("center", d3.forceCenter(200, 200))
      .alpha(0.0035)
      .on("tick", onTick)
}
