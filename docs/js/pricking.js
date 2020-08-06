function clickedDot(linkElement) {
    var dotId = linkElement.getElementsByTagName("circle")[0].attributes["id"].value
    var dot = document.getElementById(dotId)
    var color = document.getElementById("color").value
    var stitch = document.getElementById("stitch").value
    dot.getElementsByTagName("title")[0].innerHTML = `${dotId} : ${stitch} (not yet part of custom link)`
    dot.style = `fill:${color};opacity:0.65`
}
function clickedLink(linkElement) {
    var lineId = linkElement.getElementsByTagName("line")[0].attributes["id"].value
    var link = document.getElementById("customlink")
    var weight = document.querySelector('input[name = "weight"]:checked').value
    var topolinks = TopoLink.changeWeight(lineId, weight, link.href.replace(/.*=/,""))
    link.href = "?topo=" + topolinks
    showDiagram(topolinks)
}
function load() {
    var urlQuery = location.search.substr(1)
    var topolinks = typeof urlQuery === "undefined" || urlQuery.trim() == ""
        ? "lo,b4,ri,a1;lo,d4,li,a1;lo,b2,ri,a3;lo,d2,li,a3;lo,a1,li,b1;ro,b4,ri,b1;lo,b1,li,b2;lo,c1,ri,b2;lo,a3,li,b4;lo,c3,ri,b4;ro,b1,li,c1;ro,d4,ri,c1;ro,b2,li,c3;lo,d3,ri,c3;ro,a1,ri,d2;ro,c1,li,d2;ro,a3,ri,d3;ro,d2,li,d3;ro,c3,li,d4;ro,d3,ri,d4"
        : TopoLink.asString(TopoLink.fromUrlQuery(urlQuery))
    document.getElementById("customlink").href = "?topo=" + topolinks
    showDiagram(topolinks)
}
function showDiagram(linkdefs) {
    var elem =  document.getElementById("diagram")
    var links = TopoLink.fromString(linkdefs)
    var data = Data.create(links)
    console.log(data.toString())
    if (data.length == 0) {
//        elem.outerHTML = "<p><strong>whoops</strong></p>"
    } else {
//        var deltas = array2mat(rand(data.length,2)) // TODO infinite nullspace(array2mat(data))
//        elem.innerHTML = SvgPricking.create(links, deltas)
    }
}
function setDownloadContent (linkNode) {
  var svg = document.getElementById("diagram").innerHTML
  linkNode.href = 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
