---
layout: default
title: GroundForge - API
---

- [Current User Interfaces](#current-user-interfaces)
- [Demonstrators](#demonstrators)
- [IDE and build](#ide-and-build)
- [Create your own variant](#create-your-own-variant)
- [Notes on the HTML/JS code](#notes-on-the-html-js-code)
  * [URL query](#url-query)
  * [Inline SVG](#inline-svg)
  * [Event handling](event-handling)
  * [Animation alias nudging nodes](#animation-alias-nudging-nodes)
    - [pair.html](#pairhtml)
    - [thread.html](#threadhtml)
  * [Download SVG](#download-svg)

GroundForge is a library to generate tread diagrams from pair diagrams for bobbin lace.
It is partly written in ScalaJS. 
The ScalaJS code runs on a JVM platform as well as in a JavaScript environment.
Additional JavaScript functions create interaction between the web page components.

Current User Interfaces
=======================

* [pattern editor](/GroundForge/tiles)
* [nets](/GroundForge/nets)

Demonstrators
=============

A few pages are the "hello world"-s of the basic components
that work together in the user interfaces.
These components can be tweaked, combined or inspire
additional user interfaces for special use cases.

[pairTest]: pair.html?patchWidth=11&patchHeight=7&j1=clrcccrrc&i1=clrcccc&h1=cccc&g1=cclllcc&f1=lclllcrrrcllcl&e1=lclllcrrcllcl&d1=cllcrrcllc&c1=ctctctctctctctc&b1=ctctctcr&a1=c&j2=crrrc&i2=ctrrc&h2=cttrc&g2=ctttc&f2=llcrrcll&e2=llctrcll&d2=cttc&c2=crc&b2=ctcr&a2=cc&j3=crrrctc&i3=ctrrctc&h3=cttrctc&g3=ctttctc&f3=lllcrrctclll&e3=lllctrctclll&d3=cttcrc&c3=crctc&b3=ctctcrr&a3=ccc&j4=crrrclc&i4=ctrrclc&h4=cttrclc&g4=ctttclc&f4=llllcrrclcllll&e4=llllctrclcllll&d4=cttclc&c4=crclc&b4=ctclcrr&a4=ctctc&j5=crrrcllc&i5=ctrrcllc&h5=cttrcllc&g5=ctttcllc&f5=crrcllc&e5=tttctrcllcttt&d5=cttcllc&c5=crcllc&b5=ctcllcrrr&a5=cttcttc&j6=crrrclllc&i6=ctrrclllc&h6=cttrclllc&g6=ctttclllc&f6=crrclllc&e6=ctrcllllc&d6=cttclllc&c6=crclllc&b6=ctclllcrrrr&a6=ctttctttc&tile=1111111111,8888888888,1111111111,8888888888,1111111111,8888888888&shiftColsSW=0&shiftRowsSW=6&shiftColsSE=10&shiftRowsSE=6
[protoSpiders]:  proto.html?patchWidth=20&patchHeight=20&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6
[pairSpiders]:   pair.html?patchWidth=20&patchHeight=20&g1=tc&a1=ctctctcttt&l2=crcrcrclll&k2=ctctc&j2=cttcttc&i2=ctttctttc&h2=crcrc&g2=cttc&f2=clclc&e2=ctttctttc&d2=cttcttc&c2=ctctc&b2=clclclcrrr&l3=ctc&k3=ctc&j3=clllc&i3=crrcrrc&h3=clcrc&g3=cttcttc&f3=crclc&e3=cllcllc&d3=crrrc&c3=ctc&b3=ctc&a3=cc&l4=ctc&k4=cllc&j4=crrrcrrrc&i4=cllcrc&h4=cllcrrc&g4=ctttc&f4=crrcllc&e4=crrclc&d4=clllclllc&c4=crrc&b4=ctc&a4=ccc&l5=clc&k5=rctct&j5=clllcrc&i5=clllcrrc&h5=ctc&g5=ctttctttc&f5=ctc&e5=crrrcllc&d5=crrrclc&c5=lctct&b5=crc&a5=ctc&l6=rctct&k6=ctc&j6=ctc&i6=clcrclc&h6=c&f6=c&e6=clclcrc&d6=ctc&c6=ctc&b6=lctct&a6=cc&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6
[threadSpiders]: thread.html?patchWidth=20&patchHeight=20&g1=tc&a1=ctctctcttt&l2=crcrcrclll&k2=ctctc&j2=cttcttc&i2=ctttctttc&h2=crcrc&g2=cttc&f2=clclc&e2=ctttctttc&d2=cttcttc&c2=ctctc&b2=clclclcrrr&l3=ctc&k3=ctc&j3=clllc&i3=crrcrrc&h3=clcrc&g3=cttcttc&f3=crclc&e3=cllcllc&d3=crrrc&c3=ctc&b3=ctc&a3=cc&l4=ctc&k4=cllc&j4=crrrcrrrc&i4=cllcrc&h4=cllcrrc&g4=ctttc&f4=crrcllc&e4=crrclc&d4=clllclllc&c4=crrc&b4=ctc&a4=ccc&l5=clc&k5=rctct&j5=clllcrc&i5=clllcrrc&h5=ctc&g5=ctttctttc&f5=ctc&e5=crrrcllc&d5=crrrclc&c5=lctct&b5=crc&a5=ctc&l6=rctct&k6=ctc&j6=ctc&i6=clcrclc&h6=c&f6=c&e6=clclcrc&d6=ctc&c6=ctc&b6=lctct&a6=cc&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6
[protoRose]:  proto.html?patchWidth=8&patchHeight=14&footside=b,-,a,-&tile=831,4-7,-5-&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2
[pairRose]:   pair.html?patchWidth=8&patchHeight=14&b1=ctc&c1=ctllcrrc&d1=clclc&b2=cllcrrcllcrrcr&d2=ctctctc&c3=ctctll&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2
[threadRose]: thread.html?patchWidth=8&patchHeight=14&b1=ctc&c1=ctllcrrc&d1=clclc&b2=cllcrrcllcrrcr&d2=ctctctc&c3=ctctll&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2

| source | examples see&nbsp;also&nbsp;[query](#url-query) | notes |
|:---|---|:---|
| [proto.html][protoCode]  | [spiders][protoSpiders],&nbsp;[rose][protoRose],&nbsp;[test][pairRose] | Pattern definition. |
| [pair.html][pairCode]  | [spiders][pairSpiders], [rose][pairRose]  | 4 colors per stitch.<br> The page happens to be reasonably convenient to print the diagram and enumerated stitches together: position the scrollbars top left and resize the viewport to get (enough of) the diagrams visible. |
| [thread.html][threadCode]  | [spiders][threadSpiders], [rose][threadRose]  | 1 color per stitch pair diagram and thread diagram.<br> Comment in the source explains how to tweak the code for droste patterns. |
| [sheet.html][sheetCode]  | [sheet.html](sheet.html) | Pattern families.<br> A simplified hardcoded variant of the [page](https://jo-pol.github.io/GroundForge/sheet.html) that takes a family of [Tesselace patterns](GroundForge/tesselace-to-gf/) as parameter, you probably can make more and intermediate [variations][explanation]


[explanation]: /GroundForge-help/Reshape-Patterns

[threadCode]: {{ site.github.repository_url }}/blob/master/docs/API/thread.html

[pairCode]: {{ site.github.repository_url }}/blob/master/docs/API/pair.html

[protoCode]: {{ site.github.repository_url }}/blob/master/docs/API/proto.html

[sheetCode]: {{ site.github.repository_url }}/blob/master/docs/API/sheet.html

[GFCode]: {{ site.github.repository_url }}/blob/master/src/main/scala/dibl


Create your own variant
=======================

Steps to create your own variations of the demonstrator pages.

* create a new directory with sub-directories `api` and `js`
* download raw versions of the [source(s)](https://github.com/jo-pol/GroundForge/tree/master/docs/API) of your choice into the `api` directory
* download raw versions of the referenced [scripts](https://github.com/jo-pol/GroundForge/tree/master/docs/js) into the `js` directory
* your local page(s) should now behave like the public versions,
  you can grab other URL queries from the [pattern editor](/GroundForge/tiles). 

Now you can fiddle around with your own knowledge of HTML, JavaScript, CSS, whatever framework you fancy to add or embed something on your own web pages.

Note that the functions called from `GroundForge-opt.js` are also available for a JVM environment on the server side.
For that purpose you can look around in the [releases](https://github.com/d-bl/GroundForge/releases)
for a `jar` asset. The downloads above then should be taken
from the source code zip or tar of the same release.


IDE and build
=============

An alternative to wait for an irregular release:
create a local clone from the github project and 
run `mvn clean install -DskipTests` for an up to date `jar` in the `target` directory
or one of the `toJS` scripts for `GroundForge-opt.js`.

When importing the project into an IDE (for example the community edition of Intellij)
ignore the suggestion to import as an SBT project, do _import as a maven project_.

Note that plain Scala code only runs on a JVM environment
while the GroundForge library is writtein in ScalJS with the purpose
to run also in a JavaScipt environment.
File access and plain Scala libraries are limited to the test classes.
The tests run with Maven or by your IDE.

Exchanging complex data types between the library and Java or JavaScript can be complicated.
So the methods are designed to exchange primitive data as much as possible,
or the host language stores the data to be passed on to other library calls.

Note that Github builds the pattern editor with jekyll, instructions to [test locally](https://docs.github.com/en/pages/setting-up-a-github-pages-site-with-jekyll/testing-your-github-pages-site-locally-with-jekyll),
see also how to [publish](GroundForge-help/Stable) your own fork on `github.io`.


Notes on the HTML/JS code
=========================

URL query
---------

The diagram editor and some API pages for pairs and threads need the same set of query parameters.
The query of the diagram editor is assembled by JavaScript via the link button.
The query mimics what would be sent to a server when submitting the form.
Many fields of the form are hidden to the user.

To avoid outdated documentation: look for the usage of `TilesConfig.queryFields`.
* Droste patterns use additional parameters in the load function of `tiles.js`.
* `API/proto.html` uses only some of the `queryFields`.
* `API/sheet.html` does not use a query at all.

Inline SVG
----------
The demonstrators assign SVG content to `<div>` elements. Let us compare two methods to assign the content:

    d3.select('#someId').html(svg)`
    document.getElementById("someId").innerHTML = svg

The first method requires the library `js/d3.v4.min.js`, which is primarily 
intended to take care of the animation alias nudging of nodes.
The second is plain JavaScript but terminates the script with an 
exception if the id does not exist in the DOM of the page.
The SVG content is generated with calls to the library `js/GroundForge-opt.js`.
This library is compiled to JavaScript from [ScalaJS code][GFCode], look for `@JSExport` annotations.

Event handling
--------------
Except for the _pattern families_, the diagrams have out of the box event handling.

The SVG elements for stitches have an attribute `onclick="clickedStitch(event)"`,
bobbins and the starts of threads have `onclick="clickedThread(event)"`.
On `proto.html` we have `onclick="resetStitch(event)"` and hidden form fields with `onchange="showProto()"`.

Animation alias nudging nodes
-----------------------------

### pair.html

The function `PairSvg.render` generates the SVG content for a pair diagram
with [4-colors-per-stitch](../images/color-rules).
The generated elements with class `link` have identifiers that concatenate 
the identifiers of the source/target elements with class `node`. 
The identifiers of the SVG elements are unique.
The identifiers shown in pop-ups (titles) of the diagrams are only unique within
the bold area of the pattern diagram. An example:

    <path id="r0c4-r1c3" class="link" d="..." style="..."></path>
    <g id="r0c4" class="node" transform="..."><title>ctc - a1</title>ct - a1</g>
    <g id="r1c3" class="node" transform="..."><title>ctc - a1</title>ctct - b2</g>

The script `js/nudgePairs.js` uses the concatenated identifiers
to find and bind the data required by the `d3` library.
The nudge script rewrites the values for the `d` and `transform`
attributes as calculated by the `d3` forces. 

### thread.html

The function`showGraph` in `thread.html` does both the rendering and the nudging of thread
diagrams as well as old style pair diagrams. The latter are stepping stones for the first.
ScalaJS data structures are paired up with generated SVG elements to compute the forces.
A ScalaJS method uses this data to compute the `d` attribute of links with shortened starts or ends.

Download SVG
------------
A download link can be created as follows:

    var encoded = encodeURIComponent('<!--?xml version="1.0" encoding="UTF-8"?-->' + svg)
    var el = document.getElementById("someId")
    el.setAttribute('href', 'data:image/svg+xml,' + encoded)
    el.setAttribute('download', 'some-file-name.svg')

The `pattern editor` is a very crowded user interface.
Too many copies of too much data on the page might overwhelm browsers.
Some detours are implemented that set the download content only when needed.
For desktop browsers the href is set at `onHover` events, touch devices don't have such an event.
