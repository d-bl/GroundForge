
/**
 * container: d3.selection of element containing an SVG generated with PairSvg.create
 * in other words: the primary pair diagram
 */
function nudgePairs(containerId) {

    var svg = d3.select(containerId+" svg")
    svg.select("#cloned").attr("transform").replace("matrix(","").replace(/,.*/,"")

    nudgeDiagram(svg);
}

/**
 * @param svg has
 *  - elements with
 *    - class node
 *    - an id attribute
 *    - an attribute transform="translate(x,y)"
 *  - elements with
 *    - class link
 *    - an id attribute containing the IDs of their nodes separated with '-'
 *    - an attribute d defining a path with or without a midpoint
 * See also
 *   https://devdocs.io/d3~4/d3-force
 *   https://devdocs.io/d3~4/d3-selection
 */
function nudgeDiagram(svg) {

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
    var ids = n.attributes["id"].nodeValue.split("-")
    return {
      sectionType: n.attributes["class"].nodeValue.replace(/.*White/,"White").replace(/ .*/,""),
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
      return DiagramSvg.linkPath(jsLink.sectionType, s.x, s.y, t.x, t.y)
  }
  var tickCounter = 0
  function onTick() {
      if (0 !==  (++tickCounter % 10) ) return
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
  }

  // final position of diagram

  function moveToNW() {
      function minX (min, node) { return min.x < node.x ? min : node }
      function minY (min, node) { return min.y < node.y ? min : node }

      function notFloating(min) {
          if (min.index === undefined) return true;
          let s = nodes.nodes()[min.index?min.index:0].textContent;
          return !(s.endsWith('- ') || s.startsWith("Pair") || s.startsWith("thread"));
      }
      // console.log(new Date().getMilliseconds())
      let filtered = nodeData.filter(notFloating);
      if (filtered.length === 0) return; // Exit if no nodes are left after filtering
      var x = filtered.reduce(minX).x - 3
      var y = filtered.reduce(minY).y - 3
      function moveNode(jsNode) { return 'translate('+(jsNode.x-x)+','+(jsNode.y-y)+')' }
      function drawPath(jsLink) {
          var s = jsLink.source
          var t = jsLink.target
          return DiagramSvg.linkPath(jsLink.sectionType, s.x - x, s.y-y, t.x - x, t.y -y)
      }
      links.attr("d", drawPath);
      nodes.attr("transform", moveNode);
      // console.log(new Date().getMilliseconds())
  }

  // define forces with the collected data
  d3.forceSimulation(nodeData)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", d3
        .forceLink(linkData)
        .strength(50)
        .distance(11.5)
        .iterations(30))
    .force("center", d3.forceCenter(100, 100))
    .alpha(0.0035)
    .on("tick", onTick)
    .on("end", moveToNW)
}
