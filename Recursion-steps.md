The [recursive] page uses a thread diagram as pair diagram to create another thread diagram. It does so at most two times. Even with so few steps larger dimensions could quickly overwhelm a browser, while you need larger dimensions to see the full pattern.

Batch execution is faster, doesn't make a fuss if it takes a while and allows as many recursion steps as your system can handle. Downside: it requires a few hoops to jump through.

<sub><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></sub>

- [Batch execution](#batch-execution)
  * [Requirements](#requirements)
  * [Create a diagram](#create-a-diagram)
- [Functions and Parameters](#functions-and-parameters)
  * [dibl.D3Data().get](#dibld3data--get)
  * [createSVG](#createsvg)


Batch execution
===============

Many variations of the following steps will work too.

Requirements
------------

A few steps are required to create an environment to [run JavaScript] without a browser. For example:

* Install [node.js] which should work on any operating system. It creates the commands `node`, `npm` and a `node.js command prompt`. A screen shot for Windows 10:

  [<img src="https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/nodejs-command-search-thumb.png">](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/nodejs-command-search-on-windows-10.png)
* Download and unzip `GroundForge`:
  * either a [release], since 2017-01-11
  * or the latest version, possibly not yet tagged as a release: [zip] or [tar.gz]
* Finally install required libraries. Start a terminal for that purpose. For example the `node.js command prompt`, not to be confused with `node.js` itself but any dos/bash/shell prompt should do.
  * Go to a directory somewhere on the path to the [docs/js] files in the unzipped GroundForge.
  * Execute `npm install jsdom`, this creates a directory [node_modules].

Should you choose to use another environment, you may have to write a variant of [batch.js].


[docs/js]: https://github.com/d-bl/GroundForge/tree/master/docs/js
[batch.js]: https://github.com/d-bl/GroundForge/blob/master/docs/js/batch.js
[run JavaScript]: https://en.wikipedia.org/wiki/List_of_ECMAScript_engines
[node.js]: https://nodejs.org
[release]: https://github.com/d-bl/GroundForge/releases
[zip]: https://github.com/d-bl/GroundForge/archive/master.zip
[tar.gz]: https://github.com/d-bl/GroundForge/archive/master.tar.gz
[node_modules]: https://nodejs.org/download/release/v6.9.1/docs/api/modules.html#modules_loading_from_node_modules_folders

Create a diagram
----------------

An example session of `node.js`:

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/batch-session.png)

You need to type the lines in red rectangles. 
Please compare the screen shot above with the actual usage comment in your version of `batch.js`.
Note that `svgFile` is required for `createSVG`, and the result of `d3Data().get` is used by `createSVG`.
Hint: type `;0` at the end of a line to reduce the returned clutter.

With the up and down arrows on your keyboard you can repeat and edit previous lines, even of previous sessions. [More...](https://nodejs.org/download/release/v6.9.1/docs/api/repl.html#repl_commands_and_special_keys) than you might want to know as it quickly goes into details for developers.

The greyed parts depend on
* where you unzipped GroundForge
* where you want to save your diagram
* whether you changed the [properties] of the `node.js` shortcut. The `./` of the `require` command is equivalent with the 'start in' property, `../` goes one directory up, so you probably need to start with `./../../Documents`. Note that you need the unix-style slashes even on a Windows operating system.

What goes between `(...)` is documented below.

The countdown process until the diagram gets saved runs in the back ground. Wait with new commands to prevent overwhelming your system. The more nodes where created, the longer each countdown step takes and the easier your system gets overwhelmed.

[properties]: https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/nodejs-shortcut-properties.png

Functions and Parameters
========================

`dibl.D3Data().get`
-------------------

Details on the [main] web page.

* **`compactMatrix`** - see legend on matrix tab. You can copy-paste from the tool tips on the [thumbnails] page, any sequence of non-alphanumeric characters is treated as a line separator.
* **`tileType`** - see values for drop down on matrix tab: `checker` or `bricks`
* **`stitches`** - see stitches tab
* **`rows`** - see patch size tab
* **`cols`** - see patch size tab
* **`shiftLeft`** - see footside tab
* **`shiftUp`** - see footside tab

[thumbnails]: https://d-bl.github.io/GroundForge/thumbs.html

Another signature used by createSVG:

* **`stitches`** - see step 2 and 3 on the [recursive] page
* **`data`** - the result of `dibl.D3Data().get` or the result of `createSVG`


`createSVG`
-----------

Defined in [batch.js]. The global variable `svgFile` should contain the file name. Subsequent calls without changing `svgFile` overwrites previous results without any warning.

* **`data`** - the result of `dibl.D3Data().get` or the result of this function
* **`steps`** - gets split at "`;`" into stitch instructions, each value is used to create a new thread diagram from a previous thread diagram used as pair diagram, see also step 2 and 3 on the [recursive] page. An empty string creates the initial thread diagram.
* **`colors`** - gets split at "`,`" into a color per thread, each value should start with a `#` followed by three or six hexadecimal digits.
* **`countDown`** - increase the value if a (large) pattern doesn't [stretch] out properly, each increment has same effect as a gentle nudge on the web page. The value should possibly be some function of `rows`, `cols` and the final number of created nodes.


[recursive]: https://d-bl.github.io/GroundForge/recursive.html
[main]: https://d-bl.github.io/GroundForge/
[stretch]: https://github.com/d-bl/GroundForge/blob/master/docs/images/bloopers.md#3
