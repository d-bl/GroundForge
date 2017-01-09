The [recursive] page uses a thread diagram as pair diagram to create another thread diagram. It does so at most two times. Even with so few steps larger dimensions could quickly overwhelm a browser, while you need larger dimensions to see the full pattern.

Batch execution is faster and doesn't make a fuss if it takes a while though it requires a few hoops to jump through.

Batch execution
===============

Many variations of the following steps will work too.

Requirements
------------

* Download and unzip [GroundForge].
* Install [node.js].
* This will create the commands `node` and `npm`.
* Execute: `npm install jsdom`
* Per command line session:
  * dos: `set NODE_PATH="C:\???\docs\js;/C:\???\node_modules"`
  * shell: `export NODE_PATH="/???/docs/js;/???/node_modules"`

The actual value for the triple question marks depends on where you unzipped GroundForge, respectively executed the `npm` command. If you understand what you are doing you can set the [environment variable] in the registry or some profile, details vary per operating system and your choice of command line terminal. An easier solution is putting the NODE_PATH command above and node command below in a `.bat` or `.sh` file.

Create a diagram
----------------

The example shows a mix of statements as command line arguments and in interactive mode. It seems a dos environment only works properly with interactive statements. [More...](https://nodejs.org/dist/latest-v7.x/docs/api/synopsis.html)

Subsequent calls to `createSVG` without changing the `svgFile` overwrites previous results without any warning.

    node -i -e 'require("batch.js");svgFile="tmp.svg"'            
    > createSVG(d3data, steps="ct;ctc", colors="#000,#000,#f00,#f00", animations=1)
    > .exit

[environment variable]: https://en.wikipedia.org/wiki/Environment_variable
[node.js]: https://nodejs.org
[main]:https://d-bl.github.io/GroundForge/
[recursive]:https://d-bl.github.io/GroundForge/recursive.html
[initial default pattern]: https://github.com/d-bl/GroundForge/blob/abd29a92bccaaa6c8aeb73c819a59ab62a6d0ccd/docs/js/batch.js#L66-L74
[GroundForge]: https://github.com/d-bl/GroundForge/archive/master.zip
[stretch]: https://github.com/d-bl/GroundForge/blob/master/docs/images/bloopers.md#3


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

creatSVG
--------

The global variable `svgFile` should contain the file name.

* **data** - the result of `dibl.D3Data().get` or this function, the [initial default pattern] is stored in the variable d3data
* **steps** - split at "`;`" into stitch instructions (a sequence of `ctlr` characters), each value is used to create a new thread diagram from a previous thread diagram used as pair diagram
* **colors** - split at "`,`" into a color per threads
* **animations** - increase the value if a (large) pattern doesn't [stretch] out properly, same effect as a gentle nudge on the web page. The value should possibly be a function of `d3data.threadNodes().length` and/or `d3data.threadLinks().length`.