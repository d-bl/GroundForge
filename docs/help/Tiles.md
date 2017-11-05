---
layout: default
title: Tiles
javascript: /GroundForge/js/tiles.js
---

Tiles
=====

The stitch form matches the input field in the pattern form:
one stitch field for each non-dash in the pattern definition.
The digits and letters of a pattern tell where the pairs com from.
With letters at least one of the pairs travels across a dash.
An odd number of matrix rows is not possible, see issue
[#96](https://github.com/d-bl/GroundForge/issues/96).

{% include tiles-form.html %}
