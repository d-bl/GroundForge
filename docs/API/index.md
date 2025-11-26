---
layout: default
title: GroundForge - API
---

* [Current User Interfaces](#current-user-interfaces)
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

* The former pattern editor is split into
  * [pattern editor](/GroundForge/pattern)
  * [stitches](/GroundForge/stitches)
  * [droste](/GroundForge/droste)
* [nets](/GroundForge/nets)
* [snow mixer](/GroundForge/mix4snow)
* [symmetry](/GroundForge/symmetry)

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
[pairMax]: thread.html?patchWidth=26&patchHeight=40&paintStitches=ctcl&ae1=ctctt&ad1=ctctt&ac1=ctctt&aa1=ctc&y1=ctc&w1=ctc&u1=ctc&s1=ctc&q1=ctc&o1=ctc&m1=ctc&k1=ctc&i1=ctc&g1=ctc&e1=ctc&c1=ctc&b1=ctctt&a1=ctctt&ae2=ctctt&ad2=ctctt&z2=ctc&v2=ctc&r2=ctc&n2=ctc&j2=ctc&f2=ctc&b2=ctctt&a2=ctctt&ac3=ctctt&aa3=ctc&y3=ctc&w3=ctc&u3=ctc&s3=ctc&q3=ctc&o3=ctc&m3=ctc&k3=ctc&i3=ctc&g3=ctc&e3=ctc&c3=ctc&ab4=ctc&x4=ctc&t4=ctc&p4=ctc&l4=ctc&h4=ctc&d4=ctc&aa5=ctc&y5=ctc&w5=ctc&u5=ctc&s5=ctc&q5=ctc&o5=ctc&m5=ctc&k5=ctc&i5=ctc&g5=ctc&e5=ctc&c5=ctc&z6=ctc&x6=ctc&v6=ctc&t6=ctc&r6=ctc&n6=ctc&j6=ctc&f6=ctc&aa7=ctc&y7=ctc&x7=ctc&w7=ctc&v7=ctc&u7=ctc&t7=ctc&s7=ctc&q7=ctc&o7=ctc&m7=ctc&k7=ctc&i7=ctc&g7=ctc&e7=ctc&c7=ctc&ab8=ctc&z8=ctc&y8=ctc&x8=ctc&w8=ctc&v8=ctc&u8=ctc&t8=ctc&s8=ctc&r8=ctc&p8=ctc&l8=ctc&h8=ctc&d8=ctc&aa9=ctc&z9=ctc&y9=ctc&x9=ctc&w9=ctc&v9=ctc&u9=ctc&t9=ctc&s9=ctc&q9=ctc&o9=ctc&m9=ctc&k9=ctc&i9=ctc&g9=ctc&e9=ctc&c9=ctc&z10=ctc&y10=ctc&x10=ctc&w10=ctc&v10=ctc&u10=ctc&t10=ctc&s10=ctc&r10=ctc&n10=ctc&j10=ctc&f10=ctc&aa11=ctc&z11=ctc&y11=ctc&x11=ctc&w11=ctc&v11=ctc&u11=ctc&t11=ctc&s11=ctc&r11=ctc&q11=ctc&o11=ctc&m11=ctc&k11=ctc&i11=ctc&g11=ctc&e11=ctc&c11=ctc&ab12=ctc&z12=ctc&y12=ctc&x12=ctc&w12=ctc&v12=ctc&u12=ctc&t12=ctc&s12=ctc&r12=ctc&p12=ctc&l12=ctc&h12=ctc&d12=ctc&aa13=ctc&z13=ctc&y13=ctc&x13=ctc&w13=ctc&v13=ctc&u13=ctc&t13=ctc&s13=ctc&r13=ctc&q13=ctc&o13=ctc&m13=ctc&k13=ctc&i13=ctc&g13=ctc&e13=ctc&c13=ctc&z14=ctc&y14=ctc&x14=ctc&w14=ctc&v14=ctc&u14=ctc&t14=ctc&s14=ctc&r14=ctc&n14=ctc&j14=ctc&f14=ctc&aa15=ctc&y15=ctc&w15=ctc&v15=ctc&u15=ctc&t15=ctc&s15=ctc&q15=ctc&o15=ctc&m15=ctc&k15=ctc&i15=ctc&g15=ctc&e15=ctc&c15=ctc&ab16=ctc&x16=ctc&v16=ctc&t16=ctc&s16=ctc&r16=ctc&p16=ctc&l16=ctc&h16=ctc&d16=ctc&aa17=ctc&y17=ctc&w17=ctc&u17=ctc&t17=ctc&s17=ctc&q17=ctc&o17=ctc&m17=ctc&k17=ctc&i17=ctc&g17=ctc&e17=ctc&c17=ctc&z18=ctc&v18=ctc&t18=ctc&r18=ctc&n18=ctc&j18=ctc&h18=ctc&f18=ctc&aa19=ctc&y19=ctc&w19=ctc&u19=ctc&t19=ctc&s19=ctc&q19=ctc&o19=ctc&m19=ctc&k19=ctc&i19=ctc&h19=ctc&g19=ctc&e19=ctc&c19=ctc&ab20=ctc&x20=ctc&t20=ctc&s20=ctc&r20=ctc&p20=ctc&l20=ctc&j20=ctc&i20=ctc&h20=ctc&d20=ctc&aa21=ctc&y21=ctc&w21=ctc&u21=ctc&s21=ctc&r21=ctc&q21=ctc&o21=ctc&m21=ctc&k21=ctc&j21=ctc&i21=ctc&h21=ctc&g21=ctc&e21=ctc&c21=ctc&z22=ctc&v22=ctc&r22=ctc&q22=ctc&p22=ctc&n22=ctc&l22=ctc&k22=ctc&j22=ctc&i22=ctc&h22=ctc&f22=ctc&aa23=ctc&z23=ctc&y23=ctc&w23=ctc&u23=ctc&s23=ctc&q23=ctc&p23=ctc&o23=ctc&m23=ctc&l23=ctc&k23=ctc&j23=ctc&i23=ctc&h23=ctc&g23=ctc&e23=ctc&c23=ctc&ab24=ctc&z24=ctc&x24=ctc&t24=ctc&p24=ctc&n24=ctc&m24=ctc&l24=ctc&k24=ctc&j24=ctc&i24=ctc&h24=ctc&d24=ctc&aa25=ctc&z25=ctc&y25=ctc&w25=ctc&u25=ctc&s25=ctc&q25=ctc&p25=ctc&o25=ctc&n25=ctc&m25=ctc&l25=ctc&k25=ctc&j25=ctc&i25=ctc&h25=ctc&g25=ctc&e25=ctc&c25=ctc&z26=ctc&y26=ctc&x26=ctc&v26=ctc&r26=ctc&p26=ctc&o26=ctc&n26=ctc&m26=ctc&l26=ctc&k26=ctc&j26=ctc&i26=ctc&h26=ctc&f26=ctc&aa27=ctc&y27=ctc&x27=ctc&w27=ctc&u27=ctc&s27=ctc&q27=ctc&o27=ctc&n27=ctc&m27=ctc&l27=ctc&k27=ctc&j27=ctc&i27=ctc&h27=ctc&g27=ctc&e27=ctc&c27=ctc&ab28=ctc&x28=ctc&w28=ctc&v28=ctc&t28=ctc&p28=ctc&o28=ctc&n28=ctc&m28=ctc&l28=ctc&k28=ctc&j28=ctc&i28=ctc&h28=ctc&d28=ctc&aa29=ctc&y29=ctc&w29=ctc&v29=ctc&u29=ctc&s29=ctc&q29=ctc&o29=ctc&n29=ctc&m29=ctc&l29=ctc&k29=ctc&j29=ctc&i29=ctc&h29=ctc&g29=ctc&e29=ctc&c29=ctc&z30=ctc&v30=ctc&r30=ctc&p30=ctc&o30=ctc&n30=ctc&m30=ctc&l30=ctc&k30=ctc&j30=ctc&i30=ctc&h30=ctc&f30=ctc&aa31=ctc&y31=ctc&w31=ctc&v31=ctc&u31=ctc&s31=ctc&q31=ctc&p31=ctc&o31=ctc&n31=ctc&m31=ctc&l31=ctc&k31=ctc&j31=ctc&i31=ctc&h31=ctc&g31=ctc&e31=ctc&c31=ctc&ab32=ctc&x32=ctc&v32=ctc&u32=ctc&t32=ctc&r32=ctc&q32=ctc&p32=ctc&o32=ctc&n32=ctc&m32=ctc&l32=ctc&k32=ctc&j32=ctc&i32=ctc&h32=ctc&d32=ctc&aa33=ctc&y33=ctc&u33=ctc&q33=ctc&o33=ctc&n33=ctc&m33=ctc&l33=ctc&k33=ctc&j33=ctc&i33=ctc&g33=ctc&e33=ctc&c33=ctc&z34=ctc&x34=ctc&v34=ctc&u34=ctc&t34=ctc&r34=ctc&q34=ctc&p34=ctc&n34=ctc&m34=ctc&l34=ctc&k34=ctc&j34=ctc&f34=ctc&y35=ctc&x35=ctc&w35=ctc&u35=ctc&t35=ctc&s35=ctc&q35=ctc&p35=ctc&o35=ctc&m35=ctc&l35=ctc&k35=ctc&i35=ctc&h35=ctc&g35=ctc&e35=ctc&d35=ctc&c35=ctc&footside=11,r8,xx,xx&tile=4-7-4-7-4-7-4-7-4-7-4-7-4-,x-x5x-x5x-x5x-x5x-x5x-x5x-,7-4-7-4-7-4-7-4-7-4-7-4-7-,x5x-x5x-x5x-x5x-x5x-x5x-x5,4-7-4-7-4-7-4-7-4-7-4-7-4-,x-x5x-x5x-x5x-x5-5-5-5-5x-,7-4-7-4-7-4-7-4-5868686-7-,x5x-x5x-x5x-x5-211111115-5,4-7-4-7-4-7-4-5-788888886-,x-x5x-x5x-x5x-x211111114x-,7-4-7-4-7-4-7-48888888888-,x5x-x5x-x5x-x5-111111114-5,4-7-4-7-4-7-4-58888888886-,x-x5x-x5x-x5x-x111111114x-,7-4-7-4-7-4-7-4-78888-7-7-,x5x-x5x-x5x-x5-214-4-5x-x5,4-7-4-7-4-7-4-5-786-5-7-4-,x-x5-5-5x-x5x-x5-4-5x-x5x-,7-4-586-7-4-7-4-215-7-4-7-,x5x-x115-5x-x5-588x-x5x-x5,4-7-48886-7-4-214-7-4-7-4-,x-x5-11115-5-588x-x5x-x5x-,7-4-5888886-214-7-4-7-117-,x5x-x1111115-7x-x5x-x5-7-5,4-7-48888888688-4-7-4-215-,x-x5-111111114-5x-x5-588x-,7-4-588888888-5-7-4-214-7-,x5x-x111111115x-x5-588x-x5,4-7-488888888-7-4-214-7-4-,x-x5-111111115-5x-x7x-x5x-,7-4-58888888886-7-117-4-7-,x5x-x11111111115-588-5x-x5,4-7-4-7888888-7x-x4x-x7-4-,x-x5x-x11114-217-488-4-5x-,734-734-734-534-534-535---,&headside=788-,x11w,4xx-,xxx-&shiftColsSW=0&shiftRowsSW=35&shiftColsSE=26&shiftRowsSE=35

| source                    | examples see&nbsp;also&nbsp;[query](#url-query)                        | notes                                                                                                                                                                                                                                                                           |
|:--------------------------|------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [proto.html][protoCode]   | [spiders][protoSpiders],&nbsp;[rose][protoRose],&nbsp;[test][pairRose] | Pattern definition.                                                                                                                                                                                                                                                             |
| [pair.html][pairCode]     | [spiders][pairSpiders], [rose][pairRose], [max][pairMax]               | 4 colors per stitch.                                                                                                                                                                                                                                                            |
| [thread.html][threadCode] | [spiders][threadSpiders], [rose][threadRose]                           | 1 color per stitch pair diagram and thread diagram.<br> Comment in the source explains how to tweak the code for droste patterns.                                                                                                                                               |
| [sheet.html][sheetCode]   | [sheet.html](sheet.html)                                               | Pattern families.<br> A simplified hardcoded variant of the [page](/GroundForge/sheet.html) that takes a family of [Tesselace patterns](/tesselace-to-gf/) as parameter, you probably can make more and intermediate [variations][explanation] |


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
* Droste patterns use additional parameters in the load function of `droste.js`.
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
