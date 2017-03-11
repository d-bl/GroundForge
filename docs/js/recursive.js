/*
 Copyright 2016 Jo Pol
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
var data = [0,1,2,3,4]
function getRadioVal(radioName) {
  var rads = document.getElementsByName(radioName);

  for(var i=0; i<rads.length; i++) {
    if(rads[i].checked)
      return rads[i].value;
  }
  return null;
}
function setMatrix(value) {
  var p = unescape(value).split(";")
  if (p.length == 6) {
      document.getElementById("matrix").value = p[0]
      document.getElementById("tiles").value = p[1]
      document.getElementById("rows").value = p[2]
      document.getElementById("cols").value = p[3]
      document.getElementById("shiftLeft").value = p[4]
      document.getElementById("shiftUp").value = p[5]
  }
}
function pairDiagram(value) {
  var matrix = document.getElementById("matrix").value
  var tiling = document.getElementById("tiles").value
  var nrOfRows = document.getElementById("rows").value
  var nrOfCols = document.getElementById("cols").value
  var shiftLeft = document.getElementById("shiftLeft").value
  var shiftUp = document.getElementById("shiftUp").value

  var stitch = document.getElementById("s1").value
  data[1] = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitch, tiling)
  diagram.showGraph({
    container: d3.select('#d0'),
    nodes: data[1].pairNodes(),
    links: data[1].pairLinks(),
    viewWidth: 360,
    viewHeight: 160,
    diagram: data[1].pairDiagram,
    stroke: "1px"
  })
  diagram.showGraph({
    container: d3.select('#d1'),
    nodes: data[1].threadNodes(),
    links: data[1].threadLinks(),
    threadColor: '#color',
    viewWidth: 360,
    viewHeight: 160,
    diagram: data[1].threadDiagram,
    stroke: "2px"
  })
}
function threadDiagram(n) {
  var stitch = document.getElementById("s" + n).value
  data[n] = dibl.D3Data().get(stitch, data[n-1])
  document.getElementById('d'+n).innerHTML = ""
  diagram.showGraph({
    container: d3.select('#d' + n),
    nodes: data[n].threadNodes(),
    links: data[n].threadLinks(),
    threadColor: '#color',
    viewWidth: 660,
    viewHeight: 300,
    diagram: data[n].threadDiagram,
    stroke: "2px"
  })
}
