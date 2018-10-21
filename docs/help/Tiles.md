Tiles
=====

The [tiles] page is a variant of the [home] page.
On the tiles page you can't use thread diagrams as pair diagrams,
but you don't need to wrap your brains around [assigning] stitches to id's.
It has only two colors in the thread diagram,
but you don't need to [calculate] thread counts
to give the desired ones a color.

[tiles]: /GroundForge/tiles.html?tile=5831,-4-7&patchWidth=9&patchHeight=9&shiftColsSE=4&shiftRowsSE=2&shiftColsSW=0&shiftRowsSW=2&
[home]: /GroundForge
[assigning]: /GroundForge/help/Choose-Stitches#assign-stitches
[calculate]: /GroundForge/help/Thread-Colors


Catalogues
----------
The links from a catalogue page fill out the forms below the diagrams.
These forms define a base pattern, advanced users may design
new base patterns with these forms.

You can vary stitches for the base patterns and check how
contrasting threads would travel through the ground.

There are more catalogue pages than linked on the tiles page,
but the other pages link to the [home] page.
_For as long as the page is still under construction,
you may also choose a link from the image captions of the demo section
on the tiles page itself._

Invisible diagrams
------------------
Under circumstances only one of the three diagrams is shown.
Use the link button (&infin;) to show all diagrams.

Reason:
Updating the other diagrams could use too much resources and block your system.
Outdated diagrams are cleared to avoid confusion about the state of the page.

Benefits:
Using the link button allows you to step back to that set of diagrams
with the back button of your browser.
The link button also allows you to create links to a diagram to share
on your web-site, blog or where ever you like.
The long links may be a problem in emails. You may consider to tuse a
[shortening service](https://en.wikipedia.org/wiki/URL_shortening)
for those occasions.

Patch size
----------
Note that a large patch size make a diagram slower to render.
On slow devices like tables and phones that might give
the impression that links to pages with large patch size don't work at all.
So create links with small patch sizes, visitors can easily increment.

Not all diagrams work with all values for the patch size.
Try other values if the thread diagram freezes at its initial state. 

Generating the diagrams at each increment or decrement would slow down
the process of changing values if not block the the computer altogether.
To prevent confusion about the state of the diagrams, they are cleared.
when you are done wih the planned changes you can use the link button
to show diagrams again. 

Choose stitches
---------------
The faint nodes in the prototype diagram are repeats of the bright ones.
Click on a bright nodes and a yellow form field should emerge with a code
for a stitch.
Type as many `c`'s and `t`'s as you need for the stitch of your choice,
or use `l`'s and `r`'s for a left twist or right twist.

{% include stitches.html %}

Microsofts Edge browser won't show the yellow form field when clicking a node.
Use the tab key or shift-tab to browse through all fields and links on the page.

Toggle thread colors
--------------------
You may have to scroll/drag the pair diagram to make
the squares at the start of the threads visible.
Click these squares to toggle between a black or red color.

A tooltip shows when your mouse hovers over a square. 
Too tiny squares or too close together? Use the zoom function of your browser.
Usually control-shift-plus to zoom in, control-zero to reset,
on a mac use command for control.

Download
--------
You can download the diagrams to edit them with an [SVG editor].
Details for just one type of diagrams on the [reshape patterns](Reshape-Patterns) page.

[SVG editor]: https://en.wikipedia.org/wiki/Comparison_of_vector_graphics_editors#File_format_support

Advanced usage
--------------
The sections below the diagram are the engine under the hood of the car.
The form fields define the prototype diagram. 
Advanced users can play with the values to define new patterns from scratch.

### Define a repeat

You have three fields to fill with  digits and letters of the cheat sheet.

The outer fields are optional for a custom foot side. 
Note that a column more or less for the patch size may invalidate the right foot side. 
It may not always be possible to define a custom foot sides,
some attempts between the demo patterns did not yet succeed. 

The position of a digit correlates with a position in the prototype diagram.
Half circles in the prototype indicate you added a new pair for a next stitch,
or are not using a pair for a next stitch.

More details on the [advanced design](Reversed-engineering-of-patterns) page.

### Arrange the repeats

The configuration at the bottom of the section is like 
the crank for the first car models with ignition keys:
you should not need it.
The image with linked components at the top of the section is the ignition key.

Those wo nevertheless want to understand the numbers
should not interpret them as mathematical (x,y) coordinates.
Point (0,0) lies in the north west of a computer canvas, 
where western scripts start to write on a sheet of paper
or how you count rows and columns in a spread sheet
as the labels for the numbers tell.

The purple numbers define the absolute position of the solid tile.
The green numbers define the position of the arrowed green tile
relative to the solid tile.

### Demo section

The demo section is introduced for quick testing during development and bug fixing.
The introduction at the very top of the page links to catalogues with many more patterns.