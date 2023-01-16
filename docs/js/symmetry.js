var stitchDistance = 25.2 // related to clonedScale
var clonedScale = "scale(1.8)"

function clickedPair() {
    var elem = d3.event.target
    d3.select(event.target).style("marker-mid",function() {
        var n = d3.select('#twists').node().value
        if (n <= 0) return ""
        return 'url("#twist-' + n + '")'
    })
    d3.selectAll('#cloned .link').style('stroke',"rgb(0,0,0)").style('stroke-opacity',"0.25")
    d3.select("#download2").style("display","none")
}
function nrOfLinks(id){
    return d3.selectAll(".link").filter(function () {
        return this.classList.value.includes(id)
    }).size()
}
function clickedStitch(event) {
    d3.select("#download2").style("display","none")
    var elem = event.target.parentElement
    switch (document.querySelector("input[name=editMode]:checked").value) {
    case "change":
        var txt = dropTwists(d3.select("#stitchDef").node().value)
        var w = document.querySelector("#width").value - 1
        var h = document.querySelector("#height").value - 1
        function isTopBottom(elem) { return elem.id.startsWith("r0c") || elem.id.startsWith(`r${h}c`) }
        function isLeftRight(elem) { return elem.id.endsWith("c0") || elem.id.endsWith(`c${w}`) }
        if (isTopBottom(elem)) {
            d3.selectAll("#cloned .node")
              .filter(function() { return isTopBottom(this) })
              .html(function() { return "<title>"+txt+"</title>"+PairSvg.shapes(txt) })
        }
        if (isLeftRight(elem)) {
            d3.selectAll("#cloned .node")
              .filter(function() { return isLeftRight(this) })
              .html(function() { return "<title>"+txt+"</title>"+PairSvg.shapes(txt) })
        } else elem.innerHTML = "<title>"+txt+"</title>"+PairSvg.shapes(txt)
        createLegend()
        break
    case "delete":
        var deletedStitchId = elem.id
        if (4 == nrOfLinks(deletedStitchId) ) {
            d3.selectAll("#cloned .starts_at_"+deletedStitchId)
            .each(function () {
                var newXY = this.getAttribute("d").split(" ")[4]
                var newEndId = nodeId(this, "end")
                d3.selectAll(`#cloned .${findClass(this,"kiss_")}`).filter(function () {
                    // incoming pair, the same kissing pair as the outgoing pair
                    return endsAt(this, deletedStitchId)
                }).each(function () {
                    var def = this.getAttribute("d").split(" ")
                    this.setAttribute("d", withMovedMid(def[1], def[4] = newXY, def))
                    this.id = this.id.replace(/-(.*)/, '-'+newEndId)// TODO use classes
                    this.setAttribute("class",replaceEndsAt(this,newEndId))
                })
                this.parentNode.removeChild(this)
            })
            elem.parentNode.removeChild(elem)
        }
        break
    }
}
function nodeId(pathElem, at) {
    return classArray(pathElem)
        .filter(function(s) {return s.startsWith(at+'s_at_')})
        .map(function(s) {return s.replace(at+'s_at_', "")})
}
function endsAt(pathElem, id) {

    return classArray(pathElem).includes('ends_at_'+id)
}
function startsAt(pathElem, id) {

    return classArray(pathElem).includes('starts_at_'+id)
}
function classArray(elem) {

    return elem.classList.value.split(' ')
}
function findClass(elem, prefix) {

    return classArray(elem).filter(function(s){ return s.startsWith(prefix) })[0]
}
function replaceStartsAt(elem, id) {
    return classArray(elem)
        .filter(function(s) {return !s.startsWith('starts_at_')})
        .join(' ') + ' starts_at_'  + id
}
function replaceEndsAt(elem, id) {
    return classArray(elem)
        .filter(function(s) {return !s.startsWith('ends_at_')})
        .join(' ') + ' ends_at_'  + id
}
function createLegend() {
    var w = (document.querySelector("#width").value - 1) * stitchDistance
    var stitches = []
    d3.selectAll(".node title")
      .each(function(){ stitches[stitches.length] = this.innerHTML })

    d3.select("#bdpqLegend")
        .html(PairSvg.bdpqLegend(stitches.join(',')))
        .attr("transform", `translate(${w+stitchDistance},0) ${clonedScale}`)
}
function dropTwists(s) {

    return s.toLowerCase().replace(/[tlr]*([tlrc]*c)[tlr]*/,'$1')
}
function clones () {
    d3.select("#download2").style("display","none")
    var dimX = document.querySelector("#width").value - 1
    var dimY = document.querySelector("#height").value - 1
    var w = stitchDistance * dimX
    var h = stitchDistance * dimY
    var swatchScale = document.querySelector("#swatch_scale").value

    // just one of the indent arguments is non-zero
    function swatch(swatchX, swatchY, templateArrangement, indentX, indentY, indentX2, indentY2){

      // 4x4 letters separated with 3 spaces, split into a 2D array
      if (!templateArrangement.match(/([bdpq]{4} ){3}[bdpq]{4}/)) return
      var ta = templateArrangement.split(' ')

      var result = ''
      for (let swatchRow = 0; swatchRow < 6; swatchRow++) {
        for (let swatchColumn = 0; swatchColumn < 6; swatchColumn++) {
          var indentRow = swatchRow * indentX + (Math.floor(swatchRow / 2)) * indentX2
          var indentColumn = swatchColumn * indentY + (Math.floor(swatchColumn / 2)) * indentY2
          var templateCopyX = stitchDistance*(swatchColumn * dimX + ((indentRow+1) % dimX))
          var templateCopyY = stitchDistance*(swatchRow * dimY + ((indentColumn+1) % dimY))
          var taC = swatchColumn + 400*dimX - Math.floor((indentRow+1) / dimX)
          var taR = swatchRow + 400*dimY - Math.floor((indentColumn+1) / dimY)
          result += `<use xlink:href="#cl${ta[taR%4][taC%4]}" x="${templateCopyX}" y="${templateCopyY}"/>`
        }
      }
      return `
        <g transform="scale(${swatchScale}) translate(${swatchX},${swatchY})">
          <g onclick="alert(this.textContent)"><title>${templateArrangement.replace(/ /g,'\n')}

          indent:
            rows=${indentX}
            columns=${indentY}
            2-rows=${indentX2}
            2-columns=${indentY2}</title>
              <circle cx="${-w-0.3*stitchDistance}" cy="${-h+1.5*stitchDistance}" r="30" fill="#eee" />
          </g>
          ${result}
        </g>
      `
    }
    var indentSteps = document.querySelector("#indentSteps").value * 1
    var bAndOneOther = document.querySelector("#bAndOneOther").value.split(",")
    var bdpqRowsCols = document.querySelector("#bdpqRowsCols").value.split(",")
    var customPattern = document.querySelector("#customPattern").value.replace(/\n/g,' ')

    // margin between legend ans swatches
    var margin = (5 * stitchDistance) / swatchScale

    // 4 base clones out of sight and on top of one another allow translates independent of b/d/p/q
    var f8 = stitchDistance * 0.8 // some margin for the template
    d3.select('#template #clones').html(`
      <g id="clb"><use x="0" y="0" xlink:href="#cloned" transform="translate(${-w-f8},${-h-f8})" /></g>
      <g id="cld"><use x="0" y="0" xlink:href="#cloned" transform="scale(-1,1) translate(0,${-h-f8})" /></g>
      <g id="clp"><use x="0" y="0" xlink:href="#cloned" transform="scale(1,-1) translate(${-w-f8},0)" /></g>
      <g id="clq"><use x="0" y="0" xlink:href="#cloned" transform="scale(-1,-1)" /></g>

      ${swatch( 8*w+margin, margin/2 + h, bAndOneOther[2], 0, 0, 0, 0)}
      ${swatch(16*w+margin, margin/2 + h, bAndOneOther[1], 0, indentSteps, 0, 0)}
      ${swatch(23*w+margin, margin/2 + h, bAndOneOther[0], indentSteps, 0, 0, 0)}

      ${swatch( 1*w+margin, margin/2 + 10*h, bdpqRowsCols[1], 0, indentSteps, 0, 0)}
      ${swatch( 8*w+margin, margin/2 + 10*h, bdpqRowsCols[0], indentSteps, 0, 0, 0)}
      ${swatch(16*w+margin, margin/2 + 10*h, bdpqRowsCols[1], 0, 0, 0, indentSteps)}
      ${swatch(23*w+margin, margin/2 + 10*h, bdpqRowsCols[0], 0, 0, indentSteps, 0)}

      ${swatch( 1*w+margin, margin/2 + 18*h, customPattern, 0, indentSteps, 0, 0)}
      ${swatch( 8*w+margin, margin/2 + 18*h, customPattern, indentSteps, 0, 0, 0)}
      ${swatch(16*w+margin, margin/2 + 18*h, customPattern, 0, 0, 0, indentSteps)}
      ${swatch(23*w+margin, margin/2 + 18*h, customPattern, 0, 0, indentSteps, 0)}
    `)
    d3.select('#template svg')
        .attr("width", ((23+6.5)*w+margin) * swatchScale)
        .attr("height", ((18+6.8)*h+margin/2) * swatchScale)
}
function setCustom(){
    var seq = document.querySelector("#bdpqRowsCols").value.split(",")[0].split(" ")[0]
    var sq = [ // arranged in z-order in a square
        seq[0]+seq[1],
        seq[2]+seq[3]
    ]
    document.querySelector("#customPattern").value = [
        sq[0][1]+sq[0]+sq[0][0]+"\n"+
        sq[1][1]+sq[1]+sq[1][0]+"\n"+
        sq[0]+sq[0]+"\n"+
        sq[1]+sq[1]
   ]
   clones()
}
function initDiagram() {
    var pattern = document.querySelector("input[name=variant]:checked").value
    var cols = document.querySelector("#width").value
    var rows = document.querySelector("#height").value

    var q = `patchWidth=${cols}&patchHeight=${rows}&${pattern}`
    var svg = PairSvg.render(TilesConfig(q).getItemMatrix, 500, 250 , 1)

    d3.select('#template').html(svg)
    d3.select('#cloned').attr("transform", clonedScale)
    d3.selectAll('#template title').html(function() {
        return dropTwists(this.innerHTML.replace(/ - .*/,''))
    })
    clones()
    d3.selectAll(".re_clone").attr("onchange",'clones()')
    d3.select("#width, #height").attr("onchange",'setMaxIndent()')
    setMaxIndent()

    var regex = /r[0-9]+c([0-9]+)-r[0-9]+c([0-9]+)/
    var links = d3.selectAll("#cloned .link")
    links.each(function () {
        var n = Math.min(...(this.id.replace(regex,'$1_$2').split('_')))
        var classes = [this.id.replace(regex,'$1_$2'), this.id.replace(regex,'$2_$1')]
        this.classList.add('kiss_'+n)
    })
    links.style("stroke-width","5px") // wider lines are bigger targets
    links.style("stroke-opacity",0.25) // keep the twist marks visible
    links.style("stroke-linejoin","bevel")
    createLegend()
    activate(links)
}
function setMaxIndent(){
    var cols = document.querySelector("#width").value
    var rows = document.querySelector("#height").value
    var max = Math.max(cols,rows)
    document.querySelector("#indentSteps").max = (max - 1) * 4
}
function activate(links) {
    links.on("click",clickedPair)
    d3.drag().on("drag",moveStitch)(d3.selectAll(".node").filter(function(){ return 4 == nrOfLinks(this.id) }))
    dragLinks(links)
}
function dragLinks(links){
    d3.drag()
        .on("end", finishPinch)
        .on("drag", moveCenter)
        .on("start", function () {
            findKissingPair(this, 0)
        })(links)
}
function finishPinch() {
    function dist(a) {
       var def = a.getAttribute("d").split(" ")
       var powX = Math.pow(def[2]*1 - d3.event.x, 2)
       var powY = Math.pow(def[3]*1 - d3.event.y, 2)
       return powX + powY
    }
    // moveCenter moved the mid point to the mouse position
    // that implies a drag, little chance a click exactly hits the mid point
    if (dist(this) != 0 ) return

    // find the edge with the centre closest to the mouse position
    var nearest = null
    findKissingPair(this, direction(this)).each(function () {
        if (nearest == null) nearest = this
        else {
            var distThis = dist(this)
            if (distThis == 0 ) return
            else if ( dist(nearest) > distThis) nearest = this
        }
    })
    d3.selectAll(".link").style("stroke","rgb(0,0,0)").style('stroke-opacity',"0.25")
    if(nearest==null)return
    var newID = Date.now()
    var newXY = `${d3.event.x},${d3.event.y}`

    var el = document.createElementNS("http://www.w3.org/2000/svg", "g")
    el.innerHTML = PairSvg.shapes('ctc')
    el.setAttribute('transform',`translate(${d3.event.x},${d3.event.y})`)
    el.setAttribute('onclick', "clickedStitch(event)")
    el.id = newID
    d3.drag().on("drag",moveStitch)(d3.select(el))
    var container = document.querySelector('#cloned')
    container.appendChild(el)
    container.insertBefore(splitLink(this, newID, newXY), container.firstChild)
    container.insertBefore(splitLink(nearest, newID, newXY), container.firstChild)
}
function direction(lineElem){
    var def = lineElem.getAttribute("d").split(" ")
    var start = def[1].split(",")
    var end = def[4].split(",")
    var mid = [(start[0]*1+end[0]*1)/2, (start[1]*1+end[1]*1)/2]
    var mouse = [d3.event.x,d3.event.y]
    var lineAngle = angle(start,end)
    var moveAngle = angle(mid,mouse)
    var diff = lineAngle - moveAngle
    var direction = - diff / Math.abs(diff)
//    console.log(`direction: ${direction}  Angles: line=${lineAngle} move=${moveAngle}`)
    return direction
}
function angle(start, end) {
    var dx = end[0] - start[0]
    var dy = end[1] - start[1]
    if (dx == 0 && dy < 0) return 180

    var hyp = Math.sqrt(dx*dx + dy*dy)
    var sin = dx / hyp
//    console.log(`dx=${dx} dy=${dy} hyp=${hyp} sin=${sin} asin=$(Math.asin(sin)) start:${start} end:${end}`)
    return (Math.asin(sin)*180) / Math.PI
}
function moveStitch() {
    var stitchId = this.id
    var newXY = `${d3.event.x},${d3.event.y}`

    function moveEnd(){
        var def = this.getAttribute("d").split(" ")
        return withMovedMid(def[1], def[4] = newXY, def)
    }
    function moveStart(){
        var def = this.getAttribute("d").split(" ")
        return withMovedMid(def[4], def[1] = newXY, def)
    }
    this.setAttribute("transform", `translate(${newXY})`)
    d3.selectAll(".link").filter(function () {
        return startsAt(this, stitchId)
    }).attr("d", moveStart)
    d3.selectAll(".link").filter(function () {
        return endsAt(this, stitchId)
    }).attr("d", moveEnd)
}
function splitLink(nearest, newID, newXY) {
    var p2 = document.createElementNS("http://www.w3.org/2000/svg", "path")
    p2.id = Date.now()
    p2.setAttribute("d", nearest.getAttribute("d"))
    p2.setAttribute("class", replaceStartsAt(nearest,newID))
    p2.setAttribute("style", nearest.getAttribute("style"))
    var defB = p2.getAttribute("d").split(" ")
    p2.setAttribute("d", withMovedMid(defB[4], defB[1] = newXY, defB))
    var defB = nearest.getAttribute("d").split(" ")
    nearest.setAttribute("d", withMovedMid(defB[1], defB[4] = newXY, defB))
    nearest.id = Date.now()
    nearest.setAttribute("class", replaceEndsAt(nearest,newID))
    d3.select(p2).on("click",clickedPair)
    dragLinks(d3.select(p2))
    return p2
}
function findKissingPair(movedPair, direction) {

    var start = movedPair.classList.value.replace(/.*starts_at_/,"").replace(/ .*/,"")
    var end = movedPair.classList.value.replace(/.*ends_at_/,"").replace(/ .*/,"")

    var thisKissNr = findClass(movedPair,'kiss_').replace(/kiss_/,'')*1
    var kissClassLeft = `#cloned .kiss_${thisKissNr - 1}`
    var kissClassRight = `#cloned .kiss_${thisKissNr + 1}`
    console.log(`${kissClassLeft} ${kissClassRight} ${direction} start=${start} end=${end}`)
    if (direction == 0) { // start move
        var kissingPairs = d3.selectAll(kissClassLeft + "," + kissClassRight)
        kissingPairs.style("stroke","rgb(0, 255, 0)").style('stroke-opacity',"0.25")
        kissingPairs.filter(function(){
         return this.classList.value.includes('_at_'+end)
             || this.classList.value.includes('_at_'+start)
        }).style("stroke","rgb(44,162,95)")
    } else { // end move
        return d3.selectAll(direction < 0 ? kissClassLeft : kissClassRight)
    }
}
function withMovedMid(end, newEnd, def) {
    var endXY = newEnd.split(',')
    midXY = end.split(',')
    def[2] = (midXY[0]*1 + endXY[0]*1)/2
    def[3] = (midXY[1]*1 + endXY[1]*1)/2
    return def.join(" ")
}
function moveCenter() {
    var def = this.getAttribute("d").split(" ")
    def[2] = d3.event.x
    def[3] = d3.event.y
    this.setAttribute("d", def.join(" "))
}
function getWrappedSVG (q) {
    svg = d3.select(q).node().innerHTML
    return 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
function readSingleFile(evt) {
    //Retrieve the first (and only!) File from the FileList object
    var f = evt.target.files[0];
    if (f) {
        var r = new FileReader();
        r.onload = function(e) {
            var contents = e.target.result.replace(/.*?-->/,"");
            console.log( "Got the file."
                  +"  name: " + f.name
                  +"  type: " + f.type
                  +"  size: " + f.size + " bytes\n"
            )
            document.getElementById('template').innerHTML =  contents;
            activate(d3.selectAll(".link"))
            // find largest rYcX id value of nodes
            var cols=0,rows=0
            d3.selectAll(".node")
              .filter(function(){ return this.id.startsWith("r") })
              .each(function(){
                 rc = this.id.replace("r","").split("c")
                 rows = Math.max(rows,rc[0]*1)
                 cols = Math.max(cols,rc[1]*1)
              })
            document.querySelector("#width").value = cols
            document.querySelector("#height").value = rows
        }
        r.readAsText(f);
    } else {
        alert("Failed to load file");
    }
}
function loadStitchExamples() {
    var stitches = [
                       ["ctctc", "", ""],
                       ["crclct", "clcrct", ""],
                       ["clcrclc", "crclcrc", ""],
                       ["ctclctc", "ctcrctc", ""],
                       ["ctclcrctc", "ctcrclctc", ""],
                       ["ctcttctc", "", ""],
                   ]
    for (let alts of stitches) {
        document.querySelector("#gallery").innerHTML += `
            <figure>
                <img src="/GroundForge/images/stitches/${alts[0]}.png"
                     alt="${alts[0]}"
                     title="${alts[0]}&#013${alts[1]}&#013${alts[2]}">
                <figcaption>
                    <a href="javaScript:setStitch('${alts[0]}')">${alts[0]}</a><br>
                    <a href="javaScript:setStitch('${alts[1]}')">${alts[1]}</a>&nbsp;
                </figcaption>
            </figure>`
    }
}
function showStitches(){

    d3.select('#gallery').style('display','block')
}
function hideStitches(){

    //d3.select('#gallery').style('display','none')
}
function setStitch(stitch){
    n = document.querySelector("#stitchDef")
    n.value = stitch
    n.focus()
    hideStitches()
}