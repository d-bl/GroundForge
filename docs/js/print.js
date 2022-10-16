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
    d3.select('#def').attr("href","tiles?"+q)
    d3.select('#enum4perStitch').html(PairSvg.legend(itemMatrix))
    d3.select('#pair4perStitch').html(svg)
    d3.select('#pair4perStitchAnimated').html(svg)
    d3.select('#proto').html(PrototypeDiagram.create(cfg))
    nudgePairs(d3.select('#pair4perStitchAnimated'), cfg.totalCols*6, cfg.totalRows*6)
    var pairDiagram = NewPairDiagram.create(cfg)
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    d3.select('#pair1perStitch').html(DiagramSvg.render(pairDiagram, stroke, true, width, height, opacity))
    showGraph(d3.select('#pair1perStitchAnimated'), pairDiagram, stroke, width, height, opacity)
    showGraph(d3.select('#thread'), threadDiagram, stroke, width, height, opacity)
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

function showGraph(container, diagram, stroke, width, height, opacity) {
// copied from API/thread.html, added moveToNW

    var markers = true // use false for slow devices and IE-11, set them at onEnd
    container.html(DiagramSvg.render(diagram, stroke, markers, width, height, opacity))

    var nodeDefs = diagram.jsNodes()
    var linkDefs = diagram.jsLinks()//can't inline
    var links = container.selectAll(".link").data(linkDefs)
    var nodes = container.selectAll(".node").data(nodeDefs)
    function moveNode(jsNode) {
        return 'translate('+jsNode.x+','+jsNode.y+')'
    }
    function drawPath(jsLink) {
        var s = jsLink.source
        var t = jsLink.target
        var l = diagram.link(jsLink.index)
        return DiagramSvg.pathDescription(l, s.x, s.y, t.x, t.y)
    }
    var tickCounter = 0
    function onTick() {
        if (0 !=  (tickCounter++ % 5) ) return
        links.attr("d", drawPath);
        nodes.attr("transform", moveNode);
    }

  function moveToNW() {
      var x = nodeDefs.reduce(minX).x - 3
      var y = nodeDefs.reduce(minY).y - 3
      function moveNode(jsNode) { return 'translate('+(jsNode.x-x)+','+(jsNode.y-y)+')' }
      function drawPath(jsLink) {
          var s = jsLink.source
          var t = jsLink.target
          var l = diagram.link(jsLink.index)
          // priority for preventing code duplication over less independency
          return DiagramSvg.pathDescription(l, s.x - x, s.y-y, t.x - x, t.y -y)
      }
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
  }
  function minX (min, node) { return min.x < node.x ? min : node }
  function minY (min, node) { return min.y < node.y ? min : node }

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
      .force("center", d3.forceCenter(width/2, height/2))
      .alpha(0.0035)
      .on("tick", onTick)
      .on("end", moveToNW)
}

