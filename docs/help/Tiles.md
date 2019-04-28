---
layout: default
title: Tiles
---
The [tiles] page is a variant of the older home page.
The [site map] provides the context for both and lists their differences.

[tiles]: /GroundForge/tiles.html?tile=5831,-4-7&patchWidth=9&patchHeight=9&shiftColsSE=4&shiftRowsSE=2&shiftColsSW=0&shiftRowsSW=2&
[site map]: /GroundForge/help/Site-map

* [Catalogues](#catalogues)
* [Link or wand / Reload diagrams](#reload-diagrams)
* [Patch size](#patch-size)
* [Choose stitches](#choose-stitches)
* [Toggle thread colors](#toggle-thread-colors)
* [Download](#download)
* [Advanced usage](#advanced-usage)
  + [Thread diagram as pair diagram](#thread-diagram-as-pair-diagram)
  + [Define a repeat](#define-a-repeat)
  + [Arrange the repeats](#arrange-the-repeats)
  + [Foot sides](#foot-sides)
  + [Demo section](#demo-section)


Catalogues
==========
The links from a catalogue page fill out the forms in the advanced section of the page.
These forms define a base pattern.
You can vary stitches for the base patterns and check how
contrasting threads would travel through the ground.

There are more catalogue pages than linked on the tiles page,
but most links still refer to the older home page.
_For as long as the page is still under construction,
you may also choose a link from the image captions of the demo section
on the tiles page itself._

Reload diagrams
===============
Generating the pair diagram and thread diagrams can take a while
and may block the browser while busy.
Therefore only the prototype is updated when you change some value on the page.
To avoid confusion about outdated diagrams the pair diagram and thread diagrams
are hidden on these occasions. Use the link or wand button to [reload] the diagrams.

[reload]: Undo

Patch size
==========
Note that a large patch size makes a diagram slower to render.
On slow devices like tablets and phones that might give
the impression that links to pages with a large patch size
don't work at all or block the device.
So create links with patch sizes just large enough to recognize the pattern,
visitors can easily increment the size.

When a diagram has a foot side on the right,
it will usually match only for every so many columns.
If you want another number, go the "define a repeat" section
and rotate the rows in the right field.

Some diagrams may not work with all values for the patch size.
Try other values if the thread diagram stays frozen at its initial state. 

Choose stitches
===============
The faint nodes in the prototype diagram repeat the bright ones.
Click on a bright node and a yellow form field should emerge with a stitch definition.

![](images/stitch-input.png)

Type as many `c`'s and `t`'s for cross and twist as you need for the stitch of your choice,
or use `l`'s and `r`'s for a left twist or right twist.

Some stitches to copy paste:

{% include stitches.html %}

The example below applies the last stitch above with a few more twists.

![](images/foot-side-stitches.png)

[Live version](/GroundForge/tiles.html?patchWidth=3&patchHeight=8&g1=tctcttrrctct&f1=tctct&c1=ctc&b1=tctct&f2=tctct&c2=ctc&b2=tctct&a2=tctct&footside=-7,A1&tile=8,1&headside=8D,4-&footsideStitch=tctct&tileStitch=ctc&headsideStitch=tctct&shiftColsSW=-1&shiftRowsSW=2&shiftColsSE=0&shiftRowsSE=2)

When erasing the content of a stitch field, the value from "define a repeat"
in the advanced section will be filled in, if that is empty `ctc` will be filled in.
To deliberately ignore a stitch from the prototype, you'll have to fill in a dash: `-`.
The green arcs in the diagrams below show how stitches will be reconnected in the pair diagram.
One case is straight forward. In the other case two stitches are merged
because they were were connected with parallel pairs.
Too many adjacent ignored stitches may cause weird thread diagrams
with pairs swapped before a stitch is made.

![](images/ignore-stitches.png)

[Live version](/GroundForge/tiles.html?patchWidth=12&patchHeight=13&g1=ctct&e1=ctct&c1=ctct&a1=ctct&f2=ctct&b2=-&g3=ctct&e3=ctct&c3=ctct&a3=ctct&h4=ctct&f4=-&d4=ctct&b4=ctct&g5=ctct&e5=ctct&c5=ctct&a5=ctct&f6=ctct&b6=ctct&g7=ctct&e7=ctct&c7=ctct&a7=ctct&h8=ctct&f8=ctct&d8=ctct&b8=ctct&tile=5-5-5-5-,-5---5--,B-C-B-C-,-5-5-5-5,5-5-5-5-,-5---5--,B-C-B-C-,-5-5-5-5,&footsideStitch=tctct&tileStitch=ctct&headsideStitch=tctct&shiftColsSW=0&shiftRowsSW=8&shiftColsSE=8&shiftRowsSE=8)

Toggle thread colors
====================
You may have to scroll/drag the thread diagram to make
the squares at the start of the threads visible.
Click these squares to toggle between a black or red color.

A tooltip shows a thread number when your mouse hovers over a square. 
Too tiny squares or too close together? Use the zoom function of your browser.
Usually control-shift-plus to zoom in, control-zero to reset,
on a mac use command for control.

Download
========
You can download the diagrams to edit them with an [SVG editor], manipulating vectors not pixels.

[SVG editor]: https://en.wikipedia.org/wiki/Comparison_of_vector_graphics_editors#File_format_support

Advanced usage
==============

The sections  below the diagrams are the engine under the hood of the car.
The form fields define the prototype diagram. 
Advanced users can play with the values to define new patterns from scratch.

Thread diagram as pair diagram
------------------------------

![](../images/under-construction.png) The help pages need rewriting as those on the [Droste effect](Droste-effect) are still biased to the old-style page.

No prototype diagram to choose stitches for these diagrams.
The team of needs a seasoned front end engineer for a more convenient user interface.

You can choose to use one stitch everywhere. Or two different stitches:
one for each cross in the preceding thread diagram, the other for each twist.
To make exceptions to these rules you need to hover over a stitch
in the pair diagram to discover its id for stitch assignments.

An example mixing all the options mentioned above:

![](images/assign-stitches.png)

The overall default in this example is a `ctct`.
A more selective default is `ctc` for stitches that were twists in the preceding thread diagram.
Two specific stitches are set to `ct`. 

Define a repeat
---------------
The form has three fields to fill with  digits and letters of the cheat sheet.
These fields are paired with other fields to define the default stitches.

The outer panels are optional for a custom foot side. 
Note that a column more or less for the patch size may invalidate the right foot side. 

The position of a digit correlates with a position in the prototype diagram.
Half circles in the prototype indicate you added a new pair for a next stitch,
or are not using a pair for a next stitch. The symbols with a single arrow
are intended to extend an arrow of another symbol.

More details on the [advanced design](Reversed-engineering-of-patterns) page.

The right foot side may be a mirrored version of the left foot side for some patterns.
For your convenience the flip button can set the right field from the left.
You may still have to rotate the rows or adjust the patch width.  

Arrange the repeats
-------------------
The foot sides are simply repeated vertically.
The form at the bottom defines how the center is repeated.
As this form can be hard to wrap your brains around,
you can move the tiles with arrows in an image.
These actions set the values in the form.

The prototype highlights a single tile alias repeat in the top left corner,
this repeat may span just a single column or row of stitches. 

Those who really want to understand the numbers
should not interpret them as mathematical (x,y) coordinates.
Point (0,0) lies in the north west of a computer canvas, 
where western scripts start to write on a sheet of paper.
You can also memorise by counting rows and columns as in a spread sheet, 
like used for the labels for the stitches in the pair diagram.

The purple numbers in the form define the absolute position of the solid tile.
The green numbers define the position of the arrowed green tile
relative to the solid tile.

Foot sides
----------
You may want to study how threads disappearing in foot sides return back into the ground.
Foot sides are defined in the side panels of the "define a repeat" form in the advanced section.
Note that the right foot sides depends on the chosen patch width alias number of columns.

Foot sides may require one or two columns of additional stitches.
It is a matter of piecing a puzzle together with symbols from the cheat sheet.
The symbols with a single arrow can extend an arrow of any symbol to make it fit.

Annotated screen shot snippets of an example:

![](images/foot-sides.png)

[Live version](/GroundForge/tiles.html?patchWidth=7&patchHeight=18&a3=-&footside=B,-,C,-,B,-,B,-,&tile=-5-,5-5,-5-,B-C,-5-&headside=5,-,&footsideStitch=tctctr&tileStitch=ct&headsideStitch=-&shiftColsSW=-2&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=4)

The blue and purple rectangles in this example illustrate corresponding
sections in the form, prototype diagram and pair diagram.
The yellow curves in the prototype show how extended arrows are reduced
to a single connection between stitches in the pair diagram.
Note that the twist marks only indicate multiple twists, not how many.

On the right side we only added extensions.
In practice you would need at least one column with real stitches
to choose other stitches for the foot side than for the pattern.
This example shows that extensions don't need to be a straight line. 
The bold yellow curve causes two stitches connected with two pairs.
In those cases the stitches are merged into a single stitch in the pair diagram.
Thus both pairs for the bottom stitch are stretched.
You can still choose to apply a plait for the thread diagram in those cases,
well, if it happens in an added column and is not part of the ground.
