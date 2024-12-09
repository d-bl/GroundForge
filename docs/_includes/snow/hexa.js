const drosteURL = "https://d-bl.github.io/GroundForge/droste?";
const stitchesURL = "https://d-bl.github.io/GroundForge/stitches?";

function setHref(hexaId, stitchesId, drosteHrefId, printHrefId, startId) {
    const hrefNode = document.getElementById(drosteHrefId);
    const stitchArray = document.getElementById(stitchesId).value.toLowerCase().split(",");
    const startsLeft = document.getElementById(startId).value === "left";
    const nrOfStitches = stitchArray.length;
    if (nrOfStitches !== 4) {
        alert("Sorry, replacement only works for 4 stitches in the text field " + stitchArray);
        return;
    }
    const q = getQueryParams(hrefNode.getAttribute("href"));

    function replaceTile(row, col) {
        // return; // TODO make elements of plaits.svg fit together, remove arrows from hexagons
        const matrix = q.get("tile").split(",").map(str => str.split(''));
        let endsLeft = matrix[(row + 3) % 4] === '1'; // the one above
        if (startsLeft) {
            matrix[row][col] = (endsLeft ? '8' : '6');
            matrix[row][col + 1] = '3';
            matrix[(row + 1) % 4][col] = '4';
            matrix[(row + 1) % 4][col + 1] = '8';
            // TODO matrix[(row + 2) % 4][col + (1)] = '8';
        } else {
            matrix[row][col] = '3';
            matrix[row][col + 1] = (endsLeft ? '2' : '4');
            matrix[(row + 1) % 4][col] = '1';
            matrix[(row + 1) % 4][col + 1] = '7';
            // TODO matrix[(row + 2) % 4][col + (1)] = '4';
        }
        q.set("tile", matrix.map(row => row.join('')).join(','));
    }

    function replaceStitches(stitchIds) {
        const minLength = Math.min(stitchIds.length, stitchArray.length);
        var i = 0;
        for (; i < minLength; i++) {
            q.set(stitchIds[i], stitchArray[i]);
        }
    }

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
            replaceStitches(["c1", "b1", "c2", "b5"]);
            // replaceTile(0, 0);
            break;
        case "hexaNW":
        case "hexaSE":
            replaceStitches(["d5", "e5", "d6", "e9"]);
            // replaceTile(1, 2);
            break;
        case "hexaN":
        case "hexaS":
            replaceStitches(["b9", "c9", "c10", "b13"]);
            // replaceTile(2, 0);
            break;
        case "hexaNE":
        case "hexaSW":
            replaceStitches(["e13", "d13", "e14", "d1"]);
            // replaceTile(3, 2);
            break;
    }
    let newQ = Array
        .from(q.entries())
        .map(([key, value]) => `${encodeURIComponent(key)}=${value.replace(/%2C/g, ',').replace(/%2D/g, '-')}`)
        .join('&');
    hrefNode.setAttribute('href', drosteURL + newQ);
    let element = document.getElementById(printHrefId);
    element
        .setAttribute("href", stitchesURL + newQ)
    diagrams(newQ);
}

function diagrams(q) {
    console.log("--------" + q)
    const config = TilesConfig(q);
    showGraph('#threads', ThreadDiagram.create(NewPairDiagram.create(config)))
    d3.select('#threads g').attr("transform", "scale(0.5,0.5)")

    var cfg = TilesConfig(q)
    var zoom = 1.9
    var itemMatrix = cfg.getItemMatrix

    // dimensions for an A4
    var width = 744
    var height = 1052

    var svg = PairSvg.render(itemMatrix, width, height, zoom)
    d3.select('#pairs').html(svg)

    function paintThreadIntersections(titleRegex, fillColor) {
        d3.selectAll('path')
            .filter(function () {
                let titleEl = d3.select(this).select('title');
                if (!titleEl.node()) {
                    return false;
                }
                const title = titleEl.text();
                return title && titleRegex.test(title);
            })
            .style('fill', fillColor)
            .style('opacity', 0.5);
    }

    setTimeout(() => {
        nudgePairs('#pairs', cfg.totalCols * 6, cfg.totalRows * 6)
        d3.selectAll(".bobbin").remove();

        paintThreadIntersections(/[bc][1-5][0-9]$/, '#0571b0ff');
        paintThreadIntersections(/[de][5-9][0-9]$/, '#92c5deff');
        paintThreadIntersections(/[bc](9|(1[0-3]))[0-9]$/, '#ca0020ff');
        paintThreadIntersections(/[de](1|(1[3-6]))[0-9]$/, '#f4a582ff');
    }, 0);
}

function getQueryParams(url) {
    const queryParams = new Map();
    const queryString = url.split(/[?#]/)[1];
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
    var q = document.URL.split(/[?#]/)[1];
    if (!q) {
        q = "patchWidth=12&patchHeight=35&footside=r,x,x,x,x,4,r,r&tile=31rx,17x-,rxx-,rxx-,rx83,-w48,-xxr,-xxr,31xr,17-x,rx-x,rx-x,rx31,-w17,-xrx,-xrx,&headside=8,r,r,r,r,x,x,x&shiftColsSW=0&shiftRowsSW=16&shiftColsSE=4&shiftRowsSE=8&d1=rc&c1=tc&b1=lcrclc&a1=rrctt&c2=crclcr&n5=llctt&d5=cr&b5=ct&e9=lc&c9=cr&e13=cl&b13=lc";
    }
    document.getElementById('toDiagrams').setAttribute("href", drosteURL + q);
    document.getElementById('toPrintFriendly').setAttribute("href", stitchesURL + q);
    diagrams(q);
})
