---
layout: default
title: API demo
---

Panels
======

Under construction.

* [Widget demo's](#widget-demos)
* [Usage in github.io markdown](#usage-in-githubio-markdown)
* [Widget description](#widget-description)
  * [`GF_panel.load(options)`](#gf_panelloadoptions)
  * [`GF_panel.diagramSVG(options)`](#gf_paneldiagramsvgoptions)
  * [`GF_panel.nudge(id)`](#gf_panelnudgeid)

This widget is intended to replace hardcoded content on the pages
[stitches](/GroundForge/stitches), 
[nets](/GroundForge/nets), 
[droste](/GroundForge/droste) and the
[snow mixer](/GroundForge/mix4snow).


Widget demo's
-------------

<style>
        .gf_panel {display: inline-block;}
        .gf_panel > div {width: 100%; overflow: auto; resize:both; border: #ddd solid 1px; }
        .gf_panel > figcaption {width: 100%; box-sizing: border-box; background-color: #ddd; }
        .gf_panel > figcaption img, .gf_panel > figcaption > input {margin-left: 0.5em;}
        .gf_panel > figcaption > pre {padding:0; margin:0; background: #ddd}
</style>

<script src="/GroundForge/js/d3.v4.min.js" type="text/javascript"></script>
<script src="/GroundForge/js/GroundForge-opt.js" type="text/javascript"></script>
<script src="/GroundForge/js/panel.js" type="text/javascript"></script>
<script src="/GroundForge/js/nudgePairs.js" type="text/javascript"></script>
<script type="text/javascript"> 
  window.q = "patchWidth=3&patchHeight=5&c1=tc&d1=tctc&e1=tc&c2=tctc&e2=tctc&d3=tc&shiftColsSE=2&shiftRowsSE=2&shiftColsSW=-2&shiftRowsSW=2&footside=-5,B-,-2,b-,,&tile=831,4-7,-5-&headside=5-,-c,6-,-c"
</script>

### 1
<script type="text/javascript"> 
  GF_panel.load({caption: "pair diagram", id: "pairs", controls: ['diagram', 'resize'], size: {width: "400px", height: "200px"}});
  GF_panel.diagramSVG({id: 'pairs', query:window.q});
</script>

### 2
<script type="text/javascript"> 
  GF_panel.load({caption: "thread diagram", id: "threads", controls: ['diagram', 'color', 'resize']});
  GF_panel.diagramSVG({id: 'threads', query:window.q, type:'thread'});
</script>

### 3
<script type="text/javascript"> 
  GF_panel.load({caption: "stitches", id: "droste", controls: ['cleanup', 'resize'], size: {width: "400px", height: "200px"}});
  document.getElementById('droste').outerHTML = '<textarea id="droste" name="droste">ctc,cross=ct,a1=ct</textarea>';
</script>

### 4
<script type="text/javascript"> 
  GF_panel.load({caption:"<pre>bd\npq</pre>", id:"nets", size: { width:"400px", height: "2em"}});
  document.getElementById('nets').innerHTML = "No significant content, just a demo of a multiline caption.";
</script>


Usage in github.io markdown
---------------------------

See [source]({{site.github.repository_url}}/blame/master/docs/{{page.path}}#L28-L66).

This example assumes you have a fork of this repository
and its _docs_ folder [configured](https://docs.github.com/en/pages/getting-started-with-github-pages/configuring-a-publishing-source-for-your-github-pages-site#publishing-from-a-branch)
to be published as GitHub pages. Otherwise, you need to copy the scripts
and images used by the _panel.js_ and adjust all their paths.

Widget description
------------------

To use the widget functions on your own page, include (copies of) the following scripts:
[d3.v4.min.js](/GroundForge/js/d3.v4.min.js),
[GroundForge-opt.js](/GroundForge/js/GroundForge-opt.js),
[nudgePairs.js](/GroundForge/js/nudgePairs.js) and
[panel.js](/GroundForge/js/panel.js).

### `GF_panel.load(options)`

Genrerates the panel structure:

    <figure class="gf-panel"><figcaption>...</figcaption><div id="..."></div></figure>

Options:
- `caption`: mandatory string, plain text or HTML string generated at the start of the `figcaption`.
- `id`: mandatory string, id for the generated `div`.
- `controls`: optional array of strings, default empty, specifies buttons to generate in the `figcaption`:
  - `cleanup`: ![](/GroundForge/images/broom.png) removes overridden stitch definitions in a textarea.
  - `diagram`:
    - ![](/GroundForge/images/wand.png) the href should be set by the page creating the panel.
    - ![](/GroundForge/images/play.png) starts/continues nudging stitch positions
  - `color`: color chooser, looks and dialog may vary per browser. Its value is used to highlight threads or stitches in thread diagrams.
  - `resize`: ![](/GroundForge/images/maximize.png) ![](/GroundForge/images/reset-dimensions.png) ![](/GroundForge/images/minimize.png)
    For a `div` with CSS style `overflow: auto; resize: both;`.
    A border makes the hot corner at the right bottom easy to find, even when a browser hides scroll bars.
- `size`: optional, object with `width` and `height` properties, e.g. `{width: "400px", height: "200px"}`.

### `GF_panel.diagramSVG(options)`

Generates an SVG diagram inside the `div` with the specified _id_.

Also to be called by the wand button.
The page context determines where to get the query and droste steps.

Options:
- `id`: optional string, id of a panel previously created with `GF_panel.load`.
  If omitted, an SVG is returned as string to be used outside a panel context, no nudging is applied.
- `query`: mandatory string with the pattern definition and stitches.
- `type`: optional string, either `thread` or `pair`, default is `pair`.
- `steps`: array of strings, default empty, droste stitch definitions:
  for each element a pair diagram is created from the (previous) thread diagram.

### `GF_panel.nudge(id)`

Nudges the stitch positions in the panel with the given id.

Called by default for all diagrams except for a primary pair diagram (type _pair_, step _0_),
also called by ![](/GroundForge/images/play.png).
A page may call this function explicitly for its primary pair diagram as well.