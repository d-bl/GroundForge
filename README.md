# [DEMO](https://d-bl.github.io/GroundForge/)
A toolbox to design bobbin lace grounds with matching diagrams.

How it's Made / third party data and scripts
============================================

Proof of concept with D3.js
---------------------------

To get a [proof of concept] a force graph [example] with originally v3 of [D3.js] was changed into tiny thread an pair diagrams diagrams with the following steps:

- Replaced the server side JSon with the hard-coded [js/sample.js] assembled from a manual sketch [js/sample.png].
- Applied arrow heads and flattened them to line ends to emulate a [color coded pair diagrams] or to emulate the over/under effect in thread diagrams. Later versions create the over/under effect with shortened lines, for better browser support and performance, a too complicated approach for the multiple colors of a color coded diagram.
- Made nodes transparent except for bobbins.
- Assigned the thread number as a class to each section of a thread to assign colors.
- Turned the links from lines to paths with a third node to add mid-markers for twist marks.
- Initial coordinates replace the default random values, thus the animation stabalizes much quicker and it prevents rotated and flipped diagrams.

[proof of concept]: https://cdn.rawgit.com/d-bl/GroundForge/84eee36/index.html
[example]: http://bl.ocks.org/mbostock/4062045
[D3.js]: http://d3js.org/
[js/sample.js]: https://github.com/d-bl/GroundForge/blob/7a94b67/js/sample.js
[js/sample.png]: https://github.com/d-bl/GroundForge/blob/50421a2/js/sample.png
[color coded pair diagrams]: https://en.wikipedia.org/w/index.php?title=Mesh_grounded_bobbin_lace&oldid=639789191#Worker_pair_versus_two_pair_per_pin


Using data from TesseLace
-------------------------

Scientific research presented at [TesseLace] resulted in patterns defined by matrices.
These patterns are alternatives for the `js/sample.js` used for the proof of concept.
GroundForge uses a [compact] matrix format using a character to tag a node. The character defines the composition of incoming pairs of the node.
The `src/maini/scala` code compiled into `docs/js/matrix-graphs.js` transforms these matrices into data for D3js.

The geometric information within the matrices is used to initialise the thread diagrams, speeding up the animation as explained for the proof of concept.
These [pages] were used to create the thumbnails for a selection of patterns.
The diagrams lack the original geometric information after completion of the animation,
so topological duplicates were removed from the generated thumbnails.
Downloadable pattern sheets provide geometric variations that can be customised into intermediate and other variations.

The thread diagrams are not generated from the matrices,
but from the generated JSon data for pair diagrams by replacing nodes with stitches.
To keep track of the threads while constructing the diagram, 
the algorithm has to figure out a working order to create the lace just like a real lace maker does.

[pages]: https://github.com/d-bl/GroundForge/blob/master/src/test/resources/
[compact]: https://d-bl.github.io/GroundForge/docs/images/legend.png
[TesseLace]: http://TesseLace.com


Color-picker: jscolor
---------------------

Safari nor Internet Explorer support `<input type="color">`. The free [color-picker](http://jscolor.com/) works on both platforms and was easy to apply.


Scala code
==========
 
The scala code takes care of the number crunching that assembles the data for D3js and the SVG for the pattern sheet. The scala code is compiled into `matrix-graphs.js`. An HTML form takes care of configuration, JavaSript  feeds the assembled data to the [D3.js] API with `show-graphs.js` and takes care of event handling with `index.js` and `jscolor.js`.

The code under `src/main/scala/dibl` has two classes with `@JSExport` annotations. The `dibl.D3Data` results are visualised with D3js. The toSVG result of `dibl.PatternSheet` is written to files by unit tests or assigned to the innerHTML of a DOM element on the web pages. Modern browsers can display the SVG test files. 

The scripts and page in `docs/API` are minimalistic versions of its siblings in `docs` and `docs/js`, the dressed up version adds decoration, event handling, configuration and some help. The development view for the thread and pair diagrams is a slightly less minimal page. For that purpose `src/main/resources/index-dev.html` is served by sbt as `http://localhost:12345/target/scala-2.11/classes/index-dev.html`, this page immediately reflects changes in the scala code though the animation doesn't start.

[API Demo](https://d-bl.github.io/GroundForge/API)


Compile and preview
-------------------

[![Build Status](https://travis-ci.org/d-bl/GroundForge.svg?branch=master)](https://travis-ci.org/d-bl/GroundForge)


### Requirements

- The pages in the docs directory don't require any compilation until the publish phase described below.
- [sbt] 0.13.7 or higher, to compile the source code to JavaScript
- [maven], to run the test with another JVM than scala was built with
- a browser with proper SVG support, for example FireFox or Safari but not Internet Explorer.
  Chrome has proper SVG support but with the default settings it has intranet problems with the development view.

To completely [replace sbt] seems quite a detour.

[replace sbt]: http://stackoverflow.com/questions/26512750/how-to-use-scala-js-from-maven
[sbt]: http://www.scala-sbt.org/download.html
[maven]: https://maven.apache.org/


### Steps

- Fork the project and make a local clone.
- Avoid working on the master branch, rather create branches and pull requests.
- Go to the root of the local project and start the command `sbt '~fastOptJS'`
- Monitor the result on `http://localhost:12345/target/scala-2.11/classes/index-dev.html`
  It is a dressed down version of the published page, with possibly experimental features added.
  Nodes and links invisible in the published page are shown faint for debugging purposes.
- Saving code changes updates the page automatically but the animation does not start.


Important code conventions
--------------------------

- Never catch exceptions in a `Try` as exceptions terminate the JavaScript. Prevent exceptions like illegal arguments and indexes and create a `Failure` for safe execution with JavaScript.
- Restrict the use of raw js objects to the API level: the classes and methods annotated with `@JSExport`. This allows execution of test classes with another JVM than ScalaJS was built with.

The applied Scala coding techniques are explained by this [course] up and including workshop 3, except that the main code doesn't use any io, and the hand full of files written by tests hardly justify the clutter of closing files let alone using a library. 

[course]: https://github.com/DANS-KNAW/course-scala


Unit tests
----------

The command `sbt test` only compiles the test classes.
Haven't found the proper incantation to execute the test with sbt, might be caused by a dependency conflict.

The command `mvn clean test` executes the tests, your IDE might too. Launching tests with the IDE while sbt is still processing a change may cause weird errors, just try again.


Publish
-------

- Compile with `sbt '~fullOptJS'`
- Copy the content of `target\scala-2.11\groundforge-opt.js` into `docs/js/matrix-graphs.js`
- Check the results with `index.html`
- If ok: commit, push and create a pull request 
- Optional: if merged with your master branch you can check the online demo in your own github fork: `http://YOURID.github.io/GroundForge/` 
