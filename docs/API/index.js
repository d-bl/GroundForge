function load() {

  var matrix = "586- -789 2111 -4-4"
  var nrOfRows = 12
  var nrOfCols = 9
  var shiftLeft = 1
  var shiftUp = 1
  var stitches = 'A3=ctc,D4=ctc'

  var data = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitches, "checker")
  var patterns = new dibl.PatternSheet(2, "height='90mm' width='100mm'")
  patterns.add(matrix, "checker")
  var svg = (patterns.toSvgDoc().trim())

  document.getElementById("sheet").innerHTML = svg
  diagram.showGraph({
    container: '#pairs',
    nodes: data.pairNodes,
    links: data.pairLinks
  })
  diagram.showGraph({
    container: '#threads',
    nodes: data.threadNodes,
    links: data.threadLinks
  })
}
