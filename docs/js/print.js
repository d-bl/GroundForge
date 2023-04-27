function load() {
    var q = window.location.search.substr(1)
    var cfg = TilesConfig(q)
    var zoom = 1.9
    var stroke = 2
    var opacity = 0
    var itemMatrix = cfg.getItemMatrix

    // dimensions for an A4
    var width = 744
    var height = 1052

    var svg = PairSvg.render(itemMatrix, width, height, zoom)
    d3.select('#def').attr("href","tiles?"+q)
    d3.select('#enum4perStitch').html(PairSvg.legend(itemMatrix))
    d3.select('#pair4perStitch').html(svg)
    d3.select('#pair4perStitchAnimated').html(svg)
    d3.select('#proto').html(PrototypeDiagram.create(cfg))
    nudgePairs('#pair4perStitchAnimated', cfg.totalCols*6, cfg.totalRows*6)
    var pairDiagram = NewPairDiagram.create(cfg)
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    d3.select('#pair1perStitch').html(DiagramSvg.render(pairDiagram, stroke, true, width, height, opacity))
    showGraph('#pair1perStitchAnimated', pairDiagram)
    showGraph('#thread', threadDiagram)
    d3.select('#thread g').attr("transform","scale(0.5,0.5)")
    var keyValues = q.split("&")
    keyValues.find(whiting)
    keyValues.forEach(patch)
}
var seqNr = 1
function patch (kv) {
    if (!kv.trim().startsWith("patch=")) return false
    var a = kv.replace("patch=","").split(";")
    var matrix = a[0]
    var shiftStyle = a[1]
    var svg = new SheetSVG(1,"height='90mm' width='330mm'", seqNr++)
          .add(a[0], a[1]).toSvgDoc().trim()
    d3.select("body").append("div").node().innerHTML = svg
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
