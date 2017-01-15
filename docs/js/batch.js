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
/************************************************************************************************
 * Usage
 *
 * requirements:
 * -  install node.js
 * -  execute on the path of this script:
 *      nmp install jsdom
 *
 * node
 * > require("./???/docs/js/batch.svg")
 * > data1 = dibl.D3Data().get("5- -5",rows=5,cols=5,left=0,up=1,"ctct","bricks")
 * > data2 = createSVG("./???.svg", data1, "cross=ct twist=ctc;ctct","#000,#000,#f00,#f00",1)
 * > .exit
 ************************************************************************************************/

// from docs/js
require("./matrix-graphs.js")  // loads dibl.D3Data() compiled from scala code
require("./show-graph.js")     // loads diagram which feeds the dibl-data to D3js
d3 = require("./d3.v4.min.js") // loads third party modules
// from node_modules
fs = require("fs")

document = require("jsdom").jsdom()
navigator = {}

function createSVG (svgFile, data, stitches, colors, countDown) {
  if (stitches.trim().length > 0) stitches.split(";").forEach(function(s){
    console.log("applying " + s + " to " + data.threadNodes().length + " nodes")
    data = dibl.D3Data().get(s, data)
  })
  console.log("created " + data.threadNodes().length + " nodes")
  console.log("countdown until file gets saved: " + countDown)
  document.body.innerHTML = ""
  diagram.svgFile = svgFile
  diagram.showGraph({
    container: document.body,
    nodes: data.threadNodes(),
    links: data.threadLinks(),
    viewWidth: 744,
    viewHeight: 1052,
    palette: colors,
    onAnimationEnd: function() {
      if (--countDown > 0) {
          console.log("countdown " + countDown)
          diagram.sim.alpha(0.005).restart()
      } else fs.writeFile(diagram.svgFile, document.body.innerHTML, function(err) {
        if(err) return console.log(err)
        else console.log(svgFile + " was saved")
      })
    }
  })
  return data
}
