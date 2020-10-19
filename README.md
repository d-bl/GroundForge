[![Build Status](https://travis-ci.org/d-bl/GroundForge.svg?branch=master)](https://travis-ci.org/d-bl/GroundForge) 
_A toolbox to design bobbin lace grounds with matching sets of pair/thread diagrams._

- [Demo's](#demo-s)
- [Contribute to documentation](#contribute-to-documentation)
- [Functional contribution](#functional-contribution)
  * [Requirements](#requirements)
  * [Work flow](#work-flow)
  * [Important code conventions](#important-code-conventions)
  * [Tests](#tests)
-  [Use as JavaScript library](#use-as-javascript-library)
-  [Use as JVM library](#use-as-jvm-library)


Short intro's
=============
For end users (bobbin lace makers and designers):  
https://d-bl.github.io/GroundForge/

For developers:
* `src/scala/main/*` is translated to : `docs/js/GroundForge-opt.js`
* This is connected client side to HTML with : `docs/js/tiles.js`
* The source run also in a JVM environment for server-side or batch processing,
  for example something like : `src/test/Demo4Java.java`


Demo's
======

A [dressed up](https://d-bl.github.io/GroundForge/tiles?patchWidth=12&patchHeight=12&a1=ct&b1=ct&c1=ctc&d1=ctc&b2=ctc&d2=ctc&a3=ct&c3=ct&footside=b,-,a,-&tile=831,4-7,-5-&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2) version and a dressed down [API](https://d-bl.github.io/GroundForge/API/) version.
The latter shows all possible diagrams with minimal code.
The first demonstrates interaction with these diagrams
implemented with limited styling and event handling skills. 
It evolved into from a proof of concept into the *de facto* user interface.


Contribute to documentation
===========================

GroundForge has various types of documentation.

In this repository
* Tutorials
* Pages referred to by info buttons for form fields.
  Originally intended as tooltips but evolved into too large explanations. 
  
Examples of patterns
* The other repositories listed below
* Third party blogs and whatever we might not know about

Most common tasks
-----------------
* Edit the `.md` files in the `docs` folder.
* Keep the TOC in the sidebar up to date.

There is a simple [procedure] to propose simple changes to the docs that build the sites.
Simple changes could be things like typo's, grammar or
simplified phrasing hoping that automated translators do a better job.

| repository | docs | site | sidebar |
|------------|------|------|----------|
| [GroundForge] | [xxx] | [yyy] | [zzz] |
| [gw-lace-to-gf] | ... | ... | ... |
| [tesselace-to-gf] | ... | ... | ... |
| [MAE-gf] | ... | ... | ... |

[procedure]: https://help.github.com/articles/editing-files-in-another-user-s-repository/
[xxx]: https://d-bl.github.io/GroundForge/tree/master/docs/help
[yyy]: https://d-bl.github.io/GroundForge/
[zzz]: https://github.com/d-bl/GroundForge/tree/master/docs/_includes/Sidebar.html
[GroundForge]: https://d-bl.github.io/GroundForge/
[gw-lace-to-gf]: https://d-bl.github.io/gw-lace-to-gf/
[tesselace-to-gf]: https://d-bl.github.io/tesselace-to-gf/
[MAE-gf]: https://maetempels.github.io/MAE-gf/

Conventions
-----------

### metadata

The mark-down pages start with a metadata section, something like

    ---
    layout: default
    title: XYZ
    ---

Browsers show `XYZ` as tab title. Keep it short and catchy.

### links
Thumbnails in catalogues are the biggest target and should point to the pattern definition.
If available, use a sample, otherwise the thread diagram.

Use root relative links for references between the repositories.

### Scalable prickings

Both PDF and SVG are scalable and can be imported by vector capable editors
such as InkScape, Adobe Illustrator and CorelDraw.
Knipling can export PDF. When you just want a section of some file
save a (temporary) copy of the pattern, delete the rest, then export the PDF.  


Preview your changes online
---------------------------
...  
 https://d-bl.github.io/GroundForge/help/Stable   
...  


Functional contribution
=======================

Requirements
------------

- The pages in the docs directory don't require any compilation
- To execute the tests: maven or an IDE (like IntelliJ community edition)
- To compile `src/main/scala/` into `docs/js/GroundForge-opt.js`:  
  [JDK] 8 and [sbt] 1.2.7 or higher
- To create a jar: [JDK] and maven

[JDK]: https://adoptopenjdk.net/releases.html
[sbt]: https://www.scala-sbt.org/1.x/docs/Setup.html

Work flow
---------

### When contributing to the code (Scala, JavaScript, html pages)
- Fork the project and make a local clone.
- Don't push to your own master branch, but use the following work flow
  - add the parent of your fork as remote to your local repository, by our conventionsk this remote is called blessed
  - fetch the master branch of the blessed repository
  - create a topic branch from the tip of the master branch
  - push your changes to your own fork and create a pull request
- Compile your changes and copy the result from the root of the local project to `/docs/js`.
  Depending in your OS use the one liner in `toJS.sh` or `toJS.bat` the latter is not battle proven.
- Check the results with the `docs/*.html` pages
- If ok (or need advise from a reviewer): commit, push and create a pull request

### When contributing to documentation
- Optional (if you know how to meddle with branches as your own master branch
  will get another history than the parent repository):
  - [configure] your local fork to deploy `docs` at `http://YOURID.github.io/GroundForge/` 
  - after merging with your own master branch you can check your own version of the site.

[configure]: https://help.github.com/articles/configuring-a-publishing-source-for-github-pages/#publishing-your-github-pages-site-from-a-docs-folder-on-your-master-branch


Code conventions
----------------

* Never catch exceptions in a `Try` as exceptions terminate the JavaScript.
  The tests might succeed with maven, but the JavaScript breaks.
  Prevent exceptions like illegal arguments and indexes and return a `Failure`
  for safe execution with JavaScript.
* Exchange of data between Scala code and JavaScript can be complicated.  
  It is no problem for JavaScript store complex scala types,
  as long as it only needs to pass it on to other scala code and not has to process the data.
  Otherwise, primitive types are the primary choice. Diagram definitions are serialized as a URL query.
  More or less like forms submitted to a server, for human readability no escaping is applied.
* It was a terrible mistake to implement the `LinkProps` and `NodeProps` as a map.
  Caused by the initial proof of concept starting with plain JavaScript by modifying D3js examples.
  So far no success in phasing out this heritage. 
  However, these maps are hidden from the world outside these two classes as much as possible. 
* The initially applied Scala coding techniques are explained by this [course] up and including workshop 3.
  The main code doesn't use any io, and the hand full of files written by test/demo classes don't justify using a library.
  So you can skip the last task of the FileIO assignment.

[course]: https://github.com/DANS-KNAW/course-scala


Tests
-----

Use `mvn clean test` to run unit tests. As some tests read pattern links from help files using java-io, the test fail with SBT.
Maven is much faster but uses JVM while the JS used by SBT is the actual target environment.
Some classes under `src/test` are suffixed with `Demos` rather than `Spec` these runnable objects create SVG documents in a `target/test` directory for a visual check.

Use as JavaScript Library
=========================

The demo's mentioned above use the scala code as a JavaScript library
as explained under short intro for developers.
Other mash ups and more user-friendly applications could be wrapped around the library.

For manual tests with other browsers and devices than your own, you could sign-up at
<br><a href="http://browserstack.com/"><img src="https://p14.zdusercontent.com/attachment/1015988/2pBNLzsRzHKyVmXhbPYFfcqi2?token=eyJhbGciOiJkaXIiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0..u4MOjDBdY7uyB7AqmHgHyw.OG_ZUr4mjWRjpV4IE5UH_bEtx-L-4NHCjNVSBjFvNP9X9ugBGhbEmHXVTJlpI-UBmAFBTl2SVYLgE4G474L0Hu37sYTtC5G3ehtEdiUYPn2R-MfM9cxUCJVP_T1PYk9_kZowoF2wSPFvaWphfvO9bk-hykkhDfPeFQ2BHsJlTlHbpNq8Icd4sveUMnJl0zFiy-h3kGo0ImQLRZnNsmEa3qx7JTINhL-bAUpGQKmdpvWAFVhtUIz8ZkntxRnuNi5EtXD1P4tucKH8kSt5gJXnSU_O0M0Ka_pTJgVXpEQMvTs.it94EtvuwAOOEjIRwQ7z1w" width="120" height="63"></a>

Use as JVM library
==================

A JVM library allows server-side or batch processing.

The [Demo4Java.java](https://github.com/d-bl/GroundForge/blob/119-layout/src/test/scala/dibl/Demo4Java.java)
between the test classes is a simple plain java main class example that generates diagrams.
This example uses the code in `src/main` as a Java library. 

In a plain [JVM](https://www.w3schools.com/java/java_getstarted.asp)
environment, you'll need at least on your `classpath`: 
* The `.jar` from the [last release](https://github.com/d-bl/GroundForge/releases)
  or a self-built one.
* The jar at the repository URL on [scalajs-library](https://maven-repository.com/artifact/org.scala-js/scalajs-library_2.12/0.6.26)
  in the central maven repository. For the actual version, follow the tag of the release
  and find the dependency in the `pom.xml`

For a maven/scala-sdk-2.12 environment:
* [download](https://github.com/d-bl/GroundForge/) or checkout the tip of the master branch, or any other branch or commit you prefer 
* execute `maven clean install` in the unzipped directory
* add to the `pom.xml` of your own project
```
        <dependency>
            <groupId>io.github.d-bl</groupId>
            <artifactId>GroundForge</artifactId>
            <version>0.x-SNAPSHOT</version>
        </dependency>
```
