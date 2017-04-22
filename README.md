_A toolbox to design bobbin lace grounds with matching diagrams._

DEMO's
======

A [dressed up](https://d-bl.github.io/GroundForge/) version and a dressed down [docs/API](https://d-bl.github.io/GroundForge/API) version. The latter shows a starting point to configure your own web interface or embed just some of the diagrams on your own web page, though you may want to start by injecting the `show-graph.js` of the dressed up version into the dressed down version. See also the [API] for other environments than a web-browser.

[API]: docs/API.md
[![Build Status](https://travis-ci.org/d-bl/GroundForge.svg?branch=master)](https://travis-ci.org/d-bl/GroundForge) 
Note that just building does not copy the compiled code into a local version of the demo's.


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
These [pages] were used to create the gallery images for a selection of patterns.
The diagrams lack the original geometric information after completion of the animation,
so topological duplicates were removed from the generated gallery patterns.
Downloadable pricking prototypes provide geometric variations that can be customised into intermediate and other variations.

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


Compile and preview
-------------------

### Requirements

- The pages in the docs directory don't require any compilation
- To compile `src/main/scala` to JavaScript: [sbt] 0.13.9 or higher
- To execute the tests: maven or sbt
- For the developer view of the diagrams: a browser with proper SVG support, for example FireFox or Safari but not Internet Explorer.
  Chrome has proper SVG support but with the default settings it has intranet problems with the development view.

[node.js]: https://nodejs.org/en/download/
[sbt]: http://www.scala-sbt.org/download.html


### Steps to change the scala code

- Fork the project and make a local clone.
- Don't push to the master branch, create branches and pull requests.
- Go to the root of the local project and start the command `sbt '~fastOptJS'`, with `~` compilation is restarted as soon as a source is saved, without the compilation is executed once.
- Monitor the result of your changes on `http://localhost:12345/target/scala-2.11/classes/index-dev.html`
  It is a dressed down version of the published page, with possibly experimental features added.
  Nodes and links invisible in the published page are shown faint for debugging purposes.
- Saving code changes updates the page automatically but the animation does not start.


Important code conventions
--------------------------

Never catch exceptions in a `Try` as exceptions terminate the JavaScript. The tests might succeed with maven, but the JavaScript breaks. Prevent exceptions like illegal arguments and indexes and create a `Failure` for safe execution with JavaScript.

The applied Scala coding techniques are explained by this [course] up and including workshop 3. The main code doesn't use any io, and the hand full of files written by test/demo classes don't justify using a library. So you can save the last task of the FileIO assignment for other purposes.

[course]: https://github.com/DANS-KNAW/course-scala


Unit tests
----------

Both `sbt test` and `mvn clean test` do execute the tests.
Maven is much faster but uses JVM while the JS used by SBT is the actual target environment.
Some classes under `src/test` are suffixed with `Demos` rather than `Spec` these runnable objects create SVG documents in a `target/test` directory for a visual check.


Publish
-------

- Compile with `sbt '~fullOptJS'` (drop the quotes on windows)
- Copy the content of `target\scala-2.11\groundforge-opt.js` into `docs/js/matrix-graphs.js`
- Check the results with `index.html`
- If ok: commit, push and create a pull request 
- Optional (if you know what you are doing): after merging with your master branch you can check the online demo in your own github fork: `http://YOURID.github.io/GroundForge/` 
