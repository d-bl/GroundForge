function clickedStitch(event) {
    // edit mode delete/apply
}
function load() {

    // dimensions for an A4
    var width = 744
    var height = 1052

    // render the initial diagram
    var itemMatrix = TilesConfig(window.location.search.substr(1)).getItemMatrix
    var zoom = 1.9
    var svg = PairSvg.render(itemMatrix, width, height, zoom)
    d3.select('#template').html(svg)

    var red = "rgb(255, 0, 0)"
    var green = "rgb(0, 255, 0)"
    var grey = "rgb(150, 150, 150)"
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
        // split the id of the manipulated link into the ids of its nodes
        var ids = new Set(this.getAttribute("id").split("-"))

        // mark links with red that touch the manipulated link, on each side we get one that is not part of a cycle
        // TODO we actually need the rest of both cycles as potential targets
        links.filter(function () {
            var ids2 = this.getAttribute("id").split("-")
            return ids.has(ids2[0]) || ids.has(ids2[1])
        }).style("stroke",red)

        function dist(a) {
           var def = a.getAttribute("d").split(" ")
           var powX = Math.pow(def[2]*1 - d3.event.x, 2)
           var powY = Math.pow(def[3]*1 - d3.event.y, 2)
           return powX + powY
        }
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
        nearest.style["stroke"] = green
    }

    // initial stitch edit mode
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