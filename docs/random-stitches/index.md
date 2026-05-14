---
layout: default
title: Random stitch generator
sidebar: randomstitch
---

<link rel="stylesheet" href="random-stitches.css" type="text/css">
<script src="../js/stitch-gallery.js"></script>

# Random stitches generator

![][p-alfa1]

[p-alfa1]: random-stitches.svg "stitch-generator in Alphabet 1, by M. Tempels"

This feature generates a list of random stitches.  
Please note: a number that is too low or too high will be set to 1 resp. the maximal number allowed.

<span class="grid-container">
The number of stitches required.
<input type="number" name="stitchesRequired" id="stitchesRequired" min="1" max="25" value="1" onchange="GF_Random.genVal(this)" ></span>
Minimal 1, maximal 25.    

<span class="grid-container">
The maximal number of crosses in a stitch.
<input type="number" name="maxCrosses" id="maxCrosses" min="1" max="5" value="3" onchange="GF_Random.genVal(this)" ></span>
Between 1 and 5. The generated stitch has at least one cross.     

<span class="grid-container">
The maximal number of twists between two crosses.
<input type="number" name="maxTwistsBetweenCrosses" id="maxTwistsBetweenCrosses" min="1" max="5" value="1" onchange="GF_Random.genVal(this)" ></span>
Between 1 and 5. The generated stitch can have 0 twists between two crosses, e.g. "cc".    

<span class="grid-container">
The maximal number of twists at the front of the stitch, e.g. Tctc. 
<input type="number" name="maxTwistsBefore" id="maxTwistsBefore" min="0" max="5" value="0" onchange="GF_Random.genVal(this)" ></span>
Between 0 and 5. Use 0 if no twists at the front of a stitch are required.    
The generated stitch can have 0 twists at the front, e.g. "ctc".    

<span class="grid-container">
The maximal number of twists at the end of the stitch, e.g. ctcT. 
<input type="number" name="maxTwistsAfter" id="maxTwistsAfter" min="0" max="5" value="2" onchange="GF_Random.genVal(this)" ></span>
Between 0 and 5. Use 0 if no twists at the end of a stitch are required.     
The generated stitch can have 0 twists at the end, e.g. "ctc".     

<p>
<button class="button" type="button" onclick="document.getElementById('setRandomList').innerHTML = GF_Random.genRandomStitchList()">
        Generate list of stitches</button>
</p>

<p id="setRandomList"></p>