---
layout: default
title: GroundForge - API
--- 

GoundForge is a library partly written in scala. 
The scala source can run on a JVM platform and can be called from java programs,
it can also run in a JavaScript environment. 
Additional functions create web page components.

Current User Interfaces
=======================

* [pattern editor](/GroundForge/tiles)
* [nets](/GroundForge/nets)

Demonstrators
=============

A few simple pages show how to use the basic components
that work together in the user interfaces.

* new style pair diagram:
[spiders](pair.html?patchWidth=20&patchHeight=20&g1=tc&a1=ctctctcttt&l2=crcrcrclll&k2=ctctc&j2=cttcttc&i2=ctttctttc&h2=crcrc&g2=cttc&f2=clclc&e2=ctttctttc&d2=cttcttc&c2=ctctc&b2=clclclcrrr&l3=ctc&k3=ctc&j3=clllc&i3=crrcrrc&h3=clcrc&g3=cttcttc&f3=crclc&e3=cllcllc&d3=crrrc&c3=ctc&b3=ctc&a3=cc&l4=ctc&k4=cllc&j4=crrrcrrrc&i4=cllcrc&h4=cllcrrc&g4=ctttc&f4=crrcllc&e4=crrclc&d4=clllclllc&c4=crrc&b4=ctc&a4=ccc&l5=clc&k5=rctct&j5=clllcrc&i5=clllcrrc&h5=ctc&g5=ctttctttc&f5=ctc&e5=crrrcllc&d5=crrrclc&c5=lctct&b5=crc&a5=ctc&l6=rctct&k6=ctc&j6=ctc&i6=clcrclc&h6=c&f6=c&e6=clclcrc&d6=ctc&c6=ctc&b6=lctct&a6=cc&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6),
[rose](pair.html?patchWidth=8&patchHeight=14&b1=ctc&c1=ctllcrrc&d1=clclc&b2=cllcrrcllcrrcr&d2=ctctctc&c3=ctctll&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)
([code][threadCode])

* thread diagram:
[spiders](thread.html?patchWidth=20&patchHeight=20&g1=tc&a1=ctctctcttt&l2=crcrcrclll&k2=ctctc&j2=cttcttc&i2=ctttctttc&h2=crcrc&g2=cttc&f2=clclc&e2=ctttctttc&d2=cttcttc&c2=ctctc&b2=clclclcrrr&l3=ctc&k3=ctc&j3=clllc&i3=crrcrrc&h3=clcrc&g3=cttcttc&f3=crclc&e3=cllcllc&d3=crrrc&c3=ctc&b3=ctc&a3=cc&l4=ctc&k4=cllc&j4=crrrcrrrc&i4=cllcrc&h4=cllcrrc&g4=ctttc&f4=crrcllc&e4=crrclc&d4=clllclllc&c4=crrc&b4=ctc&a4=ccc&l5=clc&k5=rctct&j5=clllcrc&i5=clllcrrc&h5=ctc&g5=ctttctttc&f5=ctc&e5=crrrcllc&d5=crrrclc&c5=lctct&b5=crc&a5=ctc&l6=rctct&k6=ctc&j6=ctc&i6=clcrclc&h6=c&f6=c&e6=clclcrc&d6=ctc&c6=ctc&b6=lctct&a6=cc&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6),
[rose](thread.html?patchWidth=8&patchHeight=14&b1=ctc&c1=ctllcrrc&d1=clclc&b2=cllcrrcllcrrcr&d2=ctctctc&c3=ctctll&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)
(comment explains how to tweak the [code][pairCode]
for old style pair diagrams or droste patterns)

* prototype diagram:
[spiders](proto.html?patchWidth=20&patchHeight=20&g1=tc&a1=ctctctcttt&l2=crcrcrclll&k2=ctctc&j2=cttcttc&i2=ctttctttc&h2=crcrc&g2=cttc&f2=clclc&e2=ctttctttc&d2=cttcttc&c2=ctctc&b2=clclclcrrr&l3=ctc&k3=ctc&j3=clllc&i3=crrcrrc&h3=clcrc&g3=cttcttc&f3=crclc&e3=cllcllc&d3=crrrc&c3=ctc&b3=ctc&a3=cc&l4=ctc&k4=cllc&j4=crrrcrrrc&i4=cllcrc&h4=cllcrrc&g4=ctttc&f4=crrcllc&e4=crrclc&d4=clllclllc&c4=crrc&b4=ctc&a4=ccc&l5=clc&k5=rctct&j5=clllcrc&i5=clllcrrc&h5=ctc&g5=ctttctttc&f5=ctc&e5=crrrcllc&d5=crrrclc&c5=lctct&b5=crc&a5=ctc&l6=rctct&k6=ctc&j6=ctc&i6=clcrclc&h6=c&f6=c&e6=clclcrc&d6=ctc&c6=ctc&b6=lctct&a6=cc&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6),
[rose](proto.html?patchWidth=8&patchHeight=14&b1=ctc&c1=ctllcrrc&d1=clclc&b2=cllcrrcllcrrcr&d2=ctctctc&c3=ctctll&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2)
([code][protoCode])

* [pricking](sheet.html) variations ([code][sheetCode],
a simplified hardcoded variant of this
[page](https://jo-pol.github.io/GroundForge/sheet.html?img=214&patch=5-,-5;checker&%20patch=6;checker&%20patch=53;checker&%20patch=563;checker&%20patch=5632;checker&%20patch=56663;checker&%20patch=56353;checker&%20patch=56632;checker&%20patch=53,5-,-5;checker&%20patch=56-,6-5,-56;checker&%20patch=4-L,-L4,L4-;checker&%20patch=53,5-,35,-5;checker&%20patch=53,53,5-,-5;checker&%20patch=566-,66-5,6-56,-566;checker&%20patch=5632,56-2,5-5-,-535;checker)
that takes a family of Tesselace patterns as parameter, you probably can make more and intermediate [variations][explanation])

[explanation]: /GroundForge-help/Reshape-Patterns

[threadCode]: {{ site.github.repository_url }}/blob/master/docs/API/thread.html

[pairCode]: {{ site.github.repository_url }}/blob/master/docs/API/pair.html

[protoCode]: {{ site.github.repository_url }}/blob/master/docs/API/proto.html

[sheetCode]: {{ site.github.repository_url }}/blob/master/docs/API/sheet.html