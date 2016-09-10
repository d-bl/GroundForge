A downloadable pattern sheet of [GroundForge] may have multiple patterns. In some cases it may be relatively easy to see how one pattern is reshaped in another one. For example in the first set below the hexagon of the first variant is reshaped in to a brick for the second variant and the centre of the bow-tie in the second variant is lowered for the third variant. For the second set it becomes harder to see. Applying the same colour to shapes surrounded with the same number of line segments can help to identify the transition. Note that an edge of a shape may span multiple line segments.

[GroundForge]: https://d-bl.github.io/GroundForge/
[patterns]: https://github.com/d-bl/GroundForge/tree/gh-pages/patterns

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/reshape.png)

The a manipulated screenshot above reflects a manipulated pattern sheet. The generated sheets only use full grid positions. Other sheets may come with just a single pattern that does not look at all like the colour coded pair diagram. To create a regular patch that does look like the pair diagram (or any other shape you might prefer) you would have to do the reshaping by yourself by nudging the position of intersections. That is one of the factors that makes these patterns advanced.

Requirements
------------

A state of the art general purpose vector editor like the free InkScape. Perhaps Adobe Illustrator, Corel Draw or others are suitable too.

The editor should have "full" support for clones (as InkScape calls the feature) or `<use>` elements in SVG terminology. "Full" means that the clones are not plain copies but reflect changes made to the original. For example InkPad and OpenOffice Draw import clones properly but don't have the "full" support.

The editor should have a mode to manipulate objects, and a mode to deal with nodes on objects.


The procedure
-------------

In short:

All tiles in a patch change along with the separate tile. Select a dot by its nodes to drag the connected lines along, move nodes with the same color together to move all four connected lines.

The following step by step procedure is tested with InkScape, some details may be slightly different in other editors.

* Start with object mode.
* Right-click the tile that sits next to the cloned patch to enter the group of objects.
* Select all the objects in the group of the tile.
* Switch to node mode.
* Select a dot by dragging a square around it. This way the end of the adjacent lines are also selected and will move along with the dot.
* Less than four adjacent lines on the selected dot? Hold down the shift key when selecting the second or even third dot of the same colour.
* Drag the dots with the mouse or nudge them with arrow keys. Five time an arrow key in InkScape is a full grid position. One time shift+arrow is two grid positions.


Finish up
---------

When done with adjusting the pattern you might do all kind of other things with it. Such as scaling to match your thread width, fill a shape in your design, [bend] it to make a fan, add colomns or rows to the patch or whatever. The editor of your choice will have its own tutorials and manuals for these tasks. 

[bend]: http://tavmjong.free.fr/INKSCAPE/MANUAL/html/Paths-LivePathEffects-BendTool.html

Screenshots
-----------

_On the left_: shows how the patch repeats the slightly off-set "tile" in the top left, not unlike the [logo] tiles.

_On the right_: dots selected as objects, note how the toolbar indicates object mode with the first icon.

_At the bottom_: light blue dots and their line ends selected by their nodes, the toolbar indicates node mode with the second icon.

[logo]: https://d-bl.github.io/GroundForge/images/logo-medium.png
![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/select-dots.png)