function setVisibility() {

  var matrix = document.getElementById("matrix").value
  var matrixLines = matrix.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = Math.min(8,matrixLines.length)

  var stitches = ""
  var r = 0
  var c = 0
  var shiftRows = document.getElementById('shiftRows').value * 1
  var shiftCols = document.getElementById('shiftCols').value * 1
  var overlapRows = document.getElementById('overlapRows').value * 1
  var overlapCols = document.getElementById('overlapCols').value * 1
  for (r = 0; r < 12; r++) {
    var cols = Math.min(12,matrixLines[r%rows].length)
    for (c = 0; c < 12; c++) {
      var enable =  r < rows && c < cols && matrixLines[r][c] != "-"
      var id = "r" + (r + 1) + "-c" + (c + 1)
      if (r<8 && c<8)
       setEnabled(id, enable)
      var svgEl = document.getElementById("svg-" + id)
      var firstTile = r < rows && c < cols
      var colOffset = Math.floor(r/rows) * shiftCols
      var rowOffset = Math.floor(c/cols) * shiftRows
      var hrefValue = matrixLines[(r+rowOffset)%rows][(c+colOffset)%cols]
      svgEl.style = "opacity:" + (!firstTile || hrefValue == "-" ? "0.3;" : "1;")
      svgEl.attributes["xlink:href"].value = "#g" + hrefValue
    }
  }
  var tiling = "notYetImplemented"
  if (shiftCols == 0 && shiftRows == 0) tiling = "checker"
  else if (shiftRows == 0) tiling = shiftCols * 2 == matrixLines[0].length
  document.getElementById("link").href = "../index.html" +
    "?m=" + matrixLines.join(",") +
    ";" + tiling + ";12;12;0;2" +
    "&shiftCols=" + shiftCols + // the new tiling
    "&shiftRows=" + shiftRows + // idem
    "&s1=" + getChosenStitches() +
    ""
}
function getChosenStitches() {
  var els = document.getElementById("stitch-form").elements;
  var kvpairs = [];
  for (i=0; i < els.length; i++) {
     var e = els[i];
     if (e.className != "hide")
       kvpairs.push(e.name + "=" + e.value)
  }
  return kvpairs.join(",")
}
function setEnabled(id, enabled) {
  var x = document.getElementById(id)
  x.className = (enabled
                ? x.className.replace("hide","show")
                : x.className.replace("show","hide")
                )
}
function setPattern (matrix, shift, direction) {
  document.getElementById('matrix').value = matrix
  document.getElementById('shiftCols').value = shift
  document.getElementById('shiftRows').value = 0
  setVisibility()
}

