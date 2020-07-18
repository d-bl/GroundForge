---
layout: default
title: Help Intro
lang: en
ref: intro
---

Help Intro
==========
+ [Experimenting with bobbin lace grounds](#experimenting-with-bobbin-lace-grounds)
+ [Virtual tour](#virtual-tour)
+ [Overview Tutorial](#overview-tutorial)
   + [Highlight a thread](#highlight-a-thread)
   + [Resize pattern](#resize-pattern)
   + [Modify stitches](#modify-stitches)
   + [Undo changes](#undo-changes)
   + [Save and share changes](#save-and-share-changes)
   + [Add footsides](#add-footsides)
   + [Create a new ground pattern](#create-a-new-ground-pattern)
   + [Video](#video)
   

Experimenting with bobbin lace grounds
--------------------------------------

GroundForge is a tool that complements existing [lace ground catalogues](https://maetempels.github.io/MAE-gf/docs/license#literature) by making it possible to play with their designs.
GroundForge generates an interactive thread diagram for each ground in the catalogue which allows you to highlight the path of a thread; a useful feature when working with multiple thread colours or thicknesses in a design.
The tool also allows you to specify the combinations of stitches that are made when two pairs meet.  You can make changes and quickly observe how this affects the thread diagram.
This greatly speeds up the process of experimenting with lace grounds; no need to wind bobbins or prepare prickings. The final test is always to make a hand-worked sample&mdash;only in a physical sample can the interactions between pins and threads under tension be completely observed&mdash;however, GroundForge can help you narrow the number of samples you need to test by hand.
You can experiment and play around with the hundreds of patterns provided here, or you can create your own.
Even when reinventing the wheel, your own discoveries can bring great joy.


Virtual tour
------------

Take a walk through the various catalogues on the GroundForge website in this [screencast](https://github.com/d-bl/GroundForge/releases/download/2019-Q2/catalogues.mp4) (MP4 format, 123 MB, 2:32 min). You will notice that Rose ground, and many variations related to it, appears in each of the catalogues.  
Different stitches for the same pair diagram yield very different results.  Pause, rewind, and slow down the video to get the most out of it.

Overview Tutorial
-----------------

In the following tutorial, we will explore a versatile ground from the [Whiting catalogue] and illustrate some basic features of GroundForge.
By clicking on a photo in the catalogue, you will launch the associated page in Mrs Whiting's book *A lace guide for makers and collectors*.  
Click on the [diagram] link above the photo in the catalogue to launch the GroundForge tool, initialized with the details of the selected Whiting ground.

For each lace ground, there is a [dedicated page](../tiles.html).
The page contains a prototype diagram where the ground and its stitches are defined.
There are also two generated drawings: a working diagram (based on the International Colour Code) and a thread diagram.
Help pages, accessible by clicking on the ![info](images/information-icon.png) image above each diagram, provide more information.

Open the [diagram] for ground G-2 (column G, row 2) in the [Whiting catalogue].

[Whiting catalogue]: /gw-lace-to-gf/

[diagram]: /GroundForge/tiles?whiting=G2_P199&patchWidth=14&patchHeight=13&f1=ctctt&a1=ctcctc&j2=ctc&i2=ctcll&h2=ctctt&g2=ctctt&f2=ctctt&e2=ctctt&d2=ctctt&c2=ctcrrr&b2=ctc&j3=ctcll&i3=ctctt&h3=ctctt&g3=ctcttl&f3=ctc&e3=ctcttr&d3=ctctt&c3=ctctt&b3=ctcrrr&a3=ctc&j4=ctctt&i4=ctctt&h4=ctcttl&g4=ctc&f4=ctc&e4=ctc&d4=ctcttr&c4=ctctt&b4=ctctt&a4=ctcttt&j5=ctctt&i5=ctcttl&h5=ctc&g5=ctc&e5=ctc&d5=ctc&c5=ctcttr&b5=ctctt&a5=ctctt&tile=5----5----,-CDD632AAB,5666632222,5666632222,56666-2222&tileStitch=ctct&shiftColsSW=-5&shiftRowsSW=5&shiftColsSE=5&shiftRowsSE=5

### Highlight a thread

In the thread diagram on the far right, click on the gray square at the start of a thread.  You may need to scroll to the top of the diagram to see it.  The colour of the thread will change from black to red.  Click on the square again and the thread colour returns to black.  You can highlight more than one thread at a time.  For more colours and options, visit [thread properties](Thread-Properties).

![](images/G2-toggle-thread.png)

### Resize pattern

You can increase or decrease the size of the pattern generated in the working and thread diagrams. Above the prototype diagram, edit the number of columns and rows. There are some [restrictions to patch sizes](Patch-Size). If you make the patch size very large, performance may slow down considerably.

### Modify stitches

To play with the stitches, look at the prototype diagram on the far left.
The prototype contains a vector drawing showing the order in which pairs of threads come together.
There are multiple copies of the ground pattern in the prototype, showing how the pattern repeats.
One copy is brightly coloured, the others appear faded.  We will always work with the brightly coloured copy.

![](images/G2-stitch-editor.png)

Where two pairs come together, there is a circle with a number or letter in its centre.  Click on any of these circles and a yellow box appears with the stitch definition.  To change the stitch, type the new instructions while the yellow box is visible.

Exercise: Try to change the center of the spider from 'ctcctc' to 't'.  (Spoiler alert: The center of the spider is the black circle around the symbol '5', located in the top left corner of the prototype).

Every time you change the stitch definitions, you must regenerate the working and thread diagrams by clicking on the ![wand](../images/wand.png) image found just above the prototype diagram.

In addition to the usual 'c' for cross and 't' for twist, you can twist only the right pair by typing 'r' or type 'l' to twist only the left pair.
You can also "drop" a stitch.  That is, when two pairs meet, do not braid them together at all.  To drop a stictch, replace the stitch instructions with '-'.  Be careful when dropping stitches.  It can have unusual consequences.

Exercise: Can you create the following variation? [solution](/GroundForge/tiles?whiting=G2_P199&patchWidth=20&patchHeight=20&f1=-&a1=t&j2=ctc&i2=ctclll&h2=ctctt&g2=ctctt&f2=ctctt&e2=ctctt&d2=ctctt&c2=ctcrrr&b2=ctc&j3=ctclll&i3=ctctt&h3=ctctt&g3=ctcttl&f3=ctc&e3=ctcttr&d3=ctctt&c3=ctctt&b3=ctcrrr&a3=ctc&j4=ctctt&i4=ctctt&h4=ctcttl&g4=ctc&f4=ctc&e4=ctc&d4=ctcttr&c4=ctctt&b4=ctctt&a4=ctcttt&j5=ctctt&i5=ctcttl&h5=ctc&g5=ctc&e5=ctc&d5=ctc&c5=ctcttr&b5=ctctt&a5=ctctt&tile=5----5----,-CDD632AAB,5666632222,5666632222,56666-2222&footsideStitch=ctctt&tileStitch=ctct&headsideStitch=ctctt&shiftColsSW=-5&shiftRowsSW=5&shiftColsSE=5&shiftRowsSE=5)

![](images/G2-modified.png)

### Undo changes

To return the pattern to its original values, click on the 'Reload' button (also called 'Refresh') at the top of your browser.

### Save and share changes

Once you have invested some time working on a lace ground, you will want to save your work.  Click on the ![link](../images/link.png) image just above the prototype diagram.  This will change the text in the address bar at the top of your browser.  The text is a link to this website and contains the lace pattern as well as the stitch choices you have made.

![](images/save-link.png)

Save the link text into a file for later use.  Copy and paste the link text into the address bar of your browser to return to your current state or share it with other lacemakers.

### Create a new ground pattern

We create the prototype for the pattern in the area labelled “Forms for advanced users”. First, we map the pattern onto a grid. Symbols, comprising numbers or letters, are used to draw lines on the grid. This creates a vector diagram showing the order in which pairs of threads come together. From this description, the software generates a working diagram and a thread diagram. It requires a more [detailed tutorial](Advanced) to fully explain the many construction options available.

<a name="BK-31"></a>
Feature 31 in the [DKV pattern](http://www.deutscher-kloeppelverband.de/index.php/component/jshopping/product/view/4/47?Itemid=242) "Binche Kompakt" was published in "Kant" issue 2000/2.  [Variations](https://github.com/d-bl/GroundForge/blob/b6a765f/docs/help/index.md#tutorial) on this once lost ground can be explored in GroundForge.

### Add footsides

In addition to modelling a lace ground, GroundForge can also model a footside for it.  Left and right footsides are defined separately and depend on the number of columns in the patch.
For example, take a look at [Whiting G-2 with footsides](/GroundForge/tiles?whiting=G2_P199&patchWidth=12&patchHeight=20&j1=ctctt&e1=ctcctc&c1=ctc&b1=ctc&q2=ctctt&n2=ctc&m2=ctc&l2=ctctt&k2=ctctt&j2=ctctt&i2=ctctt&h2=ctctt&g2=ctc&f2=ctc&d2=ctc&c2=ctcrr&b2=ctc&a2=ctctt&r3=ctctt&q3=ctctt&n3=ctc&m3=ctctt&l3=ctctt&k3=ctctt&j3=ctc&i3=ctctt&h3=ctctt&g3=ctctt&f3=ctc&e3=ctc&d3=ctcll&c3=ctc&b3=ctcll&r4=ctctt&q4=ctctt&n4=ctctt&m4=ctctt&l4=ctctt&k4=ctc&j4=ctc&i4=ctc&h4=ctctt&g4=ctctt&f4=ctctt&e4=ctc&d4=ctctt&c4=ctcrr&b4=ctc&a4=ctctt&r5=ctctt&n5=ctctt&m5=ctctt&l5=ctc&k5=ctc&i5=ctc&h5=ctc&g5=ctctt&f5=ctctt&e5=ctctt&d5=ctctt&c5=ctc&b5=ctcll&q6=ctctt&c6=ctcrr&b6=ctc&a6=ctctt&r7=ctctt&q7=ctctt&d7=ctctt&c7=ctc&b7=ctcll&a7=ctctt&q8=ctctt&d8=ctctt&c8=ctcrr&b8=ctc&r9=ctctt&q9=ctctt&d9=ctcll&c9=ctc&b9=ctcll&a9=ctctt&q10=ctctt&d10=ctcll&c10=ctcrr&b10=ctc&footside=X14-,4886,-111,B883,X111,488-,1115,X786,1114,X786&tile=5----5----,-CDD632AAB,5666632222,5666632222,56666-2222&headside=XX,8X,37,37,-7,5X,17,7X,27,7X,&footsideStitch=ctc&tileStitch=ctctt&headsideStitch=ctctt&shiftColsSW=-5&shiftRowsSW=5&shiftColsSE=5&shiftRowsSE=5).
There is an advanced tutorial on how to [create footsides](Advanced#foot-sides).

### Video

To see these features, and others, in action, we have recorded the editing of a Binche lace ground.
The [screencast](https://github.com/d-bl/GroundForge/releases/download/2019-Q2/BK-31.mp4) (MP4, 35 MB, 1:47 min) follows the script below.
Steps marked in bold are more advanced and can be skipped by novice users. 

* 0:00 start with this help page (an older version is recorded)
* 0:00 follow "_with_"
* 0:05 drop center stitch: `ctc` -> `-`
* 0:10 ![wand](../images/wand.png) generate new diagrams
* 0:18 reload web page : restores original diagrams
* **0:19** scroll down to advanced section
* **0:22** drop center stitch: `353,153` -> `-5-,5-5-`
* **0:35** ![wand](../images/wand.png) : different prototype, same pair/thread diagram
* 0:41 follow (i)
* 0:44 follow "_stitches_"
* 0:45 copy edge stitch
* 0:49 previous browser tab
* 0:52 paste the copied stitch and edit into turning stitch.
       Note that ctc, twist one pair, ctc is a single stitch for GroundForge.
       It makes the runner changing direction, therefore: turning stitch. 
* 1:19 ![link](../images/link.png)
* 1:23 reload web page
* 1:26 current pattern is reloaded
* 1:27 create favourite
* 1:33 "_animate_" pair diagram
* 1:36 increase thread diagram container (alternative: ![](../images/size-inc.jpg) ![](../images/size-dec.jpg))
* 1:38 scroll down to get thread container back into view
* 1:42 toggle thread color when tooltip displays thread number
