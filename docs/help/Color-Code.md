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

<sup><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></sup>


Color code dialects
-------------------

Various dialects of color codes for pair diagrams are used by bobbin lace designers:
a [Belgian] version, a simplified [Danish] version, a [red-blue] version and perhaps more.
Allowing -even encouraging- unorthodox stitches, the liberty to use both open and closed stitches,
poor or no support for tallies, pins and gimps in GroundForge and last but not least
only "X" not ">&odot;<" for stitches asks for yet another dialect.

[Belgian]: https://www.mail-archive.com/lace@arachne.com/msg51345.html
[Danish]: https://www.mail-archive.com/lace@arachne.com/msg51355.html
[red-blue]: http://susanroberts.info/Working%20diagrams%20-%20part%202.pdf


Twist marks by GroundForge
--------------------------

The intention is a twist mark wherever there are multiple twists in a pair.
Open stitches abide this rule. Closed stitches lack twist marks if just one of the pairs need one.

Follow the progress of issue [#104]( https://github.com/d-bl/GroundForge/issues/104).

Colors by GroundForge
---------------------

This dialect is still under construction,
follow the progress of pull request [#106](https://github.com/d-bl/GroundForge/pull/106).

Anyway a plait in a pair diagram is drawn as `X`, not as `>---<`.


Tweak Colors
------------

A pair diagram has a predefined palette of colors applied to stitches. You can tweak colors to accomodate your monitor, printer or color-blindness.

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