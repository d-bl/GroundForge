---
layout: default
title: Mix Snowflakes
javascript:
  - d3.v4.min.js
  - GroundForge-opt.js
  - showGraph.js
  - nudgePairs.js
---

<div id="fragmentDiv"></div>
<script type="text/javascript" src="mix.js"></script>
<script type="text/javascript">GF_snow_mixer.init()</script>

<fieldset style="background: #FFF; max-width:95%; height:145px; overflow: auto; resize: both"><legend>Select a recipe</legend>
<a data-title="123-a" href="javascript:GF_snow_mixer.recipe('crc,crclctc,ctcrc,rcl,c,c',false)"><img src="123-a.png" alt=""></a> &nbsp;
<a data-title="123-b" href="javascript:GF_snow_mixer.recipe('rcl,ctc,crcllc,crrclcr,ctc,cl',true)"><img src="123-b.png" alt=""></a> &nbsp;
<a data-title="132-a" href="javascript:GF_snow_mixer.recipe('-,ctc,ctc,ctc,ctc,ctc',false)"><img src="132-a.png" alt=""></a> &nbsp;
<a data-title="312-a" href="javascript:GF_snow_mixer.recipe('tctc,rctcl,ctcl,ctct',true)"><img src="312-a.png" alt=""></a> &nbsp;
<a data-title="321-a" href="javascript:GF_snow_mixer.recipe('tc,rclcrc,clcrcl,ct',true)"><img src="321-a.png" alt=""></a> &nbsp;
<a data-title="321-b" href="javascript:GF_snow_mixer.recipe('tcr,lctc,ctcr,lct',false)"><img src="321-b.png" alt=""></a> &nbsp;
<a data-title="321-c" href="javascript:GF_snow_mixer.recipe('tcl,lctc,ctcr,rct',false)"><img src="321-c.png" alt=""></a> &nbsp;
<a data-title="321-d" href="javascript:GF_snow_mixer.recipe('t,lctc,ctcr,ctct',false)"><img src="321-d.png" alt=""></a> &nbsp;
<a data-title="126453-a" href="javascript:GF_snow_mixer.recipe('-,c,ctctc,ctctc,ctctc,c',true)"><img src="126453-a.png" alt=""></a> &nbsp;
<a data-title="153426-a" href="javascript:GF_snow_mixer.recipe('t,rc,ctc,rclcr,ctcl,ct',true)"><img src="153426-a.png" alt=""></a> &nbsp;
<a data-title="154326-a" href="javascript:GF_snow_mixer.recipe('t,rctc,ctctcl,ctct',true)"><img src="154326-a.png" alt=""></a> &nbsp;
<a data-title="156423-a" href="javascript:GF_snow_mixer.recipe('-,cr,crcl,clcrclcr,rcrcl,c',false)"><img src="156423-a.png" alt=""></a> &nbsp;
<a data-title="234561-a" href="javascript:GF_snow_mixer.recipe('cr,crcl,clcr,crcl,clcr,c',true)"><img src="234561-a.png" alt=""></a> &nbsp;
<a data-title="263451-a" href="javascript:GF_snow_mixer.recipe('-,cr,crcl,clcr,crcl,cl',false)"><img src="263451-a.png" alt=""></a> &nbsp;
<a data-title="321546-a" href="javascript:GF_snow_mixer.recipe('-,cl,ctcl,crcrcr,rcr,c',true)"><img src="321546-a.png" alt=""></a> &nbsp;
<a data-title="321654-a" href="javascript:GF_snow_mixer.recipe('-,lc,crc,clcrc,clcr,c,crc,cl',true)"><img src="321654-a.png" alt=""></a> &nbsp;
<a data-title="321654-b" href="javascript:GF_snow_mixer.recipe('-,cr,ctcr,clclc,lcl,c',false)"><img src="321654-b.png" alt=""></a> &nbsp;
<a data-title="354612-a" href="javascript:GF_snow_mixer.recipe('ctct,ct,ct,ct,cl,ctc',false)"><img src="354612-a.png" alt=""></a> &nbsp;
<a data-title="426153-a" href="javascript:GF_snow_mixer.recipe('lc,crclclc,crcrclc,cr',false)"><img src="426153-a.png" alt=""></a> &nbsp;
<a data-title="426153-b" href="javascript:GF_snow_mixer.recipe('cr,ctcl,ctcr,ctcl,ctc,c',false)"><img src="426153-b.png" alt=""></a> &nbsp;
<a data-title="456123-a" href="javascript:GF_snow_mixer.recipe('r,lrc,ctcr,lct',false)"><img src="456123-a.png" alt=""></a> &nbsp;
<a data-title="456123-b" href="javascript:GF_snow_mixer.recipe('c,ctc,rclc,ctc,rc,rcl,ctc,c',false)"><img src="456123-b.png" alt=""></a> &nbsp;
<a data-title="462513-a" href="javascript:GF_snow_mixer.recipe('rc,clcrc,clctc,rcl',true)"><img src="462513-a.png" alt=""></a> &nbsp;
<a data-title="564312-b" href="javascript:GF_snow_mixer.recipe('lcrc,clcrc,clcrc,clcr',false)"><img src="564312-a.png" alt=""></a> &nbsp;
<a data-title="563412-c" href="javascript:GF_snow_mixer.recipe('-,c,ctctc,clcr,rctc,c',false)"><img src="563412-a.png" alt=""></a> &nbsp;
<a data-title="623451-a" href="javascript:GF_snow_mixer.recipe('r,c,crc,ctc,lcrcl,ctc,crc,cl',true)"><img src="623451-a.png" alt=""></a> &nbsp;
<a data-title="623541-a" href="javascript:GF_snow_mixer.recipe('-,ctc,ct,crc,ctc,ctc',true)"><img src="623541-a.png" alt=""></a> &nbsp;
<a data-title="623541-b" href="javascript:GF_snow_mixer.recipe('-,cl,ctctcr,ct,ctc,c',false)"><img src="623541-b.png" alt=""></a> &nbsp;
<a href="/GroundForge-help/snow-mix#recipes-for-the-mixer" rel="help">variations and more...</a>
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

