Stitch specifications
---------------------

A text field takes the definition of stitches to use. When hovering with your mouse over a stitch in the pair diagram, it shows you an ID of a stitch like `A1`. Assign stitches to theses ID's with `c` , `t`, `l`, `r` for cross, twist, left-twist and right-twist. A stitch requires at least a cross, violating this requirement would implicitly change the pair diagram.

If you drop the ID for the first stitch definition, it is used as default for not mentioned ID's in the pattern. When using thread diagrams as pair diagrams, you can also use `cross` and `twist` as defaults.

No spaces are allowed around the equal sign between the ID and instruction of a stitch definition.
Any sequence of punctuation characters separates the stitch definitions.
You can try a `p` for a pin in a stitch definition, but that rarely works as desired,
see issue [51](https://github.com/d-bl/GroundForge/issues/51) for the technical details.

Buttons and Browser History
---------------------------

The show buttons doesn't store your choice of stitches (nor thread colors, nor manual changes to the parameters form) in your browser history.

When the cursor is in a stitch field you can return to a previous choice for that field with CTRL-Z (Windows, *nix systems) or COMMAND-Z (Mac), FireFox and Chrome also let you choose from a list of previous values with the same start, erase the field and the down or up arrow of your keyboard shows all your previous choices.

Right-click the ![](https://d-bl.github.io/GroundForge/images/link.png) button button to save the complete set of choices (except for the thread colors) as bookmark, favorite or however your browser calls it. When recalling the bookmark you will have to generate the diagrams again with the show button. Just clicking the button saves the set of choices in the browser history allowing you to return with the back button of your browser, you will have to regenerate the diagrams. Your browser will automatically remember the choices you made on the gallery page or the linked stitches examples you choose. The browser history gets lost when you close the browser, you bookmarks or favorites will stay.

An Example
----------

The image below shows screen shot snippets of a pattern and its stitches.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/stitch-ids.png)

* _Top left:_ the definition of a pattern. You can choose one from thumbnails, or [assemble] something else.
  The digits inside the circles determine the configuration of arrows arriving at that position. 
  The rows and columns determine the ID's assigned to stitches.
  For example the top-left (5) gets ID A1. Well, that's the theory.
  Under circumstance the ID's in a row of stitches may start somewhere halfway the matrix
  and the stitches wander a little away from their matrix position.
* _Top right:_ how the pattern definition looks like when repeated. The colored dots are explained on the "reshape" pages.
* _Bottom:_ the diagrams generated with the chosen stitches.
  When hovering with your mouse over a stitch (in a generated pattern, not the screenshot above), your browser is supposed to show the ID and instructions of a stitch.

[assemble]: https://github.com/d-bl/GroundForge/wiki/Reversed-engineering-of-patterns
[51]: https://github.com/d-bl/GroundForge/issues/51