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
function load() {
  var q = window.location.search.substr(1)
  var width = 350
  var height = 500
  var zoom = 1.6
  var svg = PairSvg.render(TilesConfig(q), width,height, zoom)
  d3.select('#initial').html(svg)
  d3.select('#animated').html(svg)
  nudgePairs(d3.select('#animated'), 350/zoom, height/zoom)
}
