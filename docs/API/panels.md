---
layout: default
title: API demo
---

Panels
======

Under construction.

This widget is intended to replace hardcoded content on the pages [
stitches](/GroundForge/stitches), 
[nets](/GroundForge/nets), 
[droste](/GroundForge/droste) and the
[snow mixer](/GroundForge/mix4snow).


Widget demo's
-------------

<script src="/GroundForge/js/d3.v4.min.js" type="text/javascript"></script>
<script src="/GroundForge/js/GroundForge-opt.js" type="text/javascript"></script>
<script src="/GroundForge/js/panel.js" type="text/javascript"></script>
<script src="/GroundForge/js/nudgePairs.js" type="text/javascript"></script>
<style>
        figure {max-width: 90%; display: inline-block;}
        figure pre {display: inline-block; padding:0; margin:0; background: #ddd}
        figure div {overflow: auto;resize: both}
        figcaption {text-align: center;background-color: #ddd;padding: 5px; border-radius: 5px; display: flex; align-items: center;}
        figcaption img {padding-left: 0.5em;}
</style>
<script type="text/javascript"> 
  window.q = "patchWidth=11&patchHeight=11&c1=tc&d1=tctc&e1=tc&c2=tctc&e2=tctc&d3=tc&shiftColsSE=2&shiftRowsSE=2&shiftColsSW=-2&shiftRowsSW=2&footside=-5,B-,-2,b-,,&tile=831,4-7,-5-&headside=5-,-c,6-,-c"
</script>

### 1
<script type="text/javascript"> 
  GF_panel.load({caption: "pair diagram", id: "pairs", controls: ['diagram', 'resize']});
  document.getElementById('pairs').innerHTML = GF_panel.diagramSVG({query:window.q});
</script>

### 2
<script type="text/javascript"> 
  GF_panel.load({caption: "thread diagram", id: "threads", controls: ['diagram', 'color', 'resize']});
  document.getElementById('threads').innerHTML = GF_panel.diagramSVG({query:window.q, type:'thread'});
  nudgeDiagram(d3.select('#threads').select("svg"))
</script>

### 3
<script type="text/javascript"> 
  GF_panel.load({caption:"<pre>bd\npq</pre>", id:"nets", controls: ['resize']}); 
  document.getElementById('nets').innerHTML = "no content, just a demo of multiline caption";
</script>

### 4
<script type="text/javascript"> 
  GF_panel.load({caption:"stitches", id:"droste",controls: ['cleanup', 'resize']});
  const content = '<textarea id="droste" name="droste2">ctc,cross=ct</textarea>';
  document.getElementById('droste').outerHTML = content;
</script>


Usage in github.io markdown
---------------------------

See [source]({{site.github.repository_url}}/blame/master/docs/{{page.path}}#L21-L59).
This example assumes you have a fork of this repository
and its _docs_ folder [configured](https://docs.github.com/en/pages/getting-started-with-github-pages/configuring-a-publishing-source-for-your-github-pages-site#publishing-from-a-branch)
to be published as GitHub pages. Otherwise, you need to copy the scripts
and images used by the _panel.js_ and adjust all their paths.

Please note that it is better practice to move the styles into your own CSS files.
