Color code
----------

See [issue#49](https://github.com/d-bl/GroundForge/issues/49)


Twist marks
-----------

The intention is a twist mark wherever there are multiple twists in a pair.
Open stitches abide this rule. Closed stitches lack twist marks if just one of the pairs need one.

See [issue#104]( https://github.com/d-bl/GroundForge/issues/104)


Tweak Colors
------------

A pair diagram has a predefined palette of colors applied to stitches. You can tweak colors to accomodate your monitor, printer or color-blindness.

Download a pair diagram and open it with some plain text editor (or show it full screen and right click to show the page source), you will see a start marker and an end marker for each color except black, something like:
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

Change the stroke value to adjust a color, several color choosers on the web can provide you a RGB value. Save the changes and open the file again in your browser or SVG editor and you will see the adjusted colors all over the diagram.