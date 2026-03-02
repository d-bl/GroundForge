---
layout: default
title: patterns
javascript:
  - GroundForge-opt.js
  - panel.js
---

Pattern gallery
===============


**Work in progress, to do**:
* Sidebar subjects?

---

Clicking one letter shows all variants. 

<style>
    .gf_panel {display: inline-block; margin: 4px; }
    .gf_panel > div {width: 98%; overflow: auto; resize:both; border: #ddd solid 1px; }
    .gf_panel > figcaption {width: 100%; height:2.5em; padding-bottom: 0.2em; padding-left:0.5em; margin-left:0; margin-bottom:0; box-sizing: border-box; display: flex; align-items: center; background-color: #ddd; }
    .gf_panel > figcaption img {margin-left: 0.5em; }
    .gf_panel > figcaption > input {margin-left: 0.5em; width: 3em; }
    .gf_panel > div > textarea { height: 4.5em; width: 100% }
    .gf_panel > div > input { width: calc(100% - 1px) }
     #patterns > svg {display: inline-block; overflow: hidden; padding-top: 5px; padding-right: 5px; vertical-align: top; }
</style>
<script type="text/javascript" src="tile-gallery.js"></script>
<script> GF_tiles.load(document.getElementById('main-content')); </script>
<div id="previews"> </div>