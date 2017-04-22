Different methods allow to reproduce a pattern with GroundForge.

- [Recognize patterns](#recognize-patterns)
- [2-Step method](#2-step-method)
- [Matrix from pair diagram](#matrix-from-pair-diagram)

<sub><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></sub>

Recognize patterns
==================

The easiest methods to experiment with grounds are explained in the [main] page.
The generated pair diagrams don't care about plaits and something-pin-something and draw them as plain cloth stitches.
Furthermore are the distances between stitches optimized to some average.
So you may have to simplify the desired pattern, mirror and mould it to recognize it in the gallery. 

An example:

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/recognize.png)

* left: the desired pattern
* 2nd: simplified something-pin-something to plain stitches
* 3rd: snapping stitches to grid positions, they happen to abide the rules for a valid matrix
  (diagonal connections span just one square, horizontal and vertical span one or two squares)
* 4th: a partially squeezed version that also abides the rules
* top right: the matching pattern from the [gallery]
* bottom right: a flipped version of the stitches on the [main] page also matches

Not all patterns abiding matrix requirements are that easy to match with a pattern from the gallery as shown below.
In that case forget the grid when moulding to look for a matching pattern or stitch.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/recognize2.png)

2-Step method
==============

The image below tries to show how to dissect a pair diagram as if it is a thread diagram.
The blue shapes enclose stitches with two pairs alias threads.
The red shapes collect stitches into a repeat with three rows of the diagonal base pattern, its matrix:

    5-
    -5
    5-

The weaving base pattern has always a checkerboard tiling. The diagonal base pattern needs a brick tiling with an odd number of rows and a checkeboard tiling with an even number of rows.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/disect-pairs-as-threads.png)

The ID calculator can sometimes shift a column.
The effect for this particular matrix is shown in the table below.
Fixing this bug would break existing links beyond control of the project.
As usually the patterns are distorted beyond recognizing the underlying matrix,
fixing the bug would introduce a worse problem.

Hover over stitches in the pair diagram to find out the actual ID of a stitch.
At step one we need the following stitch definitions to reproduce the pattern at step two.

    A1=ct B2=cr B3=crcl

The result of the mistaken ID calculator:

|  .  |     |  A  |  B  |     |  A  |  B  |
| --- | --- | --- | --- | --- | --- | --- |
|  1  |     | A1  | B1  |     | A1  | B1  |
|  2  |     | A2  | B2  |     | A2  | B2  |
|  3  |     | B3  | A3  |     | B3  | A3  |
|     |     |     |     |     |     |     |
|  1  |     | B1  | A1  |     | B1  | A1  |
|  2  |     | B2  | A2  |     | B2  | A2  |
|  3  |     | A3  | B3  |     | A3  | B3  |

Matrix from pair diagram
========================

A template contains objects to analyse the matrix required for the parameters.
Each object represents a stitch and the pairs used to make the stitch.
The objects consist of a symbol and two arrows.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/matrix-template.png)

The purpose is to copy-paste these objects together to something that looks like
a (distorted) version of the desired pattern.
This method can be quite error prone.

When using pencil and paper start with the simplifications and moulding as described under [Recognize patterns](#recognize-patterns). Then lookup the digits or letters from the template.

You can also use a general purpose editor like Adobe-Illustrator, CorelDraw or the free InkScape
and the SVG template (<a href="https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/template.svg" download="GroundForge-template.svg">download</a>, [preview]).
The objects are shown two times on the template, once in a compact group, once in an alpha-numerical order.
The symbol in each object has six free snapping points, two of them should be connected with other objects.
Copy-paste these objects to assemble a pattern without changing the length or directions of lines connecting the symbols. It might need some out-of-the-box thinking: the Binche snow flake example below has horizontal connections and the vertical connection is reduced to a single stitch. Stretching the stitch into a kind of plait with a hole (`ctcttctc`) reveals traditional connections in the thread diagram.

[preview]: https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/template.svg

The next step is figuring out the tiling. The tiles in the example are surrounded with a red-dotted line and stacked as bricks in a wall. An alternative stacking method looks like a checker board though the tiles can be rectangles. Finally read the matrix from the symbols within one tile. Note that empty spots require a dash as shown on [choose stitches](Choose-Stitches), it would get confusing with this example.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/matrix-example.png)

[main]: https://d-bl.github.io/GroundForge/
[gallery]: https://d-bl.github.io/GroundForge/gallery.html
[snow flake]: https://d-bl.github.io/GroundForge/?tiles=bricks&matrix=L3H-AB-CD-%0D%0A6-2H-256-L%0D%0A-5----5---&stitches=ctc+H3%3Dctcttctc+A1%3Dctcll+B2%3Dctcll+E1%3Dctcrr+D2%3Dctcrr&rows=12&cols=14&left=1&up=1&transparency=0&#steps