const drosteURL = "/GroundForge/droste?source=mix4snow&";
const stitchesURL = "/GroundForge/stitches?source=mix4snow&";

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
            replaceTile(12, 0);
            replaceStitches(["g16", "h16", "g1", "h1", "g2", "h2", "g3", "h3", "g4", "h4"]);
            break;
        case "hexaNW":
        case "hexaSE":
            replaceTile(0, 2);
            replaceStitches(["i4", "j4", "i5", "j5", "i6", "j6", "i7", "j7", "i8", "j8"]);
            break;
        case "hexaN":
        case "hexaS":
            replaceTile(4, 0);
            replaceStitches(["g8", "h8", "g9", "h9", "g10", "h10", "g11", "h11", "g12", "h12"]);
            break;
        case "hexaNE":
        case "hexaSW":
            replaceTile(8, 2);
            replaceStitches(["i12", "j12", "i13", "j13", "i14", "j14", "i15", "j15", "i16", "j16"]);
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
        d3.select('#pairs').selectAll(".node").attr("onclick",null)
        d3.select('#pairs').selectAll(".node").on("click",clickedPairStitch)

        paintThreadIntersections(/[gh]([1-4]|(16))[0-9]$/, '#0571b0ff');
        paintThreadIntersections(/[ij][4-8][0-9]$/, '#92c5deff');
        paintThreadIntersections(/[gh]([89]|(1[0-2]))[0-9]$/, '#ca0020ff');
        paintThreadIntersections(/[ij](1[2-6])[0-9]$/, '#f4a582ff');
    }, 0);
}

function recipe(stitches,startsLeft) {
    // used as link  in a markdown containing the form
    // javascript:recipe("ctc,...",true/false)
    document.getElementById('replacement').value = stitches
    document.getElementById(startsLeft?'left':'right').checked = true
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
            const [key, value] = pair.split(/=(.+)/);
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
        q = "tile=48x-,xrx-,xrx-,xr83,-x48,-xxr,-xxr,31xr,17-x,rx-x,rx-x,rx31,x-17,x-rx,x-rx,83rx" +
            "&f8=llttcrr&footside=-----x,-----x,-----x,-----x,-----4,-----r,-----r,-----r&" +
            "&u8=rrttcll&headside=x,x,x,8,r,r,r,r" +
            "&shiftColsSW=0&shiftRowsSW=16&shiftColsSE=4&shiftRowsSE=8&patchWidth=14&patchHeight=35" +
            "&i1=rc&h1=ct&g1=clcrcl&h2=crclcr&n5=llctt&i5=ctc&g5=ct&j9=lc&h9=ctc&j13=ctc&g13=lc&g16=tc&h16=rclcrc&h4=ct&h8=cr&g8=ctc&g9=lc&g12=lc&i12=ctc&j12=cl&i13=rc&j16=lc&j4=ctc&i4=cr&j5=lc&i8=rc&j8=lc&i16=rc" +
            "&droste2=g160=g161=h160=ttctc,g15=h41=h42=ctctt" +
            ",,g80=g81=j120=j121=lllctc,h80=h81=rrrctc,g120=g121=ctclll,h92=h93=i160=i161=ctcrrr" +
            ",j121=lllctcl,j133=ctcl,i53=i41=ctcr" +
            ",,f80=p81=ctcttttttttttttlllctc,f82=rrtctctrr"+
            ",,u82=ctc,u81=tttttttctctttttt,u83=tttttctc,u80=tttctc";
    }
    document.getElementById('toDiagrams').setAttribute("href", drosteURL + q);
    document.getElementById('toPrintFriendly').setAttribute("href", stitchesURL + q);
    diagrams(q);
})
