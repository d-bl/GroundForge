function load() {
  fullyTransparant = document.getElementById('transparency').value
  document.getElementById('pairs').innerHTML = ""
  document.getElementById('threads').innerHTML = ""
  var stackAsBricks = document.getElementById('bricks').checked
  var nrOfRows = document.getElementById('rows').value
  var nrOfCols = document.getElementById('cols').value
  var shiftLeft = document.getElementById('left').value
  var shiftUp = document.getElementById('up').value
  var matrix = document.getElementById('matrix').value
  var stitches = document.getElementById('stitches').value
  var data = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, stitches, stackAsBricks)
  var pairScale = document.getElementById('pairScale').value * 1
  var threadScale = document.getElementById('threadScale').value * 1
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
        if (fields[0].type != "checkbox")
          fields[0].value = val
        else {
          // the value of a flag is ignored
          // because it is also ignored when the pattern sheet is created
          fields[0].checked = true
        }
      }
      // create pattern sheet
      if (key && key == 'matrix') {
         // the matrix and bricks arguments also fill form fields
         patterns.add(val, location.indexOf('&bricks') >= 0)
      } else if (key && key == 'patch') {
         // a patch argument is only used for the pattern sheet
         // we have to split the value in the matrix and optional bricks flag
         var patchArgs = val.split(";")
         patterns.add(patchArgs[0], patchArgs[1]=='bricks')
      }
  })
  document.getElementById("sheet").innerHTML = (patterns.toSvgDoc().trim())
}
function wrapSvg(svg){
  var xmlHead = '<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->'
  return "<img src='data:image/svg+xml," + encodeURIComponent(xmlHead + svg) + "' download='diagram.svg'/>"
}
function makeDownloadable(id) {
  var container = document.getElementById(id)
  if (container) {
    if (container.firstElementChild.localName == "img") return
    var namespaces = 'xmlns:svg="http://www.w3.org/2000/svg" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"'
    var svg = container.innerHTML.replace('pointer-events="all"',namespaces)
    container.innerHTML = wrapSvg(svg)
  }
}
function allDownloadable () {
  makeDownloadable('pairs')
  makeDownloadable('threads')
  makeDownloadable('sheet')
}
