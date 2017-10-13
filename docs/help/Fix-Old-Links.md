---
layout: default
title: Fix Old Links
---

Fix Old Links
=============

Occasionally changes to the web pages affect the result of a link
you may have saved as bookmark, favorite, on a blog or wherever.
To recover the old content you can download an old version and
cut-and-paste the page address.


Download an old version
-----------------------

You can download and unzip the _source code_ of a [released] version or an in between version.
To download an in-between version examine the list of [commits],
copy the sha (an alphanumeric identifier) and replace `master` with the sha in the link:

    https://github.com/d-bl/GroundForge/archive/master.zip

Download the last version before the troubled link was created.

[commits]: https://github.com/d-bl/GroundForge/commits/master
[released]: https://github.com/d-bl/GroundForge/releases/


Cut-and-paste page address
--------------------------

Locate the unzipped file `index.html` (up to the release of 2016-10-2
it lives in the root, for later versions in `docs`) and open it in a browser.
From an online address starting with `d-bl.github.io/GroundForge/?`
or `d-bl.github.io/GroundForge/index.html?`
copy the question mark and the rest at the end of the page address `index.html`.


Precautions
-----------

To prevent having to track down the proper version you can archive a page.
Before linking to or archive the home page, make sure to click the
![link](images/link.png) button.

To be able to share any archived page you can use a general online service
like the [web archive] (alias way back machine) which preserves
a live snapshot of a web page, including images and scripts.

[web archive]: https://web.archive.org/
