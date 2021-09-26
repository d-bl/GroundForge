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
  showGraph ("#diagonal", t1, `diagonal&${diagonal}&a1=${t1}`)
  showGraph ("#same", t1, `${hor2x2}&a1=${t1}&a2=${t1}&b1=${t1}&b2=${t1}`)
  if (t1.includes('l') || t1.includes('r')) {
    showGraph ("#alternating", `${t1} -> ${t2}\n${t2} <- ${t1}`, `alternating&${hor2x2}&a1=${t1}&a2=${t2}&b1=${t2}&b2=${t1}`)
    showGraph ("#altRows", `${t1} -> ${t1}\n${t2} <- ${t2}`, `alternating-rows&${hor2x2}&a1=${t1}&a2=${t1}&b1=${t2}&b2=${t2}`)
    showGraph ("#altCols", `${t1} -> ${t2}\n${t1} <- ${t2}`, `alternating-columns&${hor2x2}&a1=${t1}&a2=${t2}&b1=${t1}&b2=${t2}`)
  }
}
function showGraph(id, caption, q) {

  // model

  var config = TilesConfig(q)
  var diagram = ThreadDiagram.create(NewPairDiagram.create(config))
  var nodeDefs = diagram.jsNodes()
  var linkDefs = diagram.jsLinks()//can't inline

  // render

  var scale = 2
  var height = 200
  var width = 200
  var stroke = "2px"
  var markers = false // use true for pair diagrams on fast devices and not IE-11
  var fig = d3.select(`#diagrams`).append("figure")
  var container = fig.append("div")
  container.html(D3jsSVG.render(diagram, stroke, markers, width, height))
  var a = fig.append("figcaption").append("pre").append("a")
  a.text(caption)
  a.attr("href",'tiles?' + q)

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
