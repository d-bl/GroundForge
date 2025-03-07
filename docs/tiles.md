---
layout: default
title: GroundForge - obsolete 
---

In April 2023, the tiles page was split and in December 2024 the content reduced to the following links:

* <a href="/GroundForge/pattern" id="pattern">pattern</a> - (re)assemble the pair diagram from scratch.
* <a href="/GroundForge/stitches" id="stitches">stitches</a> - choose stitches on the pair diagram to generate a thread diagram. 
* <a href="/GroundForge/droste" id="droste">droste</a> - use thread diagram as pair diagram.

The new pages link back and forth to one another, preserving the pattern definitions.
Following the links above also preserves the original pattern definition.

Other changes

* Pair diagrams have a new [color code] (on the droste page since the first of January 2025).
* Each page has a button that reveals relevant help pages.
  Some help subjects have half minute silent screen recordings.
* The pages are made [print friendly] by themselves: clutter is removed.
  You can also use the print function of the browser to save the diagrams as a PDF document. 
  A link in the document will bring you back to the current state of the pattern except for colors in threads digrams.
* The top lines in the banner (and the home button at the top of the sidebar)
  always leads to the landing page with an overview of the tools and galleries.

[color code]: /GroundForge-help/color-rules
[print friendly]: /GroundForge-help/clips/print-as-pdf

<script>
document.addEventListener('DOMContentLoaded', (event) => {
    var q = document.URL.split('?')[1];
    if (q) {
        document.getElementById('pattern').setAttribute("href", "pattern?" + q);
        document.getElementById('stitches').setAttribute("href", "stitches?" + q);
        document.getElementById('droste').setAttribute("href", "droste?" + q);
    }
})
</script>
