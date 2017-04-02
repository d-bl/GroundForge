The image below shows an example of a pattern and its stitches.
When hovering with your mouse over a stitch in a generated diagram,
your browser is supposed to show the ID and instructions of a stitch.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/stitch-ids.png)

* _Top left:_ the definition of a pattern. You can choose one from thumbnails, or [assemble] something else.
  The rows and columns determine the ID's assigned to stitches.
  The digits inside the circles determine the configuration of incoming arrows at that position. 
* _Top right:_ how the pattern definition looks like when repeated
* _Bottom:_ the diagrams generated with the chosen stitches


If the first stitch definition is not prefixed with an ID,
it's used as default for not mentioned stitches.
No spaces allowed around the equal sign between the ID and instruction of a stitch definition.
Any sequence of punctuation characters separates the stitch definitions.
A stitch instruction contains the characters `ctlr` for
cross, twist, left twist or right twist.
You can try a `p` for a pin, but that rarely works as desired,
see issue [51](https://github.com/d-bl/GroundForge/issues/51) for the technical details.

[assemble]: Reversed-engineering-of-patterns.md
[51]: https://github.com/d-bl/GroundForge/issues/51