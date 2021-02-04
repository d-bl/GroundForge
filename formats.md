Current formats
===============

* **Tiles page**: the storage format is a set of URL query parameters, for example a
  [rose](https://d-bl.github.io/GroundForge/tiles?patchWidth=9&patchHeight=10&c1=ct&b1=ctct&a1=ct&c2=ct&a2=ct&b3=ctct&tile=831,4-7,-5-&tileStitch=ct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)
  ground. 
* **Sheet page** (with [dance](https://d-bl.github.io/GroundForge-help/Reshape-Patterns)
  leaders): More compact query parameters, no stitches only simple and horizontal brick tile layout.
  Again a [rose](https://d-bl.github.io/GroundForge/sheet.html?patch=5831%20-4-7;bricks&patch=-437%2034-7;bricks&patch=4830%20--77;bricks)
  ground as example.
* **Inkscape plugin template**:  
  Documented at the bottom of https://tesselace.com/tools/inkscape-extension/  
  rose: https://d-bl.github.io/tesselace-to-gf/tl/3_4_8/rose.txt  
  Three coordinates per stitch and dimensions of the simple tile layout. 
* **Experimental draft** with a proof of concept on the pricking page:
  Connections between stiches are defined by the id-s of the stitches and some additional info. 
  Aa an example an irregularly shaped and (for now more or less randomly) rotated [rose](https://jo-pol.github.io/GroundForge/pricking?topo=lo,b4,ri,a1,1;lo,d4,li,a1,1;lo,b3,ri,a3,2;lo,d3,li,a3,2;lo,a1,li,b1,4;ro,b4,ri,b1,2;lo,b1,li,b2,2;lo,c1,ri,b2,0.5;lo,b2,li,b3,2;lo,c3,ri,b3,2;lo,a3,li,b4,1;ro,b3,ri,b4,2;ro,b1,li,c1,2;lo,d1,ri,c1,2;ro,b2,li,c3,0.5;lo,d2,ri,c3,0.5;ro,a1,ri,d1,4;ro,d4,li,d1,2;ro,c1,li,d2,0.5;ro,d1,ri,d2,2;ro,c3,li,d3,2;ro,d2,ri,d3,2;ro,a3,ri,d4,1;ro,d3,li,d4,2)
  ground.  
  Similarities between id-s and a chessboard or spreadsheet is a coincidence.  
  The page supports also the same query parameters as the tiles page.
  
Implemented conversions
-----------------------

* **tiles -> inkscape template**:  
  Download button on the caption of the "patern definition" panel. 
  It does not yet support overlapping tile layouts.
  The templates have more freedom than the tile page, 
  so the other way around is not possible.
* **sheet -> tiles**:  
  Built in links. The other way around would be possible but is not yet implemented.

Thoughts on a new format
========================

A rough sketch
--------------
Let's start with an example that stitches current practices together 

![](src/test/resources/storage-format.svg)

    right-source;left-source;stitch
    SE[1,+1,+0] ;SW[1,-1,+0];N [1,0],ctct ,#FF0000
    SE[1,+0,+1] ;N [1,-1,+1];W [0,1],ct   ,#00FF00
    N [1,+1,+1] ;SW[1,+0,+1];E [2,1],ct   ,#00FF00
    W [1,+1,+1] ;E [1,-1,+1];S [1,2],ctct ,#FF0000
    S [1,-1,+0] ;E [1,+0,+1];SE[2,2],ct   ,#00FF00
    W [1,+0,+1] ;S [1,-1,+0];SW[0,2],ct   ,#00FF00
    
    Legend per column
    ...-source: id[strength, dx, dy]
    stitch:     id[x,y], instructions (ctlrp), color

Any similarity between id-s and a wind rose is a coincidence.

* Header line mandatory. Order of columns may vary.
* Strengths, deltas and coordinates are optional.
* Decimals are allowed for deltas and coordinates.
* Coordinates or deltas allow a quick re-render.
* Strengths (for a spring layout) may help to limit modifications for starch/wire-free patterns. Default 1.

###Conversions
The Inkscape plugin template format does not use id-s,
but coordinates for all three columns and a simple tile size.

Map cheat sheet symbols to deltas: `dibl.Matrix.toRelativeSources`
Expand to simple matrices: `dibl.proto.PairParams.toSimple3x2`
(commit `a192295d`, branch jo-pol/without-distortion,
so far only bricks, how to determine the simple size for an overlapping tile?)

Requirements
------------

* Minimal graph definition: edges around nodes in clockwise order. 
  We could start with the leftmost pair required to make a stitch 
  and end with the leftmost pair when the stitch is done.
  ID-s for the nodes connected by the edges are enough information.
* Algorithms are the bottleneck. Redundancy in storage (dx-dy for edges and/or x-y for nodes) may speed up re-rendering.
* From the new proof of concept we can identify three types of objects to render and click on:  
  Faces, edges and nodes.  
  Information for edges and location of nodes (the stitches) is by definition includes in the faces (the holes between the pairs/threads)
  For nodes we only 
* Users want a legend for stitches. A cross-reference with stitch instructions
  on one side and representations on the other side could serve as a style sheet.
  The new proof of concept provides a free choice of colors
  and so far an enumeration of shapes.
  Ultimately that might become a string-template with parameters like x,y,id.
  Not sure whether to store these cross-references together with the pattern and/or separately.
  Perhaps a user may want to override across-reference that comes with a pattern.
* For thread diagrams one c(ross) or t(wist) for one stitch should be sufficient when
  no threads as pairs are desired. 
  The other nodes can be calculated recursively though that is a matter of performance versus storage. 

Storage
-------
When avoiding file upload and sticking to pattern links, storage may be a bottleneck:  
a safe [maximum URL length](https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers?rq=1)
is 2K.
So we might be better off with a base64 encoding of binary data.
Without redundancy for faces we could do with deltas of two edges per node.
That makes one short integer for an id, 4 floats for deltas, and a variable length string for the instructions.
When encoding a `t` as `lr` we need more characters per stitch, but each character fits in 2 bits.
That makes at least `16+4*32+16=5*32=160` bits, 24 bits require 4 base64 characters,
that makes about 27 characters per stitch, for turning stitches and plaits 1 or 2 characters more.
With a 2K limit and some 20 characters up to the query, that means at most 65 stitches per pattern.
A droste pattern may quickly exceed that limit, for example https://www.instagram.com/p/CKT2gCxpBNd/
So we should not try.

Notes
-----
A possible library for json: https://github.com/circe/circe#readme

For color names, See https://www.w3schools.com/colors/colors_names.asp  
Need research whether Inkscape and other editors support the same set of names
for the same hex-values. 
The default value depends on the instructions which in turn defaults to ctc.

Comment/metadata? Such as a link to the tiles page, copyright (default cc-0?), author.

Currently, pins are connected with only two nodes of its surrounding face. 
Connecting them with all nodes and applying special forces to their connections might cause better behaviour.
An idea against the distortion: connect the outer nodes with a (sticky?) frame.

Implementation with D3js
========================
currently used version/API | for later versions and release notes see | notes
-----|-------|----
https://github.com/d3/d3/blob/v4.4.0/API.md | https://github.com/d3/d3/releases 
https://github.com/d3/d3-force/tree/v1.0.4 | https://github.com/d3/d3-force/releases | v2.0.0 doesn't support IE any more <br> [our configuration](https://github.com/d-bl/GroundForge/blob/3ecc7b2bc74432e522f3a503f867f4aa5fcba7b0/docs/js/tiles.js#L149-L159)

Interesting examples (possibly still v3)
* [sticky nodes](https://bl.ocks.org/mbostock/3750558), see also our latest use of [sticky-pins](https://github.com/d-bl/GroundForge/releases/tag/last-with-sticky-pins)
* [collapsible](https://bl.ocks.org/mbostock/1093130) possibly an "_expand all_" to replace the current conversion of pair to thread diagrams
* [editor](http://bl.ocks.org/rkirsling/5001347)
* 3D structures [Hamilton Graph builder](http://bl.ocks.org/christophermanning/raw/1703449/#/[25,50,75,100]100/0/0)
