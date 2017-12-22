var stitches = {}

function setVisibility() {

  var matrixLines = getMatrixLines()
  var rows = matrixLines.length
  var cols = matrixLines[0].length

  var shiftRowsSE = document.getElementById('shiftRowsSE').value * 1
  var shiftColsSE = document.getElementById('shiftColsSE').value * 1
  var shiftRowsSW = document.getElementById('shiftRowsSW').value * 1
  var shiftColsSW = document.getElementById('shiftColsSW').value * 1

  // clear the prototype as there might be gaps between overlapping tiles
  for (var r = 0; r < 12; r++)
    for (var c = 0; c < 12; c++)
      setNode(r, c, "-", true)

  // pattern prototype
  for (var i=0 ; i<12 ; i++) // tiles in a diagonal
    for (var j=-12 ; j<12 ; j++) // parallel diagonals
      for (var r=0; r+(i*shiftRowsSE)+(j*shiftRowsSW) < 12 && r<rows; r++)
        for (var c=0 ; c+(i*shiftColsSE)+(j*shiftColsSW) < 12 && c<cols; c++) {
          // t in rt/ct stands for target cell
          var rt = r+(i*shiftRowsSE)+(j*shiftRowsSW)
          var ct = c+(i*shiftColsSE)+(j*shiftColsSW)
          if (rt >= 0 && ct >=0)
            setNode(rt, ct, matrixLines[r][c], i==0 && j==0)
        }

  // go button
  var tiling = "other" // meaning: use the new arguments [r/c][SE/SW]
  if (shiftColsSE == cols && shiftRowsSE == rows && ( (shiftColsSW == -cols && shiftRowsSW == 0)
                                                   || (shiftColsSW == 0     && shiftRowsSW == rows)
                                                    ))
    tiling = "checker"
  else if (shiftColsSE*2 == cols && shiftRowsSE == rows && shiftColsSW*2 == -cols && shiftRowsSW == rows)
    tiling = "bricks"
  var stitches = getChosenStitches(matrixLines)
  var link = document.getElementById("link")
  if (tiling == "other")
    link.style = "display:none"
  else {
    link.style = "display:inline-block"
    link.href = "../index.html" +
      "?s2=&s3="+
      "&cSE=" + shiftColsSE +
      "&rSE=" + shiftRowsSE +
      "&cSW=" + shiftColsSW +
      "&rSW=" + shiftRowsSW +
      "&s1=" + stitches +
      "&m=" + matrixLines.join(",") +
      ";" + tiling + ";12;12;0;2" +
      ""
  }
  document.getElementById("stitches").innerHTML = stitches.replace(/,/g,", ")
}
function setNode(r, c, val, firstTile) {

  var id = "r" + (r + 1) + "-c" + (c + 1)
  var svgEl = document.getElementById("svg-" + id)
  var activeNode = firstTile && val != "-"
  svgEl.attributes["xlink:href"].value = "#g" + val
  svgEl.attributes["onclick"].value = (activeNode ? "setStitch(this)" : "")
  svgEl.style = "stroke:#000;opacity:" + (activeNode ? "1;" : "0.3;")
  var stitch = stitches[id]
  svgEl.innerHTML = (stitch != "-" ? "<title>"+stitch+"</title>" : "")
}
function setStitch(source) {

  var id = source.attributes["id"].value.substr(4)
  selected = prompt("Stitch for " + id, stitches[id])
  if (selected) {
    stitches[id] = selected
    setVisibility()
    source.style = "stroke:#d0d;opacity:1"
  }
}
function getChosenStitches(matrixLines) {
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  var kvs = []
  var keys = Object.keys(stitches)
  for (var i=0 ; i<keys.length ; i++) {
    var rc = keys[i].split("-")
    var r = rc[0].substr(1) * 1 - 1
    var c = rc[1].substr(1) * 1 - 1
    if (r < rows && c < cols && matrixLines[r][c] != "-") {
      c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[rc[1].substr(1) * 1 - 1]
      kvs.push(c + (r+1) + "=" + stitches[keys[i]])
    }
  }
  return kvs.join(",")
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