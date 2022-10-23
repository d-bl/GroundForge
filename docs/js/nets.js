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
function more(set, button) {
  generate(d3.select('#b').node().value, set, d3.select('#colors').node().checked)
  button.style='display:none'
  // when we have more buttons, check if there is a single distinct value, then all must be none
  // https://stackoverflow.com/questions/33118036/how-to-get-distinct-values-in-d3-js
  if (d3.select('#more2').style('display')=="none" && d3.select('#more4').style('display')=="none")
    d3.select('#more').style('display','none')
  return false
}
function load() {
  const search = window.location.search.replace(/set=./,'')
  const urlParams = new URLSearchParams(search)
  var img = urlParams.get("img")
  if (img) d3.select('#diagrams').append("img")
    .attr("src", '/MAE-gf/images/ctrl/'+img+'.jpg')
    .attr("onload", "this.width/=3;this.onload=null;")
  var b = urlParams.get("b")
  if (b) b = b.toLowerCase().replace(/[^ctlr]/g,"").trim()
  d3.select('#b').node().value = b
  if (!b) b = "crctl"
  generate(b, "", urlParams.has("colors"))
}
function generate (b, set, colors) {
  const d = b.replace(/l/g,"R").replace(/r/g,"L").toLowerCase()
  const p = b.split("").reverse().join("")
  const q = d.split("").reverse().join("")
  const hor2x2 = "tile=88,11&a1=rctctctctt&l2=lctctctctt&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2&patchWidth=10&patchHeight=12&headside=x,7&footside=4,x"
  const diamond = "tile=-5-,5-5,-5-&headsideStitch=ctctt&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2&patchWidth=12&patchHeight=12&headside=x,7&footside=4,x"
  const kat = "tile=B-C-,---5,C-B-,-5--,B-C-,---5,C-B-,-5--&shiftColsSW=0&shiftRowsSW=8&shiftColsSE=4&shiftRowsSE=8&patchWidth=17&patchHeight=18&footside=x,4,x,x&headside=x,x,x,7&footsideStitch=ctctctctl&headsideStitch=ctctctctr"
  const weavingKat = "tile=-5---5--,6v9v6v9v,---5---5,2z0z2z0z&headsideStitch=ctct&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=8&shiftRowsSE=4&patchWidth=15&patchHeight=16&footside=rx,r8,x4,11&footside=rx,r8,x4,11&headside=xx,88,7r,1r&footsideStitch=ctct&a2=ctctctctll&headsideStitch=ctct&o4=ctctctctrr"
  d3.select('#b').node().value = b
  d3.select('#mb').text(b)
  d3.select('#md').text(d)
  d3.select('#mp').text(p)
  d3.select('#mq').text(q)
  d3.select('#legend').text(`${ b == p ? "b=p , q=d, " : ""} ${ b == d ? "b=d, q=p" : ""}`)

// https://jo-pol.github.io/GroundForge/tiles?patchWidth=11&patchHeight=16&h1=ctc&d1=ctc&a1=ctct&o2=ctct&n2=ctct&i2=ctc&g2=ctc&e2=ctc&c2=ctc&b2=ctct&a2=ctctctctll&o3=ctct&n3=ctct&j3=ctc&f3=ctc&b3=ctct&o4=ctctctctrr&n4=ctct&i4=ctc&g4=ctc&e4=ctc&c4=ctc&b4=ctct&a4=ctct&footside=rx,r8,x4,11&tile=-5---5--,6v9v6v9v,---5---5,2z0z2z0z&headside=xx,88,7r,1r&footsideStitch=ctct&tileStitch=ctc&headsideStitch=ctct&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=8&shiftRowsSE=4

  if (b == d && b == p)
    d3.select('#more').style('display', "none")
  if (b == d || b == p)
    d3.select('#more4').style('display', "none")

  if (!set) {
    d3.select('#colors').node().checked = colors
    showGraph ("diamond", `a1=${b}&n2=${b}&d2=${b}&b2=${b}&c3=${b}&c1=${b}&${diamond}`)
    showGraph ("Paris / kat", `d1=${b}&b1=${b}&e2=${b}&a2=${b}&d3=${b}&b3=${b}&s4=${b}&c4=${b}&d5=${b}&b5=${b}&e6=${b}&d7=${b}&b7=${b}&c8=${b}&${kat}`)
    showGraph ("weaving kat", `h1=${b}&d1=${b}&a1=${b}&s2=${b}&r2=${b}&i2=${b}&g2=${b}&e2=${b}&c2=${b}&b2=${b}&a2=${b}&s3=${b}&r3=${b}&j3=${b}&f3=${b}&b3=${b}&s4=${b}&r4=${b}&i4=${b}&g4=${b}&e4=${b}&c4=${b}&b4=${b}&a4=${b}&${weavingKat}`)
    showGraph ("bb ->\nbb <-", `b1=${b}&c1=${b}&b2=${b}&c2=${b}&${hor2x2}`)
  }
  if (set == "2") {
    if (b != d) {
      showGraph ("bb ->\ndd <-", `b1=${b}&c1=${b}&b2=${d}&c2=${d}&${hor2x2}`)
      showGraph ("bd ->\nbd <-", `b1=${b}&c1=${d}&b2=${b}&c2=${d}&${hor2x2}`)
      showGraph ("bd ->\ndb <-", `b1=${b}&c1=${d}&b2=${d}&c2=${b}&${hor2x2}`)
    }
    if (b != p) {
      showGraph ("bb ->\npp <-", `b1=${b}&c1=${b}&b2=${p}&c2=${p}&${hor2x2}`)
      showGraph ("bp ->\nbp <-", `b1=${b}&c1=${p}&b2=${b}&c2=${p}&${hor2x2}`)
      showGraph ("bp ->\npb <-", `b1=${b}&c1=${p}&b2=${p}&c2=${b}&${hor2x2}`)
    }
    if (b != q) {
      showGraph ("bb ->\nqq <-", `b1=${b}&c1=${b}&b2=${q}&c2=${q}&${hor2x2}`)
      showGraph ("bq ->\nbq <-", `b1=${b}&c1=${q}&b2=${b}&c2=${q}&${hor2x2}`)
      showGraph ("bq ->\nqb <-", `b1=${b}&c1=${q}&b2=${q}&c2=${b}&${hor2x2}`)
    }
  }
  if (set == "4") {
    showGraph ("bd ->\npq <-", `b1=${b}&c1=${d}&b2=${p}&c2=${q}&${hor2x2}`)
    showGraph ("bd ->\nqp <-", `b1=${b}&c1=${d}&b2=${q}&c2=${p}&${hor2x2}`)
    showGraph ("bp ->\ndq <-", `b1=${b}&c1=${p}&b2=${d}&c2=${q}&${hor2x2}`)
    showGraph ("bp ->\nqd <-", `b1=${b}&c1=${p}&b2=${q}&c2=${d}&${hor2x2}`)
    showGraph ("bq ->\ndp <-", `b1=${b}&c1=${q}&b2=${d}&c2=${p}&${hor2x2}`)
    showGraph ("bq ->\npd <-", `b1=${b}&c1=${q}&b2=${p}&c2=${d}&${hor2x2}`)
  }
  setColors(colors)
}
function setColors(colors) {
  if(!colors) {
    d3.select('#color-hint').style("display", "none")
    d3.selectAll('.node').style("opacity","0")
    d3.select('#pairs').attr("src","images/dots-legend-without.png")
  } else {
    d3.select('#color-hint').style("display", "inline")
    d3.selectAll('.node').style("opacity","0.2")
    d3.select('#pairs').attr("src","images/dots-legend.png")

    d3.selectAll('.ct-b1, .ct-c4, .ct-c3, .ct-d7, .ct-g2').style("fill","#0000FF")
    d3.selectAll('.ct-b2, .ct-d1, .ct-b3, .ct-d5, .ct-b7, .ct-f3, .ct-i4, .ct-i2').style("fill","#FF0000")

    d3.selectAll('.ct-c1, .ct-e2, .ct-b5, .ct-j3, .ct-g4').style("fill","#00FFFF")
    d3.selectAll('.ct-c1, .ct-e2, .ct-b5, .ct-j3, .ct-g4').style("opacity","0.25")

    d3.selectAll('.ct-c2, .ct-e4, .ct-d3, .ct-e6, .ct-c8, .ct-h1, .ct-d2').style("fill","#00FF00")
    d3.selectAll('.ct-c2, .ct-e4, .ct-d3, .ct-e6, .ct-c8, .ct-h1, .ct-d2').style("opacity","0.3")
  }
  d3.selectAll('.bobbin').style("opacity","1")
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
  const stroke = "3px"
  const markers = false // use true for pair diagrams on fast devices and other browsers than IE-11
  const svg = DiagramSvg.render(diagram, stroke, markers, width, height)
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
      return DiagramSvg.pathDescription(l, s.x, s.y, t.x, t.y)
  }
  var count = 0
  function onTick() {
      if ((count++ % 3) != 0) return;
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
    .alphaDecay(0.03)
    .on("tick", onTick)
}
