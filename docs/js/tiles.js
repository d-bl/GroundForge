function setVisibility() {

  var matrix = document.getElementById("matrix").value
  var matrixLines = matrix.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = Math.min(8,matrixLines.length)

  var stitches = ""
  var r = 0
  var c = 0
  var shift = document.getElementById('shift').value * 1
  var shiftCols = document.getElementById('cols').checked
  for (r = 0; r < 8; r++) {
    var cols = Math.min(8,matrixLines[r%rows].length)
    for (c = 0; c < 8; c++) {
      var enable =  r < rows && c < cols && matrixLines[r][c] != "-"
      var id = "r" + (r + 1) + "-c" + (c + 1)
      setEnabled(id, enable)
      var svgEl = document.getElementById("svg-" + id)
      var firstTile = r < rows && c < cols
      var colOffset = (!shiftCols ? 0 : Math.floor(r/rows) * shift)
      var rowOffset = (shiftCols ? 0 : Math.floor(c/cols) * shift)
      var hrefValue = matrixLines[(r+rowOffset)%rows][(c+colOffset)%cols]
      svgEl.style = "opacity:" + (!firstTile || hrefValue == "-" ? "0.3;" : "1;")
      svgEl.attributes["xlink:href"].value = "#g" + hrefValue
    }
  }
  document.getElementById("link").href = "../index.html" +
    "?matrix=" + matrixLines.join(",") +
    "&tiles=" + (shift == 0 ? "checker" : ((shift * 2 == matrixLines[0].length ) && shiftCols ? "bricks" : "notYetImplemented" )) +
    "&" + (shiftCols ? "shiftCols=" : "shiftRows=" ) + shift +
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
  document.getElementById('shift').value = shift
  document.getElementById(direction).checked = true
  setVisibility()
}

