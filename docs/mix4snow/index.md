---
layout: default
title: Mix Snowflakes
sidebar: mix4snow
javascript:
  - d3.v4.min.js
  - GroundForge-opt.js
  - showGraph.js
  - nudgePairs.js
---

Snow Mixer
==========

You can find some hints below the diagrams.

<fieldset style="height:145px; overflow: auto; resize: both"><legend>Select a recipe</legend>
<a data-title="123-a" href="javascript:GF_snow_mixer.recipe('crc,crclctc,ctcrc,rcl,c,c','right')"><img src="123-a.png" alt=""></a> &nbsp;
<a data-title="123-b" href="javascript:GF_snow_mixer.recipe('rcl,ctc,crcllc,crrclcr,ctc,cl','left')"><img src="123-b.png" alt=""></a> &nbsp;
<a data-title="132-a" href="javascript:GF_snow_mixer.recipe('-,ctc,ctc,ctc,ctc,ctc','right')"><img src="132-a.png" alt=""></a> &nbsp;
<a data-title="312-a" href="javascript:GF_snow_mixer.recipe('tctc,rctcl,ctcl,ctct','left')"><img src="312-a.png" alt=""></a> &nbsp;
<a data-title="321-a" href="javascript:GF_snow_mixer.recipe('tc,rclcrc,clcrcl,ct','left')"><img src="321-a.png" alt=""></a> &nbsp;
<a data-title="321-b" href="javascript:GF_snow_mixer.recipe('tcr,lctc,ctcr,lct','right')"><img src="321-b.png" alt=""></a> &nbsp;
<a data-title="321-c" href="javascript:GF_snow_mixer.recipe('tcl,lctc,ctcr,rct','right')"><img src="321-c.png" alt=""></a> &nbsp;
<a data-title="321-d" href="javascript:GF_snow_mixer.recipe('t,lctc,ctcr,ctct','right')"><img src="321-d.png" alt=""></a> &nbsp;
<a data-title="126453-a" href="javascript:GF_snow_mixer.recipe('-,c,ctctc,ctctc,ctctc,c','left')"><img src="126453-a.png" alt=""></a> &nbsp;
<a data-title="153426-a" href="javascript:GF_snow_mixer.recipe('t,rc,ctc,rclcr,ctcl,ct','left')"><img src="153426-a.png" alt=""></a> &nbsp;
<a data-title="154326-a" href="javascript:GF_snow_mixer.recipe('t,rctc,ctctcl,ctct','left')"><img src="154326-a.png" alt=""></a> &nbsp;
<a data-title="156423-a" href="javascript:GF_snow_mixer.recipe('-,cr,crcl,clcrclcr,rcrcl,c','right')"><img src="156423-a.png" alt=""></a> &nbsp;
<a data-title="234561-a" href="javascript:GF_snow_mixer.recipe('cr,crcl,clcr,crcl,clcr,c','left')"><img src="234561-a.png" alt=""></a> &nbsp;
<a data-title="263451-a" href="javascript:GF_snow_mixer.recipe('-,cr,crcl,clcr,crcl,cl','right')"><img src="263451-a.png" alt=""></a> &nbsp;
<a data-title="321546-a" href="javascript:GF_snow_mixer.recipe('-,cl,ctcl,crcrcr,rcr,c','left')"><img src="321546-a.png" alt=""></a> &nbsp;
<a data-title="321654-a" href="javascript:GF_snow_mixer.recipe('-,lc,crc,clcrc,clcr,c,crc,cl','left')"><img src="321654-a.png" alt=""></a> &nbsp;
<a data-title="321654-b" href="javascript:GF_snow_mixer.recipe('-,cr,ctcr,clclc,lcl,c','right')"><img src="321654-b.png" alt=""></a> &nbsp;
<a data-title="354612-a" href="javascript:GF_snow_mixer.recipe('ctct,ct,ct,ct,cl,ctc','right')"><img src="354612-a.png" alt=""></a> &nbsp;
<a data-title="426153-a" href="javascript:GF_snow_mixer.recipe('lc,crclclc,crcrclc,cr','right')"><img src="426153-a.png" alt=""></a> &nbsp;
<a data-title="426153-b" href="javascript:GF_snow_mixer.recipe('cr,ctcl,ctcr,ctcl,ctc,c','right')"><img src="426153-b.png" alt=""></a> &nbsp;
<a data-title="456123-a" href="javascript:GF_snow_mixer.recipe('r,lrc,ctcr,lct','right')"><img src="456123-a.png" alt=""></a> &nbsp;
<a data-title="456123-b" href="javascript:GF_snow_mixer.recipe('c,ctc,rclc,ctc,rc,rcl,ctc,c','right')"><img src="456123-b.png" alt=""></a> &nbsp;
<a data-title="462513-a" href="javascript:GF_snow_mixer.recipe('rc,clcrc,clctc,rcl','left')"><img src="462513-a.png" alt=""></a> &nbsp;
<a data-title="564312-b" href="javascript:GF_snow_mixer.recipe('lcrc,clcrc,clcrc,clcr','right')"><img src="564312-a.png" alt=""></a> &nbsp;
<a data-title="563412-c" href="javascript:GF_snow_mixer.recipe('-,c,ctctc,clcr,rctc,c','right')"><img src="563412-a.png" alt=""></a> &nbsp;
<a data-title="623451-a" href="javascript:GF_snow_mixer.recipe('r,c,crc,ctc,lcrcl,ctc,crc,cl','left')"><img src="623451-a.png" alt=""></a> &nbsp;
<a data-title="623541-a" href="javascript:GF_snow_mixer.recipe('-,ctc,ct,crc,ctc,ctc','left')"><img src="623541-a.png" alt=""></a> &nbsp;
<a data-title="623541-b" href="javascript:GF_snow_mixer.recipe('-,cl,ctctcr,ct,ctc,c','right')"><img src="623541-b.png" alt=""></a> &nbsp;
</fieldset>

<div id="fragmentDiv"></div>
<script type="text/javascript" src="mix.js"></script>
<script type="text/javascript">GF_snow_mixer.init()</script>

The same set of four colors serves different independent purposes.
* The colors of the hexagons in _apply recipe_ match groups of stitches in the thread diagram.
* The pair diagram has a [color-code] that can express unorthodox stitches.

To study the anatomy of the recipes:
* Hover over stitches for tooltips,
the tooltips in the thread diagram will show stitch IDs
that start with the same value as the corresponding stitch in the pair diagram.
* Click a stitch in the pair diagram to remove the color from the corresponding stitch in the thread diagram.
Apply the last recipe again to restore the colors.

Some recipes put more stitches in the recipe box than others.
They may clutter the perimeters of the diagrams.
You can move the content for a better view.

The go-to buttons lead to pages where you can [highlight] threads 
for three-pair respectively six-pair connections and [replace stitches].
You can save all three pages as a [PDF] document.
After tweak footside, you can return here via the droste page (follow _threads as pairs_).

A [tutorial] gives a general introduction, elaborates choosing stitches and tweaking [footsides] and [recipes].
 
[color-code]: /GroundForge-help/color-rules
[tutorial]: /GroundForge-help/snow-mix
[highlight]: /GroundForge-help/clips/color
[replace stitches]: /GroundForge-help/clips/flip
[PDF]: /GroundForge-help/clips/print-as-pdf
[footsides]: /GroundForge-help/snow-mix#footsides
[recipes]: /GroundForge-help/snow-mix#recipes-for-the-mixer

