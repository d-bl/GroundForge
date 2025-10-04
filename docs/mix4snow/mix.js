const GF_snow_mixer = {

    drosteURL: "/GroundForge/droste?source=mix4snow&",
    stitchesURL: "/GroundForge/stitches?source=mix4snow&",
    q4: "tile=48x-,xrx-,xrx-,xr83,-x48,-xxr,-xxr,31xr,17-x,rx-x,rx-x,rx31,x-17,x-rx,x-rx,83rx" +
        // foot side and head side are repeated to support two different snowflakes along the edge
        "&f8=crc&f16=crc&footside=-----x,-----x,-----x,-----x,-----4,-----r,-----r,-----r,-----x,-----x,-----x,-----x,-----4,-----r,-----r,-----r&" +
        "&u8=clc&u16=clc&headside=x,x,x,8,r,r,r,r,x,x,x,8,r,r,r,r" +
        "&shiftColsSW=0&shiftRowsSW=16&shiftColsSE=4&shiftRowsSE=8&patchWidth=14&patchHeight=35" +
        "&i1=rc&h1=ct&g1=clcrcl&h2=crclcr&n5=llctt&i5=ctc&g5=ct&j9=lc&h9=ctc&j13=ctc&g13=lc&g16=tc&h16=rclcrc&h4=ct&h8=cr&g8=ctc&g9=lc&g12=lc&i12=ctc&j12=cl&i13=rc&j16=lc&j4=ctc&i4=cr&j5=lc&i8=rc&j8=lc&i16=rc" +
        "&droste2=g160=g161=h160=ttctc,g15=h41=h42=ctctt" +
        ",,g80=j120=j121=lllctc,g81=lllctcl,h80=rrrctc,h81=rrrctcr,g120=g121=ctclll,h92=h93=i160=i161=ctcrrr" +
        ",j121=lllctcl,j133=ctcl,i53=i41=ctcr" +
        ",,f80=f82=f160=f162=ctcllllllll" +
        ",f81=f161=lllllctclllll" +
        ",u80=u82=u160=u162=ctcrrrrrrrr" +
        ",u81=u161=rrrrrctcrrrrr",
    q1: "tile=48,xr,xr,xr,y-,x-,x-,83" +
        "&f8=crc&f16=crc&footside=-----x,-----x,-----x,-----x,-----4,-----r,-----r,-----r&" +
        "&u8=clc&u16=clc&headside=x,x,x,8,r,r,r,r" +
        "&shiftColsSW=0&shiftRowsSW=8&shiftColsSE=2&shiftRowsSE=4&patchWidth=14&patchHeight=35" +
        "&h1=ct&g1=clcrcl&h2=crclcr&g5=ct&h9=ct&h4=ct&h8=rclcrc&g8=tc&j4=rclcrc&i4=tc&j5=ct&i8=rc&j8=ct&i16=rc" +
        "&droste2=g160=g161=h160=ttctc,g15=h41=h42=ctctt" +
        ",,g80=j120=lllctc,g81=lllctcl,h80=rrrctc,h81=rrrctcr,g120=g121=ctclll,h92=h93=ctcrrr" +
        ",,f80=f82=f160=f162=ctcllllllll" +
        ",f81=f161=lllllctclllll" +
        ",u80=u82=u160=u162=ctcrrrrrrrr" +
        ",u81=u161=rrrrrctcrrrrr",

    getToDrosteElement() {
        return document.getElementById('toDiagrams')
        },
    getToStitchesElement() {
        return document.getElementById('toPrintFriendly')
    },
    getNrOfSnowflakes() {
        return document.getElementById('nrOfSnowFlakes').value;
    },
    getPairsStartLeft() {
        return document.getElementById("left").checked
    },
    getFormContainer() {
        return document.getElementById('fragmentDiv');
    },
    getRecipeStitches() {
        return document.getElementById('replacement').value
        .toLowerCase().replaceAll(/[^crlt,]/g, '')
    },
    recipe(stitches, startsLeft) {
        // used as link like javascript:recipe("ctc,...",true/false)
        document.getElementById('replacement').value = stitches
        document.getElementById(startsLeft ? 'left' : 'right').checked = true
    },
    flipRadio() {
        const startsLeft = document.getElementById("left").checked;
        document.getElementById(startsLeft ? 'right' : 'left').checked = true
    },

    flip_b2d(id) {
        const n = document.getElementById(id);
        n.value = n.value.toLowerCase().replace(/l/g, "R").replace(/r/g, "L").toLowerCase();
        this.flipRadio();
        n.focus();
    },

    flip_b2p(id) {
        const n = document.getElementById(id);
        n.value = n.value.toLowerCase().split("").reverse().join("");
        this.flipRadio();
        n.focus();
    },

    updatePattern(q) {
        if (q.includes('shiftRowsSW=16')) {
            document.getElementById('apply_single_recipe').style.display = 'none';
            document.getElementById('hexas').style.display = 'inline-block';
        } else {
            document.getElementById('apply_single_recipe').style.display = 'inline-block';
            document.getElementById('hexas').style.display = 'none';
        }
        document.getElementById('toDiagrams').setAttribute("href", this.drosteURL + q);
        document.getElementById('toPrintFriendly').setAttribute("href", this.stitchesURL + q);
        GF_snow_mixer.diagrams(GF_snow_mixer.twistFootsides(q));
    },

    nrOfSnowFlakesChanged() {
        switch (GF_snow_mixer.getNrOfSnowflakes()) {
            case "1":
                this.updatePattern(this.q1);
                break;
            case "4":
                this.updatePattern(this.q4);
                break;
        }
    },


    setHref(hexaId) {
        function parseMatrix(str) {
            return str.split(',').map(row => row.split(''));
        }

        function matrixToString(matrix) {
            return matrix.map(row => row.join('')).join(',');
        }

        const hrefNode = this.getToDrosteElement();
        const stitchArray = this.getRecipeStitches().split(/[,.]/);
        const nrOfStitches = stitchArray.length;
        const q = this.getQueryParams(hrefNode.getAttribute("href"));
        const startsLeft = this.getPairsStartLeft();

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
            replacement[i] = replacement[i - 1];
        }

        function replaceStitches(stitchIds) {
            const ids = [];
            if (startsLeft) {
                for (let i = 0; i < stitchIds.length; i += 1) {
                    ids[i] = stitchIds[i];
                }
            } else {
                for (let i = 0; i < stitchIds.length; i += 2) {
                    ids[i] = stitchIds[i + 1];
                    ids[i + 1] = stitchIds[i];
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
            const h = matrix.length;
            const w = matrix[0].length;
            // replace the sub-matrix
            for (let i = 0; i < 8; i++) {
                for (let j = 0; j < 2; j++) {
                    matrix[(row + i) % h][(col + j)] = replacement[i][j];
                }
            }

            function reconnect(row, col) {
                const slice1 = matrix[(row + 15) % 16].slice(col, col + 2);
                const slice2 = matrix[row % 16].slice(col, col + 2);
                const transition = matrixToString([slice1, slice2]);
                switch (transition) {
                    case "rx,-x":
                    case "17,-x":
                        matrix[row][col + 1] = "w";
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
                        matrix[row][col + 1] = "x";
                        break;
                }
            }

            if(hexaId !== "hexaOne") {
                reconnect(row, col);
                reconnect((row + 8) % 16, col);
            } else {
                if (GF_snow_mixer.getPairsStartLeft()) {
                    matrix[4][0] = "y";
                } else {
                    matrix[3][1] = "8";
                }
            }
            q.set("tile", matrixToString(matrix))
        }

        switch (hexaId) {
            case "hexaOne":
                replaceTile(4, 0);
                replaceStitches(["g8", "h8", "g1", "h1", "g2", "h2", "g3", "h3", "g4", "h4"]);
                break;
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
            .map(([key, value]) => !value ? '' : `${encodeURIComponent(key)}=${value.replace(/%2C/g, ',').replace(/%2D/g, '-')}`)
            .join('&');
        hrefNode.setAttribute('href', this.drosteURL + newQ);
        this.getToStitchesElement()
            .setAttribute("href", this.stitchesURL + newQ)
        this.diagrams(this.twistFootsides(newQ));
    },

     diagrams(q) {
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
            nudgePairs('#pairs')
            d3.select('#pairs').selectAll(".node").attr("onclick", null)
            d3.select('#pairs').selectAll(".node").on("click", this.clickedPairStitch)

            if (GF_snow_mixer.getNrOfSnowflakes() === "1") {
                // 8 is the first row, 1, the second, 4 the last and optionally 2 and 3 between
                const nrOfRows = Math.ceil(GF_snow_mixer.getRecipeStitches().split(',').length / 2);
                paintThreadIntersections(/[g][248]/, '#0571b0ff');
                paintThreadIntersections(/[h][248]/, '#ca0020ff');
                paintThreadIntersections(/[h][13]/, '#92c5deff');
                paintThreadIntersections(/[g][13]/, '#f4a582ff');
                if (nrOfRows === 2 || nrOfRows === 4) {
                    paintThreadIntersections(/h4/, '#92c5deff');
                    paintThreadIntersections(/g4/, '#f4a582ff');
                }
            } else {
                paintThreadIntersections(/[gh]([1-4]|(16))[0-9a-z]$/, '#0571b0ff');
                paintThreadIntersections(/[ij][4-8][0-9a-z]$/, '#92c5deff');
                paintThreadIntersections(/[gh]([89]|(1[0-2]))[0-9a-z]$/, '#ca0020ff');
                paintThreadIntersections(/[ij](1[2-6])[0-9a-z]$/, '#f4a582ff');
            }
        }, 0);
    },


     clickedPairStitch(event) {
        const selectedClass = d3.select(d3.event.currentTarget)
            .select('title').text().replace(/.* /g, '');
        d3.select('#threads').selectAll(".ct-" + selectedClass).style('opacity', "0")
    },

     getQueryParams(url) {
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
    },

    twistFootsides(q) {
        return q.replace("f8=crc&f16=crc", "f8=ttcrctt&f16=ttcrctt").replace("u8=clc&u16=clc", "u8=tclcttt&u16=tclcttt");
    },

    init () {
         fetch('fragment.html')
             .then(response => response.text())
             .then(html => {
                 this.getFormContainer().innerHTML = html;

                 var q = window.location.search.substring(1);
                 // the search string may have been set by webstorm
                 if (!q || !q.includes('tile')) {
                     q = this.q4;
                 }
                 this.updatePattern(q);

             })
             .catch(err => console.error('Failed to load fragment:', err));

     }

}