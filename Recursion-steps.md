The [recursive] page uses a thread diagram as pair diagram to create another thread diagram. It does so at most two times. Even with so few steps larger dimensions could quickly overwhelm a browser, while you need larger dimensions to see the full pattern.

Off-line execution is faster, doesn't make a fuss if it takes a while, allows you to abort at your own discretion and allows as many recursion steps as your system can handle. Downside: it requires a few hoops to jump through: install an environment to execute JavaScript without a browser and get familiar with two or three function calls.

<sub>_[Table of contents generated with markdown-toc](http://ecotrust-canada.github.io/markdown-toc/)_</sub>

- [Off-line execution](#off-line-execution)
  * [Set up node.js](#set-up-nodejs)
  * [Create thread diagram with node.js](#create-thread-diagram-with-nodejs)
- [Functions and Parameters](#functions-and-parameters)
  * [Function `createSVG`](#function--createsvg-)
  * [Object `dibl.D3Data`](#object--dibld3data-)
    + [Constructor](#constructor)
    + [Factory methods `get`](#factory-methods--get-)
  * [Object `dibl.PatternSheet`](#object--diblpatternsheet-)
    + [Constructor](#constructor-1)
    + [method **`add`**](#method----add---)
    + [JavaScript example](#javascript-example)
    + [Java example](#java-example)


Off-line execution
==================

The SVG documents with the diagrams can also be generated in other JavaScript environments than a web-browser. Many variations of the following steps are possible.

Set up node.js
--------------

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

Create thread diagram with node.js
----------------------------------

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

Function `createSVG`
--------------------

Only JavaScript

Defined in [batch.js].

* **`svgFile`** - the global variable became the first argument _after [release] 2017-01-11_. Existing files are overwritten without a warning.
* **`data`** - the result of `dibl.D3Data().get` or the result of this function. With an empty string for steps, this argument can be a JavaScript object with just the functions `threadNodes()` and `threadLinks()`. 
* **`steps`** - gets split at "`;`" into stitch instructions, each value is used to create a new thread diagram from a previous thread diagram used as pair diagram, see also step 2 and 3 on the [recursive] page. An empty string creates the initial thread diagram.
* **`colors`** - gets split at "`,`" into a color per thread, each value should start with a `#` followed by three or six hexadecimal digits.
* **`countDown`** - increase the value if a (large) pattern doesn't [stretch] out properly, each increment has same effect as a gentle nudge on the web page. The value should possibly be some function of `rows`, `cols` and the final number of created nodes.


[recursive]: https://d-bl.github.io/GroundForge/recursive.html
[main]: https://d-bl.github.io/GroundForge/
[stretch]: https://github.com/d-bl/GroundForge/blob/master/docs/images/bloopers.md#3

The following JavaScript example (with an empty string for `steps`) creates a thread diagram of a twist.

```JavaSCript
data = new function() {
  this.threadNodes = function (){ return [
    { x: 500, y: 500, startOf: 'thread1', title: 1 },
    { x:   0, y: 500, startOf: 'thread2', title: 2 },
    { title: 'twist' },
    { x: 500, y:   0, bobbin: 'true' },
    { x:   0, y:   0, bobbin: 'true' }
  ]}
  this.threadLinks = function(){ return [
    { source: 0, target: 2, thread: 1, start: 'thread', end: 'white' },
    { source: 1, target: 2, thread: 2, start: 'thread' },
    { source: 2, target: 3, thread: 2, end: 'white', left: true },
    { source: 2, target: 4, thread: 1, start: 'white', right: true }
  ]}
}
createSVG("./twist.svg",data,"","#f00,#0f0",1);0
```

Object `dibl.D3Data`
--------------------

The object has functions returning arrays of maps:
- **`pairNodes()`**
  - ...
- **`pairLinks()`**
  - ...
- **`threadNodes()`**
  - The `title` property is shown as tool tip by the major desktip browsers when hovering with the mouse over the node
  - The `x`/`y` properties are the initial position of the node and can prevent a rotated and/or flipped diagram
  - The `startOf` property is required to paint threads. Its value should start with '`thread`' followed by a number.
  - The `bobbin` property causes a special shape for the node.
- **`threadLinks()`**
  - The `thread` property is required to paint threads.
  - The `left`/`right` properties determine the curve direction, to prevent the links of repeated twists lying on top of one another.
  - The `source`/`target` properties are indexes in the `threadNodes` array.
  - The `end`/`start` properties with value `white` determine which end has some distance to the node for the over/under effect. Value `thread` marks the start of a thread to paint an individual thread in interactive mode.


### Constructor

The (Scala only) constructor expects a pair diagram as argument which is an object with functions returning arrays of maps:
  - **`nodes()`** - assigned as is to the returned object
  - **`links()`** - assigned as is to the returned object

### Factory methods `get`

Both Scala and JavaScript, details on the [main] web page.

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



Object `dibl.PatternSheet`
--------------------------

### Constructor

* **`patchRows`** - number of rows for the generated patterns. Recommended value: 3 for a portrait A4 or letter sheet, 2 for landscape. 
* **`pageSize`** - string with attributes for the root SVG element, in practice the dimensions of the page.

### method **`add`**

* **`compactMatrix`** - see legend on matrix tab of the [main] web page. You can copy-paste from the tool tips on the [thumbnails] page, any sequence of non-alphanumeric characters is treated as a line separator.
* **`tileType`** - see values for drop down on matrix tab: `checker` or `bricks`.

### JavaScript example

```JavaScript
var patterns = new dibl.PatternSheet(2, "height='100mm' width='110mm'")
patterns.add("586- -789 2111 -4-4", "checker");0
fs.writeFile("./sheet.svg", patterns.toSvgDoc(), function(err) {
    if(err) return console.log(err)
    else console.log("saved")
});0
```

### Java example

```java
PatternSheet patterns = new dibl.PatternSheet(2, "height='100mm' width='110mm'");
patterns.add("586- -789 2111 -4-4", "checker");
new java.io.FileOutputStream("sheet.svg").write(patterns.toSvgDoc().getBytes());
```
