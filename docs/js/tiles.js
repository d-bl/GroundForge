var stitches = {}

// number of cloned elements in the built-in SVG FIXME not maintained
var maxRows = 12
var maxCols = 12

function showProto() {

  var q = query()
  var config = dibl.Config().create(q)
  d3.select('#clones').node().innerHTML = dibl.InteractiveSVG().create(config)
  d3.select('#link').node().href = '?'+q
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
  // firstChild == <title>
  var className = "."+d3.event.target.firstChild.innerHTML.replace(" ", "")
  var segments = d3.selectAll(className)
  var newColor = d3.event.altKey? "#000" : "#F00"
  segments.style("stroke", newColor)
  segments.filter(".node").style("fill", newColor)
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

  svg = d3.select(id).node().innerHTML.
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
      var el = d3.select("#"+k).node()
      if (el) el.value = v
    }
  }
  showProto()
  showThreadDiagram()
}
function sample(tile, shiftColsSE, shiftRowsSE, shiftColsSW, shiftRowsSW, footside, headside, repeatWidth, repeatHeight) {

  d3.select('#repeatWidth').property("value",repeatWidth ? repeatWidth : (footside?3:12))
  d3.select('#repeatHeight').property("value",repeatHeight ? repeatHeight : 12)
  d3.select('#tile').property("value",tile)
  d3.select('#footside').property("value",footside ? footside : "")
  d3.select('#headside').property("value",headside ? headside : "")
  d3.select('#shiftColsSE').property("value",shiftColsSE)
  d3.select('#shiftRowsSE').property("value",shiftRowsSE)
  d3.select('#shiftColsSW').property("value",shiftColsSW)
  d3.select('#shiftRowsSW').property("value",shiftRowsSW)
  showProto()
  showThreadDiagram()
}
function asChecker() {

  var matrixLines = d3.select('#tile').node().value.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  d3.select('#shiftRowsSE').property("value", rows)
  d3.select('#shiftColsSE').property("value", cols)
  d3.select('#shiftRowsSW').property("value", rows)
  d3.select('#shiftColsSW').property("value", 0)
  showProto()
}
function brickToLeft() {

  d3.select('#shiftColsSE').node().value--
  d3.select('#shiftColsSW').node().value--
  showProto()
}
function brickToRight() {

  d3.select('#shiftColsSE').node().value++
  d3.select('#shiftColsSW').node().value++
  showProto()
}
function brickUp() {

  d3.select('#shiftRowsSE').node().value--
  showProto()
}
function brickDown() {

  d3.select('#shiftRowsSE').node().value++
  showProto()
}
function asStack() {

  d3.select('#shiftRowsSE').property("value",0)
  d3.select('#shiftColsSE').property("value",0)
  d3.select('#shiftRowsSW').property("value",0)
  d3.select('#shiftColsSW').property("value",0)
  showProto()
}
function brickToSW() {

  d3.select('#shiftColsSW').node().value--
  d3.select('#shiftRowsSW').node().value++
  showProto()
}
function brickToNE() {

  d3.select('#shiftColsSW').node().value++
  d3.select('#shiftRowsSW').node().value--
  showProto()
}
function brickToSE() {

  d3.select('#shiftColsSE').node().value++
  d3.select('#shiftRowsSE').node().value++
  showProto()
}
function brickToNW() {

  d3.select('#shiftColsSE').node().value--
  d3.select('#shiftRowsSE').node().value--
  showProto()
}
