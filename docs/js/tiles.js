function setVisibility() {

  var matrix = document.getElementById("matrix").value
  var matrixLines = matrix.split(/[^-a-zA-Z0-9]+/)
  var rows = Math.min(8,matrixLines.length)

  var stitches = ""
  var r = 0
  var c = 0
  var noShift = document.getElementById('shift').value == 0
  for (r = 0; r < 8; r++) {
    cols = Math.min(8,matrixLines[r%rows].length)
    for (c = 0; c < 8; c++) {
      var enable =  r < rows && c < cols && matrixLines[r][c] != "-"
      var id = "r" + (r + 1) + "-c" + (c + 1)
      setEnabled(id, enable)
      var svgId = "svgR" + (r + 1) + "-C" + (c + 1)
      var el = document.getElementById(svgId)
      var href = el.attributes["xlink:href"]
      if (enable) {
        href.value = "#g" + matrixLines[r][c]
        el.style = "opacity:1.0;"
      } else if (noShift) {
        href.value = "#g" + matrixLines[r%rows][c%cols]
        el.style = "opacity:0.4;"
      } else {
        href.value = "#g-"
        el.style = "opacity:0.4;"
      }
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

