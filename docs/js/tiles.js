var valueFilter = /[^a-zA-Z0-9,-]/g
var isMobile = /iPad|iPhone|iPod|Mobi/.test(navigator.userAgent)

function setStitch(sourceNode) {

  var id = sourceNode.dataset.formid
  var el = document.getElementById(id)
  el.focus()
}
function clearStitches() {

  d3.selectAll("svg input").attr("value","")
  showProto()
}
function toKeyValueString (formField) {
    var n = formField.name
    var v = formField.value
    return !n || !v ? '' : n + '=' +  v.replace(/\n/g,",").replace(valueFilter,"")
}
function isKeyValue (formField) {
    return formField.name && formField.value
}
function submitQuery() {

  return d3.selectAll('input, textarea')
    .nodes()
    .filter(isKeyValue)
    .map(toKeyValueString)
    .join("&")
}
function showProto() {

  var config = dibl.Config().create(submitQuery())
  d3.select("#clones").html(dibl.InteractiveSVG().create(config))

  // we might now have a new set of stitches in the form
  d3.select("#link").node().href = "?" + submitQuery()

  d3.select("#animations").style("display", "none")
  d3.selectAll("#threadDiagram, #pairDiagram").html("")
  d3.selectAll("textarea").attr("rows", config.maxTileRows + 1)
  d3.select("#footside").attr("cols", config.leftMatrixCols + 2)
  d3.select("#tile"    ).attr("cols", config.centerMatrixCols + 2)
  d3.select("#headside").attr("cols", config.rightMatrixCols + 2)
  d3.select("#prototype").style("height", (config.totalRows * 27 + 30) + "px"
                        ).style("width", (config.totalCols * 27 + 60) + "px")
}
function scrollIntoViewIfPossible(container) {
  // despite w3Schools documentation not available for IE / Edge(?)
  // see also https://developer.microsoft.com/en-us/microsoft-edge/platform/issues/15534521/
  if (container.scrollIntoView) {
    container.scrollIntoView({ block: "start", behavior: 'smooth' })
  }
}
function scrollToIfPossible(container, x, y) {
  if (container.scrollTop !== undefined && container.scrollLeft !== undefined) {
    container.scrollTop = y
    container.scrollLeft = x
  }
}
function showDiagrams() {

  d3.select('#animations').style('display',"inline-block")

  var markers = true // use false for slow devices and IE-11, set them at onEnd

  var pairContainer = d3.select("#pairDiagram")
  var pairContainerNode = pairContainer.node()
  var config = dibl.Config().create(submitQuery())
  var pairDiagram = pairContainerNode.data = dibl.NewPairDiagram().create(config)
  pairContainer.html(dibl.D3jsSVG().render(pairDiagram, "1px", markers, 744, 1052))
  scrollToIfPossible(pairContainerNode,0,0)
  if (pairDiagram.jsNodes().length == 1) return

  var threadContainer = d3.select("#threadDiagram")
  var threadContainerNode = threadContainer.node()
  var threadDiagram = threadContainerNode.data = dibl.ThreadDiagram().create(pairDiagram)
  threadContainer.html(dibl.D3jsSVG().render(threadDiagram, "2px", markers, 744, 1052).replace("<g>","<g transform='scale(0.5,0.5)'>"))
  if (threadDiagram.jsNodes().length == 1 || threadContainerNode.innerHTML.indexOf("Need a new pair from") >= 0)  return

  animateDiagram(threadContainer)
  scrollToIfPossible(threadContainerNode, 75, 175)
  threadContainer.selectAll(".threadStart").on("click", paintThread)
}
function animateDiagram(container) {

  var diagram = container.node().data
  var nodeDefs = diagram.jsNodes()
  var linkDefs = diagram.jsLinks()//can't inline
  scrollToIfPossible(container.node(), 220, 400)
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
  var tickCounter = 0
  function onTick() {
      if ( isMobile && (tickCounter++ % 5) != 0) return // skip rendering
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
function paintThread() {

  // firstChild == <title>
  var className = "."+d3.event.target.firstChild.innerHTML.replace(" ", "")
  var segments = d3.selectAll(className)
  console.log(segments.style("stroke"))
  var newColor = segments.style("stroke")+"" == "rgb(255, 0, 0)" ? "#000" : "#F00"
  segments.style("stroke", newColor)
  segments.filter(".node").style("fill", newColor)
}
function setDownloadContent (linkNode, id) {

  svg = d3.select(id).node().innerHTML.
      replace('pointer-events="all"', '').
      replace(/<path [^>]+opacity: 0;.+?path>/g, '')
  linkNode.href = 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
function setField (keyValueString) {

    var kv = keyValueString.split("=")
    if (kv.length > 1) {
      var k = kv[0].trim().replace(/[^a-zA-Z]/g,"")
      var v = kv[1].trim().replace(valueFilter,"").replace(/,/g,"\n")
      d3.select('#'+k).property("value", v)
    }
}
function load() {

  var keyValueStrings = window.location.search.substr(1).split("&")
  keyValueStrings.forEach(setField)
  showProto()
  if (keyValueStrings.length >= 7 ) // TODO this is not a good safeguard against invalid/incomplete search arguments
    showDiagrams()
}
function sample(tile, shiftColsSE, shiftRowsSE, shiftColsSW, shiftRowsSW, footside, headside, repeatWidth, repeatHeight) {

  d3.select('#repeatWidth').property("value", repeatWidth ? repeatWidth : (footside?3:12))
  d3.select('#repeatHeight').property("value", repeatHeight ? repeatHeight : 12)
  d3.select('#tile').property("value", tile)
  d3.select('#footside').property("value", footside ? footside : "")
  d3.select('#headside').property("value", headside ? headside : "")
  d3.select('#shiftColsSE').property("value", shiftColsSE)
  d3.select('#shiftRowsSE').property("value", shiftRowsSE)
  d3.select('#shiftColsSW').property("value", shiftColsSW)
  d3.select('#shiftRowsSW').property("value", shiftRowsSW)

  scrollIntoViewIfPossible(d3.select("#diagrams").node())
  showProto()
  showDiagrams()
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

  d3.select('#shiftRowsSE').property("value", 0)
  d3.select('#shiftColsSE').property("value", 0)
  d3.select('#shiftRowsSW').property("value", 0)
  d3.select('#shiftColsSW').property("value", 0)
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
function resizeBoth(selector, scaleValue) {

  resize(d3.select(selector), 'width', scaleValue)
  resize(d3.select(selector), 'height', scaleValue)
}
function resize(container, orientation, scaleValue) {
  var oldValue = container.style(orientation)
  var units = oldValue.replace(/[0-9]/g,'')
  var newValue = Math.round(oldValue.replace(/[^0-9]/g,'') * scaleValue)
  container.style(orientation, newValue + units)
}