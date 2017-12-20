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

  // clear the prototype as there might be gaps between overlapping tiles
  for (var r = 0; r < 12; r++)
    for (var c = 0; c < 12; c++)
      setNode(r, c, "-", true)

  // pattern prototype
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

  // href of go button
  var link = document.getElementById("link")
  var oldTiling = "notYetImplemented"
  if (shiftColsSW == cols && shiftRowsSW == rows && ( (shiftColsSE == -cols && shiftRowsSE == 0)
                                                   || (shiftColsSE == 0     && shiftRowsSE == rows)
                                                    ))
    oldTiling = "checker"
  if (shiftColsSW*2 == cols && shiftRowsSW == rows && shiftColsSE*2 == -cols && shiftRowsSW == rows)
    oldTiling = "bricks"
  if (oldTiling == "notYetImplemented")
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
function sample(matrix, shiftColsSE, shiftRowsSE, shiftColsSW, shiftRowsSW) {
  document.getElementById('matrix').value = matrix
  document.getElementById('shiftColsSE').value = shiftColsSE
  document.getElementById('shiftRowsSE').value = shiftRowsSE
  document.getElementById('shiftColsSW').value = shiftColsSW
  document.getElementById('shiftRowsSW').value = shiftRowsSW
  setVisibility()
}

