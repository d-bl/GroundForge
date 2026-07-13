---
layout: default
title: API
---

* [Current User Interfaces](#current-user-interfaces)
* [Work in progress](#widgets--work-in-progress)
* [Demonstrators](#demonstrators)
* [Create your own variant](#create-your-own-variant)
* [Java environment](#java-environment)
* [Notes on the HTML/JS code](#notes-on-the-htmljs-code)
  * [URL query](#url-query)
  * [Inline SVG](#inline-svg)
  * [Event handling](#event-handling)
  * [Animation alias nudging nodes](#animation-alias-nudging-nodes)
    * [pair.html](#pairhtml)
    * [thread.html](#threadhtml)
  * [Download SVG](#download-svg)

GroundForge is a library to generate tread diagrams from pair diagrams for bobbin lace.

Current User Interfaces
=======================

* The former editor (aka the page _tiles_) is split into
  * [pattern editor](/GroundForge/pattern)
  * [stitches](/GroundForge/stitches)
  * [droste](/GroundForge/droste)
* Later pages
  * [nets](/GroundForge/nets)
  * [snow mixer](/GroundForge/mix4snow)
  * [symmetry](/GroundForge/symmetry)

Widgets / Work in progress
================

The next sections describe API's and demonstrators that caused
too much copy-pasting between the current user interfaces.
Some new scripts are started to share code between the user interfaces.

* [Stitch gallery](stitch-gallery) shares code between _nets_, _stitches_ and _droste_.
* [Panels](panels) is started to be resued on several existing pages before developing new pages.
  The stitches gallery is not yet flexible enough to be used along with the panels.
* [Hybrid](../drosteMixer.md) was started to test the _panel.js_ code.
  It is evolving into a page that combines functionality of
  the pages _stitches_, _droste_ and a variant of the _snow mixer_.
  This variant wass inspired when writing 
  [snow with two droste steps](/GroundForge-help/snow-mix/droste),
  it connects the 3/6 pair snowflakes in an [asymmetric](//GroundForge-help/snow-mix/droste/#spiderysnowyasym-connections/) way.
  By hiding or not generating certain components, the script could be used
  to replace the three pages.
* A [tiling gallery](/GroundForge/tileGallery) 
  is emerging, stand alone and for the hybrid page.
  

Demonstrators
=============

A few demonstrators are not covered by the widgets. 
The pages listed below are some "hello world"-s of the basic components
that work together in the user interfaces.
These components can be tweaked, combined or inspire
additional user interfaces for special use cases.

| source                    | examples see&nbsp;also&nbsp;[query](#url-query)                            | notes                                                                                                                                                                                                                                          |
|:--------------------------|----------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [proto.html][protoCode]   | [spiders][protoSpiders], [rose][protoRose]                                 | Pattern definition.                                                                                                                                                                                                                            |
| [sheet.html][sheetCode]   | [sheet.html](sheet.html)                                                   | Pattern families.<br> A simplified hardcoded variant of the [page](/GroundForge/sheet.html) that takes a family of [Tesselace patterns](/tesselace-to-gf/) as parameter, you probably can make more and intermediate [variations][explanation] |

[explanation]: /GroundForge-help/Reshape-Patterns

[protoSpiders]:  proto.html?patchWidth=20&patchHeight=20&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6

[protoRose]:  proto.html?patchWidth=8&patchHeight=14&footside=b,-,a,-&tile=831,4-7,-5-&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2

[protoCode]: {{ site.github.repository_url }}/blob/master/docs/API/proto.html

[sheetCode]: {{ site.github.repository_url }}/blob/master/docs/API/sheet.html

[GFCode]: {{ site.github.repository_url }}/blob/master/src/main/scala/dibl


Create your own variant
=======================

Steps to create your own variations of the demonstrator pages.

* create a new directory with sub-directories `api` and `js`
* download raw versions of the [source(s)](https://github.com/d-bl/GroundForge/tree/master/docs/API) of your choice into the `api` directory
* download raw versions of the referenced [scripts](https://github.com/d-bl/GroundForge/tree/master/docs/js) into the `js` directory
* your local page(s) should now behave like the public versions,
  you can grab other URL queries from the examples on [MAE-gf](/MAE-gf). 

Now you can fiddle around with your own knowledge of HTML, JavaScript, CSS, whatever framework you fancy to add or embed something on your own web pages.

Java environment
================

Note that the functions called from `GroundForge-opt.js` are also available for a JVM environment on the server side.
For that purpose you can look around in the [releases](https://github.com/d-bl/GroundForge/releases)
for a `jar` asset. The downloads above then should be taken
from the source code zip or tar of the same release.

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
see also how to [publish](/GroundForge-help/Stable) your own fork on `github.io`.


Notes on the HTML/JS code
=========================

URL query
---------

The diagram editor and some API pages for pairs and threads need the same set of query parameters.
The query of the diagram editor is assembled by JavaScript via the link button.
The query mimics what would be sent to a server when submitting the form.
Many fields of the form are hidden to the user.

To avoid outdated documentation: look for the usage of `TilesConfig.queryFields`.
* Droste patterns use additional parameters
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
with [4-colors-per-stitch](/GroundForge-help/color-rules).
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

The former `tiles` page used to be a very crowded user interface.
Too many copies of too much data on the page might overwhelm browsers.
Some detours are implemented that set the download content only when needed.
For desktop browsers the href is set at `onHover` events, touch devices don't have such an event.
