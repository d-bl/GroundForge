# [DEMO](https://d-bl.github.io/GroundForge/)
A toolbox to design bobbin lace grounds with matching diagrams.

The code under `src/main/scala` generates  alternative data for `sample.js` in the initial [gh-pages branch].


## Compile and preview

Requirements

- [sbt] 0.13.7 or higher
- a browser with proper SVG support, for example FireFox or Safari but not Internet Explorer.
  Chrome has proper SVG support but with the default settings it has intranet problems with the development view.


Steps

- Make a local clone of the project, it can be convenient to have two local clones,
  one for the master branch, one for the gh-pages branch.
- Go to the directory of your local clone with the master branche
- Start the command `sbt ~fastOptJS`
- Monitor the result on `http://localhost:12345/target/scala-2.11/classes/index-dev.html`
  It is a dressed down version of the demo page, with experimental features added.
  Nodes and links invisible in the demo page are shown faint for debugging purposes.
- Saving code changes updates the page automatically but not completely properly.


## Important code conventions

- Don't catch exceptions in a `Try` but prevent them to create a `Failure` for safe execution with JavaScript.
- Restrict the use of raw js objects to the API: the classes and methods annotated with `@JSExport`. This allows execution of test classes with another JVM than ScalaJS was built with.

The applied Scala coding techniques are explained by this [course] up and including workshop 3, except that the main code doesn't use any io, and the simple io in the test code doesn't justify the use of any library. 

[course]: https://github.com/DANS-KNAW/course-scala


## Unit tests

The command `sbt test` only compiles the test classes.
Haven't found the proper incantation to execute the test with sbt.

The command `mvn clean test` executes the tests, your IDE might too.
To completely [replace sbt] seems quite a detour.

Launching tests with the IDE while sbt is still processing a change may cause weird errors, just try again.

[replace sbt]: http://stackoverflow.com/questions/26512750/how-to-use-scala-js-from-maven


## Publish

- Compile with `sbt ~fullOptJS`
- Copy the content of `target\scala-2.11\groundforge-opt.js` in the master branch
  into `matrix-graphs.js` in the [gh-pages branch]
- Check the results with `index.html`
- If ok: commit, push and check the online demo in your own github fork: `http://YOURID.github.io/GroundForge/`
- Create a pull request

[sbt]: http://www.scala-sbt.org/download.html
[gh-pages branch]: https://github.com/d-bl/GroundForge/commit/84eee36324e448bf16c12dec08b55bf4814bedb0
