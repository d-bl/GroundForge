for f in target/test/permutations/*.svg
do
  /Applications/Inkscape.app/Contents/MacOS/inkscape $f -o `echo $f|sed 's/.svg/.png/'` -w 200 -h 200 -D
done
