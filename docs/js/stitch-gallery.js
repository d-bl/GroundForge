const GF_stitches = {

    lastValidStitchValue: "ct", // the initial value by loadStitchForm
    imagesLocation: "/GroundForge/images/stitches",
    stitches: [
        "cllcrrcllcrrc",
        "ctctctc",
        "ct",
        "ctct",
        "clcrclc",
        "ctctc",
        "ctclctc",
        "crclct",
        "ctclcrctc",
        "ctcttctc",
        "crcllrrrc",
        "tctctllctctr",
    ],

    fixStitchValue(inputField) {
        const value = inputField.value.toLowerCase();
        inputField.value = value
        if (/^([-]|([tclr])*)$/.test(value)) {
            GF_stitches.lastValidStitchValue = value;
        } else {
            const pos1 = inputField.selectionStart - 1;
            const pos2 = inputField.selectionEnd - 1;
            inputField.value = GF_stitches.lastValidStitchValue;
            inputField.setSelectionRange(pos1, pos2);
            if (typeof window.AudioContext !== "undefined") {
                const ctx = new window.AudioContext();
                const o = ctx.createOscillator();
                o.type = "sine";
                o.frequency.value = 440;
                o.connect(ctx.destination);
                o.start();
                o.stop(ctx.currentTime + 0.05);
            }
        }
    },

    loadStitchExamples() {
        for (let stitch of GF_stitches.stitches) {
            document.querySelector("#gallery").innerHTML += `
            <figure>
                <svg width="20" height="54">
                  <g transform="scale(2,2)">
                    <g transform="translate(5,6)">
                      ${PairSvg.shapes(stitch)}
                    </g>
                  </g>
                </svg>
                <img src="${GF_stitches.imagesLocation}/${stitch}.svg"
                     title="${stitch}">
                <figcaption>
                     <a href="javaScript:GF_stitches.setStitch('${stitch}')">${stitch}</a>&nbsp;
                </figcaption>
            </figure>`
        }
    },

    paintStitchValue() {
        return d3.select("#stitchDef").node().value.toLowerCase().replace(/[^ctlrp-]/g, '')
    },

    load(isDroste) {
        this.loadStitchExamples();
        this.loadStitchForm(isDroste);
        this.setColorCode();
    },

    loadStitchForm() {
        let p = document.createElement("p")
        p.innerHTML += `
            <span id="colorCode"></span>
            <input type="text" value="ct"
                   id="stitchDef" name="stitchDef"
                   onkeyup="GF_stitches.setColorCode()"
                   oninput="GF_stitches.fixStitchValue(this)"
                   onclick="return false" onsubmit="return false"
            >
            Flip: 
            <button onclick="GF_stitches.flip2d()">↔</button>
            <button onclick="GF_stitches.flip2p()">↕</button>
            <button onclick="GF_stitches.flip2q()">both</button>
            Random stitch:
            <button onclick="GF_stitches.stgen()">generate</button>`
        let element = document.querySelector("#gallery");
        element.parentNode.insertBefore(p, element.nextSibling)
        this.setStitch("ct")
    },

    flip2d() {
        let n = d3.select('#stitchDef').node()
        let s = n.value.toLowerCase().replace(/l/g, "R").replace(/r/g, "L").toLowerCase()
        this.setStitch(s)
    },

    flip2p() {
        let n = d3.select('#stitchDef').node()
        let s = n.value.toLowerCase().split("").reverse().join("")
        this.setStitch(s)
    },

    flip2q() {
        this.flip2d()
        this.flip2p()
    },

    /* genStitch(maxCrosses, maxTwistsBetweenCrosses, maxTwistsBefore,  maxTwistsAfter)   */
    stgen() {
        let s = genStitch(4,1,1,1).toLowerCase()
        this.setStitch(s)
    },

    setColorCode() {
        let node = d3.select("#stitchDef").node();
        if (node)
            document.querySelector('#colorCode').innerHTML = `
        <svg width="20px" height="25px">
          <g transform="scale(2,2)">
            <g transform="translate(5,6)">
              ${PairSvg.shapes(node.value.toLowerCase())}
            </g>
          </g>
        </svg>`
    },

    setStitch(stitch) {
        const n = document.querySelector("#stitchDef")
        n.value = stitch
        this.lastValidStitchValue = stitch;
        this.setColorCode();
    }
}
