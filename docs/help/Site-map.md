---
layout: default
title: Site Map
---

Site Map
========

The arrows in the diagram represent links between pages.
The page names (or address fragments) link to examples.
A legend and more notes follow the overview.

{% include site-map.svg %}


index.html
----------

The main page can reuse thread diagrams as pair diagrams up to two times.
It has a workaround for a bug in IE-11, the Edge browser doesn't have this bug.


tiles.html
----------

Choosing stitches or thread colors is more intuitive.
Zooming and [untangling] is not supported,
instead you can adjust the space occupied on the page.

[untangling]: https://github.com/d-bl/GroundForge/releases/download/2017-06-05/untangle.mp4

Diagram properties
------------------

Page            | Thread diagrams | Pair diagrams                   | Tiling    | Foot sides         
----------------|-----------------|---------------------------------|-----------|--------------------
index.html      | any color       | color coded / rounded           | simple    | generated          
tiles.html      | black/red       | color coded / squared + rounded | complex   | fringes or custom defined 
sheet.html      | -               | black &amp; white / squared     | simple    | -                  
tesselace-index | -               | black &amp; white / rounded     | n.a.      | -                  

Squared : stitches are placed on square grid positions<br>
Rounded : an animated process makes the holes of a squared pattern as round as possible<br>
Many squared diagrams can result in the same rounded diagram.

### Tiling

A tile is a rectangular grid section with stitches.

Withe simple tiling these are repeated in both directions as on a checker board, 
or horizontally shifted by a half rectangle as in a brick wall. 

With complex tiling the rectangles can be shifted either horizontally
or vertically by any number of stitches,
or the rectangles partially overlap each others corners. 

Patterns expressed with complex tiles can be expressed with simple tiles
but will require larger tiles.
As a consequence the same stitch should be assigned multiple times.