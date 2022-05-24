---
layout: default
title: GroundForge - API
--- 

GoundForge is a library partly written in scala. 
The scala source runs on a JVM platform as well as in a JavaScript environment
though data structures are not very compatible.

Additional JavaScript functions create interaction between the web page components.

Current User Interfaces
=======================

* [pattern editor](/GroundForge/tiles)
* [nets](/GroundForge/nets)

Demonstrators
=============

A few simple pages show developers how to use the basic components
that work together in the user interfaces.
They are intended as stepping stones
to create additional user interfaces for special use cases.

* **pair** diagram with a new style color coding:
[spiders](pair.html?patchWidth=20&patchHeight=20&g1=tc&a1=ctctctcttt&l2=crcrcrclll&k2=ctctc&j2=cttcttc&i2=ctttctttc&h2=crcrc&g2=cttc&f2=clclc&e2=ctttctttc&d2=cttcttc&c2=ctctc&b2=clclclcrrr&l3=ctc&k3=ctc&j3=clllc&i3=crrcrrc&h3=clcrc&g3=cttcttc&f3=crclc&e3=cllcllc&d3=crrrc&c3=ctc&b3=ctc&a3=cc&l4=ctc&k4=cllc&j4=crrrcrrrc&i4=cllcrc&h4=cllcrrc&g4=ctttc&f4=crrcllc&e4=crrclc&d4=clllclllc&c4=crrc&b4=ctc&a4=ccc&l5=clc&k5=rctct&j5=clllcrc&i5=clllcrrc&h5=ctc&g5=ctttctttc&f5=ctc&e5=crrrcllc&d5=crrrclc&c5=lctct&b5=crc&a5=ctc&l6=rctct&k6=ctc&j6=ctc&i6=clcrclc&h6=c&f6=c&e6=clclcrc&d6=ctc&c6=ctc&b6=lctct&a6=cc&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6),
[rose](pair.html?patchWidth=8&patchHeight=14&b1=ctc&c1=ctllcrrc&d1=clclc&b2=cllcrrcllcrrcr&d2=ctctctc&c3=ctctll&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)
special [test](pair.html?patchWidth=11&patchHeight=7&j1=clrcccrrc&i1=clrcccc&h1=cccc&g1=cclllcc&f1=lclllcrrrcllcl&e1=lclllcrrcllcl&d1=cllcrrcllc&c1=ctctctctctctctc&b1=ctctctcr&a1=c&j2=crrrc&i2=ctrrc&h2=cttrc&g2=ctttc&f2=llcrrcll&e2=llctrcll&d2=cttc&c2=crc&b2=ctcr&a2=cc&j3=crrrctc&i3=ctrrctc&h3=cttrctc&g3=ctttctc&f3=lllcrrctclll&e3=lllctrctclll&d3=cttcrc&c3=crctc&b3=ctctcrr&a3=ccc&j4=crrrclc&i4=ctrrclc&h4=cttrclc&g4=ctttclc&f4=llllcrrclcllll&e4=llllctrclcllll&d4=cttclc&c4=crclc&b4=ctclcrr&a4=ctctc&j5=crrrcllc&i5=ctrrcllc&h5=cttrcllc&g5=ctttcllc&f5=crrcllc&e5=tttctrcllcttt&d5=cttcllc&c5=crcllc&b5=ctcllcrrr&a5=cttcttc&j6=crrrclllc&i6=ctrrclllc&h6=cttrclllc&g6=ctttclllc&f6=crrclllc&e6=ctrcllllc&d6=cttclllc&c6=crclllc&b6=ctclllcrrrr&a6=ctttctttc&tile=1111111111,8888888888,1111111111,8888888888,1111111111,8888888888&shiftColsSW=0&shiftRowsSW=6&shiftColsSE=10&shiftRowsSE=6)
([source code][threadCode]; 
the page happens to be reasonably convenient to print the diagram and legend together:
position the scrollbars top left and resize the viewport to get (enough of) the diagrams visible)

* **thread** diagram and old style pair diagram:
[spiders](thread.html?patchWidth=20&patchHeight=20&g1=tc&a1=ctctctcttt&l2=crcrcrclll&k2=ctctc&j2=cttcttc&i2=ctttctttc&h2=crcrc&g2=cttc&f2=clclc&e2=ctttctttc&d2=cttcttc&c2=ctctc&b2=clclclcrrr&l3=ctc&k3=ctc&j3=clllc&i3=crrcrrc&h3=clcrc&g3=cttcttc&f3=crclc&e3=cllcllc&d3=crrrc&c3=ctc&b3=ctc&a3=cc&l4=ctc&k4=cllc&j4=crrrcrrrc&i4=cllcrc&h4=cllcrrc&g4=ctttc&f4=crrcllc&e4=crrclc&d4=clllclllc&c4=crrc&b4=ctc&a4=ccc&l5=clc&k5=rctct&j5=clllcrc&i5=clllcrrc&h5=ctc&g5=ctttctttc&f5=ctc&e5=crrrcllc&d5=crrrclc&c5=lctct&b5=crc&a5=ctc&l6=rctct&k6=ctc&j6=ctc&i6=clcrclc&h6=c&f6=c&e6=clclcrc&d6=ctc&c6=ctc&b6=lctct&a6=cc&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6),
[rose](thread.html?patchWidth=8&patchHeight=14&b1=ctc&c1=ctllcrrc&d1=clclc&b2=cllcrrcllcrrcr&d2=ctctctc&c3=ctctll&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)
(comment explains how to tweak the [source code][pairCode]
for droste patterns)

* **prototype** diagram:
[spiders](proto.html?patchWidth=20&patchHeight=20&g1=tc&a1=ctctctcttt&l2=crcrcrclll&k2=ctctc&j2=cttcttc&i2=ctttctttc&h2=crcrc&g2=cttc&f2=clclc&e2=ctttctttc&d2=cttcttc&c2=ctctc&b2=clclclcrrr&l3=ctc&k3=ctc&j3=clllc&i3=crrcrrc&h3=clcrc&g3=cttcttc&f3=crclc&e3=cllcllc&d3=crrrc&c3=ctc&b3=ctc&a3=cc&l4=ctc&k4=cllc&j4=crrrcrrrc&i4=cllcrc&h4=cllcrrc&g4=ctttc&f4=crrcllc&e4=crrclc&d4=clllclllc&c4=crrc&b4=ctc&a4=ccc&l5=clc&k5=rctct&j5=clllcrc&i5=clllcrrc&h5=ctc&g5=ctttctttc&f5=ctc&e5=crrrcllc&d5=crrrclc&c5=lctct&b5=crc&a5=ctc&l6=rctct&k6=ctc&j6=ctc&i6=clcrclc&h6=c&f6=c&e6=clclcrc&d6=ctc&c6=ctc&b6=lctct&a6=cc&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6),
[rose](proto.html?patchWidth=8&patchHeight=14&b1=ctc&c1=ctllcrrc&d1=clclc&b2=cllcrrcllcrrcr&d2=ctctctc&c3=ctctll&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)
([source code][protoCode])

* **pattern families** in a [single SVG](sheet.html) page ([source code][sheetCode],
a simplified hardcoded variant of the
[page](https://jo-pol.github.io/GroundForge/sheet.html?img=214&patch=5-,-5;checker&%20patch=6;checker&%20patch=53;checker&%20patch=563;checker&%20patch=5632;checker&%20patch=56663;checker&%20patch=56353;checker&%20patch=56632;checker&%20patch=53,5-,-5;checker&%20patch=56-,6-5,-56;checker&%20patch=4-L,-L4,L4-;checker&%20patch=53,5-,35,-5;checker&%20patch=53,53,5-,-5;checker&%20patch=566-,66-5,6-56,-566;checker&%20patch=5632,56-2,5-5-,-535;checker)
that takes a family of [Tesselace patterns](GroundForge/tesselace-to-gf/) as parameter, you probably can make more and intermediate [variations][explanation])

[explanation]: /GroundForge-help/Reshape-Patterns

[threadCode]: {{ site.github.repository_url }}/blob/master/docs/API/thread.html

[pairCode]: {{ site.github.repository_url }}/blob/master/docs/API/pair.html

[protoCode]: {{ site.github.repository_url }}/blob/master/docs/API/proto.html

[sheetCode]: {{ site.github.repository_url }}/blob/master/docs/API/sheet.html

[GFCode]: {{ site.github.repository_url }}/blob/master/src/main/scala

Notes on the HTML/JS code
=========================

Inline SVG
----------
The demonstrators assign SVG content to `<div>` elements. Two methods to assign the content:

    d3.select('#someId').html(svg)`
    document.getElementById("someId").innerHTML = svg

The first method requires the library `js/d3.v4.min.js`, which is primarily 
intended to take care of the animation alias nudging of nodes.
The second is plain Javascript but terminates the script with an 
exception if the id does not exist in the DOM of the page.
The SVG content is generated with calls to the library `js/GroundForge-opt.js`.
This library is compiled to Javascript from [scala code][GFCode], look for `@JSExport` annotations.

SVG downloads
-------------
A download link can be created as follows:

    var encoded = encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8"?-->' + svg)
    var el = document.getElementById("someId")
    el.setAttribute('href', 'data:image/svg+xml,' + encoded)
    el.setAttribute('download', 'some-file-name.svg')

The `pattern editor` is a very crowded user interface.
Too many copies of too much data on the page might overwhelm browsers.
Some detours are implemented that set the download content only when needed.
For desktop browsers the href is set at `onHover` events, touch devices don't have such an event.

Animation alias nudging nodes
=============================
The script `js/nudgePairs` works only for pair diagrams generated with `PairSvg` deploying the new style of color coding.
It relies on the fact that the identifiers of the link elements concatenate the identifiers
of the source/target nodes separated with a dash. Note that these identifiers are unique
The identifiers shown in pop-ups (titles) of the diagrams are only unique within the bold
symbols of the prototype diagram. 

`showGraph` in `thread.html` does both the rendering and the nudging 
of old style pair diagrams as well as thread diagrams. 
Scala data structures are paired up with SVG elements to compute the forces.
A scala method uses this data to compute links that may have a shortened start or end.