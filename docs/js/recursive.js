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
function loadUrlArgs() {
  var location = (window.location.href + "").replace("#","")
  // for each key-value pair in the URL query
  location.replace(/[?&]+([^=&]+)(=([^&]*))?/gi, function(m,key,m2,value) {
    if(key=="m") setMatrix(value)
    else if(key.match(/s[1-3]/).length > 0)
      document.getElementById(key).value = decodeURIComponent(value).replace(/[+]/g, " ")
  })
}
function createUrlArgs() {
    var result = "recursive.html?m=" +
      escape(document.getElementById("matrix").value) +";" +
      document.getElementById("tiles").value +";" +
      document.getElementById("rows").value +";" +
      document.getElementById("cols").value +";" +
      document.getElementById("shiftLeft").value +";" +
      document.getElementById("shiftUp").value +"&s1=" +
      document.getElementById("s1").value +"&s2=" +
      document.getElementById("s2").value +"&s3=" +
      document.getElementById("s3").value
    return result
}
function setMatrix(value) {
  var p = unescape(value).split(";")
  if (p.length == 6) {
      document.getElementById("matrix").value = decode(p[0])
      document.getElementById("tiles").value = p[1]
      document.getElementById("rows").value = p[2]
      document.getElementById("cols").value = p[3]
      document.getElementById("shiftLeft").value = p[4]
      document.getElementById("shiftUp").value = p[5]
      replaceClass("step1", "show","hide")
      replaceClass("step2", "show", "hide")
      replaceClass("step3", "show", "hide")
  }
}
function replaceClass(select, oldValue, newValue) {
  var x = document.getElementsByClassName(select);
  var i;
  for (i = 0; i < x.length; i++) {
      x[i].className = x[i].className.replace(oldValue,newValue)
  }
}
function pairDiagram(value) {
  var matrix = document.getElementById("matrix").value
  var tiling = document.getElementById("tiles").value
  var nrOfRows = document.getElementById("rows").value
  var nrOfCols = document.getElementById("cols").value
  var shiftLeft = document.getElementById("shiftLeft").value
  var shiftUp = document.getElementById("shiftUp").value
  replaceClass("step1", "hide","show")
  replaceClass("step2", "show", "hide")
  replaceClass("step3", "show", "hide")

  var stitch = document.getElementById("s1").value
  data[1] = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitch, tiling)
  diagram.showGraph({
    container: d3.select('#d0'),
    nodes: data[1].pairNodes(),
    links: data[1].pairLinks(),
    diagram: data[1].pairDiagram,
    stroke: "1px"
  })
  diagram.showGraph({
    container: d3.select('#d1'),
    nodes: data[1].threadNodes(),
    links: data[1].threadLinks(),
    threadColor: '#color',
    diagram: data[1].threadDiagram,
    stroke: "2px"
  })
}
function threadDiagram(n) {
  var stitch = document.getElementById("s" + n).value
  replaceClass("step"+n, "hide","show")
  if (n==2)
    replaceClass("step3", "show","hide")
  data[n] = dibl.D3Data().get(stitch, data[n-1])
  document.getElementById('d'+n).innerHTML = ""
  diagram.showGraph({
    container: d3.select('#d' + n),
    nodes: data[n].threadNodes(),
    links: data[n].threadLinks(),
    threadColor: '#color',
    diagram: data[n].threadDiagram,
    stroke: "2px"
  })
}
