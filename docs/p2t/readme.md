A technical birds eye overview
===============================

The file `svgPairsToThreads.js` defines a JavaScript object called `GF_svgP2T`, 
which provides a set of functions for processing and generating SVG diagrams.
Color coded pair diagrams provide instructions for a bobbin lace maker.
Thread diagrams visualise how the threads flow through the lace.
Given a pair diagram, the script generates the corresponding thread diagram.

Various classes on the SVG elements have different functions:
* Chances for custom presentation by CSS rules and interaction in pages displaying the diagrams.
* Structural information for further processing.

Call structure
--------------

### High-level Call Flow

* `init()` is the main entry point and sets up the application.
  * On file upload or demo load:
    * `readSVGFile()`
    * `processUploadedSvg()`
      * `coyModifiedTemplateToDoc()`
      * `addCaptionedLegendElementsToDoc()` iterates over elements of an uploaded `#bdqpLegend` element.
      * `addThreadDiagramToDoc()` iterates over elements of an uploaded `#template` element
* `newStitch()` is called by functions that need to draw stitches, including:
  * `newLegendStitch()`
  * `addThreadDiagramToDoc()`
* `addThreadClasses()` is currently called at the end of newStitch().
  The structural classes for the thread diagram are still under construction. 
  Once complete, this function will be called by `addThreadDiagramToDoc()` and `newLegendStitch()`.

### Summary Table:

| Function                        | Called By                                  | Calls (main)                                                                             |
|---------------------------------|--------------------------------------------|------------------------------------------------------------------------------------------|
| init                            | (external, on page load)                   | readSVGFile <br> processUploadedSvg                                                      |
| readSVGFile                     | init, (file upload)                        | (none)                                                                                   |
| processUploadedSvg              | init, (file upload)                        | coyModifiedTemplateToDoc <br> addCaptionedLegendElementsToDoc <br> addThreadDiagramToDoc |
| coyModifiedTemplateToDoc        | processUploadedSvg                         | newSVG                                                                                   |
| addCaptionedLegendElementsToDoc | processUploadedSvg                         | newLegendStitch                                                                          |
| addThreadDiagramToDoc           | processUploadedSvg                         | newSVG <br> newStitch                                                                    |
| newLegendStitch                 | addCaptionedLegendElementsToDoc            | newSVG <br> newStitch                                                                    |
| newStitch                       | newLegendStitch <br> addThreadDiagramToDoc | addThreadClasses, (several local helpers)                                                |
| addThreadClasses                | newStitch                                  | (none)                                                                                   |

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
  The style attributes are replaced to allow static and interactive styling with CSS.
  Also, some classes are added to simplify the conversion to a thread diagram.
* The color code and text label from the legend in the upload become captions for little thread diagrams.
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
We have multiple groups of classes for edges:
* starts/ends_left/right_at_<node-id>
* starts/ends_white
* thread_<nr> (nodes also have a thread number for the thread on top of the stitch)
* kiss_<nr> (nodes have two kiss numbers, one for each thread/pair)
This kissing path number (and corresponding color) 
helped to debug the direction of bends for repeated actions.
Hint: The developer tools of the mayor browsers have a style editor. 
Uncomment the `.kiss_` rules at the bottom of `styles.css` to override the thread colors.

Composing the thread diagram
----------------------------

The position of stitches in the thread diagram is defined by the position of stitches in the pair diagram.
Currently, the stitches all get the same size and orientation.
This is okay for a torchon net, when just dropping stitches from a torchon net,
we get no more problems than gaps in threads.

When adding and/or moving stitches we run into more problems.
The following image is an overlay of the demo diagrams: the thread diagram, the pair diagram
and green shapes with corners at the mid-points of edges around the noes of the pair diagram.

![](envelopes.svg)

The green shapes are drawn a little smaller to avoid confusion.
The shapes along the perimeter apply to a swatch with only b tiles without indents.
Other titling will need different shapes along the perimeter.

One stitch appears outside its green shape: it means trouble when washing the real lace.

From repeating the template we learn something from the third stitch in the top row:
The twist are defined on edges between stitches. 
In the thread diagrams, the twists should also be drawn independently, not as part of either stitch.


### Reshaping stitches

When widening the green squares in the drawing above by 50%, 
we get the bounding boxes for the currently generated thread stitches.
That works well for a plain Torchon ground.
As an alternative to some matrix transformation to distort the stitches after creation, 
we could start the kissing paths as shown in blue below. 
The arrows on the red lines indicate how to make th threads kiss for a ctc stitch.

![](init-kissing.svg)

However, the green shapes discussed so far, are no basis for bounding shapes of stitches.
Instead, it seems we should initialize the kissing threads bending along with the kissing pairs.
Like sketched below for one stitch. Not sure how that will look like.

![](init-bend-kissing.svg)

The sketch below has bright green lines traversing along starting points of kissing paths
and brownish green traversing along end points of kissing paths.
These lines start and end with segments perpendicular to adjacent edges in the pair diagram.
The length of these segments depend on the length of the edges 
as well as the angle with neighboring edges.
The three segments might need more or less the same length.
This may be a conflicting requirements.

![](perpendicular.svg)