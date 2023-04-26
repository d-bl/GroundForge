function load() {

    let q = window.location.search.substr(1)+""
    if(!q)
        q = 'patchWidth=8&patchHeight=8&footside=r,1&tile=888,111&headside=8,r&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=1&shiftRowsSE=2&a1=ctctctcll&j2=ctctctcrr&b2=ct'
    let w = q.replace(/.*patchWidth=/,"").replace(/&.*/,"");
    let h = q.replace(/.*patchHeight=/,"").replace(/&.*/,"");
    d3.select("#patchHeight").attr("value",h)
    d3.select("#patchWidth").attr("value",w)
    setColorCode()
    showThread(show(q))
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
    d3.select('#thread').node().innerHTML = ''
    q.split("&").find(whiting)
    return cfg
}
function redrawThreads(){
    showThread(TilesConfig(getQ()))
    d3.selectAll("#thread .node").on("click",clickedNode)
}
function showThread(cfg) {
    var pairDiagram = NewPairDiagram.create(cfg) // old style pair diagram
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    showGraph('#thread', threadDiagram)
    d3.select('#thread g').attr("transform","scale(0.5,0.5)")
    d3.selectAll("#thread .node").on("click",clickedNode)
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

  return d3.select("#stitchDef").node().value.toLowerCase().replace(/[^ctlrp-]/g,'')
}
function setColorCode() {
    d3.select("#colorCode").html(`
        <svg width="20" height="25">
          <g transform="scale(2,2)">
            <g transform="translate(5,6)">
              ${PairSvg.shapes(d3.select("#stitchDef").node().value)}
            </g>
          </g>
        </svg>`)
}
function flip2d() {
  var n = d3.select('#stitchDef').node()
  n.value = n.value.toLowerCase().replace(/l/g,"R").replace(/r/g,"L").toLowerCase()
  setColorCode()
  n.focus()
  return false;
}
function flip2p() {
  var n = d3.select('#stitchDef').node()
  n.value = n.value.toLowerCase().split("").reverse().join("")
  setColorCode()
  n.focus()
  return false;
}
function flip2q() {
  flip2d()
  flip2p()
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
function clickedNode(event) {
    const selectedClass = d3.event.currentTarget.classList.toString().replace(/ *node */,'')
    if (selectedClass == "threadStart") return
    var color = d3.select('#threadColor').node().value
    d3.selectAll("#thread ." + selectedClass)
        .style("stroke", color).style("fill", color).style('opacity',"0.4")
}

