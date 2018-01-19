function load() {
  var nr = 1
  var location = (window.location.href + "").replace("#","")
  var container = document.getElementById("patterns")
  var multiSheet = new dibl.SheetSVG(3, "width='210mm' height='297mm'")

  // for each key-value pair in the URL query
  location.replace(/[?&]+([^=&]+)(=([^&]*))?/gi, function(m,key,m2,value) {
    var values = decodeURIComponent(value).replace(/[+]/g, " ").split(";")
    if (values.length == 2) {
      var singleSheet = new dibl.PatternSVG(2, "height='100mm' width='100mm'", "PATTERN" + nr++)
      multiSheet.add(values[0], values[1])
      singleSheet.add(values[0], values[1])
      container.innerHTML += (singleSheet.toSvgDoc().trim())
    }
  })
  document.getElementById("download").href = 'data:image/svg+xml,' +
   encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' +
    multiSheet.toSvgDoc().trim())
}
