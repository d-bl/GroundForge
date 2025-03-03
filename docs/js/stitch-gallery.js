function loadStitchExamples() {
    var stitches = [
        "cllcrrcllcrrc",
        "ctctctc",
        "ct",
        "ctct",
        "crcllrrrc",
        "clcrclc",
        "ctctc",
        "ctclctc",
        "crclct",
        "ctclcrctc",
        "ctcttctc",
        "tctctllctctr",
    ]
    for (let stitch of stitches) {
        document.querySelector("#gallery").innerHTML += `
            <figure>
                <svg width="20" height="54">
                  <g transform="scale(2,2)">
                    <g transform="translate(5,6)">
                      ${PairSvg.shapes(stitch)}
                    </g>
                  </g>
                </svg>
                <img src="/GroundForge/images/stitches/${stitch}.png"
                     title="${stitch}">
                <figcaption>
                    <a href="javaScript:setStitch('${stitch}')">${stitch}</a>&nbsp;
                </figcaption>
            </figure>`
    }
}

function paintStitchValue() {

    return d3.select("#stitchDef").node().value.toLowerCase().replace(/[^ctlrp-]/g, '')
}

function loadStitchForm(isDroste) {
    let p = document.createElement("p")
    const ign = isDroste ? '' : `<a class="button" href="javascript:setIgnoredStitches()">assign to ignored</a>`
    const note = !isDroste ? '' : 'Out of date diagrams/stitches are highlighted.'
    p.innerHTML += `
            <span id="colorCode"></span>
            <input type="text" id="stitchDef" name="stitchDef" value="ct" onkeyup="setColorCode()" onclick="return false" onsubmit="return false">
            flip: 
            <a class="button" href="javascript:flip2d()">&harr;</a>
            <a class="button" href="javascript:flip2p()">&varr;</a>
            <a class="button" href="javascript:flip2q()">both</a>`
    let element = document.querySelector("#gallery");
    element.parentNode.insertBefore(p, element.nextSibling)
    setStitch("ct")
}

function flip2d() {
    var n = d3.select('#stitchDef').node()
    n.value = n.value.toLowerCase().replace(/l/g, "R").replace(/r/g, "L").toLowerCase()
    setColorCode()
    n.focus()
}

function flip2p() {
    var n = d3.select('#stitchDef').node()
    n.value = n.value.toLowerCase().split("").reverse().join("")
    setColorCode()
    n.focus()
}

function flip2q() {
    flip2d()
    flip2p()
}

function setColorCode() {
    let node = d3.select("#stitchDef").node();
    if (node)
        document.querySelector('#colorCode').innerHTML = `
        <svg width="20px" height="25px">
          <g transform="scale(2,2)">
            <g transform="translate(5,6)">
              ${PairSvg.shapes(node.value)}
            </g>
          </g>
        </svg>`
}

function setStitch(stitch) {
    const n = document.querySelector("#stitchDef")
    n.value = stitch
    n.focus()
    setColorCode();
}
