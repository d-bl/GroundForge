function nudgeNewPairs(container, width, height) {
  function toXY(s) {
    var xy = s.split(",")
    return {x:xy[0]*1,y:xy[1]*1}
  }
  var nodeDefs = container.selectAll(".node").nodes().map(function(n){
    var result = toXY(n.attributes["transform"].nodeValue.replace("translate(","").replace(")",""))
    result.id = n.attributes["id"].nodeValue
    return result
  })
  var nodeMap = new Map()
  for (i=0; i< nodeDefs.length; i++)
    nodeMap.set(nodeDefs[i].id, i)

  var linkDefs = container.selectAll(".link").nodes().map(function(n){
    var xys = n.attributes["d"].nodeValue.replace("M ","").split(" ").map(toXY)
    var ids = n.attributes["id"].nodeValue.split("-")
    return { mid: xys.length > 2, source: nodeMap.get(ids[0]), target: nodeMap.get(ids[1]) }
  })

  var links = container.selectAll(".link").data(linkDefs )
  var nodes = container.selectAll(".node").data( nodeDefs )

  function moveNode(jsNode) {
      return 'translate('+jsNode.x+','+jsNode.y+')'
  }
  function drawPath(jsLink) {
      var s = jsLink.source
      var t = jsLink.target
      var mX = s.x + (t.x - s.x) / 2
      var mY = s.y + (t.y - s.y) / 2
      var s2 = `M ${s.x},${s.y} ${mX},${mY} ${t.x},${t.y}`
      return s2
  }
  function onTick() {
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
  }
  var forceLink = d3
    .forceLink(linkDefs)
    .strength(50)
    .distance(12)
    .iterations(30)
  d3.forceSimulation(nodeDefs)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", forceLink)
    .force("center", d3.forceCenter(width/2, height/2))
    .alpha(0.0035)
    .on("tick", onTick)
}