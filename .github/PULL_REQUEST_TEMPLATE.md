When applied it will
--------------------

Open checkboxes indicate a preview that is not yet ready for merge.

* ...
* [ ] ...


How should this be manually tested?
-----------------------------------

* Run `sbt fullOptJS` from the root of the project
* There should be no differences between `target/scala-2.11/groundforge-opt.js` and `docs/js/matrix-graphs.js`, otherwise paste the first into the latter.
* Explore the pages `docs/*.html` and `docs/*.md`.
* Run the demo classes between the tests and examine the produced `target/*.svg` files.
* ...
