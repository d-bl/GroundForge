
/**
 * container: d3.selection of element containing an SVG generated with PairSvg.create
 * height and width of the container must be set in pixels
 * https://devdocs.io/d3~4/d3-force
 * https://devdocs.io/d3~4/d3-selection
 */
function nudgePairs(container, cx, cy) {

  var svg = container.select("svg")
  var zoom = 1 * svg.select("#cloned").attr("transform").replace("matrix(","").replace(/,.*/,"")
  var svgWidth = svg.attr("width")
  var svgHeight = svg.attr("height")

  // collect data of the SVG elements with class node

  function getNodeData(n){
    var xys = n.attributes["transform"].nodeValue
               .replace("translate(","").replace(")","").split(",")
    return {
      x: xys[0]*1,
      y: xys[1]*1,
      id: n.attributes["id"].nodeValue
    }
  }

  var nodeSelection = svg.selectAll(".node")
  var nodeData = nodeSelection.nodes().map(getNodeData)
  var nodeMap = new Map()
  for (i=0; i< nodeData.length; i++)
    nodeMap.set(nodeData[i].id, i)

  // collect data of the SVG elements with class link
  // each link-id is split into the IDs of their nodes

  function getLinkData(n){
    var hasMidPoint = n.attributes["d"].nodeValue
                       .replace(/[^ ]/g,"").length > 2
    var ids = n.attributes["id"].nodeValue.split("-")
    return {
      mid: hasMidPoint,
      source: nodeMap.get(ids[0]),
      target: nodeMap.get(ids[1])
    }
  }
  var linkSelection = svg.selectAll(".link")
  var linkData = linkSelection.nodes().map(getLinkData)

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
      // priority for preventing code duplication over less independency
      return PairSvg.linkPath(jsLink.mid, s.x, s.y, t.x, t.y)
  }
  var tickCounter = 0
  function onTick() {
      if (0 !=  (tickCounter++ % 5) ) return
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
  }

  // final position of diagram

  function moveToNW() {
      console.log(new Date().getMilliseconds())
      var x = nodeData.reduce(minX).x - 3
      var y = nodeData.reduce(minY).y - 3
      console.log(`minX = ${x}; minY = ${y}`)
      function moveNode(jsNode) { return 'translate('+(jsNode.x-x)+','+(jsNode.y-y)+')' }
      function drawPath(jsLink) {
          var s = jsLink.source
          var t = jsLink.target
          // priority for preventing code duplication over less independency
          return PairSvg.linkPath(jsLink.mid, s.x - x, s.y-y, t.x - x, t.y -y)
      }
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
      console.log(new Date().getMilliseconds())
  }
  function minX (min, node) { return min.x < node.x ? min : node }
  function minY (min, node) { return min.y < node.y ? min : node }

  // define forces with the collected data

  var forceLink = d3
    .forceLink(linkData)
    .strength(50)
    .distance(11.5)
    .iterations(30)
  d3.forceSimulation(nodeData)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", forceLink)
    .force("center", d3.forceCenter(cx, cy))
    .alpha(0.0035)
    .on("tick", onTick)
    .on("end", moveToNW)
}
