/*
 Copyright 2017 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
function load() {

  var matrix = "586- -789 2111 -4-4"
  var nrOfRows = 12
  var nrOfCols = 9
  var shiftLeft = 1
  var shiftUp = 1
  var stitches = ''

  var patterns = new dibl.PatternSheet(2, "height='90mm' width='100mm'")
  patterns.add(matrix, "checker")
  document.getElementById("sheet").innerHTML = (patterns.toSvgDoc().trim())

  var data = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitches, "checker")
  showGraph({
    container: d3.select('#pairs'),
    nodes: data.pairNodes(),
    links: data.pairLinks(),
    diagram: data.pairDiagram,
    stroke: "1px"
  })
  showGraph({
    container: d3.select('#threads'),
    nodes: data.threadNodes(),
    links: data.threadLinks(),
    diagram: data.threadDiagram,
    stroke: "2px"
  })
}
function showGraph(args) {
    var svg = dibl.SVG()
    var markers = true // use false for slow devices and IE-11, set them at onEnd
    args.container.node().innerHTML = svg.render(args.diagram, args.stroke, markers, 400, 400)
    var links = args.container.selectAll(".link").data(args.links)
    var nodes = args.container.selectAll(".node").data(args.nodes)
    function moveNode(jsNode) {
        return 'translate('+jsNode.x+','+jsNode.y+')'
    }
    function drawPath(jsLink) {
        var s = jsLink.source
        var t = jsLink.target
        var l = args.diagram.link(jsLink.index)
        return  svg.pathDescription(l, s.x, s.y, t.x, t.y)
    }
    function onTick() {
        links.attr("d", drawPath);
        nodes.attr("transform", moveNode);
        count++
    }
    function onEnd() {
        console.log("time for " + count + " ticks " + (new Date().getTime() - tickStart))
    }
    var count = 0
    var tickStart = new Date().getTime()

    // duplication of src/main/resources/force.js.applyForce(...)
    function strength(link){ return link.weak ? 1 : 50 }
    d3.forceSimulation(args.nodes)
      .force("charge", d3.forceManyBody().strength(-1000))
      .force("link", d3.forceLink(args.links).strength(strength).distance(12).iterations(30))
      .force("center", d3.forceCenter(200, 200))
      .alpha(0.0035)
      .on("tick", onTick)
      .on("end", onEnd)
}
