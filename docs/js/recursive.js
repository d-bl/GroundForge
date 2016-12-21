var data = [0,1,2,3,4]
function getRadioVal(radioName) {
  var rads = document.getElementsByName(radioName);

  for(var rad in rads) {
    if(rads[rad].checked)
      return rads[rad].value;
  }
  return null;
}
function pairDiagram(value) {
  var p = value.split(";")
  var matrix = p[0]
  var tiling = p[1]
  var nrOfRows = p[2]
  var nrOfCols = p[3]
  var shiftLeft = 0
  var shiftUp = 0
  var stitch = document.getElementById("s1").value
  data[1] = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitch, tiling)
  document.getElementById('d0').innerHTML = ""
  document.getElementById('d1').innerHTML = ""
  diagram.showGraph({
    container: '#d0',
    nodes: data[1].pairNodes(),
    links: data[1].pairLinks()
  })
  diagram.showGraph({
    container: '#d1',
    nodes: data[1].threadNodes(),
    links: data[1].threadLinks()
  })
}
function threadDiagram(n) {
  var stitch = document.getElementById("s" + n).value
  data[n] = dibl.D3Data().get(stitch, data[n-1])
  document.getElementById('d'+n).innerHTML = ""
  diagram.showGraph({
    container: '#d' + n,
    nodes: data[n].threadNodes(),
    links: data[n].threadLinks()
  })
}
