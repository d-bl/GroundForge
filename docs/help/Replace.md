---
layout: default
title: Replace stitches
---
[intro]: /GroundForge/help/intro

Replace / skip stitches
=======================
The second screen-cast video on the [intro] page shows how to change or replace stitches. 
The faint nodes in the prototype diagram repeat the bold ones.
Click on a bold node to make a yellow form field appear with a stitch definition.

![](images/stitch-input.png)

Type as many `c`'s and `t`'s for cross and twist as you need for the stitch of your choice,
or use `l`'s and `r`'s for a left twist or right twist.

Some stitches to copy paste:

{% include stitches.html %}

The example below applies the last stitch above with a few more twists.

![](images/foot-side-stitches.png)

[Life version](/GroundForge/tiles?patchWidth=3&patchHeight=8&g1=tctcttrrctct&f1=tctct&c1=ctc&b1=tctct&f2=tctct&c2=ctc&b2=tctct&a2=tctct&footside=-7,A1&tile=8,1&headside=8D,4-&footsideStitch=tctct&tileStitch=ctc&headsideStitch=tctct&shiftColsSW=-1&shiftRowsSW=2&shiftColsSE=0&shiftRowsSE=2)

When erasing the content of a stitch field, the value from "define a repeat"
in the advanced section will be filled in, if that is empty `ctc` will be filled in.
To deliberately ignore a stitch from the prototype, you'll have to fill in a dash: `-`.
The green arcs in the diagrams below show how stitches will be reconnected in the pair diagram.
One case is straight forward. In the other case two stitches are merged
because they were were connected with parallel pairs.
Too many adjacent ignored stitches may cause weird thread diagrams
with pairs swapped before a stitch is made.

![](images/ignore-stitches.png)

[Life version](/GroundForge/tiles?patchWidth=12&patchHeight=13&g1=ctct&e1=ctct&c1=ctct&a1=ctct&f2=ctct&b2=-&g3=ctct&e3=ctct&c3=ctct&a3=ctct&h4=ctct&f4=-&d4=ctct&b4=ctct&g5=ctct&e5=ctct&c5=ctct&a5=ctct&f6=ctct&b6=ctct&g7=ctct&e7=ctct&c7=ctct&a7=ctct&h8=ctct&f8=ctct&d8=ctct&b8=ctct&tile=5-5-5-5-,-5---5--,B-C-B-C-,-5-5-5-5,5-5-5-5-,-5---5--,B-C-B-C-,-5-5-5-5,&footsideStitch=tctct&tileStitch=ctct&headsideStitch=tctct&shiftColsSW=0&shiftRowsSW=8&shiftColsSE=8&shiftRowsSE=8)
