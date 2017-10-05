- [Patch size: columns and rows](#patch-size--columns-and-rows)
- [Footsides: shift](#footsides--shift)
- [Pattern: Matrix and tiling](#pattern--matrix-and-tiling)
- [Illustration](#illustration)


<sub><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></sub>


This page is about the sets of fields in the parameters form on the main [GroundForge] page.

[GroundForge]: /GroundForge/

Patch size: columns and rows
----------------------------

For larger diagrams in the sense of more stitches, increase the number of rows and columns in the parameters form.
Note that the numbers don't reflect the number of pairs.

_Warning_ : With complex stitches and large numbers of rows and columns, your browser can get overwhelmed when showing the diagrams of level two or three.


Footsides: shift
----------------

To try another foot side, change the shift values in the parameters form.

_Under construction_

Chosen stitches will no longer match the pattern,
proper pair/thread diagrams might break,
a self designed pattern however might get fixed.

[One] pattern from the [TesseLace-Index](TesseLace-Index) works only for 5 columns.
18 more out of 449 TesseLace patterns (including the pricking variations) may suffer from bug [#93](https://github.com/d-bl/GroundForge/issues/93).

[One]: /GroundForge/?m=B-B-%20-B-B%20C-C-%20-C-C%3Bbricks%3B12%3B5%3B0%3B0&s1=ct%20A1%3Dct&s2=cross%3Dctc%20twist%3Dctc&s3=


Pattern: Matrix and tiling
--------------------------

The [example pages](Example) initialize the parameters with a valid set of values
and may recommend experiments.

The letters and digits in the text field define the connections between the dots within
the green rectangle in the top left of the illustration.
With non-zero shift values for the footsides, columns and rows are virtually moved to the end.

The tiling defines how the green section is repeated in the pattern.
Usually only one tiling value is valid for a particular matrix.

The diagram button will show the configured pattern (pricking prototype) on another tab.

More on the page to [design](Reversed-engineering-of-patterns) your own patterns from scratch.


Illustration
------------

The diagram below illustrates the initial state of an animated pair diagram.

The green rectangle in the left upper corner is exactly one repeat of the pattern.

The red rectangle is a cutout or patch of the pattern.
Compare the top-left corner with the green repeat:
it is shifted by one, both horizontally and vertically.
The patch has nine columns but uses only six pairs.

![](/GroundForge/images/cutout.png)