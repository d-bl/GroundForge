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
  const urlParams = new URLSearchParams(window.location.search)
  var b = urlParams.get("b")
  if (b) b = b.toLowerCase().replace(/[^ctlr]/g,"").trim()
  if (!b) b = "crctl"
  const d = b.replace(/l/g,"R").replace(/r/g,"L").toLowerCase()
  const p = b.split("").reverse().join("")
  const q = d.split("").reverse().join("")
  const hor2x2 = "tile=88,11&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2&patchWidth=10&patchHeight=12&headside=x,7&footside=4,x"
  const diagonal = "tile=5&shiftColsSW=-1&shiftRowsSW=1&shiftColsSE=1&shiftRowsSE=1&patchWidth=10&patchHeight=12&headside=7,x&footside=x,4"
  const paris = "tile=B-C-,---5&t&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2&patchWidth=12&patchHeight=18"
  const honeycomb = "tile=-5--,6v9v,---5,2z0z&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4"
  d3.select(`#set2`).attr("href",`?b=${b}&set=${2}`)
  d3.select(`#set4`).attr("href",`?b=${b}&set=${4}`)
  d3.select(`#set`).attr("href",`?b=${b}`)

  const set = urlParams.get("set")
  if (!set) {
    showGraph ("diagonal\npair diagram", `b1=${b}&${diagonal}`)
    showGraph ("paris", `tileStitch=${b}&${paris}`)
    showGraph ("honeycomb", `tileStitch=${b}&${honeycomb}`)
    showGraph ("bb ->\nbb <-", `&b1=${b}&c1=${b}&b2=${b}&c2=${b}&${hor2x2}`)
  }
  if (b != d && set != "4") {
    showGraph ("bb ->\ndd <-", `b1=${b}&c1=${b}&b2=${d}&c2=${d}&${hor2x2}`)
    if (set == "2") {
      showGraph ("bd ->\nbd <-", `b1=${b}&c1=${d}&b2=${b}&c2=${d}&${hor2x2}`)
      showGraph ("bd ->\ndb <-", `b1=${b}&c1=${d}&b2=${d}&c2=${b}&${hor2x2}`)
    }
  }
  if (b != p && set != "4") {
    showGraph ("bb ->\npp <-", `b1=${b}&c1=${b}&b2=${p}&c2=${p}&${hor2x2}`)
    if (set == "bbxx") {
      showGraph ("bp ->\nbp <-", `b1=${b}&c1=${p}&b2=${b}&c2=${p}&${hor2x2}`)
      showGraph ("bp ->\npb <-", `b1=${b}&c1=${p}&b2=${p}&c2=${b}&${hor2x2}`)
    }
  }
  if (b != q && set != "4") {
    showGraph ("bb ->\nqq <-", `b1=${b}&c1=${b}&b2=${q}&c2=${q}&${hor2x2}`)
    if (set == "2") {
      showGraph ("bq ->\nbq <-", `b1=${b}&c1=${q}&b2=${b}&c2=${q}&${hor2x2}`)
      showGraph ("bq ->\nqb <-", `b1=${b}&c1=${q}&b2=${q}&c2=${b}&${hor2x2}`)
    }
  }
  if (b != q && set != "2") {
    showGraph ("bd ->\npq <-", `b1=${p}&c1=${d}&b2=${p}&c2=${q}&${hor2x2}`)
  }
  if (set == "4") {
    showGraph ("bd ->\nqp <-", `b1=${p}&c1=${d}&b2=${q}&c2=${p}&${hor2x2}`)
    showGraph ("bp ->\ndq <-", `b1=${b}&c1=${p}&b2=${d}&c2=${q}&${hor2x2}`)
    showGraph ("bp ->\nqd <-", `b1=${b}&c1=${p}&b2=${q}&c2=${d}&${hor2x2}`)
    showGraph ("bq ->\ndp <-", `b1=${p}&c1=${q}&b2=${d}&c2=${p}&${hor2x2}`)
    showGraph ("bq ->\npd <-", `b1=${p}&c1=${q}&b2=${p}&c2=${d}&${hor2x2}`)
  }
  if (b != d || b!= p || true) {
    d3.select(`#legend`).text(`b = ${b}, d = ${d}, p = ${p}, q = ${q} ${ b == p ? "; b=p , q=d" : ""} ${ b == d ? "; b=d, q=p" : ""}`)
  }
}
function showGraph(caption, q) {

  // model

  const config = TilesConfig(q)
  const diagram = ThreadDiagram.create(NewPairDiagram.create(config))
  const nodeDefs = diagram.jsNodes()
  const linkDefs = diagram.jsLinks()//can't inline

  // render

  const height = 180
  const width = 180
  const stroke = "2px"
  const markers = false // use true for pair diagrams on fast devices and other browsers than IE-11
  const svg = D3jsSVG.render(diagram, stroke, markers, width, height)
  const fig = d3.select(`#diagrams`).append("figure")
  const container = fig.append("div")
  container.html(svg.replace("<g>","<g transform='scale(0.5,0.5)'>"))
  fig.append("figcaption").append("pre").append("a")
     .text(caption).attr("href",'tiles?' + q).attr("target", '_blank')

  // nudge nodes with force graph of the  D3js library
  // TODO variations of the rest of this function are found in other scripts too

  const links = container.selectAll(".link").data(linkDefs)
  const nodes = container.selectAll(".node").data(nodeDefs)
  function moveNode(jsNode) {
      return 'translate('+jsNode.x+','+jsNode.y+')'
  }
  function drawPath(jsLink) {
      const s = jsLink.source
      const t = jsLink.target
      const l = diagram.link(jsLink.index)
      return D3jsSVG.pathDescription(l, s.x, s.y, t.x, t.y)
  }
  function onTick() {
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
  }
  const forceLink = d3
    .forceLink(linkDefs)
    .strength(50)
    .distance(12)
    .iterations(30)
  d3.forceSimulation(nodeDefs)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", forceLink)
    .force("center", d3.forceCenter(width/2, height/2))
    .alpha(0.0035)
    .on("tick", onTick)
}
