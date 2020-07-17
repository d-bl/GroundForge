---
layout: default
title: Advanced forms
---

Advanced forms
==============

[lace ground page]: /GroundForge/tiles?tile=5831,-4-7&patchWidth=9&patchHeight=9&shiftColsSE=4&shiftRowsSE=2&shiftColsSW=0&shiftRowsSW=2&

+ [Define the base pattern](#define-the-base-pattern)
+ [Arrange the repeats](#arrange-the-repeats)
+ [Foot sides](#foot-sides)
+ [Thread diagram as pair diagram](#thread-diagram-as-pair-diagram)

On each [lace ground page], below the prototype, pair and thread diagrams, you will find an area labelled _Forms for advanced users_.  These forms are the controls for creating and editing the prototype.
You can use these controls to modify an existing pattern (add a headside or a footside, for example) or to create a new pattern.

Prototype Tutorial
------------------
We will use Rose ground as an example and define a prototype for it from scratch.
There are many ways to draw the pair diagram for Rose ground.
One way to think of it is as a checkerboard with a diamond inside each of the black squares.

### Define the base pattern
A lace ground consists of a small pattern that is repeated, like a wallpaper or printed fabric pattern, to cover the area you need to fill.
This small base pattern is called a __repeat__.  The form labelled _Define a repeat_ contains three text boxes.  
The left and right boxes control the headside and footside.  We will discuss them later.  The middle box controls the repeated pattern.

From the large pattern, we need to identify the base pattern.  There are many ways to do this and over the course of this tutorial, we will show a few of them.
In the image below, each rectangle highlights one repeat of the Rose pattern.  The repeating rectangles are arranged like vertical bricks.
For convenience, on the far right of the image we have moved the rectangles slightly so that it is easy to see all the intersections of pairs within a rectangle (no intersection lies on the edge of a rectangle).

![](images/repeat.png)

Next we will map the pairs in the lace ground to a grid.  Within one repeat rectangle, the intersections lie on two columns and four rows as shown by the red dashed lines in the figure below.  The red dashed lines form a grid

![](images/rose-grid-symbols.png)

For each position in the grid, we assign a symbol.  The symbol specifies the angle and direction of the two pairs that are pointing at that row/column position.
The meaning of each symbol is shown in the "Cheat sheet" on the right of the "Define a repeat" area.  For example, in row 1/column 1 the green arrows correspond to the symbol '4'.  Similarly, in row 3/column 2, there is no intersection of pairs we represent by the symbol '-'.

We can now fill in the middle section of the "Define a repeat" area.

![](images/rose_vertical_brick_definition.png)

### Glue copies together

Now that the base pattern is defined, we need to specify how to connect copies of this pattern together.  This is done in the area labelled _Arrange the repeats_.

Consider several meters of a lace edging.  The pattern for the edging is not several meters long.  It is a smaller pattern that you repeat over and over again by sliding the pattern along.  This gives a long strip.  To make a rectangle from a small pattern, we need to slide the pattern in two directions.

Cosndier the image below. Red dashed lines show the grid overlaid on the pattern.

![](images/rose-translation-vectors.png)

To position the blue rectangle on top of the yellow rectangle, we must slide it down 4 rows.
To position the blue rectangle on top of the red rectangle, we must slide it over 2 columns and down 2 rows.  These translations are entered in the configuration information below.

![](images/rose_vertical_brick_arrange.png)

The parameters to arrange the tiles can be hard to understand.
An image with four diagrams should simplify the task.
The filled tiles and arrows act as buttons that change the configuration values
and move the faint stitches in the prototype diagram.

![](images/arrange-tiles.png)

The top left tile in each diagram of the image represents the bold set of stitches in the prototype diagram.
Each solid tile in the linked image represents a copy made of the top left tile
alias the bold stitches in the prototype.
The tiles with a slightly fainter purple edge represent copies of the chosen solid tile.
The tiles with a green stroke are copies of the tiles with a purple fill or stroke.

The purple numbers in the form define the absolute position of the filled purple tile,
and the relative position of subsequent purple tiles.
The green numbers define the position of one row of green tiles relative to the purple tiles.

The recommended procedure:
- Select a filled purple tile that closely matches the pattern.
- Use the arrows around the purple to nudge the purple row in place.
- Use the arrows around the green tile to nudge the rest in place.

Try the procedure with [rose ground](https://d-bl.github.io/GroundForge/tiles?patchWidth=9&patchHeight=10&c1=ctct&b1=ct&a1=rctctt&c2=ctct&a2=ctct&b3=ctct&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-3&shiftRowsSW=3&shiftColsSE=3&shiftRowsSE=3)
and stitch the tiles together with the links in the overlapping diagram.
Note that another pattern than traditional rose ground is possible.

Those who really want to understand the numbers
should not interpret them as mathematical (x,y) coordinates.
Point (0,0) lies in the north west of a computer canvas, 
where western scripts start to write on a sheet of paper.
You can also memorise by counting rows and columns as in a spread sheet, 
like used for the labels for the stitches in the pair diagram.

When a link specifies no shift values at all, you'll get a checkerboard arrangement.

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

[Life version](/GroundForge/tiles?patchWidth=7&patchHeight=18&a3=-&footside=B,-,C,-,B,-,B,-,&tile=-5-,5-5,-5-,B-C,-5-&headside=5,-,&footsideStitch=tctctr&tileStitch=ct&headsideStitch=-&shiftColsSW=-2&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=4)

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

Thread diagram as pair diagram
------------------------------

Sorry, nothing like the prototype diagram to choose stitches for these sets of diagrams.
The team of GroundForge could use the help of a seasoned front end engineer for a more convenient user interface.

You can choose to use one stitch everywhere. Or two different stitches:
one for each cross in the preceding thread diagram, the other for each twist.
To make exceptions to these rules you need to hover over a stitch
in the pair diagram to discover its id for stitch assignments.

An example mixing all the options mentioned above:

![](images/assign-stitches.png)

The overall default in this example is a `ctct`.
A more selective default is `ctc` for stitches that were twists in the preceding thread diagram.
Two specific stitches are set to `ct`.

The page on the [Droste effect](Droste-effect) has some example patterns.

![](../images/under-construction.png) Note that spaces may have unexpected results, 
recommended delimiters between assignments: new lines or `,`.

Foot sides can get complicated to define when a pattern has a worker.
At the second and third level two respective four pairs
work from left to right before returning from right to left.
In practice it might be very well possible to alternate the direction every other row.
