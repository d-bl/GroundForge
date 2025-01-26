function load() {

    let q = window.location.search.substr(1)+""
    if(!q)
        q = 'patchWidth=8&patchHeight=8&footside=r,1&tile=888,111&headside=8,r&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=1&shiftRowsSE=2&a1=ctctctcll&j2=ctctctcrr&b2=ct'
    dimInit(q);
    // document.getElementById("helpMenuButton").focus()
    setColorCode()
    showThread(show(q))
}
function reloadPair() {
    var cfg = TilesConfig(q)
    // dimensions for an A1
    let width = 2245
    let height = 3178

    let zoom = 1.9
    let itemMatrix = cfg.getItemMatrix
    document.getElementById('pair').innerHTML = PairSvg.render(itemMatrix, width, height, zoom)
}
function getQ() {
    return d3.select('#to_self').attr('href').replace(/.*[?]/, "");
}

function getDim(q) {
    let w = q.replace(/.*patchWidth=/, "").replace(/&.*/, "");
    let h = q.replace(/.*patchHeight=/, "").replace(/&.*/, "");
    return [w,h]
}
function dimInit(q) {
    let [w,h] = getDim(q);
    d3.select("#patchHeight").attr("value", h).on("change", dimChanged)
    d3.select("#patchWidth").attr("value", w).on("change", dimChanged)
}
function dimChanged() {
    let q = getQ()
    let w = d3.select("#patchWidth").node().value
    let h = d3.select("#patchHeight").node().value
    let [wq, hq] = getDim(q)
    if (w == wq && h == hq) return // apparently first action on the page
    q = getQ()
        .replace(new RegExp('patchWidth=[0-9]+'),'patchWidth='+w)
        .replace(new RegExp('patchHeight=[0-9]+'),'patchHeight='+h)
    show(q) // TODO spoiler to reuse methods on pattern and droste
    return void(0)
}
function show(q) {
    var cfg = TilesConfig(q)

    // dimensions for an A1
    let width = 2245
    let height = 3178

    let zoom = 1.9
    let itemMatrix = cfg.getItemMatrix
    document.getElementById('to_self').href = "stitches?"+q
    document.getElementById('to_pattern').href = "pattern.html?"+q
    document.getElementById('to_droste').href = "droste.html?"+q
    document.getElementById('enum').innerHTML = PairSvg.legend(itemMatrix)
    document.getElementById('pair').innerHTML = PairSvg.render(itemMatrix, width, height, zoom)
    document.getElementById('thread').innerHTML = ''
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
}
function minimize(containerId) {
    d3.select(containerId).style("width","250px").style("height","250px")
}
function clickedStitch(event) {

    var id = event.currentTarget.getElementsByTagName("title")[0].innerHTML.replace(/.* /,"")
    var replacement = `${id}=${paintStitchValue()}`
    var search = new RegExp(`${id}=[ctlr]+`,'gi')
    let attr = getQ();
    if (search.test(attr))
        q = attr.replace(search,replacement)
    else
        q = attr + "&" + replacement
    show(q)
}
function setAllStitches() {
    var replacement = `=${paintStitchValue()}&`
    var search = new RegExp(`=[ctlr]+&`,'gi')
    var searchLast = new RegExp(`=[ctlr]+$`,'gi')
    var searchLast = new RegExp(`=[ctlr]+$`,'g')
    let q = getQ()
        .replace(search, replacement)
        .replace(searchLast, replacement)
    show(q)
    show(getQ().replace(searchLast, replacement))
}
function setIgnoredStitches() {
    var replacement = `=${paintStitchValue()}`
    let q = getQ().split('&').map((kv => replaceIgnored(kv, replacement))).join('&')
    show(q)
}
function replaceIgnored(kv, replacement) {
    if (/[a-z][0-9]+=-/.test(kv)) {
        return kv.replace(/=-/, replacement);
    }
    return kv;
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
function toggleVisibility(id) {
    console.log('toggleVisibility '+id)
    var x = document.getElementById(id);
    if (x.style.display === "block") {
        x.style.display = "none";
    } else {
        x.style.display = "block";
    }
    return void(0)
}