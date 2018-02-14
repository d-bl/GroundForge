---
layout: default
title: Site Map
---

Site Map
========

The  diagram gives an overview of the available pages
and how to navigate between them.
The page names are linked to examples.
A legend and some notes follow the overview.

{% include site-map.svg %}


index
-----

The main page can reuse thread diagrams as pair diagrams up to two times.
It has a workaround for a bug in IE-11, the Edge browser doesn't have this bug.
Currently you might have to assign stitches for multiple repeats of a pattern.
No custom foot sides.


tiles
-----

Choosing stitches or thread colors is more intuitive.
A bug may assign the chosen stitches incorrectly in the animated diagrams,
until fixed, stitches are not saved with pattern links.


Diagrams
--------

Page            | Thread diagrams | Pair diagrams
----------------|-----------------|-----------------------
index           | any color       | color coded / rounded
tiles           | black/red       | color coded / squared + rounded
sheet           | -               | black&white / squared
tesselace-index | -               | black&white / rounded

Squared : stitches are placed on square grid positions<br>
Rounded : an animation creates round holes from a squared pattern<br>
Many squared diagrams can result in the same rounded diagram.
