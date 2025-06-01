High level documentation of the script
======================================

A technical birds eye overview.

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
The dynamic content is rendered by JavaScipt:
* A [modified](https://github.com/d-bl/GroundForge/blob/731783fbf208789e0882ce9fd7e0c6fb8d0df94d/docs/p2t/svgPairsToThreads.js#L318-L324)
  version of the template found in the upload. This is a color-coded pair diagram.
  The modifications simplify further processing.
* Legend elements from the upload: the text label, the color code and a thread diagram.
* A larger version of the thread diagram and smaller set of bdpq versions.
  These mirrored version are intended to build swatches.
  Currently, the d and p versions have the wrong over/user effect.
  The b (top-left) and q (bottom-right) versions are correct.

Function newStitch
------------------

The function is used for the legend entries as wel as stitches in the pair diagram.

The function starts with four paths, each with as may nodes as there are legal characters to define a stitch.
Legal characters are ctlr. A loop over the characters dictates between which paths nodes should meet in the middle:
The two left, two right, two center or (in case of a t) two left and two right paths.
Some of the original nodes are not moved together. They are deleted and their edges merged.

Classes for the SVG elements provide structural information.
We have two groups of classes for edges: starts/ends_left/right_at_<node-id> and starts/ends_white.
The edges also inherit the kissing path number of the original four paths.
This kissing path number (and corresponding color) helped to debug the direction of bends for repeated actions.
Not sure this number has any further use.
You can reveal the kissing path colors with the inspector of your browser:
remove the [thread styles](https://github.com/d-bl/GroundForge/blob/0d59a2d3183c57987966bb6827e230c7b4718129/docs/p2t/index.html#L10-L13).

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