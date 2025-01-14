---
layout: default
title: GF; Mix Snowflakes
javascript:
  - d3.v4.min.js
  - GroundForge-opt.js
  - showGraph.js
  - nudgePairs.js
---

Mix four snowflakes
===================

<script>{% include mix4snow/hexa.js %}</script>
{% include mix4snow/hexa.html %}

<fieldset><legend>Select a recipe</legend>
<a href="javascript:recipe('tcl,rctc,crl,r',true)" title="family 456123"><img src="diamond.png" alt="diamond"></a> &nbsp;
<a href="javascript:recipe('tc,rclcrc,clcrcl,ct',true)" title="family 321"><img src="star.png" alt="star"></a> &nbsp;
<a href="javascript:recipe('ctc,ctcl,ctc,ctc',true)" title="family 651234"><img src="leaning-spider.png" alt="leaning crossed spider"></a> &nbsp;
<a href="javascript:recipe('cr,ctc,ctc,lc',false)" title="family 321"><img src="ring-s.png" alt="ring │"></a> &nbsp;
<a href="javascript:recipe('cl,ctc,ctc,rc',false)" title="family 321"><img src="ring-e.png" alt="ring ╱"></a> &nbsp;
<a href="javascript:recipe('tctc,rctc,ctcl,t',true)" title="family 321"><img src="triangle.png" alt="triangle"></a> &nbsp;
<a href="javascript:recipe('crc,crclctc,ctcrc,rcl,c,c',false)" title="family 123"><img src="weaving-4x4.png" alt="weaving 4x4"></a> &nbsp;
<a href="javascript:recipe('lc,crc,ctc,lcrcl,ctc,crc,c,r',false)" title="family 623451"><img src="spider-2heads.png" alt="spider with 2 heads"></a> &nbsp;
</fieldset>
<br>
The same set of four colors serve two independent purposes.
The colors of the hexagons match groups of stitches in the thread diagram.
The pair diagram has a [color-code] that can express unorthodox stitches.
Clicking a stitch in the pair diagram de-colors the corresponding stitch in the thread diagram

A [tutorial] not only explains how to modify and create recipes, 
but also how to choose stitches for six pair snowflakes and see their flow of threads.

[color-code]: /GroundForge-help/color-rules
[tutorial]: /GroundForge-help/snow-mix
