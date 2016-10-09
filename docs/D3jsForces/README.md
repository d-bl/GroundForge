Two minified versions of `show-graph.js`, both receiving tha same data to compare the force configurations between two D3js versions:
[v3](https://github.com/d-bl/GroundForge/blob/master/docs/D3jsForces/v3/show-graph.js#L36-L44), 
[v4](https://github.com/d-bl/GroundForge/blob/master/docs/D3jsForces/v4/show-graph.js#L36-L40)
  
The initial alpha value is set to the minimal value for v3 and for v4 to a value resulting in about the same number of ticks. The v4 version has problems with the top row of nodes starting in the top left corner and v4 also doesn't adjust the size.

First of all the shapes should become regular. Once that requirement is met, the nodes should get as close as possible, a little overlap as an exception in the corners might be acceptable.

Demos: 
[v3](https://d-bl.github.io/GroundForge/D3jsForces/v3),
[v4](https://d-bl.github.io/GroundForge/D3jsForces/v4),
the [dressed up](https://d-bl.github.io/GroundForge/)
version