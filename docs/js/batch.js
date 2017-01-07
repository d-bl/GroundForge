/*
 Copyright 2017 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
/******************************************************************
 install:      node.js
 execute:      nmp install jsdom
 dos terminal: NODE_PATH=C:\???\docs\js;/C:\???\node_modules
 shell:        export NODE_PATH=/???/docs/js;/???/node_modules

 execute:      node -e 'require("batch.js");svgFile="tmp.svg"'
*******************************************************************/

// from GroundForge/docs/js
require("matrix-graphs.js")
require("show-graph.js")
d3 = require("d3.v4.min.js")
// from node_modules
fs = require("fs")
document = require("jsdom").jsdom()

navigator = {}
function createSVG (colors) {
  diagram.showGraph({
    container: document.body,
    nodes: finalStep.threadNodes(),
    links: finalStep.threadLinks(),
    viewWidth: 744,
    viewHeight: 1052,
    palette: colors,
    onAnimationEnd: function() {
      fs.writeFile(svgFile, document.body.innerHTML, function(err) {
        if(err) return console.log(err)
        else console.log(svgFile + " was saved!")
      })
    }
  })
}

// initial parameters
var matrix = "5-"
var tiling = "bricks"
var nrOfRows = 9
var nrOfCols = 9
var shiftLeft = 0
var shiftUp = 1

// calculate the patterns step by step
var step1 = dibl.D3Data().get(matrix, nrOfRows, nrOfCols, shiftLeft, shiftUp, "ctct", tiling)
var finalStep = dibl.D3Data().get("ctct", step1)
createSVG("#000,#000,#F00,#F00")
