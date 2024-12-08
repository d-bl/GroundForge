---
layout: default
title: Snowflakes
javascript:
  - d3.v4.min.js
  - GroundForge-opt.js
  - showGraph.js
  - nudgePairs.js
---

Compositions of 3/6-pair connections.
=====================================

![sample](sample.png?align=left)
A thread diagram of a three pair connection can serve as a pair diagram for a six-pair connection alias snowflake.
A convenience form lets you replace the stitches that composes a three pair connection at once.

The [default](?) diagram reconstructs a double hexagonal ring pair with an arbitrary snowflake,
the sample shown on the left is invented by Babette.

![](capture-extract.svg?align=right)
On the right the first repeat of the [default](?) template: four groups of stitches to replace with the content of the text field.
The colors match the hexagons below.
Note that the light blue group starts with the right two  pairs, the others start left.
[MAE-gf] has some examples for replacements and a recipe for more.
The pair diagram has a [color code] that caters for unorthodox stitches. 
Otherwise, corresponding elements in different diagrams have the same color. 

[MAE-gf]: /MAE-gf/docs/snow-stitches/#examples
[color code]: /GroundForge-help/color-rules

Click one of the hexagons to apply the stitches in the text field.

<script>{% include snow/hexa.js %}</script>
{% include snow/hexa.html %}

When satisfied with the result, you can follow the link to the _pairs from threads_ page to assign stitches for the actual snowflakes.
Even without that last step it can be fun to play with configurations of snowflakes.

Challenge for the form developer:  
Support a variety of replacement stitch counts and starting sides.
For that purpose it should be possible to combine any of the following elements into a valid template
and keep track of counts and directions.

![](plaits.svg)
