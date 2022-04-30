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

  document.getElementById("sheet").innerHTML = new SheetSVG(
       2, // The number of generated rows. A landscape A4 can contain 3 columns and 2 rows.
       "height='90mm' width='100mm'"// Attributes for the SVG root element. Defaults to landscape A4.
       // an optional third argument: id-prefix in case of multiple free floating patterns on an html page
  ).add("5831,-4-7", "bricks")
   .toSvgDoc()
   .trim()

  // compare with the form fields on https://d-bl.github.io/GroundForge/tiles
  // and the address created by the link button
  // note that the bold elements in the prototype diagrams are (hidden) form fields
  var q = "patchWidth=8&patchHeight=14"
                      + "&b1=cr&c1=ctctc&d1=clclc&b2=ctt&d2=cttl&c3=ctctll"
                      + "&footside=b,-,a,-&footsideStitch=-"
                      + "&tile=831,4-7,-5-&tileStitch=ctct"
                      + "&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2"
  var config = TilesConfig(q)
  d3.select('#proto').html(PrototypeDiagram.create(config))

  var pairDiagram = NewPairDiagram.create(config)
  var primaryPairSvg = PairSvg.render(config, 350,500)
  d3.select('#pairsStatic').html(primaryPairSvg)
  d3.select('#pairs').html(primaryPairSvg)
  nudge(d3.select('#pairs'), pairDiagram, 230, 220)

  var threadDiagram = ThreadDiagram.create(pairDiagram)
  showGraph(d3.select('#threads'), threadDiagram, "2px",520,800, 2, config, 0.1)

  // for "ctct" alternatives see:
  // https://d-bl.github.io/GroundForge-help/Replace
  var drostePairs = PairDiagram.create("twist=ct\nb24=c34=c35=d25=ctct", threadDiagram)
  showGraph(d3.select('#drostePairs'), drostePairs, "2px",460,850, 2, config, 0.1)

  var drosteThreads = ThreadDiagram.create(drostePairs)
  showGraph(d3.select('#drosteThreads'), drosteThreads, "2px",1600,2200, 4, config, 0.1)

  var threadSegments = d3.selectAll("#threads .thread8")
  threadSegments.style("stroke", "#F00")
  threadSegments.filter(".node").style("fill", "#F00")
}
function showGraph(container, diagram, stroke, width, height, scale, config, transparency) {
  var markers = true // use false for slow devices and IE-11, set them at onEnd
  container.html(DiagramSvg.render(diagram, stroke, markers, width, height, transparency))
  nudge(container, diagram, width, height)
}
function nudge(container, diagram, width, height) {
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
      return DiagramSvg.pathDescription(l, s.x, s.y, t.x, t.y)
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
