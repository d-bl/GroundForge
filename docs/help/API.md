---
layout: default
title: API
---

API
===

- [Execution environments](#execution-environments)
  * [Web browser](#web-browser)
  * [JVM alias Scala / Java](#jvm-alias-scala--java)
  * [Set up node.js for JavaScript](#set-up-nodejs-for-javascript)
  * [Create a thread diagram with node.js](#create-a-thread-diagram-with-nodejs)
- [Functions and Classes](#functions-and-classes)
  * [Functions <strike>`createSVG`</strike>, `createThreadSVG`, `createPairSVG`](#functions-createsvg-createthreadsvg-createpairsvg)
    + [JavaScript example](#javascript-example)
  * [Class `dibl.D3Data`](#class-dibld3data)
    + [Methods, all returning arrays of maps](#methods-all-returning-arrays-of-maps)
    + [Constructor](#constructor)
    + [Factory methods `get`](#factory-methods-get)
  * [Class `dibl.SheetSVG`](#class-diblsheetsvg)
    + [Method **`add`**](#method-add)
    + [Constructor](#constructor-1)
    + [JavaScript example](#javascript-example-1)
    + [Java example](#java-example)
    + [Scala example](#scala-example)


Many alternative environments and mash-ups are possible.
Modern browsers can display the SVG files resulting from
the demo classes under `src/test`, or created by off-line execution.


Execution environments
======================

Web browser
-----------

Demos: a [dressed up](/GroundForge/) version and a dressed down [docs/API](/GroundForge/API/index.html) version.

The scripts and page in `docs/API` are minimalistic versions of its siblings in `docs` and `docs/js`.
The dressed up version adds decoration, event handling, configuration and some help.

The scripts `index.js` and `jscolor.js` collect the configuration from an HTML form.
The collected data is passed on to the scala code which is compiled into `GroundForge-opt.js`. 
This scala code does the number crunching to create the diagrams bound by a squared grid
and creates input for [D3.js] to render the dynamic pair and thread diagrams.

The development view for the thread and pair diagrams is a slightly less minimal page.
For that purpose `src/main/resources/index-dev.html` is served by sbt as
`http://localhost:12345/target/scala-2.11/classes/index-dev.html`,
this page immediately reflects changes in the scala code though the simulation doesn't start.

[D3.js]: http://d3js.org/


JVM alias Scala / Java
----------------------

To skip a build and/or put the jar with [dependencies] on your class path,
you can create a scala worksheet between the source code.
The extension of worksheets is `.sc` which is git-ignored.
An IDE can compile and run the worksheet as you type.

The `SheetSVG` is ready to create an SVG in a JVM environment.
A [PatternDemos class] shows batch generation of pair/thread diagrams.
Though written in scala both can as easily be used in java.

[facades]: https://github.com/spaced/scala-js-d3/issues/25
[PatternDemos class]: https://github.com/d-bl/GroundForge/blob/master/src/test/scala/dibl/PatternDemos.scala
[dependencies]: https://github.com/d-bl/GroundForge/blob/b97deb1963be7e9cacb8836e708783174c3f877a/pom.xml#L12-L28


Set up node.js for JavaScript
-----------------------------

A few steps are required to create an environment to [run JavaScript] without a browser. For example:

* Install [node.js] which should work on any operating system. It creates the commands `node`, `npm` and a `node.js command prompt`. A screen shot for Windows 10:

  [<img src="/GroundForge/help/images/nodejs-command-search-thumb.png">](/GroundForge/help/images/nodejs-command-search-on-windows-10.png)
* Download and unzip `GroundForge`:
  * either a [release], since 2017-01-11
  * or the latest version, possibly not yet tagged as a release: [zip] or [tar.gz]
* Finally install required libraries. Start a terminal for that purpose. For example the `node.js command prompt`, not to be confused with `node.js` itself but any dos/bash/shell prompt should do.
  * Go to a directory somewhere on the path to the [docs/js] files in the unzipped GroundForge.
  * Execute `npm install jsdom`, this creates a directory [node_modules].

Should you choose to use another environment, you may have to write a variant of [create-svg.js].


[docs/js]: https://github.com/d-bl/GroundForge/tree/master/docs/js
[create-svg.js]: https://github.com/d-bl/GroundForge/blob/master/src/main/resources/create-svg.js
[run JavaScript]: https://en.wikipedia.org/wiki/List_of_ECMAScript_engines
[node.js]: https://nodejs.org
[release]: https://github.com/d-bl/GroundForge/releases
[zip]: https://github.com/d-bl/GroundForge/archive/master.zip
[tar.gz]: https://github.com/d-bl/GroundForge/archive/master.tar.gz
[node_modules]: https://nodejs.org/download/release/v6.9.1/docs/api/modules.html#modules_loading_from_node_modules_folders


Create a thread diagram with node.js
------------------------------------

An example session of `node.js`:

![node.js session](/GroundForge/help/images/node-js-session.png)

Type the lines after the prompt `>`. 
Please compare the screen shot above with the actual usage comment in your version of <strike>`batch.js`</strike> `create-svg.js`.
Note that the result of `d3Data().get` is used by `createSVG`,
the `;0` at the end of a line reduces the returned clutter.

With the up and down arrows on your keyboard you can repeat and edit previous lines,
even of previous sessions. [More...] than you might want to know
as it quickly goes into details for developers.

[More...]: https://nodejs.org/download/release/v6.9.1/docs/api/repl.html#repl_commands_and_special_keys

The greyed parts depend on
* where you unzipped GroundForge
* where you want to save your diagram
* whether you changed the [properties] of the `node.js` shortcut. The `./` for the file names is equivalent with the 'start in' property, `../` goes one directory up, so you probably need to start with `./../../Documents`. Note that you need the unix-style slashes even on a Windows operating system.

What goes between `(...)` is documented below.

The countdown process until the diagram gets saved runs in the back ground. The more nodes where created, the longer each countdown step takes. Issuing a new command might abort the one still running.

[properties]: /GroundForge/help/images/nodejs-shortcut-properties.png

 
Functions and Classes
=====================

The code under `src/main/scala/dibl` has two classes with `@JSExport` annotations: D3Data and SheetSVG.
This allows to access them in a JavaScript environment such as browsers and node.js.


Functions <strike>`createSVG`</strike>, `createThreadSVG`, `createPairSVG`
-------------------------------------------------------------------------

Convenience methods defined for a node.js environment,
defined in <strike>batch.js</strike> [create-svg.js],
_the script was renamed after [release] 2017-01-11_.

    createThreadSVG(svgFile,data,steps,colors,countDown)
    createPairSVG(svgFile,data,countDown)

* **`svgFile`** - the global variable became the first argument _after [release] 2017-01-11_. Existing files are overwritten without a warning.
* **`data`** - the result of `dibl.D3Data().get` or the result of this function. With an empty string for steps, this argument can be a JavaScript object with just the functions `threadNodes()` and `threadLinks()`. 
* **`steps`** - gets split at "`;`" into stitch instructions, each value is used to create a new thread diagram from a previous thread diagram used as pair diagram, see also step 2 and 3 on the [main] page. An empty string creates the initial thread diagram.
* **`colors`** - gets split at "`,`" into a color per thread, each value should start with a `#` followed by three or six hexadecimal digits.
* **`countDown`** - increase the value if a (large) pattern doesn't [stretch] out properly, each increment has same effect as a gentle nudge on the web page. The value should possibly be some function of `rows`, `cols` and the final number of created nodes.


[main]: /GroundForge/
[stretch]: /GroundForge/help/Bloopers#3


### JavaScript example

The following JavaScript example (with an empty string for `steps`) creates a thread diagram of a twist.

```javascript
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
createThreadSVG("./twist.svg",data,"","#f00,#0f0",1);0
```


Class `dibl.D3Data`
--------------------

### Methods, all returning arrays of maps

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

The constructor (Scala/Java only) expects a pair diagram as argument which is an object with functions returning arrays of maps:
  - **`nodes()`** - assigned as is to the returned object
  - **`links()`** - assigned as is to the returned object


### Factory methods `get`

Download version [0f39fa2](https://github.com/d-bl/GroundForge/archive/0f39fa2.zip) and examine `docs/index.html` for details.

* **`compactMatrix`** - ..., any sequence of non-alphanumeric characters or dashes is treated as a line separator.
* **`tileType`** - see values for drop down on matrix tab: `checker` or `bricks`
* **`stitches`** - see stitches tab
* **`rows`** - see patch size tab
* **`cols`** - see patch size tab
* **`shiftLeft`** - see footside tab
* **`shiftUp`** - see footside tab

Another signature used by `createThreadSVG`:

* **`stitches`** - see level 2 and 3 on the [main] page
* **`data`** - the result of `dibl.D3Data().get` or the result of `createSVG`


Class `dibl.SheetSVG`
--------------------------
This [demo](/GroundForge/sheet.html?img=376&patch=B-C-%20---5%20C-B-%20-5--;checker&patch=5831%20-4-7;checker&patch=68%20-4;checker&patch=-4-7%205---%20-C-B%203158;bricks&patch=5-O-E-%20-E-5-O%205-O-E-;bricks)
shows a set of diagrams arranged depending on the available width.

### Method **`add`**

* **`compactMatrix`** - Matrix as explained in this [help page](/GroundForge/help/Reversed-engineering-of-patterns), for example `B-C-,---5,C-B-,-5--`.
More examples are available via the [TesseLace Index](/GroundForge/help/TesseLace-Index).
Select one and copy-paste the matrices displayed just above one of the diagrams. 
* **`tileType`** - `checker` or `bricks`.


### Constructor

* **`patchRows`** - default: `2`. Number of rows for the generated patterns. 
* **`pageSize`** - default: `"height='210mm' width='297mm'"` which is a landscape A4. String with attributes for the root SVG element. An A4 or US-letter sized [sheet of paper] can take 2x3 patches.

[sheet of paper]: https://en.wikipedia.org/wiki/Paper_size


### JavaScript example

With a local copy of `show-graph.js` from the source code of release
[2017-06-05](https://github.com/d-bl/GroundForge/releases/tag/2017-06-05)
or possibly a later version:

```html
<html>
<head>
    <script src="GroundForge-opt.js" type="text/javascript"></script>
</head>
<body>
    <div id="diagram"></div>
    <script>
        var sheet = new dibl.SheetSVG(2, "height='100mm' width='100mm'", "1")
        sheet.add("B-C-,---5,C-B-,-5--","checker")
        document.getElementById("diagram").innerHTML += sheet.toSvgDoc().trim()
    </script>
</body>
</html>
```


### Java example

```java
SheetSVG patterns = new dibl.SheetSVG(2, "height='100mm' width='110mm'");
patterns.add("586- -789 2111 -4-4", "checker");
new java.io.FileOutputStream("sheet.svg").write(patterns.toSvgDoc().getBytes());
```

Of course you should close the stream.


### Scala example

```scala
val patterns = dibl.SheetSVG(2, "height='100mm' width='110mm'")
patterns.add("586- -789 2111 -4-4", "checker")
scala.reflect.io.File("PATH/TO/GroundForge/target/sheet.svg").writeAll(patterns.toSvgDoc())
```

