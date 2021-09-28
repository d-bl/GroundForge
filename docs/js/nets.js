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
  var b = window.location.search.substr(1).toLowerCase()
  var d = b.replace("l","R").replace("r","L").toLowerCase()
  var p = b.split("").reverse().join("")
  var q = d.split("").reverse().join("")
  var hor2x2 = "tile=88,11&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2&patchWidth=10&patchHeight=12&headside=x,7&footside=4,x"
  var diagonal = "tile=5&shiftColsSW=-1&shiftRowsSW=1&shiftColsSE=1&shiftRowsSE=1&patchWidth=10&patchHeight=12&headside=7,x&footside=x,4"
  var paris = "tile=B-C-,---5&t&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2&patchWidth=12&patchHeight=18"
  var honeycomb = "tile=-5--,6v9v,---5,2z0z&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4"
  showGraph ("diagonal\npair diagram", `diagonal&${diagonal}&b1=${b}`)
  showGraph ("paris", `paris&${paris}&tileStitch=${b}`)
  showGraph ("honeycomb", `honeycomb&${honeycomb}&tileStitch=${b}`)
  showGraph ("bb ->\nbb <-", `${hor2x2}&b1=${b}&b2=${b}&c1=${b}&c2=${b}`)
  if (b != d) {
    showGraph ("bd ->\ndb <-", `alternating&${hor2x2}&b1=${b}&b2=${d}&c1=${d}&c2=${b}`)
    showGraph ("bb ->\ndd <-", `alternating-rows&${hor2x2}&b1=${b}&b2=${b}&c1=${d}&c2=${d}`)
    showGraph ("bd ->\nbd <-", `alternating-columns&${hor2x2}&b1=${b}&b2=${d}&c1=${b}&c2=${d}`)
  }
  if (b != d || b!= p) {
    // TODO what if only b-p are different stitches?
    showGraph ("bd ->\npq <-", `alternating-columns&${hor2x2}&b1=${b}&b2=${d}&c1=${p}&c2=${q}`)
    d3.select(`#diagrams`).append("p").text(`Mirrored stitches: b=${b}, d=${d}, p=${p}, q=${q}.`)
  }
}
function showGraph(caption, q) {

  // model

  var config = TilesConfig(q)
  var diagram = ThreadDiagram.create(NewPairDiagram.create(config))
  var nodeDefs = diagram.jsNodes()
  var linkDefs = diagram.jsLinks()//can't inline

  // render

  var scale = 2
  var height = 180
  var width = 180
  var stroke = "2px"
  var markers = false // use true for pair diagrams on fast devices and other browsers than IE-11
  var svg = D3jsSVG.render(diagram, stroke, markers, width, height)
  var fig = d3.select(`#diagrams`).append("figure")
  var container = fig.append("div")
  container.html(svg.replace("<g>","<g transform='scale(0.5,0.5)'>"))
  fig.append("figcaption").append("pre").append("a")
     .text(caption).attr("href",'tiles?' + q).attr("target", '_blank')

  // nudge nodes with force graph of the  D3js library
  // TODO the rest of this function is found in other scripts too,
  //  extract into a single source for more functionality and stay in sync

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
