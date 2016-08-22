[DEMO](https://d-bl.github.io/GroundForge/move-nodes)

Dragging a single node is possible.

Most nodes in the diagram have the following two classes: "node" and one of "A1"..."D4".
The latter classes are also titles and thus shown as tooltips by some browsers.

# Proof of concept to achieve
Move nodes with class "A1" (or anything up to "D4") by dragging just one of them.

# TODO
* the function `dragended` in `graphs.js` needs to call some variant of `moveNode` that changes the values of `x` and `y` with the amount that the node has been dragged
* exclude the dragged node from the selection
* perhaps move the code somehow from dragended to dragged for a better user experience
* using D3.force to manually drag nodes around feels like a detour