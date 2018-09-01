---
layout: default
title: Site Map
---

Site Map
========

The arrows in the diagram represent links between pages.
The page names (or address fragments) link to examples.
A legend and more notes follow the overview.

Note that many of the help pages listed in the side bar are about the `index` page.
The new `tiles` page links to its own help page, which is not yet in the side bar.

{% include site-map.svg %}


index
-----

The main page can reuse thread diagrams as pair diagrams up to two times.
It has a workaround for a bug in IE-11, the Edge browser doesn't have this bug.
Currently you might have to assign stitches for multiple repeats of a pattern.
No custom foot sides.


tiles
-----

Choosing stitches or thread colors is more intuitive
but as a consequence only black and red for thread colors.
Zooming and [untangling] is not supported,
instead you can adjust the space occupied on the page.

[untangling]: https://github.com/d-bl/GroundForge/releases/download/2017-06-05/untangle.mp4

Diagrams
--------

Page            | Thread diagrams | Pair diagrams
----------------|-----------------|-----------------------
index           | any color       | color coded / rounded
tiles           | black/red       | color coded / squared + rounded
sheet           | -               | black &amp; white / squared
tesselace-index | -               | black &amp; white / rounded

Squared : stitches are placed on square grid positions<br>
Rounded : an animated proces makes the holes of a squared pattern as round as possible<br>
Many squared diagrams can result in the same rounded diagram.
