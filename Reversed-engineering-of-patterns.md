Different methods allow to reproduce a pattern with GroundForge.
The easiest methods to experiment with grounds are explained in the [main] page.
The [gallery] explains that plaits and something-pin-something are drawn as plain cloth stitches.
Furthermore are the distances between stitches optimized to some average distance.
So you may have to simplify the pattern, mirror and mould it to recognize it in the gallery. 


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
This method is too error prone to prepare with pencil and paper. Two methods can prevent mistakes.

* Use the SVG template (<a href="https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/template.svg" download="GroundForge-template.svg">download</a>, [preview])
  and a third-party general purpose editor like Adobe-Illustrator, CorelDraw or the free InkScape.
  The objects are shown two times on the template, once in a compact group, once in an alpha-numerical order.
  The symbol in each object has six free snapping points, two of them should be connected with other objects.
  Copy-paste these objects to assemble a pattern without changing the length or directions of lines connecting the symbols. It might need some out-of-the-box thinking: the Binche snow flake example below has horizontal connections and the vertical connection is reduced to a single stitch. Stretching the stitch into a kind of plait with a hole (`ctcttctc`) reveals traditional connections in the thread diagram.

[preview]: https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/template.svg

* The other method requires a patch of the lace (don't sew/knot off) and squared paper.
  Make the pattern with a few repeats as usual but with tcptc at each stitch and very large pin distances.
  The large distances should allow you to move the pins to the corners of the squares.
  Diagonal pairs should span just a single square,
  horizontal and vertical pairs should span one or at most two squares.
  If you can't meet these requirements, it is not possible to generate the pattern with GroundForge.
  If you can meet the requirements, look up the symbols from the template for each pin.

The next step is figuring out the tiling. The tiles in the example are surrounded with a red-dotted line and stacked as bricks in a wall. An alternative stacking method looks like a checker board though the tiles can be rectangles. Finally read the matrix from the symbols within one tile. Note that empty spots require a dash as shown on [choose stitches](Choose-Stitches), it would get confusing with this example.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/matrix-example.png)

[main]: https://d-bl.github.io/GroundForge/
[gallery]: https://d-bl.github.io/GroundForge/gallery.html
[snow flake]: https://d-bl.github.io/GroundForge/?tiles=bricks&matrix=L3H-AB-CD-%0D%0A6-2H-256-L%0D%0A-5----5---&stitches=ctc+H3%3Dctcttctc+A1%3Dctcll+B2%3Dctcll+E1%3Dctcrr+D2%3Dctcrr&rows=12&cols=14&left=1&up=1&transparency=0&#steps