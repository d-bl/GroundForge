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
An example is a dynamic link to the main page created with this
[html form](https://github.com/d-bl/GroundForge/blob/master/docs/_includes/stitch-form.html)
embedded in the help page to [choose stitches](/GroundForge/help/Choose-Stitches).
A [JavaScript](https://github.com/d-bl/GroundForge/blob/master/docs/js/stitches.js)
creates the dynamic link of the `go` button and dis/en-ables stitch fields for the selected pattern variant.
The script requires the following CSS:

```css
.hide { display: none; }
.show { display: block; }
```

Some [optional CCS](https://github.com/d-bl/GroundForge/blob/master/docs/assets/css/style.scss)
on `#stitch-form` elements (ignore the rest) that suites the
[architect theme](https://github.com/pages-themes/architect#readme).
on github pages.


Diagrams
--------

The [API](/GroundForge/API) page ([source code](https://github.com/d-bl/GroundForge/tree/master/docs/API))
is a dressed down page with all possible diagrams
but no handling for user input to define patterns,
choose stitches, toggle thread colors or whatever.

Look in `tiles.html` and `tiles.js`
for further details on user interaction.