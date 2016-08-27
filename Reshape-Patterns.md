A downloadable pattern sheet of [GroundForge] may have multiple patterns. In some cases it may be relatively easy to see how one pattern is reshaped in another one. For example in the first set below the hexagon of the first variant is reshaped in to a brick for the second variant and the centre of the bow-tie in the second variant is lowered for the third variant. For the second set it becomes harder to see. Applying the same colour to shapes surrounded with the same number of line segments can help to identify the transition. Note that an edge of a shape may span multiple line segments.

[GroundForge]: https://d-bl.github.io/GroundForge/
[patterns]: https://github.com/d-bl/GroundForge/tree/gh-pages/patterns

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/reshape.png)

The a manipulated screenshot above reflects a manipulated pattern sheet. The generated sheets only use full grid positions. Other sheets may come with just a single pattern that does not look at all like the colour coded pair diagram. To create a regular patch that does look like the pair diagram (or any other shape you might prefer) you would have to do the reshaping by yourself by nudging the position of intersections. That is one of the factors that makes these patterns advanced.

Reshape with InkScape
=====================

The lines that connect the dots are InkScape connectors. When a dot moves the adjacent lines follow. Other SVG editors won't recognize the connectors and will treat the dots and lines independently. 

Select dots
-----------

At first selecting the dots is a bit hard as invisible line ends are sitting on top, this is visible on the web page where you downloaded the sheet. Follow the steps shown in the [screenshot](#screenshot) to get the dots on top:

1. drag a rectangle with your mouse around a dot
2. from the context menu or from the _edit_ menu: _select same object type_
3. raise selection to top

From now on yo can select dots also by clicking on them.

Dots with the same colour should move together to keep the pattern regular. You might not see much differences with some of the dots, but your computer can tell them apart. To move related dots select one and then apply _select same fill colour_. Then you can drag the dots with the mouse or move them with the arrows on your keyboard. Five times with an arrow key is a full grid position. Shift + arrow-key once is two grid positions.

Prevent hick-ups and recover
----------------------------

Working with lots of connectors is a heavy task for InkScape, so be gentle for your system resources by deleting anything but the patch you are going to manipulate.

When using the arrow keys the lines get a step behind and other weird things may happen in various situations. To recover select all the dots (start with one, then _select same object type_) and drag the full diagram a bit with the mouse. Then InkScape realigns the lines properly with the dots.

Don't forget about the undo button and regularly save your changes.

Finish up
---------

When done with adjusting the pattern you might do all kind of other things with it. Such as filling some shape in your design, [bend] it to make a fan, add coloumns or rows to the patch or whatever. InkScape has lots of tutorials and manuals for these tasks. 

[bend]: http://tavmjong.free.fr/INKSCAPE/MANUAL/html/Paths-LivePathEffects-BendTool.html

To be gentle with your system resources it might be wise to first disconnect the lines and the dots. Don't forget to make a copy of your achievements so far to fall back on when getting second thoughts, undoing and attempts to recover from the next procedure might not have the expected results. Select all lines like you selected dots: start with one, then _select same object type_. Use an arrow key to nudge the lines a step away from the dots and move the same step back. From now on the lines won't follow the dots any more, check that fact to see whether you succeeded.

Screenshot
----------

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/select-dots.png)