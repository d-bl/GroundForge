function load() {
    showThread(show(window.location.search.substr(1)))
}
function show(q) {
    var cfg = TilesConfig(q)

    // dimensions for an A1
    var width = 2245
    var height = 3178

    var zoom = 1.9
    var itemMatrix = cfg.getItemMatrix
    var svg = PairSvg.render(itemMatrix, width, height, zoom)
    d3.select('#def').attr("href","pattern.html?"+q)
    d3.select('#enum').html(PairSvg.legend(itemMatrix))
    d3.select('#pair').html(svg)
    d3.select('#forces').on("click",function () {
        nudgePairs('#pair', cfg.totalCols*6,cfg.totalRows*6)
    })
    q.split("&").find(whiting)
    return cfg
}
function redrawThreads(){
    var q = d3.select('#def').attr('href')
    showThread(TilesConfig(q))
}
function showThread(cfg) {
    // dimensions for an A1
    var width = 2245
    var height = 3178

    var opacity = 0
    var stroke = 2
    var pairDiagram = NewPairDiagram.create(cfg) // old style pair diagram
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    showGraph(d3.select('#thread'), threadDiagram, stroke, width, height, opacity)
    d3.select('#thread g').attr("transform","scale(0.5,0.5)")
}
function maximize(containerId) {
    d3.select(containerId).style("width","100%").style("height","100%")
    return false;
}
function minimize(containerId) {
    d3.select(containerId).style("width","250px").style("height","250px")
    return false;
}
function paintStitchValue () {

  return d3.select("#paintStitches").node().value.toLowerCase().replace(/[^ctlrp-]/g,'')
}
function flipStitch() {
  var n = d3.select('#paintStitches').node()
  n.value=n.value.toLowerCase().replace(/l/g,"R").replace(/r/g,"l").replace(/R/g,"r")
  return false;
}
function clickedStitch(event) {

    var id = event.currentTarget.getElementsByTagName("title")[0].innerHTML.replace(/.* /,"")
    var replacement = `${id}=${paintStitchValue()}`
    var search = new RegExp(`${id}=[ctlr]+`,'g')
    let attr = d3.select('#def').attr('href');
    if (search.test(attr))
        q = attr.replace(search,replacement)
    else
        q = attr + "&" + replacement
    d3.select('#thread').node().innerHTML = ''
    show(q)
}
function setAllStitches() {
    var replacement = `=${paintStitchValue()}`
    var search = new RegExp(`=[ctlr]+`,'g')
    let q = d3.select('#def').attr('href')
        .replace(search,replacement)
    show(q)
}
function setIgnoredStitches() {
    var replacement = `=${paintStitchValue()}`
    var search = new RegExp(`=-`,'g')
    let q = d3.select('#def').attr('href')
        .replace(search,replacement)
    show(q)
}
function whiting (kv) {
    var k = kv.trim().replace(/[^a-zA-Z0-9]/g,"")
    if (!kv.trim().startsWith("whiting")) return false
    // side effect: add whiting link
    var pageNr = kv.split("P")[1]
    var cellNr = kv.split("_")[0].split("=")[1]
    var w = d3.select('#whiting')
    w.style("display","inline-block")
    w.node().innerHTML =
        "<img src='/gw-lace-to-gf/w/page" + pageNr + "a.gif' title='"+cellNr+"'>"+
        " Page <a href='http://www.theedkins.co.uk/jo/lace/whiting/page" + pageNr + ".htm'>" + pageNr + "</a> "+
        "of '<em>A Lace Guide for Makers and Collectors</em>' by Gertrude Whiting; 1920."
    return true
}
function clickedThread(event) {
    var threadSegments = d3.selectAll("#thread ." + event.currentTarget.textContent.replace(" ",""))
    var color = d3.select('#threadColor').node().value
    threadSegments.style("stroke", color)
    threadSegments.filter(".node").style("fill", color)
}

