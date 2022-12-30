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
        return this.getAttribute("id").includes(id)
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
            d3.selectAll("#cloned .link").filter(function () {
                return this.id.startsWith(deletedStitchId + '-') // incoming pair
            }).each(function () {
                var newXY = this.getAttribute("d").split(" ")[4]
                var newEndId = this.id.replace(/.*-/,'')
                d3.selectAll(`#cloned .${this.classList[1]}`).filter(function () {
                    // outgoing pair, the same kissing pair as the incoming pair
                    return this.id.endsWith('-' + deletedStitchId)
                }).each(function () {
                    var def = this.getAttribute("d").split(" ")
                    this.setAttribute("d", withMovedMid(def[1], def[4] = newXY, def))
                    this.id = this.id.replace(/-(.*)/, '-'+newEndId)
                })
                this.parentNode.removeChild(this)
            })
            elem.parentNode.removeChild(elem)
        }
        break
    }
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
    var dimX =  document.querySelector("#width").value - 1
    var dimY = document.querySelector("#height").value - 1
    var w = stitchDistance * dimX
    var h = stitchDistance * dimY

    // just one of the indent arguments is non-zero
    function pattern(patternX, patternY, templateArrangement, indentX, indentY, indentX2, indentY2){

      // 4x4 letters separated with 3 spaces, split into a 2D array
      if (!templateArrangement.match(/([bdpq]{4} ){3}[bdpq]{4}/)) return
      var ta = templateArrangement.split(' ')

      var result = ''
      for (let swatchRow = 0; swatchRow < 6; swatchRow++) {
        for (let swatchColumn = 0; swatchColumn < 6; swatchColumn++) {
          var indentRow = swatchRow * indentX + (Math.floor(swatchRow / 2)) * indentX2
          var indentColumn = swatchColumn * indentY + (Math.floor(swatchColumn / 2)) * indentY2
          var swatchX = stitchDistance*(swatchColumn * dimX + ((indentRow+1) % dimX))
          var swatchY = stitchDistance*(swatchRow * dimY + ((indentColumn+1) % dimY))
          var taC = swatchColumn + 400*dimX - Math.floor((indentRow+1) / dimX)
          var taR = swatchRow + 400*dimY - Math.floor((indentColumn+1) / dimY)
          result += `<use xlink:href="#cl${ta[taR%4][taC%4]}" x="${swatchX}" y="${swatchY}"/>`
        }
      }
      return `
        <g transform="scale(${document.querySelector("#swatch_scale").value}) translate(${patternX},${patternY})">
          ${result}
        </g>
      `
    }
    var indentSteps = document.querySelector("#indentSteps").value * 1
    var bAndOneOther = document.querySelector("#bAndOneOther").value.split(",")
    var bdpqRowsCols = document.querySelector("#bdpqRowsCols").value.split(",")
    var customPattern = document.querySelector("#customPattern").value.replace(/\n/g,' ')
    var f8 = stitchDistance * 0.8 // some margin for the template

    // 4 base clones out of sight and on top of one another allow translates independent of b/d/p/q
    // TODO position the patterns depending on the width of the viewport
    d3.select('#template #clones').html(`
      <g id="clb"><use x="0" y="0" xlink:href="#cloned" transform="translate(${-w-f8},${-h-f8})" /></g>
      <g id="cld"><use x="0" y="0" xlink:href="#cloned" transform="scale(-1,1) translate(0,${-h-f8})" /></g>
      <g id="clp"><use x="0" y="0" xlink:href="#cloned" transform="scale(1,-1) translate(${-w-f8},0)" /></g>
      <g id="clq"><use x="0" y="0" xlink:href="#cloned" transform="scale(-1,-1)" /></g>

      ${pattern( 9*w, h, bAndOneOther[2], 0, 0, 0, 0)}
      ${pattern(16*w, h, bAndOneOther[1], 0, indentSteps, 0, 0)}
      ${pattern(23*w, h, bAndOneOther[0], indentSteps, 0, 0, 0)}

      ${pattern(f8+w, 10*h, bdpqRowsCols[1], 0, indentSteps, 0, 0)}
      ${pattern( 8*w, 10*h, bdpqRowsCols[0], indentSteps, 0, 0, 0)}
      ${pattern(16*w, 10*h, bdpqRowsCols[1], 0, 0, 0, indentSteps)}
      ${pattern(23*w, 10*h, bdpqRowsCols[0], 0, 0, indentSteps, 0)}

      ${pattern(f8+w, 18*h, customPattern, 0, indentSteps, 0, 0)}
      ${pattern( 8*w, 18*h, customPattern, indentSteps, 0, 0, 0)}
      ${pattern(16*w, 18*h, customPattern, 0, 0, 0, indentSteps)}
      ${pattern(23*w, 18*h, customPattern, 0, 0, indentSteps, 0)}
    `)
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

    var w = 11 * stitchDistance * (document.querySelector("#width").value - 1)
    var h = 8 * stitchDistance * (document.querySelector("#height").value - 1)
    var q = `patchWidth=${cols}&patchHeight=${rows}&${pattern}`
    var svg = PairSvg.render(TilesConfig(q).getItemMatrix, w, h , 1)

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
    var links = d3.selectAll(".link")
    links.each(function () {
        this.classList.add('kiss_' + this.id.replace(regex,'$1_$2'))
        this.classList.add('kiss_' + this.id.replace(regex,'$2_$1'))
    })
    links.style("stroke-width","5px") // wider lines are bigger targets
    links.style("stroke-opacity",0.25) // keep the twist marks visible
    links.style("stroke-linejoin","bevel")
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
    createLegend()
}
function dragLinks(links){
    d3.drag()
        .on("end", finishPinch)
        .on("drag", moveCenter)
        .on("start", function () {
            findKissingPairs(this).style("stroke","rgb(0, 255, 0)").style('stroke-opacity',"0.25")
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
    kissingPairs = findKissingPairs(this)
    kissingPairs.style("stroke","rgb(0,0,0)").style('stroke-opacity',"0.25")

    // find the edge with the centre closest to the mouse position
    var nearest = null
    kissingPairs.each(function () {
        if (nearest == null) nearest = this
        else {
            distThis = dist(this)
            if (distThis == 0 ) return
            else if ( dist(nearest) > distThis) nearest = this
        }
    })
    var newID = Date.now()
    var newXY = `${d3.event.x},${d3.event.y}`

    var el = document.createElementNS("http://www.w3.org/2000/svg", "g")
    el.innerHTML = PairSvg.shapes('ctc')
    el.setAttribute('transform',`translate(${d3.event.x},${d3.event.y})`)
    el.setAttribute('onclick', "clickedStitch(event)")
    el.setAttribute('id', newID)
    d3.drag().on("drag",moveStitch)(d3.select(el))
    var container = document.querySelector('#cloned')
    container.appendChild(el)
    container.insertBefore(splitLink(this, newID, newXY), container.firstChild)
    container.insertBefore(splitLink(nearest, newID, newXY), container.firstChild)
}
function moveStitch() {
    var id = this.getAttribute("id")
    // TODO for now it is the responsibility of the user to stay within the cycles
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
        return this.getAttribute("id").startsWith(id+"-")
    }).attr("d", moveStart)
    d3.selectAll(".link").filter(function () {
        return this.getAttribute("id").endsWith("-"+id)
    }).attr("d", moveEnd)
}
function splitLink(nearest, newID, newXY) {
    var p2 = document.createElementNS("http://www.w3.org/2000/svg", "path")
    p2.setAttribute("id",newID+nearest.id.replace(/.*-/,"-"))
    p2.setAttribute("d", nearest.getAttribute("d"))
    p2.setAttribute("class", nearest.getAttribute("class"))
    p2.setAttribute("style", "stroke: rgb(0, 0, 0); stroke-width: 5px; fill: none; stroke-opacity: 0.25; stroke-linejoin: bevel;")
    var defB = p2.getAttribute("d").split(" ")
    p2.setAttribute("d", withMovedMid(defB[4], defB[1] = newXY, defB))
    var defB = nearest.getAttribute("d").split(" ")
    nearest.setAttribute("d", withMovedMid(defB[1], defB[4] = newXY, defB))
    nearest.setAttribute("id", nearest.id.replace(/-.*/,"-")+newID)
    d3.select(p2).on("click",clickedPair)
    dragLinks(d3.select(p2))
    return p2
}
function findKissingPairs(movedPair) {

   // split the id of the manipulated link into the ids of its nodes
   var involvedStitchIds = new Set(movedPair.getAttribute("id").split("-"))

   var thisClassNrs = movedPair.classList[1].replace('kiss_','').split('_')
   var kissMin = Math.min(...thisClassNrs)*1
   var kissMax = Math.max(...thisClassNrs)*1
   var kissClasses = `#cloned .kiss_${kissMin-1}_${kissMin}, #cloned .kiss_${kissMax}_${kissMax+1}`
   return d3.selectAll(kissClasses).filter(function () {
      var ids2 = this.getAttribute("id").split("-")
      return !(involvedStitchIds.has(ids2[0]) || involvedStitchIds.has(ids2[1]))
   })
   // TODO reduce to cycle
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
            var ids = []
            d3.selectAll(".node")
              .filter(function(){ return this.id.startsWith("r") })
              .each(function(){ ids[ids.length] = this.id.replace("r","").split("c") })
            var rows = Math.max(...ids.map(function(a){return a[0]})) + 1
            var cols = Math.max(...ids.map(function(a){return a[1]})) + 1
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