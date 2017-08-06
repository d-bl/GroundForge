function setVisibility() {

  var matrixLines = document.getElementById("matrix").value.split("%0A")
  var diagonal = document.getElementById("matrix").value.startsWith("-5")
  var values = document.getElementById("tileDimension").value.split("x")
  var height = values[0]
  var width = values[1]
  var stitches = ""
  var m = []
  for (r = 0; r < 4; r++) {
    if (r < height)
      m[r] = matrixLines[r].substring(0,width)
    for (c = 0; c < 8; c++) {
      var enable =  c < width && r < height && (!diagonal || c%2 == r%2)
      var id = "r" + (r + 1) + "-c" + (c + 1)
      setEnabled(id, enable)
      if (enable)
        stitches += document.getElementById(id).name
            + "=" + document.getElementById(id).value + " "
    }
  }
  document.getElementById("link").href = "index.html?m=" + m
  + ";" + document.getElementById("tiling").value
  + ";8;8;0;0"
  + "&s1=" + stitches
  + "&s2=&s3=&"
}
function setEnabled(id, enabled) {
  var x = document.getElementById(id)
  x.className = (enabled
                ? x.className.replace("hide","show")
                : x.className.replace("show","hide")
                )
  x.disabled = !enabled
}
