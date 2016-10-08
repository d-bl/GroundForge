Two minified versions of `show-graph.js`, both receiving tha same data.
 
Purpose: compare the force configurations between two D3js versions:
[v3](https://github.com/d-bl/GroundForge/blob/master/docs/D3jsForces/v3/show-graph.js#L35-43), 
[v4](https://github.com/d-bl/GroundForge/blob/master/docs/D3jsForces/v4/show-graph.js#L35-41)
  
The initial alpha value is set to the minimal value for v3 and for v4 to a value resulting in about the same number of ticks. The top row of nodes starting in the top left corner causes problems for v4 and v4 also doesn't adjust reduce the size.

Demos: 
[v3](https://github.com/d-bl/GroundForge/D3jsForces/v3),
[v4](https://github.com/d-bl/GroundForge/D3jsForces/v4)