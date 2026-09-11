/**
 * GroundForge page generator.
 *
 * The surrounding page should take care of the house style like headers footers and menu's with help pages..
 *
 * Requires:
 * - d3.v4.min.js for nudgeDiagram
 * - GroundForge-opt.js to render diagrams and color code
 * - nudgeDiagram of nudgePairs.js
 * - panel.js
 * - GF_Random of stitch-gallery.js
 * @namespace
 */
const GF_hybrid = {
    content_home: '.',
    dirtyBackGround: "#f0f0f0",
    getRandomStitch() {
        return GF_Random.genRandomStitch(3, 2, 1, 1);
    },
    recipes: {
        snow4: [
            // screenshots taken at 50% zoom level
            // ndb: patterns from "naar de bron" by Nora Andries
            ['ndb-9Z.png', 'RCLRCR', 'X0=CL,X1=LLCLCR,X2=LLCLCRCLCLL,X3=CRCLCRCL,X4=CLCRLL,X5=C', 'X00=X70=C,X80=X81=X90=X91=LRT '],
            ['ndb-10N.png', 'CLCLRCRCLR', 'X0=X7=C,X1=RCLCR,X2=CRC,X3=RCTC,X4=CTCL,X5=CLCR,X6=CRCL,X8=X9=TT', 'X00=,X70=C,X80=X81=X90=X91=TT '],
            ['spider-1-ring.png', 'CLRCLR', 'X1=X2=CTCTC,X4=X5=TT'],
            ['spider-2-rings.png', 'CLRCLR', 'X1=CTCRCTC,X2=CTCLCTC,X4=X5=TT'],
            ['spider-3-rings.png', 'CLRCCLRCLR', 'X3=CTCCTC,X8=X9=TT', 'X33=CTCCTC,X80=X81=X90=X91=TT']
        ],
        snow3: [
            ['123-a.png', 'RCRCRC', 'CRC,CRCLCTC,CTCRC,RCL,C,C'],
            ['123-b.png', 'LCLCLC', 'RCL,CTC,CRCLLC,CRRCLCR,CTC,CL'],
            ['132-a.png', 'CRCRC', 'CTC,CTC,CTC,CTC,CTC'],
            ['312-a.png', 'LCLC', 'TCTC,RCTCL,CTCL,CTCT'],
            ['321-a.png', 'LCLC', 'TC,RCLCRC,CLCRCL,CT'],
            ['321-b.png', 'RCRC', 'TCR,LCTC,CTCR,LCT'],
            ['321-c.png', 'RCRC', 'TCL,LCTC,CTCR,RCT'],
            ['321-d.png', 'RCRC', 'T,LCTC,CTCR,CTCT'],
            ['126453-a.png', 'CLCLC', 'C,CTCTC,CTCTC,CTCTC,C'],
            ['153426-a.png', 'LCLCLC', 'T,RC,CTC,RCLCR,CTCL,CT'],
            ['154326-a.png', 'LCLC', 'T,RCTC,CTCTCL,CTCT'],
            ['156423-a.png', 'CRCRC', 'CR,CRCL,CLCRCLCR,RCRCL,C'],
            ['234561-a.png', 'LCLCLC', 'CR,CRCL,CLCR,CRCL,CLCR,C'],
            ['263451-a.png', 'CRCRC', 'CR,CRCL,CLCR,CRCL,CL'],
            ['321546-a.png', 'CLCLC', 'CL,CTCL,CRCRCR,RCR,C'],
            ['321654-a.png', 'CLCLCLC', 'LC,CRC,CLCRC,CLCR,C,CRC,CL'],
            ['321654-b.png', 'CRCRC', 'CR,CTCR,CLCLC,LCL,C'],
            ['354612-a.png', 'RCRCRC', 'CTCT,CT,CT,CT,CL,CTC'],
            ['426153-a.png', 'RCRC', 'LC,CRCLCLC,CRCRCLC,CR'],
            ['426153-b.png', 'RCRCRC', 'CR,CTCL,CTCR,CTCL,CTC,C'],
            ['456123-a.png', 'RCRC', 'R,LRC,CTCR,LCT'],
            ['456123-b.png', 'RCRCRCRC', 'C,CTC,RCLC,CTC,RC,RCL,CTC,C'],
            ['462513-a.png', 'LCLC', 'RC,CLCRC,CLCTC,RCL'],
            ['564312-a.png', 'RCRC', 'LCRC,CLCRC,CLCRC,CLCR'],
            ['563412-a.png', 'CRCRC', 'C,CTCTC,CLCR,RCTC,C'],
            ['623451-a.png', 'LCLCLCLC', 'R,C,CRC,CTC,LCRCL,CTC,CRC,CL'],
            ['623541-a.png', 'CLCLC', 'CTC,CT,CRC,CTC,CTC'],
            ['623541-b.png', 'CRCRC', 'CL,CTCTCR,CT,CTC,C']
        ],
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
        createSnowGallery(recipes, containerId, imgPath) {
            // TODO make imgPath member of object
            const container = document.getElementById(containerId);
            for (let [img, basicStitch, droste1, droste2] of recipes) {
                container.insertAdjacentHTML('beforeend', `
                            <button type="button"
                                    class="recipe-btn">
                              <img src="${GF_hybrid.content_home}/${imgPath}/${img}"
                                   onclick="GF_hybrid.tweak.setRecipe('${basicStitch}','${droste1}','${droste2 ? droste2 : ''}')"
                                   alt="${basicStitch} ; ${droste1}${droste2 ? ' ; ' + droste2 : ''}">
                            </button>
                        `);
            }
        },
        createStitchGallery(containerId) {
            const element = document.querySelector("#" + containerId);
            // random
            element.innerHTML += `
              <button type="button" class="recipe-btn"
                      onclick="GF_hybrid.tweak.setRecipe(GF_hybrid.getRandomStitch())">
                <img src="${GF_hybrid.content_home}/images/stitches/random.svg" title="random stitch">
                <br>random
              </button>`;
            // set of predefined stitches
            for (let stitch of GF_hybrid.recipes.stitches) {
                element.innerHTML += `
            <button type="button" class="recipe-btn" onclick="GF_hybrid.tweak.setRecipe('${stitch}')">
                <svg width="20" height="24">
                  <g transform="scale(2,2)">
                    <g transform="translate(5,6)">
                      ${PairSvg.shapes(stitch)}
                    </g>
                  </g>
                </svg>
                <img src="${GF_hybrid.content_home}/images/stitches/${stitch}.svg"
                     title="${stitch}">
                <br>${stitch}
            </button>`
            }
        },
    },
    tweak: {
        getHtmlString() { return `
            <p>
            ${this.basicStitch.getHtmlString()} <br>
            ${this.drosteOnBasicStitch.getHtmlString()}
            </p>
            ${this.flip.getHtmlString()}
        `;
        },
        basicStitch: {
            id: 'basicStitchInput',
            lastValid: '', // TODO make it a data attribute
            getHtmlString() {
                const other = `document.getElementById('${GF_hybrid.tweak.drosteOnBasicStitch.id}')`;
                return `
            <label>Basic stitch:
                <span id="colorCode"></span>
                <input type="text" id="${this.id}"
                        value="${GF_hybrid.tweak.basicStitch.lastValid}" placeholder="empty=random; type ? for more info"
                        oninput="GF_hybrid.tweak.basicStitch.fixInput(this, document.getElementById('${GF_hybrid.tweak.drosteOnBasicStitch.id}'))"
                />
             </label>`;
            },
            setColorCode() {
                document.querySelector('#colorCode').innerHTML = `
                    <svg width="20px" height="25px">
                      <g transform="scale(2,2)">
                        <g transform="translate(5,6)">
                          ${PairSvg.shapes(this.lastValid.toUpperCase())}
                        </g>
                      </g>
                    </svg>`
            },
            fixInput(basicStitchEl, drosteOnBasicEl) {
                let value = basicStitchEl.value.toLowerCase().trim();
                const hasDroste = drosteOnBasicEl && drosteOnBasicEl.value.trim() !== '';
                const regexp = hasDroste ? /^[tclr]*$/ : /^(-|([tclr])*)$/;
                if (!regexp.test(value)) {
                    basicStitchEl.value = this.lastValid;
                    const pos1 = Math.max(0, drosteOnBasicEl.selectionStart - 1);
                    const pos2 = Math.max(0, drosteOnBasicEl.selectionEnd - 1);
                    basicStitchEl.setSelectionRange(pos1, pos2);
                    if (document.getElementById('pairStep').value === '0') {
                        GF_hybrid.toast.show("Characters for Basic stitch: CTLR, or just '-' to ignore a stitch.");
                    } else {
                        GF_hybrid.toast.show("Characters for Basic stitch: CTLR");
                    }
                    return;
                }
                if (hasDroste) {
                    value = value.replace(/[tT]/g, 'LR');
                }
                basicStitchEl.value = value.toUpperCase();
                this.lastValid = value;
                this.setColorCode();
            },
        },
        drosteOnBasicStitch: {
            id: 'drosteStitches',
            lastValid: '',  // TODO make it a data attribute
            getHtmlString() {
                return `
            <label>Droste applied to basic stitch:
                <input type="text" id="${this.id}"
                        value="${this.lastValid}" placeholder="Type ? for info"
                        oninput="GF_hybrid.tweak.drosteOnBasicStitch.fixInput(document.getElementById('${GF_hybrid.tweak.basicStitch.id}'), this)"
                />
            </label>`
            },
            msg: `
                "Droste applied to basic stitch" needs either numbered stitches,
                 or as many stitches as characters in "Basic stitch".
                 Separators: ";.," 
                 Numbered stitch: "X12=CTCT".
                 Default stitch: "CTC".
            `,
            fixInput(basicStitchEl, drosteOnBasicEl) {
                function isValid(str) {
                    if(str === '') return true
                    const validChars = /[^x0-9=ctlr,.;]/i
                    const repeatedSeparator = /[,.;][,.;]/;
                    const groupRegex = /^(x(([0-9]+)=?)?)?[ctlr]*$/i;
                    if (validChars.test(str)) return false;
                    if (repeatedSeparator.test(str)) return false;
                    const stitches = str.split(/[,.;]/);
                    if (stitches.length > basicStitchEl.value.length) return false;
                    return stitches.every(g => groupRegex.test(g));
                }
                const value = drosteOnBasicEl.value.trim().toUpperCase();
                if (isValid(value)) {
                    this.lastValid = value;
                    drosteOnBasicEl.value = value;
                } else {
                    drosteOnBasicEl.value = this.lastValid;
                    const pos1 = Math.max(0, drosteOnBasicEl.selectionStart - 1);
                    const pos2 = Math.max(0, drosteOnBasicEl.selectionEnd - 1);
                    drosteOnBasicEl.setSelectionRange(pos1, pos2);
                    GF_hybrid.toast.show(this.msg);
                }
            },
        },
        flip: {
            getHtmlString() { return `
                <p>Flip:
                <button onclick="GF_hybrid.tweak.flip.apply('b2d')">&harr;</button>
                <button onclick="GF_hybrid.tweak.flip.apply('b2p')">&varr;</button>
                <button onclick="GF_hybrid.tweak.flip.apply('b2d');GF_hybrid.tweak.flip.apply('b2p')">both</button>
                </p>
                `;
            },
            apply(direction) {
                function flip2(value) {
                    switch (direction) {
                        case 'b2d': return value.toLowerCase()
                            .replace(/l/g, "R")
                            .replace(/r/g, "L");
                        case 'b2p': return value
                            .split("").reverse().join("");
                    }
                }
                const basicEl = document.getElementById(GF_hybrid.tweak.basicStitch.id);
                const drosteEl = document.getElementById(GF_hybrid.tweak.drosteOnBasicStitch.id);
                const basicValue = basicEl.value.toLowerCase()
                    .replaceAll(/[^crlt]/g, '')
                if (drosteEl && drosteEl.value.trim()  !== '') {
                    if (drosteEl.value.includes('=')) {
                        const tLessBasicValue = basicValue.replace(/[t]/g, 'lr');
                        const arr = Array(tLessBasicValue.length).fill('ctc');
                        const keyValuePairs = drosteEl.value.toLowerCase()
                            .replaceAll(/[^crltx0-9=;,.]/g, '')
                            .split(/[;,.]/)
                        for (const kv of keyValuePairs) {
                            const value = kv.replace(/.*=/, '')
                            const keys = kv.replace(/=[^=]*$/, '').split(/=/)
                            for (const key of keys) {
                                arr[parseInt(key.replace(/x/i, ''))] = value;
                            }
                        }
                        const flipped = flip2(arr.join(';'))
                            .split(';');
                        for (let i = 0; i < flipped.length; i++) {
                            flipped[i] = `x${i}=${flipped[i]}`;
                        }
                        GF_hybrid.tweak.setRecipe(
                            flip2(tLessBasicValue),
                            flipped.join(';')
                                .replace(/x[0-9]+=ctc(;|$)/gi,'')
                                .replace(/;$/,'')
                        );
                    } else {
                        GF_hybrid.tweak.setRecipe(flip2(basicValue), flip2(drosteEl.value));
                    }
                } else {
                    GF_hybrid.tweak.setRecipe(flip2(basicValue));
                }
            },
        },
        setRecipe(basicStitch, droste1Stitches, droste2Stitches) {
            const basicEl = document.getElementById(this.basicStitch.id);
            if (basicEl) {
                basicEl.value = basicStitch ?? '';
                this.basicStitch.lastValid = basicStitch ?? '';
                this.basicStitch.setColorCode();
            }
            const drosteOnBasicEl = document.getElementById(this.drosteOnBasicStitch.id);
            if (drosteOnBasicEl) {
                drosteOnBasicEl.value = droste1Stitches ?? '';
                this.drosteOnBasicStitch.lastValid = droste1Stitches ?? '';
            }
            // TODO: second step of droste stitches, requires more intelligence in resetting previously assigned stitches
        }
    },
    galleryPanels: {
        specs: {
            'pattern': {caption: 'Pattern gallery', height: '150px', load(){GF_tiles.loadGallery({jsAction: 'GF_hybrid.setPattern(this);return false;', containerId: 'pattern'});} },
            'snow3': {caption: '3/6 pair snow gallery', height: '50px', load(){GF_hybrid.recipes.createSnowGallery(GF_hybrid.recipes.snow3, 'snow3', `mix4snow`);}},
            'snow4': {caption: '4/8 pair snow gallery', height: '65px', load(){GF_hybrid.recipes.createSnowGallery(GF_hybrid.recipes.snow4, 'snow4', `images/4-8-legs`);}},
            'stitches': {caption: 'Stitches gallery', height: '100px', load(){GF_hybrid.recipes.createStitchGallery('stitches');}}
        },
        createHTML(container) {
            const galleryKeys = Object.keys(this.specs);
            for(let i = 0; i<galleryKeys.length; i++){
                const key1 = galleryKeys[i];
                let options = ''
                galleryKeys.forEach(function (key2) {
                    const caption = GF_hybrid.galleryPanels.specs[key2].caption;
                    const clazz = `${key2.startsWith('snow') ? 'class= "snowOption"' : ''}`;
                    if (key2 === key1) {
                        options += `<option ${clazz} value='${key2}' disabled selected>${caption}</option>`;
                    } else {
                        options += `<option ${clazz} value='${key2}'>${caption}</option>`;
                    }
                });
                const chooser = `<select class="galleryChooser" onchange="GF_hybrid.galleryPanels.switchVisibleGallery(this, ${i});">${options}</select>`;
                const sizeOptions = {width:'100%', height: this.specs[key1].height};
                GF_panel.load({caption: chooser, id: key1, controls: ["resize"], size: sizeOptions, parent: container});
                document.getElementById(key1).parentNode.style.display = 'none';
            }
            document.getElementById('pattern').parentNode.insertAdjacentHTML(
                'beforeend',`
                <div style="display: inline-block">More on
                <a href="/tesselace-gf">tesselace-gf</a>,
                <a href="/MAE-gf">MAE-gf</a> or
                <a href="/GroundForge/pattern">D.I.Y.</a>
                </div>
                `);

            // allways needed
            this.specs.stitches.load();
            this.specs.stitches.loaded = true;
        },
        toggleSnowOptions(disable) {
            Array.from(document.getElementsByClassName('snowOption'))
                .forEach(option => { option.disabled = disable; });
        },

        switchVisibleGallery(chooser, initialIndex){
            chooser.parentNode.parentNode.style.display = 'none';
            this.initVisibleGallery(chooser.value);
            chooser.options[initialIndex].disabled = false;
            chooser.selectedIndex = initialIndex;
            chooser.options[initialIndex].disabled = true;
        },
        initVisibleGallery(galleryId){
            if (!this.specs[galleryId].loaded) {
                this.specs[galleryId].load()
                this.specs[galleryId].loaded = true;
            }
            document.getElementById(galleryId).parentNode.style.display = 'block';
        },
        onlyStitches(){
            // show panel containing the gallery
            const stitchesEl = document.getElementById('stitches').parentNode;
            stitchesEl.style.display = 'block';
            // no choice for other galleries in panel caption:
            stitchesEl.getElementsByTagName('select')[0].outerHTML = 'select stitch example';
        },
        isSnowVisible() {
            return GF_hybrid.isVisible(("snow3")) || GF_hybrid.isVisible(("snow4"));
        }
    },
    swatchSize: {
        getHtmlString() {
            return `
            Swatch size:
            <span style="display: inline-block; vertical-align: top">
                <label>
                    <input type="number" name="patchWidth" id="patchWidth" min="1" max="28" >
                    columns
                </label>
                <br>
                <label>
                    <input type="number" name="patchHeight" id="patchHeight" min="1" max="35" >
                    rows
                </label>
            </span>
            `;},
        init(){
            document.querySelectorAll('#patchWidth, #patchHeight')
                .forEach(el => {
                    el.value = GF_hybrid.patternLink.getValueOf(el.id);
                    el.addEventListener('input', GF_hybrid.swatchSize.fixSwatchSize);
                    el.dataset.lastValid = el.value;
                });
        },
        fixSwatchSize(changedEl) {
            const currentTarget = changedEl.currentTarget;
            if (Number.isNaN(currentTarget.valueAsNumber) || currentTarget.valueAsNumber < 1 ) {
                GF_hybrid.toast.show("Please enter positive numbers for the swatch size.");
                currentTarget.value = currentTarget.dataset.lastValid;
                return;
            }
            currentTarget.dataset.lastValid = currentTarget.value;
            GF_hybrid.patternLink.setKeyValue(currentTarget.name, currentTarget.value);

            const config = TilesConfig(GF_hybrid.patternLink.getValue());
            const width = Number.parseInt(document.getElementById('patchWidth')?.value ?? '', 10) || 0;
            const height = Number.parseInt(document.getElementById('patchHeight')?.value ?? '', 10) || 0;
            if (config.centerMatrixRows * 1.5 > height || config.centerMatrixCols * 1.5 > width) {
                GF_hybrid.toast.show(
                    `Recommended swatch size: at least 1.5 tiles. Tile size is ${config.centerMatrixCols}x${config.centerMatrixRows}.`
                );
            } else if( currentTarget.validity.rangeOverflow ){
                GF_hybrid.toast.show("Large, dense swatches may slow down diagram rendering and overload browsers.");
            }
        }
    },
    patternLink: {
        params: new URLSearchParams((window.location.search).includes('patchWidth')
            ? window.location.search.replaceAll(/[^a-zA-Z0-9=,.&-]/g,'')
            : "patchWidth=7&patchHeight=7&footside=---x,---4,---x,---4&tile=5-,-5,5-,-5&headside=-,c,-,c,&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=2&e1=lclc&l2=llctt&f2=rcrc&d2=rrctt&e3=rcrc&l4=llctt&f4=lclc&d4=rrctt&droste2=e12=clcrcl,e13=ct,f42=ctcl,e32=f22=ctcr,e33=f43=lct,e31=f21=lctc,e11=rclcrc,f23=rct,f41=rctc,e10=tc,f20=tcl,e30=f40=tcr"
        ),
        getLinkHtmlString() {return `<a id="selfRef" href="?${decodeURIComponent(this.params.toString())}">Pattern</a>`},
        setParams(newParams) {
            // for historical reasons: step-1 is URL argument &droste2
            for(const [key,value] of Object.entries(newParams)) {
                this.params.set(key, value);
                if (/^droste[123]$/.test(key)) {
                    document.getElementById(key).value = value;
                }
            }
            this.setValue(decodeURIComponent(this.params.toString()));
        },
        setValue(value) {
            document.getElementById('selfRef').href = '?' + value;
        },
        getValueOf(key) {
            return new URLSearchParams(this.getValue()).get(key);
        },
        getValue() {
            return decodeURIComponent(this.params.toString());
        },
        appendToDroste(stepNr, keyValuePairs) {
            const id = 'droste' + (stepNr + 2);
            const oldValue = this.params.get(id);
            this.params.set(id, (oldValue ? oldValue + ',' : '') + keyValuePairs);
            this.setValue(decodeURIComponent(this.params.toString()));
        },
        setKeyValue(key, value) {
            this.params.set(key, value);
            this.setValue(decodeURIComponent(this.params.toString()));
        },
    },
    isVisible(id) {
        const el = document.getElementById(id);
        if (!el) return false;
        const cs = window.getComputedStyle(el);
        if (cs.display === 'none' || cs.visibility === 'hidden' || cs.opacity === '0') return false;
        const rect = el.getBoundingClientRect();
        return rect.width > 0 && rect.height > 0;
    },
    steps:{
        getHtmlString(diagramType){ return `
            <label >
                ${diagramType === 'droste'
                ? '<span>Droste step number: ' // one that rules the hidden others (pair/thread)
                : `${diagramType}s<span>, step: ` // the start of the diagram label should always be visible
                }
                <input type='number' min='0' max='3' value='0'
                id='${diagramType}Step' name='${diagramType}Step' title='droste step' >
                </span>
            </label>
            `;
        },
        init(pageType) {
            const params = new URLSearchParams(GF_hybrid.patternLink.getValue());
            const pairStep = document.getElementById('pairStep');
            const threadStep = document.getElementById('threadStep');
            const drosteStep = document.getElementById('drosteStep');
            switch(pageType) {
                case 'drosteMixer':
                    pairStep.value = params.get('pairStep') || 0;
                    threadStep.value = params.get('threadStep') || 1;
                    GF_hybrid.hideParents(['drosteStep']);
                    break;
                case 'droste':
                    pairStep.value = threadStep.value = drosteStep.value = params.get('pairStep') || 1;
                    GF_hybrid.hideParents(['pairStep','threadStep']);
                    break;
                default: // in practice: stitches
                    pairStep.value = threadStep.value = drosteStep.value = 0;
                    GF_hybrid.hideParents(['pairStep','threadStep', 'drosteStep']);
            }
        },
        setListeners() {
            function markDirty(id) {
                const panelIds = id === 'drosteStep'
                    ? ['pair_panel', 'thread_panel']
                    : [id.replace('Step', '') + '_panel'];

                panelIds.forEach(pid => {
                    const panelEl = document.getElementById(pid);
                    if (panelEl.getElementsByTagName('svg').length > 0) {
                        panelEl.style.backgroundColor = GF_hybrid.dirtyBackGround;
                    }
                });
            }
            function fixStepNr(e) {
                const val = parseInt(e.target.value, 10);
                const snowVisible = GF_hybrid.galleryPanels.isSnowVisible() || document.getElementById("drosteStitches").value.trim() !== '';
                const max = snowVisible && e.target.id === 'pairStep' ? 2 : 3;
                const step = Number.isNaN(val) ? 0 : Math.max(0, val);
                if (val !== step || step > max) {
                        GF_hybrid.toast.show("Step: min=0, large numbers overwelm browsers.");
                }
                GF_hybrid.galleryPanels.toggleSnowOptions(step === 3);
                e.target.value = step;
                markDirty(e.target.id);
                return step;
            }
            function toggleIgnoredVisible(step) {
                document.getElementById('ignored').style.display = step === 0 ? 'inline-block' : 'none';
            }
            document.getElementById('threadStep').addEventListener('input', fixStepNr);
            document.getElementById('pairStep').addEventListener('input', e => {
                const step = fixStepNr(e);
                toggleIgnoredVisible(step);
                document.getElementById('drosteStep').value = step;
            });
            document.getElementById('drosteStep').addEventListener('input', e => {
                const step = fixStepNr(e);
                toggleIgnoredVisible(step);
                document.getElementById('threadStep').value = step;
                document.getElementById('pairStep').value = step;
            });
        }
    },
    generateSelectedDiagram(diagramType) {
        const stepNr = parseInt(document.getElementById(`${diagramType}Step`).value, 10);
        const q = this.patternLink.getValue();
        const steps = [];
        const params = new URLSearchParams(q);
        for (let i = 0; i < stepNr; i++) {
            const s = params.get('droste'+(i+2));
            steps[i] = s ? s : "ctc";
        }
        GF_panel.diagramSVG({id: diagramType+ '_panel', query: q, type: diagramType, steps: steps});
        document.getElementById(diagramType+ '_panel').style.backgroundColor = "";
        if(diagramType==='pair')
            this.generateLegend();
    },
    setStitchEvents() {
        function stitchHandler(event) {
            const drosteOnBasicValue = document.getElementById(GF_hybrid.tweak.drosteOnBasicStitch.id).value;
            const newStitchInput = document.getElementById(GF_hybrid.tweak.basicStitch.id).value;
            const newStitchValue = newStitchInput
                ? newStitchInput
                : GF_hybrid.getRandomStitch();

            const selectedText = event.currentTarget.textContent;
            const selectedStitchId = selectedText.replace(/.* /, "");

            const pairPanel = document.getElementById('pair_panel');
            for (let title of pairPanel.getElementsByTagName('title')) {
                if (title.innerHTML === selectedText) {
                    title.parentNode.insertAdjacentHTML(
                        'beforeend',
                        '<circle cx="0" cy="0" r="9" fill="#000" style="opacity: 0.15;"></circle>'
                    );
                }
            }
            const threadPanel = document.getElementById('thread_panel');
            for (let path of threadPanel.getElementsByTagName('path')) {
                if (path.textContent.includes(' - ' + selectedStitchId)) {
                    path.style.opacity = 0.5;
                }
            }
            const stepNr = parseInt(document.getElementById("pairStep").value);
            // TODO  patternLink instead
            if (0 === stepNr) {
                GF_hybrid.patternLink.setKeyValue(selectedStitchId, newStitchValue);
            } else {
                GF_hybrid.patternLink.appendToDroste(stepNr,selectedStitchId+"="+newStitchValue);
            }
            if (drosteOnBasicValue.trim() === '') {
                return;
            }
            let extraSteps = ''
            if (drosteOnBasicValue.includes('=')) {
                const count = newStitchValue.replaceAll(/t/g, 'lr').length;
                for (let i = 0; i < count; i++) {
                    // make sure not to inherit previous definitions
                    // TODO more complicated for a second droste step
                    extraSteps += `${selectedStitchId}${i}=`;
                }
                extraSteps += 'ctc,';
                extraSteps += drosteOnBasicValue.replaceAll(/x/gi, selectedStitchId);
            } else {
                const newDrosteStitches = drosteOnBasicValue.split(/[,.]/);
                for (let i = 0; i < newDrosteStitches.length; i++) {
                    extraSteps += `,${selectedStitchId}${i}=${newDrosteStitches[i]}`;
                }
            }
            GF_hybrid.patternLink.setKeyValue(selectedStitchId, newStitchValue);
            GF_hybrid.patternLink.appendToDroste(stepNr, extraSteps);
        }

        Array.from(document
            .getElementById('pair_panel')
            .querySelectorAll('title')
        ).forEach(function (title) {
            if (!title.textContent.startsWith('Pair'))
                title.parentNode.addEventListener('click', stitchHandler)
        });
    },
    scrollIfTooLittleIsVisible(elementId) {
        const threadPanel = document.getElementById(elementId);
        const rect = threadPanel.getBoundingClientRect();
        const vh = window.innerHeight || document.documentElement.clientHeight;
        const visibleHeight = Math.max(0, Math.min(rect.bottom, vh) - Math.max(rect.top, 0));
        if (visibleHeight / rect.height < 0.3) {
            threadPanel.scrollIntoView({behavior: 'smooth', block: 'center'});
        }
    },
    setPattern(element) {
        let q = element.getAttribute('xlink:href').split('?')[1];
        this.patternLink.setValue(q);
        document.getElementById('pairStep').value = 0;
        document.getElementById('droste1').value = '';
        document.getElementById('droste2').value = '';
        document.getElementById('droste3').value = '';
        this.generateSelectedDiagram('pair');
        this.setStitchEvents();
        this.generateLegend();
        document.getElementById('thread_panel').innerHTML = '';
        GF_panel.scrollIfTooLittleIsVisible(document.getElementById('pair_panel'));
    },
    generateLegend(){
        const dict = {};
        Array.from(document.getElementById('pair_panel')
            .querySelectorAll('.node'))
            .forEach(node => {
                const text = node.textContent.toLowerCase();
                if (!text.startsWith('pair')) {
                    const minorKey = text.split(' ')[0];
                    const majorKey = minorKey
                        .replace(/^[tlr]+/g, '')
                        .replace(/[tlr]+$/g, '');
                    const value = text.substring(text.lastIndexOf(' ') + 1);
                    if (!dict[majorKey]) dict[majorKey] = {};
                    if (!dict[majorKey][minorKey]) {
                        dict[majorKey][minorKey] = new Set();
                    }
                    dict[majorKey][minorKey].add(value);
                }
            });
        const target = document.getElementById('legend_panel');
        target.innerHTML = '';
        Object.keys(dict).sort().forEach(key => {
            target.insertAdjacentHTML('beforeend',`
                  <svg width="25px" height="25px">
                      <g transform="scale(3,3)">
                        <g transform="translate(4,4)">
                          ${PairSvg.shapes(key)}
                        </g>
                      </g>
                    </svg><br>
                    ${Object.entries(dict[key]).sort()
                            .map(([k, v]) => `${k}: ${Array.from(v).join(', ')}`)
                            .join('<br>')
                    }
                <br>`)
        });
    },
    toast: {
        /** id of the div (or whatever) to contain the message, styled more or less like the status bar of the browser */
        id: "toast",
        /** To be combined with set, typically at onmouseleave */
        hide(){
            document.getElementById(this.id).style.display = 'none';
        },
        /** Typically used at onmouseenter */
        set(message){
            const toast = document.getElementById(this.id);
            toast.textContent = message;
            toast.style.display = 'block';
            return toast
        },
        /** Combines set and hide, typically for input errors, automatically cleared */
        show(message){
            const toast = this.set(message)

            function hideToast() {
                toast.style.display = 'none';
                window.removeEventListener('mousedown', hideToast);
                window.removeEventListener('keydown', hideToast);
                window.removeEventListener('focus', hideToast, true);
            }

            window.addEventListener('mousedown', hideToast);
            window.addEventListener('keydown', hideToast);
            window.addEventListener('focus', hideToast, true);
        },
        showSaveHint(evt){
            const hasPointerType = evt && typeof evt.pointerType === 'string';
            const touchCapable = navigator.maxTouchPoints > 0 || ('ontouchstart' in window);
            if (!hasPointerType && touchCapable) {
                return;
            }
            const isTouch = hasPointerType && (evt.pointerType === 'touch' || evt.pointerType === 'pen');
            if (isTouch) {
                this.show('Firefox: print - share (to docs, ...), Chrome/Safari: share - print - print - to files; ');
            } else {
                this.set('Print with PDF as destination, prepare by adjusting panel sizes.');
            }
        }
    },
    /**
     * Loads all components required for the droste mixer.
     * @memberof GF_hybrid
     * @param {!HTMLElement} container receives the generated components
     */
    load(container) {
        console.log('================ Loading panels ================');
        const pairWandHref = "javascript:GF_hybrid.generateSelectedDiagram('pair');GF_hybrid.setStitchEvents();document.getElementById('thread_panel').style.backgroundColor = GF_hybrid.dirtyBackGround;void(0);";
        const threadWandHref = "javascript:GF_hybrid.generateSelectedDiagram('thread')";
        this.galleryPanels.createHTML(container);
        GF_panel.load({caption: "tweak selected stitch", id: "tweak", size:{width:'100%', height: 'auto'}, parent: container});
        container.insertAdjacentHTML('beforeend',`
            <p>
                ${this.patternLink.getLinkHtmlString()}
                <input id="saveDiagramsBtn" value="save/share diagrams" type="button" class="noprint"
                 onclick="if (! ('ontouchstart' in window || 'ontouchmove' in window)) window.print()"
                 >
            </p>
            <p class="noprint">
                Assign tweaked stitch <button onclick="GF_hybrid.assignToAll()" >to all</button>
                <button onclick="GF_hybrid.assignToIgnored()" id="ignored">to ignored</button>
                or&nbsp;click a stitch in the pair diagram.
            </p>
            <p class="noprint">
            ${this.steps.getHtmlString("droste")}
            ${GF_hybrid.swatchSize.getHtmlString()}
            </p>
            <div id="toast"></div>
        `);
        GF_hybrid.swatchSize.init();
        const saveBtn = document.getElementById('saveDiagramsBtn');
        if (saveBtn) {
            saveBtn.addEventListener('pointerenter', e => this.toast.showSaveHint(e));
            saveBtn.addEventListener('pointerdown', e => this.toast.showSaveHint(e));
            saveBtn.addEventListener('mouseenter', e => this.toast.showSaveHint(e));
            saveBtn.addEventListener('touchstart', e => this.toast.showSaveHint(e));
            saveBtn.addEventListener('pointerleave', () => this.toast.hide());
            saveBtn.addEventListener('pointercancel', () => this.toast.hide());
            saveBtn.addEventListener('mouseleave', () => this.toast.hide());
        }
        GF_panel.load({caption: this.steps.getHtmlString("pair"), id: "pair_panel", wandHref: pairWandHref, controls: ["resize"], parent: container});
        GF_panel.load({caption: this.steps.getHtmlString("thread"), id: "thread_panel", wandHref: threadWandHref, controls: ["resize", "color"], parent: container});
        GF_panel.load({caption: 'stitch enumeration', id: "legend_panel", controls: ["resize"], parent: container});
        this.steps.setListeners();
        document.getElementById('tweak').insertAdjacentHTML('beforeend', GF_hybrid.tweak.getHtmlString());
        document.getElementById('tweak').parentNode.style = `width: calc(100% - 7px)`;
        for (let type of ["pair", "thread"]) {
            const panelEl = document.getElementById(type + '_panel');
            panelEl.innerHTML = "Click/tap the wand to (re)generate the diagram. Large diagrams may take several seconds.";
            panelEl.style.color = "#bbbbbb";
        }
        console.log('================ Loaded panels ================');
    },
    deferredLoadingHandle: null,

    deferredLoading() {
        if (this.deferredLoadingHandle && this.deferredLoadingHandle.cancel) {
            this.deferredLoadingHandle.cancel();
        }

        let canceled = false;
        let timer = null;
        const cancel = () => {
            canceled = true;
            if (timer) clearTimeout(timer);
        };

        const scheduleIdle = (fn) => {
            if ("requestIdleCallback" in window) {
                requestIdleCallback(() => {
                    if (!canceled) fn();
                }); // no timeout: wait for real idle
            } else {
                setTimeout(() => {
                    if (!canceled) fn();
                }, 0);
            }
        };

        this.deferredLoadingHandle = { cancel };

        // Give images/layout a head start
        timer = setTimeout(() => {
            scheduleIdle(() => {
                this.generateSelectedDiagram("pair");
                this.setStitchEvents();

                scheduleIdle(() => {
                    this.generateSelectedDiagram("thread");
                    if (this.deferredLoadingHandle?.cancel === cancel) {
                        this.deferredLoadingHandle = null;
                    }
                });
            });
        }, 30); // tune 80-200ms
    },
    hideParents(hiddenElements) {
        for (let id of hiddenElements) {
            document.getElementById(id).parentNode.style.display = 'none';
        }
    },
    /**
     * Wrapper for load. Initial step is 1
     *
     * @param {!HTMLElement} container receives the generated components
     */
    loadDroste(container){
        this.load(container);
        GF_hybrid.galleryPanels.onlyStitches();
        this.hideParents([GF_hybrid.tweak.drosteOnBasicStitch.id]);
        this.steps.init('droste');
        GF_hybrid.deferredLoading();
    },
    /**
     * Wrapper for load. Initial step is 0
     *
     * @param {!HTMLElement} container receives the generated components
     * */
    loadStitches(container){
        this.load(container);
        GF_hybrid.galleryPanels.onlyStitches();
        this.hideParents([GF_hybrid.tweak.drosteOnBasicStitch.id]);
        this.steps.init('stitches');
        GF_hybrid.deferredLoading();
    },
    /**
     * Wrapper for load. Hides the third step field
     *
     * @param {!HTMLElement} container receives the generated components
     * */
    loadDrosteMixer(container){
        this.load(container);
        this.steps.init('drosteMixer');
        const pairStep = document.getElementById('pairStep').value * 1;
        GF_hybrid.galleryPanels.toggleSnowOptions(pairStep === 3)
        if (pairStep === 3) {
            this.galleryPanels.initVisibleGallery('stitches');
        } else {
            this.galleryPanels.initVisibleGallery('snow4');
        }
        GF_hybrid.deferredLoading();
    },
    assignToIgnored() {
        // limited to 100 columns, this avoids conflicts with other numbered params such as droste
        const stitchValue = document.getElementById(GF_hybrid.tweak.basicStitch.id).value;
        const tags = new Set(
            Array.from(new URLSearchParams(GF_hybrid.patternLink.getValue()))
                .filter(([k, v]) => v === '-' && /^[a-z][a-z]?[0-9]+$/i.test(k))
                .map(([k]) => k)
        );
        if (tags.size === 0) {
            this.toast.show("No ignored stitches.")
        } else {
            this.assignToSelected(tags, stitchValue)
        }
    },
    assignToSelected(tagSet, stitchValue) {
        document.getElementById('pair_panel').style.backgroundColor = GF_hybrid.dirtyBackGround;
        for (const key of tagSet) {
            this.patternLink.setKeyValue(key, stitchValue ? stitchValue : this.getRandomStitch());
        }
    },
    assignToAll() {
        const stepValue = document.getElementById('pairStep').value * 1;
        const stitchValue = document.getElementById(GF_hybrid.tweak.basicStitch.id).value;
        if (document.getElementById(GF_hybrid.tweak.drosteOnBasicStitch.id).value.trim() !== '') {
            this.toast.show("Assign to all is not implemented for droste applied to basic stitch")
        } else if (stepValue !== 0 && stitchValue) {
            document.getElementById('droste' + stepValue)
                .value = stitchValue; // set a new default for this droste level
        } else {
            const tags = new Set(
                [...document.querySelectorAll('#pair_panel title')]
                    .map(el => (el.textContent || '')
                        .toLowerCase()
                        .split(' - ')[1]?.trim() || ''
                    )
            );
            tags.delete('');
            if (tags.size === 0) {
                this.toast.show("No stitches found in the pair diagram.")
            } else {
                this.assignToSelected(tags, stitchValue)
            }
        }
    }
}
