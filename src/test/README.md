...Spec.scala
=============
These classes are automatically executed with the maven build.
A travis job publicly shows failures:
with a banner on the project-readme or
when pushing commits push to the branch of a pull request.

Demo classes
============
Many problems are hard to test automatically.
Visual inspection of batches of diagrams can help.
The patterns are generated in `target/test/`<class-name>

Demo4Java.java
--------------
Generates all diagrams for some patterns that needed some debug attention in the past.
The method `writeNudgedDiagram` prepares for to be developed
flat torus embedding as replacement for the force graphs that distort towards edges.

Most ...Demos.scala
--------------
Generate one type of diagram for all patterns
found on the `Tesselace-Index` and `Whiting-index` pages.
The tesselace patterns are grouped by identical topology,
file names `NNN-n.svg`. Whiting file names `Xnn_Pnnn.svg`.
No force-graphs applied to the pair diagrams nor thread diagrams.


TesselaceThumbs.scala
=====================
Another batch program that generates pair diagrams with force graphs applied.
Not intended for testing but to update the thumbnails on the `Tesselace-index` page.
Slow because it depends on a javascript engine.
**Warning**: This engine runs in a thread that does not terminate without manual intervention.
