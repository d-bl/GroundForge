function clickedStitch(event) {
    alert(`you clicked ${event.currentTarget.id}: ${event.currentTarget.textContent}`)
}
function load() {

    // dimensions for an A4
    var width = 744
    var height = 1052

    // render the initial diagram
    var cfg = TilesConfig(window.location.search.substr(1))
    console.log(`rows=${cfg.totalRows} cols=${cfg.totalCols} -> ${(cfg.totalRows+2)*15},${(cfg.totalCols+2)*15} ; A4=${width},${height}`)
    var zoom = 1.9
    var itemMatrix = cfg.getItemMatrix
    var svg = PairSvg.render(itemMatrix, width, height, zoom)
    d3.select('#initial').html(svg)

    var red = "rgb(255, 0, 0)"
    var green = "rgb(0, 255, 0)"
    var grey = "rgb(150, 150, 150)"
    var links = d3.selectAll(".link")
    links.style("stroke-width","5px") // wider lines are bigger targets
    links.style("stroke",grey) // keep the twist marks visible
    d3.drag()
        .on("start", function () {
            links.style("stroke",grey)
        })
        .on("drag", function () {
            var def = this.getAttribute("d").split(" ")
            def[2] = d3.event.x
            def[3] = d3.event.y
            this.setAttribute("d", def.join(" "))
        })
        .on("end", function () {
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
        })(links)
}