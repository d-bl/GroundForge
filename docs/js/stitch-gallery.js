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
    let list1 = d3.selectAll("#pair title")
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
function setStitch(stitch) {
    const n = document.querySelector("#stitchDef")
    n.value = stitch
    n.focus()
    setColorCode() // only exists in stitches.js !!!
}