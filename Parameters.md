Patch size
------------
For larger diagrams in the sense of more stitches, increase rows and columns in the parameters form.
Note that the numbers don't reflect the number of pairs.

_Warning_ : With complex stitches and large numbers of rows and columns, your browser can get overwhelmed when showing the diagrams of level two or three.

Footsides
---------

To try another foot side, change the shift values in the parameters form. The algorithm however is limited and may produce bugs for some values in some patterns. 18 out of 824 patterns (including the pricking variations) may suffer from bug [#93](https://github.com/d-bl/GroundForge/issues/93),
play with the shift values to workaround the problem. So far just one pattern doesn't work at all.


Pattern: Matrix and tiling
--------------------------

The [example pages](Example) initialize the parameters with a valid set of values and may recommend experiments.
You can also [design](Reversed-engineering-of-patterns) your own patterns from scratch.


An example
----------

In theory a pattern can be repeated endlessly in both directions.
A generated diagram is a cutout or patch,
for example the red rectangle in the image below.
The example has eight rows, nine columns and is shifted by one in both directions.

_Due to a bug you might need a stitch in both upper corners._

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/cutout.png)