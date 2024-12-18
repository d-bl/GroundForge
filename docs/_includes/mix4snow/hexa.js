const drosteURL = "https://d-bl.github.io/GroundForge/droste?source=mix4snow&";
const stitchesURL = "https://d-bl.github.io/GroundForge/stitches?=mix4snow&";

function setHref(hexaId, stitchesId, drosteHrefId, printHrefId, startId) {
    function parseMatrix(str) {
        return str.split(',').map(row => row.split(''));
    }
    function matrixToString(matrix) {
        return matrix.map(row => row.join('')).join(',');
    }
    const hrefNode = document.getElementById(drosteHrefId);
    const stitchArray = document.getElementById(stitchesId).value.toLowerCase().replaceAll(' ','').split(/[,.]/);
    const nrOfStitches = stitchArray.length;
    const q = getQueryParams(hrefNode.getAttribute("href"));
    const startsLeft = document.getElementById("left").checked;

    switch (stitchArray.length) {
        case 4:
        case 6:
        case 8:
        case 10:
            break;
        default:
            alert("Please enter 4, 6, 8 or 10 stitches.");
            return;
    }

    const replacement = parseMatrix(startsLeft ? "x-,x-,x-,83,48,xr,xr,xr" : "-x,-x,-x,31,17,rx,rx,rx");
    // replace xr/rx rows with previous row as far as needed
    const max = 3 + Math.ceil(nrOfStitches / 2)
    for (let i = 5; i < max; i++) {
        replacement[i] = replacement[i-1];
    }
    console.log("replacement: ", nrOfStitches, max, matrixToString(replacement));

    function replaceStitches(stitchIds) {
        const ids = [];
        if (startsLeft) {
            for (let i = 0; i < stitchIds.length ; i += 1) {
                ids[i] = stitchIds[i];
            }
        } else {
            for (let i = 0; i < stitchIds.length ; i += 2) {
                ids[i] = stitchIds[i + 1];
                ids[i+1] = stitchIds[i];
            }
        }
        if ((stitchArray.length % 2) === 1)
            stitchArray[stitchArray.length] = '-';
        const minLength = Math.min(ids.length, stitchArray.length);
        for (let i = 0; i < minLength; i++) {
            q.set(ids[i], stitchArray[i]);
        }
        q.set(ids[9], stitchArray[stitchArray.length - 1]);
    }

    function replaceTile(row, col) {
        const matrix = parseMatrix(q.get("tile"));
        // replace the sub-matrix
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 2; j++) {
                matrix[(row + i) % 16][(col + j)] = replacement[i][j];
            }
        }
        function reconnect (row, col) {
            const slice1 = matrix[(row + 15) % 16].slice(col, col + 2);
            const slice2 = matrix[row % 16].slice(col, col + 2);
            const transition = matrixToString([slice1, slice2]);
            console.log("reconnect: ", row, col, transition, startsLeft, hexaId, matrixToString(replacement));
            switch (transition) {
                case "rx,-x":
                case "17,-x":
                    matrix[row][col+1] = "w";
                    break;
                case "xr,x-":
                case "48,x-":
                    matrix[row][col] = "y";
                    break;
                case "rx,y-":
                case "17,y-":
                    matrix[row][col] = "x";
                    break;
                case "xr,-w":
                case "48,-w":
                    matrix[row][col+1] = "x";
                    break;
            }
        }
        reconnect(row, col);
        reconnect((row+8)%16, col);
        q.set("tile", matrixToString(matrix))
    }

    switch (hexaId) {
        case "hexaCenter":
            replaceTile(13, 0);
            replaceStitches(["b1", "c1", "b2", "c2", "b3", "c3", "b4", "c4", "b5", "c5"]);
            break;
        case "hexaNW":
        case "hexaSE":
            replaceTile(1, 2);
            replaceStitches(["d5", "e5", "d6", "e6", "d7", "e7", "d8", "e8", "d9", "e9"]);
            break;
        case "hexaN":
        case "hexaS":
            replaceTile(5, 0);
            replaceStitches(["b9", "c9", "b10", "c10", "b11", "c11", "b12", "c12", "b13", "c13"]);
            break;
        case "hexaNE":
        case "hexaSW":
            replaceTile(9, 2);
            replaceStitches(["d13", "e13", "d14", "e14", "d15", "e15", "d16", "e16", "d1", "e1"]);
            break;
    }
    let newQ = Array
        .from(q.entries())
        .map(([key, value]) => !value?'':`${encodeURIComponent(key)}=${value.replace(/%2C/g, ',').replace(/%2D/g, '-')}`)
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
        d3.select('#pairs').selectAll(".node").attr("onclick",null)
        d3.select('#pairs').selectAll(".node").on("click",clickedPairStitch)

        paintThreadIntersections(/[bc][1-5][0-9]$/, '#0571b0ff');
        paintThreadIntersections(/[de][5-9][0-9]$/, '#92c5deff');
        paintThreadIntersections(/[bc](9|(1[0-3]))[0-9]$/, '#ca0020ff');
        paintThreadIntersections(/[de](1|(1[3-6]))[0-9]$/, '#f4a582ff');
    }, 0);
}


function clickedPairStitch(event) {
    const selectedClass = d3.select(d3.event.currentTarget)
                            .select('title').text().replace(/.* /g, '');
    console.log("selectedClass: ", selectedClass)
    d3.select('#threads').selectAll(".ct-" + selectedClass).style('opacity',"0")
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
