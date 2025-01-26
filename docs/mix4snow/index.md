---
layout: default
title: Mix Snowflakes
javascript:
  - d3.v4.min.js
  - GroundForge-opt.js
  - showGraph.js
  - nudgePairs.js
---

<script>{% include mix4snow/hexa.js %}</script>
{% include mix4snow/hexa.html %}

<fieldset style="background: #FFF"><legend>Select a recipe</legend>
<a href="javascript:recipe('crc,crclctc,ctcrc,rcl,c,c',false)" title="family 123"><img src="weaving-4x4.png" alt="weaving 4x4"></a> &nbsp;
<a href="javascript:recipe('tc,rclcrc,clcrcl,ct',true)" title="family 321"><img src="star.png" alt="star"></a> &nbsp;
<a href="javascript:recipe('tcr,lctc,ctcr,lct',false)" title="family 321"><img src="ring-s.png" alt="ring │"></a> &nbsp;
<a href="javascript:recipe('tcl,lctc,ctcr,rct',false)" title="family 321"><img src="ring-e.png" alt="ring ╱"></a> &nbsp;
<a href="javascript:recipe('tctc,rctc,ctcl,t',true)" title="family 321"><img src="triangle.png" alt="triangle"></a> &nbsp;
<a href="javascript:recipe('lc,crclclc,crcrclc,cr',false)" title="family 426153"><img src="open.png" alt="open"></a> &nbsp;
<a href="javascript:recipe('tcl,rctc,crl,r',true)" title="family 456123"><img src="diamond.png" alt="diamond"></a> &nbsp;
<a href="javascript:recipe('c,ctc,rclc,ctc,rc,rcl,ctc,c',false)" title="family 456123"><img src="dogwood.png" alt="dogwood"></a> &nbsp;
<a href="javascript:recipe('lcr,ctclc,crclc,cr',false)" title="family 531642"><img src="acorn.png" alt="acorn"></a> &nbsp;
<a href="javascript:recipe('lcrc,clcrc,clcrc,clcr',false)" title="family 564312"><img src="flanders.png" alt="ringed flanders"></a> &nbsp;
<a href="javascript:recipe('lc,crc,ctc,lcrcl,ctc,crc,c,r',false)" title="family 623451"><img src="spider-2heads.png" alt="spider with 2 heads"></a> &nbsp;
<a href="javascript:recipe('tctc,rctcl,ctcl,ctct',true)" title="family 651234"><img src="leaning-spider.png" alt="leaning crossed spider"></a> &nbsp;
</fieldset>
<br>

The same set of four colors serves two independent purposes.
The colors of the hexagons match groups of stitches in the thread diagram.
The pair diagram has a [color-code] that can express unorthodox stitches.

Click a stitch in the pair diagram to remove the color from the corresponding stitch in the thread diagram.
The go-to buttons lead to pages where you can [highlight] threads 
for three-pair respectively six-pair connections and [replace stitches].
You can save all three pages as a [PDF] document.
The [tutorial] elaborates choosing stitches and has some bonus subjects like
foot sides and modify or create recipes.

[color-code]: /GroundForge-help/color-rules
[tutorial]: /GroundForge-help/snow-mix
[highlight]: /GroundForge-help/clips/color
[replace stitches]: /GroundForge-help/clips/flip
[PDF]: /GroundForge-help/clips/print-as-pdf

