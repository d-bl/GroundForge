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
function getValueOfDropDown(id, defaultValue) {
  var e = document.getElementById(id)
  return e.selectedIndex >=0 ? e.options[e.selectedIndex].value : defaultValue
}
function load() {
  fullyTransparant = document.getElementById('transparency').value
  document.getElementById('pairs').innerHTML = ""
  document.getElementById('threads').innerHTML = ""

  var nrOfRows = document.getElementById('rows').value
  var nrOfCols = document.getElementById('cols').value
  var shiftLeft = document.getElementById('left').value
  var shiftUp = document.getElementById('up').value
  var matrix = document.getElementById('matrix').value
  var stitches = document.getElementById('stitches').value
  var tileType = getValueOfDropDown('tiles', '')

  var startTime = new Date().getTime()
  var data = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitches, tileType)
  console.log ("D3Data.get elapse time "+(new Date().getTime() - startTime))

  var colors = ''
  for(i=1; i <= 16 ; i++) {
    var el = document.getElementById('color' + i)
    el.style.backgroundColor = '#' + el.value
    colors += ',#'+ el.value
  }
  colors = colors.replace(/,#FFFFFF/g,'').replace(/^,/,'')

  diagram.showGraph({
    container: '#pairs',
    nodes: data.pairNodes(),
    links: data.pairLinks(),
    viewWidth: 440,
    viewHeight: 260
  })
  diagram.showGraph({
    container: '#threads',
    nodes: data.threadNodes(),
    links: data.threadLinks(),
    threadColor: '#color',
    palette: colors,
    viewWidth: 440,
    viewHeight: 260
  })
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
var lastShown = "thumbnails"
function toggle(id, radio){
  if (lastShown != undefined ) {
    var el1a = document.getElementById(lastShown+"FieldSet")
    el1a.classList.remove('show')
    el1a.classList.add('hide')
    var el1b = document.getElementById(lastShown+"Tab")
    el1b.classList.remove('activeTab')
    el1b.classList.add('inactiveTab')
  }
  var el2a = document.getElementById(id+"FieldSet")
  el2a.classList.remove('hide')
  el2a.classList.add('show')
  var el2b = document.getElementById(id+"Tab")
  el2b.classList.remove('inactiveTab')
  el2b.classList.add('activeTab')
  lastShown = id
}
function init() {
  var location = (window.location.href + "").replace("#","")
  var patterns = new dibl.PatternSheet(2, "height='210mm' width='297mm'")

  // for each key-value pair in the URL query
  location.replace(/[?&]+([^=&]+)(=([^&]*))?/gi, function(m,key,m2,value) {

      // assign the value to the field that has the key as id
      var fields = document.getElementsByName(key)
      var val = decodeURIComponent(value).replace(/[+]/g, " ")
      if (fields.length > 0) {
        if (fields[0].type != "select-one")
          fields[0].value = val
        else
          for(index = 0 ; index < fields[0].length ; index++)
            if(fields[0][index].value == val) {
              fields[0].selectedIndex = index
              break
            }
      }
      // create pattern sheet
      if (key && key == 'patch') {
         // a patch argument is used for the pattern sheet, not for a form field
         // we have to split the value in the matrix and optional type of tiles
         var patchArgs = val.split(";")
         patterns.add(patchArgs[0], patchArgs[1] ? patchArgs[1] : "checker")
      }
  })
  var tiles = document.getElementById("tiles").value
  patterns.add(document.getElementById("matrix").value, tiles ? tiles : "checker")
  document.getElementById("sheet").innerHTML = (patterns.toSvgDoc().trim())
}
function updatePatternSheet() {
  var tiles = document.getElementById("tiles").value
  var patterns = new dibl.PatternSheet(2, "height='140mm' width='180mm'")
  patterns.add(document.getElementById("matrix").value, tiles ? tiles : "checker")
  document.getElementById("sheet").innerHTML = (patterns.toSvgDoc().trim())
}
function setDownloadContent (comp, id) {
  var container = document.getElementById(id)
  if (!container) return
  if (container.firstElementChild.localName != "svg") return
  var svg = id == 'sheet'
    ? container.innerHTML
    : container.innerHTML.
      replace('pointer-events="all"', '').
      replace(/<path [^>]+opacity: 0;.+?path>/g, '')
  comp.href = 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
