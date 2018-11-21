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
function loadUrlArgs() {
  var location = (window.location.href + "").replace("#","")
  // for each key-value pair in the URL query
  location.replace(/[?&]+([^=&]+)(=([^&]*))?/gi, function(m,key,m2,value) {
  val = decodeURIComponent(value).replace(/[+]/g, " ")
    var match = key.match(/s[1-3]/)
    if(key=="m") setMatrix(value)
    else if(match && match.length > 0)
      document.getElementById(key).value = val
      // support for backward compatible links:
    else if(key == "stitches")
      document.getElementById("s1").value = val
    else if(key == "left")
      document.getElementById("shiftLeft").value = val
    else if(key == "up")
      document.getElementById("shiftUp").value = val
    else if(key == "rows" || key == "cols" || key == "matrix")
      document.getElementById(key).value = val
    else if( key == "tiles")
      setTiling(val)
  })
}
function setTiling (val) {
  var el = document.getElementById("tiles")
  for(index = 0 ; index < el.length ; index++)
    if(el[index].value == val) {
      el.selectedIndex = index
      break
    }
}
function createUrlArgs() {
    var result = "index.html?m=" +
      encodeURIComponent(
        document.getElementById("matrix").value +";" +
        document.getElementById("tiles").value +";" +
        document.getElementById("rows").value +";" +
        document.getElementById("cols").value +";" +
        document.getElementById("shiftLeft").value +";" +
        document.getElementById("shiftUp").value
      ) +"&s1=" +
      encodeURIComponent(document.getElementById("s1").value) +"&s2=" +
      encodeURIComponent(document.getElementById("s2").value) +"&s3=" +
      encodeURIComponent(document.getElementById("s3").value)
    return result
}
function createPrickingArgs() {
    var result = "sheet.html?patch=" +
      encodeURIComponent(
        document.getElementById("matrix").value +";" +
        document.getElementById("tiles").value
      )
    return result
}
function setMatrix(value) {
  var p = decodeURIComponent(value).split(";")
  if (p.length == 6) {
      document.getElementById("matrix").value = p[0]
      setTiling(p[1])
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
function toggle(el,id) {
  el.className = el.className.replace("showi","hide")
  el2 = document.getElementById(id)
  el2.className = el2.className.replace("hide","showi")
}
function toggleMore(el,id) {
  toggle(el, id)
  document.getElementById("examples-content").className = "showi"
}
function toggleLess(el,id) {
  toggle(el, id)
  document.getElementById("examples-content").className = "hide"
}
var data = [0,1,2,3,4]
function firstStep() {
  replaceClass("step1", "hide","show")
  replaceClass("step2", "show", "hide")
  replaceClass("step3", "show", "hide")

  var p = dibl.PairDiagram().get(
    document.getElementById("matrix").value,
    document.getElementById("tiles").value,
    document.getElementById("rows").value,
    document.getElementById("cols").value,
    document.getElementById("shiftLeft").value,
    document.getElementById("shiftUp").value,
    document.getElementById("s1").value
  )
  showDiagrams(1, p)
}
function nextStep(n) {
  replaceClass("step"+n, "hide","show")
  if (n==2)
    replaceClass("step3", "show","hide")

  var stitches = document.getElementById("s" + n).value
  showDiagrams(n, dibl.PairDiagram().create(stitches, data[n-1].threadDiagram))
}
function showDiagrams(stepNr, pairDiagram){
  var threadDiagram = dibl.ThreadDiagram().create(pairDiagram)
  data[stepNr] = { "pairDiagram": pairDiagram, "threadDiagram": threadDiagram }
  showDiagram("#p" + stepNr, "1px", pairDiagram, "")
  showDiagram("#t" + stepNr, "2px", threadDiagram, getColors("s" + stepNr + "c"))
}
function showDiagram(id, threadWidth, data, colors) {
  diagram.showGraph({
    container: d3.select(id),
    nodes: data.jsNodes(),
    links: data.jsLinks(),
    diagram: data,
    stroke: threadWidth,
    palette: colors
  })
}
function setDownloadContent (comp, id) {
  svg = document.getElementById(id).innerHTML.
      replace('pointer-events="all"', '').
      replace(/<path [^>]+opacity: 0;.+?path>/g, '')
  comp.href = 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
function getColors (idPrefix) {
  var colors = ''
  for(i=1; i <= 16 ; i++) {
    var el = document.getElementById(idPrefix + i)
    el.style.backgroundColor = '#' + el.value
    colors += ',#'+ el.value
  }
  return colors.replace(/,#FFFFFF/g,'').replace(/^,/,'')
}
function onChangeColor(el) {
  if (el.value == 'FFFFFF') {
    var i = el.id.replace('color','') * 1
    for (i = i+1 ; i <=16 ; i++) {
      el2 = document.getElementById('color' + i)
      el2.value = 'FFFFFF'
      el2.style.backgroundColor = '#FFFFFF'
      el2.style.color = '#000000'
    }
  }
}

