---
layout: default
title: Tile change
---
Change the tile layout
======================

The "Tile layout" panel has visual controls to 
define the way a custom base pattern repeats.
Select one of the blue tile buttons to activate the desired configuration.
Subsequently, use the black arrow buttons to nudge the repeating tiles in one grid-point steps 
until the pattern repeats in a valid way.

An explanation of the video follows after introducing properties of the pattern.  
See also the [tutorial](Advanced) for a step-by-step introduction to create patterns.  
<video width="414" height="414" controls style="border: 1px solid; padding-top: 2px;">
    <source src="images/brick-to-overlap-animation.mp4" type="video/mp4">
    Your browser does not support an inline <a href="images/brick-to-overlap-animation.mp4">video</a>.
</video>

Choose a base tile
------------------

Patterns can have different types and sizes of tiles, each having their pros and cons.
The image below shows ground [F4](https://d-bl.github.io/GroundForge/tiles?whiting=F4_P180&patchWidth=9&patchHeight=9&d1=ctc&c1=ctc&b1=ctc&a1=ctc&d2=ctc&c2=ctcllctc&a2=ctcrrctc&tile=1483,8-48&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)
by Gertrude Whiting, marked with some possible base tiles. 

![](images/brick-to-overlap-prepare.png)

The faint stitches repeat the bright ones. The 2x4 rectangles around the stitches show that the repetition has a brick layout.
The 3x3 tile looks more like a unit presented in traditional text books.
The larger tiles allows you to vary stitches in more units. 

The video in words
------------------

* Type the characters of the custom base tile of your choice (in this case the 3x3) in the second text field.
* Select a tile layout with a blue square/rectangle and adjust with the black arrows.
  * Not recorded:
    * For the 4x4 tile you can select the "simple" layout at the left top and you are done.
    * For the 4x8 tile you can choose between "simple" and "brick" layout.
  * For the 3x3 tile (recorded): start with the "alternating" layout on the right, 
    then move the tiles together until the corners _overlap_ with the following steps.
    * Move the purple tile with the black arrows in north-west direction until you have proper diagonals.
    * Then move the green tile to the north-east get the diagonals together.
    * The overlap can cause unexpected behaviour. 
      Fix this by changing the bottom corners to "-" in the input field.
      Note thate these stitches become faint.

Subsequently, you will have to adjust what stitches to make at each grid position to get the original thread diagram.
Use two browser windows, one with the old tile layout, one with the new tile layout.
Then you can copy-paste the corresponding stitches.

