---
layout: default
title: Choose Stitches
javascript: /GroundForge/js/stitches.js
---

Choose Stitches
===============

- [Stitch definitions](#stitch-definitions)
- [Assign stitches](#assign-stitches)
- [An Example](#an-example)
- [ID's](#ids)

Subsequent paragraphs explain how to set a stitches field manually
for patterns and dimensions not covered by this convenience form.

{% include stitch-form.html %}
<script>setVisibility()</script>

**Pattern properties:**
{% include gallery.html src="help/stitches/diagonal.png" caption="diagonal" %}
{% include gallery.html src="help/stitches/weaving.png" caption="weaving" %}
{% include gallery.html src="help/stitches/bricks.png" caption="bricks" %}
{% include gallery.html src="help/stitches/checker.png" caption="checker" %}

The position of a field in the form reflects the position of a stitch
within one brick or checkerboard field of the pattern.

**Stitches to copy-paste:**
{% include gallery.html src="help/stitches/crclct.png" caption="crclct<br>&nbsp;" %}
{% include gallery.html src="help/stitches/clcrclc.png" caption="clcrclc<br>crclcrc" %}
{% include gallery.html src="help/stitches/ctctc.png" caption="ctctc<br>&nbsp;" %}
{% include gallery.html src="help/stitches/ctclctc.png" caption="ctclctc<br>ctcrctc" %}
{% include gallery.html src="help/stitches/ctclcrctc.png" caption="ctclcrctc<br>ctcrclctc" %}
{% include gallery.html src="help/stitches/ctcttctc.png" caption="ctcttctc<br>&nbsp;" %}
{% include gallery.html src="help/stitches/tctctllctctr.png" caption="tctctllctctr<br>tctctrrctctl" %}

Only less trivial stitches are listed, drop or add twists at will.
A second string is the mirrored version of the stitch.
The last one is an edge stitch without a pin.


Stitch definitions
------------------

The definition of a stitch consists of a sequence  of the characters
`c`, `t`, `l`, `r` for cross, twist, left-twist and right-twist. 
A stitch requires at least a cross,
violating this requirement would implicitly change the pair diagram.
Invalid characters are ignored.

You can try a `p` for a pin, but that rarely works as desired,
see issue [#51] for the technical details.


Assign stitches
---------------

The form fields for stitches on the main page expect defaults and/or assignments.
For example `A1=B2=ct` assigns a closed half stitch to id A1 and B2
which represents the upper left stitch of the pattern and the stitch south-east of A1.
No spaces are allowed around the equal sign between the id and stitch definition.

If a form field starts with a stitch definition, it is used as default.
When a thread diagrams is used as pair diagram,
you can use the keywords `cross` and `twist` as id's for defaults.


An Example
----------

The image below shows screen shot snippets of a pattern and its stitches.

![](/GroundForge/help/images/stitch-ids.png)

* _Top left:_ the definition of a pattern created by one of the example page.
  The digits inside the circles determine the configuration of arrows arriving at that position. 
* _Top right:_ how the pattern definition looks like when repeated. The colored dots are explained on the "reshape" pages.
* _Bottom:_ the diagrams generated with the chosen stitches.
  When hovering with your mouse over a stitch (in a generated pattern, not the screenshot above), your browser is supposed to show the id and instructions of a stitch.

[main]: /GroundForge/
[assemble]: /GroundForge/help/Reversed-engineering-of-patterns
[#51]: https://github.com/d-bl/GroundForge/issues/51
[#96]: https://github.com/d-bl/GroundForge/issues/96

Id's
----

The rows and columns determine the id's assigned to stitches.
Well, that's the theory.
Under circumstances the id's in a row of stitches may be off by one column,
see issue [#96]. As for example with the following brick matrix:

` ` | ` ` | => | ` ` | ` ` 
----|-----|----|-----|----
`-` | `5` |    | A1  | B1
`5` | `-` |    | A2  | B2
`-` | `5` |    | B3  | A3

Also the distortion between matrix and pair diagram may be confusing
as the influence of footside.
So hover with your mouse over a stitch in the pair diagram to be sure.
