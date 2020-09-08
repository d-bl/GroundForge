---
layout: default
title: Advanced forms
---

Advanced forms
==============

[lace ground page]: /GroundForge/tiles?tile=5831,-4-7&patchWidth=9&patchHeight=9&shiftColsSE=4&shiftRowsSE=2&shiftColsSW=0&shiftRowsSW=2&

+ [Prototype Tutorial](#prototype-tutorial)
   + [Define the base pattern](#define-the-base-pattern)
   + [Glue copies together](#glue-copies-together)
   + [Initialize stitches](#initialize-stitches)
+ [Footside Tutorial](#footside-tutorial)
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

Next we will map the pairs in the lace ground to a grid.  Within one repeat rectangle, the intersections lie on two columns and four rows as shown by the red dashed lines in the figure below.  The red dashed lines form a grid.

![](images/rose-grid-symbols.png)

For each position in the grid, we assign a symbol.  The symbol specifies the angle and direction of the two pairs that are pointing at that row/column position.
The meaning of each symbol is shown in the "Cheat sheet" on the right of the "Define a repeat" area.  For example, in row 1/column 1 the green arrows correspond to the symbol '4'.  Similarly, in row 3/column 2, there is no intersection of pairs which we represent by the symbol '-'.

We can now fill in the middle section of the "Define a repeat" area.

![](images/rose_vertical_brick_definition.png)

Notice that the prototype diagram is updated to match the symbols as soon as you click outside of the box.

### Glue copies together

Now that the base pattern is defined, we need to specify how to connect copies of this pattern together.  This is done in the area labelled _Arrange the repeats_.

Consider several meters of a lace edging.  The pattern for the edging is not several meters long.  It is a smaller pattern that you repeat over and over again by sliding the pattern along in one direction.  This gives a long strip.  To make a large rectangular patch from a small pattern, we need to slide the pattern in two directions.

Consider the image below. Red dashed lines show the grid overlaid on the pattern.

![](images/rose-translation-vectors.png)

To position the blue rectangle on top of the yellow rectangle, we must slide the blue rectangle down 4 rows.
To position the blue rectangle on top of the red rectangle, we must slide it over 2 columns and down 2 rows.  These translations are entered in the configuration information below.

![](images/rose_vertical_brick_arrange.png) 

If you are more comfortable with visual tools, we have also provided images that you can click on.
For a vertical brick pattern like our Rose ground example, first click on the picture of vertical bricks:

![](images/vertical-bricks.png)

If the prototype pattern does not immediately align correctly, click on the black arrows above and below the solid purple brick to adjust how the two columns of bricks align.  
Each click will move the second column up or down by one row relative to the first column.  Look at the prototype diagram to see how the copies move around.

[Final result](/GroundForge/tiles?patchWidth=13&patchHeight=10&a1=ctct&b2=ctct&a2=ct&a3=ctct&b4=ctct&a4=ct&tile=4-,15,7-,83,,&footsideStitch=ctctt&tileStitch=ctct&headsideStitch=ctctt&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=2)

### Another Rose ground example
There is more than one way to choose the base pattern.  
Starting from scratch again, we will think about Rose ground as a checkerboard pattern of repeated squares.
In the figure below, the coloured squares each contain one copy of the pattern.  

![](images/rose-checker-arrange.png)

Again we want to make sure that none of the line intersections are on the edge of the base pattern.  This time we make the base pattern a little bit larger, as shown in the second drawing from the left, to encompass any intersections along its border.  Notice that the squares overlap a little bit.

As before, we apply a grid to the base pattern and assign symbols to each line intersection.  This time we have three rows and three columns in the base pattern.
Finally, we arrange the copies.  To slide the blue square over the yellow square, we must move it down 2 rows and left 2 columns.  To slide the blue square over the green square, we must move it right 2 columns and down 2 rows.  Note: The pattern has three rows and three columns but the squares overlap in the corner, hence the slides are only a distance of two.

![](images/rose-checker-define-repeat.png) 

You can enter these slide moves directly into the Configuration area.
If you are more comfortable with visual tools, click on the image of a checkerboard with black arrows:

![](images/overlapping-checkers.png)

Initially, the copies will be placed corner to corner.  Click on the black arrows to nudge the copies so that they overlap.

[Final result](/GroundForge/tiles?patchWidth=12&patchHeight=12&c1=ctct&b1=ctct&a1=ctct&c2=ctct&a2=ctct&c3=ctct&b3=ctct&a3=ctct&tile=831,4-7,158&footsideStitch=ctctt&tileStitch=ctct&headsideStitch=ctctt&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)

### Initialize stitches

For convenience, you can set all stitches in the pattern to the same value.  In the "Define a repeat" area, just below the middle box where you typed symbols for the prototype, there is a small text box.  Enter a stitch as a sequence of 'c', 't', 'r' or 'l' (for example, 'cttct').  Click on the "initialize stitches button" and then on the ![wand](../images/wand.png) image above the prototype diagram to regenerate the thread and pair diagrams.  This is a quick way to see how a ground looks in cloth-stitch, half-stitch or whole-stitch.  Once initialized, you can change the stitches used for individual intersections by editing the prototype diagram.  Each time you click on "initialize stitches button", the values in the prototype are reset.

Footside Tutorial
-----------------
In the "Define a repeat" section, the central panel defines the ground, and the left and right side panels define the left and right footsides.

![](images/definition_labels.png)

The leftmost edge of the central patch is determined by the leftmost column in the ground definition.  If you want to change where the patch starts on the left, you must choose a different starting point for the base pattern and modify the ground definition accordingly.

The rightmost edge of the patch depends on the number of columns specified in the "Patch size".  If you change the number of columns in the patch size, the right footside may no longer match correctly.  Note that the number of columns required for the footsides are not included in the "Patch size".  They are added on top of the width that you specify.

First, we will consider the simple example of Torchon Ground.  Lay a square grid over the pair diagram. A "kissing path" is a path that follows one set of pairs in the pair diagram.  When two pairs intersect, the kissing path does not cross to the other side, it just "kisses" and continues.  You can think of it as the path that a pair of threads would follow if a turning stitch (cttct) is used at each intersection.  Trace the kissing paths for the pairs at the edge of the pair diagram, shown in yellow and blue below.  Add a kissing path for the last pair (shown in red below).  Finally, label the incoming pair intersections using the symbols in the Cheat Sheet.

![](images/simple-footside.png)

Below is the completed footside definition. Footside definitions are repeated in one direction, like a frieze pattern.  In this example, the definition for the left footside of the Torchon ground is "X,4" so the left footside will be "X,4,X,4,X,4,..." for as many rows as are in the patch. Note that in this example, the footside definition has two rows while the ground definition only has one row.  The ground and footside definitions may not always have the same number of rows.

![](images/simple-footside-definition.png)

Now consider the more complicated example of [G-4] in the Whiting sampler.  We follow the same steps: lay a grid over the pair diagram and trace out the kissing paths on the left edge of the pattern.  Note that this time the blue and green kissing paths are not complete.  There are missing sections meaning that pairs are left hanging.  For the left footside, we need to complete the blue and green kissing paths.  We also added a red kissing path to make a clean straight edge. In total, three columns were required to define this footside.

![](images/complex-footside.png)

Below is the completed footside definition.  Notice the red box around "V,6" in the ground area of the prototype.  In the original definition, this was "-,L" which draws a long horizontal edge.  The long edge stuck out on the left side of the patch.  We can get a better looking footside by shortening this edge.  In order to get a short edge on the left side of the patch, we changed the ground definition to use two short horizontal edges in a row ("V,6") instead of one long edge ("-,L").  The symbols "V,6" and "-,L" look the same in the thread diagram.  On the footside, we can cut the ground pattern off at "6" which gives a nicer looking result (see red box around "C,6", the "C" intersection is part of the footside.  The "6" intersection is part of the ground).  We invite you to experiment with [G-4 with left footside] and develop a right footside.

![](images/complex-footside-definition.png)

In these examples, the footside has ended in a simple straight line.  Othe roptions are possible.  You can define a wavy footside or you can make the footside more elaborate to include several passives.

[G-4]: /GroundForge/tiles?whiting=G4_P201&patchWidth=40&patchHeight=19&i1=ctctt&f1=ctc&e1=ctc&d1=ctc&c1=ctc&a1=ctctt&g2=ctc&i3=ctc&f3=ctc&e3=ctc&d3=ctc&c3=ctcll&a3=ctctt&n4=ctctt&l4=ctctt&j4=ctctt&h4=ctctt&f4=ctt&d4=ctcll&c4=ctcll&b4=ctctt&g5=ctctt&c5=ctctt&n6=ctctt&j6=ctctt&m7=c&k7=ctc&j7=ctc&i7=ctctt&g7=ctctt&e7=ctctt&c7=ctctt&a7=ctctt&tile=5-m998-z5-----,------5-------,g-aaab-wd-----,-246-m-l-o-k-e,--5---5---y-w-,---w-y---b---c,h-g-5-n-l3h-e-,&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=-7&shiftRowsSW=7&shiftColsSE=7&shiftRowsSE=7

[passives]: /GroundForge/tiles?patchWidth=19&patchHeight=21&y1=ctcttr&g1=ctct&a1=ctcttl&x2=ctc&w2=ctc&h2=ct&f2=ct&d2=ct&c2=ctc&b2=ctc&x3=ctcrr&w3=ctc&i3=ctct&g3=ctc&e3=ctct&d3=ct&c3=ctc&b3=ctcll&y4=ctcttr&x4=ctc&w4=ctc&h4=ctc&f4=ctc&c4=ctc&b4=ctc&a4=ctcttl&i5=ctc&h5=ctc&g5=ctc&f5=ctc&e5=ctc&d5=ct&g6=ctc&y7=ctcttr&x7=ctcrr&w7=ctc&i7=ctcr&h7=ctc&g7=ctc&f7=ctc&e7=ctcl&d7=ct&c7=ctc&b7=ctcll&a7=ctcttl&x8=ctc&w8=ctc&h8=ctcr&f8=ctcl&d8=ct&c8=ctc&b8=ctc&i9=ctct&g9=ctct&e9=ctct&x10=ctcrr&w10=ctc&h10=ct&f10=ct&d10=ct&c10=ctc&b10=ctcll&footside=B--,XCD,-11,B88,XXX,---,AAA,X78,X--,-AA&tile=---5--,D-B-C-,15-5-5,--5-5-,C63532,---5--,AAB3CD,8-5-5-,-5-5-5,B-5-5-&headside=--C,ABX,88-,11C,XXX,---,DDD,14X,--X,DD-&footsideStitch=ctct&tileStitch=ctc&headsideStitch=ctct&shiftColsSW=0&shiftRowsSW=10&shiftColsSE=6&shiftRowsSE=5

[G-4 with left footside]: /GroundForge/tiles?patchWidth=28&patchHeight=40&l1=ctctt&i1=ctc&h1=ctc&g1=ctc&f1=ctc&d1=ctctt&j2=ctcrr&l3=ctctt&i3=ctc&h3=ctc&g3=ctc&f3=ctcll&d3=ctctt&q4=ctctt&o4=ctctt&m4=ctctt&k4=ctctt&i4=ctt&g4=ctcll&f4=ctcll&e4=ctctt&c4=ctctt&a4=ctctt&j5=ctctt&f5=ctctt&q6=ctctt&m6=ctctt&c6=ctctt&p7=c&n7=ctc&m7=ctc&l7=ctctt&j7=ctctt&h7=ctctt&f7=ctctt&d7=ctctt&c7=ctctt&c9=ctctt&a9=ctctt&c11=ctctt&c12=ctctt&c14=ctctt&a14=ctctt&footside=X--,X--,---,G-E,XW-,X-C,X-1,-Y-,B-B,XW-,X-C,X-4,-Y-,B-N&tile=5-m998-z5-----,------5-------,g-aaab-wd-----,-246-mv6-o-k-e,--5---5---y-w-,---w-y---b---c,h-g-5-n-l3h-e-,&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=-7&shiftRowsSW=7&shiftColsSE=7&shiftRowsSE=7

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
