---
layout: default
title: Modify stitches
---

Modify stitches
=======================
One of the diagrams shows faint and bright stitches.
The faint stitches repeat the bright ones.
Click slightly north-east of a bright stitch to open a yellow input field. 
Don't use your mouse to position the cursor inside the input field,
you would most likely select another input field.
User the arrows on your keyboard instead. 

![](images/stitch-input.png)

Code stitches
-------------

Type as many `c`'s and `t`'s for cross and twist as you need for the stitch of your choice,
or use `l`'s and `r`'s for a left twist or right twist.
Some stitches to copy paste:

{% include stitches.html %}

The example below applies the bottom (mirrored) version of the last stitch above with a few more twists.
You can [try](/GroundForge/tiles?patchWidth=3&patchHeight=8&g1=tctcttrrctct&f1=tctct&c1=ctc&b1=tctct&f2=tctct&c2=ctc&b2=tctct&a2=tctct&footside=-7,A1&tile=8,1&headside=8D,4-&footsideStitch=tctct&tileStitch=ctc&headsideStitch=tctct&shiftColsSW=-1&shiftRowsSW=2&shiftColsSE=0&shiftRowsSE=2)
variations.

![](images/foot-side-stitches.png)

Drop stitches
-------------
To deliberately ignore a stitch from the prototype, define the stitch with just a dash: `-`.
Empty stitch fields fall back to a default.
This default can be definen by yourself as in the form of the screen shot above.
If even that field is empty you'll get a `ctc`.

_Warning_: too many adjacent dropped stitches may cause weird thread diagrams
with pairs swapped before a stitch is made.

Reconnected stitches
--------------------
The green annotations in the screen shot below show how stitches will be reconnected in the pair diagram.
One case is straight forward. In the other case stitches are merged two by two,
becouse GroundForge considers stitch-pin-stitch as a single stitch in pair diagrams.

The ![animate](../images/animate.png) button makes rubber bands of each connection between stitches.
Applying this button can bridge the gap between the shapes of the holes in the pattern definition and pair diagrams. 

You can [try](/GroundForge/tiles?patchWidth=12&patchHeight=13&g1=ctct&e1=ctct&c1=ctct&a1=ctct&f2=ctct&b2=-&g3=ctct&e3=ctct&c3=ctct&a3=ctct&h4=ctct&f4=-&d4=ctct&b4=ctct&g5=ctct&e5=ctct&c5=ctct&a5=ctct&f6=ctct&b6=ctct&g7=ctct&e7=ctct&c7=ctct&a7=ctct&h8=ctct&f8=ctct&d8=ctct&b8=ctct&tile=5-5-5-5-,-5---5--,B-C-B-C-,-5-5-5-5,5-5-5-5-,-5---5--,B-C-B-C-,-5-5-5-5,&footsideStitch=tctct&tileStitch=ctct&headsideStitch=tctct&shiftColsSW=0&shiftRowsSW=8&shiftColsSE=8&shiftRowsSE=8)
variations.

![](images/ignore-stitches.png)

