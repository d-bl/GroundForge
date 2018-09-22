---
layout: default
title: Reshape Patterns
---

Reshape Patterns
================

The animation below moves all intersections in the diagram that correspond with a dot in the top right repeat next to the diagram.
Dots with the same color should move together.
Occasionally more dots are moved together to preserve symmetry.

![](animation/GIFCreator-me.gif)

The animation shows some of these [patterns]. For other variants, the figure with colored dots would need to span multiple repeats to allow a transformation. 

[patterns]: /GroundForge/sheet.html?patch=B-C-,---5,C-B-,-5--;checker&patch=-4-7,5---,-C-B,3158;bricks&patch=5-O-E-,-E-5-O,5-O-E-;bricks&patch=158-,---5,C-B-;checker&patch=8-76,124-;checker&patch=5831,-4-7;checker&patch=68,-4;checker&patch=6868,-4-4,6868,-4-4;checker

In some cases it may be relatively easy to see how one pattern is reshaped in another one. For example in the first row below, the hexagon of the first variant is reshaped in to a brick for the second variant and the center of the bow-tie in the second variant is lowered for the third variant. For the second row it is harder to see. Applying the same color to shapes surrounded with the same number of line segments can help to identify the transition. Note that an edge of a shape may span multiple line segments.


![](images/reshape.png)


Your own variations in practice
-------------------------------

Features and exact procedures may vary between [SVG editor]s. The least elaborate method in a nutshell:

[SVG editor]: https://en.wikipedia.org/wiki/Comparison_of_vector_graphics_editors#File_format_support

1)
  Follow the download link on the page with the [sources][patterns] of the animation. Open the file with the editor of your choice.  
  More patterns are available through the `vari` links on the [Tesselace-index] and some other catalogues of patterns.

2)
  If the diagrams are rendered as on the web page, you most likely won't need this step.  
  Provided the isolated dotted repeat is rendered properly, you can reconstruct the rest of the patch by making clones (=/= copies!) of this group of objects.
  The colored dots are guides to seam the copied repeats together. The following image shows four repeats almost in place. Of course the dots should snap exactly on top of one another.  
  There may be multiple dots on a single spot, you may want to delete these duplicates in advance. The original repeat must be a group before cloning.
  ![](images/recover-patch.png)

3)
  You are free to rearrange and ungroup the patterns, but don't ungroup individual repeats: it would break the links between clones and originals.  
  Nevertheless the editor should provide some means to manipulate objects within the group, or rather control points of multiple objects in a group.  
  The close up below is made with InkScape. It shows control points of multiple selected objects: a dot and four line ends at the center of the dot. When moving these control points for all dots of the same color together, the diagram should remain valid and the corresponding points in the patch should move along. The grey dots can move individually.   
   ![](images/select-node-close-up.png)

Some tried editors (they might have been improved):
* Lace8 and Knipling (💰, not on Mac) don't support SVG and fail at step 1.
* InkScape (free, needs crutches on a Mac) can skip step 2.
  More lace related tutorials on a [blog] by Veronika Irvine.
* Affinity Designer (💰) doesn't need step 2, step 3 not yet tried.
* InkPad (free, for iPads) doesn't need step 2, version 1.6 fails at step 3.
* CorelDraw (💰💰) version x8 requires step 2.
* Adobe Illustrator  (💰💰, subscription) v2015-3.1 fails at step 3.

Details for InkScape and CorelDraw on [Reshape-using-clones]. A more elaborate method, but also more flexible and supported by more editors, is on [Reshape-using-copies].

[blog]: https://tesselace.com/blog/

Finish up
---------

When done with adjusting the pattern you might do all kind of other things with it. Such as scaling to match your thread width, fill a shape in your design, [bend] it to make a fan, add columns or rows to the patch or whatever. The editor of your choice will have its own tutorials and manuals for these tasks. 

[bend]: http://tavmjong.free.fr/INKSCAPE/MANUAL/html/Paths-LivePathEffects-BendTool.html
