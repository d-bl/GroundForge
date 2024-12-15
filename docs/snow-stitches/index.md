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
A thread diagram of a three pair connection can serve as a 
pair diagram for a six-pair connection alias snowflake.
Each connection is composed with a group of two pair stitches.
A convenience form lets you replace such a group at once.

The [default](?) diagram reconstructs a double hexagonal ring pair with an arbitrary snowflake.
This double ring is invented by Babette, a sample is shown on the left.

![](capture-extract.svg?align=right)
On the right the first repeat of the [default](?) template: 
four groups of stitches with three pairs, you can replace them one by one through the form.
The colors match the hexagons below and the thread diagram.
The pair diagram has a [color code] that caters for unorthodox stitches.

Click one of the hexagons to apply the stitches in the text field.
An odd number of stitches needs a [workaround].
Just a few recipes for the form [here](/MAE-gf/docs/snow-stitches/563412-145236.svg)
and [here](/MAE-gf/docs/misca#3-paired-join),
we suggest to create and collect your own [recipes](/MAE-gf/docs/snow-stitches/#pair-diagrams-interpreted-as-thread-diagrams-with-blobs).

[MAE-gf]: /MAE-gf/docs/snow-stitches/#examples
[color code]: /GroundForge-help/color-rules
[workaround]: https://github.com/d-bl/GroundForge/blob/master/docs/_includes/snow/README.md#odd-number-of-stitches
[saved PDF]: /GroundForge-help/clips/print-as-pdf

<script>{% include snow/hexa.js %}</script>
{% include snow/hexa.html %}

When satisfied with the result, you can follow the link to the _pairs from threads_ page to assign stitches for the actual snowflakes.
Even without that last step it can be fun to play with configurations of snowflakes.

Links in a [saved PDF] may lead you back to the _stitches_ or _droste_ page. 
For patterns created by this page, you can replace that part in the address bar
with _snow_stitches_ to return to this page.

