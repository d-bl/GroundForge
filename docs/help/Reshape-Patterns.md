---
layout: default
title: Reshape Patterns
---

Reshape Patterns
================

  * [Introduction](#introduction)
  * [Make variations in general terms](#make-variations-in-general-terms)
  * [Evaluated Editors](#evaluated-editors)
  * [Step 3 in small steps (InkScape)](#step-3-in-small-steps--inkscape-)
  * [Insert or transform into a design](#insert-or-transform-into-a-design)


Introduction
------------

The animation below moves corresponding intersections together.
On the right two isolated repeats, one leads the dance, the other is static.
Dots with the same color always move together,
occasionally more dots are moved together to preserve symmetry.

![](animation/cropped.gif)

The animation shows some of these [patterns]. For the other variants, corresponding intersections of every other repeat should be moved together. In other words: the isolated repeat that leads the dance should span multiple repeats.

[patterns]: ../sheet.html?patch=B-C-,---5,C-B-,-5--;checker&patch=-4-7,5---,-C-B,3158;bricks&patch=5-O-E-,-E-5-O,5-O-E-;bricks&patch=158-,---5,C-B-;checker&patch=8-76,124-;checker&patch=5831,-4-7;checker&patch=68,-4;checker&patch=6868,-4-4,6868,-4-4;checker

In some cases it may be relatively easy to see how one pattern is reshaped in another one. For example in the first row below, the hexagon of the first variant is reshaped in to a brick for the second variant and the center of the bow-tie in the second variant is lowered for the third variant. For the second row it is harder to see. Applying the same color to shapes surrounded with the same number of line segments can help to identify the transition. Note that an edge of a shape may span multiple line segments.


![](images/reshape.png)


Make variations in general terms
--------------------------------

Features and exact procedures may vary between SVG editors. The least elaborate method in a nutshell:

1. Follow the download link on the page with the [sources][patterns] of the animation. Open the file with the editor of your choice.  
  More patterns are available through the `vari` links on the [Tesselace-Index](TesseLace-Index) and some other catalogues of patterns.
2. If the diagrams are rendered as on the web page, you most likely won't need this step.  
  Provided the isolated dotted repeat is rendered properly, you can reconstruct the rest of the patch by making clones (=/= copies!) of this group of objects. The colored dots are guides to seam the copied repeats together. The following image shows four repeats almost in place. Of course the dots should snap exactly on top of one another. The original repeat must be a group before cloning.  
  ![](images/recover-patch.png)
3. You are free to rearrange and ungroup the patterns, but don't ungroup individual repeats: it would break the links between clones and originals.   
  The close up below shows control points of multiple selected objects: a dot and four line ends at the center of the dot. When moving these control points for all dots of the same color together, the corresponding points in the patch should move along and the diagram should remain valid. The grey dots can move individually.  
  ![](images/select-node-close-up.png)

Evaluated Editors
-----------------

The procedure above is tried for just some of the available [SVG editor]s. They might have been improved since then, check for a free trial period to convince yourself you can work with it:
* Lace8 and Knipling (💰, not on Mac)  
  these don't understand the downloads at all.
* InkScape (free, needs crutches on a Mac)  
  won't need step 2.
* Affinity Designer (💰, Mac, iPad and Windows)  
  use symbols for step 2 as shown by [graphicheck] and many others. Step 3: Use the layer panel to select all objects in the group of one repeat (click the first, shift-click the last); continue with the node tool as for InkScape.
* CorelDRAW (💰💰, Home and Student Suite 💰, Windows)  
  use `clone` from the edit menu for step 2.
* InkPad (free, iPad)  
  version 1.6 requires the workaround below.
* Adobe Illustrator  (💰💰, subscription)  
  not tried. Perhaps these [instructions] can be of use for step 2. Start at about 5:50. From another: "Any changes made while in `pattern editing mode` will be applied to the swatch upon exit".

[graphicheck]: https://graphicheck.com/blog/Affinity-Designer-Background-Patterns-Mock-up-Template
[instructions]: https://www.youtube.com/watch?v=aQM3RLWN0vQ

Workaround for step 3:
* Optionally assign new unique colors to the grey dots on the original repeat to make corresponding dots easier to recognize.
* Create a new patch with 2x2 or 3x3 copies of the original repeat, as in step 2. A smaller number makes it harder to get an impression of the manipulated pattern, a larger number makes the next task more elaborate.
* Select corresponding dots one by one through the whole patch to move them together.


Step 3 in small steps (InkScape)
--------------------------------

* Start with object mode.
  * Double-click the pattern of your choice to enter the group of objects.
  * Double-click the isolated repeat with coloured dots to enter the group of objects.
  * Select all the objects in the group of the repeat.
* Switch to node mode.
  * Select a dot by dragging a square around it. Because all objects were selected in advance, the ends of the adjacent lines are also selected and will move along with the dot.
  * Less than four adjacent lines on the selected dot? Hold down the shift key when selecting the second or even third dot of the same colour.
  * Dragging the nodes with the mouse is dangerous as you easily loose your selection. Nudge the selection around with arrow keys. Five times an arrow key in InkScape is a full grid position. One time shift+arrow is two grid positions.


Insert or transform into a design
---------------------------------

When done with adjusting the pattern you might do all kind of other things with it. Such as scaling to match your thread width, [fill] a shape in your design, [bend] it to make a fan, add columns or rows to the patch or whatever. The editor of your choice will have its own tutorials and manuals for these tasks. 

[SVG editor]: https://en.wikipedia.org/wiki/Comparison_of_vector_graphics_editors#File_format_support

[fill]: https://tesselace.com/2017/11/08/filling-a-shape-with-a-lace-ground/

[bend]: http://tavmjong.free.fr/INKSCAPE/MANUAL/html/Paths-LivePathEffects-BendTool.html
