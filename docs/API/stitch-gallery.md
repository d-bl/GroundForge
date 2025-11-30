---
layout: default
title: API demo
---

Demo - Stitch Gallery
=====================

<div id="gallery" style="height: 95px; overflow: auto; resize: both;"></div>  
<script src="/GroundForge/js/GroundForge-opt.js" type="text/javascript"></script>  
<script src="/GroundForge/js/stitch-gallery.js" type="text/javascript"></script>  
<script type="text/javascript">  
GF_stitches.load();  
GF_stitches.lastValidStitchValue='crcl';  
</script>

Usage in github.io markdown
============================

See [source]({{site.github.repository_url}}/blob/master/docs/{{page.path}}).

Widget description
==================

This widget is used on [stitches](/GroundForge/stitches), [nets](/GroundForge/nets) and [droste](/GroundForge/droste) pages.
The [symmetry](/GroundForge/symmetry) page uses a simple _textarea_ in a more complex context.

Get a copy of the [scripts](/GroundForge/blob/master/docs/js/)
and [images](/GroundForge/tree/master/images/stitches).
Adjust the path to the scripts in the usage example as needed.

Usage example:

    <head>
        ...
        <script src="GroundForge-opt.js" type="text/javascript"></script>
        <script src="stitch-gallery.js" type="text/javascript"></script>
        ...
    </head>
    <body onload="GF_stitches.load();GF_stitches.lastValidStitchValue='crcl'; ...">
        ...
        <div id="gallery"></div>
        ...
    </body>

HTML genarated after `<div id="gallery"></div>`:

    <p>
      <span id="colorCode">...</span>
      <textarea id="stitchDef" ...>...</textarea>
    </p>

* `#gallery`: will get `<figure>` elements generated from the configurable variable `GF_stitches.stitches`
* `#colorCode`: contains a visual representation (SVG) of the `#stitchDef` value
* `#stitchDef`: the stitch value, other controls on the page can read the value

Customization variables:

* `GF_stitches.lastValidStitchValue`: controls invalid input in the generated `<textarea>`, default: `ct`
* `GF_stitches.stitches`: an array of stitch values to show in the gallery.
  You need `png` images in the `imageLocation` directory with names identical to the configured stitch values.
* `GF_stitches.imagesLocation `: the location of the stitch images (the default: `/GroundForge/images/stitches` works in the d-bl environment or your own forks)

[Styling](https://github.com/d-bl/GroundForge/blob/6b3716a66acf36bc68cc56fd14880e22a96d9861/docs/css/stitches.css#L17-L22)
suggestions.