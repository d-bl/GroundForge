function clickedPair() {
    var elem = d3.event.target
    d3.select(event.target).style("marker-mid",function() {
        var n = d3.select('#twists').attr("value")
        if (n <= 0) return ""
            return 'url("#twist-' + n + '")'
    })
    clones()
}
function clickedStitch(event) {
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
    clones()
}
function dropTwists(s) {
    return s.toLowerCase().replace(/[tlr]*([tlrc]*c)[tlr]*/,'$1')
}
function initDiagram() {
    var pattern = document.querySelector("input[name=variant]:checked").value
    var cols = document.querySelector("#width").value
    var rows = document.querySelector("#height").value

    // factor is related to scale of #cloned
    var w = 25.2 * (document.querySelector("#width").value - 1)
    var h = 25.2 * (document.querySelector("#height").value - 1)

    var q = `patchWidth=${cols}&patchHeight=${rows}&${pattern}`
    var zoom = 1.9
    var svg = PairSvg.render(TilesConfig(q).getItemMatrix, w * 4 + 24, h * 4 + 24, zoom)

    d3.select('#template').html(svg)
    d3.select('#cloned').attr("transform",`translate(${w},${h}),scale(1.8,1.8)`)
    d3.selectAll('#template title').html(function() {
        return dropTwists(this.innerHTML.replace(/ - .*/,''))
    })
    d3.select('#template #clones').style("opacity",0.3).html(`
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${-w},${-h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(0,${-h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${w},${-h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${2*w},${-h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${-w},0)" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${w},0)" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${2*w},0)" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${-w},${h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(0,${h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${w},${h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${2*w},${h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${-w},${2*h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(0,${2*h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${w},${2*h})" />
        <use x="0" y="0" xlink:href="#cloned" transform="translate(${2*w},${2*h})" />
    `)

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
        clones()
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
        clones()
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
