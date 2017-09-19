Color code dialects
-------------------

Various dialects of color codes for pair diagrams are used by bobbin lace designers:
a [Belgian] version, a simplified [Danish] version, a [red-blue] version and perhaps more.
Allowing -even encouraging- unorthodox stitches, the liberty to use both open and closed stitches
and poor or no support for tallies, pins and gimps in GroundForge asks for yet another dialect,
this dialect is still under construction,
see [issue#49](https://github.com/d-bl/GroundForge/issues/49).

[Belgian]: https://www.mail-archive.com/lace@arachne.com/msg51345.html
[Danish]: https://www.mail-archive.com/lace@arachne.com/msg51355.html
[red-blue]: http://susanroberts.info/Working%20diagrams%20-%20part%202.pdf


Twist marks by GroundForge
--------------------------

The intention is a twist mark wherever there are multiple twists in a pair.
Open stitches abide this rule. Closed stitches lack twist marks if just one of the pairs need one.

See [issue#104]( https://github.com/d-bl/GroundForge/issues/104)


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