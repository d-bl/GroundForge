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

<fieldset style="background: #FFF; max-width:95%; height:145px; overflow: auto; resize: both"><legend>Select a recipe</legend>
<a href="javascript:recipe('crc,crclctc,ctcrc,rcl,c,c',false)" title="123-a"><img src="123-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,ctc,ctc,ctc,ctc,ctc',false)" title="132-a"><img src="132-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('tctc,rctcl,ctcl,ctct',true)" title="312-a"><img src="312-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('tc,rclcrc,clcrcl,ct',true)" title="321-a"><img src="321-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('tcr,lctc,ctcr,lct',false)" title="321-b"><img src="321-b.png" alt=""></a> &nbsp;
<a href="javascript:recipe('tcl,lctc,ctcr,rct',false)" title="321-c"><img src="321-c.png" alt=""></a> &nbsp;
<a href="javascript:recipe('t,lctc,ctcr,ctct',false)" title="321-d"><img src="321-d.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,c,ctctc,ctctc,ctctc,c',true)" title="126453-a"><img src="126453-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('t,rc,ctc,rclcr,ctcl,ct',true)" title="153426-a"><img src="153426-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('t,rctc,ctctcl,ctct',true)" title="154326-a"><img src="154326-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,cr,crcl,clcrclcr,rcrcl,c',false)" title="156423-a"><img src="156423-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('cr,crcl,clcr,crcl,clcr,c',true)" title="234561-a"><img src="234561-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,cr,crcl,clcr,crcl,cl',false)" title="263451-a"><img src="263451-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,cl,ctcl,crcrcr,rcr,c',true)" title="321546-a"><img src="321546-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,lc,crc,clcrc,clcr,c,crc,cl',true)" title="321654-a"><img src="321654-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,cr,ctcr,clclc,lcl,c',false)" title="321654-b"><img src="321654-b.png" alt=""></a> &nbsp;
<a href="javascript:recipe('ctct,ct,ct,ct,cl,ctc',false)" title="354612-a"><img src="354612-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('lc,crclclc,crcrclc,cr',false)" title="426153-a"><img src="426153-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('cr,ctcl,ctcr,ctcl,ctc,c',false)" title="426153-b"><img src="426153-b.png" alt=""></a> &nbsp;
<a href="javascript:recipe('r,lrc,ctcr,lct',false)" title="456123-a"><img src="456123-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('c,ctc,rclc,ctc,rc,rcl,ctc,c',false)" title="456123-b"><img src="456123-b.png" alt=""></a> &nbsp;
<a href="javascript:recipe('rc,clcrc,clctc,rcl',true)" title="462513-a"><img src="462513-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('lcrc,clcrc,clcrc,clcr',false)" title="564312-b"><img src="564312-b.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,c,ctctc,clcr,rctc,c',false)" title="563412-c"><img src="563412-c.png" alt=""></a> &nbsp;
<a href="javascript:recipe('r,c,crc,ctc,lcrcl,ctc,crc,cl',true)" title="623451-a"><img src="623451-a.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,ctc,ct,crc,ctc,ctc',true)" title="623541-b"><img src="623541-b.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,cl,ctctcr,ct,ctc,c',false)" title="623541-c"><img src="623541-c.png" alt=""></a> &nbsp;
<a href="javascript:recipe('-,c,ctctc,clcr,rctc,c',false)" title="623541-d"><img src="623541-d.png" alt=""></a> &nbsp;
<a href="/GroundForge-help/snow-mix#recipes-for-the-mixer">more...</a>
</fieldset>
<br>

The same set of four colors serves two independent purposes.
The colors of the hexagons match groups of stitches in the thread diagram.
The pair diagram has a [color-code] that can express unorthodox stitches.

Some recipes put more stitches in the recipe box than others.
They may clutter the perimeters of the pair diagram.
Resize the panel (drag the bottom right corner) or move the content around for a better view.
To study the anatomy of the recipes:
Hover over a stitch in the pair diagram for a tooltip, 
click to remove the color from the corresponding stitch in the thread diagram.

The go-to buttons lead to pages where you can [highlight] threads 
for three-pair respectively six-pair connections and [replace stitches].
You can save all three pages as a [PDF] document.
A [tutorial] gives a general introduction, elaborates choosing stitches, footsides and recipes.

[color-code]: /GroundForge-help/color-rules
[tutorial]: /GroundForge-help/snow-mix
[highlight]: /GroundForge-help/clips/color
[replace stitches]: /GroundForge-help/clips/flip
[PDF]: /GroundForge-help/clips/print-as-pdf

