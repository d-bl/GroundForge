var stitches = {}

// number of cloned elements in the built-in SVG
var maxRows = 12
var maxCols = 12

function showProto() {

  var config = dibl.Config().create(query())
  document.getElementById('clones').innerHTML = dibl.SVG().createPrototype(config)
  maxRows = config.totalRows
  maxCols = config.totalCols
}
function collectStitches() {
  var kvs = []
  var keys = Object.keys(stitches)
  for (var i=0 ; i<keys.length ; i++) {
    var rc = keys[i].split("-")
    var r = rc[0].substr(1) * 1 - 1
    var c = rc[1].substr(1) * 1 - 1
    var id = dibl.Stitches().toID(r, c).toUpperCase()
    kvs.push(id + "=" + stitches[keys[i]])
  }
  document.getElementById("stitches").innerHTML = kvs.join(", ")
}
function showThreadDiagram() {

  var config = dibl.Config().create(query())
  var stitches = document.getElementById("stitches").innerHTML
  var pairDiagram = dibl.PairDiagram().get(config.encodedMatrix,"checker",config.totalRows,config.totalCols,0,2,stitches)
  var diagram = dibl.ThreadDiagram().create(pairDiagram)
  var nodeDefs = diagram.jsNodes()
  var linkDefs = diagram.jsLinks()//can't inline
  var container = d3.select("#diagram")
  var svg = dibl.SVG()
  var markers = true // use false for slow devices and IE-11, set them at onEnd
   container.node().innerHTML = svg.render(diagram, "2px", markers, 270, 270)
  var links = container.selectAll(".link").data(linkDefs)
  var nodes = container.selectAll(".node").data(nodeDefs)

  function moveNode(jsNode) {
      return 'translate('+jsNode.x+','+jsNode.y+')'
  }
  function drawPath(jsLink) {
      var s = jsLink.source
      var t = jsLink.target
      var l = diagram.link(jsLink.index)
      return  svg.pathDescription(l, s.x, s.y, t.x, t.y)
  }
  function onTick() {
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
  }

  // duplication of src/main/resources/force.js.applyForce(...)
  function strength(link){ return link.weak ? 1 : 50 }
  d3.forceSimulation(nodeDefs)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", d3.forceLink(linkDefs).strength(strength).distance(12).iterations(30))
    .force("center", d3.forceCenter(150, 125))
    .alpha(0.0035)
    .on("tick", onTick)
}
function setStitch(source) {

  var id = source.attributes["id"].value.substr(4)
  selected = askForStitch("Stitch for " + id, stitches[id] ? stitches[id] : "ctc")
  if (selected && selected != "") {
    stitches[id] = selected
    collectStitches()
    showProto()
    showThreadDiagram()
  }
}
function clearStitches() {

  stitches = {}
  collectStitches()
  showProto()
}
function allStitches() {

  selected = askForStitch("reset all stitches to: ", "")
  if (selected && selected.trim() != "") {
    for (var r=1; r <= maxRows; r++)
      for (var c=1 ; c <= maxCols; c++)
        stitches["r"+r+"-c"+c] = selected
    collectStitches()
    showProto()
  }
}
function defaultStitches() {

  selected = askForStitch("set remaining stitches to: ", "ctc")
  if (selected && selected.trim() != "") {
    for (var r=1; r <= maxRows; r++)
      for (var c=1 ; c <= maxCols; c++) {
        var id = "r"+r+"-c"+c
        if (!stitches[id] || stitches[id] == "")
          stitches[id] = selected
      }
    collectStitches()
    showProto()
  }
}
function askForStitch(s, defaultStitch){
  // making the input valid as early as possible protects against injection when displaying the value
  return dibl.Stitches().makeValid(prompt(s, defaultStitch? defaultStitch : "ctc"), defaultStitch)
}
function query() {
  var kvpairs = []
  var els = document.forms[0].elements
  for (i in els) {
     var e = els[i]
     if (e && e.name && e.value)
     kvpairs.push((e.name).replace("matrix","tile") + "=" + (e.value).replace(/\n/g,","))//encodeURIComponent
  }
  return kvpairs.join("&") + "&" + document.getElementById("stitches").innerHTML.replace(/, /g,"&").toLowerCase()
}
function sample(matrix, shiftColsSE, shiftRowsSE, shiftColsSW, shiftRowsSW, footside, headside, repeatWidth, repeatHeight) {

  document.getElementById('repeatWidth').value = repeatWidth ? repeatWidth : (footside?3:12)
  document.getElementById('repeatHeight').value = repeatHeight ? repeatHeight : 12
  document.getElementById('matrix').value = matrix
  document.getElementById('footside').value = footside ? footside : ""
  document.getElementById('headside').value = headside ? headside : ""
  document.getElementById('shiftColsSE').value = shiftColsSE
  document.getElementById('shiftRowsSE').value = shiftRowsSE
  document.getElementById('shiftColsSW').value = shiftColsSW
  document.getElementById('shiftRowsSW').value = shiftRowsSW
  showProto()
}
function asChecker() {

  var matrixLines = document.getElementById('matrix').value.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  document.getElementById('shiftRowsSE').value = rows
  document.getElementById('shiftColsSE').value = cols
  document.getElementById('shiftRowsSW').value = rows
  document.getElementById('shiftColsSW').value = 0
  showProto()
}
function brickToLeft() {

  document.getElementById('shiftColsSE').value--
  document.getElementById('shiftColsSW').value--
  showProto()
}
function brickToRight() {

  document.getElementById('shiftColsSE').value++
  document.getElementById('shiftColsSW').value++
  showProto()
}
function brickUp() {

  document.getElementById('shiftRowsSE').value--
  showProto()
}
function brickDown() {

  document.getElementById('shiftRowsSE').value++
  showProto()
}