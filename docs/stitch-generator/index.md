---
layout: default
title: Stitch Generator
sidebar: default
javascript:
  - stitchGen.js
---

<script type="text/javascript" src="../js/stitchGen.js"></script>
<link rel="stylesheet" href="../css/buttons.css" type="text/css">
<link rel="stylesheet" href="../css/stitchGen.css" type="text/css">

# Stitches Generator

![][p-alfa1]

[p-alfa1]: stitch-generator.svg "stitch-generator in Alphabet 1, by M. Tempels"

This feature generates a list of random stitches.  
Please note: a number that is too low or too high will be set to 1 resp. the maximal number allowed.   

<table class="stgen">
    <tr>
        <td><label for="stitchesRequired">The number of stitches required </label></td>
        <td><input type="number" name="stitchesRequired" id="stitchesRequired" min="1" max="25" value="1" onchange="genVal(this)" ></td>
        <td>Minimal 1, maximal 25.</td>
    </tr>
    <tr>
         <td><label for="maxCrosses">The maximal number of crosses in a stitch </label></td>
         <td><input type="number" name="maxCrosses" id="maxCrosses" min="1" max="5" value="3" onchange="genVal(this)" ></td>
         <td>Between 1 and 5. <br> 
            The generated stitch has at least one cross.</td>
    </tr>
    <tr>
        <td><label for="maxTwistsBetweenCrosses">The maximal number of twists between two crosses </label></td>
        <td><input type="number" name="maxTwistsBetweenCrosses" id="maxTwistsBetweenCrosses" min="1" max="5" value="1" onchange="genVal(this)" ></td>
        <td>Between 1 and 5. <br> 
            The generated stitch can have 0 twists between two crosses, e.g. "cc".</td>
    </tr>
    <tr>
        <td><label for="maxTwistsBetweenStitches">The maximal number of twists between two stitches </label></td>
        <td><input type="number" name="maxTwistsBetweenStitches" id="maxTwistsBetweenStitches" min="1" max="5" value="2" onchange="genVal(this)" ></td>
        <td>Between 1 and 5. <br> 
            The generated stitch can have 0 twists at the front and at the back, e.g. "ctc".</td>
    </tr>
    <tr>
       <td>Position of twists between stitches</td>
       <td><input type="checkbox" id="twistsBefore" name="twistsBefore" value="tBefore">
           <label for="twistsBefore">before</label>
       <br>
           <input type="checkbox" id="twistsAfter" name="twistsAfter" value="tAfter" checked>
           <label for="twistsAfter">after</label>
       </td>
       <td>Between two stitches, twists can be at the front (Rctc), at the back (ctcL), at front and back (RctcL) of the generated stitch or none (ctc).</td>
    </tr>
</table>

<div>
    <br>
    <button onclick="document.getElementById('slag').innerHTML = genStitchList()">
        Generate list of stitches</button>
    <br><br>
</div>

<p id="slag"></p>
