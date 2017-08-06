function setVisibility() {

  var diagonal = document.getElementById("matrixDiagonal").checked
  var values = document.getElementById("tileDimension").value.split("x")
  var height = values[0]
  var width = values[1]
  for (c = 0; c < 8; c++) {
    for (r = 0; r < 4; r++) {
        setEnabled(
          "r"+(r+1)+"-c"+(c+1),
          c < width && r < height && (!diagonal || c%2 == r%2)
        )
    }
  }
}
function setEnabled(id, enabled) {
  var x = document.getElementById(id)
  x.className = (enabled
                ? x.className.replace("hide","show")
                : x.className.replace("show","hide")
                )
  x.disabled = !enabled
}
