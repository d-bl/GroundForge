
/**
 * container: DOM element obtained with d3.select containing an SVG generated with PairSvg.create
 */
function nudgePairs(container) {

  var svg = container.select("svg")
  var width = svg.attr("width") / 1.9
  var height = svg.attr("height") / 1.9
  var zoom = 1 * svg.select("g").attr("transform").replace("matrix(","").replace(/,.*/,"")

  var nodeSelection = svg.selectAll(".node")
  var linkSelection = svg.selectAll(".link")

  // scroll to center of SVG

  var containerNode = container.node()
  if (containerNode.scrollTop !== undefined && containerNode.scrollLeft !== undefined) {
    containerNode.scrollTop = height / 2
    containerNode.scrollLeft = width / 2
  }

  // collect data of the SVG elements with class node

  var nodeData = nodeSelection.nodes().map(function(n){
    var xys = n.attributes["transform"].nodeValue
               .replace("translate(","").replace(")","").split(",")
    return {
      x: xys[0]*1,
      y: xys[1]*1,
      id: n.attributes["id"].nodeValue
    }
  })
  var nodeMap = new Map()
  for (i=0; i< nodeData.length; i++)
    nodeMap.set(nodeData[i].id, i)

  // collect data of the SVG elements with class link
  // each link-id is split into the IDs of their nodes

  var linkData = linkSelection.nodes().map(function(n){
    var hasMidPoint = n.attributes["d"].nodeValue
                       .replace(/[^ ]/g,"").length > 2
    var ids = n.attributes["id"].nodeValue.split("-")
    return {
      mid: hasMidPoint,
      source: nodeMap.get(ids[0]),
      target: nodeMap.get(ids[1])
    }
  })

  // bind collected data

  var links = linkSelection.data(linkData)
  var nodes = nodeSelection.data(nodeData)

  // redraw

  function moveNode(jsNode) {
      return 'translate('+jsNode.x+','+jsNode.y+')'
  }
  function drawPath(jsLink) {
      var s = jsLink.source
      var t = jsLink.target
      return PairSvg.linkPath(jsLink.mid, s.x, s.y, t.x, t.y)
  }
  var tickCounter = 0
  function onTick() {
      if (0 !=  (tickCounter++ % 5) ) return
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
  }

  // define forces with the collected data

  var forceLink = d3
    .forceLink(linkData)
    .strength(50)
    .distance(11.5)
    .iterations(30)
  d3.forceSimulation(nodeData)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", forceLink)
    .force("center", d3.forceCenter(width/2, height/2))
    .alpha(0.0035)
    .on("tick", onTick)
}
