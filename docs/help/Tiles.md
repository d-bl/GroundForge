---
layout: default
title: Tiles
---
The [tiles] page is a variant of the older home page.
The [site map] provides the context for both and lists their differences.

[tiles]: /GroundForge/tiles.html?tile=5831,-4-7&patchWidth=9&patchHeight=9&shiftColsSE=4&shiftRowsSE=2&shiftColsSW=0&shiftRowsSW=2&
[site map]: /GroundForge/help/Site-map

* [Catalogues](#catalogues)
* [Link or wand](#link-or-wand)
* [Patch size](#patch-size)
* [Choose stitches](#choose-stitches)
* [Toggle thread colors](#toggle-thread-colors)
* [Download](#download)
* [Advanced usage](#advanced-usage)
  + [Define a repeat](#define-a-repeat)
  + [Arrange the repeats](#arrange-the-repeats)
  + [Foot side trick](#foot-side-trick)
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

Link or wand
============
Generating the pair diagram and thread diagrams can take a while
and may block the browser while busy.
Therefore only the prototype is updated when you change some value on the page.
To avoid confusion about outdated diagrams the pair diagram and thread diagrams ar hidden on these occasions.

Both the link button and wand button will show the diagrams again.
The wand button will be faster as it doesn't reload the page.

The link button reloads the page with the configured diagrams.
The back button of your browser can step back to these actions.
These actions also allow you to create links to a diagram to share
on your web-site, blog or where ever you like.
The long links may be a problem in emails. You may consider to use a
[shortening service](https://en.wikipedia.org/wiki/URL_shortening)
for those occasions or cut the stitch arguments (sections like `xxx=ctc&`)
out of the link.

Patch size
==========
Note that a large patch size makes a diagram slower to render.
On slow devices like tablets and phones that might give
the impression that links to pages with a large patch size
don't work at all or block the device.
So create links with patch sizes just large enough to recognize the pattern,
visitors can easily increment the size.

When the prototype diagram has a foot side on the right,
it will usually match only for every so many columns.
If you want another number, go the "define a repeat" section
and rotate the rows in the right field.

Some diagrams may not work with all values for the patch size.
Try other values if the thread diagram stays frozen at its initial state. 

Choose stitches
===============
The faint nodes in the prototype diagram repeat the bright ones.
Click on a bright nodes and a yellow form field should emerge with a code
for a stitch.
Type as many `c`'s and `t`'s as you need for the stitch of your choice,
or use `l`'s and `r`'s for a left twist or right twist.

{% include stitches.html %}

Microsofts Edge browser won't show the yellow form field when clicking a node.
Use the tab key or shift-tab to browse through all fields and links on the page.

Toggle thread colors
====================
You may have to scroll/drag the pair diagram to make
the squares at the start of the threads visible.
Click these squares to toggle between a black or red color.

A tooltip shows when your mouse hovers over a square. 
Too tiny squares or too close together? Use the zoom function of your browser.
Usually control-shift-plus to zoom in, control-zero to reset,
on a mac use command for control.

Download
========
You can download the diagrams to edit them with an [SVG editor].
Details for just one type of diagrams on the [reshape patterns](Reshape-Patterns) page.

[SVG editor]: https://en.wikipedia.org/wiki/Comparison_of_vector_graphics_editors#File_format_support

Advanced usage
==============
The sections below the diagram are the engine under the hood of the car.
The form fields define the prototype diagram. 
Advanced users can play with the values to define new patterns from scratch.

Define a repeat
---------------
You have one row with three fields to fill with  digits and letters of the cheat sheet.
The other row defines the default stitches for the sections above.

The outer fields are optional for a custom foot side. 
Note that a column more or less for the patch size may invalidate the right foot side. 

The position of a digit correlates with a position in the prototype diagram.
Half circles in the prototype indicate you added a new pair for a next stitch,
or are not using a pair for a next stitch.

The right foot side may be a mirrored version of the left foot side for some patterns.
For your convenience the flip button can set the right field from the left.
You may still have to rotate the rows or adjust the patch width.  

More details on the [advanced design](Reversed-engineering-of-patterns) page.

Arrange the repeats
-------------------
The foot sides are simply repeated vertically, but the centre section has more options. 

The configuration at the bottom of the section is more or less like 
the crank for the first car models with ignition keys:
the hard way to start your car but usually not needed.
The image with linked components at the top of the section is
like the ignition key: the simple way to arrange the tiles.

The prototype highlights a single tile alias repeat in the top left corner,
this repeat may span just a single column or row of stitches. 

Those wo nevertheless want to understand the numbers
should not interpret them as mathematical (x,y) coordinates.
Point (0,0) lies in the north west of a computer canvas, 
where western scripts start to write on a sheet of paper
or how you count rows and columns in a spread sheet
as the labels for the numbers tell.

The purple numbers define the absolute position of the solid tile.
The green numbers define the position of the arrowed green tile
relative to the solid tile.

Foot side trick
---------------
A trick allows to create foot sides in the thread diagram.
It is a trick because the pair diagram won't reflect the the thread diagram along the edges.
* Extend the pattern in the foot side fields under "define a repeat".
  The screenshots annotate these sections with dark green borders.
* Initialize the stitches with `-`, `cttct`, `-`.
  * The `cttct` keeps all pairs parallel in a zig-zagging way.
    Toggling the color of some of the pairs might give a clue where to trim the lace.
  * A dash means "do nothing" at this spot, allowing to trim the lace while a footside emerges.
    Don't worry about the dangling bobbins next to the pattern.
* Try wich stitches you need for the desired footside.
  On the left what Mary Niven calls the old edging of flanders lace, on the right the modern edging.
* Additional twists in the foot side pair can prevent distortion of the diagram.
  For the path taken by contrasting pairs in the final design it is only important
  whether the number of twists are odd or even.
* The traditional cloth stitch pairs of traditional footsides won't influence the paths of contrasting pairs.
  
![](/GroundForge/help/images/foot-sides.png)

Demo section
------------
The demo section is introduced for quick testing during development and bug fixing.
The introduction at the very top of the page links to catalogues with many more patterns.
