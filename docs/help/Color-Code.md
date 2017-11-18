---
layout: default
title: Color Code
---

Color Code
==========

- [Color code dialects](#color-code-dialects)
- [Twist marks by GroundForge](#twist-marks-by-groundforge)
- [Colors by GroundForge](#colors-by-groundforge)
- [Tweak Colors](#tweak-colors)


Color code dialects
-------------------

Various dialects of color codes for pair diagrams are used by bobbin lace designers:
a [Belgian] version, a simplified [Danish] version, a [red-blue] version and perhaps more.
Allowing -even encouraging- unorthodox stitches, the liberty to use both open and closed stitches,
poor or no support for tallies, pins and gimps in GroundForge and other limitations asks for yet another dialect.

[Belgian]: https://www.mail-archive.com/lace@arachne.com/msg51345.html
[Danish]: https://www.mail-archive.com/lace@arachne.com/msg51355.html
[red-blue]: http://susanroberts.info/Working%20diagrams%20-%20part%202.pdf


Twist marks by GroundForge
--------------------------

The intention is a twist mark wherever there are multiple twists in a pair.
Open stitches abide this rule. Closed stitches lack twist marks if just one of the pairs need one.

Follow the progress of issue [#104](https://github.com/d-bl/GroundForge/issues/104).

Colors by GroundForge
---------------------

A stitch-pin-stitch is not drawn as "ꅉ" (pins are hardly supported anyway) and 
a plait in a pair diagram is not drawn as ">---<" but both as "X".

stitch name         | color      | examples and notes (neither are exhaustive)
--------------------|------------|--------------------------------------------
half stitch         | green      | `ct`, `tc`, `ttc`, `tct` (just one time `c` and both pairs twisted at least once)
cloth stitch        | purple     | `ctc`, `ctcl`, `rctc` (just `ctc` and at most on pair twisted)
2x half stitches    | red        | `ctct`, `tctc`, `ctct` (just `ctc` and both pairs twisted at least once)
plait               | blue       | `ctctc` (a `c` followed by at least two times `tc`)
turning stitch      | turquoise  | `cttc` (no more than two times `c`)
,, variants         | brown      | `cllc`, `crrc`, `ctlc`, `ctrc`

Stitches not matching anything above will be black.


Tweak Colors
------------

A pair diagram has a predefined palette of colors applied to stitches. You can tweak colors to accommodate your monitor, printer or color-blindness.

Download a pair diagram and open it with some plain text editor (or show it full screen and right click to show the page source), you will see a start/end marker definition for each color except black, something like:
```xml
<svg ...>
  <g>
    <defs>
      ...
      <marker id="start-red" ...><path ... stroke="#f00"></path></marker>
      ...
    </defs>
  </g>
  ...
</svg>
```

Change the stroke value to adjust a color, several color choosers on the web can provide a RGB value. Save the changes and open the file again in your browser or SVG editor and you will see the adjusted colors all over the diagram.
