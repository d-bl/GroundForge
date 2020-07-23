function changeWeight(linkElement) {
    var lineId = linkElement.getElementsByTagName("line")[0].attributes["id"].value
    var linkdefs = document.getElementById("linkdefs")
    var weight = 1.0 * document.getElementById("weight")
    console.log("changing length of " + lineId + " is not yet implemented")
    linkdefs.innerHTML = TopoLink.changeWeight(lineId, weight, linkdefs.innerHTML)
}
function load() {
    // var links = TopoLink.fromUrlQuery(window.location.search.substr(1))
    var links = "b4,a1,f,t,1.0;d4,a1,t,t,1.0;b2,a3,f,t,1.0;d2,a3,t,t,1.0;a1,b1,t,t,1.0;b4,b1,f,f,1.0;b1,b2,t,t,1.0;c1,b2,f,t,1.0;a3,b4,t,t,1.0;c3,b4,f,t,1.0;b1,c1,t,f,1.0;d4,c1,f,f,1.0;b2,c3,t,f,1.0;d3,c3,f,t,1.0;a1,d2,f,f,1.0;c1,d2,t,f,1.0;a3,d3,f,f,1.0;d2,d3,t,f,1.0;c3,d4,t,f,1.0;d3,d4,f,f,1.0"
    document.getElementById("linkdefs").innerHTML = links

    console.log("laloprint " + laloprint(rand(3), true))
}