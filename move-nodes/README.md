[DEMO](https://d-bl.github.io/GroundForge/move-nodes)

Dragging a single node is possible.

Most nodes in the diagram have the following two classes: "node" and one of "A1"..."D4".
The latter classes are also titles and thus shown as tooltips by some browsers.

# Proof of concept to achieve
Move nodes with class "A1" (or anything up to "D4") by dragging just one of them.

# TODO
* [x] The function `dragended` in `graphs.js` needs to call some variant of `moveNode` that changes the values of `x` and `y` with the amount that the node has been dragged.
* [x] Exclude the dragged node from the selection.
* [ ] Move some of the code from dragended to dragged for a better user experience.
* [ ] Trim the thread starts.
* [ ] Using D3.force to manually drag nodes around feels like a detour.
* [ ] Try connectors in InkScape, do they move along with nodes? That would be a more flexible solution. Then the plugin would need to select all nodes with the same class. The website could even add semi-transparent colors to the nodes, for example more red from A1 to A4 and more blue from A1 to D1.