function load() {

    let q = window.location.search.substring(1)+""
    d3.select('#to_stitches').attr('href','stitches.html?'+q)
    d3.select('#to_self').attr('href','droste.html?'+q)
    setDroste(2,q)
    setDroste(3,q)
    showThread(TilesConfig(q))
}
function setDroste(level, q){
    let id = 'droste'+level
    d3.select("#" + id)
        .on('change', instructionsChanged)
    if(!q.includes(id)) return
    let s = q
        .replace(new RegExp(`.*${id}=`),"")
        .replace(/&.*/,"");
    d3.select("#" + id)
        .property('value', s)
}
function instructionsChanged(event) {
    clear2()
    clear3()
    let q = changeQ(3, changeQ(2, getQ()))
    d3.select('#to_stitches').attr('href','stitches.html?'+q)
    d3.select('#to_self').attr('href','droste.html?'+q)
}
function changeQ(level, q){
    console.log('hello')
    let id = 'droste'+level
    let s = d3.select("#droste2").property('value')
    if(q.includes(id))
        q = q.replace(new RegExp(id+'=[^&]+'),`${id}=${s}`)
    else q = `${q}&${id}=${s}`
    console.log('------'+q)
    return q
}
function getQ() {
    return d3.select('#to_stitches').attr('href').replace(/.*[?]/, "");
}
function showThread(cfg) {
    var pairDiagram = NewPairDiagram.create(cfg) // old style pair diagram
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    showGraph('#thread', threadDiagram)
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
function clear2() {
    d3.selectAll("#drostePair2, #drosteThread2, #drostePair3, #drosteThread3").html("")
    d3.selectAll(".colorCode").style("display", "none")
    d3.selectAll("#drostePair2DownloadLink, #drosteThread2DownloadLink, #drostePair3DownloadLink, #drosteThread3DownloadLink")
        .attr("href", "#?pleasePrepareFirst")
        .style("display", "none")
    return false
}
function clear3() {
    d3.selectAll("#drostePair3, #drosteThread3").html("")
    d3.selectAll(".colorCode").style("display", "none")
    d3.selectAll("#drostePair3DownloadLink, #drosteThread3DownloadLink")
        .attr("href", "#?pleasePrepareFirst")
        .style("display", "none")
    return false
}
function showDroste(level) {
    d3.select("#drosteFields" + level).style("display", "block")
    var el = d3.select("#drosteThread" + level).node().firstElementChild
    if (el && el.id.startsWith("svg")) return


    var s = d3.select("#droste" + level).node().value
    var l = PairDiagram.drosteLegend(s)
    // d3.select("#drosteFields" + level + " .colorCode").node().innerHTML = l

    var q = getQ()
    console.log("------"+q)
    // d3.select("#link").node().href = "?" + q
    var drosteThreads1 = ThreadDiagram.create(NewPairDiagram.create( TilesConfig(q)))
    var drostePairs2 = PairDiagram.create(stitches = d3.select("#droste2").node().value, drosteThreads1)
    var drosteThreads2 = ThreadDiagram.create(drostePairs2)
    // TODO the diagrams above may have been calculated before

    if (level == 2) {
        setPairDiagram("#drostePair2", drostePairs2)
        setThreadDiagram("#drosteThread2", drosteThreads2)
    } else if (level == 3) {
        var drostePairs3 = PairDiagram.create(stitches = d3.select("#droste3").node().value, drosteThreads2)
        setPairDiagram("#drostePair3", drostePairs3)
        setThreadDiagram("#drosteThread3", ThreadDiagram.create(drostePairs3))
    }
    return false
}
function setPairDiagram(containerID, diagram) {
    var container = d3.select(containerID)
    container.node().data = diagram
    container.html(DiagramSvg.render(diagram, "1px", markers = true, 744, 1052, 0.0))
    showGraph(containerID, diagram)
}
function setThreadDiagram(containerID, diagram) {
    var container = d3.select(containerID)
    container.node().data = diagram
    container.html(DiagramSvg.render(diagram, "2px", markers = true, 744, 1052, 0.0).replace("<g>","<g transform='scale(0.5,0.5)'>"))
    showGraph(containerID, diagram)
    container.selectAll(".threadStart").on("click", clickedThread)
    container.selectAll(".bobbin").on("click", clickedThread)
}
function clickedThread(event) {
    let classNameAsXpath = '.' + event.currentTarget.textContent.replace(" ", "");
    let threadSegments = d3.selectAll(classNameAsXpath)
    let color = d3.select('#threadColor').node().value
    threadSegments.style("stroke", color)
    threadSegments.filter(".node").style("fill", color)
}
