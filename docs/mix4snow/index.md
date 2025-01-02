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

The colors of the hexagons match the thread diagram.
The pair diagram has a [color code] that caters for unorthodox stitches.

[color code]: /GroundForge-help/color-rules
[saved PDF]: /GroundForge-help/clips/print-as-pdf

<script>{% include mix4snow/hexa.js %}</script>
{% include mix4snow/hexa.html %}

* Click one of the hexagons to replace the corresponding group in the diagrams with the recipe.
* Click an image below to select its recipe.<!-- true: start left -->  
[![diamond](diamond.png)](javascript:recipe('tcl,rctc,crl,r',true) "family 456123") &nbsp;
[![star](star.png)](javascript:recipe('tc,rclcrc,clcrcl,ct',true) "family 321") &nbsp;
[![leaning crossed spider](leaning-spider.png)](javascript:recipe('ctc,ctcl,ctc,ctc',true) "family 651234") &nbsp;
[![ring │](ring-s.png)](javascript:recipe('cr,ctc,ctc,lc',false) "family 321") &nbsp;
[![ring ╱](ring-e.png)](javascript:recipe('cl,ctc,ctc,rc',false) "family 321") &nbsp;
[![triangle.png](triangle.png)](javascript:recipe('tctc,rctc,ctcl,t',true) "family 321") &nbsp;
[![weaving 4x4](weaving-4x4.png)](javascript:recipe('crc,crclctc,ctcrc,rcl,c,c',false) "family 123") &nbsp;
[![spider with 2 heads](spider-2heads.png)](javascript:recipe('lc,crc,ctc,lcrcl,ctc,crc,c,r',false) "family 623451") &nbsp;

See the <a href="/GroundForge-help/snow-mix" target="_blank">tutorial</a>
for more information and (how to create) more recipes.
