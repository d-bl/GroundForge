_Customize a generated pattern to your own taste._


Editor Requirements
-------------------

The procedure below is written for [InkScape] but another state of the art general purpose vector editor might allow similar actions.

The alternative editor should have "full" support for clones (as InkScape calls the feature) or `<use>` elements in SVG terminology. "Full" means that the clones are not plain copies but reflect changes made to the original. For example InkPad and OpenOffice Draw import clones properly but don't have the "full" support.

The alternative editor should have a mode to manipulate objects, and a mode to deal with nodes on objects.

[InkScape]: http://inkscape.org


The procedure
-------------

In short:

Download a pattern-sheet from [GroundForge] and open it with [InkScape]. 
All tiles (or repeats) in a patch change along with changes made to the slightly separated tile/repeat in the top left corner. Select a dot by its nodes to drag the connected lines along, move nodes with the same color together to move all four connected lines.

[GroundForge]: https://d-bl.github.io/GroundForge/

Step by step.

* Start with object mode.
* Right-click the tile that sits next to the cloned patch to enter the group of objects.
* Select all the objects in the group of the tile.
* Switch to node mode.
* Select a dot by dragging a square around it. This way the end of the adjacent lines are also selected and will move along with the dot.
* Less than four adjacent lines on the selected dot? Hold down the shift key when selecting the second or even third dot of the same colour.
* Drag the dots with the mouse or nudge them with arrow keys. Five time an arrow key in InkScape is a full grid position. One time shift+arrow is two grid positions.


Screenshots
-----------

_On the left_: shows how the patch repeats the slightly off-set "tile" in the top left, not unlike the [logo] tiles.

_On the right_: dots selected as objects, note the dashed rectangles around the two selected dots. The toolbar indicates object mode with the first icon.

_At the bottom_: light blue dots and their line ends selected by their nodes. The toolbar indicates node mode with the second icon.

[logo]: https://d-bl.github.io/GroundForge/images/logo-medium.png
![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/reshape-using-clones.png)