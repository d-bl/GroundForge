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

  var config = TilesConfig(submitQuery())
  d3.select("#prototype").html(PrototypeDiagram.create(config))
  d3.select("#link").node().href = "?" + submitQuery() // don't extract var, we might now have other form fields
  d3.select("#animations").style("display", "none")
  d3.selectAll("#threadDiagram, #pairDiagram, #drostePair2, #drosteThread2, #drostePair3, #drosteThread3").html("")
  d3.selectAll("#pattern textarea").attr("rows", config.maxTileRows + 1)
  d3.select("#footside").attr("cols", config.leftMatrixCols + 2)
  d3.select("#tile"    ).attr("cols", config.centerMatrixCols + 2)
  d3.select("#headside").attr("cols", config.rightMatrixCols + 2)
  d3.select("#prototype").style("height", (config.totalRows * 27 + 30) + "px"
                        ).style("width", (config.totalCols * 27 + 60) + "px")
  return config
}
function clear2() {
  d3.selectAll("#drostePair2, #drosteThread2, #drostePair3, #drosteThread3").html("")
}
function clear3() {
  d3.selectAll("#drostePair3, #drosteThread3").html("")
}
function flip(){
  var left = d3.select("#footside").node().value
  console.log(Matrix.flip)
  d3.select("#headside").node().value = Matrix.flip(left)
  showProto()
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
function showDiagrams(config) {

  d3.select('#animations').style('display',"inline-block")

  var markers = true // use false for slow devices and IE-11, set them at onEnd

  var pairContainer = d3.select("#pairDiagram")
  var pairContainerNode = pairContainer.node()
  if (!config)
      var config = TilesConfig(submitQuery())
  var pairDiagram = pairContainerNode.data = NewPairDiagram.create(config)
  pairContainer.html(D3jsSVG.render(pairDiagram, "1px", markers, 744, 1052))
  scrollToIfPossible(pairContainerNode,0,0)
  if (pairDiagram.jsNodes().length == 1) return

  var threadContainer = d3.select("#threadDiagram")
  var threadContainerNode = threadContainer.node()
  var threadDiagram = threadContainerNode.data = ThreadDiagram.create(pairDiagram)
  threadContainer.html(D3jsSVG.render(threadDiagram, "2px", markers, 744, 1052, 0.0).replace("<g>","<g transform='scale(0.5,0.5)'>"))
  if (threadDiagram.jsNodes().length == 1 || threadContainerNode.innerHTML.indexOf("Need a new pair from") >= 0)  return

  animateDiagram(threadContainer, 744, 1052)
  threadContainer.selectAll(".threadStart").on("click", paintThread)
}
function animateDiagram(container, forceCenterX, forceCenterY) {

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
      return  D3jsSVG.pathDescription(l, s.x, s.y, t.x, t.y)
  }
  var tickCounter = 0
  function onTick() {
      if ( isMobile && (tickCounter++ % 5) != 0) return // skip rendering
      //if (tickCounter++ >=0) terminateAnimation()
      links.attr("d", drawPath)
      nodes.attr("transform", moveNode)
  }

  // read 'weak' as 'invisible'
  function strength(link){ return link.weak ? link.withPin ? 40 : 10 : 50 }
  var forceLink = d3
    .forceLink(linkDefs)
    .strength(strength)
    .distance(12)
    .iterations(30)
  d3.forceSimulation(nodeDefs)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", forceLink)
    .force("center", d3.forceCenter(forceCenterX, forceCenterY))
    .alpha(0.0035)
    .on("tick", onTick)
}
function paintThread() {

  // firstChild == <title>
  var className = " ."+d3.event.target.firstChild.innerHTML.replace(" ", "")
  var segments = d3.selectAll(className)
  var newColor = segments.style("stroke")+"" == "rgb(255, 0, 0)" ? "#000" : "#F00"
  segments.style("stroke", newColor)
  segments.filter(".node").style("fill", newColor)
}
function setDownloadContent (linkNode, id) {

  svg = d3.select(id).node().innerHTML.
      replace('pointer-events="all"', '').
      replace(/<path [^>]+opacity: 0;.+?path>/g, '').
      replace(/<\/foreignObject>/g, '</input></foreignObject>') // even if provided, closing tag gets swallowed
  linkNode.href = 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
function setField (keyValueString) {

    var kv = keyValueString.split("=")
    if (kv.length > 1) {
      var k = kv[0].trim().replace(/[^a-zA-Z0-9]/g,"")
      var v = kv[1].trim().replace(valueFilter,"").replace(/,/g,"\n")
      d3.select('#'+k).property("value", v)
    }
}
function whiting (kv) {
    var k = kv.trim().replace(/[^a-zA-Z0-9]/g,"")
    if (!kv.startsWith("whiting")) return false
    // side effect: add whiting link
    var pageNr = kv.split("P")[1]
    var cellNr = kv.split("_")[0].split("=")[1]
    d3.select('#whiting').node().innerHTML =
        "<img src='help/w/page" + pageNr + "a.gif' title='"+cellNr+"'>"+
        " Page <a href='http://www.gwydir.demon.co.uk/jo/lace/whiting/page" + pageNr + ".htm'>" + pageNr + "</a> "+
        "of '<em>A Lace Guide for Makers and Collectors</em>' by Gertrude Whiting; 1920."
    return true
}
function load() {

  var keyValueStrings = window.location.search.substr(1).split("&")
  keyValueStrings.forEach(setField)
  showProto() // this creates a dynamic part of the form
  keyValueStrings.forEach(setField) // fill the form fields again
  showDiagrams(showProto())
  keyValueStrings.find(whiting)
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
function asHorBricks() {

  var matrixLines = d3.select('#tile').node().value.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  d3.select('#shiftRowsSE').property("value", rows)
  d3.select('#shiftColsSE').property("value", cols - Math.round(cols/2))
  d3.select('#shiftRowsSW').property("value", rows)
  d3.select('#shiftColsSW').property("value", - Math.round(cols/2))
  showProto()
}
function asVerBricks() {

  var matrixLines = d3.select('#tile').node().value.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  d3.select('#shiftRowsSE').property("value", Math.round(rows/2))
  d3.select('#shiftColsSE').property("value", cols)
  d3.select('#shiftRowsSW').property("value", rows)
  d3.select('#shiftColsSW').property("value", 0)
  showProto()
}
function withOverlap() {

  var matrixLines = d3.select('#tile').node().value.toUpperCase().trim().split(/[^-A-Z0-9]+/)
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  d3.select('#shiftRowsSE').property("value", rows)
  d3.select('#shiftColsSE').property("value", cols)
  d3.select('#shiftRowsSW').property("value", rows)
  d3.select('#shiftColsSW').property("value", - cols)
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
function seToLeft() {

  d3.select('#shiftColsSE').node().value--
  showProto()
}
function seUp() {

  d3.select('#shiftRowsSE').node().value--
  showProto()
}
function seToRight() {

  d3.select('#shiftColsSE').node().value++
  showProto()
}
function seDown() {

  d3.select('#shiftRowsSE').node().value++
  showProto()
}
function swToRight() {

  d3.select('#shiftColsSW').node().value++
  showProto()
}
function swUp() {

  d3.select('#shiftRowsSW').node().value--
  showProto()
}
function swToLeft() {

  d3.select('#shiftColsSW').node().value--
  showProto()
}
function swDown() {

  d3.select('#shiftRowsSW').node().value++
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
function showDroste() {
  var q = submitQuery()
  d3.select("#link").node().href = "?" + q
  d3.select("#drosteFields").style("display", "block")
  var config = TilesConfig(q)
  var pairDiagram = NewPairDiagram.create(config)
  var threadDiagram = ThreadDiagram.create(pairDiagram)
  // TODO the diagrams above have been calculated before
  var drostePairs2 = PairDiagram.create(d3.select("#droste2").node().value, threadDiagram)
  var drosteThreads2 = ThreadDiagram.create(drostePairs2)
  var drostePairs3 = PairDiagram.create(d3.select("#droste3").node().value, drosteThreads2)
  var drosteThreads3 = ThreadDiagram.create(drostePairs3)

  var markers = true // use false for slow devices and IE-11, set them at onEnd

  var pairContainer2 = d3.select("#drostePair2")
  pairContainer2.node().data = drostePairs2
  pairContainer2.html(D3jsSVG.render(drostePairs2, "1px", markers, 744, 1052))

  var threadContainer2 = d3.select("#drosteThread2")
  threadContainer2.node().data = drosteThreads2
  threadContainer2.html(D3jsSVG.render(drosteThreads2, "2px", markers, 744, 1052, 0.0).replace("<g>","<g transform='scale(0.5,0.5)'>"))

  var pairContainer3 = d3.select("#drostePair3")
  pairContainer3.node().data = drostePairs3
  pairContainer3.html(D3jsSVG.render(drostePairs3, "1px", markers, 744, 1052))

  var threadContainer3 = d3.select("#drosteThread3")
  threadContainer3.node().data = drosteThreads3
  threadContainer3.html(D3jsSVG.render(drosteThreads3, "2px", markers, 744, 1052, 0.0).replace("<g>","<g transform='scale(0.5,0.5)'>"))

  animateDiagram(pairContainer2, 350, 526)
  animateDiagram(threadContainer2, 744, 1052)
  animateDiagram(pairContainer3, 350, 526)
  animateDiagram(threadContainer3, 744, 1052)
  threadContainer2.selectAll(".threadStart").on("click", paintThread)
  threadContainer3.selectAll(".threadStart").on("click", paintThread)
}
