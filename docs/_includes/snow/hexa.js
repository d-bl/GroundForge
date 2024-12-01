const stitchesURL = "https://d-bl.github.io/GroundForge/stitches?";

function setHref(hexaId, stitchesId, hrefId) {
    const hrefNode = document.getElementById(hrefId);
    const stitchArray = document.getElementById(stitchesId).value.toLowerCase().split(",");
    const nrOfStitches = stitchArray.length;
    if (nrOfStitches !== 4) {
        alert("Sorry, replacement only works for 4 stitches in the text field "+stitchArray);
        return;
    }
    const q = getQueryParams(hrefNode.getAttribute("href"));
    switch (hexaId) {
        // S and Z represent the working orders of the stitches
        //
        // b1,c1
        //   S               center
        // b2,c2   d2,e2
        //           Z                  SE/NW
        // b3,c3   d3,e3
        //   S               N/S
        // b4,c4   d4,e4
        //           S                  NE/SW
        //         d1,e1
        case "hexaCenter":
            q.set("c1", stitchArray[0]);
            q.set("b1", stitchArray[1]);
            q.set("c2", stitchArray[2]);
            q.set("b2", stitchArray[3]);
            break;
        case "hexaNW":
        case "hexaSE":
            q.set("d2", stitchArray[0]);
            q.set("e2", stitchArray[1]);
            q.set("d3", stitchArray[2]);
            q.set("e3", stitchArray[3]);
            break;
        case "hexaN":
        case "hexaS":
            q.set("c3", stitchArray[0]);
            q.set("b3", stitchArray[1]);
            q.set("c4", stitchArray[2]);
            q.set("b4", stitchArray[3]);
            break;
        case "hexaNE":
        case "hexaSW":
            q.set("e4", stitchArray[0]);
            q.set("d4", stitchArray[1]);
            q.set("e1", stitchArray[2]);
            q.set("d1", stitchArray[3]);
            break;
    }
    let newQ = Array
        .from(q.entries())
        .map(([key, value]) => `${encodeURIComponent(key)}=${value.replace(/%2C/g, ',').replace(/%2D/g, '-')}`)
        .join('&');
    console.log(newQ);
    hrefNode.setAttribute('href', stitchesURL + newQ);
}

function getQueryParams(url) {
    const queryParams = new Map();
    const queryString = url.split('?')[1];
    if (queryString) {
        const pairs = queryString.split('&');
        pairs.forEach(pair => {
            const [key, value] = pair.split('=');
            queryParams.set(decodeURIComponent(key), decodeURIComponent(value));
        });
    }
    return queryParams;
}

function flip_b2d(id) {
    const n = document.getElementById(id);
    n.value = n.value.toLowerCase().replace(/l/g, "R").replace(/r/g, "L").toLowerCase();
    n.focus();
}

function flip_b2p(id) {
    const n = document.getElementById(id);
    n.value = n.value.toLowerCase().split("").reverse().join("");
    n.focus();
}

document.addEventListener('DOMContentLoaded', (event) => {
    const q = document.URL.split('?')[1];
    if (q) {
        document.getElementById('toDiagrams').setAttribute("href", stitchesURL + q);
    }
})
