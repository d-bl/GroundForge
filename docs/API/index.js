function load() {

  var matrix = "586-,-4-5,5-21,-5-7"
  var nrOfRows = 12
  var nrOfCols = 9
  var shiftLeft = 1
  var shiftUp = 1
  var stitches = 'A3=tctc,D4=lctc,B4=rctc,A1=ctc,A2=tctc,C1=tctc,D2=rctc,B2=lctc,C3=ctc,C4=tctc'
  var data = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitches, "checker")
  var patterns = new dibl.PatternSheet(2, "height='210mm' width='297mm'")
  patterns.add(matrix, "checker")
  var svg = (patterns.toSvgDoc().trim())

  document.getElementById("sheet").innerHTML = svg
  diagram.showGraph({
    container: '#pairs',
    nodes: data.pairNodes,
    links: data.pairLinks,
    scale: 0.6,
    transform: 0.6,
    onAnimationEnd: function() { setHref(document.getElementById("dlPair"),'pairs') }
  })
  diagram.showGraph({
    container: '#threads',
    nodes: data.threadNodes,
    links: data.threadLinks,
    scale: 1,
    transform: 1,
    threadColor: '#color',
    palette: 1
  })
}
