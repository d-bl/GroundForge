---
layout: default
title: GroundForge - Home
---

<p id="fallBack" style="display: none;">
You seem to have followed an old link to a pattern definition, you can try to recover the pattern:
download release 
<a href="https://github.com/d-bl/GroundForge/releases/download/2019-Q1/GroundForge-pages.zip"
>2019&ndash;Q1</a>, unzip, open the <code>index.html</code> file in your browser
and copy-paste the following text at the end of the address:
<br>
<input type="text" value="" id="toWayBack" style="width: 100%"/>
</p>
<script>
  var args = window.location.href.replace(/[^?]+/,"")
  document.getElementById("toWayBack").value = args
  if (args && args.trim() != "") {
    document.getElementById("fallBack").style = "display:block"
  }
</script>

Select a pair diagram from one of the [catalogue](#pattern-catalogues) pages or create your own.
Choose stitches for thread diagram variations and toggle the thread colors
to investigate the paths for contrasting threads. 
A [tutorial](help) explains how. 

Below two pair diagrams, each with four examples of thread diagrams.

![](help/images/weaving.png)

![](help/images/paris.png)

Pattern Catalogues
-----------------

Some pages have images with "diagram" links in their captions:
[Tesselace index](https://d-bl.github.io/tesselace-to-gf),
index on [Gertrude Whiting]'s sampler
and [Flanders](https://maetempels.github.io/MAE-gf/docs/flanders) (red/blue patches).

[Gertrude Whiting]: https://d-bl.github.io/gw-lace-to-gf

Other pages have their own conventions, such as linked images. 
[Paris](https://maetempels.github.io/MAE-gf/docs/paris), 
[bias](https://maetempels.github.io/MAE-gf/docs/bias), 
[spiders](https://maetempels.github.io/MAE-gf/docs/spiders), 
[snow](https://maetempels.github.io/MAE-gf/docs/snowflakes), 
[triangular](https://maetempels.github.io/MAE-gf/docs/tria), 
[lotus](https://maetempels.github.io/MAE-gf/docs/lotus), 
[3-pair joins](https://maetempels.github.io/MAE-gf/docs/misca#3-paired-join), 
and more from [MAE-gf](https://maetempels.github.io/MAE-gf/) (More Attractive Examples - *g*round*f*orge).

Pierre Fouché made 23 white patches of Tesselace 
[interpretations](https://github.com/veronika/tesselace-to-gf/blob/e21c823/docs/fouche_3x4.md).

A versatile [Binche](help/Binche) ground explained.