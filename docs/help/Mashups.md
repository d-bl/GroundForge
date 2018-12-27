---
layout: default
title: Mash-ups
---
Create mash-ups
===============

* [Convenience Forms](#convenience-forms)
* [Diagrams with a square grid](#diagrams-with-a-square-grid)
    + [Fixed layout on a landscape A4](#fixed-layout-on-a-landscape-a4)
    + [Dynamic layout](#dynamic-layout)
* [Pair and thread diagrams](#pair-and-thread-diagrams)

This page gives some hints to mix and match components of the web pages
to customize them to your own taste and needs.
A dressed down [page](../API/index.html) reduces the required html and JavaScript to a minimum. 

The readme in the project root describes how to use the library in a JVM environment.


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
on `#stitch-form` elements (ignore the rest) that suites the [architect theme](https://github.com/pages-themes/architect#readme).


Diagrams with a square grid
---------------------------

Links as in the TesseLace index ([example](/GroundForge/sheet.html?img=376&patch=B-C-%20---5%20C-B-%20-5--;checker&patch=5831%20-4-7;checker&patch=68%20-4;checker&patch=-4-7%205---%20-C-B%203158;bricks&patch=5-O-E-%20-E-5-O%205-O-E-;bricks))
wrap diagrams in page with additional information.
Download `groundforge-opt.js` from a [release](https://github.com/d-bl/GroundForge/releases)
to create pages with personalised annotations and decoration.
The following paragraphs show some examples.


### Fixed layout on a landscape A4

The first argument of `dibl.SheetSVG` specifies the number of diagrams per column,
2 for landscape A4/letter, 3 for portrait A4/letter.  

```html
<html>
<head>
    <script src="groundforge-opt.js" type="text/javascript"></script>
</head>
<body>
    <div id="diagrams"></div>
    <script>
        var sheet = new dibl.SheetSVG(2, "height='210mm' width='297mm'")
        sheet.add("B-C-,---5,C-B-,-5--","checker")
        sheet.add("5831,-4-7","checker")
        sheet.add("68,-4","checker")
        sheet.add("5-O-E-,-E-5-O,5-O-E-","checker")
        sheet.add("-4-7,5---,-C-B,3158","checker")
        document.getElementById("diagrams").innerHTML += sheet.toSvgDoc().trim()
    </script>
</body>
</html>
```


### Dynamic layout

The dynamic layout will fit as many diagrams on a row as allowed by your monitor or the printing preferences (scale, A4/Letter, landscape/portrait)

```html
<html>
<head>
    <script src="groundforge-opt.js" type="text/javascript"></script>
    <style>svg {display : inline-block;}</style>
    <script>
        var nr = 0
        function add(matrix, tiling) {
            var singleSheet = new dibl.SheetSVG(2, "height='250px' width='250px'", "PATTERN" + nr++)
            singleSheet.add(matrix, tiling)
            document.getElementsByTagName("BODY")[0].innerHTML += (singleSheet.toSvgDoc().trim())
        }
        function load() {
            add("B-C-,---5,C-B-,-5--","checker")
            add("5831,-4-7","checker")
            add("68,-4","checker")
            add("5-O-E-,-E-5-O,5-O-E-","checker")
            add("-4-7,5---,-C-B,3158","checker")
        }
    </script>
</head>
<body onload="load()">
</body>
</html>
```


Pair and thread diagrams
------------------------

_Deprecated demo_:  
~~The javascript for pair and thread diagrams on the home page is more complex,
even without all the bells and whistles like panning, zooming dragging and downloads.
Because of the complexity a link to a [demo](../API/)
(which also includes three JavaScript lines for a diagram on a square grid) 
and a link to actual the [source code](https://github.com/d-bl/GroundForge/tree/master/docs/API)
of the demo.
So far the diagrams as on the home page with generated foot sides but only checker and brick tiling.~~

The tiles page has no bells and whistles. Thus the
[javascript](https://github.com/d-bl/GroundForge/blob/master/docs/js/tiles.js)
functions `showDiagrams` and `animateDiagram` are the demo.

In the function `showDiagrams` we find something like

    dibl.NewPairDiagram().create(dibl.Config().create(queryString))

And example of the `queryString` for a pattern (a braid):

    patchWidth=3&patchHeight=5&a1=ctctt&b1=ctctt&c1=ctctt&tile=158&shiftColsSW=0&shiftRowsSW=0&shiftColsSE=0&shiftRowsSE=1

In the deprecated demo that would be the call:

    dibl.D3Data().get("158", 5, 4, 0, 0, "a1=ctctt b1=ctctt c1=ctctt", "checker")
    
Somehow we need one column more.
Possibly because of the attempt to generate a foot side
which is already embedded in the pattern definition.
