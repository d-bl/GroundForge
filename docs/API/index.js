function load() {

  var matrix = "586- -789 2111 -4-4"
  var nrOfRows = 12
  var nrOfCols = 9
  var shiftLeft = 1
  var shiftUp = 1
  var stitches = ''

  var patterns = new dibl.PatternSheet(2, "height='90mm' width='100mm'")
  patterns.add(matrix, "checker")
  var svg = (patterns.toSvgDoc().trim())
  var sheetElement = document.getElementById("sheet")
  sheetElement.innerHTML = svg

  // TODO force redraw

  var start = new Date().getTime()
  var data = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitches, "checker")
  console.log("time for pair diagram " + (new Date().getTime() - start))

  diagram.showGraph({
    container: '#pairs',
    nodes: data.pairNodes(),
    links: data.pairLinks()
  })

  // TODO force redraw

  start = new Date().getTime()
  var nodes = data.threadNodes()
  var links = data.threadLinks()
  console.log("time for pair diagram " + (new Date().getTime() - start))

  diagram.showGraph({
    container: '#threads',
    nodes: nodes,
    links: links
  })
}
