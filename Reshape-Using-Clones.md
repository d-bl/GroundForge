_Customize a generated pattern to your own taste._


Editor Requirements
-------------------

The SVG editor should have "full" support for clones or `<use>` elements in SVG terminology. "Full" means that the clones are not plain copies but reflect changes made to the original. For example InkPad and OpenOffice Draw import clones properly but don't have the "full" support.

The editor should have a mode to manipulate objects, and a mode to deal with nodes on objects.


The procedure
-------------

In short:

Download a pattern-sheet from [GroundForge] and open it with the SVG editor. 
All tiles (or repeats) in a patch change along with changes made to the slightly separated tile/repeat in the top left corner. Select a dot by its nodes to drag the connected lines along, move nodes with the same color together to move all four connected lines.

[InkScape]: http://inkscape.org
[GroundForge]: https://d-bl.github.io/GroundForge/

Step by step for [InkScape] v0.91.

* Start with object mode.
* Right-click the tile that sits next to the cloned patch to enter the group of objects.
* Select all the objects in the group of the tile.
* Switch to node mode.
* Select a dot by dragging a square around it. This way the end of the adjacent lines are also selected and will move along with the dot.
* Less than four adjacent lines on the selected dot? Hold down the shift key when selecting the second or even third dot of the same colour.
* Drag the dots with the mouse or nudge them with arrow keys. Five time an arrow key in InkScape is a full grid position. One time shift+arrow is two grid positions.

CorelDRAW X8 does support clones, but does not import them correctly and hence needs some preparations.

* remove the patch with the very pale dots
* the remaining repeat with bright dots may have a few duplicate dots, remove these duplicates in advance.
* rebuild the removed patch:
  * make a clone of the bright repeat
  * duplicate the clone
  * attach it to the first clone as described for "reshaping using copies"
  * duplicate both clones
  * etcetera
* select all the objects in the original repeat one by one with ctrl-shift-click
* switch to node mode and continue as for InkScape


Screenshots for InkScape v0.91
------------------------------

_On the left_: shows how the patch repeats the slightly off-set "tile" in the top left, not unlike the [logo] tiles.

_On the right_: dots selected as objects, note the dashed rectangles around the two selected dots. The toolbar indicates object mode with the first icon.

_At the bottom_: light blue dots and their line ends selected by their nodes. The toolbar indicates node mode with the second icon.

[logo]: https://d-bl.github.io/GroundForge/images/logo-medium.png
![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/reshape-using-clones.png)

Screenshots for CorelDraw X8
----------------------------

Moving the blue dots reveals there was a duplicate dot that wasn't removed.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/reshape-with-cdr.png)
