[DEMO](http://jo-pol.github.io/DiBL/tensioned/)

The code under `src/main/scala` generates  alternative data for `sample.js` in the [gh-pages branch].
The patterns are generated from predefined [matrices].



# Compile and preview

Requirements

- [sbt] 0.13.7 or higher
- a browser with proper SVG support, for example FireFox, Chrome, Safari but not Internet Explorer.

Steps

- Make a local clone of the project, it can be convenient to have two local clones,
  one for the master branch, one for the gh-pages branch.
- Go to `web/tensioned` inside the clone.
- Start the command `sbt ~fastOptJS`
- Monitor the result on `http://localhost:12345/target/scala-2.11/classes/index-dev.html`
  It is a dressed down version of the demo page, with experimental features added.
  Nodes and links invisible in the demo page are shown faint for debugging purposes.
- Saving code changes updates the page automatically.

Important code conventions

- Don't catch exceptions in a `Try` but prevent them to create a `Failure` for safe execution as JavaScript.
- Restrict the use of raw js objects to the `D3Data` class to allow execution of test classes with a recent JVM.


# Unit tests

The command `sbt test` only compiles the test classes.
Haven't found the proper incantation to execute the test with sbt.

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
