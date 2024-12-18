---
layout: default
title: Snowflakes
javascript:
  - d3.v4.min.js
  - GroundForge-opt.js
  - showGraph.js
  - nudgePairs.js
---

Mix three/six-pair connections
==============================

![sample](sample.png?align=left)
On the left a sample of lace by Babette.
She invented a double hexagonal ring pair around a snowflake.
The intersections of the double ring pairs are in fact compact snowflakes.
This inspired to explore a mix of snowflakes in general. 

The pair diagram of a six pair snowflake can be interpreted
as a thread diagram of a three pair connection. 
Each three pair connection can be composed with a group of two pair stitches.
The form below starts with the last step to provide building blocks for a combination of four snowflakes. 

![](capture-extract.svg?align=right)
On the right the first repeat of the [default](?) template:
four groups of stitches with three pairs.
The groups in this example reconstruct a double hexagonal ring pair with an arbitrary snowflake.
The colors of the groups match the hexagons below and the thread diagram.
The pair diagram has a [color code] that caters for unorthodox stitches.

Click one of the hexagons to replace the groups in the diagrams.
An odd number of stitches (minimal three) needs a [workaround].
We support up to 10 stitches.

[MAE-gf]: /MAE-gf/docs/snow-stitches/#examples
[color code]: /GroundForge-help/color-rules
[workaround]: https://github.com/d-bl/GroundForge/blob/master/docs/_includes/snow/README.md#odd-number-of-stitches
[saved PDF]: /GroundForge-help/clips/print-as-pdf

<script>{% include mix4snow/hexa.js %}</script>
{% include mix4snow/hexa.html %}

Click an image below to assign its recipe to the form:<!-- true: start left -->  
[![weaving-4x4](weaving-4x4.png)](javascript:recipe('crc,crclctc,ctcrc,rcl,c,c',false) "family 123") &nbsp;
[![triangle.png](triangle.png)](javascript:recipe('ctc,ctc,ctcl,t',true) "family 321") &nbsp;
[![star](star.png)](javascript:recipe('tc,rclcrc,clcrcl,ct',true) "family 321") &nbsp;
[![square](square.png)](javascript:recipe('ttrcl,rrctc,crll,tt',true) "family 456123") &nbsp;
[![╲](ring-w.png)](javascript:recipe('cr,ctc,ctc,lc',true) "family 321")
[![│](ring-s.png)](javascript:recipe('cr,ctc,ctcr,lc',false) "family 321") &nbsp;
[![╱](ring-e.png)](javascript:recipe('cl,ctc,ctc,rc',false) "family 321") &nbsp;

A catalog with recipes can barely scratch the surface of the possibilities.
We have a few more [here](/MAE-gf/docs/snow-stitches/#examples)
and [here](/MAE-gf/docs/misca#3-paired-join).
and instructions to [create](/MAE-gf/docs/snow-stitches/#pair-diagrams-interpreted-as-thread-diagrams-with-blobs)
your own recipes from pair diagrams you find in literature.
Clicking a stitch in the pair diagram will remove the color from the corresponding stitch in the thread diagram.
This can help to trouble shoot a recipe.

When satisfied with the result, you can follow the link to the _pairs from threads_ page
to assign stitches for the actual snowflakes.
Even without that last step it can be fun to play with configurations of snowflakes.

Hint: print this page as PDF to collect your own recipes. 
Reopen in the browser to copy-paste the text field.

