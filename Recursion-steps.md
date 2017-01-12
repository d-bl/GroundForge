The [recursive] page uses a thread diagram as pair diagram to create another thread diagram. It does so at most two times. Even with so few steps larger dimensions could quickly overwhelm a browser, while you need larger dimensions to see the full pattern.

Batch execution is faster, doesn't make a fuss if it takes a while and allows as many recursion steps as your system can handle. Downside: it requires a few hoops to jump through.

Batch execution
===============

Many variations of the following steps will work too.

Requirements
------------

* Install [node.js].
* This will create the commands `node` and `npm`.
* Download and unzip [GroundForge].
* Start a terminal, for example the `node.js command prompt`
  * go to the directory where you unzipped GroundForge
  * execute `npm install jsdom`, this creates a directory [node_modules] which must be either in the same folders as the `.js` files of GoundForge, or at a higher level.

[node.js]: https://nodejs.org
[GroundForge]: https://github.com/d-bl/GroundForge/releases
[node_modules]: https://nodejs.org/download/release/v6.9.1/docs/api/modules.html#modules_loading_from_node_modules_folders

Create a diagram
----------------

An example session of `node`:

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/batch-session.png)

You need to type the lines in red rectangles.
Note that `svgFile` is required for `createSVG`, and the `data` returned by d3Data().get is used by `createSVG`.

With the up and down arrows on your keyboard you can repeat and edit previous lines, even of previous sessions. [More...](https://nodejs.org/download/release/v6.9.1/docs/api/repl.html#repl_commands_and_special_keys) than you might want to know as it quickly goes into details for developers.

The greyed parts depend on
* where you unzipped GroundForge
* where you want to save your diagram
* whether you changed the properties (start at) of `node.js`, by Windows' default you probably need to start with `./../../Documents`. Note that you need the unix-style slashes even on a Windows operating system.

What goes between `(...)` is documented below.

The countdown process until the diagram gets saved runs in the back ground. Wait with new commands to prevent overwhelming your system. The more nodes where created, the longer each countdown step takes and the easier your system gets overwhelmed.


Functions and Parameters
========================

dibl.D3Data().get
-----------------

Details on the [main] web page.

* **compactMatrix** - see legend on matrix tab
* **tileType** - see values for drop down on matrix tab: `checker` or `bricks`
* **stitches** - see stitches tab
* **rows** - see patch size tab
* **cols** - see patch size tab
* **shiftLeft** - see footside tab
* **shiftUp** - see footside tab

createSVG
---------

The global variable `svgFile` should contain the file name. Subsequent calls without changing `svgFile` overwrites previous results without any warning.

* **data** - the result of `dibl.D3Data().get` or the result of this function
* **steps** - gets split at "`;`" into stitch instructions, each value is used to create a new thread diagram from a previous thread diagram used as pair diagram, see also step 2 and 3 on the [recursive] page. An empty string creates the initial thread diagram.
* **colors** - gets split at "`,`" into a color per thread, each value should start with a `#` followed by three or six hexadecimal digits.
* **countDown** - increase the value if a (large) pattern doesn't [stretch] out properly, each increment has same effect as a gentle nudge on the web page. The value should possibly be some function of `rows`, `cols` and the final number of created nodes.


[recursive]: https://d-bl.github.io/GroundForge/recursive.html
[main]: https://d-bl.github.io/GroundForge/
[stretch]: https://github.com/d-bl/GroundForge/blob/master/docs/images/bloopers.md#3
