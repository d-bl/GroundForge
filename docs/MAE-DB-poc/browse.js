// Your JSON data
const data = {
    "tags": [
        {
            "value": "3-pair join",
            "description": "3-pair join"
        },
        {
            "value": "4-pair join"
        },
        {
            "value": "6-pair join"
        },
        {
            "value": "snowflake",
            "description": "Three out-coming sets of legs connect to three other snowflakes. As far as each set has an even number of pairs: a pin between each couple of two incoming/outgoing pairs. "
        },
        {
            "value": "spider",
            "description": "Two out-coming sets of legs connect to two other spiders"
        }
    ],
    "examples": [
        {
            "tags": ["6-pair join", "snowflake"],
            "image": "https://d-bl.github.io/MAE-gf/images/snow_6/bs-623451.png",
            "url": "https://d-bl.github.io/GroundForge/stitches?whiting=G4_P201&patchWidth=21&patchHeight=14&i1=ctctt&f1=ctc&e1=ctc&d1=ctc&b1=ctc&a1=ctctt&g2=ctcrr&i3=ctctt&f3=ctc&e3=ctc&d3=ctc&b3=ctcll&a3=ctctt&n4=ctctt&l4=ctctt&j4=ctctt&h4=ctctt&f4=ctt&d4=ctcll&c4=ctcll&b4=ctctt&g5=ctctt&c5=ctctt&n6=ctctt&j6=ctctt&m7=c&k7=ctc&j7=ctc&i7=ctctt&g7=ctctt&e7=ctctt&c7=ctctt&a7=ctctt&tile=56-o98-z5-----,------5-------,ag-aab-wd-----,-256-m-l-o-k-e,--5---5---y-w-,---w-y---b---c,h-g-5-n-l3h-e-,&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=-7&shiftRowsSW=7&shiftColsSE=7&shiftRowsSE=7"
        },
        {
            "tags": ["4-pair join", "spider"],
            "image": "https://d-bl.github.io/MAE-gf/images/spiders/s-4x2.png",
            "url": "https://d-bl.github.io/GroundForge/stitches?whiting=F2_P178&patchWidth=12&patchHeight=15&b1=ctcctc&a2=ctcll&c2=ctcrr&b3=ctctt&d3=ctc&a4=ctc&c4=ctc&tile=-5--,B-C-,-5-5,5-5-&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=-2&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=4"
        },
        {
            "tags": ["4-pair join", "snowflake"],
            "image": "https://d-bl.github.io/MAE-gf/images/snow/G38.svg",
            "url": "https://d-bl.github.io/GroundForge/stitches?patchWidth=20&patchHeight=20&e1=-&c1=ctc&a1=ctc&e2=-&b2=tctct&e3=ctctctc&c3=ctc&a3=ctc&tile=5-5-5-,x5x-r-,4-7-r-&footsideStitch=ctctt&tileStitch=ctctctc&headsideStitch=ctctt&shiftColsSW=-3&shiftRowsSW=3&shiftColsSE=3&shiftRowsSE=3"
        }
    ]
};

// Count tags
const tagCounts = {};
data.examples.forEach(e => {
    e.tags.forEach(tag => {
        tagCounts[tag] = (tagCounts[tag] || 0) + 1;
    });
});
const allTags = Object.keys(tagCounts);

// State for selected tag
let selectedTag = null;

// Build a map from tag value to description
const tagDescriptions = {};
data.tags.forEach(t => {
    tagDescriptions[t.value] = t.description || t.value;
});

// State for selected tags (multiple)
let selectedTags = [];

// Render facets with checkboxes
function getFilteredExamples() {
    return selectedTags.length
        ? data.examples.filter(e => selectedTags.every(tag => e.tags.includes(tag)))
        : data.examples;
}

function getFilteredTagCounts(filteredExamples) {
    const counts = {};
    filteredExamples.forEach(e => {
        e.tags.forEach(tag => {
            counts[tag] = (counts[tag] || 0) + 1;
        });
    });
    return counts;
}

function renderFacets() {
    const filteredExamples = getFilteredExamples();
    const filteredTagCounts = getFilteredTagCounts(filteredExamples);
    const filteredTags = Object.keys(filteredTagCounts);

    const facetsDiv = document.getElementById('facets');
    facetsDiv.innerHTML = filteredTags.map(tag =>
        `<p></p><label class="facet" title="">
            <input type="checkbox" data-tag="${tag}" ${selectedTags.includes(tag) ? 'checked' : ''}>
            <strong>${tag} (${filteredTagCounts[tag]})</strong>
            ${tagDescriptions[tag] || tag}
        </label></p>`
    ).join('');
}

function renderExamples() {
    const filteredExamples = getFilteredExamples();
    const examplesDiv = document.getElementById('examples');
    examplesDiv.innerHTML = filteredExamples.map(e =>
        `<div class="example">
            <img src="${e.image}" alt="Example" width="200"/>
            <a href="${e.url}">&infin;</a>
            ${e.tags.map(tag => `${tag}`).join(', ')}
        </div>`
    ).join('');
}

document.getElementById('facets').addEventListener('change', e => {
    if (e.target.type === 'checkbox') {
        const tag = e.target.dataset.tag;
        if (e.target.checked) {
            if (!selectedTags.includes(tag)) selectedTags.push(tag);
        } else {
            selectedTags = selectedTags.filter(t => t !== tag);
        }
        renderFacets();
        renderExamples();
    }
});

// Initial render
renderFacets();
renderExamples();

// Event delegation for facet clicks
document.getElementById('facets').addEventListener('click', e => {
    if (e.target.classList.contains('facet')) {
        selectedTag = e.target.dataset.tag === selectedTag ? null : e.target.dataset.tag;
        renderFacets();
        renderExamples();
    }
});

// Initial render
renderFacets();
renderExamples();
