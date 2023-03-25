function load() {
    var q = window.location.search.substr(1)
    var cfg = TilesConfig(q)

    // dimensions for an A1
    var width = 2245
    var height = 3178

    var opacity = 0
    var zoom = 1.9
    var stroke = 2
    var itemMatrix = cfg.getItemMatrix
    var svg = PairSvg.render(itemMatrix, width, height, zoom)
    d3.select('#def').attr("href","pattern.html?"+q)
    d3.select('#enum').html(PairSvg.legend(itemMatrix))
    d3.select('#pair').html(svg)
    d3.select('#forces').on("click",function () {
        nudgePairs('#pair', cfg.totalCols*6,cfg.totalRows*6)
    })
    var pairDiagram = NewPairDiagram.create(cfg)
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    showGraph(d3.select('#thread'), threadDiagram, stroke, width, height, opacity)
    d3.select('#thread g').attr("transform","scale(0.5,0.5)")
    q.split("&").find(whiting)
}
function paintStitchValue () {

  return d3.select("#paintStitches").node().value
}
function flipStitch() {
  var n = d3.select('#paintStitches').node()
  n.value=n.value.toLowerCase().replace(/l/g,"R").replace(/r/g,"l").replace(/R/g,"r")
  return false;
}
function clickedStitch(event) {

  var id = event.currentTarget.getElementsByTagName("title")[0].innerHTML.replace(/.* /,"")
  d3.select('#'+id).attr("value", paintStitchValue())
}
function clearStitches() {

  d3.selectAll("svg input").attr("value",paintStitchValue())
}
function whiting (kv) {
    var k = kv.trim().replace(/[^a-zA-Z0-9]/g,"")
    if (!kv.trim().startsWith("whiting")) return false
    // side effect: add whiting link
    var pageNr = kv.split("P")[1]
    var cellNr = kv.split("_")[0].split("=")[1]
    var w = d3.select('#whiting')
    w.style("display","inline-block")
    w.node().innerHTML =
        "<img src='/gw-lace-to-gf/w/page" + pageNr + "a.gif' title='"+cellNr+"'>"+
        " Page <a href='http://www.theedkins.co.uk/jo/lace/whiting/page" + pageNr + ".htm'>" + pageNr + "</a> "+
        "of '<em>A Lace Guide for Makers and Collectors</em>' by Gertrude Whiting; 1920."
    return true
}
function clickedThread(event) {
    var threadSegments = d3.selectAll("#thread ." + event.currentTarget.textContent.replace(" ",""))
    var color = d3.select('#threadColor').node().value
    threadSegments.style("stroke", color)
    threadSegments.filter(".node").style("fill", color)
}

