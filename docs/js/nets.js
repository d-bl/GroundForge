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
function stitchChanged(){
  for (set of ['1','2','4','1d','2d','4d']) {
    d3.select('#diagrams'+set).selectAll('*').remove()
    d3.select('#more'+set).style('display', "inline")
  }
  const b = sanitizeStitch(d3.select('#stitchDef').node().value)
  const d = b.replace(/l/g,"R").replace(/r/g,"L").toLowerCase()
  const p = b.split("").reverse().join("")
  const q = d.split("").reverse().join("")
  d3.select('#mb').text(b)
  d3.select('#md').text(d)
  d3.select('#mp').text(p)
  d3.select('#mq').text(q)
}

function sanitizeStitch(b) {
  return b.toLowerCase().replace(/[^ctlr]/g, "").trim();
}

function load() {
  const search = window.location.search.replace(/set=./,'')
  const urlParams = new URLSearchParams(search)
  let img = urlParams.get("img")
  if (img) {
    document.getElementById('sample').addEventListener('error', function() {
      this.style.display = 'none';
    });

    img = '/MAE-gf/images/ctrl/'
        + img.replace(/[^a-zA-Z0-9-_]/g, "")
        +".jpg"
    let sample = document.getElementById("sample");
    sample.style.display = 'inline-block';
    sample.setAttribute("src",img)
  }

  let b = urlParams.get("b") // backward compatible with old links
  if (!b) b = urlParams.get("stitchDef") // new submits
  let stitchDefInput = d3.select('#stitchDef').node();
  stitchDefInput.value = !b ? "crcl": sanitizeStitch(b)
  let previousValue = stitchDefInput.value
  stitchDefInput.addEventListener('keyup', function() {
    let newValue = sanitizeStitch(stitchDefInput.value)
    if (newValue !== previousValue) {
      previousValue = stitchDefInput.value
      stitchChanged()
    }
  })

  d3.select('#colors').node().checked = urlParams.has("colors")
  d3.selectAll('#gallery a').attr("href", function() {
    return this.href + ';stitchWand()'
  })
  generate('1')
}
function generate(set) {
  const b = sanitizeStitch(d3.select('#stitchDef').node().value)
  const d = b.replace(/l/g,"R").replace(/r/g,"L").toLowerCase()
  const p = b.split("").reverse().join("")
  const q = d.split("").reverse().join("")
  // patchWidth=9&patchHeight=11&footside=--,-b,&tile=5-5,-5-,5-5&headside=-,c,&shiftColsSW=-4&shiftRowsSW=2&shiftColsSE=3&shiftRowsSE=1
  const hor2x2  = "patchWidth=16&patchHeight=18&footside=--r-,----,--g-,--r-&tile=n-n-,---,g-g,---&headside=n,r,r,-&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4"
  const diamond = "patchWidth=11&patchHeight=14&footside=----,---b,&tile=5-5,-5-,5-5&headside=-,c,&shiftColsSW=-4&shiftRowsSW=2&shiftColsSE=3&shiftRowsSE=1"
  d3.select('#stitchDef').node().value = b
  d3.select('#mb').text(b)
  d3.select('#md').text(d)
  d3.select('#mp').text(p)
  d3.select('#mq').text(q)

  d3.select('#more'+set).style('display', "none")

  if (!set || set === '1') {
    showGraph (set+'d',"diagonal", `e1=${b}&g1=${b}&e3=${b}&g3=${b}&f2=${b}&${diamond}`)
    showGraph (set,"horizontal",       `e1=${b}&g1=${b}&e3=${b}&g3=${b}&${hor2x2}`)
  }
  const pattern = set.endsWith('d') ? diamond : hor2x2;

  if (set.startsWith('2')) {
    if (b !== d) {
      showGraph(set, "bb\ndd", `e1=${b}&g1=${b}&e3=${d}&g3=${d}&${pattern}`)
      showGraph(set, "bd\nbd", `e1=${b}&g1=${d}&e3=${b}&g3=${d}&${pattern}`)
      showGraph(set, "bd\ndb", `e1=${b}&g1=${d}&e3=${d}&g3=${b}&${pattern}`)
    }
    if (b !== p) {
      showGraph(set, "bb\npp", `e1=${b}&g1=${b}&e3=${p}&g3=${p}&${pattern}`)
      showGraph(set, "bp\nbp", `e1=${b}&g1=${p}&e3=${b}&g3=${p}&${pattern}`)
      showGraph(set, "bp\npb", `e1=${b}&g1=${p}&e3=${p}&g3=${b}&${pattern}`)
    }
    if (b !== d && b!==p) {
      showGraph(set, "bb\nqq", `e1=${b}&g1=${b}&e3=${q}&g3=${q}&${pattern}`)
      showGraph(set, "bq\nbq", `e1=${b}&g1=${q}&e3=${b}&g3=${q}&${pattern}`)
      showGraph(set, "bq\nqb", `e1=${b}&g1=${q}&e3=${q}&g3=${b}&${pattern}`)
    }
    if (b === d && b === p && b === q) {
      d3.select(`#diagrams` + set).append("p").text("b=d=p=q, see diagrams with only b")
    }
  }
  if (set.startsWith('4')) {
    if (b === d && b === p && b === q) {
      d3.select(`#diagrams`+set).append("p").text("b=d=p=q, see diagrams with only b")
    } else if (b === d ) {
      d3.select(`#diagrams`+set).append("p").text("b=d, see diagrams with b+p")
    } else if (b === p ) {
      d3.select(`#diagrams`+set).append("p").text("b=p, see diagrams with b+d")
    } else {
        showGraph(set, "bd\npq", `e1=${b}&g1=${d}&e3=${p}&g3=${q}&${pattern}`)
        showGraph(set, "bd\nqp", `e1=${b}&g1=${d}&e3=${q}&g3=${p}&${pattern}`)
        showGraph (set,"bp\nqd", `e1=${b}&g1=${p}&e3=${q}&g3=${d}&${pattern}`)
        showGraph (set,"bq\npd", `e1=${b}&g1=${q}&e3=${p}&g3=${d}&${pattern}`)
        showGraph(set, "bp\ndq", `e1=${b}&g1=${p}&e3=${d}&g3=${q}&${pattern}`)
        showGraph(set, "bq\ndp", `e1=${b}&g1=${q}&e3=${d}&g3=${p}&${pattern}`)
    }
  }
  setColors()
}
function setColors() {
  const colors = d3.select('#colors').node().checked
  if(!colors) {
    d3.selectAll('.node').style("opacity","0")
    d3.select('#pairs').attr("src","images/dots-legend-without.png")
  } else {
    d3.select('#pairs').attr("src","images/dots-legend.svg")
    d3.selectAll('.ct-e1').style("fill", '#0571b0ff').style("opacity", "0.5")
    d3.selectAll('.ct-e3').style("fill", '#ca0020ff').style("opacity", "0.5")
    d3.selectAll('.ct-g1').style("fill", '#f4a582ff').style("opacity", "0.5")
    d3.selectAll('.ct-g3').style("fill", '#92c5deff').style("opacity", "0.5")
    d3.selectAll('.bobbin').style("opacity","0")
  }
}
function showGraph(set, caption, q) {

  // model

  const config = TilesConfig(q)
  const diagram = ThreadDiagram.create(NewPairDiagram.create(config))

  // render

  const height = 250
  const width = 250
  const stroke = "3px"
  const markers = false // use true for pair diagrams on fast devices and other browsers than IE-11
  const svg = DiagramSvg.render(diagram, stroke, markers, width, height)
  const fig = d3.select(`#diagrams`+set).append("figure")
  const container = fig.append("div")
  container.html(svg.replace("<g>","<g transform='scale(0.6,0.6)'>"))
  fig.append("figcaption").append("pre").append("a")
     .text(caption).attr("href",'stitches.html?' + q).attr("target", '_blank')
  nudgeDiagram(container.select("svg"))
}
