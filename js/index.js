function getValueOf(id, defaultValue) {
  var e = document.getElementById(id)
  return e.selectedIndex >=0 ? e.options[e.selectedIndex].value : defaultValue}
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
  var tileType = getValueOf('tiles', '')
  var data = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitches, tileType)
  var pairScale = document.getElementById('pairScale').value * 1
  var threadScale = document.getElementById('threadScale').value * 1
  var colors = ''
  for(i=1; i <= 16 ; i++)
    colors += document.getElementById('color' + i).value + ','
  colors = colors.replace(/transparent,/g,'').replace(/#FFFFFF,/g,'').replace(/^$/g,'#000000').replace(/,$/,'')

  diagram.showGraph({
    container: '#pairs',
    width: 500,
    height: 500,
    nodes: data.pairNodes,
    links: data.pairLinks,
    scale: pairScale,
    transform: "translate(0,0)scale(" + pairScale + ")",
  })
  diagram.showGraph({
    threadColor: '#color',
    container: '#threads',
    width: 700,
    height:  700,
    nodes: data.threadNodes,
    links: data.threadLinks,
    scale: threadScale,
    transform: "translate(0,0)scale(" + threadScale + ")",
    palette: colors
  })
}
function init() {
  var location = (window.location.href + "").replace("#","")
  var patterns = new dibl.PatternSheet(2, "height='210mm' width='297mm'")

  // no advanced options for default page and fully configured thumbnail links
  var iq = location.indexOf('?')
  if (iq >= 0 && iq+1 != location.length && location.indexOf('hideAdvanced') < 0 ) {
    var elems = document.getElementsByClassName('advanced')
    for (i=0 ; i<elems.length ; i++) {
        elems[i].classList.remove('hide')
        elems[i].classList.add('show')
    }
  }
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
         patterns.add(patchArgs[0], patchArgs[1])
      }
  })
  patterns.add(document.getElementById("matrix").value, document.getElementById("tiles").value)
  document.getElementById("sheet").innerHTML = (patterns.toSvgDoc().trim())
}
function setHref (comp, id) {
  var container = document.getElementById(id)
  if (!container) return
  if (container.firstElementChild.localName != "svg") return
  var svg = id == 'sheet'
    ? container.innerHTML
    : container.innerHTML.
      replace('pointer-events="all"', 'xmlns:svg="http://www.w3.org/2000/svg" ' +
                                      'xmlns="http://www.w3.org/2000/svg" ' +
                                      'xmlns:xlink="http://www.w3.org/1999/xlink"').
      replace(/transform="[^"]+"/, '').
      replace(/<rect.*?rect>/, '').
      replace(/<circle [^>]+opacity: 0;.+?circle>/g, '').
      replace(/<path [^>]+opacity: 0;.+?path>/g, '')
  comp.href = 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
