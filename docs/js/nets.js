/*
 Copyright 2022 Jo Pol
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
  var t1 = window.location.search.substr(1).toLowerCase()
  var t2 = t1.replace("l","R").replace("r","L").toLowerCase()
  console.log(`t1=${t1} t2=${t2} `)
  var hor2x2 = "tile=88,11&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2&patchWidth=8&patchHeight=8"
  var diagonal = "tile=5&shiftColsSW=-1&shiftRowsSW=1&shiftColsSE=1&shiftRowsSE=1&patchWidth=8&patchHeight=8"
  showGraph ("#diagonal", `diagonal&${diagonal}&a1=${t1}`)
  showGraph ("#same", `${hor2x2}&a1=${t1}&a2=${t1}&b1=${t1}&b2=${t1}`)
  if (t1.includes('l') || t1.includes('r')) {
    showGraph ("#alternating", `alternating&${hor2x2}&a1=${t1}&a2=${t2}&b1=${t2}&b2=${t1}`)
    showGraph ("#altRows", `alternating-rows&${hor2x2}&a1=${t1}&a2=${t1}&b1=${t2}&b2=${t2}`)
    showGraph ("#altCols", `alternating-columns&${hor2x2}&a1=${t1}&a2=${t2}&b1=${t1}&b2=${t2}`)
  }
}
function showGraph(id, q) {
  d3.select(`${id}Link`).node().href = 'tiles?' + q
  var container = d3.select(id)
  var config = TilesConfig(q)
  var diagram = ThreadDiagram.create(NewPairDiagram.create(config))
  var scale = 2
  var height = 200
  var width = 200
  var stroke = "2px"
  var nodeDefs = diagram.jsNodes()
  var linkDefs = diagram.jsLinks()//can't inline
  var markers = true // use false for slow devices and IE-11, set them at onEnd
  container.html(D3jsSVG.render(diagram, stroke, markers, width, height))

  // nudge nodes with force graph of the  D3js library

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
