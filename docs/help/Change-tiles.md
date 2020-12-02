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

<video width="414" height="414" controls style="border: 1px solid; padding-top: 2px;">
    <source src="images/brick-to-overlap-animation.mp4" type="video/mp4">
    Your browser does not support an inline <a href="images/brick-to-overlap-animation.mp4">video</a>.
</video>  

An explanation of the silent video follows after introducing properties of the pattern.  

First the order of typing text and clicking buttons as shown in the video.
The video shows what happens in the diagram with each action.
In the video the "tile layout" panel wrapped to the next line due to a smaller browser window.  

![](images/brick-to-overlap-order.png)

An example pattern
------------------

Patterns can have different types and sizes of tiles, each having their pros and cons.

The image below shows ground [F4](https://d-bl.github.io/GroundForge/tiles?whiting=F4_P180&patchWidth=9&patchHeight=9&d1=ctc&c1=ctc&b1=ctc&a1=ctc&d2=ctc&c2=ctcllctc&a2=ctcrrctc&tile=1483,8-48&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)
by Gertrude Whiting as defined in the [catalogue](/gw-lace-to-gf),
marked with some possible base tiles. 

![](images/brick-to-overlap-prepare.png)

The faint stitches repeat the bright ones. 
The 4x2 rectangles (one with all the bright stitches) show that the repetition has a brick layout.
The 3x3 tile looks more like a unit presented in traditional text books.
The larger tiles allows you to vary stitches in more units. 

Tutorial sections explain how to find a [simple](Advanced#simple-arrangement) 
tile (in this case the 4x4) a [brick](Advanced#creating-a-smaller-base-tile) 
(a rotated variant of the 4x2 above) or [overlap](Advanced#overlap-arrangement) (the 3x3).

The video in words
------------------

* Type the characters of the custom base tile of your choice (in this case the 3x3) in the second text field.
* Start with the blue tile in the "corner-to-corner" layout on the right, 
    then nudge the tiles until the corners _overlap_ with the following steps.
  * Move the purple tile with the black arrows in north-west direction until you have proper diagonals.
  * Then move the green tile to the north-east to get the diagonals together.
  * The overlap can cause unexpected behaviour. 
    Fix this by changing the bottom corners to "-" in the input field.
    Please note thate these stitches become faint in the diagram.
* Without nudging you can create valid patterns with the following combinations (not recorded):
  * The 4x4 tile with the "simple" layout at the left top.
  * The 4x2 tile with "brick" layout. 
  * The 8x4 tile with the "simple" as well as the "brick" layout.
  

Changed stitches
----------------
Note below how the black and blue stitches swap in the pattern as a consequence of the tile change.
  
![](images/brick-to-overlap-stitches.png)

You will have to adjust to get the original thread diagram.
Use two browser windows, one with the old tile layout, one with the new tile layout.
Then you can copy-paste the corresponding [stitches](Replace).

