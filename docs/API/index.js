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

  var patterns = new SheetSVG(2, "height='90mm' width='100mm'")
  patterns.add("586-,-789,2111,-4-4", "bricks")
  document.getElementById("sheet").innerHTML = patterns.toSvgDoc().trim()

  var q = "patchWidth=8&patchHeight=14"
                      + "&footside=b,-,a,-&footsideStitch=-"
                      + "&tile=831,4-7,-5-&tileStitch=ctct"
                      + "&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2"
  var config = TilesConfig(q)
  d3.select('#proto').html(PrototypeDiagram.create(config))

  var pairDiagram = NewPairDiagram.create(config)
  showGraph(d3.select('#pairs'), pairDiagram, "1px", 200,300)

  var threadDiagram = ThreadDiagram.create(pairDiagram)
  showGraph(d3.select('#threads'), threadDiagram, "2px",450,800)

  var drostePairs = PairDiagram.create("ctct", threadDiagram)
  showGraph(d3.select('#drostePairs'), drostePairs, "1px",600,900)

  var drosteThreads = ThreadDiagram.create("ctct", threadDiagram)
  showGraph(d3.select('#drosteThreads'), drosteThreads, "2px",600,1400)
}
function showGraph(container, diagram, stroke, width, height) {
    var markers = true // use false for slow devices and IE-11, set them at onEnd
    container.html(D3jsSVG.render(diagram, stroke, markers, width, height))
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
        return D3jsSVG.pathDescription(l, s.x, s.y, t.x, t.y)
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
      .force("center", d3.forceCenter(width/2, height/2))
      .alpha(0.0035)
      .on("tick", onTick)
}
