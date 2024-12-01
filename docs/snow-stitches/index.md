---
layout: default
title: Snowflakes
---

A recipe for snowflake fun based on a [template].

![sample](sample.png?align=left)
Babette posted her invention of a double ring pair around snowflakes.
It is a variation of ground B4.1 in [Viele Gute Gründe] by Ulrike Volker-Löhr,
Curiosity by the GroundForge team led to a recipe for all kinds of variations.

![](capture-extract.svg?align=right)
The recipe boils down to replace three-pair spiders in a template and use the resulting thread diagram as pair diagram.
[MAE-gf](/MAE-gf/docs/snow-stitches/#examples) has some examples for replacements and a recipe for more.
On a fresh page, the _to-diagrams_ button shows a reconstruction of the double ring pair.
The star in the center of the hexagonal ring is a snowflake 
which can become one of B3.47-B3.51 and more.

The [template] requires to replace sixteen stitches:
one for each corner of the squares shown on the right.
The form below reduces that task to four actions.
The hexagons below match the squares above.
Note that in both cases one element is flipped.

<script>{% include snow/hexa.js %}</script>
{% include snow/hexa.html %}

A click on a hexagon should replace all stitches of one spider in the template with the stitches in the text field.
When going to the diagrams page, you should see the result.
When satisfied, follow the links to the _pairs from threads_ page to assign stitches for the actual snowflakes.
Even without that last step it can be fun to play with configurations of snowflakes.

Challenge for the form developer: support a variety of replacement stitch counts and starting sides.
For that purpose it should be possible to combine any of the following elements into a valid template
and keep track of count changes and directions.

![](plaits.svg)

[Viele Gute Gründe]: https://www.librarything.com/work/2331526/book/11899122
[example]: https://d-bl.github.io/GroundForge/stitches?patchWidth=11&patchHeight=10&footside=b,-,b,-&tile=3217,1783,3248,1731,&headside=7,8,-,c&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=2&m1=llctt&e1=ctc&d1=rc&c1=tc&b1=lcrclc&a1=rrctt&m2=llctt&e2=ctc&d2=cr&c2=crclcr&b2=ct&e3=lc&d3=ctc&c3=cr&b3=ctc&a3=rrctt&m4=llctt&e4=cl&d4=ctc&c4=ctc&b4=lc&droste2=
[template]: https://d-bl.github.io/GroundForge/stitches.html?patchWidth=11&patchHeight=10&footside=b,-,b,-&tile=3217,1783,3248,1731,&headside=7,8,-,c&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=2&m1=llctt&e1=ctc&d1=ctc&c1=ctc&b1=ctc&a1=rrctt&m2=llctt&e2=ctc&d2=ctc&c2=ctc&b2=ctc&e3=ctc&d3=ctc&c3=ctc&b3=ctc&a3=rrctt&m4=llctt&e4=ctc&d4=ctc&c4=ctc&b4=ctc

