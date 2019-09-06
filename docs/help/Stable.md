---
layout: default
title: Stable version / 3rd party pages
---

* [Stable version](#stable-version)
* [Reuse on 3rd part pages](#reuse-on-3rd-part-pages)
* [Batch or server side](#Batch-or-server-side)

Stable version
==============

When giving a demo or course, you do not want to be surprised with significant changes to the website.
The lists of steps below will give you your own stable version.

* Register or login at github.
* [Fork] the project, in this context to _not_ stay synced.
* [Configure] the `/docs` folder as publishing source for GitHub Pages,
  it will tell you where you can access the pages.
* A simple alternative to get synced again:
  delete your own fork and repeat the process.
  This way you don't need to install anything, just web access and a browser.
  But it is a brute force approach that might cause permanent loss of online artifacts.

[Configure]: https://help.github.com/en/articles/configuring-a-publishing-source-for-github-pages
[Fork]: https://help.github.com/en/articles/fork-a-repo#fork-an-example-repository

Some links in your own copy may be hardwired back to the official version.
Monitor the address bar of your browser.
When it shows `d-bl` at the start, replace it with your own github account name to return to your own copy.

<a name="reuse"></a>

Reuse on 3rd part pages
=======================

Third party webmasters can reuse smaller or larger portions of the GroundForge website.
The most simple option is a screen shot snippet of a specific pattern 
(or your own diagram) and the corresponding link.
Remember to use the ![link](../images/link.png) button before you 
copy-paste the address of a specific pattern.
Other options require the possibility to deploy your own custom JavaScripts.
The following suggestions assume a proficient knowledge of html and javascript.

A convenience form can create a dynamic link to a base pattern.
Thus visitors can make variations on a family of patterns by choosing stitches or other properties.
Known examples:
* [valenciennes] used on the [Whiting-Index]
* [woble.html] based on [MAE-gf/droste]

[MAE-gf/droste]: https://maetempels.github.io/MAE-gf/docs/droste
[woble.html]: https://github.com/MAETempels/MAE-gf/blob/master/_includes/wobble.html
[valenciennes]: https://github.com/d-bl/GroundForge/blob/master/docs/_includes/val-variants.html
[Whiting-Index]: https://d-bl.github.io/GroundForge/help/Whiting-Index#val
[woble.html]: https://github.com/MAETempels/MAE-gf/blob/master/_includes/wobble.html

You can even embed one or more of the dynamic diagrams on your pages.
To get started download both files on [docs/API]: save the raw versions together in one directory. 
Change one JavaScript reference in the html file to the following address
(note that `{NNN}` means the latest commit [number]):

```
https://raw.githubusercontent.com/d-bl/GroundForge/{NNN}/docs/js/GroundForge-opt.js
```
Now the downloaded page should show the same diagrams as the public [API page].

Finally edit the `load` function in the downloaded script to drop the diagrams you don't want.
Assign dynamic values to the hardcode value `q` and/or arguments for `patterns.add`. 
An option to obtain these values are the convenience forms discussed above.

Batch or server side
====================
A Java library is also available for batch processing or server side processing.
This library provides the same classes as called form the `load` function in the JavaScript. 
The `jar` file is available between the assets of the [latest release],
it might be behind on the API web page.
Sadly no known java equivalent for the force graphs of the D3js library called in the `showGraph` function. 
Without applying force graphs, the thread diagrams are of no use
and after all generating thread diagrams from pair diagram is the core of GroundForge.

[latest release]: https://github.com/d-bl/GroundForge/releases/latest
[number]: https://github.com/d-bl/GroundForge/commits/master/docs/js/GroundForge-opt.js
[docs/API]: https://github.com/d-bl/GroundForge/tree/master/docs/API
[API page]: https://d-bl.github.io/GroundForge/API
