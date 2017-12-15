function setVisibility() {

  var matrix = document.getElementById("matrix").value
  var matrixLines = matrix.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = matrixLines.length
  var cols = matrixLines[0].length

  var shiftRows = document.getElementById('shiftRows').value * 1
  var shiftCols = document.getElementById('shiftCols').value * 1

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
    // checkers and bricks without overlap, TODO omit once the else part is completed
    for (var r = 0; r < 12; r++) {
      for (var c = 0; c < 12; c++) {
        var colOffset = Math.floor(r/rows) * shiftCols
        var rowOffset = Math.floor(c/cols) * shiftRows
        // s in rs/cs stands for source cell
        var rs = (r+rowOffset)%rows
        var cs = (c+colOffset)%cols
        setNode(r, c, matrixLines[rs][cs], r < rows && c < cols)
      }
    }
  else {
    // clear the matrix as the NE and SW parts will not be filled
    for (var r = 0; r < 12; r++)
      for (var c = 0; c < 12; c++)
        setNode(r, c, "-", true)
    // so far just one diagonal of overlapping tiles
    for (var i=0 ; i<12 ; i++) // tiles in a diagonal
      for (var r=0; r+(i*shiftRows) < 12 && r<rows; r++)
        for (var c=0 ; c+(i*shiftCols) < 12 && c<cols; c++) {
          // t in rt/ct stands for target cell
          var rt = r+(i*shiftRows)
          var ct = c+(i*shiftCols)
          setNode(rt, ct, matrixLines[r][c], rt < rows && ct < cols)
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
  } else {
    document.getElementById('shiftCols').value = 0
    document.getElementById('shiftRows').value = shift
  }
  setVisibility()
}
function sample(matrix, shiftRows, shiftCols) {
  document.getElementById('matrix').value = matrix
  document.getElementById('shiftCols').value = shiftCols
  document.getElementById('shiftRows').value = shiftRows
  setVisibility()
}

