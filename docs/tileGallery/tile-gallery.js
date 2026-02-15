GF_tiles = {
    content_home: '/GroundForge',
    showPreviews(clickedElement){
        const previewDiv = document.getElementById('previews');
        previewDiv.innerHTML = '';
        Array.from(clickedElement.parentElement.children)
            .filter(el => el.tagName.toLowerCase() === 'a')
            .forEach(element => {
                const q = element.getAttribute('xlink:href').split('?')[1];
                const panelId = `preview_${(element.textContent)}`;
                GF_panel.load({id: panelId, parent: previewDiv, caption: `
                    ${element.textContent.trim()}: change&nbsp;
                    <a href="${element.getAttribute('href')}">pattern</a>&nbsp;or&nbsp;
                    <a href="${element.getAttribute('href').replace(/pattern.html/, 'stitches.html')}">stitches</a>&nbsp;
                `});
                GF_panel.diagramSVG({id: panelId, query: q, type: 'pair'});
                const diagram = document.getElementById(panelId);
                diagram.style.resize = 'none';
                diagram.style.overflow = 'hidden';
                diagram.style.width = '182px';
                diagram.style.height = '166px';
                diagram.querySelector(':scope > svg > g')
                    .setAttribute('transform','scale(1.3) translate(-65,-18)');
            })
        GF_panel.scrollIfTooLittleIsVisible(previewDiv);
        return false;
    },
    load(parent = document.body) {
        GF_panel.load({caption: "w.i.p.", id: "patterns", controls: ["resize"], size:{width:'310px', height: '300px'}, parent: parent});
        parent.insertAdjacentHTML('beforeend', `<div id="previews"></div>`);
        this.loadGallery({});
    },
    loadGallery(namedArgs) {
        const {
            jsAction = 'GF_tiles.showPreviews(this)',
            containerId = 'patterns'
        } = namedArgs;
        const svg = `${this.content_home}/tileGallery/index.svg`;
        fetch(svg) // as <img src> it would not be part of the dom
            .then(response => response.text())
            .then(svg => {
                const containerEl = document.getElementById(containerId);
                containerEl.insertAdjacentHTML('beforeend', svg);
                const svgEl = containerEl.querySelector(`:scope > svg`);
                svgEl.querySelectorAll(`:scope a`).forEach(el => {
                    const link = el.getAttribute('xlink:href');
                    if (link !== null) {
                        el.setAttribute('href', link.replace(/.*io.GroundForge/, '/GroundForge'));
                        if (link.includes('?')) {
                            el.setAttribute('onclick', `javascript:${jsAction};return false;`);
                        }
                    }
                })
                const units = svgEl.getAttribute('width').replace(/[0-9]/g, '');
                const w = svgEl.getAttribute('width').replace(/[^0-9]/g, '');
                const h = svgEl.getAttribute('height').replace(/[^0-9]/g, '');
                // scale by changing page dimensions
                svgEl.setAttribute('width', (w*0.65)+units);
                svgEl.setAttribute('height', (h*0.65)+units)
            });
    }
};
