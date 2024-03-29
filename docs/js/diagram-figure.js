function getDownloadContent (id) {
    svg = d3.select(id).node().innerHTML.
    replace('pointer-events="all"', '').
    replace(/<path[^>]+opacity: 0[;"].+?path>/g, '').
    replace(/<foreignObject[\s\S]*?foreignObject>/g, '')
    return 'data:image/svg+xml,' + encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8" standalone="no"?-->' + svg)
}
function prepareDownload(contentId) {
    // touch devices follow the href before onfocus changed it
    // in that case we temporarily need another link
    // that link is hidden as soon as followed
    var linkId = contentId + "DownloadLink"
    d3.select(linkId)
        .attr("href",getDownloadContent(contentId))
        .style("display","inline-block")
}
function maximize(containerId) {
    d3.select(containerId).style("width","100%").style("height","90vh")
    return false;
}
function minimize(containerId) {
    d3.select(containerId).style("width","250px").style("height","0")
    return false;
}
function resetDimensions(containerId) {
    d3.select(containerId).style("width","250px").style("height","250px")
    return false;
}
