- [Form fields](Form-fields)
- [Stitch definitions](#stitch-definitions)
- [Assign stitches](#assign-stitches)
- [An Example](#an-example)
- [Buttons and Browser History](#buttons-and-browser-history)

<sub><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></sub>


Form fields
===========

Two methods are used to specify stitches.

* The [droste] page has one field per stitch.
  The position of the fields in the form reflects the position in the pattern.
  A cheat-sheet provides a visual representation for some less trivial stitches.
* The [main] page has one text field to define all stitches of a pattern.
  Hover with the cursor over stitches in the pair diagram
  and your browser should show a tool tip with the id
  for a position in the pattern and the current stitch assigned to the id.


Stitch definitions
==================

The definition of a stitch consists of a sequence  of the characters
`c`, `t`, `l`, `r` for cross, twist, left-twist and right-twist. 
A stitch requires at least a cross, violating this requirement would implicitly change the pair diagram.
Invalid characters are ignored.

You can try a `p` for a pin, but that rarely works as desired,
see issue [#51] for the technical details.



Assign stitches
===============

The form fields for stitches on the main page expect defaults and/or assignments. For example `A1=ctc` assigns a plain cloth stitch to id A1 which represents the upper left stitch of the pattern.
No spaces are allowed around the equal sign between the id and stitch definition.

If a form field starts with a stitch definition, it is used as default.
When a thread diagrams is used as pair diagram, you can use the keywords `cross` and `twist` as id's for defaults.


An Example
==========

The image below shows screen shot snippets of a pattern and its stitches.

![](https://raw.githubusercontent.com/wiki/d-bl/GroundForge/images/stitch-ids.png)

* _Top left:_ the definition of a pattern created by one of the example page.
  The digits inside the circles determine the configuration of arrows arriving at that position. 
  The rows and columns determine the id's assigned to stitches.
  For example the top-left (5) gets id A1. Well, that's the theory.
  Under circumstance the id's in a row of stitches may start somewhere halfway the matrix
  and the stitches wander a little away from their matrix position,
  see issue [#96].
* _Top right:_ how the pattern definition looks like when repeated. The colored dots are explained on the "reshape" pages.
* _Bottom:_ the diagrams generated with the chosen stitches.
  When hovering with your mouse over a stitch (in a generated pattern, not the screenshot above), your browser is supposed to show the id and instructions of a stitch.

[droste]: https://d-bl.github.io/GroundForge/droste.html
[main]: https://d-bl.github.io/GroundForge/
[assemble]: https://github.com/d-bl/GroundForge/wiki/Reversed-engineering-of-patterns
[#51]: https://github.com/d-bl/GroundForge/issues/51
[#96]: https://github.com/d-bl/GroundForge/issues/96

Buttons and Browser History
===========================

The show buttons doesn't store your choice of stitches (nor thread colors, nor manual changes to the parameters form) in your browser history.

When the cursor is in a stitch field you can return to a previous choice for that field with CTRL-Z (Windows, *nix systems) or COMMAND-Z (Mac), FireFox and Chrome also let you choose from a list of previous values with the same start, erase the field and the down or up arrow of your keyboard shows all your previous choices.

Right-click the ![](https://d-bl.github.io/GroundForge/images/link.png) button button to save the complete set of choices (except for the thread colors) as bookmark, favorite or however your browser calls it. When recalling the bookmark you will have to generate the diagrams again with the show button. Just clicking the button saves the set of choices in the browser history allowing you to return with the back button of your browser, you will have to regenerate the diagrams. Your browser will automatically remember the choices you made on an example page or the linked stitches examples you choose. The browser history gets lost when you close the browser, you bookmarks or favorites will stay.
