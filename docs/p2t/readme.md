A technical birds eye overview.
===============================


The `GF_svgP2T` object in `svgPairsToThreads.js` provides functions for generating thread diagrams from color coded pair diagrams for bobbin lace.

The most important functions:
* `init` and `readSvgFile` take care of I/O and listen to upload events.
* `processUploadedSvg` calls
  * `coyModifiedTemplateToDoc`
  * `addCaptionedLegendElementsToDoc` iterates over elements of an uploaded `#bdqpLegend` element.  
    * to call `newLegendStitch` which in turn calls `newStitch`.
  * `addThreadDiagramToDoc`, this iterates over elements of an uploaded `#template` element
    * to call `newStitch` which generates an indivudual stitch. The stitches are not yet connected to one another.
* `addThreadClasses` is currently called stitch by stitch. Once the stitches are connected,
  it should be called for the whole pair diagram.


Uploads
-------

Uploads are sanitized. This excludes `<use>` elements, because of href attributes.
In practise this means we don´t get the swatches from the symmetry page.
We will get the dots next to the swatches.
The titles of these dots provide tooltips with the swatch parameters.
Neither the swatches nor the dots are used in the script.

Page structure
--------------

The page has little static content: a button and its label to upload files.
The dynamic content is rendered by JavaScript:
* A modified version of the template found in the upload. This is a color-coded pair diagram.
  The style attribute is replaced to allow static and interactive styling with CSS.
* Tthe color code and text label from the legend in the upload become captions for little thread diagrams.
* A larger version of the thread diagram and smaller set of bdpq versions.
  These mirrored version are intended to build swatches.
  Currently, the d and p versions have the wrong over/under effect.
  The b (top-left) and q (bottom-right) versions are correct.

Function newStitch
------------------

Core function: parse a stitch notation string (e.g., "ctc", "ctlr") and create an SVG thread diagram.

The following images show steps leading to a thread diagram from `ctc`.

![](stitch-stages.svg)

A `t` makes two pairs of nodes at the same height kiss one another.
This explains why the following two stitches have the same color code, 
will be also identical in real lace, yet are drawn differently.

![](same-or-not.png)

Classes for the SVG elements provide structural information.
We have two groups of classes for edges: starts/ends_left/right_at_<node-id> and starts/ends_white.
The edges also inherit the kissing path number of the original four paths.
This kissing path number (and corresponding color) helped to debug the direction of bends for repeated actions.
In `styles.css` you can uncomment the `.kiss_` rules at the bottom to override the thread colors,
the thread diagrams still needs debugging.

Composing the thread diagram
----------------------------

The position of stitches in the thread diagram is defined by the position of stitches in the pair diagram.
Currently, the stitches all get the same size and orientation.
This is okay for a torchon net, when just dropping stitches from a torchon net,
we get no more problems than gaps in threads.

When adding and/or moving stitches we run into problems.
The following image is an overlay of the thread diagram, the pair diagram
and green shapes with corners at the mid-points of edges around the noes of the pair diagram.

![](envelopes.svg)

The green shapes are drawn a little smaller to avoid confusion.
The shapes along the perimeter apply to a swatch with only b tiles without indents.
Other titling will need different shapes along the perimeter.

From the green squares and corresponding thread-diagram stitches at the same position
we learn that the bounding box for the stitches are 50% wider than the square.

One stitch appears outside its green shape: it means trouble when washing the real lace.

From repeating the template we learn something from the third stitch in the top row:
The twist are defined on edges between stitches. The corners of the shapes on those edges should move outwards
and the twists rendered separately, not as part of either stitch

### Jumping to wrong conclusions

As an alternative to some matrix transformation to distort the stitches after creation, 
we could start the kissing paths as shown in blue below, 
after we increased the top and bottom of the green shape by 50%. 
The arrows on the red lines indicate how to move the nodes on the kissing pairs together for a ctc stitch.

![](init-kissing.svg)

Instead, it seems we should initialize the kissing threads bending along with the kissing pairs.
Like sketched below for one stitch. Not sure how that will look like.

![](init-bend-kissing.svg)

This looks like we need lines perpendicular to the edges of the pair diagram.
These can determine the start and end points of the initial kissing paths.
The sketch below has bright green for starting points and brownish green for end points.
The length of these perpendicular lines depend on the legth of the edges
but also on the angle with neighboring edges.

![](perpendicular.svg)