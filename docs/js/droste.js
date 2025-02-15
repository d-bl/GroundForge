function load() {

    let q = window.location.search.substring(1)+""
    if(!q) q = "patchWidth=12&patchHeight=10&footside=b-,-5,y-,-y,,&tile=L-O-,---5,H-E-,-5--&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4&e1=ct&c1=ct&a1=ctctcl&f2=ct&b2=ctctctcl&e3=ct&c3=ctc&d4=ct&droste2=twist=ct"
    document.getElementById('to_stitches').href = 'stitches.html?'+q
    document.getElementById('to_self').href = 'droste.html?'+q

    setTextArea(2, q)
    setTextArea(3, q)

    // initial thread diagram used as pair diagram
    let pairDiagram = NewPairDiagram.create(TilesConfig(q)) // old style pair diagram
    document.getElementById("droste2").data = ThreadDiagram.create(pairDiagram)
    setPairLevel2()

    d3.select('#droste2').on('keypress',clearThreadLevel2)
    d3.select('#droste3').on('keypress',clearThreadLevel3)
    d3.select('#droste2').on('input',clearThreadLevel2)
    d3.select('#droste3').on('input',clearThreadLevel3)
    d3.select('#droste2').on('change',clearAll)
    d3.select('#wandPairLevel2').on('click',setPairLevel2)
    d3.select('#wandPairLevel3').on('click',setPairLevel3)
    d3.select('#wandThreadLevel2').on('click',setThreadLevel2)
    d3.select('#wandThreadLevel3').on('click',setThreadLevel3)
}
function clickedStitch(event) {
    function circle() {
        const svgNS = "http://www.w3.org/2000/svg";
        const circle = document.createElementNS(svgNS, "circle");
        circle.setAttribute("cx", 0);
        circle.setAttribute("cy", 0);
        circle.setAttribute("r", 9);
        circle.setAttribute("fill", '#000');
        circle.style.opacity = 0.15;
        return circle;
    }

// Example usage:
    const svgElement = document.querySelector('svg');

    // Find the closest ancestor with the onclick attribute
    const targetElement = event.currentTarget.closest('[onclick]');

    const eventTitle = targetElement.getElementsByTagName("title")[0].innerHTML;

    const containerId = event.currentTarget.closest('svg').parentNode.id;
    const textId = 'droste' + containerId.charAt(containerId.length - 1);
    const threadId = 'drosteThread' + containerId.charAt(containerId.length - 1);
    const stitchId = eventTitle.replace(/.* /,"");
    const stitchDef = document.getElementById("stitchDef").value

    // Add the stitch to the textarea
    document.getElementById(textId).value += `\n${stitchId}=${stitchDef}`;

    // shade for out of date diagram panels
    if (textId === 'droste2') {
        clearThreadLevel2()
    }else {
        clearThreadLevel3()
    }

    // highlight out of date nodes in thread diagram (their titles contain stitchId)
    const threads  = document.getElementById(threadId)
    for (let path of threads.getElementsByTagName('path')) {
        const titleEl = path.getElementsByTagName("title")[0];
        if (!titleEl) continue;
        let title = titleEl.innerHTML;
        if (title.includes(' - ' + stitchId)) {
            console.log(`title: ${title} ${path.style.fill}`)
            path.style.opacity = 1;
        }
    }

    // highlight out of date nodes in pair diagram
    const matchingSiblings = [];
    for (sibling of targetElement.parentNode.children) {
        let titleEl = sibling.getElementsByTagName("title")[0];
        if (titleEl && eventTitle === titleEl.innerHTML) {
            sibling.appendChild(circle());
            // TODO changing shape would be better but thread marks are a problem
        }
    }
    setLinks(2)
}
function clearAll(){
    document.getElementById('drostePair2').innerHTML = ""
    clearThreadLevel2()
}
function setLinks(level){
    let key = "droste" + level;
    let value = document.getElementById(key).value
        .replace(/\n+/g,',').replace(/[^a-zA-Z0-9=,]/g,'')
    let l = document.getElementById('to_self')
        .href
        .split('&').filter(function (s) {return !s.startsWith(key+'=')})
        .join('&') + `&${key}=${value}`
    console.log('new link: '+l)
    document.getElementById('to_self').href = l
    let source = 'stitches'
    if (window.location.search.includes('source='))
        source = window.location.search.replace(/.*source=/,'').replace(/&.*/,'')
    document.getElementById('to_stitches').href = l.replace('droste.html',source)
}
function setTextArea(level, q){ // level 2 == step 1
    let key = 'droste'+level
    if(!q.includes(key+'=')) return // level mentioned in query?
    let value = q.split('&').filter(function (s) {return s.startsWith(key+'=')});
    document.getElementById(key).value = value[0].replace(key+'=','')
        .replace(/[^[a-zA-Z0-9=,]/g,"")
        .replace(/,/g,"\n")
}
function changeQ(level, q){
    let id = 'droste'+level
    let s = d3.select("#"+id).property('value').split('\n').join(',')
    return `${q.replace(new RegExp(id+'=[^&]+'),'')}&${id}=${s}`
}
function getQ() {
    return document.getElementById('to_stitches').href.replace(/.*[?]/, "");
}
function clearThreadLevel2() { // level 2 == step 1
    setLinks(2)
    clearDownloadLinks()
    let panels = d3.selectAll("#drostePair2, #drosteThread2, #drostePair3, #drosteThread3");
    panels.property("data","")
    panels.style("background-color","#EEE")
}
function clearThreadLevel3() { // level 3 == step 2
    setLinks(3)
    clearDownloadLinks()
    let panels = d3.selectAll("#drostePair3, #drosteThread3");
    panels.property(data,"")
    panels.style("background-color","#EEE")
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
        pairContainer.innerHTML = ''
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
    setLinks(level)
    const containerId = 'drostePair' + level;
    const textValue = document.getElementById("droste"+level).value;
    const pairDiagram = PairDiagram.create(textValue, threadDiagram)
    const container = document.getElementById(containerId)
    container.data = pairDiagram
    container.innerHtml = DiagramSvg.render(pairDiagram, "1px", true, 744, 1052, 0.0)
    container.style.backgroundColor = '#FFF';
    showGraph('#'+containerId, pairDiagram)
    d3.select(`#${containerId} g`)
        .attr("transform","scale(1.3)")
}
function setThreadDiagram(level, pairDiagram) {
    setLinks(level)
    let containerID = 'drosteThread'+level
    var threadDiagram = ThreadDiagram.create(pairDiagram)
    var container = document.getElementById(containerID)
    container.data = threadDiagram
    container.innerHTML  = DiagramSvg.render(threadDiagram, "2px", true, 744, 1052, 0.0)
    container.style.backgroundColor = '#FFF';
    showGraph('#'+containerID, threadDiagram)
    d3.select(`#${containerID} g`)
        .attr("transform","scale(0.5)")
}


function toggleVisibility(id) {
    var x = document.getElementById(id);
    if (x.style.display === "block") {
        x.style.display = "none";
    } else {
        x.style.display = "block";
    }
}
function cleanupStitches() {
    const textValue = document.getElementById('droste2').value
    let id2instruction = {}
    textValue.split(/[\n,]/ ).forEach(function (line) {
        if(line.includes('=')) {
            let stitchValue = line.replace(/.*=/, "")
            let stitchIds = line.replace(/=.*/, "").split(/=/)
            stitchIds.forEach(function (stitchId) {
                id2instruction[stitchId] = stitchValue
            })
        }
    })
    let invertedMap = {};

    for (const [key, value] of Object.entries(id2instruction)) {
        if (!invertedMap[value]) {
            invertedMap[value] = [];
        }
        invertedMap[value].push(key);
    }
    let longestKey = null;
    let maxLength = 0;

    for (const [key, valueArray] of Object.entries(invertedMap)) {
        if (valueArray.length > maxLength) {
            maxLength = valueArray.length;
            longestKey = key;
        }
    }
    let lines= [longestKey]
    for (const [key, valueArray] of Object.entries(invertedMap)) {
        if(key!==longestKey)
            lines.push(valueArray.join('=') + '=' + key)
    }
    document.getElementById('droste2').value = lines.join('\n')
    setLinks(2)
}