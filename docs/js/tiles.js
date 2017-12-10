function setVisibility() {

  var matrix = document.getElementById("matrix").value
  var matrixLines = matrix.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = matrixLines.length
  var cols = matrixLines[0].length

  var shiftRows = document.getElementById('shiftRows').value * 1
  var shiftCols = document.getElementById('shiftCols').value * 1
  var overlapRows = document.getElementById('overlapRows').value * 1
  var overlapCols = document.getElementById('overlapCols').value * 1

  // dis/en-able stitches
  for (var r = 0; r < 8; r++) {
    for (var c = 0; c < 8; c++) {
      var enable =  r < rows && c < cols && matrixLines[r][c] != "-"
      var x = document.getElementById(id(r,c))
      x.className = (enable
                    ? x.className.replace("hide","show")
                    : x.className.replace("show","hide")
                    )
    }
  }

  // pattern prototype
  if (shiftCols == 0 || shiftRows == 0)
    for (var r = 0; r < 12; r++) {
      for (var c = 0; c < 12; c++) {
        var colOffset = Math.floor(r/rows) * shiftCols
        var rowOffset = Math.floor(c/cols) * shiftRows
        var hrefValue = matrixLines[(r+rowOffset)%rows][(c+colOffset)%cols]
        setNode(r, c, hrefValue, r < rows && c < cols)
      }
    }
  else
    for (var r = 0; r < 12; r++) {
      for (var c = 0; c < 12; c++) {
        // TODO
        var hrefValue = (r<rows && c<cols ? matrixLines[r%rows][c%cols] : "-")
        setNode(r, c, hrefValue, r < rows && c < cols)
      }
    }

  // href of go button
  var link = document.getElementById("link")
  var tiling = "notYetImplemented"
  if (shiftCols == 0 && shiftRows == 0) tiling = "checker"
  else if (shiftRows == 0) tiling = shiftCols * 2 == matrixLines[0].length
  if (tiling == "notYetImplemented")
    link.style = "display:none"
  else {
    link.style = "display:inline-block"
    link.href = "../index.html" +
      "?m=" + matrixLines.join(",") +
      ";" + tiling + ";12;12;0;2" +
      "&shiftCols=" + shiftCols + // the new tiling
      "&shiftRows=" + shiftRows + // idem
      "&s1=" + getChosenStitches() +
      ""
  }
}
function setNode(r, c, val, firstTile) {
  var svgEl = document.getElementById("svg-" + id(r,c))
  svgEl.style = "opacity:" + (!firstTile || val == "-" ? "0.3;" : "1;")
  svgEl.attributes["xlink:href"].value = "#g" + val
}
function id(r, c) {
  return "r" + (r + 1) + "-c" + (c + 1)
}
function getChosenStitches() {
  var els = document.getElementById("stitch-form").elements;
  var kvpairs = [];
  for (var i=0; i < els.length; i++) {
     var e = els[i];
     if (e.className != "hide")
       kvpairs.push(e.name + "=" + e.value)
  }
  return kvpairs.join(",")
}
function setPattern (matrix, shift, direction) {
  document.getElementById('matrix').value = matrix
  if (direction == "cols") {
    document.getElementById('shiftCols').value = shift
    document.getElementById('shiftRows').value = 0
    document.getElementById('overlapCols').value = 0
    document.getElementById('overlapRows').value = 0
  } else {
    document.getElementById('shiftCols').value = 0
    document.getElementById('shiftRows').value = shift
    document.getElementById('overlapCols').value = 0
    document.getElementById('overlapRows').value = 0
  }
  setVisibility()
}
function sample() {
  document.getElementById('matrix').value = "889C 468- 468D 2748  17-4"
  document.getElementById('shiftCols').value = 1
  document.getElementById('shiftRows').value = 2
  document.getElementById('overlapCols').value = 1
  document.getElementById('overlapRows').value = 2
  setVisibility()
}

