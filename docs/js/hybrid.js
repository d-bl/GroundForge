/**
 * GroundForge page generator.
 *
 * The surrounding page should take care of the house style like headers footers and menu's with help pages..
 *
 * Requires:
 * - d3.v4.min.js
 * - GroundForge-opt.js
 * - nudgePairs.js (nudgeDiagram)
 * - panel.js
 * - stitch-gallery.js
 * @namespace
 */
const GF_hybrid = {
    content_home: '.',
    snow4:[
        // screenshots taken at 50% zoom level
        // ndb: patterns from "naar de bron" by Nora Andries
        ['ndb-9Z.png','RCLRCR','X0=CL,X1=LLCLCR,X2=LLCLCRCLCLL,X3=CRCLCRCL,X4=CLCRLL,X5=C','X00=X70=C,X80=X81=X90=X91=LRT '],
        ['ndb-10N.png','CLCLRCRCLR','X0=X7=C,X1=RCLCR,X2=CRC,X3=RCTC,X4=CTCL,X5=CLCR,X6=CRCL,X8=X9=TT','X00=,X70=C,X80=X81=X90=X91=TT '],
        ['spider-1-ring.png','CLRCLR','X1=X2=CTCTC,X4=X5=TT'],
        ['spider-2-rings.png','CLRCLR','X1=CTCRCTC,X2=CTCLCTC,X4=X5=TT'],
        ['spider-3-rings.png','CLRCCLRCLR','X3=CTCCTC,X8=X9=TT','X33=CTCCTC,X80=X81=X90=X91=TT']
    ],
    snow3: [
        ['123-a.png',   'RCRCRC','CRC,CRCLCTC,CTCRC,RCL,C,C'],
        ['123-b.png',   'LCLCLC','RCL,CTC,CRCLLC,CRRCLCR,CTC,CL'],
        ['132-a.png',   'CRCRC','CTC,CTC,CTC,CTC,CTC'],
        ['312-a.png',   'LCLC','TCTC,RCTCL,CTCL,CTCT'],
        ['321-a.png',   'LCLC','TC,RCLCRC,CLCRCL,CT'],
        ['321-b.png',   'RCRC','TCR,LCTC,CTCR,LCT'],
        ['321-c.png',   'RCRC','TCL,LCTC,CTCR,RCT'],
        ['321-d.png',   'RCRC','T,LCTC,CTCR,CTCT'],
        ['126453-a.png','CLCLC','C,CTCTC,CTCTC,CTCTC,C'],
        ['153426-a.png','LCLCLC','T,RC,CTC,RCLCR,CTCL,CT'],
        ['154326-a.png','LCLC','T,RCTC,CTCTCL,CTCT'],
        ['156423-a.png','CRCRC','CR,CRCL,CLCRCLCR,RCRCL,C'],
        ['234561-a.png','LCLCLC','CR,CRCL,CLCR,CRCL,CLCR,C'],
        ['263451-a.png','CRCRC','CR,CRCL,CLCR,CRCL,CL'],
        ['321546-a.png','CLCLC','CL,CTCL,CRCRCR,RCR,C'],
        ['321654-a.png','CLCLCLC','LC,CRC,CLCRC,CLCR,C,CRC,CL'],
        ['321654-b.png','CRCRC','CR,CTCR,CLCLC,LCL,C'],
        ['354612-a.png','RCRCRC','CTCT,CT,CT,CT,CL,CTC'],
        ['426153-a.png','RCRC','LC,CRCLCLC,CRCRCLC,CR'],
        ['426153-b.png','RCRCRC','CR,CTCL,CTCR,CTCL,CTC,C'],
        ['456123-a.png','RCRC','R,LRC,CTCR,LCT'],
        ['456123-b.png','RCRCRCRC','C,CTC,RCLC,CTC,RC,RCL,CTC,C'],
        ['462513-a.png','LCLC','RC,CLCRC,CLCTC,RCL'],
        ['564312-a.png','RCRC','LCRC,CLCRC,CLCRC,CLCR'],
        ['563412-a.png','CRCRC','C,CTCTC,CLCR,RCTC,C'],
        ['623451-a.png','LCLCLCLC','R,C,CRC,CTC,LCRCL,CTC,CRC,CL'],
        ['623541-a.png','CLCLC','CTC,CT,CRC,CTC,CTC'],
        ['623541-b.png','CRCRC','CL,CTCTCR,CT,CTC,C']
    ],
    basicStitch: {
        id: 'basicStitchInput',
        lastValid: 'LCLC',
        htmlString() {
            const other = `document.getElementById('${GF_hybrid.drosteOnBasicStitch.id}`;
            return `
            <label>Basic stitch:
                <input type="text" id="${this.id}"
                        value="${this.lastValid}" placeholder="Type ? for info"
                        oninput="GF_hybrid.basicStitch.fixInput(this,${other}'))"
                />
             </label>`
        },
        msg: `
            Basic stitch only allows the characters  T, C, L, R.
            When "Droste applied to basic stitch" has content,
            T is replaced with LR for proper flipping.
            Without droste, just a "-" is also allowed to drop stitches. 
            `,
        fixInput(basicStitchEl, drostOnBasicEl) {
            let value = basicStitchEl.value.toLowerCase().trim();
            const hasDroste = drostOnBasicEl && drostOnBasicEl.value.trim() !== '';
            const regexp = hasDroste ? /^[tclr]*$/ : /^(-|([tclr])*)$/;
            if (!regexp.test(value)) {
                basicStitchEl.value = this.lastValid;
                const pos1 = basicStitchEl.selectionStart - 1;
                const pos2 = basicStitchEl.selectionEnd - 1;
                basicStitchEl.setSelectionRange(pos1, pos2);
                GF_hybrid.showToast(this.msg);
                return;
            }
            if (hasDroste) {
                value = value.replace(/[tT]/g, 'LR');
            }
            basicStitchEl.value = value.toUpperCase()
            this.lastValid = value;
        },
    },
    drosteOnBasicStitch: {
        id: 'drosteStitches',
        lastValid: 'TC,RCLCRC,CLCRCL,CT',
        htmlString() {
            const other = `document.getElementById('${GF_hybrid.basicStitch.id}`;
            return `
            <label>Droste applied to basic stitch:
                <input type="text" id="${this.id}"
                        value="${this.lastValid}" placeholder="Type ? for info"
                        oninput="GF_hybrid.drosteOnBasicStitch.fixInput(${other}'), this)"
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
            drosteOnBasicEl.value = value;
            if (isValid(value)) {
                GF_hybrid.drosteOnBasicStitch.lastValid = value;
                drosteOnBasicEl.value = value;
            } else {
                drosteOnBasicEl.value = GF_hybrid.drosteOnBasicStitch.lastValid;
                const pos1 = drosteOnBasicEl.selectionStart - 1;
                const pos2 = drosteOnBasicEl.selectionEnd - 1;
                drosteOnBasicEl.setSelectionRange(pos1, pos2);
                GF_hybrid.showToast(this.msg);
            }
        },
    },
    generateSelectedDiagram(diagramType) {
        const drosteIndex = parseInt(document.getElementById(`${diagramType}Step`).value, 10);
        const steps = [];
        for (let i = 1; i <= drosteIndex; i++) {
            const textarea = document.getElementById(`droste${i}`);
            const txt = textarea && textarea.value.trim() ? textarea.value.trim() : "ctc";
            steps.push(txt);
        }
        const q = document.getElementById('droste0').value;
        GF_panel.diagramSVG({id: diagramType+ '_panel', query: q, type: diagramType, steps: steps});
        document.getElementById(diagramType+ '_panel').style.backgroundColor = "";
        if(diagramType==='pair')
            this.generateLegend();
    },
    setStitchEvents() {
        function stitchHandler(event) {
            const newStitchValue = document.getElementById('basicStitchInput').value;
            const drosteValue = document.getElementById(GF_hybrid.drosteOnBasicStitch.id).value;
            if (newStitchValue === '') return;

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
            let extraSteps = ''
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
            const droste0 = document.getElementById('droste0');
            const params = new URLSearchParams(droste0.value);
            params.set(selectedStitchId, newStitchValue);
            params.set("pairStep", document.getElementById('pairStep').value);
            params.set("threadStep", document.getElementById('threadStep').value);
            params.set(drosteId, extraSteps.replaceAll('\n', ',').trim());
            droste0.value = decodeURIComponent(params.toString());
            document.getElementById('selfRef').href = '?' + droste0.value
            document.getElementById('selfRef').style.display = 'inline';
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
    flip(direction) {
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
        const basicEl = document.getElementById('basicStitchInput');
        const drosteEl = document.getElementById(GF_hybrid.drosteOnBasicStitch.id);
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
                drosteEl.value = flipped.join(';')
                    .replace(/x[0-9]+=ctc(;|$)/gi,'')
                    .replace(/;$/,'');
                basicEl.value = flip2(tLessBasicValue);
            } else {
                drosteEl.value = flip2(drosteEl.value);
                basicEl.value = flip2(basicValue);
            }
        } else {
            basicEl.value = flip2(basicValue);
        }
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
        document.getElementById('pairStep').value = 0;
        document.getElementById('droste0').value = q;
        document.getElementById('droste1').value = '';
        document.getElementById('droste2').value = '';
        document.getElementById('droste3').value = '';
        this.generateSelectedDiagram('pair');
        GF_hybrid.setStitchEvents();
        document.getElementById('selfRef').style.display = 'none';
        document.getElementById('thread_panel').innerHTML = '';
        GF_panel.scrollIfTooLittleIsVisible(document.getElementById('pair_panel'));
    },
    otherGallery(chooser, initialIndex){
        chooser.parentNode.parentNode.style.display = 'none';
        document.getElementById(chooser.value).parentNode.style.display = 'block';
        chooser.options[initialIndex].disabled = false;
        chooser.selectedIndex = initialIndex;
        chooser.options[initialIndex].disabled = true;
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
        GF_stitches.setStitch = function(stitch) {
            document.getElementById('basicStitchInput').value = stitch;
            document.getElementById(GF_hybrid.drosteOnBasicStitch.id).value = "";
            GF_hybrid.setColorCode();
        }

        function createSnowGallery(recipes, containerId, imgPath) {
            const container = document.getElementById(containerId);
            for (let [img, basicStitch, droste1, droste2] of recipes) {
                container.insertAdjacentHTML('beforeend', `
                    <a href="javascript:GF_hybrid.setRecipe('${basicStitch}','${droste1}','${droste2}')">
                    <img src="${GF_hybrid.content_home}/${imgPath}/${img}" alt="${img}">
                    </a>
                `);
            }
        }

        function galleryPanels() {
            const galleries = {
                'pattern': {caption: 'Pattern gallery', height: '150px'},
                'snow3': {caption: '3/6 pair snow gallery', height: '50px'},
                'snow4': {caption: '4/8 pair snow gallery', height: '65px'},
                'stitches': {caption: 'Stitches gallery', height: '100px'}
            };
            const galleryKeys = Object.keys(galleries);
            for(i = 0; i<galleryKeys.length; i++){
                const key1 = galleryKeys[i];
                let options = ''
                galleryKeys.forEach(function (key2) {
                    if (key2 === key1) {
                        options += `<option value='${key2}' disabled selected>${galleries[key2]['caption']}</option>`;
                    } else {
                        options += `<option value='${key2}'>${galleries[key2]['caption']}</option>`;
                    }
                });
                const chooser = `<select onchange="GF_hybrid.otherGallery(this, ${i});">${options}</select>`;
                const sizeOptions = {width:'100%', height: galleries[key1]['height']};
                GF_panel.load({caption: chooser, id: key1, controls: ["resize"], size: sizeOptions, parent: container});
                document.getElementById(key1).parentNode.style.display = 'none';
            }
            GF_tiles.loadGallery({jsAction: 'GF_hybrid.setPattern(this);return false;', containerId: 'pattern'});
            createSnowGallery(GF_hybrid.snow3, 'snow3', `mix4snow`);
            createSnowGallery(GF_hybrid.snow4, 'snow4', `images/4-8-legs`);
            GF_stitches.loadStitchExamples("#stitches");
        }
        function twister(type) {
            return `<input type='number' min='0' max='3' value='0' id='${type}Step' name='${type}Step' title='droste step'></label>`;
        }
        function prefixedTwister(type){
            return `${type}s<label>, step: ${twister(type)}`;
        }
        const pairWandHref = "javascript:GF_hybrid.generateSelectedDiagram('pair');GF_hybrid.setStitchEvents()";
        const threadWandHref = "javascript:GF_hybrid.generateSelectedDiagram('thread')";
        const legendWandHref = "javascript:GF_hybrid.generateLegend()";
        let q = new URL(document.documentURI).search.slice(1)
            .replaceAll(/[^a-zA-Z0-9=,.&-]/g,'');
        if (q === "" || !q.includes('shiftRows')) {
            q = "patchWidth=7&patchHeight=7&footside=---x,---4,---x,---4&tile=5-,-5,5-,-5&headside=-,c,-,c,&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=2&e1=lclc&l2=llctt&f2=rcrc&d2=rrctt&e3=rcrc&l4=llctt&f4=lclc&d4=rrctt&droste2=e12=clcrcl,e13=ct,f42=ctcl,e32=f22=ctcr,e33=f43=lct,e31=f21=lctc,e11=rclcrc,f23=rct,f41=rctc,e10=tc,f20=tcl,e30=f40=tcr"
        }
        galleryPanels();
        GF_panel.load({caption: "tweak selected stitch", id: "tweak", size:{width:'98%', height: 'auto'}, parent: container});
        container.insertAdjacentHTML('beforeend',`
            <p>
                Assign tweaked stitch <button onclick="GF_hybrid.assignToAll()">to all</button>
                <button onclick="GF_hybrid.showToast('Assign to ignored is not yet implemented')" id="ignored">to ignored</button>
                or click a stich in the pair diagram.
                <a href="?${q}" id="selfRef" style="display:none;">Updated pattern</a>
            </p>
            <p>
                <label>Droste step number: ${twister("droste")}</label>
            </p>
            <div id="toast"></div>
        `);
        GF_panel.load({caption: prefixedTwister("pair"), id: "pair_panel", wandHref: pairWandHref, controls: ["resize"], parent: container});
        GF_panel.load({caption: prefixedTwister("thread"), id: "thread_panel", wandHref: threadWandHref, controls: ["resize", "color"], parent: container});
        GF_panel.load({caption: 'stitch enumeration', id: "legend_panel", controls: ["resize"], parent: container});
        GF_panel.load({caption: "specifications", id: "specs", controls: ["resize"], size:{width: '100%', height: '300px'}, parent: container});
        document.getElementById('tweak').insertAdjacentHTML('beforeend',`
            <p>
            ${this.basicStitch.htmlString()} <br>
            ${this.drosteOnBasicStitch.htmlString()}
            </p>
            <p>Flip:
            <button onclick="GF_hybrid.flip('b2d')">&harr;</button>
            <button onclick="GF_hybrid.flip('b2p')">&varr;</button>
            <button onclick="GF_hybrid.flip('b2d');GF_hybrid.flip('b2p')">both</button>
            </p>
        `);
        const params = new URLSearchParams(q);
        document.getElementById('tweak').parentNode.style = `width: calc(100% - 7px)`;
        document.getElementById('pairStep').value = params.get('pairStep') || 0;
        document.getElementById('threadStep').value = params.get('threadStep') || 1;
        const specsPanelContent = document.getElementById('specs');
        specsPanelContent.innerHTML = `
          <a href="javascript:['droste1','droste2','droste3'].forEach(GF_panel.cleanupStitches)" title="Reduce panel content"><img src="${this.content_home}/images/broom.png"></a>
          Specs collected from URL and clicks:
          <input type="text" id="droste0" value="${q}">
          <textarea id="droste1" spellcheck="false" placeholder="droste step 1, default all: ctc">${(params.get('droste2')||'').replaceAll(',','\n') || ''}</textarea>
          <textarea id="droste2" spellcheck="false" placeholder="droste step 2, default all: ctc">${(params.get('droste3')||'').replaceAll(',','\n') || ''}</textarea>
          <textarea id="droste3" spellcheck="false" placeholder="droste step 3, default all: ctc">${(params.get('droste4')||'').replaceAll(',','\n') || ''}</textarea>
        `;
        specsPanelContent.parentNode.style.display = "block";
        specsPanelContent.style.width = "100%";
        specsPanelContent.style.height = "0";
        for (let type of ["pair", "thread"]) {
            const panelEl = document.getElementById(type + '_panel');
            panelEl.innerHTML = "Click/tap the wand to (re)generate the diagram.";
            panelEl.style.color = "#bbbbbb";
        }

        function markDirty(id) {
            const panelIds = id === 'drosteStep'
                ? ['pair_panel', 'thread_panel']
                : [id.replace('Step', '') + '_panel'];

            panelIds.forEach(pid => {
                const panelEl = document.getElementById(pid);
                if (panelEl.getElementsByTagName('svg').length > 0)
                    panelEl.style.backgroundColor = "#f3f3f3";
            });
        }
        function stepNrChanged(e) {
            const val = parseInt(e.target.value, 10);
            // TODO: in case of a snow gallery: limit to 2
            const step = isNaN(val) ? 0 : Math.min(3, Math.max(0, val));
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
        document.getElementById('threadStep').addEventListener('change', stepNrChanged);
        document.getElementById('pairStep').addEventListener('change', e => {
            const step = stepNrChanged(e);
            setDisplayValues(step);
            document.getElementById('drosteStep').value = step;
        });
        document.getElementById('drosteStep').addEventListener('change', e => {
            const step = stepNrChanged(e);
            setDisplayValues(step);
            document.getElementById('threadStep').value = step;
            document.getElementById('pairStep').value = step;
        });
        console.log('================ Loaded panels ================');
    },
    setRecipe(basicStitch, drosteStitches) {
        document.getElementById('basicStitchInput').value = basicStitch;
        document.getElementById(GF_hybrid.drosteOnBasicStitch.id).value = drosteStitches;
        // TODO: second step of droste stitches, requires more intelligence in resetting previously assigned stitches
    },
    /**
     * Wrapper for loadSimple. Initial step is 1 and specs panel is shown immediately
     *
     * @param {!HTMLElement} container receives the generated components
     */
    loadDroste(container){
        this.loadSimple(container, 1, [GF_hybrid.drosteOnBasicStitch.id, 'pairStep', 'threadStep', 'snow3', 'snow4'] );
    },
    /**
     * Wrapper for loadSimple. Initial step is 0 and specs panel is initially hidden, shown when step becomes larger.
     *
     * @param {!HTMLElement} container receives the generated components
     * */
    loadStitches(container){
        this.loadSimple(container, 0, [GF_hybrid.drosteOnBasicStitch.id, 'pairStep', 'threadStep', 'snow3',  'snow4', 'drosteStep', 'specs'] );
    },
    /**
     * Wrapper for load. Hides the third step field
     *
     * @param {!HTMLElement} container receives the generated components
     * */
    loadDrosteMixer(container){
        this.load(container);
        document.getElementById('snow4').parentNode.style.display = 'block';
        document.getElementById('drosteStep').parentNode.style.display = 'none';
    },
    /**
     * Wrapper for load. Hiding some elements.
     */
    loadSimple(container, initialStep, hiddenElements){
        // Clear galleries that will not be used
        GF_tiles = {loadGallery (namedArgs){ }}; // dummy to avoid errors
        this.snow3 = []; // clear for performance
        this.snow4 = []; // clear for performance

        this.load(container, initialStep);
        for (let id of ['pairStep', 'threadStep', 'drosteStep']) {
            document.getElementById(id).value = initialStep;
        }
        for (let id of hiddenElements) {
            document.getElementById(id).parentNode.style.display = 'none';
        }
        document.getElementById('basicStitchInput').previousSibling.remove(); // remove label
        const stitchesEl = document.getElementById('stitches').parentNode;
        stitchesEl.style.display = 'block'; // make visible, whichever gallery is visible by default
        stitchesEl.getElementsByTagName('select')[0].outerHTML = 'select stitch example'; // no choice for other galleries
    },
    assignToAll() {
        const stepValue = document.getElementById('pairStep').value * 1;
        const stitchValue = document.getElementById('basicStitchInput').value;
        const stitchTitles = Array.from(document.getElementById('pair_panel')
            .getElementsByTagName('title')
        );
        if (document.getElementById(GF_hybrid.drosteOnBasicStitch.id).value.trim() !== '') {
            this.showToast("Assign to all is not implemented for droste applied to basic stitch")
        } else if (stepValue !== 0) {
            document.getElementById('droste' + stepValue).value =
                stitchValue; // default for this droste level
        } else if (!stitchTitles || stitchTitles.length === 0) {
            this.showToast("No stitches found in the pair diagram.")
        } else {
            document.getElementById('pair_panel').style.backgroundColor = "#f0f0f0"; // TODO share code with markDirty
            const d0 = document.getElementById('droste0');
            const params = new URLSearchParams(d0.value);
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
                    params.set(tag, stitchValue);
                }
            });
            d0.value = Array.from(params).map(([k, v]) => `${k}=${v}`).join('&');
            console.log("---------"+d0.value);

        }
    }
}