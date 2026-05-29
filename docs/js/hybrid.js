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
                                   onclick="GF_hybrid.tweak.setRecipe('${basicStitch}','${droste1}','${droste2 ?? ''}')"
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
            lastValid: '',
            getHtmlString() {
                const other = `document.getElementById('${GF_hybrid.tweak.drosteOnBasicStitch.id}`;
                return `
            <label>Basic stitch:
                <span id="colorCode"></span>
                <input type="text" id="${this.id}"
                        value="${GF_hybrid.tweak.basicStitch.lastValid}" placeholder="empty=random; type ? for more info"
                        oninput="GF_hybrid.tweak.basicStitch.fixInput(this,${other}'))"
                />
             </label>`
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
            fixInput(basicStitchEl, drostOnBasicEl) {
                let value = basicStitchEl.value.toLowerCase().trim();
                const hasDroste = drostOnBasicEl && drostOnBasicEl.value.trim() !== '';
                const regexp = hasDroste ? /^[tclr]*$/ : /^(-|([tclr])*)$/;
                if (!regexp.test(value)) {
                    basicStitchEl.value = this.lastValid;
                    const pos1 = basicStitchEl.selectionStart - 1;
                    const pos2 = basicStitchEl.selectionEnd - 1;
                    basicStitchEl.setSelectionRange(pos1, pos2);
                    if (GF_hybrid.isVisible('drosteStep')) {
                        GF_hybrid.showToast("Possible stitch characters: CTLR, or (at step level zero) a single '-' to ignore a stitch."  );
                    } else if (!GF_hybrid.isVisible('pairStep')) {
                        GF_hybrid.showToast("Possible stitch characters: CTLR, or a single '-' to ignore a stitch."  );
                    } else {
                        GF_hybrid.showToast("Possible stitch characters: CTLR. At pair step level zero, a single '-' is possible to ignore a stitch. " +
                            "With content in 'droste on basic stitches', T is replaced with LR for proper flipping."
                        );
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
            lastValid: '',
            getHtmlString() {
                const other = `document.getElementById('${GF_hybrid.tweak.basicStitch.id}`;
                return `
            <label>Droste applied to basic stitch:
                <input type="text" id="${this.id}"
                        value="${this.lastValid}" placeholder="Type ? for info"
                        oninput="GF_hybrid.tweak.drosteOnBasicStitch.fixInput(${other}'), this)"
                />
            </label>`
            },
            msg: `
                "Droste applied to basic stitch" needs either numbered stitches,
                 or as many stitches as characters in "Basic stitch".
                 Allowed separators between stitches: ";.," 
                 Example of a numbered stitch: "X12=CTCT".
                 Default for not specified stitches is "CTC".
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
                    const pos1 = drosteOnBasicEl.selectionStart - 1;
                    const pos2 = drosteOnBasicEl.selectionEnd - 1;
                    drosteOnBasicEl.setSelectionRange(pos1, pos2);
                    GF_hybrid.showToast(this.msg);
                }
            },
        },
        flip: {
            getHtmlString() { return `
                <p>Flip:
                <button onclick="GF_hybrid.tweak.flip.apply('b2d')">&harr;</button>
                <button onclick="GF_hybrid.tweak.flip.apply('b2p')">&varr;</button>
                <button onclick="GF_hybrid.tweak.flip.apply('b2d');GF_hybrid.recipes.flip.apply('b2p')">both</button>
                </p>
                `;
            },
            apply(direction) {
                function flip2(value) {
                    switch (direction) {
                        case 'b2d': return value
                            .replace(/l/g, "R")
                            .replace(/r/g, "L")
                            .toLowerCase();
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
                    if (key2 === key1) {
                        options += `<option value='${key2}' disabled selected>${caption}</option>`;
                    } else {
                        options += `<option value='${key2}'>${caption}</option>`;
                    }
                });
                const chooser = `<select onchange="GF_hybrid.galleryPanels.switchVisibleGallery(this, ${i});">${options}</select>`;
                const sizeOptions = {width:'100%', height: this.specs[key1].height};
                GF_panel.load({caption: chooser, id: key1, controls: ["resize"], size: sizeOptions, parent: container});
                document.getElementById(key1).parentNode.style.display = 'none';
            }
            // allways needed
            this.specs.stitches.load();
            this.specs.stitches.loaded = true;
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
        }
    },
    swatchSize: {
        getHtmlString() { return `
            Swatch size:
            <span style="display: inline-block; vertical-align: top">
                <label>
                    <input type="number" name="patchWidth" id="patchWidth" min="3" max="28" value="?" oninput="GF_hybrid.swatchSize.valueChanged()" autofocus="">
                    columns
                </label>
                <br>
                <label>
                    <input type="number" name="patchHeight" id="patchHeight" min="3" max="35" value="?" oninput="GF_hybrid.swatchSize.valueChanged()">
                    rows
                </label>
            </span>
            `;},
        valueChanged() {
            // TODO validate input, update first specs field and mark diagrams dirty
            GF_hybrid.showToast('Swatch size is not yet implemented. Workaround: patchWidth=NN&patchHeight=NN in first specifications field, then click wand.');
        }
    },
    patternInfo: { // TODO also the specs fields for droste
        getLinkHtmlString(q) {return `<a href="${q}" id="selfRef" style="display:none;">Updated pattern</a>`},
        getSpecsHtmlString(q) {return `<input type="text" id="droste0" value="${q}">`},
        setValue(value) {
            const specsField = document.getElementById('droste0');
            if(specsField) {
                specsField.value = value;
            }
            const patternLink = document.getElementById('selfRef');
            if(patternLink) {
                patternLink.href = '?' + value;
                patternLink.style.display = 'inline';
            }
            console.log("---------" + value);
        },
        getValue() {
            const specsField = document.getElementById('droste0');
            if(specsField) {
                return specsField.value;
            }
            const patternLink = document.getElementById('selfRef');
            if(patternLink) {
                return patternLink.href.split('?')[1];
            }
        }
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
        getHtmlString(type){ return `
            <label >
                ${type === 'droste'
                ? '<span>Droste step number: ' // one that rules the hidden others (pair/thread)
                : `${type}s<span>, step: `
                }
                <input type='number' min='0' max='3' value='0'
                id='${type}Step' name='${type}Step' title='droste step' >
            </label>
            `;
        },
        init(type) {
            const specs = document.getElementById('droste0');
            const params = new URLSearchParams(specs ? specs.value : '');
            const pairStep = document.getElementById('pairStep');
            const threadStep = document.getElementById('threadStep');
            let hide = [];
            switch(type) {
                case 'drosteMixer':
                    pairStep.value = params.get('pairStep') || 0;
                    threadStep.value = params.get('threadStep') || 1;
                    GF_hybrid.hideParents(['drosteStep']);
                    break;
                case 'droste':
                    pairStep.value = params.get('pairStep') || 1;
                    threadStep.value = params.get('pairStep') || 1;
                    GF_hybrid.hideParents(['pairStep','threadStep']);
                    break;
                default: // in practice: stitches
                    pairStep.value = 0;
                    threadStep.value = 0;
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
                const snowVisible = GF_hybrid.isVisible(("snow3")) || GF_hybrid.isVisible(("snow4"));
                const max = snowVisible && e.target.id === 'pairStep' ? 2 : 3;
                const step = isNaN(val) ? 0 : Math.min(max, Math.max(0, val));
                if (val !== step) {
                    if (GF_hybrid.isVisible('drosteStep')) {
                        GF_hybrid.showToast("Steps: min=0, max=3.");
                    }
                    else {
                        GF_hybrid.showToast("Steps: min=0, max=3, max for pairs is 2 when a snow gallery is visible.");
                    }
                }
                e.target.value = step;
                markDirty(e.target.id);
                return step;
            }
            function setDisplayValues(step) {
                document.getElementById('ignored').style.display = step === 0 ? 'inline-block' : 'none';
                if (step > 0) {
                    const specsStyle = document.getElementById('specs').style;
                    if (specsStyle.display === 'none')
                        specsStyle.height = '0';
                    specsStyle.display = 'block';
                }
            }
            document.getElementById('threadStep').addEventListener('input', fixStepNr);
            document.getElementById('pairStep').addEventListener('input', e => {
                const step = fixStepNr(e);
                setDisplayValues(step);
                document.getElementById('drosteStep').value = step;
            });
            document.getElementById('drosteStep').addEventListener('input', e => {
                const step = fixStepNr(e);
                setDisplayValues(step);
                document.getElementById('threadStep').value = step;
                document.getElementById('pairStep').value = step;
            });
        }
    },
    generateSelectedDiagram(diagramType) {
        const drosteIndex = parseInt(document.getElementById(`${diagramType}Step`).value, 10);
        const steps = [];
        for (let i = 1; i <= drosteIndex; i++) {
            const textarea = document.getElementById(`droste${i}`);
            const txt = textarea && textarea.value.trim() ? textarea.value.trim() : "ctc";
            steps.push(txt);
        }
        const q = this.patternInfo.getValue();
        GF_panel.diagramSVG({id: diagramType+ '_panel', query: q, type: diagramType, steps: steps});
        document.getElementById(diagramType+ '_panel').style.backgroundColor = "";
        if(diagramType==='pair')
            this.generateLegend();
    },
    setStitchEvents() {
        function stitchHandler(event) {
            const drosteValue = document.getElementById(GF_hybrid.tweak.drosteOnBasicStitch.id).value;
            const newStitchInput = document.getElementById(GF_hybrid.tweak.basicStitch.id).value;
            const newStitchValue = newStitchInput
                ? newStitchInput
                : this.getRandomStitch();

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
            const drosteIndex = parseInt(document.getElementById("pairStep").value);
            const drosteInput = document.getElementById('droste' + drosteIndex);
            if (drosteIndex === 0) {
                for (let kv of drosteInput.value.split(/&/)) {
                    let [key, value] = kv.split('=');
                    if (key === selectedStitchId) {
                        drosteInput.value = drosteInput.value.replace(kv, `${selectedStitchId}=${newStitchValue}`);
                        break;
                    }
                }
            } else {
                drosteInput.value += `\n${selectedStitchId}=${newStitchValue}`;
            }
            if (drosteValue.trim() === '') {
                return;
            }
            let extraSteps = '\n'
            if (drosteValue.includes('=')) {
                const count = newStitchValue.replaceAll(/t/g, 'lr').length;
                for (let i = 0; i < count; i++) {
                    // make sure not to inherit previous definitions
                    // TODO more complicated for a second droste step
                    extraSteps += `${selectedStitchId}${i}=`;
                }
                extraSteps += 'ctc\n';
                extraSteps += drosteValue.replaceAll(/x/gi, selectedStitchId);
            } else {
                const newDrosteStitches = drosteValue.split(/[,.]/);
                for (let i = 0; i < newDrosteStitches.length; i++) {
                    extraSteps += `\n${selectedStitchId}${i}=${newDrosteStitches[i]}`;
                }
            }
            const drosteId = 'droste' + (drosteIndex + 1);
            const params = new URLSearchParams(GF_hybrid.patternInfo.getValue());
            params.set(selectedStitchId, newStitchValue);
            params.set("pairStep", document.getElementById('pairStep').value);
            params.set("threadStep", document.getElementById('threadStep').value);
            params.set(drosteId, extraSteps.replaceAll('\n', ',').trim());
            GF_hybrid.patternInfo.setValue(decodeURIComponent(params.toString()));
            // last as it may fail when stepLevel is too high for the droste applied to basic stitch
            document.getElementById(drosteId).value += extraSteps + '\n';
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
        this.patternInfo.setValue(q);
        document.getElementById('pairStep').value = 0;
        document.getElementById('droste1').value = '';
        document.getElementById('droste2').value = '';
        document.getElementById('droste3').value = '';
        this.generateSelectedDiagram('pair');
        GF_hybrid.setStitchEvents();
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
    showToast(message) {
        const toast = document.getElementById('toast');
        toast.textContent = message;
        toast.style.display = 'block';

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
    /**
     * Loads all components required for the droste mixer.
     * @memberof GF_hybrid
     * @param {!HTMLElement} container receives the generated components
     */
    load(container) {
        console.log('================ Loading panels ================');
        const pairWandHref = "javascript:GF_hybrid.generateSelectedDiagram('pair');GF_hybrid.setStitchEvents()";
        const threadWandHref = "javascript:GF_hybrid.generateSelectedDiagram('thread')";
        let q = new URL(document.documentURI).search.slice(1)
            .replaceAll(/[^a-zA-Z0-9=,.&-]/g,'');
        if (q === "" || !q.includes('shiftRows')) {
            q = "patchWidth=7&patchHeight=7&footside=---x,---4,---x,---4&tile=5-,-5,5-,-5&headside=-,c,-,c,&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=2&e1=lclc&l2=llctt&f2=rcrc&d2=rrctt&e3=rcrc&l4=llctt&f4=lclc&d4=rrctt&droste2=e12=clcrcl,e13=ct,f42=ctcl,e32=f22=ctcr,e33=f43=lct,e31=f21=lctc,e11=rclcrc,f23=rct,f41=rctc,e10=tc,f20=tcl,e30=f40=tcr"
        }
        this.galleryPanels.createHTML(container);
        GF_panel.load({caption: "tweak selected stitch", id: "tweak", size:{width:'98%', height: 'auto'}, parent: container});
        container.insertAdjacentHTML('beforeend',`
            <p>
                Assign tweaked stitch <button onclick="GF_hybrid.assignToAll()" >to all</button>
                <button onclick="GF_hybrid.assignToIgnored()" id="ignored">to ignored</button>
                or click a stich in the pair diagram.
                ${this.patternInfo.getLinkHtmlString(q)}
            </p>
            <p>
            ${this.steps.getHtmlString("droste")}
            ${GF_hybrid.swatchSize.getHtmlString()}
            </p>
            <div id="toast"></div>
        `);
        GF_panel.load({caption: this.steps.getHtmlString("pair"), id: "pair_panel", wandHref: pairWandHref, controls: ["resize"], parent: container});
        GF_panel.load({caption: this.steps.getHtmlString("thread"), id: "thread_panel", wandHref: threadWandHref, controls: ["resize", "color"], parent: container});
        GF_panel.load({caption: 'stitch enumeration', id: "legend_panel", controls: ["resize"], parent: container});
        GF_panel.load({caption: "specifications", id: "specs", controls: ["resize"], size:{width: '100%', height: '300px'}, parent: container});
        this.steps.setListeners();
        document.getElementById('tweak').insertAdjacentHTML('beforeend', GF_hybrid.tweak.getHtmlString());
        const params = new URLSearchParams(q);
        document.getElementById('tweak').parentNode.style = `width: calc(100% - 7px)`;
        const specsPanelContent = document.getElementById('specs');
        function drosteTextField(level) {
            const paramValue = (params.get('droste'+(level+1)) || '').replaceAll(',','\n') + '\n' || '';
            return `<textarea id="droste${level}" spellcheck="false" placeholder="droste step ${level}, default all: ctc">${paramValue}</textarea>`
        }
        specsPanelContent.innerHTML = `
          <a href="javascript:['droste1','droste2','droste3'].forEach(GF_panel.cleanupStitches)" 
             title="Reduce panel content"
             ><img src="${this.content_home}/images/broom.png"></a>
          Specs collected from URL and clicks:
          ${this.patternInfo.getSpecsHtmlString(q)}
          ${drosteTextField(1)}
          ${drosteTextField(2)}
          ${drosteTextField(3)}
        `;
        specsPanelContent.parentNode.style.display = "block";
        specsPanelContent.style.width = "100%";
        specsPanelContent.style.height = "0";
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
     * Wrapper for load. Initial step is 1 and specs panel is shown immediately
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
     * Wrapper for load. Initial step is 0 and specs panel is initially hidden, shown when step becomes larger.
     *
     * @param {!HTMLElement} container receives the generated components
     * */
    loadStitches(container){
        this.load(container);
        GF_hybrid.galleryPanels.onlyStitches();
        this.hideParents([GF_hybrid.tweak.drosteOnBasicStitch.id, 'specs']);
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
        this.galleryPanels.initVisibleGallery('snow4');
        this.steps.init('drosteMixer');
        GF_hybrid.deferredLoading();
    },
    assignToIgnored() {
        const stepValue = document.getElementById('pairStep').value * 1;
        const stitchValue = document.getElementById(GF_hybrid.tweak.basicStitch.id).value;
        let query = this.patternInfo.getValue();

        // key=- where key is letters+digits (e.g. e1=-, f42=-)
        // this also matches droste1=- but not expecting just a dash as content for the droste specs
        const regexp = /(^|&)([a-z]+[0-9]+=)-(&|$)/gi

        if (document.getElementById(GF_hybrid.tweak.drosteOnBasicStitch.id).value.trim() !== '') {
            this.showToast("Assign to ignored is not implemented for droste applied to basic stitch")
        } else if (stepValue !== 0 && stitchValue) {
            this.showToast("Assign to ignored is only implemented for step 1")
        } else if (!regexp.test(query)) {
            this.showToast("No ignored stitches.")
        } else {
            if (stitchValue) {
                query = query.replace(regexp, `$1$2${stitchValue}$3`);
            } else {
                query = query.replace(regexp, (match, sep, keyEq, tail) => {
                    const rnd = this.getRandomStitch();
                    return `${sep}${keyEq}${rnd}${tail}`;
                });
            }
            document.getElementById('pair_panel').style.backgroundColor = GF_hybrid.dirtyBackGround;
            this.patternInfo.setValue(query);
        }
    },
    assignToAll() {
        const stepValue = document.getElementById('pairStep').value * 1;
        const stitchValue = document.getElementById(GF_hybrid.tweak.basicStitch.id).value;
        const stitchTitles = Array.from(document.getElementById('pair_panel')
            .getElementsByTagName('title')
        );
        if (document.getElementById(GF_hybrid.tweak.drosteOnBasicStitch.id).value.trim() !== '') {
            this.showToast("Assign to all is not implemented for droste applied to basic stitch")
        } else if (stepValue !== 0 && stitchValue) {
            document.getElementById('droste' + stepValue).value =
                stitchValue; // default for this droste level
        } else if (!stitchTitles || stitchTitles.length === 0) {
            this.showToast("No stitches found in the pair diagram.")
        } else {
            document.getElementById('pair_panel').style.backgroundColor = GF_hybrid.dirtyBackGround;
            const params = new URLSearchParams(this.patternInfo.getValue());
            const regex = /^[a-zA-Z]{1,2}\d+$/;
            // remove predefined stitches
            for (const key of Array.from(params.keys())) {
                if (regex.test(key)) {
                    params.delete(key);
                }
            }
            // add stitches with ID-s from diagram
            stitchTitles.forEach(el => {
                const [stitch,tag = ''] = el.textContent.toLowerCase().split(/ - /);
                if (tag !== '') {
                    const newValue = stitchValue
                        ? stitchValue
                        : this.getRandomStitch();
                    params.set(tag, newValue);
                }
            });
            this.patternInfo.setValue(Array.from(params).map(([k, v]) => `${k}=${v}`).join('&'));
        }
    }
}