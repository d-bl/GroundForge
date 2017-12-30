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
          if (rt >= 0 && ct >=0)
            setNode(rt, ct, matrixLines[r][c], stitches["r"+(r+1)+"-c"+(c+1)], i==0 && j==0)
        }

  // collect chosen stitches
  var kvs = []
  var keys = Object.keys(stitches)
  for (var i=0 ; i<keys.length ; i++) {
    var rc = keys[i].split("-")
    var r = rc[0].substr(1) * 1 - 1
    var c = rc[1].substr(1) * 1 - 1
    if (r < rows && c < cols && matrixLines[r][c] != "-") {
      var id = dibl.Stitches().toID(r, c).toUpperCase()
      kvs.push(id + "=" + stitches[keys[i]])
    }
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
function show() {
  var matrixLines = getMatrixLines()
  var stitches = document.getElementById("stitches").innerHTML
  var pairDiagram = dibl.PairDiagram().get(matrixLines.join(" "),tiling(),12,12,0,0,stitches)
  var diagram = dibl.ThreadDiagram().create(pairDiagram)
  showGraph({
    nodes: diagram.jsNodes(),
    links: diagram.jsLinks(),
    diagram: diagram,
    container: "#diagram"
  })
}
function showGraph(args) {
    var svg = dibl.SVG()
    var markers = true // use false for slow devices and IE-11, set them at onEnd
    var container = d3.select(args.container)
    container.node().innerHTML = svg.render(args.diagram, "2px", markers, 300, 260)
    var links = container.selectAll(".link").data(args.links)
    var nodes = container.selectAll(".node").data(args.nodes)
    function moveNode(jsNode) {
        return 'translate('+jsNode.x+','+jsNode.y+')'
    }
    function drawPath(jsLink) {
        var s = jsLink.source
        var t = jsLink.target
        var l = args.diagram.link(jsLink.index)
        return  svg.pathDescription(l, s.x, s.y, t.x, t.y)
    }
    function onTick() {
        links.attr("d", drawPath);
        nodes.attr("transform", moveNode);
        count++
    }
    function onEnd() {
        console.log("time for " + count + " ticks " + (new Date().getTime() - tickStart))
    }
    var count = 0
    var tickStart = new Date().getTime()

    // duplication of src/main/resources/force.js.applyForce(...)
    function strength(link){ return link.weak ? 1 : 50 }
    d3.forceSimulation(args.nodes)
        .force("charge", d3.forceManyBody().strength(-1000))
        .force("link", d3.forceLink(args.links).strength(strength).distance(12).iterations(30))
        .force("center", d3.forceCenter(cx, cy))
        .alpha(0.0035)
        .on("tick", simTicked)
        .on("end", simEnded)
}
function setNode(r, c, arrows, stitch, firstTile) {

  var svgEl = document.getElementById("svg-r" +  + (r + 1) + "-c" + (c + 1))
  var activeNode = firstTile && arrows != "-"

  var color = ""
  if (stitch && stitch != "" && arrows != "-") {
    color = dibl.Stitches().defaultColorValue(stitch)
    if (color == "")
      color = "black"
  }
  svgEl.attributes["xlink:href"].value = "#g" + arrows
  svgEl.attributes["onclick"].value = (activeNode ? "setStitch(this)" : "")
  svgEl.style = "stroke:" + color + ";opacity:" + (activeNode ? "1;" : "0.3;")
  svgEl.innerHTML = (stitch != "-" ? "<title>"+stitch+"</title>" : "")
}
function setStitch(source) {

  var id = source.attributes["id"].value.substr(4)
  selected = prompt("Stitch for " + id, stitches[id])
  if (selected && selected != "") {
    stitches[id] = selected.trim()
    setVisibility()
  }
}
function allStitches(source) {

  selected = prompt("reset all stitches to: ", "ctc")
  if (selected && selected.trim() != "") {
    for (var r=1; r <= maxRows; r++)
      for (var c=1 ; c <= maxCols; c++)
        stitches["r"+r+"-c"+c] = selected.trim()
    setVisibility()
  }
}
function defaultStitches(source) {

  selected = prompt("set remaining stitches to: ", "ctc")
  if (selected && selected.trim() != "") {
    for (var r=1; r <= maxRows; r++)
      for (var c=1 ; c <= maxCols; c++) {
        var id = "r"+r+"-c"+c
        if (!stitches[id] || stitches[id] == "")
          stitches[id] = selected.trim()
      }
    setVisibility()
  }
}
function getMatrixLines() {

  return document.getElementById("matrix").value.toUpperCase().trim().split(/[^-A-Z0-9]+/)
}
function sample(matrix, shiftColsSE, shiftRowsSE, shiftColsSW, shiftRowsSW) {

  document.getElementById('matrix').value = matrix
  document.getElementById('shiftColsSE').value = shiftColsSE
  document.getElementById('shiftRowsSE').value = shiftRowsSE
  document.getElementById('shiftColsSW').value = shiftColsSW
  document.getElementById('shiftRowsSW').value = shiftRowsSW
  setVisibility()
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