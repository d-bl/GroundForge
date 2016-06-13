The prominent thumbnails on the [main page] of the web application gives you a quick start with valid configurations for some of the available patterns. Other patterns require more tweaking of advanced options to produce a complete thread diagram. This problem is caused by a limited algorithm to create proper foot sides. Only specific combinations of shift-values and number of columns will produce complete thread diagrams.

[main page]: https://github.com/d-bl/
[TesseLace.com]: https://TesseLace.com
[issue #51]: https://github.com/d-bl/GroundForge/issues/51

# Matrices

The legend for the matrix option explains how a diagram is constructed from a few short alpha-numeric lines.
The research project show cased at [Tesselace.com] is about algorithms that find valid matrices, the thumbnails cover matrices published in 2012 and 2014.

The matrices should have lines of equal length, otherwise the diagram will show just a single dot with a tool tip complaining about the varying length. Characters other than dashes, digits an letters in the legend are ignored and thus may cause the same result. Not every sequence of digits and dashes result in proper lace patterns. For example a zero, one, two or three should not be followed by a three, six, eight or nine. Below a dash you can't have a one, four seven or eight. More rules should take care always two arrows are leaving each digit. Breaking these rules will result in diagrams with internal loose ends or other anomalies. 

The matrices can be stacked as a brick wall or as a checkerboard:

     ___ ___ ___      ___ ___ ___ 
    |___|___|___|    |___|___|___|
    __|___|___|__    |___|___|___|
    |___|___|___|    |___|___|___|

# Hidden diagram components

Hidden components may enlighten the current problems with pins, see also [issue #51].

# Buttons

* `Show` (re)generates both diagrams without requiring web access. The pattern sheet is not affected.
* `Default` requires web access. It shortens the URL by stripping the part after the question mark an generates the default diagrams.
* `Submit` requires web access and regenerates both diagrams. It creates new values after the question mark in the URL and reloads the page from the web. The new URL allow to create a bookmark or favorite with the chosen configuration.