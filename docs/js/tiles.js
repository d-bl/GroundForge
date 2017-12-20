function setVisibility() {

  var matrix = document.getElementById("matrix").value
  var matrixLines = matrix.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = matrixLines.length
  var cols = matrixLines[0].length

  var shiftRowsSE = document.getElementById('shiftRowsSE').value * 1
  var shiftColsSE = document.getElementById('shiftColsSE').value * 1
  var shiftRowsSW = document.getElementById('shiftRowsSW').value * 1
  var shiftColsSW = document.getElementById('shiftColsSW').value * 1

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
  if (shiftColsSE == 0 || shiftRowsSE == 0 && (shiftColsSW == 0 || shiftRowsSW == 0))
    // checkers and bricks without overlap, TODO omit once the else part is completed
    for (var r = 0; r < 12; r++) {
      for (var c = 0; c < 12; c++) {
        var colOffset = Math.floor(r/rows) * shiftColsSE
        var rowOffset = Math.floor(c/cols) * shiftRowsSE
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
      for (var j=-12 ; j<12 ; j++) // parallel diagonals
        for (var r=0; r+(i*shiftRowsSE)+(j*shiftRowsSW) < 12 && r<rows; r++)
          for (var c=0 ; c+(i*shiftColsSE)+(j*shiftColsSW) < 12 && c<cols; c++) {
            // t in rt/ct stands for target cell
            var rt = r+(i*shiftRowsSE)+(j*shiftRowsSW)
            var ct = c+(i*shiftColsSE)+(j*shiftColsSW)
            if (rt >= 0 && ct >=0)
              setNode(rt, ct, matrixLines[r][c], i==0 && j==0)
          }
  }

  // href of go button
  var link = document.getElementById("link")
  var oldTiling = "notYetImplemented"
  if (shiftColsSW == 0 && shiftRowsSW == 0  && shiftRowsSE == 0)
    if (shiftColsSE == 0) tiling = "checker"
    else oldTiling = shiftCols * 2 == matrixLines[0].length
  if (tiling == "notYetImplemented")
    link.style = "display:none"
  else {
    link.style = "display:inline-block"
    link.href = "../index.html" +
      "?m=" + matrixLines.join(",") +
      ";" + oldTiling + ";12;12;0;2" +
      "&shiftColsSE=" + shiftColsSE +
      "&shiftRowsSE=" + shiftRowsSE +
      "&shiftColsSW=" + shiftColsSW +
      "&shiftRowsSW=" + shiftRowsSW +
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
    document.getElementById('shiftColsSE').value = shift
    document.getElementById('shiftRowsSE').value = 0
    document.getElementById('shiftColsSW').value = 0
    document.getElementById('shiftRowsSW').value = 0
  } else {
    document.getElementById('shiftColsSE').value = 0
    document.getElementById('shiftRowsSE').value = shift
    document.getElementById('shiftColsSW').value = 0
    document.getElementById('shiftRowsSW').value = 0
  }
  setVisibility()
}
function sample(matrix, shiftColsSE, shiftRowsSE, shiftColsSW, shiftRowsSW) {
  document.getElementById('matrix').value = matrix
  document.getElementById('shiftColsSE').value = shiftColsSE
  document.getElementById('shiftRowsSE').value = shiftRowsSE
  document.getElementById('shiftColsSW').value = shiftColsSW
  document.getElementById('shiftRowsSW').value = shiftRowsSW
  setVisibility()
}

