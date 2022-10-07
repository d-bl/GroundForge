function load() {
    var q = window.location.search.substr(1)
    var cfg = TilesConfig(q)

    // dimensions for an A4
    var width = 744
    var height = 1052
    console.log(`rows=${cfg.totalRows} cols=${cfg.totalCols} -> ${(cfg.totalRows+2)*15},${(cfg.totalCols+2)*15} ; A4=${width},${height}`)

    var zoom = 1.9
    var itemMatrix = cfg.getItemMatrix
    var svg = PairSvg.render(itemMatrix, width, height, zoom)
    d3.select('#enum4perStitch').html(PairSvg.legend(itemMatrix))
    d3.select('#pair4perStitch').html(svg)
    d3.select('#pair4perStitchAnimated').html(svg)
    d3.select('#proto').html(PrototypeDiagram.create(cfg))
    nudgePairs(d3.select('#pair4perStitchAnimated'), cfg.totalCols/2 * 12, cfg.totalRows/2 * 12)
    var pairDiagram = NewPairDiagram.create(cfg)
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    d3.select('#pair1perStitch').html(DiagramSvg.render(pairDiagram, 2, true, width, height, 0))
    d3.select('#thread').html(DiagramSvg.render(threadDiagram, 2, true, width, height, 0))
    var strokeWidth = 2
    nodeTransparency = 0
    // TODO centralize
    showGraph(d3.select('#pair1perStitchAnimated'), pairDiagram, strokeWidth, width, height, 0)
    showGraph(d3.select('#thread'), threadDiagram, strokeWidth, width, height, nodeTransparency)
    d3.select('#thread g').attr("transform","scale(0.5,0.5)")
}
function showGraph(container, diagram, stroke, width, height, transparency) {
// copied from API/thread.html, added moveToNW
    var markers = true // use false for slow devices and IE-11, set them at onEnd
    container.html(DiagramSvg.render(diagram, stroke, markers, width, height, transparency))

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
      console.log(new Date().getMilliseconds())
      var x = nodeDefs.reduce(minX).x - 3
      var y = nodeDefs.reduce(minY).y - 3
      console.log(`minX = ${x}; minY = ${y}`)
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
      console.log(new Date().getMilliseconds())
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
      .on("tick", moveToNW)
}

