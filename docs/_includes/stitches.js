function setVisibility() {

  var base = document.getElementById("base").value
  var diagonal = base.startsWith("5") || base.startsWith("-")
  var matrixLines = base.split(";")[0].split("%0D")
  var height = matrixLines.length
  var width = matrixLines[0].length
  var stitches = ""
  var m = []
  for (r = 0; r < 6; r++) {
    for (c = 0; c < 8; c++) {
      var enable =  c < width && r < height && (!diagonal || c%2 == r%2)
      var id = "r" + (r + 1) + "-c" + (c + 1)
      setEnabled(id, enable)
      if (enable)
        stitches += document.getElementById(id).name
            + "=" + document.getElementById(id).value.trim() + " "
    }
  }
  document.getElementById("link").href = "/GroundForge/index.html?m="
   + document.getElementById("base").value
   + stitches
}
function setEnabled(id, enabled) {
  var x = document.getElementById(id)
  x.className = (enabled
                ? x.className.replace("hide","show")
                : x.className.replace("show","hide")
                )
}

