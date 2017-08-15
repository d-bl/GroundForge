Footsides
---------

To try another foot side, change the shift values in the parameters form.
The algorithm however is limited and may produce bugs for some values in some patterns.
18 out of 824 patterns (including the pricking variations) may suffer from bug [#93](https://github.com/d-bl/GroundForge/issues/93),
play with the shift values to workaround the problem. So far just one pattern doesn't work at all.


Pattern: Matrix and tiling
--------------------------

The [example pages](Example) initialize the parameters with a valid set of values and may recommend experiments.

The letters and digits in the text field define the dots within the green rectangle of the image below.
With non-zero shift values for the footsides, columns and rows are virtually moved to the end.

The tiling defines how the green section is repeated in the pattern.
Usually only one tiling value is valid for a particular matrix.

More on the page to [design](Reversed-engineering-of-patterns) your own patterns from scratch.


Patch size
------------

For larger diagrams in the sense of more stitches, increase rows and columns in the parameters form.

_Warning_ : With complex stitches and large numbers of rows and columns, your browser can get overwhelmed when showing the diagrams of level two or three.

Note that the numbers don't reflect the number of pairs.
The diagram below illustrates the initial state of the animated pair diagram.
The red rectangle is a cutout or patch, shifted by one in both directions.
The patch has nine columns but uses only six pairs.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/cutout.png)