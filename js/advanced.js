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
  var threadScale = 0.6 //document.getElementById('threadScale').value * 1
  diagram.showGraph({
    container: '#pairs',
    width: document.getElementById('pairWidth').value,
    height:  document.getElementById('pairHeight').value,
    nodes: data.pairNodes,
    links: data.pairLinks,
    scale: pairScale,
    transform: "translate(0,0)scale(" + pairScale + ")",
  })
  diagram.showGraph({
    threadColor: '#color',
    container: '#threads',
    width: document.getElementById('threadWidth').value,
    height:  document.getElementById('threadHeight').value,
    nodes: data.threadNodes,
    links: data.threadLinks,
    scale: threadScale,
    transform: "translate(0,0)scale(" + threadScale + ")",
  })
}
function setMatrix(x){
   document.getElementById('matrix').value = x.options[x.selectedIndex].value.replace(/ +/g,'\n')
   document.getElementById('bricks').checked = true
   x.selectedIndex = 0
   load()
}
function init() {
  var location = (window.location.href + "").replace("#","")
  if (location.indexOf("?") >= 0) {
    location.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        var fields = document.getElementsByName(key)
        if (fields.length > 0) {
            fields[0].value = decodeURIComponent(value).replace(/[+]/g," ")
        }
    })
  }
}
