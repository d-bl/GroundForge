GF_tiles = {
    content_home: '/GroundForge',
    gallery: [
        "H2Checker.svg",
        "H4Bathroom.svg",
        "H4Checker.svg",
        "D1.svg",
        "D2Metro.svg",
        "D2Diagonal.svg",
        "D3Metro.svg",
        "D3Bathroom.svg",
        "D4Metro.svg",
        "D4Checker.svg",
        "D4Bathroom.svg",
        "D4Diamonds.svg",
        "D4Steps.svg",
        "D5Steps.svg"
    ],
    showPreviews(clickedElement) {
        const previewDiv = document.getElementById('previews');
        if (!previewDiv || !clickedElement || !clickedElement.parentElement) return false;
        previewDiv.innerHTML = '';
        Array.from(clickedElement.parentElement.children)
            .filter(el => el.tagName && el.tagName.toLowerCase() === 'a')
            .forEach(element => {
                const xlink = element.getAttribute('xlink:href');
                if (!xlink || !xlink.includes('?')) return;
                const q = xlink.split('?')[1];
                const text = (element.textContent || '').trim();
                const safePanelId = `preview_${text.replace(/[^\w-]/g, '_')}`;
                GF_panel.load({
                    id: safePanelId,
                    parent: previewDiv,
                    size:{width:'360px', height: '200px'},
                    caption: `
                    ${text}: change&nbsp;
                    <a href="${(xlink||'').replace(/pattern.html/, 'stitches.html')}" target="_blank">stitches</a>&nbsp;
                    &nbsp;or&nbsp;
                    <a href="${xlink}" target="_blank">pattern</a>
                    `
                });
                GF_panel.diagramSVG({id: safePanelId, query: q, type: 'pair'});
                const panelContent = document.getElementById(safePanelId);
                if (!panelContent) return;
                panelContent.style.resize = 'none';
                panelContent.style.overflow = 'hidden';
                panelContent.lastElementChild.querySelector(':scope > g')
                    .setAttribute('transform','scale(1.3) translate(-65,-18)');
                panelContent.lastElementChild.setAttribute('width', 182);
                panelContent.lastElementChild.setAttribute('height', 166);

                panelContent.insertAdjacentHTML('beforeend', PrototypeDiagram.create(TilesConfig(q)));
                panelContent.lastElementChild.getElementById("layer1")
                    .setAttribute('transform','translate(-90,-1900) scale(2.0)');
                panelContent.lastElementChild.setAttribute('width', 170);
                panelContent.lastElementChild.setAttribute('height', 210);
                panelContent.lastElementChild.querySelectorAll('foreignObject')
                    .forEach(foreignObj => foreignObj.remove());
            });
        GF_panel.scrollIfTooLittleIsVisible(previewDiv);
        return false;
    },
    load(parent = document.body) {
        GF_panel.load({caption: "configurations", id: "patterns", controls: ["resize"], size:{width:'100%', height: '300px'}, parent: parent});
        parent.insertAdjacentHTML('beforeend', `<div id="previews"></div>`);
        this.loadGallery({});
    },
    async loadGallery(namedArgs) {
        const {
            jsAction = 'GF_tiles.showPreviews(this)',
            containerId = 'patterns'
        } = namedArgs;
        const containerEl = document.getElementById(containerId);
        for (const svg of this.gallery) {
            // as <img src> it would not be part of the dom
            const response = await fetch(`${this.content_home}/tileGallery/${svg}`);
            const svgText = await response.text();
            containerEl.insertAdjacentHTML('beforeend', svgText);
            const svgEl = containerEl.lastElementChild;
            svgEl.querySelectorAll(`:scope a`).forEach(el => {
                const link = el.getAttribute('xlink:href');
                if (link !== null) {
                    el.setAttribute('xlink:href', link.replace(/.*io.GroundForge/, '/GroundForge'));
                    if (link.includes('?')) {
                        el.setAttribute('onclick', `javascript:${jsAction};return false;`);
                    }
                }
            });
            svgEl.setAttribute('width', "100");
            svgEl.removeAttribute('height');
        }
    }
};
