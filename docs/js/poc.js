function clickedDot(linkElement, event) {
    var link = document.getElementById("customlink")
    var shape = linkElement.getElementsByTagName("title")[0].parentElement
    var shapeId = shape.attributes["id"].value
    var action = document.querySelector('input[name = "stitch"]:checked').value
    if (action == "remove") {
        // for now show the event is applied, custom link shows the calculated value
        shape.getElementsByTagName("title")[0].innerHTML = `${shapeId} : deleted from custom link`
        shape.style = `fill:${color};opacity:0.0`
        var currentLinks = link.href.replace(/.*=/,"")
        var newlinks = TopoLink.removeStitch(shapeId,currentLinks)
        link.href = "?topo=" + newlinks
        showDiagram(newlinks)
    } else {
        var color = document.getElementById("color").value
        var stitch = document.getElementById("stitch").value
        var cx = shape.getAttribute("cx") * 1
        var cy = shape.getAttribute("cy") * 1
        var t = `<title>${shapeId} ${stitch}</title>`
        var s = `style="fill:${color};opacity:0.65" id="${shapeId}"`
        switch(document.getElementById("shape").value) {
          case "square":
            shape.outerHTML = `<rect x="${cx - 8}" y="${cy - 8}" width="16" height="16" ${s}>${t}</rect>`
            break;
          case "diamond":
            var points = `${cx + 0},${cy + 11} ${cx + 11},${cy + 0} ${cx},${cy - 11} ${cx- 11},${cy}`
            shape.outerHTML = `<polygon points="${points}" ${s}>${t}</polygon>`
            break;
          case "ne":
            var points = `${cx + 8},${cy + 8} ${cx + 8},${cy - 8} ${cx - 8},${cy - 8}`
            shape.outerHTML = `<polygon points="${points}" ${s}>${t}</polygon>`
            break;
          case "nw":
            var points = `${cx + 8},${cy - 8} ${cx - 8},${cy - 8} ${cx - 8},${cy + 8}`
            shape.outerHTML = `<polygon points="${points}" ${s}>${t}</polygon>`
            break;
          case "sw":
            var points = `${cx - 8},${cy - 8} ${cx - 8},${cy + 8} ${cx + 8},${cy + 8}`
            shape.outerHTML = `<polygon points="${points}" ${s}>${t}</polygon>`
            break;
          case "se":
            var points = `${cx - 8},${cy + 8} ${cx + 8},${cy + 8} ${cx + 8},${cy - 8}`
            shape.outerHTML = `<polygon points="${points}" ${s}>${t}</polygon>`
            break;
          case "north":
            var points = `${cx + 11},${cy + 6} ${cx - 11},${cy + 6} ${cx},${cy - 11}`
            shape.outerHTML = `<polygon points="${points}" ${s}>${t}</polygon>`
            break;
          case "south":
            var points = `${cx + 11},${cy - 6} ${cx - 11},${cy - 6} ${cx},${cy + 11}`
            shape.outerHTML = `<polygon points="${points}" ${s}>${t}</polygon>`
            break;
          case "east":
            var points = `${cx + 8},${cy - 10} ${cx - 10},${cy} ${cx + 8},${cy + 10}`
            shape.outerHTML = `<polygon points="${points}" ${s}>${t}</polygon>`
            break;
          case "west":
            var points = `${cx - 8},${cy - 10} ${cx + 10},${cy} ${cx - 8},${cy + 10}`
            shape.outerHTML = `<polygon points="${points}" ${s}>${t}</polygon>`
            break;
          case "left":
            var points = `${cx + 0.6},${cy - 8.311283} a 8,8 0 0 0 -6.9496799,3.980729 8,8 0 0 0 -0.01701,8.008993 8,8 0 0 0 6.9327099,4.010211`
            shape.outerHTML = `<path d="M ${points}" ${s}>${t}</path>`
            break;
          case "right":
            var points = `${cx + 0.6},${cy - 7.7} a 8,8 0 0 1 6.935907,3.987789 8,8 0 0 1 0.01376,8.000568 8,8 0 0 1 -6.922151,4.011619`
            shape.outerHTML = `<path d="m ${points}" ${s}>${t}</path>`
            break;
          case "bottom":
            var points = `${cx + 8.6},${cy + 0.3} a 8,8 0 0 1 -4.007957,6.924526 8,8 0 0 1 -8.0007935,-0.0097 8,8 0 0 1 -3.9912372,-6.934177`
            shape.outerHTML = `<path d="m ${points}" ${s}>${t}</path>`
            break;
          case "top":
            var points = `${cx - 8},${cy + 0.3} a 8,8 0 0 1 7.9863634,-7.986107 8,8 0 0 1 8.013519,7.958859`
            shape.outerHTML = `<path d="m ${points}" ${s}>${t}</path>`
            break;
          case "star":
            var points = `${cx + 6},${cy + 8.3} c -1.9321729,1.403808 -3.3124016,-2.216838 -5.7006147,-2.237061 -2.4237202,-0.02052 -3.8741562,3.564059 -5.8350582,2.139382 -1.932176,-1.403804 1.084749,-3.835321 0.365984,-6.112896 -0.729451,-2.311438 -4.586802,-2.583187 -3.837806,-4.8883654 0.738022,-2.2714077 3.982814,-0.1535215 5.926806,-1.5409169 1.9728943,-1.4080229 1.039356,-5.1605561 3.4631642,-5.1605584 2.3882981,-2.3e-6 1.376765,3.7404401 3.2969818,5.1605576 1.9487679,1.4412315 5.22915963,-0.6062125 5.97815963,1.6989647 0.73802697,2.2714064 -3.13192573,2.4652404 -3.88915773,4.7303164 -0.768491,2.298753 2.19244103,4.785897 0.231541,6.210577`
            shape.outerHTML = `<path d="m ${points} z" ${s}>${t}</path>`
            break;
          default:
            // regular expression to implement new shapes:
            // search: ([0-9.e-]+),([0-9.e-]+)
            // replace: \${cx + $1},\${cy + $2}
            // finally replace "+ -" with "-"
            shape.outerHTML = `<circle cx="${cx}" cy="${cy}" r="8" ${s}>${t}</circle>`
        }
    }
    return false
}
function clickedLink(linkElement, event) {
    var weight = document.querySelector('input[name = "weight"]:checked').value
    var lineId = linkElement.getElementsByTagName("line")[0].attributes["id"].value
    var link = document.getElementById("customlink")
    var topolinks = TopoLink.changeWeight(lineId, weight, link.href.replace(/.*=/,""))
    link.href = "?topo=" + topolinks

    // for now show the event is applied, custom link shows the calculated value
    var titleElem = linkElement.getElementsByTagName("title")[0]
    titleElem.innerHTML = titleElem.innerHTML + `; ${weight}`
    showDiagram(topolinks)
    return false
}
function setPattern(list) {
  document.getElementById("diagram").innerHTML = "please wait"
  window.location.replace(list.options[list.selectedIndex].value)
}
function changeSpeed(scaleElement) {
    var w = 1 - (scaleElement.value / 100)
    document.getElementById("weaker").value = w
    document.getElementById("stronger").value = 1 / w
}
function load() {
    document.getElementById("diagram").innerHTML = "please wait"
    var urlQuery = location.search.substr(1)
    var topolinks = typeof urlQuery === "undefined" || urlQuery.trim() == ""
        ? "lo,b4,ri,a1;lo,d4,li,a1;lo,b2,ri,a3;lo,d2,li,a3;lo,a1,li,b1;ro,b4,ri,b1;lo,b1,li,b2;lo,c1,ri,b2;lo,a3,li,b4;lo,c3,ri,b4;ro,b1,li,c1;ro,d4,ri,c1;ro,b2,li,c3;lo,d3,ri,c3;ro,a1,ri,d2;ro,c1,li,d2;ro,a3,ri,d3;ro,d2,li,d3;ro,c3,li,d4;ro,d3,ri,d4"
        : TopoLink.asString(TopoLink.fromUrlQuery(urlQuery))
    document.getElementById("customlink").href = "?topo=" + topolinks
    showDiagram(topolinks)
}
function showDiagram(topolinks) {
    document.getElementById("diagram").innerHTML = "please wait"
    var elem =  document.getElementById("diagram")
    var links = TopoLink.fromUrlQuery("topo="+topolinks)
    var data = Data.create(links)
    if (data.length == 0) {
        console.log(links.toString())
        elem.innerHTML = elem.innerHTML + "<p>whoops, console logging may have details</p>"
    } else {
        const { u, v, q } = SVDJS.SVD(data)
        elem.innerHTML = SvgPricking.create(Delta.create(links,u,v,q))
    }
}
function setDownloadContent (linkNode) {
  var svg = document.getElementById("diagram").innerHTML
  linkNode.href = 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
