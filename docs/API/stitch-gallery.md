---
layout: default
title: API demo
---

Stitch Gallery
==============

* [Widget demo](#widget-demo)
* [Usage in github.io markdown](#usage-in-githubio-markdown)
  * [Styles](#styles)
* [Widget description](#widget-description)
* [Customization variables](#customization-variables)

Widget demo
-----------

<style>
  #gallery {max-height: 120px; overflow: auto; resize: both;}
  p:has(#stitchDef) a.button {color: #2879d0;}
</style>
<script src="/GroundForge/js/d3.v4.min.js" type="text/javascript"></script>
<script src="/GroundForge/js/GroundForge-opt.js" type="text/javascript"></script>
<script src="/GroundForge/js/stitch-gallery.js" type="text/javascript"></script>
<div id="gallery"></div>
<script type="text/javascript"> GF_stitches.load(); </script>

Usage in github.io markdown
---------------------------

See [source]({{site.github.repository_url}}/blame/master/docs/{{page.path}}#L18-L26).
This example assumes you have a fork of this repository and its docs folder configured as GitHub pages.

### Styles

Please note that it is better practice to move the styles into your own CSS files.

You might want to restrict the max-height to narrow devices like mobile phones,
or hide the text input and flip links/buttons:

    p:has(#stitchDef) { display: none; }

Some other [suggestions](/GroundForge/blob/6b3716a66acf36bc68cc56fd14880e22a96d9861/docs/css/stitches.css#L17-L22).


Widget description
------------------

This widget is used on [stitches](/GroundForge/stitches), [nets](/GroundForge/nets) and [droste](/GroundForge/droste) pages.
The [symmetry](/GroundForge/symmetry) page uses a plain text input in a more complex context.

You may need your own copy of the [scripts](/GroundForge/blob/master/docs/js/)
and [images](/GroundForge/tree/master/images/stitches).
Adjust the path to the scripts in the usage example as needed.

Usage example:

    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        ...
        <script src="d3.v4.min.js" type="text/javascript"></script>
        <script src="GroundForge-opt.js" type="text/javascript"></script>
        <script src="stitch-gallery.js" type="text/javascript"></script>
        ...
    </head>
    <body onload="GF_stitches.load();GF_stitches.lastValidStitchValue='crcl'; ...">
        ...
        <div id="gallery"></div>
        ...
    </body>

Some HTML is generated after `<div id="gallery"></div>`:

    <p>
      <span id="colorCode">...</span>
      <input id="stitchDef" type="text" ...>
      ...
    </p>

* `#gallery`: will get `<figure>` elements generated from the configurable variable `GF_stitches.stitches`
* `#colorCode`: contains a visual representation (SVG) of the `#stitchDef` value
* `#stitchDef`: the stitch value, other controls on the page can read the value

Customization variables
-----------------------

* `GF_stitches.lastValidStitchValue`: controls invalid input in the generated text input field, default: `ct`,
  this value is also used to initialize the text input field of the widget.
* `GF_stitches.stitches`: an array of stitch values to show in the gallery.
  You need `png` images in the `imageLocation` directory with names identical to the configured stitch values.
* `GF_stitches.imagesLocation `: the location of the stitch images (the default: `/GroundForge/images/stitches` works in the d-bl environment or your own forks)
