---
layout: default
title: Snowflakes
javascript:
  - d3.v4.min.js
  - GroundForge-opt.js
  - showGraph.js
---

![sample](sample.png?align=left)
This convenience form lets you replace groups of stitches at once.

The [default](?) diagram reconstructs a double hexagonal ring pair with an arbitrary snowflake,
the sample shown on the left is invented by Babette.

![](capture-extract.svg?align=right)
On the right an extract of the [default](?) template: four groups of stitches to replace.
The top left group matches the center hexagon below.
The faint arrows in the hexagons are duplicates of the dark ones, or the other way around.
Note that one group starts with the right two  pairs, the others start left.
[MAE-gf] has some examples for replacements and a recipe for more.
See also the color code [legend]. 

[MAE-gf]: /MAE-gf/docs/snow-stitches/#examples
[legend]: /GroundForge-help/color-rules

Click one of the hexagons to apply the stitches in the text field.

<script>{% include snow/hexa.js %}</script>
{% include snow/hexa.html %}

When satisfied with the result, you can follow the link to the _pairs from threads_ page to assign stitches for the actual snowflakes.
Even without that last step it can be fun to play with configurations of snowflakes.

Challenge for the form developer:  
Support a variety of replacement stitch counts and starting sides.
For that purpose it should be possible to combine any of the following elements into a valid template
and keep track of counts and directions.
With up to six stitches to construct a snowflake, we would get 256 templates.

![](plaits.svg)
