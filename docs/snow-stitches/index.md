---
layout: default
title: Snowflakes
javascript:
  - d3.v4.min.js
  - GroundForge-opt.js
  - showGraph.js
  - nudgePairs.js
---

three/six-pair connections
==========================

![sample](sample.png?align=left)
A thread diagram of a three pair connection can serve as a pair diagram for a six-pair connection alias snowflake.
Each connection is composed with a group of two pair stitches. A convenience form lets you replace such a group of at once.

The [default](?) diagram reconstructs a double hexagonal ring pair with an arbitrary snowflake.
This double ring is invented by Babette, a sample is shown on the left.

![](capture-extract.svg?align=right)
On the right the first repeat of the [default](?) template: 
four groups of stitches to replace with the content of the text field.
The colors match the hexagons below and the thread diagram.
Note that the light blue group starts with the left two  pairs, the others start right.
The pair diagram has a [color code] that caters for unorthodox stitches.

[MAE-gf] has some examples for replacements to put in the text field and a recipe for more.
Click one of the hexagons to apply the stitches in the text field.
An odd number of stitches needs a [workaround].

[MAE-gf]: /MAE-gf/docs/snow-stitches/#examples
[color code]: /GroundForge-help/color-rules
[workaround]: https://github.com/d-bl/GroundForge/blob/master/docs/_includes/snow/README.md#odd-number-of-stitches

<script>{% include snow/hexa.js %}</script>
{% include snow/hexa.html %}

When satisfied with the result, you can follow the link to the _pairs from threads_ page to assign stitches for the actual snowflakes.
Even without that last step it can be fun to play with configurations of snowflakes.

