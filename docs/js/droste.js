function load() {

    let q = window.location.search.substring(1)+""
    if(!q) q = "patchWidth=12&patchHeight=10&footside=b-,-5,y-,-y,,&tile=L-O-,---5,H-E-,-5--&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4&e1=ct&c1=ct&a1=ctctcl&f2=ct&b2=ctctctcl&e3=ct&c3=ctc&d4=ct&droste2=twist=ct&droste3=#"
    d3.select('#to_stitches').attr('href','stitches.html?'+q)
    d3.select('#to_self').attr('href','droste.html?'+q)

    d3.select('#to_stitches').attr('href','stitches.html?'+q)
    d3.select('#to_self').attr('href','droste.html?'+q)

    setTextArea(2, q)
    setTextArea(3, q)

    // initial thread diagram used as pair diagram
    let trimmed = q.split('&').map(unduplicate).join('&')
    let pairDiagram = NewPairDiagram.create(TilesConfig(trimmed)) // old style pair diagram
    document.getElementById("droste2").data = ThreadDiagram.create(pairDiagram)
    setPairLevel2()

    d3.select('#droste2').on('keyup',clearThreadLevel2)
    d3.select('#droste3').on('keyup',clearThreadLevel3)
    d3.select('#wandPairLevel2').on('click',setPairLevel2)
    d3.select('#wandPairLevel3').on('click',setPairLevel3)
    d3.select('#wandThreadLevel2').on('click',setThreadLevel2)
    d3.select('#wandThreadLevel3').on('click',setThreadLevel3)
}
function unduplicate(s){
    // prevent invisible floating nodes caused by repeated twists and/or crossings
    var kv = s.split('=')
    return kv[0]+'='+kv[1]
        .replace(/[tlrp]*t[tlrp]+/g, 't')
        .replace(/[tlrp]+t/g, 't')
        .replace(/[lp]*[rp]+l[lp]+/g, 't')
        .replace(/[lp]+[rp]+/g, 't')
        .replace(/[lp]+/g, 'l')
        .replace(/[rp]+/g, 'r')
        .replace(/[cp]+c[cp]+/g, '')
}
function setTextArea(level, q){ // level 2 == step 1
    let id = 'droste'+level
    if(!q.includes(id)) return // level mentioned in query?
    document.getElementById(id).value = q
        .replace(new RegExp(`.*[?&]${id}=`),"")
        .replace(/&.*/,"")
}
function changeQ(level, q){
    let id = 'droste'+level
    let s = d3.select("#"+id).property('value').split('\n').join(',')
    return `${q.replace(new RegExp(id+'=[^&]+'),'')}&${id}=${s}`
}
function getQ() {
    return d3.select('#to_stitches').attr('href').replace(/.*[?]/, "");
}
function clearThreadLevel2() { // level 2 == step 1
    d3.selectAll("#drosteThread2, #drostePair3, #drosteThread3").html("")
    d3.selectAll("#drostePair2, #drosteThread2, #drostePair3, #drosteThread3").data("")
    clearDownloadLinks();
}
function clearThreadLevel3() { // level 3 == step 2
    d3.selectAll("#drosteThread3").html("")
    d3.selectAll("#drostePair3, #drosteThread3").data("")
    clearDownloadLinks();
}
function clearDownloadLinks() {
    d3.selectAll("#drostePair2DownloadLink, #drosteThread2DownloadLink, #drostePair3DownloadLink, #drosteThread3DownloadLink")
        .attr("href", "#?pleasePrepareFirst")
        .style("display", "none")
}
function setPairLevel2() { // level 2 == step 1
    const threadDiagram = document.getElementById("droste2").data // set at page load
    setPairDiagram(2, threadDiagram)
}
function setThreadLevel2() {
    setThreadDiagram(2, getPairLevel2())
}
function setPairLevel3() { // level 3 == step 2
    setPairDiagram(3, getThreadLevel2())
}
function setThreadLevel3() {
    setThreadDiagram(3, getPairLevel3())
}
function getPairLevel2() {
    let pairContainer = document.getElementById('drostePair2');
    let pairDiagram = pairContainer.data
    if (!pairDiagram) {
        let textValue = document.getElementById("droste2").value;
        let threadLevel1 = document.getElementById("droste2").data;
        pairDiagram = PairDiagram.create(textValue, threadLevel1)
        pairContainer.data = pairDiagram
    }
    return pairDiagram;
}
function getThreadLevel2() {
    let threadContainer = document.getElementById('drosteThread2');
    let threadDiagram = threadContainer.data
    if (!threadDiagram) {
        threadDiagram = ThreadDiagram.create(getPairLevel2())
        threadContainer.data = threadDiagram
    }
    return threadDiagram
}
function getPairLevel3() {
    let pairContainer = document.getElementById("drostePair3");
    let pairDiagram = pairContainer.data
    if (!pairDiagram) {
        let textValue = document.getElementById("droste2").value;
        pairDiagram = PairDiagram.create(textValue, getThreadLevel2())
        pairContainer.data = pairDiagram
    }
    return pairDiagram;
}
function setPairDiagram(level, threadDiagram) {
    const containerId = 'drostePair' + level;
    const textValue = document.getElementById("droste2").value;
    const pairDiagram = PairDiagram.create(textValue, threadDiagram)
    const container = document.getElementById(containerId)
    container.data = pairDiagram
    container.innerHtml = DiagramSvg.render(pairDiagram, "1px", true, 744, 1052, 0.0)
    showGraph('#'+containerId, pairDiagram)
}
function setThreadDiagram(level, pairDiagram) {
    let containerID = 'drosteThread'+level
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    var container = document.getElementById(containerID)
    container.data = threadDiagram
    container.innerHTML  = DiagramSvg.render(threadDiagram, "2px", true, 744, 1052, 0.0).replace("<g>","<g transform='scale(0.5,0.5)'>")
    showGraph('#'+containerID, threadDiagram)
    d3.select(`#${containerID} g`)
        .attr("transform","scale(0.5,0.5)")
    d3.selectAll(`#${containerID} .threadStart`).on("click", clickedThread)
    d3.selectAll(`#${containerID} .bobbin`).on("click", clickedThread)
}
function clickedThread(event) {
    let classNameAsXpath = '.' + event.currentTarget.textContent.replace(" ", "");
    let threadSegments = d3.selectAll(classNameAsXpath)
    let color = d3.select('#threadColor').node().value
    threadSegments.style("stroke", color)
    threadSegments.filter(".node").style("fill", color)
}

function toggleVisibility(id) {
    console.log('toggleVisibility '+id)
    var x = document.getElementById(id);
    if (x.style.display === "block") {
        x.style.display = "none";
    } else {
        x.style.display = "block";
    }
}