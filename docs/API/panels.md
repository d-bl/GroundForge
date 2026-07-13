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
  GF_panel.load({
    caption: "pair diagram",
    id: "pairs",
    wandHref: 'javascript: return 0',
    controls: ['resize'],
    size: {width: "400px", height: "200px"},
    parent: document.currentScript.parentNode
  });
  GF_panel.diagramSVG({id: 'pairs', query:window.q, type: "pair"});
</script>

### 2
<script type="text/javascript"> 
  GF_panel.load({
    caption: "thread diagram",
    id: "thread_panel",
    wandHref: 'javascript: return 0',
    controls: ['color', 'resize'],
    parent: document.currentScript.parentNode
  });
  GF_panel.diagramSVG({id: 'thread_panel', query:window.q, type:'thread'});
</script>

### 3
<script type="text/javascript"> 
  GF_panel.load({
    caption: "stitches",
    id: "droste",
    controls: ['cleanup', 'resize'],
    size: {width: "400px", height: "200px"},
    parent: document.currentScript.parentNode
  });
  document.getElementById('droste').outerHTML = '<textarea id="droste" name="droste">ctc,cross=ct,a1=ct</textarea>';
</script>

### 4
<script type="text/javascript"> 
  const opts4 = {caption:"<pre>bd\npq</pre>", id:"nets", size: { width:"400px", height: "2em"}, parent: document.currentScript.parentNode};
  GF_panel.load(opts4);
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

See jsDoc, either in the source code or as generated with a GitHub workflow, uploaded with releases.
