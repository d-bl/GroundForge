function load() {

    let q = window.location.search.substr(1)+""
    let w = q.replace(/.*patchWidth=/,"").replace(/&.*/,"");
    let h = q.replace(/.*patchHeight=/,"").replace(/&.*/,"");
    d3.select("#patchHeight").attr("value",h)
    d3.select("#patchWidth").attr("value",w)
    showThread(show(window.location.search.substr(1)))
}
function applyForces() {
    var cfg = TilesConfig(getQ())
    nudgePairs('#pair', cfg.totalCols*6,cfg.totalRows*6)
}
function getQ() {
    return d3.select('#to_pattern').attr('href').replace(/.*[?]/, "");
}

function dimChanged() {
    let q = getQ()
    let w = d3.select("#patchWidth").node().value
    let h = d3.select("#patchHeight").node().value
    q = getQ()
        .replace(new RegExp('patchWidth=[0-9]+'),'patchWidth='+w)
        .replace(new RegExp('patchHeight=[0-9]+'),'patchHeight='+h)
    showThread(show(q))
}
function show(q) {
    var cfg = TilesConfig(q)

    // dimensions for an A1
    let width = 2245
    let height = 3178

    let zoom = 1.9
    let itemMatrix = cfg.getItemMatrix
    let svg = PairSvg.render(itemMatrix, width, height, zoom)
    d3.select('#to_self').attr("href","stitches?"+q)
    d3.select('#to_pattern').attr("href","pattern.html?"+q)
    d3.select('#to_droste').attr("href","droste.html?"+q)
    d3.select('#enum').html(PairSvg.legend(itemMatrix))
    d3.select('#pair').html(svg)
    q.split("&").find(whiting)
    return cfg
}
function redrawThreads(){
    showThread(TilesConfig(getQ()))
}
function showThread(cfg) {
    var pairDiagram = NewPairDiagram.create(cfg) // old style pair diagram
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    showGraph('#thread', threadDiagram)
    d3.select('#thread g').attr("transform","scale(0.5,0.5)")
}
function maximize(containerId) {
    d3.select(containerId).style("width","100%").style("height","90vh")
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
    let attr = getQ();
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
    show(getQ().replace(search, replacement))
}
function setIgnoredStitches() {
    var replacement = `=${paintStitchValue()}`
    var search = new RegExp(`=-`,'g')
    show(getQ().replace(search, replacement))
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

