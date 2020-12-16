---
layout: default
title: History of GroundForge
---


How it's Made / third party data and scripts
============================================

Proof of concept with D3.js
---------------------------

To get a [proof of concept] a force graph [example] with originally v3 of [D3.js] was changed into tiny thread an pair diagrams diagrams with the following steps:

- Replaced the server side JSon with the hard-coded [js/sample.js] assembled from a manual sketch [js/sample.png].
- Applied arrow heads and flattened them to line ends to emulate a [color coded pair diagrams] or to emulate the over/under effect in thread diagrams. Later versions create the over/under effect with shortened lines, for better browser support and performance, a too complicated approach for the multiple colors of a color coded diagram.
- Made nodes transparent except for bobbins.
- Assigned the thread number as a class to each section of a thread to assign colors.
- Turned the links from lines to paths with a third node to add mid-markers for twist marks.
- Initial coordinates replace the default random values, thus the animation stabalizes much quicker and it prevents rotated and flipped diagrams.

[proof of concept]: https://cdn.rawgit.com/d-bl/GroundForge/84eee36/index.html
[example]: http://bl.ocks.org/mbostock/4062045
[D3.js]: http://d3js.org/
[js/sample.js]: https://github.com/d-bl/GroundForge/blob/7a94b67/js/sample.js
[js/sample.png]: https://github.com/d-bl/GroundForge/blob/50421a2/js/sample.png
[color coded pair diagrams]: https://en.wikipedia.org/w/index.php?title=Mesh_grounded_bobbin_lace&oldid=639789191#Worker_pair_versus_two_pair_per_pin


Using data from TesseLace
-------------------------

Scientific research presented at [TesseLace] resulted in patterns defined by matrices.
These patterns are alternatives for the `js/sample.js` used for the proof of concept.
GroundForge uses a compact matrix format using a character to tag a node.
The character defines the [composition] of incoming pairs of the node.

The outdated class [TesselaceThumbs] generated the SVG versions of the images for the former TesseLace Index, 
which can still be found under `help` in this [zip](https://github.com/d-bl/GroundForge/releases/download/2019-Q2/GroundForge-pages.zip).
A batch script with InkScape exports converted these images into PNG. 
The diagrams lack the original geometric information after completion of the animation,
so topological duplicates were merged into single images.
The geometric variations are provided via a link, they can be downloaded as SVG documents
and customised into intermediate and other variations that are not bound to a square grid.

The matrices are converted to node and link objects to define pair diagrams.
The [D3jsSVG] object turns these nodes and links to the initial SVG documents.
The geometric information within the matrices is used for the initial coordinates of the nodes.
JSon data generated from these [node] and [link] objects provides a [hook]
for the D3js library to [adjust] the coordinates of the nodes. 

The thread diagrams are generated from the nodes and links of the pair diagram.
Each node is replaced with a set of linked nodes as defined by the sequence of twist and cross actions.
To keep track of the threads while constructing the diagram, 
the algorithm figures out a working order to create the lace just like a real lace maker does.

[adjust]: https://github.com/d-bl/GroundForge/blob/master/docs/js/tiles.js#L105
[hook]: https://github.com/d-bl/GroundForge/blob/master/docs/js/tiles.js#L91-L93
[D3jsSVG]: https://github.com/d-bl/GroundForge/blob/master/src/main/scala/dibl/D3jsSVG.scala
[link]: https://github.com/d-bl/GroundForge/blob/918ab7aa3601e709475aa4b80baa388f2bd1161e/src/main/scala/dibl/LinkProps.scala#L36-L44
[node]: https://github.com/d-bl/GroundForge/blob/918ab7aa3601e709475aa4b80baa388f2bd1161e/src/main/scala/dibl/NodeProps.scala#L27-L32
[TesselaceThumbs]: https://github.com/d-bl/GroundForge/blob/918ab7aa3601e709475aa4b80baa388f2bd1161e/src/test/scala/dibl/TesselaceThumbs.scala#L66
[composition]: ../images/matrix-template.png
[TesseLace]: http://TesseLace.com
