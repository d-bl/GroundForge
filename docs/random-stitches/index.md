---
layout: default
title: Stitch Generator
sidebar: randomstitch
---

<link rel="stylesheet" href="random-stitches.css" type="text/css">
<script src="../js/stitch-gallery.js"></script>

# Stitches Generator

![][p-alfa1]

[p-alfa1]: random-stitches.svg "stitch-generator in Alphabet 1, by M. Tempels"

This feature generates a list of random stitches.  
Please note: a number that is too low or too high will be set to 1 resp. the maximal number allowed.

<table class="stgen">
    <tr>
        <td><label for="stitchesRequired">The number of stitches required </label></td>
        <td><input type="number" name="stitchesRequired" id="stitchesRequired" min="1" max="25" value="1" onchange="GF_Random.genVal(this)" ></td>
        <td>Minimal 1, maximal 25.</td>
    </tr>
    <tr>
         <td><label for="maxCrosses">The maximal number of crosses in a stitch </label></td>
         <td><input type="number" name="maxCrosses" id="maxCrosses" min="1" max="5" value="3" onchange="GF_Random.genVal(this)" ></td>
         <td>Between 1 and 5. <br> 
            The generated stitch has at least one cross.</td>
    </tr>
    <tr>
        <td><label for="maxTwistsBetweenCrosses">The maximal number of twists between two crosses </label></td>
        <td><input type="number" name="maxTwistsBetweenCrosses" id="maxTwistsBetweenCrosses" min="1" max="5" value="1" onchange="GF_Random.genVal(this)" ></td>
        <td>Between 1 and 5. <br> 
            The generated stitch can have 0 twists between two crosses, e.g. "cc".</td>
    </tr>
    <tr>
         <td><label for="maxTwistsBefore">The maximal number of twists at the front of stitch. </label></td>
         <td><input type="number" name="maxTwistsBefore" id="maxTwistsBefore" min="0" max="5" value="2" onchange="GF_Random.genVal(this)" ></td>
         <td>Between 0 and 5. <br>
                    0 if no twists at the front of a stitch are required. The generated stitch can have 0 twists at the front, e.g. "ctc".</td>
        </tr>
        <tr>
           <td><label for="maxTwistsAfter">The maximal number of twists between two stitches </label></td>
           <td><input type="number" name="maxTwistsAfter" id="maxTwistsAfter" min="0" max="5" value="2" onchange="GF_Random.genVal(this)" ></td>
           <td>Between 0 and 5. <br>
              0 if no twists at the end of a stitch are required. The generated stitch can have 0 twists at the end, e.g. "ctc".</td>
        </tr>
</table>

<div>
    <br>
    <button class="button" type="button" onclick="document.getElementById('setRandomList').innerHTML = GF_Random.genRandomStitchList()">
        Generate list of stitches</button>
    <br><br>
</div>

<p id="setRandomList"></p>
