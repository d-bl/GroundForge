The [main page] of the web application gives you a quick start with valid configurations for some of the available patterns. The [advanced page] allows more patterns but due to a limited algorithm to create foot sides, not all configurations will produce a thread diagram. Pages in the side bar explain how to discover valid configurations and how to browse through available patterns.

[advanced page]: https://github.com/d-bl/advanced.html
[main page]: https://github.com/d-bl/
[TesseLace.com]: https://TesseLace.com
[issue #51]: https://github.com/d-bl/GroundForge/issues/51

The matrices should have lines of equal length, otherwise the diagram will show just a single dot with a tool tip complaining about the varying length. Characters other than digits and dashes are ignored and thus may cause the same result if mistaken for a digit. Not every sequence of digits and dashes result in proper lace patterns. For example a zero, one, two or three should not be followed by a three, six, eight or nine. Below a dash you can't have a one, four seven or eight. More rules should take care always two arrows are leaving each digit. Breaking these rules will result in diagrams with internal loose ends or other anomalies. The research project show cased at [Tesselace.com] is about algorithms that find valid matrices, even the advanced matrices are just a selection.

The matrices can be stacked as a brick wall or as a checkerboard:

     ___ ___ ___      ___ ___ ___ 
    |___|___|___|    |___|___|___|
    __|___|___|__    |___|___|___|
    |___|___|___|    |___|___|___|

Hidden components may enlighten the current problems with pins, see also [issue #51].

Purpose of the buttons:

* `Show` (re)generates both diagrams without requiring web access.
* `Reset` just resets the values in the form without page reload nor regenerating the diagrams.
* `Default` requires web access. It shortens the URL by stripping the part after the question mark an generates the default diagrams.
* `Submit` requires web access and regenerates both diagrams. It creates new values after the question mark in the URL and reloads the page from the web. The new URL allow to create a bookmark or favorite with the chosen configuration.