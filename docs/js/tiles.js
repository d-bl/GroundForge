var stitches = {}

// number of cloned elements in the built-in SVG
var maxRows = 12
var maxCols = 12

function setVisibility() {

  var matrixLines = getMatrixLines()
  var rows = matrixLines.length
  var cols = matrixLines[0].length

  var shiftRowsSE = document.getElementById('shiftRowsSE').value * 1
  var shiftColsSE = document.getElementById('shiftColsSE').value * 1
  var shiftRowsSW = document.getElementById('shiftRowsSW').value * 1
  var shiftColsSW = document.getElementById('shiftColsSW').value * 1

  // clear the prototype as there might be gaps between overlapping tiles
  for (var r = 0; r < maxRows; r++)
    for (var c = 0; c < maxCols; c++)
      setNode(r, c, "-", "", true)

  // pattern prototype
  for (var i=0 ; i<maxRows ; i++) // tiles in a diagonal
    for (var j=-maxCols ; j<maxCols ; j++) // parallel diagonals
      for (var r=0; r+(i*shiftRowsSE)+(j*shiftRowsSW) < maxRows && r<rows; r++)
        for (var c=0 ; c+(i*shiftColsSE)+(j*shiftColsSW) < maxCols && c<cols; c++) {
          // t in rt/ct stands for target cell
          var rt = r+(i*shiftRowsSE)+(j*shiftRowsSW)
          var ct = c+(i*shiftColsSE)+(j*shiftColsSW)
          if (rt >= 0 && ct >=0) {
            setNode(rt, ct, matrixLines[r][c], stitches["r"+(r+1)+"-c"+(c+1)], i==0 && j==0)
          }
        }

  collectStitches()
}
function collectStitches() {
  var kvs = []
  var keys = Object.keys(stitches)
  for (var i=0 ; i<keys.length ; i++) {
    var rc = keys[i].split("-")
    var r = rc[0].substr(1) * 1 - 1
    var c = rc[1].substr(1) * 1 - 1
    var id = dibl.Stitches().toID(r, c).toUpperCase()
    kvs.push(id + "=" + stitches[keys[i]])
  }
  document.getElementById("stitches").innerHTML = kvs.join(", ")
}
function tiling() {
  var matrixLines = getMatrixLines()
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  var shiftRowsSE = document.getElementById('shiftRowsSE').value * 1
  var shiftColsSE = document.getElementById('shiftColsSE').value * 1
  var shiftRowsSW = document.getElementById('shiftRowsSW').value * 1
  var shiftColsSW = document.getElementById('shiftColsSW').value * 1
  if (shiftColsSE ==  cols && shiftRowsSE == rows &&
   ( (shiftColsSW == -cols && shiftRowsSW == 0)
  || (shiftColsSW == 0     && shiftRowsSW == rows)
   )
  ) return "checker"
  else if (shiftColsSE*2 == cols && shiftRowsSE == rows && shiftColsSW*2 == -cols && shiftRowsSW == rows)
    return "bricks"
  return "other"// meaning: use the new arguments [r/c][SE/SW]
}
function show(matrix, tiling, rows, cols) {

  var stitches = document.getElementById("stitches").innerHTML
  var pairDiagram = dibl.PairDiagram().get(matrix,tiling,rows,cols,0,2,stitches)
  var diagram = dibl.ThreadDiagram().create(pairDiagram)
  var nodeDefs = diagram.jsNodes()
  var linkDefs = diagram.jsLinks()//can't inline
  var container = d3.select("#diagram")
  var svg = dibl.SVG()
  var markers = true // use false for slow devices and IE-11, set them at onEnd
  container.node().innerHTML = svg.render(diagram, "2px", markers, 300, 260)
  var links = container.selectAll(".link").data(linkDefs)
  var nodes = container.selectAll(".node").data(nodeDefs)

  function moveNode(jsNode) {
      return 'translate('+jsNode.x+','+jsNode.y+')'
  }
  function drawPath(jsLink) {
      var s = jsLink.source
      var t = jsLink.target
      var l = diagram.link(jsLink.index)
      return  svg.pathDescription(l, s.x, s.y, t.x, t.y)
  }
  function onTick() {
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
  }

  // duplication of src/main/resources/force.js.applyForce(...)
  function strength(link){ return link.weak ? 1 : 50 }
  d3.forceSimulation(nodeDefs)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", d3.forceLink(linkDefs).strength(strength).distance(12).iterations(30))
    .force("center", d3.forceCenter(150, 125))
    .alpha(0.0035)
    .on("tick", onTick)
}
function setNode(r, c, arrows, stitch, firstTile) {

  var svgEl = document.getElementById("svg-r" + (r + 1) + "-c" + (c + 1))
  if(!svgEl)
    return // after newProto there might be less rows or columns
  var activeNode = firstTile && arrows != "-"

  var color = ""
  if (stitch && stitch != "" && arrows != "-") {
    color = dibl.Stitches().defaultColorValue(stitch)
    if (color == "")
      color = "black"
  }
  svgEl.attributes["xlink:href"].value = "#g" + arrows
  svgEl.style = "stroke:" + color + ";opacity:" + (activeNode ? "1;" : "0.3;")
  svgEl.innerHTML = (stitch != "-" ? "<title>"+stitch+"</title>" : "")
  svgEl.attributes["onclick"].value = (activeNode ? "setStitch(this)" : "")
}
function setStitch(source) {

  var id = source.attributes["id"].value.substr(4)
  selected = askForStitch("Stitch for " + id, stitches[id] ? stitches[id] : "ctc")
  if (selected && selected != "") {
    stitches[id] = selected
    setVisibility()
  }
}
function allStitches() {

  selected = askForStitch("reset all stitches to: ", "")
  if (selected && selected.trim() != "") {
    for (var r=1; r <= maxRows; r++)
      for (var c=1 ; c <= maxCols; c++)
        stitches["r"+r+"-c"+c] = selected
    setVisibility()
  }
}
function defaultStitches() {

  selected = askForStitch("set remaining stitches to: ", "ctc")
  if (selected && selected.trim() != "") {
    for (var r=1; r <= maxRows; r++)
      for (var c=1 ; c <= maxCols; c++) {
        var id = "r"+r+"-c"+c
        if (!stitches[id] || stitches[id] == "")
          stitches[id] = selected
      }
    setVisibility()
  }
}
function askForStitch(s, defaultStitch){
  // making the input valid as early as possible protects against injection when displaying the value
  return dibl.Stitches().makeValid(prompt(s, defaultStitch? defaultStitch : "ctc"), defaultStitch)
}
function getMatrixLines() {

  var footside = toLines(document.getElementById('footside').value)
  var headside = toLines(document.getElementById('headside').value)
  var tile = toLines(document.getElementById('matrix').value)
  return tile
}
function toLines(s) {
  return s.toUpperCase().trim().split(/[^-A-Z0-9]+/)
}
function query() {
  var kvpairs = []
  var els = document.forms[0].elements
  for (i in els) {
     var e = els[i]
     if (e && e.name && e.value)
     kvpairs.push((e.name).replace("matrix","tile") + "=" + (e.value).replace(/\n/g,","))//encodeURIComponent
  }
  return kvpairs.join("&") + "&" + document.getElementById("stitches").innerHTML.replace(/, /g,"&").toLowercase()
}
function newProto() {
  var config = dibl.Config().create(query())
  document.getElementById('clones').innerHTML = dibl.SVG().createPrototype(config)
  maxRows = config.totalRows
  maxCols = config.totalCols
  show(config.encodedMatrix,"checker", maxRows, maxCols)
}
function sample(matrix, shiftColsSE, shiftRowsSE, shiftColsSW, shiftRowsSW, footside, headside, repeatWidth) {

  document.getElementById('repeatWidth').value = repeatWidth ? repeatWidth : (footside?3:12)
  document.getElementById('matrix').value = matrix
  document.getElementById('footside').value = footside ? footside : ""
  document.getElementById('headside').value = headside ? headside : ""
  document.getElementById('shiftColsSE').value = shiftColsSE
  document.getElementById('shiftRowsSE').value = shiftRowsSE
  document.getElementById('shiftColsSW').value = shiftColsSW
  document.getElementById('shiftRowsSW').value = shiftRowsSW
  setVisibility()
  if (footside && headside) newProto()
}
function asChecker() {

  var matrixLines = getMatrixLines()
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  document.getElementById('shiftRowsSE').value = rows
  document.getElementById('shiftColsSE').value = cols
  document.getElementById('shiftRowsSW').value = rows
  document.getElementById('shiftColsSW').value = 0
  setVisibility()
  dibl.Config().create(query())
}
function brickToLeft() {

  document.getElementById('shiftColsSE').value--
  document.getElementById('shiftColsSW').value--
  setVisibility()
}
function brickToRight() {

  document.getElementById('shiftColsSE').value++
  document.getElementById('shiftColsSW').value++
  setVisibility()
}
function brickUp() {

  document.getElementById('shiftRowsSE').value--
  setVisibility()
}
function brickDown() {

  document.getElementById('shiftRowsSE').value++
  setVisibility()
}