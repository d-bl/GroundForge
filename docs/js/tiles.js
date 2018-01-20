var stitches = {}

// number of cloned elements in the built-in SVG
var maxRows = 12
var maxCols = 12

function showProto() {

  var q = query()
  var config = dibl.Config().create(q)
  document.getElementById('clones').innerHTML = dibl.InteractiveSVG().create(config)
  document.getElementById('link').href = '?'+q
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
  return kvs.join(", ")
}
function paintThread() {
  var className = "."+d3.event.target.firstChild.innerHTML.replace(" ","")
  d3.selectAll(className).style("stroke", d3.event.altKey? "#000" : "#F00")
}
function showThreadDiagram() {

  var markers = true // use false for slow devices and IE-11, set them at onEnd
  var threadContainer = d3.select("#threadDiagram")
  var threadContainerNode = threadContainer.node()
  threadContainerNode.innerHTML = ""

  var pairContainerNode = d3.select("#pairDiagram").node()
  var pairDiagram = pairContainerNode.data = dibl.Config().create(query()).pairDiagram
  pairContainerNode.innerHTML = dibl.D3jsSVG().render(pairDiagram, "1px", markers, 744, 1052)
  if (pairDiagram.jsNodes().length == 1) return

  var threadDiagram = threadContainerNode.data = dibl.ThreadDiagram().create(pairDiagram)
  threadContainerNode.innerHTML = dibl.D3jsSVG().render(threadDiagram, "2px", markers, 744, 1052)
  if (threadDiagram.jsNodes().length == 1 || threadContainer.node().innerHTML.indexOf("Need a new pair from") >= 0)  return

  animateDiagram(threadContainer)
  threadContainer.selectAll(".threadStart").on("click", paintThread)
}
function setDownloadContent (comp, id) {
  svg = document.getElementById(id).innerHTML.
      replace('pointer-events="all"', '').
      replace(/<path [^>]+opacity: 0;.+?path>/g, '')
  comp.href = 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
function animateDiagram(container) {

  var diagram = container.node().data
  var nodeDefs = diagram.jsNodes()
  var linkDefs = diagram.jsLinks()//can't inline
  container.node().scrollTop = 400
  container.node().scrollLeft = 220
  var links = container.selectAll(".link").data(linkDefs)
  var nodes = container.selectAll(".node").data(nodeDefs)

  function moveNode(jsNode) {
      return 'translate('+jsNode.x+','+jsNode.y+')'
  }
  function drawPath(jsLink) {
      var s = jsLink.source
      var t = jsLink.target
      var l = diagram.link(jsLink.index)
      return  dibl.D3jsSVG().pathDescription(l, s.x, s.y, t.x, t.y)
  }
  function onTick() {
      links.attr("d", drawPath)
      nodes.attr("transform", moveNode)
      //terminateAnimation()
  }

  // duplication of src/main/resources/force.js.applyForce(...)
  function strength(link){ return link.weak ? 1 : 50 }
  d3.forceSimulation(nodeDefs)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", d3.forceLink(linkDefs).strength(strength).distance(12).iterations(30))
    .force("center", d3.forceCenter(372, 526))
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
     var v = e.value
     if (e && e.name && v)
     kvpairs.push(e.name + "=" + (v).replace(/\n/g,","))//encodeURIComponent
  }
  return kvpairs.join("&") + "&" + collectStitches().replace(/, /g,"&").toLowerCase()
}
function load() {
  var kvpairs = (window.location.href + '').replace(/.*\?/,"").split("&")
  for (var i in kvpairs) {
    var kv = kvpairs[i].split("=")
    if (kv.length == 2) {
      var k = kv[0].trim()
      var v = kv[1].trim().replace(/,/g,"\n")
      var el = document.getElementById(k)
      if (el) el.value = v
    }
  }
  showProto()
  showThreadDiagram()
}
function sample(tile, shiftColsSE, shiftRowsSE, shiftColsSW, shiftRowsSW, footside, headside, repeatWidth, repeatHeight) {
  document.getElementById('repeatWidth').value = repeatWidth ? repeatWidth : (footside?3:12)
  document.getElementById('repeatHeight').value = repeatHeight ? repeatHeight : 12
  document.getElementById('tile').value = tile
  document.getElementById('footside').value = footside ? footside : ""
  document.getElementById('headside').value = headside ? headside : ""
  document.getElementById('shiftColsSE').value = shiftColsSE
  document.getElementById('shiftRowsSE').value = shiftRowsSE
  document.getElementById('shiftColsSW').value = shiftColsSW
  document.getElementById('shiftRowsSW').value = shiftRowsSW
  showProto()
  showThreadDiagram()
}
function asChecker() {

  var matrixLines = document.getElementById('tile').value.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  document.getElementById('shiftRowsSE').value = rows
  document.getElementById('shiftColsSE').value = cols
  document.getElementById('shiftRowsSW').value = rows
  document.getElementById('shiftColsSW').value = 0
  showProto()
  showThreadDiagram()
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
function asStack() {

  document.getElementById('shiftRowsSE').value = 0
  document.getElementById('shiftColsSE').value = 0
  document.getElementById('shiftRowsSW').value = 0
  document.getElementById('shiftColsSW').value = 0
  showProto()
}
function brickToSW() {

  document.getElementById('shiftColsSW').value--
  document.getElementById('shiftRowsSW').value++
  showProto()
}
function brickToNE() {

  document.getElementById('shiftColsSW').value++
  document.getElementById('shiftRowsSW').value--
  showProto()
}
function brickToSE() {

  document.getElementById('shiftColsSE').value++
  document.getElementById('shiftRowsSE').value++
  showProto()
}
function brickToNW() {

  document.getElementById('shiftColsSE').value--
  document.getElementById('shiftRowsSE').value--
  showProto()
}
