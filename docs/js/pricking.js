function changeWeight(linkElement) {
    var linlId = linkElement.getElementsByTagName("line")[0].attributes["id"].value
    var linkdefs = document.getElementById("linkdefs")
    var weight = 1.0 * document.getElementById("weight")
    alert(`changing length of ${lineId} is not yet implemented`)
    linkdefs.innerHTML = TopoLink.changeWeight(lineId, weight, linkdefs.innerHTML)
    //showDiagram(linkdefs.innerHTML)
}
function load() {
    var links = "b4,a1,lo,ri,1.0;d4,a1,lo,li,1.0;b2,a3,lo,ri,1.0;d2,a3,lo,li,1.0;a1,b1,lo,li,1.0;b4,b1,ro,ri,1.0;b1,b2,lo,li,1.0;c1,b2,lo,ri,1.0;a3,b4,lo,li,1.0;c3,b4,lo,ri,1.0;b1,c1,ro,li,1.0;d4,c1,ro,ri,1.0;b2,c3,ro,li,1.0;d3,c3,lo,ri,1.0;a1,d2,ro,ri,1.0;c1,d2,ro,li,1.0;a3,d3,ro,ri,1.0;d2,d3,ro,li,1.0;c3,d4,ro,li,1.0;d3,d4,ro,ri,1.0"
    document.getElementById("linkdefs").innerHTML = links
    return // TODO so far until compiled

    var links = TopoLink.fromUrlQuery(window.location.search.substr(1))
    document.getElementById("linkdefs").innerHTML = TopoLink.asSTring(links)
    showDiagram(links)
}
function showDiagram(linkdefs) {
    var elem =  document.getElementById("diagrams")
    var links = TopoLink.fromString(linkdefs)
    if (links.length == 0) {
        elem.innerHTML = "whoops"
    } else {
        var data = Data.create(links)
        if (data.length == 0) {
            elem.innerHTML = "whoops"
        } else {
            var deltas = nullspace(array2mat(data)))
            elem.innerHTML = SvgPricking.create(links, deltas)
        }
    }
}