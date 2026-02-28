GF_tiles = {
    content_home: '/GroundForge',
    gallery: [
        "index.svg"
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
                const hrefAttr = element.getAttribute('href');
                GF_panel.load({
                    id: safePanelId,
                    parent: previewDiv,
                    caption: `
                    ${text}: change&nbsp;
                    <a href="${hrefAttr}">pattern</a>&nbsp;or&nbsp;
                    <a href="${(hrefAttr||'').replace(/pattern.html/, 'stitches.html')}">stitches</a>&nbsp;
                    `
                });
                GF_panel.diagramSVG({id: safePanelId, query: q, type: 'pair'});
                const diagram = document.getElementById(safePanelId);
                if (!diagram) return;
                diagram.style.resize = 'none';
                diagram.style.overflow = 'hidden';
                diagram.style.width = '182px';
                diagram.style.height = '166px';
                const g = diagram.querySelector(':scope > svg > g');
                if (g) {
                    g.setAttribute('transform','scale(1.3) translate(-65,-18)');
                }
            });
        GF_panel.scrollIfTooLittleIsVisible(previewDiv);
        return false;
    },
    load(parent = document.body) {
        GF_panel.load({caption: "w.i.p.", id: "patterns", controls: ["resize"], size:{width:'310px', height: '300px'}, parent: parent});
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
