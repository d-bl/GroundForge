---
layout: default
title: Stitch Generator
sidebar: default
---

<link rel="stylesheet" href="random-stitches.css" type="text/css">
<script src="../js/randomStitch.js"></script>

# Stitches Generator

![][p-alfa1]

[p-alfa1]: random-stitches.svg "stitch-generator in Alphabet 1, by M. Tempels"

This feature generates a list of random stitches.  
Please note: a number that is too low or too high will be set to 1 resp. the maximal number allowed.

<input type="number" name="stitchesRequired" id="stitchesRequired" min="1" max="25" value="1" onchange="genVal(this)" >&nbsp;
**The number of stitches generated.** 
Minimal 1, maximal 25.

<input type="number" name="maxCrosses" id="maxCrosses" min="1" max="5" value="3" onchange="genVal(this)" >&nbsp;
**The maximal number of crosses in a stitch.**
Minimal 1, maximal 5. The generated stitch has at least one cross.

<input type="number" name="maxTwistsBetweenCrosses" id="maxTwistsBetweenCrosses" min="1" max="5" value="1" onchange="genVal(this)" >&nbsp;
**The maximal number of twists between two crosses.**
Minimal 1, maximal 5.
The generated stitch can have 0 twists between two crosses, e.g. "cc".


<input type="number" name="maxTwistsBetweenStitches" id="maxTwistsBetweenStitches" min="1" max="5" value="2" onchange="genVal(this)" >&nbsp;
**The maximal number of twists between two stitches**
Minimal 1, maximal 5.
The generated stitch can have 0 twists at the front and at the back, e.g. "ctc".</td>

<div id="between">
<input type="checkbox" id="twistsBefore" name="twistsBefore" value="tBefore">
<label for="twistsBefore">before</label><br>
<input type="checkbox" id="twistsAfter" name="twistsAfter" value="tAfter" checked>
<label for="twistsAfter">after</label>
</div>&nbsp;
**Position of twists between stitches:**
Twists can be at the front (Rctc), at the back (ctcL), at front and back (RctcL) of the generated stitch or none (ctc).

<button onclick="document.getElementById('slag').innerHTML = genRandomStitchList()">
        Generate list of stitches</button>
<p id="slag"></p>
