function showGraph(containerId, diagram) {

    var container = d3.select(containerId)

    // dimensions for an A1
    var width = 2245
    var height = 3178

    var opacity = 0
    var stroke = containerId.toLowerCase().includes('thread') ? 4 : 2

    var markers = true // use false for slow devices and IE-11, set them at onEnd
    container.html(DiagramSvg.render(diagram, stroke, markers, width, height, opacity))
    if (containerId.toLowerCase().includes('thread')) {
        container.selectAll(`.threadStart`).on("click", clickedThread)
        container.selectAll(`.bobbin`).on("click", clickedThread)
        container.selectAll(".node").on("click",clickedNode)
    }
    container.selectAll('.threadStart').style("fill","rgb(0,0,0)").style('opacity',"0.4")
    nudgeDiagram(container.select("svg"))
}
function clickedThread(event) {
    let classNameAsXpath = '.' + event.currentTarget.textContent.replace(" ", "");
    let threadSegments = d3.selectAll(classNameAsXpath)
    let color = document.getElementById('threadColor').value
    threadSegments.style("stroke", color)
    threadSegments.filter(".node").style("fill", color)
}
function clickedNode(event) {
    const selectedClass = d3.event.currentTarget.classList.toString().replace(/ *node */,'')
    if (selectedClass === "threadStart") return
    var color = d3.select('#threadColor').node().value
    d3.selectAll("." + selectedClass)
        .style("stroke", color).style("fill", color).style('opacity',"0.4")
}
