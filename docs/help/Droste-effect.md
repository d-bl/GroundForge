---
layout: default
title: Droste effect
---

Droste effect
=============

Thread diagram as pair diagram
------------------------------
**This feature is still under construction.  We welcome any suggestions on how to improve it.**

In a typical pair diagram, two pairs intersect at each crossing and then continue.  In a thread diagram, two threads meet at each crossing and then continue.  So why not take a thread diagram, flatten all the over/under information, and use it as a pair diagram?  Using this technique, you can quickly create a new, usually more complex, pattern.  This process can be repeated over and over again, as long as your computer has enough power to handle the increased complexity.    In mathematics, this is called ["recursion"](https://en.wikipedia.org/wiki/Recursion).  In art, it is sometimes called the ["Droste effect"](https://en.wikipedia.org/wiki/Droste_effect).

![](images/simple-droste.png)

To apply this technique in GroundForge, click on the ![wand](../images/wand.png) image beside _First level_ under the _Thread diagram as pair diagram_ heading. 
 
![](images/reuse.png)

There are several options for specifying which stitches appear in the new thread diagram.

By default, every stitch is `ctc`.  You can assign a new default stitch by typing the actions (such as `ct`) in the input box that appears to the left of the new pair diagram. You can also assign a stitch to every "cross" in the original thread diagram by typing `cross=...` (for example, `cross=ctcll`) in the input box.  Similarly, you assign a stitch to every "twist" in the original thread diagram using `twist=...`.  For finer control, you can specify a stitch for a specific intersection.  First find the identity of the intersection in the new pair diagram (hover over the intersection until the id appears), then give it a new value in the input box such as `b10=clcl`.  You can combine any of these options in the input box.  Use a new line or a comma to separate each instruction. After editing the stitches in the input box, click on the ![wand](../images/wand.png) image beside _First level_ under the _Thread diagram as pair diagram_ heading. 

![](images/droste-assign-stitches.png)

To repeat this process a second time, click on the ![wand](../images/wand.png) image beside _Second level_ under the _Thread diagram as pair diagram_ heading.   This will create another new thread diagram by using the thread diagram from the first level as a pair diagram.

![](images/droste-repeat-assign-stitches.png)


## Examples

Pair diagrams of several traditional grounds are very similar to thread diagrams of more basic grounds.  The following tables show a few examples.
 
 The first table uses a basic net with different stitches.   The first column shows a thread diagram of the base pattern.  The second column gives the stitches used in the _Edit Pattern_ and _First Level_ areas.  The last column lists patterns in the Whiting catalogue that either match exactly or have minor variations.  Click on the stitch definitions in the second column to open the pattern in GroundForge.

Base  | Base &rarr; First Level    | First level     | [Whiting catalogue](/gw-lace-to-gf)
-------------------------------------------------------------------------------------|----------------|-------------|--------- 
![](stitches/ct.png) ![](stitches/ct-color1.png) | [ct &rarr; ctct]      | ![](/gw-lace-to-gf/w/page76a.gif) | A5, A6, B5, C6, [A7-H7,G11]
![](stitches/ctct.png) ![](stitches/ctct-color1a.png) ![](stitches/ctct-color1b.png) | [ctct &rarr; ct/ctct] | ![](/gw-lace-to-gf/w/page120a.gif) ![](stitches/ctct-color2a.png) ![](stitches/ctct-color2b.png) | A2, B2, B6, C6
![](stitches/crclct.png) | [crclct &rarr; ct/ctct] | ![](/gw-lace-to-gf/w/page139a.gif) ![](stitches/crclct-color2.png) | C6
![](stitches/clcrclc.png) | [clcrclc &rarr; ctc and twists] | ![](stitches/clcrclc-color2.png)
![](stitches/ctctc.png) | [ctctc &rarr; ctc and twists] | ![](/gw-lace-to-gf/w/page178a.gif) | F2

In the following table, the "cloth motif" pattern is the base pattern.

Base  | Base &rarr; First Level    | First level     | [Whiting catalogue](/gw-lace-to-gf)
-------------------------------------------------------------------------------------|----------------|-------------|--------- 
![](stitches/ctc-ctcr.png) ![](stitches/ctc-ctcr-color.png)  | [ctct/ctcl &rarr; ctc/tt/tctct] | ![](/gw-lace-to-gf/w/page150a.gif) | D16
![](stitches/2x-ct-ctct.png) | [ct/ctct &rarr; ct/ctct] | "tilted" rose ground


[ct &rarr; ctct]: /GroundForge/tiles?tile=-5&tileStitch=ct&droste2=ctct&patchWidth=5&patchHeight=6&shiftColsSW=-2&shiftRowsSW=0&shiftColsSE=1&shiftRowsSE=1
[ctct &rarr; ct/ctct]: /GroundForge/tiles?patchWidth=5&patchHeight=6&b1=ctct&tile=-5&footsideStitch=ctctt&tileStitch=ctct&headsideStitch=ctctt&shiftColsSW=-2&shiftRowsSW=0&shiftColsSE=1&shiftRowsSE=1&droste2=ct,cross=ctct
[crclct &rarr; ct/ctct]: /GroundForge/tiles?tile=-5&tileStitch=crclct&droste2=ctct,b16=b15=b12=ct&patchWidth=5&patchHeight=6&shiftColsSW=-2&shiftRowsSW=0&shiftColsSE=1&shiftRowsSE=1
[clcrclc &rarr; ctc]: /GroundForge/tiles?tile=-5&tileStitch=clcrclc&droste2=ctc,b16=ctct,b13=ctcr,b15=ctcl&patchWidth=5&patchHeight=6&shiftColsSW=-2&shiftRowsSW=0&shiftColsSE=1&shiftRowsSE=1
[clcrclc &rarr; ctc and twists]: /GroundForge/tiles?tile=-5&tileStitch=ctctc&droste2=ctc,B16=ctcttt,B15=ctcrrr,B14=ctclll,b13=ctcctc&patchWidth=5&patchHeight=6&shiftColsSW=-2&shiftRowsSW=0&shiftColsSE=1&shiftRowsSE=1
[ctctc &rarr; ctc and twists]: /GroundForge/tiles?tile=-5&tileStitch=ctctc&droste2=ctc,B16=ctcttt,B15=ctcrrr,B14=ctclll,b13=ctcctc&patchWidth=5&patchHeight=6&shiftColsSW=-2&shiftRowsSW=0&shiftColsSE=1&shiftRowsSE=1

[ctct/ctcl &rarr; ctc/tt/tctct]: /GroundForge/tiles?tile=8,1&a1=ctct&a2=ctcl&droste2=ctc,a24=a15=tt,a14=tctct&patchWidth=4&patchHeight=4&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=1&shiftRowsSE=2
[ct/ctct &rarr; ct/ctct]: /GroundForge/tiles?tile=88,11&tileStitch=ctct&b1=ct&a2=ct&droste2=cross=ctct,twist=ct&patchWidth=6&patchHeight=6&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2
[A7-H7,G11]: /gw-lace-to-gf#val


Experiment
-------------------------

For the base pattern, start with something small.  For example, enter an even number of alternating rows of `8`'s and `1`'s or `-`'s and `5`'s in the _Pattern definition_ area of _Edit pattern_. Then click on the blue square of the simple layout (see below).  Next click on the ![wand](../images/wand.png) image beside _First level_ under the _Thread diagram as pair diagram_ heading. Remember to keep the swatch size small.  A large swatch size may be very slow. 

![](images/init-droste.png)

Try a variety of stitches in the base pattern and the first and second level patterns.  Here are some to consider:

{% include stitches.html %}