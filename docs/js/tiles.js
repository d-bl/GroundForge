function setVisibility() {

  var matrix = document.getElementById("matrix").value
  var matrixLines = matrix.split(/[^-a-zA-Z0-9]+/)
  var rows = Math.min(8,matrixLines.length)

  var stitches = ""
  var r = 0
  var c = 0
  for (r = 0; r < 8; r++) {
    for (c = 0; c < 8; c++) {
      var cols = -1
      if (r < rows) cols = Math.min(8,matrixLines[r].length)
      var enable =  r < rows && c >= 0 && c < cols && matrixLines[r][c] != "-"
      var id = "r" + (r + 1) + "-c" + (c + 1)
      setEnabled(id, enable)
      var svgId = "svgR" + (r + 1) + "-C" + (c + 1)
      var href = document.getElementById(svgId).attributes["xlink:href"]
      if (enable)
        href.value = "#g" + matrixLines[r][c]
      else
        href.value = "#g-"
    }
  }
  // TODO document.getElementById("diagram").innerHTML += "generating diagram"
  document.getElementById("link").href = "javascript:alert('not yet implemented')"
  document.getElementById('patternByName')[0].selected = "selected"
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

