Bloopers
========

To err is human, for a real mess you need a computer.

1
-

![busy bobbins](busy.gif)

[docs/show-graph.js]: https://github.com/d-bl/GroundForge/blob/52459fe36c69cf9dcf869148c8321577e6f3dd1d/docs/js/show-graph.js#L178-L180
[Download](https://github.com/d-bl/GroundForge/archive/52459fe36c69cf9dcf869148c8321577e6f3dd1d.zip),
extend the forces in [docs/show-graph.js] with something like

    .force("collide", d3.forceCollide(10).strength(10).iterations(30))
 
 and try `docs/index.html`.

2
-

![tipped over pillow](tipped-over.png)

Same [download](https://github.com/d-bl/GroundForge/archive/52459fe36c69cf9dcf869148c8321577e6f3dd1d.zip),
try repeated twists for the first thread diagram in the page `docs/recursive.html` then show the second thread diagram.
