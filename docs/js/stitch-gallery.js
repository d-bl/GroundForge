function loadStitchExamples() {
    var stitches = [
        ["ct", "", ""],
        ["ctct", "", ""],
        ["ctctc", "", ""],
        ["crclct", "clcrct", ""],
        ["clcrclc", "crclcrc", ""],
        ["ctclctc", "ctcrctc", ""],
        ["ctclcrctc", "ctcrclctc", ""],
        ["ctcttctc", "", ""],
        ["cllcrrcllcrrc", "crrcllcrrcllc", ""],
        ["tctctllctctr", "ltctctrrctct", "winkie pin"],
]
    for (let alts of stitches) {
        document.querySelector("#gallery").innerHTML += `
            <figure>
                <img src="/GroundForge/images/stitches/${alts[0]}.png"
                     alt="${alts[0]}"
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
}