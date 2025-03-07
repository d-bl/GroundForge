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
function stitchWand(){
  d3.select('#diagrams1').selectAll('*').remove()
  d3.select('#diagrams2').selectAll('*').remove()
  d3.select('#diagrams4').selectAll('*').remove()
  d3.select('#more1').style('display', "inline")
  d3.select('#more2').style('display', "inline")
  d3.select('#more4').style('display', "inline")
  const b = d3.select('#stitchDef').node().value.toLowerCase().replace(/[^ctlr]/g,"").trim()
  const d = b.replace(/l/g,"R").replace(/r/g,"L").toLowerCase()
  const p = b.split("").reverse().join("")
  const q = d.split("").reverse().join("")
  d3.select('#mb').text(b)
  d3.select('#md').text(d)
  d3.select('#mp').text(p)
  d3.select('#mq').text(q)
}
function load() {
  const search = window.location.search.replace(/set=./,'')
  const urlParams = new URLSearchParams(search)
  var img = urlParams.get("img")
  if (img) d3.select('#diagrams1').append("img")
    .attr("src", '/MAE-gf/images/ctrl/'+img+'.jpg')
    .attr("onload", "this.width/=3;this.onload=null;")
  var b = urlParams.get("b")
  if (b) b = b.toLowerCase().replace(/[^ctlr]/g,"").trim()
  if (!b) b = "ctcl"
  d3.select('#stitchDef').node().value = b
  d3.select('#stitchDef').attr("onchange", "stitchWand()")
  generate(1, urlParams.has("colors"))
}
function generate(set, colors) {
  const b = (d3.select('#stitchDef').node().value)
  const d = b.replace(/l/g,"R").replace(/r/g,"L").toLowerCase()
  const p = b.split("").reverse().join("")
  const q = d.split("").reverse().join("")
  const hor2x2 = "tile=88,11&a1=rctctctctt&l2=lctctctctt&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2&patchWidth=10&patchHeight=12&headside=x,7&footside=4,x"
  const diamond = "tile=-5-,5-5,-5-&a1=ctctctl&n2=ctctctr&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2&patchWidth=12&patchHeight=12&headside=x,7&footside=4,x"
  const kat = "tile=B-C-,---5,C-B-,-5--,B-C-,---5,C-B-,-5--&shiftColsSW=0&shiftRowsSW=8&shiftColsSE=4&shiftRowsSE=8&patchWidth=17&patchHeight=18&footside=x,4,x,x&headside=x,x,x,7&a2=ctctctctll&s4=ctctctctrr"
  const weavingKat = "tile=-5---5--,6v9v6v9v,---5---5,2z0z2z0z&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=8&shiftRowsSE=4&patchWidth=17&patchHeight=16&footside=rx,r8,x4,11&headside=7X,88,xr,1r&a2=ctctctctll&u4=ctctctctrr"
  d3.select('#stitchDef').node().value = b
  d3.select('#mb').text(b)
  d3.select('#md').text(d)
  d3.select('#mp').text(p)
  d3.select('#mq').text(q)

// https://jo-pol.github.io/GroundForge/tiles?patchWidth=11&patchHeight=16&h1=ctc&d1=ctc&a1=ctct&o2=ctct&n2=ctct&i2=ctc&g2=ctc&e2=ctc&c2=ctc&b2=ctct&a2=ctctctctll&o3=ctct&n3=ctct&j3=ctc&f3=ctc&b3=ctct&o4=ctctctctrr&n4=ctct&i4=ctc&g4=ctc&e4=ctc&c4=ctc&b4=ctct&a4=ctct&footside=rx,r8,x4,11&tile=-5---5--,6v9v6v9v,---5---5,2z0z2z0z&headside=xx,88,7r,1r&footsideStitch=ctct&tileStitch=ctc&headsideStitch=ctct&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=8&shiftRowsSE=4

  d3.select('#more'+set).style('display', "none")

  if (!set || set == 1) {
    d3.select('#colors').node().checked = colors
    showGraph (set,"diamond", `a1=${b}&n2=${b}&d2=${b}&b2=${b}&c3=${b}&c1=${b}&${diamond}`)
    showGraph (set,"Paris / kat", `d1=${b}&b1=${b}&e2=${b}&a2=${b}&d3=${b}&b3=${b}&s4=${b}&c4=${b}&d5=${b}&b5=${b}&e6=${b}&d7=${b}&b7=${b}&c8=${b}&${kat}`)
    showGraph (set,"weaving kat", `h1=${b}&d1=${b}&a1=${b}&s2=${b}&r2=${b}&i2=${b}&g2=${b}&e2=${b}&c2=${b}&b2=${b}&a2=${b}&s3=${b}&r3=${b}&j3=${b}&f3=${b}&b3=${b}&s4=${b}&r4=${b}&i4=${b}&g4=${b}&e4=${b}&c4=${b}&b4=${b}&a4=${b}&${weavingKat}`)
    showGraph (set,"bb ->\nbb <-", `b1=${b}&c1=${b}&b2=${b}&c2=${b}&${hor2x2}`)
  }
  if (set == 2) {
    if (b !== d) {
      showGraph(set, "bb ->\ndd <-", `b1=${b}&c1=${b}&b2=${d}&c2=${d}&${hor2x2}`)
      showGraph(set, "bd ->\nbd <-", `b1=${b}&c1=${d}&b2=${b}&c2=${d}&${hor2x2}`)
      showGraph(set, "bd ->\ndb <-", `b1=${b}&c1=${d}&b2=${d}&c2=${b}&${hor2x2}`)
    }
    if (b !== p) {
      showGraph(set, "bb ->\npp <-", `b1=${b}&c1=${b}&b2=${p}&c2=${p}&${hor2x2}`)
      showGraph(set, "bp ->\nbp <-", `b1=${b}&c1=${p}&b2=${b}&c2=${p}&${hor2x2}`)
      showGraph(set, "bp ->\npb <-", `b1=${b}&c1=${p}&b2=${p}&c2=${b}&${hor2x2}`)
    }
    if (b !== d && b!==p) {
      showGraph(set, "bb ->\nqq <-", `b1=${b}&c1=${b}&b2=${q}&c2=${q}&${hor2x2}`)
      showGraph(set, "bq ->\nbq <-", `b1=${b}&c1=${q}&b2=${b}&c2=${q}&${hor2x2}`)
      showGraph(set, "bq ->\nqb <-", `b1=${b}&c1=${q}&b2=${q}&c2=${b}&${hor2x2}`)
    }
    if (b === d && b === p && b === q) {
      d3.select(`#diagrams` + set).append("p").text("b=d=p=q, see diagrams with only b")
    }
  }
  if (set == 4) {
    if (b === d && b === p && b === q) {
      d3.select(`#diagrams`+set).append("p").text("b=d=p=q, see diagrams with only b")
    } else if (b === d ) {
      d3.select(`#diagrams`+set).append("p").text("b=d, see diagrams with b+p")
    } else if (b === p ) {
      d3.select(`#diagrams`+set).append("p").text("b=p, see diagrams with b+d")
    } else {
        showGraph(set, "bd ->\npq <-", `b1=${b}&c1=${d}&b2=${p}&c2=${q}&${hor2x2}`)
        showGraph(set, "bd ->\nqp <-", `b1=${b}&c1=${d}&b2=${q}&c2=${p}&${hor2x2}`)
        showGraph (set,"bp ->\nqd <-", `b1=${b}&c1=${p}&b2=${q}&c2=${d}&${hor2x2}`)
        showGraph (set,"bq ->\npd <-", `b1=${b}&c1=${q}&b2=${p}&c2=${d}&${hor2x2}`)
        showGraph(set, "bp ->\ndq <-", `b1=${b}&c1=${p}&b2=${d}&c2=${q}&${hor2x2}`)
        showGraph(set, "bq ->\ndp <-", `b1=${b}&c1=${q}&b2=${d}&c2=${p}&${hor2x2}`)
    }
  }
  setColors(colors)
}
function setColors(colors) {
  if(!colors) {
    d3.selectAll('.node').style("opacity","0")
    d3.select('#pairs').attr("src","images/dots-legend-without.png")
  } else {
    d3.select('#color-hint').style("display", "inline")
    d3.select('#pairs').attr("src","images/dots-legend.png")

    d3.selectAll('.ct-b1, .ct-c4, .ct-c3, .ct-d7, .ct-g2')
        .style("fill",'#377eb8').style("opacity","0.4")
    d3.selectAll('.ct-b2, .ct-d1, .ct-b3, .ct-d5, .ct-b7, .ct-f3, .ct-i4, .ct-i2')
        .style("fill",'#e41a1c').style("opacity","0.4")
    d3.selectAll('.ct-c1, .ct-e2, .ct-b5, .ct-j3, .ct-g4')
        .style("fill",'#984ea3').style("opacity","0.4")
    d3.selectAll('.ct-c2, .ct-e4, .ct-d3, .ct-e6, .ct-c8, .ct-h1, .ct-d2')
        .style("fill",'#4daf4a').style("opacity","0.4")

  }
  d3.selectAll('.bobbin').style("opacity","1")
}
function showGraph(nr, caption, q) {

  // model

  const config = TilesConfig(q)
  const diagram = ThreadDiagram.create(NewPairDiagram.create(config))

  // render

  const height = 280
  const width = 280
  const stroke = "3px"
  const markers = false // use true for pair diagrams on fast devices and other browsers than IE-11
  const svg = DiagramSvg.render(diagram, stroke, markers, width, height)
  const fig = d3.select(`#diagrams`+nr).append("figure")
  const container = fig.append("div")
  container.html(svg.replace("<g>","<g transform='scale(0.7,0.7)'>"))
  fig.append("figcaption").append("pre").append("a")
     .text(caption).attr("href",'stitches.html?' + q).attr("target", '_blank')
  nudgeDiagram(container.select("svg"))
}
