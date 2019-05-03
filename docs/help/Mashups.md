---
layout: default
title: Mash-ups
---
Create mash-ups
===============

The readme in the project root describes how to use the library in a JVM environment.
On this page a few pointers for a browser environment.

Convenience Forms
-----------------

You can create convenience forms for specific families of patterns.
An example is a dynamic link created with this
[html form](https://github.com/d-bl/GroundForge/blob/master/docs/_includes/val-variants.html)
embedded in the Whiting-index page to assemble a [Valencienes ground](/GroundForge/help/Whiting-Index#val).
A JavaScript creates the dynamic link of the `go` button for the selected pattern variant.


Diagrams
--------

The [API](/GroundForge/API) page ([source code](https://github.com/d-bl/GroundForge/tree/master/docs/API))
is a dressed down page with all possible diagrams
but no handling for user input to define patterns,
choose stitches, toggle thread colors or whatever.

Look in `tiles.html` and `tiles.js`
for further details on user interaction.
