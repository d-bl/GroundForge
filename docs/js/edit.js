function clickedPair() {
    var elem = d3.event.target
    d3.select(event.target).style("marker-mid",function() {
        var n = d3.select('#twists').attr("value")
        if (n <= 0) return ""
            return 'url("#twist-' + n + '")'
    })
    d3.select("#download2").style("display","none")
}
function clickedStitch(event) {
    d3.select("#download2").style("display","none")
    var elem = event.target.parentElement
    switch (document.querySelector("input[name=editMode]:checked").value) {
    case "delete":
        var links = d3.selectAll(".link").filter(function () {
            return this.getAttribute("id").includes(elem.id)
        })
        if (4 == links.size()) {
            // TODO reconnect pairs, first add (kissing) pair nrs as class
            elem.parentNode.removeChild(elem)
        }
        break
    case "change":
        var txt = dropTwists(d3.select("#stitchDef").node().value)
        elem.innerHTML = "<title>"+txt+"</title>"+PairSvg.shapes(txt)
        break
    }
}
function dropTwists(s) {
    return s.toLowerCase().replace(/[tlr]*([tlrc]*c)[tlr]*/,'$1')
}
function clones (f) { // f is a
    d3.select("#download2").style("display","none")
    var f8 = f * 0.8
    var w = f * (document.querySelector("#width").value - 1)
    var h = f * (document.querySelector("#height").value - 1)
    var dx
    var dy
    if (document.querySelector("input[name=shift]:checked").value == 'vert') {
        dx = 0
        dy = f * document.querySelector("#shiftSteps").value
    } else {
        dx = f * document.querySelector("#shiftSteps").value
        dy = 0
    }
    function pattern(x,y,s){
      return `
        <g transform="scale(0.4,0.4) translate(${x},${y})">
          <use xlink:href="#cl${s[0]}" x="0" y="0" />
          <use xlink:href="#cl${s[1]}" x="${w}" y="0" />
          <use xlink:href="#cl${s[2]}" x="${2*w}" y="${dy}" />
          <use xlink:href="#cl${s[3]}" x="${3*w}" y="${dy}" />

          <use xlink:href="#cl${s[4]}" x="0" y="${h}" />
          <use xlink:href="#cl${s[5]}" x="${w}" y="${h}" />
          <use xlink:href="#cl${s[6]}" x="${2*w}" y="${h+dy}" />
          <use xlink:href="#cl${s[7]}" x="${3*w}" y="${h+dy}" />

          <use xlink:href="#cl${s[8]}" x="${dx}" y="${2*h}" />
          <use xlink:href="#cl${s[9]}" x="${w+dx}" y="${2*h}" />
          <use xlink:href="#cl${s[10]}" x="${2*w+dx}" y="${2*h+dy}" />
          <use xlink:href="#cl${s[11]}" x="${3*w+dx}" y="${2*h+dy}" />

          <use xlink:href="#cl${s[12]}" x="${dx}" y="${3*h}" />
          <use xlink:href="#cl${s[13]}" x="${w+dx}" y="${3*h}" />
          <use xlink:href="#cl${s[14]}" x="${2*w+dx}" y="${3*h+dy}" />
          <use xlink:href="#cl${s[15]}" x="${3*w+dx}" y="${3*h+dy}" />
        </g>
      `
    }
    var b = ''
    var d = `scale(-1,1) translate(${-w-f8},0)`
    var p = `scale(1,-1) translate(0,${-h-f8})`
    var q = `scale(-1,-1) translate(${-w-f8},${-h-f8})`

    // 4 base clones out of sight and on top of one another allow translates independent of b/d/p/q
    // TODO position the patterns depending on the width of the viewport
    d3.select('#template #clones').html(`
      <g id="clb"><use x="0" y="0" xlink:href="#cloned" transform="translate(${-w-f8},${-h-f8})" /></g>
      <g id="cld"><use x="0" y="0" xlink:href="#cloned" transform="scale(-1,1) translate(0,${-h-f8})" /></g>
      <g id="clp"><use x="0" y="0" xlink:href="#cloned" transform="scale(1,-1) translate(${-w-f8},0)" /></g>
      <g id="clq"><use x="0" y="0" xlink:href="#cloned" transform="scale(-1,-1)" /></g>
      ${pattern(5*w, h+f8, 'bbbb'+'bbbb'+'bbbb'+'bbbb')}
      ${pattern(12*w, h+f8, 'bbbb'+'dddd'+'bbbb'+'dddd')}
      ${pattern(17*w, 6.5*h+f8, 'bdbd'+'bdbd'+'bdbd'+'bdbd')}

      ${pattern(10*w, 6.5*h+f8, 'bqbq'+'bqbq'+'bqbq'+'bqbq')}
      ${pattern(15*w, 12*h+f8, 'bdpq'+'pqbd'+'bdpq'+'pqdb')}

      ${pattern(1.5*w, 6.5*h+f8, 'bpbp'+'dqdq'+'bpbp'+'qpqp')}
      ${pattern(7*w, 12*h+f8, 'dbdb'+'qpqp'+'bdbd'+'pqpq')}
    `)
}
function initDiagram() {
    var pattern = document.querySelector("input[name=variant]:checked").value
    var cols = document.querySelector("#width").value
    var rows = document.querySelector("#height").value

    // factor is related to scale of #cloned
    var f = 25.2
    var w = 12 * f * (document.querySelector("#width").value - 1)
    var h = 9 * f * (document.querySelector("#height").value - 1)
    var q = `patchWidth=${cols}&patchHeight=${rows}&${pattern}`
    var zoom = 1.9
    var svg = PairSvg.render(TilesConfig(q).getItemMatrix, w, h , zoom)

    d3.select('#template').html(svg)
    d3.select('#cloned').attr("transform",`scale(1.8,1.8)`)
    d3.selectAll('#template title').html(function() {
        return dropTwists(this.innerHTML.replace(/ - .*/,''))
    })
    clones(f)

    // only needed at onload but here we have the value of f available
    d3.selectAll(".re_clone").attr("onchange",`clones(${f})`)

    var red = "rgb(255, 0, 0)"
    var green = "rgb(0, 255, 0)"
    var grey = "rgb(220, 220, 220)"
    var links = d3.selectAll(".link")

    function moveStitch() {
        var id = this.getAttribute("id")
        var newX = d3.event.x
        var newY = d3.event.y
        // TODO for now it is the responsibility of the user to stay withing the
        var newXY = `${newX},${newY}`

        function moveEnd(){
            var def = this.getAttribute("d").split(" ")
            def[4] = newXY
            return withMovedMid(def[1], newX, newY, def)
        }
        function moveStart(){
            var def = this.getAttribute("d").split(" ")
            def[1] = newXY
            return withMovedMid(def[4], newX, newY, def)
        }
        this.setAttribute("transform", `translate(${newXY})`)
        links.filter(function () {
            return this.getAttribute("id").startsWith(id+"-")
        }).attr("d", moveStart)
        links.filter(function () {
            return this.getAttribute("id").endsWith("-"+id)
        }).attr("d", moveEnd)
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

        // split the id of the manipulated link into the ids of its nodes
        var ids = new Set(this.getAttribute("id").split("-"))

        // mark links with red that touch the manipulated link, on each side we get one that is not part of a cycle
        // TODO we actually need the rest of both cycles as potential targets
        links.filter(function () {
            var ids2 = this.getAttribute("id").split("-")
            return ids.has(ids2[0]) || ids.has(ids2[1])
        }).style("stroke",red)

        // find the edge with the centre closest to the mouse position
        var nearest = null
        links.each(function () {
            if (nearest == null) nearest = this
            else if (this.style["stroke"] == red) return // avoid neighbour edges
            else {
                distThis = dist(this)
                if (distThis == 0 ) return
                else if ( dist(nearest) > distThis) nearest = this
            }
        })
        // TODO reuse moveCenter
        var def = nearest.getAttribute("d").split(" ")
        def[2] = d3.event.x
        def[3] = d3.event.y
        nearest.setAttribute("d", def.join(" "))
    }

    links.on("click",clickedPair)
    d3.drag().on("drag",moveStitch)(d3.selectAll(".node"))

    links.style("stroke-width","5px") // wider lines are bigger targets
    links.style("stroke",grey) // keep the twist marks visible
    d3.drag()
        .on("end", finishPinch)
        .on("drag", moveCenter)
        .on("start", function () {
            links.style("stroke",grey) // reset pinch highlights
        })(links)
}
function withMovedMid(end, newX, newY, def) {
    xy = end.split(',')
    def[2] = (xy[0]*1 + newX)/2
    def[3] = (xy[1]*1 + newY)/2
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
