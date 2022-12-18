var green = "rgb(0, 255, 0)"
var grey = "rgb(200, 200, 200)"
function clickedPair() {
    var elem = d3.event.target
    d3.select(event.target).style("marker-mid",function() {
        var n = d3.select('#twists').node().value
        if (n <= 0) return ""
            return 'url("#twist-' + n + '")'
    })
    d3.selectAll('#cloned .link').style('stroke',grey) // revert drag().on("start", ...)
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
        elem.innerHTML = "<title>"+txt+"</title>"+PairSvg.shapes(txt)
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
function dropTwists(s) {

    return s.toLowerCase().replace(/[tlr]*([tlrc]*c)[tlr]*/,'$1')
}
function clones (f) { // f is a
    d3.select("#download2").style("display","none")
    var stitchesW = document.querySelector("#width").value * 1
    var stitchesH = document.querySelector("#height").value * 1
    var f8 = f * 0.8
    var w = f * (stitchesW - 1)
    var h = f * (stitchesH - 1)
    var indentSteps = document.querySelector("#indentSteps").value * 1
    var bAndOneOther = document.querySelector("#bAndOneOther").value.split(",")
    var bdpqRowsCols = document.querySelector("#bdpqRowsCols").value.split(",")
    var b = ''
    var d = `scale(-1,1) translate(${-w-f8},0)`
    var p = `scale(1,-1) translate(0,${-h-f8})`
    var q = `scale(-1,-1) translate(${-w-f8},${-h-f8})`

    function pattern(patternX, patternY, s, indentX, indentY){
      console.log (`w=${w} h=${h} indentX=${indentX} indentY=${indentY}`)
      var m = s.split(' ')
      var result = ''
      for (let row = 0; row < 6; row++) {
        for (let column = 0; column < 6; column++) {
          x = column * w + f * ((row * indentX) % (stitchesW - 1))
          y = row * h + f * ((column * indentY) % (stitchesH - 1))
          var c = column + Math.floor((row * indentX)/(stitchesW - 1))
          var r = row + Math.floor((column * indentY)/(stitchesH - 1))
          result += `<use xlink:href="#cl${m[r%4][c%4]}" x="${x}" y="${y}"/>`
        }
      }
      return `
        <g transform="scale(0.3,0.3) translate(${patternX},${patternY})">
          ${result}
        </g>
      `
    }

    // 4 base clones out of sight and on top of one another allow translates independent of b/d/p/q
    // TODO position the patterns depending on the width of the viewport
    d3.select('#template #clones').html(`
      <g id="clb"><use x="0" y="0" xlink:href="#cloned" transform="translate(${-w-f8},${-h-f8})" /></g>
      <g id="cld"><use x="0" y="0" xlink:href="#cloned" transform="scale(-1,1) translate(0,${-h-f8})" /></g>
      <g id="clp"><use x="0" y="0" xlink:href="#cloned" transform="scale(1,-1) translate(${-w-f8},0)" /></g>
      <g id="clq"><use x="0" y="0" xlink:href="#cloned" transform="scale(-1,-1)" /></g>

      ${pattern( 9*w, h+f8, bAndOneOther[2], 0, 0)}
      ${pattern(20*w, h+f8, bAndOneOther[1], 0, indentSteps)}
      ${pattern(27*w, h+f8, bAndOneOther[0], indentSteps, 0)}

      ${pattern(w+f8, 10*h+f8, bdpqRowsCols[1], 0, indentSteps)}
      ${pattern( 8*w, 10*h+f8, bdpqRowsCols[0], indentSteps, 0)}

      ${pattern( 9*w, 18*h+f8, 'dbpq dbpq dbpq dbpq', 0, 0)}
      ${pattern(16*w, 18*h+f8, 'bbbb dddd qqqq pppp', 0, 0)}
      ${pattern(23*w, 18*h+f8, 'dbdb qpqp bdbd pqpq', 0, 0)}
      ${pattern(30*w, 18*h+f8, 'bpbp dqdq bpbp dqdq', 0, 0)}
    `)
}
function initDiagram() {
    var pattern = document.querySelector("input[name=variant]:checked").value
    var cols = document.querySelector("#width").value
    var rows = document.querySelector("#height").value


    var clonedScale = "scale(1.8,1.8)"
    var f = 25.2 // related to clonedScale
    var w = 14.7 * f * (document.querySelector("#width").value - 1)
    var h = 9 * f * (document.querySelector("#height").value - 1)
    var q = `patchWidth=${cols}&patchHeight=${rows}&${pattern}`
    var svg = PairSvg.render(TilesConfig(q).getItemMatrix, w, h , 1)

    d3.select('#template').html(svg)
    d3.select('#cloned').attr("transform", clonedScale)
    d3.selectAll('#template title').html(function() {
        return dropTwists(this.innerHTML.replace(/ - .*/,''))
    })
    clones(f)

    // only needed at onload but here we have the value of f available
    d3.selectAll(".re_clone").attr("onchange",`clones(${f})`)

    var regex = /r[0-9]+c([0-9]+)-r[0-9]+c([0-9]+)/
    var links = d3.selectAll(".link")
    links.each(function () {
        this.classList.add('kiss_' + this.id.replace(regex,'$1_$2'))
        this.classList.add('kiss_' + this.id.replace(regex,'$2_$1'))
    })
    links.on("click",clickedPair)
    links.style("stroke-width","5px") // wider lines are bigger targets
    links.style("stroke",grey) // keep the twist marks visible
    links.style("stroke-linejoin","bevel")
    d3.drag().on("drag",moveStitch)(d3.selectAll(".node").filter(function(){ return 4 == nrOfLinks(this.id) }))
    dragLinks(links)
}
function dragLinks(links){
    d3.drag()
        .on("end", finishPinch)
        .on("drag", moveCenter)
        .on("start", function () {
            findKissingPairs(this).style("stroke",green)
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
    kissingPairs.style("stroke",grey)

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
    //TODO add pairs referring to: el.setAttribute('id', `${this.id}_${nearest.id}`)
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
    p2.setAttribute("style", "stroke: rgb(200, 200, 200); stroke-width: 5px; fill: none; opacity: 1; stroke-linejoin: bevel;")
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
            activateEdit()
        }
        r.readAsText(f);
    } else {
        alert("Failed to load file");
    }
}
