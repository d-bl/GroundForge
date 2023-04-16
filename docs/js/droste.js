function load() {

    let q = window.location.search.substring(1)+""
    d3.select('#to_stitches').attr('href','stitches.html?'+q)
    d3.select('#to_self').attr('href','droste.html?'+q)
    setLinks(2, q)
    setLinks(3, q)
    let trimmed = q.split('&').map(unduplicate).join('&')
    let cfg = TilesConfig(trimmed)
    let pairDiagram = NewPairDiagram.create(cfg) // old style pair diagram
    let threadDiagram = ThreadDiagram.create(pairDiagram)
    d3.select("#droste2").node().data = threadDiagram
    var drostePairs2 = PairDiagram.create(d3.select("#droste2").node().value, threadDiagram)

    setPairDiagram("#drostePair2", drostePairs2)
    d3.select('#drosteThread2 g')
        .attr("transform","scale(0.5,0.5)")
}
function unduplicate(s){
    return s // TODO a 'lrlr' sequence is not reduced
        .replace(/[tlrp]+t[tlrp]+/g, 't')
        .replace(/[lp]+l[lp]+/g, 't')
        .replace(/[rp]+r[rp]+/g, 't')
        .replace(/[cp]+c[cp]+/g, '')
}
function setLinks(level, q){
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
    if (event && event.currentTarget.id.endsWith('2'))
        clear2()
    clear3()
    let q = changeQ(3, changeQ(2, getQ()))
    console.log("==="+q)
    d3.select('#to_stitches').attr('href','stitches.html?'+q)
    d3.select('#to_self').attr('href','droste.html?'+q)
}
function changeQ(level, q){
    let id = 'droste'+level
    let s = d3.select("#"+id).property('value').split('\n').join(',')
    if(q.includes(id))
        return q.replace(new RegExp(id+'=[^&]+'),`${id}=${s}`)
    else return `${q}&${id}=${s}`
}
function getQ() {
    return d3.select('#to_stitches').attr('href').replace(/.*[?]/, "");
}
function clear2() {
    var threadDiagram = d3.select("#droste2").node().data
    var p2 = PairDiagram.create(d3.select("#droste2").node().value, threadDiagram)
    setPairDiagram("#drostePair2", p2)
    d3.selectAll("#drosteThread2, #drostePair3, #drosteThread3").html("")
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
    var el = d3.select("#drosteThread" + level).node().firstElementChild
    if (el && el.id.startsWith("svg")) return

    var drosteThreads1 = d3.select("#droste2").node().data
    var drostePairs2 = PairDiagram.create(d3.select("#droste2").node().value, drosteThreads1)
    var drosteThreads2 = ThreadDiagram.create(drostePairs2)

    if (level == 2) {
        setPairDiagram("#drostePair2", drostePairs2)
        setThreadDiagram("#drosteThread2", drosteThreads2)
        d3.select('#drosteThread2 g')
            .attr("transform","scale(0.5,0.5)")
    } else if (level == 3) {
        // TODO regenerate level two with variant of unduplicate
        var drostePairs3 = PairDiagram.create(d3.select("#droste3").node().value, drosteThreads2)
        setPairDiagram("#drostePair3", drostePairs3)
        setThreadDiagram("#drosteThread3", ThreadDiagram.create(drostePairs3))
        d3.select('#drosteThread3 g')
            .attr("transform","scale(0.5,0.5)")
    }
    return false
}
function setPairDiagram(containerID, diagram) {
    var container = d3.select(containerID)
    container.html(DiagramSvg.render(diagram, "1px", true, 744, 1052, 0.0))
    showGraph(containerID, diagram)
}
function setThreadDiagram(containerID, diagram) {
    var container = d3.select(containerID)
    container.html(DiagramSvg.render(diagram, "2px", true, 744, 1052, 0.0).replace("<g>","<g transform='scale(0.5,0.5)'>"))
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
