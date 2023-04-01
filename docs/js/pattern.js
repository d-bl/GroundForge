var valueFilter = /[^a-zA-Z0-9,-=]/g
var isMobile = /iPad|iPhone|iPod|Mobi/.test(navigator.userAgent)
function toKeyValueString (formField) {
    var n = formField.name
    var v = formField.value
    return !n || !v ? '' : n + '=' +  v.replace(/\n/g,",").replace(valueFilter,"")
}
function isKeyValue (formField) {

    return formField.name && formField.value
}
function submitQuery() {

  return d3.selectAll('input, textarea')
    .nodes()
    .filter(isKeyValue)
    .map(toKeyValueString)
    .join("&")
}
function maximize(containerId) {
  d3.select(containerId).style("width","100%").style("height","100%")
  return false;
}
function minimize(containerId) {
  d3.select(containerId).style("width","250px").style("height","250px")
  return false;
}
function tesselace(query){

  // keep tesselace reference as long as tile definition is unchanged
  var tesselace = ""
  if (window.location.search.substr(1).includes("tesselace=")) {
    // obtain tile definition from url
    var urlTile = window.location.search.replace(/(.*[?&])?tile=/, "").replace(/&.*/, "")
    // obtain new tile definition from user interface
    var configTile = query.replace(/(.*[?&])?tile=/, "").replace(/&.*/, "")
    // compare to tile definition from user interface
    if (urlTile == configTile) {
        // formulate tesselace reference
        tesselace = window.location.search.replace(/(.*[?&])?tesselace=/, "tesselace=").replace(/&.*/, "&")
    }
  }
  return tesselace
}
function pocRef (q) {
  return "" +
    q.replace(       /.*(tile=[^&]+).*/,"\$1") + "&" +
    q.replace( /.*(patchWidth=[^&]+).*/,"\$1") + "&" +
    q.replace(/.*(patchHeight=[^&]+).*/,"\$1") + "&" +
    q.replace(/.*(shiftColsSE=[^&]+).*/,"\$1") + "&" +
    q.replace(/.*(shiftRowsSE=[^&]+).*/,"\$1") + "&" +
    q.replace(/.*(shiftColsSW=[^&]+).*/,"\$1") + "&" +
    q.replace(/.*(shiftRowsSW=[^&]+).*/,"\$1") + "&" +
    ""
}
function showProto(q) {

  console.log("start showProto")
  var config = TilesConfig(q ? q : submitQuery())
  d3.select("#prototype").html(PrototypeDiagram.create(config))

  // new form fields may have been added what changes the query
  var query = submitQuery()

  console.log("cleared")
  //var encoded = encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8"?-->' + svg)
  d3.select("#to_self").node().href = "pattern?" + query
  d3.select("#to_stitches").node().href = "stitches.html?" + query
  d3.selectAll("#pattern textarea").attr("rows", config.maxTileRows + 1)
  d3.select("#footside").attr("cols", config.leftMatrixCols + 2)
  d3.select("#tile"    ).attr("cols", config.centerMatrixCols + 2)
  d3.select("#headside").attr("cols", config.rightMatrixCols + 2)
  console.log("done showProto")
  d3.select("#simpleTiling").on("click",function() {asSimple()})
  return config
}
function toggleCheatSheet(imgElement) {
  var value = imgElement.dataset.img;
  if (imgElement.src && imgElement.src.includes("extended")) {
    imgElement.src = "images/matrix-template.png";
    imgElement.title="click to show additional symbols";
  } else {
    imgElement.src = "images/matrix-template-extended.png";
    imgElement.title="click to only show basic symbols";
  }
}
function flip(){
  var left = d3.select("#footside").node().value
  console.log(Matrix.flip)
  d3.select("#headside").node().value = Matrix.flip(left)
  showProto()
}
function scrollIntoViewIfPossible(container) {
  // despite w3Schools documentation not available for IE / Edge(?)
  // see also https://developer.microsoft.com/en-us/microsoft-edge/platform/issues/15534521/
  if (container.scrollIntoView) {
    container.scrollIntoView({ block: "start", behavior: 'smooth' })
  }
}
function scrollToIfPossible(container, x, y) {
  if (container.scrollTop !== undefined && container.scrollLeft !== undefined) {
    container.scrollTop = y
    container.scrollLeft = x
  }
}
function setField (keyValueString) {

    var k = keyValueString.replace(/=.*/,"").trim().replace(/[^a-zA-Z0-9]/g,"")
    var v = keyValueString.replace(/[^=]+=/,"").trim().replace(valueFilter,"").replace(/,/g,"\n")
    if (k) d3.select('#'+k).property("value", v)
}
function load() {

  var q = window.location.search.substr(1)
  var keyValueStrings = q.split("&")
  keyValueStrings.forEach(setField)
  d3.select("#tyleSVG").attr("data", "_includes/tileLayout.svg")
  showProto(q) // this creates a dynamic part of the form
}
function getMatrixLines() {

  return d3.select('#tile').node().value.toUpperCase().trim().split(/[^-A-Z0-9]+/)
}
function asSimple() {
  var matrixLines = getMatrixLines()
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  d3.select('#shiftRowsSE').property("value", rows)
  d3.select('#shiftColsSE').property("value", cols)
  d3.select('#shiftRowsSW').property("value", rows)
  d3.select('#shiftColsSW').property("value", 0)
  showProto()
}
function asHorBricks() {

  var matrixLines = getMatrixLines()
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  d3.select('#shiftRowsSE').property("value", rows)
  d3.select('#shiftColsSE').property("value", cols - Math.round(cols/2))
  d3.select('#shiftRowsSW').property("value", rows)
  d3.select('#shiftColsSW').property("value", - Math.round(cols/2))
  showProto()
}
function asVerBricks() {

  var matrixLines = getMatrixLines()
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  d3.select('#shiftRowsSE').property("value", Math.round(rows/2))
  d3.select('#shiftColsSE').property("value", cols)
  d3.select('#shiftRowsSW').property("value", rows)
  d3.select('#shiftColsSW').property("value", 0)
  showProto()
}
function asCornerToCorner() {

  var matrixLines = getMatrixLines()
  var rows = matrixLines.length
  var cols = matrixLines[0].length
  d3.select('#shiftRowsSE').property("value", rows)
  d3.select('#shiftColsSE').property("value", cols)
  d3.select('#shiftRowsSW').property("value", rows)
  d3.select('#shiftColsSW').property("value", - cols)
  showProto()
}
function brickToLeft() {

  d3.select('#shiftColsSE').node().value--
  d3.select('#shiftColsSW').node().value--
  showProto()
}
function brickToRight() {

  d3.select('#shiftColsSE').node().value++
  d3.select('#shiftColsSW').node().value++
  showProto()
}
function brickUp() {

  d3.select('#shiftRowsSE').node().value--
  showProto()
}
function brickDown() {

  d3.select('#shiftRowsSE').node().value++
  showProto()
}
function seToLeft() {

  d3.select('#shiftColsSE').node().value--
  showProto()
}
function seUp() {

  d3.select('#shiftRowsSE').node().value--
  showProto()
}
function seToRight() {

  d3.select('#shiftColsSE').node().value++
  showProto()
}
function seDown() {

  d3.select('#shiftRowsSE').node().value++
  showProto()
}
function swToRight() {

  d3.select('#shiftColsSW').node().value++
  showProto()
}
function swUp() {

  d3.select('#shiftRowsSW').node().value--
  showProto()
}
function swToLeft() {

  d3.select('#shiftColsSW').node().value--
  showProto()
}
function swDown() {

  d3.select('#shiftRowsSW').node().value++
  showProto()
}
