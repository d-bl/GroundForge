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

// converts x/y of Diagram.nodes to https://github.com/d3/d3-force/#simulation_nodes
function nodesToJS(sNodes) {
  var result = new Array(sNodes.size())
  for ( i = sNodes.size() ; i-- > 0 ; ){
    link = sNodes.apply(i)
    result[i] = {
      x: link.x(),
      y: link.y()
    }
  }
//  print("== nodes == "+JSON.stringify(result))
  return result
}

// converts Diagram.links for https://github.com/d3/d3-force/#links
function linksToJS(sLinks) {
  var result = new Array(sLinks.size())
  for ( i = sLinks.size() ; i-- > 0 ; ){
    link = sLinks.apply(i)
    result[i] = {
      source: link.source(),
      target: link.target(),
      weak: link.weak()
    }
  }
//  print("== links == "+JSON.stringify(result))
  return result
}

// nudges the x/y values in the converted Diagram.nodes
function applyForce(barrier, center, nodes, links) {
  var forceLink = d3
    .forceLink(links)
    .strength(function(link){ return link.weak? 5 : 50 })
    .distance(12)
    .iterations(30)
  d3.forceSimulation(nodes)
    .force("charge", d3.forceManyBody().strength(-1000))
    .force("link", forceLink)
    .force("center", d3.forceCenter(center.x(), center.y()))
    .alpha(0.0035)
    .on("end", function() { barrier.await() })
}
