# Topological exploration of patterns

The bridges page explores the interleaved patterns of https://tesselace.com/research/bridges2012/

The 4x4 and misc pages explore patterns of the [inkscape plugin]. Some of the checker board matrices happen to be valid when stacked as a brick wall.

## Collect PNG thumbnails in a web page

* The web pages add the matrix strings as id's on the SVG elements. These id's are used to create links from the PNG thumbnails to the advanced page.
* Let the animation of the web page complete.
* Use the inspector of a browser like FireFox to copy all the SVG objects into a file, say: thumbs.txt. Copy the innerHTML of the div with id "diagrams" to get all the SVG objects at once.
* Make sure each `<svg>` element starts at a new line and the file(s) end with a new line.
* Execute the following commands, they:
  1 split the files
  2 convert SVG files to PNG files
  3 create a thumbnail page using the id's of the SVG root element to create links to the advanced page.

            cat thumbs.txt | awk '/<svg id/ {n++}{print > sprintf("%03d.svg", n)}'
            for i in *.svg; do '/C/Program Files/Inkscape/inkscape.exe' $i --export-png=`echo $i | sed -e 's/svg$/png/'`; done
            grep svg *.svg | sed -e 's!" .*!!' -e 's!\([0-9][0-9][0-9]\)[^"]*"\(checker\|brick\) \(.*\)!<a href="advanced.html?matrix=\3\&\2s=on"><img src="\1.png"></a>!' > tmp.html

* For as far as the thumbnails were not in the desired order in the original web pages(s): Use the inspector again to drag the `<svg>` elements inside the inspector into other positions. Don't know were you dropped the last one? Find the selected element, hover with the mouse over it and the thumbnail gets highlighted.

[inkscape plugin]: https://github.com/d-bl/inkscape-bobbinlace/tree/master/input/lace_ground/checker
[Rearrange]: https://github.com/d-bl/inkscape-bobbinlace/issues/14