function loadStitchExamples() {
    var stitches = [
        ["cllcrrcllcrrc", "crrcllcrrcllc", ""],
        ["ctctctc", "", ""],
        ["ct", "", ""],
        ["ctct", "", ""],
        ["crcllrrrc", "crcttrc", ""],
        ["clcrclc", "crclcrc", ""],
        ["ctctc", "", ""],
        ["ctclctc", "ctcrctc", ""],
        ["crclct", "clcrct", ""],
        ["ctclcrctc", "ctcrclctc", ""],
        ["ctcttctc", "", ""],
        ["tctctllctctr", "ltctctrrctct", "winkie pin"],
    ]
    let list1 = d3.selectAll("#pair title")
    for (let alts of stitches) {
        document.querySelector("#gallery").innerHTML += `
            <figure>
                <svg width="20" height="54">
                  <g transform="scale(2,2)">
                    <g transform="translate(5,6)">
                      ${PairSvg.shapes(alts[0])}
                    </g>
                    <g transform="translate(5,21)">
                      ${PairSvg.shapes(alts[1])}
                    </g>
                  </g>
                </svg>
                <img src="/GroundForge/images/stitches/${alts[0]}.png"
                     title="${alts[0]}&#013${alts[1]}&#013${alts[2]}">
                <figcaption>
                    <a href="javaScript:setStitch('${alts[0]}')">${alts[0]}</a><br>
                    <a href="javaScript:setStitch('${alts[1]}')">${alts[1]}</a>&nbsp;
                </figcaption>
            </figure>`
    }
}
function setStitch(stitch) {
    n = document.querySelector("#stitchDef")
    n.value = stitch
    n.focus()
    setColorCode() // only exists in stitches.js !!!
}