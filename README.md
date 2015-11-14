[DEMO](http://jo-pol.github.io/DiBL/tensioned/)

# How it's made

Sep by step changed an [example] for [D3.js]:

- replaced the server side JSon with the hard-coded `sample.js`
- applied arrow heads reduced to colored line ends to emulate [color coded] diagrams
- made nodes transparent except for bobbins
- added properties to identify the links of a single thread
- turned the links from lines to paths to add text: pipe symbols serve as twist marks
- [scala code] compiled into a `matrix-graphs.js` generates alternatives for the `sample.js`

[example]: http://bl.ocks.org/mbostock/4062045
[D3.js]: http://d3js.org/
[color coded]: https://en.wikipedia.org/w/index.php?title=Mesh_grounded_bobbin_lace&oldid=639789191#Worker_pair_versus_two_pair_per_pin
[scala code]: https://github.com/jo-pol/DiBL/tree/master/web/tensioned/