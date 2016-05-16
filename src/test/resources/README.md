# Topological exploration of patterns

The bridges page explores the interleaved patterns of https://tesselace.com/research/bridges2012/

The 4x4 pages explore patterns of the [inkscape plugin] some of the checker board matrices happen to be valid when stacked as a brick wall.

## Rearrange the thumbnails

* The matrix strings are added as id's on the SVG elements.
* Use the inspector of for example FireFox or Chrome to remove the JavaScript at the bottom of the DOM
* Drag the `<svg>` elements inside the inspector into other positions. Don't know were you dropped the last? Find the selected element, hover with the mouse over it and the thumbnail gets highlighted.
* Now and then save the work by copy-pasting the outerHTML of the `<html>` element.

## Create individual thumbnails

* Let the animation of the html page complete.
* Use the inspector of the browser to copy all the SVG objects into a file, say: thumbs.txt
* Split the one liner file into multiple lines at each  the smallest ones may not be valid.
* Execute the following commands and figure out how to assign the SVG id to the png property 'title' or change the file name.

    cat thumbs.txt | awk '/<svg id/ {n++}{print > "thumb-" n ".svg"}'
    for i in thumb-*.svg; do '/C/Program Files/Inkscape/inkscape.exe' $i --export-png=`echo $i | sed -e 's/svg$/png/'`; done

[inkscape plugin]: https://github.com/d-bl/inkscape-bobbinlace/tree/master/input/lace_ground/checker
[Rearrange]: https://github.com/d-bl/inkscape-bobbinlace/issues/14