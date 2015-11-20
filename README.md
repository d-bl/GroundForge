[DEMO](http://jo-pol.github.io/DiBL/tensioned/)

The code under `src/main/scala` generates  alternative data for `sample.js` in the [gh-pages branch].
The patterns are generated from predefined [matrices].

It is convenient for a developer to have two local clones of the project,
one for the master branch, one for the gh-pages branch.

# Compile and preview

requirements:
- [sbt] 0.13.7 or higher
- a browser with proper SVG support, not IE

steps:
- Make a local clone of the project
- Go to `web/tensioned` inside the clone
- Start the command `sbt ~fastOptJS`
- Monitor the result on `http://localhost:12345/target/scala-2.11/classes/index-dev.html`
  It is a dressed down version of the demo page, nodes and links invisible in the demo page
  are shown faint for debugging purposes.
- Saving code changes updates the page automatically

# Unit tests

The command `sbt test` only compiles the test classes.
Haven't found the proper incantation to execute them with sbt.

With the project imported in Intellij it is possible to execute the test classes one by one.
Launching tests causes problems when sbt is still processing a change, just try again.

# Publish

- Compile with `sbt ~fullOptJS`
- Copy the content of `target\scala-2.11\dibl-tensioned-opt.js` in the master branch
  into `tensioned/matrix-graphs.js` in the [gh-pages branch]
- Check the results with `tensioned/index.html`
- If ok: commit, push and check the online demo in your own github fork: `http://YOURID.github.io/DiBL/tensioned/`

[bug]: https://connect.microsoft.com/IE/feedback/details/801938/dynamically-updated-svg-path-with-a-marker-end-does-not-update
[sbt]: http://www.scala-sbt.org/download.html
[gh-pages branch]: https://github.com/jo-pol/DiBL/tree/gh-pages/tensioned
[matrices]: https://github.com/jo-pol/DiBL/blob/17a80e930df3540c95a5cb1bf68dec4f094ce202/web/tensioned/src/main/scala/dibl/Matrix.scala#L158-L167
