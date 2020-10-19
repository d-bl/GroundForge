[![Build Status](https://travis-ci.org/d-bl/GroundForge.svg?branch=master)](https://travis-ci.org/d-bl/GroundForge) 
_A toolbox to design bobbin lace grounds with matching sets of pair/thread diagrams._

- [DEMO's](#demo-s)
- [How it's Made / third party data and scripts](#how-it-s-made---third-party-data-and-scripts)
  * [Proof of concept with D3.js](#proof-of-concept-with-d3js)
  * [Using data from TesseLace](#using-data-from-tesselace)
  * [Color-picker: jscolor](#color-picker--jscolor)
- [Compile and preview](#compile-and-preview)
  * [Requirements](#requirements)
  * [Work flow](#work-flow)
  * [Important code conventions](#important-code-conventions)
  * [Tests](#tests)
  [Use as JavaScript library](#use-as-javascript-library)
  [Use as JVM library](#use-as-jvm-library)


Short intro's
=============
For users (bobbin lace makers and designers) with screen-casts:<br>
https://d-bl.github.io/GroundForge/help/index

For developers:
* `src/scala/main/*` is translated to : `docs/js/GroundForge-opt.js`
* This is connected client side to HTML with : `docs/js/tiles.js`
* Translated to JVM for server-side or batch processing, for example something like : `src/test/Demo4Java.java`


DEMO's
======

A [dressed up](https://d-bl.github.io/GroundForge/tiles?patchWidth=12&patchHeight=12&a1=ct&b1=ct&c1=ctc&d1=ctc&b2=ctc&d2=ctc&a3=ct&c3=ct&footside=b,-,a,-&tile=831,4-7,-5-&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2) version and a dressed down [API](https://d-bl.github.io/GroundForge/API/) version.
The latter shows some sample code to configure your own web interface or embed just some of the diagrams on your own web page. 
See also the [API](https://d-bl.github.io/GroundForge/docs/API.md) for other environments than a web-browser.


Compile and preview
===================

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
- travis may report some of the internal link problems on the help pages in `docs/help`
- Optional (if you know how to meddle with branches as your own master branch
  will get another history than the parent repository):
  - [configure] your local fork to deploy `docs` at `http://YOURID.github.io/GroundForge/` 
  - after merging with your own master branch you can check your own version of the site.

[configure]: https://help.github.com/articles/configuring-a-publishing-source-for-github-pages/#publishing-your-github-pages-site-from-a-docs-folder-on-your-master-branch


Important code conventions
--------------------------

Never catch exceptions in a `Try` as exceptions terminate the JavaScript. The tests might succeed with maven, but the JavaScript breaks. Prevent exceptions like illegal arguments and indexes and create a `Failure` for safe execution with JavaScript.

The applied Scala coding techniques are explained by this [course] up and including workshop 3. The main code doesn't use any io, and the hand full of files written by test/demo classes don't justify using a library. So you can save the last task of the FileIO assignment for other purposes.

[course]: https://github.com/DANS-KNAW/course-scala


Tests
-----

Use `mvn clean test` to run unit tests. As some tests read pattern links from help files using java-io, the test fail with SBT.
Maven is much faster but uses JVM while the JS used by SBT is the actual target environment.
Some classes under `src/test` are suffixed with `Demos` rather than `Spec` these runnable objects create SVG documents in a `target/test` directory for a visual check.

Use as JavaScript Library
=========================

The html pages in the `docs` directory use the code in `src/main` as a JavaScript library.
This code is compiled to `GroundForge-opt.js`
The page `docs\API` is a dressed down page showing the minimal code for all diagrams of one hard-coded pattern,
the JavaScript more or less mimics the `Demo4Java` discussed below.
The pages direct under `docs` are the dressed up pages
deployed on github pages allowing to experiment with bobbin lace grounds. 

These pages have no automated tests
other than an internal link checker in the Ruby task by the [travis](https://travis-ci.org/d-bl/GroundForge) job.

For manual tests with other browsers and devices than your own, you could sign-up at
<br><a href="http://browserstack.com/"><img src="https://p14.zdusercontent.com/attachment/1015988/2pBNLzsRzHKyVmXhbPYFfcqi2?token=eyJhbGciOiJkaXIiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0..u4MOjDBdY7uyB7AqmHgHyw.OG_ZUr4mjWRjpV4IE5UH_bEtx-L-4NHCjNVSBjFvNP9X9ugBGhbEmHXVTJlpI-UBmAFBTl2SVYLgE4G474L0Hu37sYTtC5G3ehtEdiUYPn2R-MfM9cxUCJVP_T1PYk9_kZowoF2wSPFvaWphfvO9bk-hykkhDfPeFQ2BHsJlTlHbpNq8Icd4sveUMnJl0zFiy-h3kGo0ImQLRZnNsmEa3qx7JTINhL-bAUpGQKmdpvWAFVhtUIz8ZkntxRnuNi5EtXD1P4tucKH8kSt5gJXnSU_O0M0Ka_pTJgVXpEQMvTs.it94EtvuwAOOEjIRwQ7z1w" width="120" height="63"></a>

Use as JVM library
==================

The [Demo4Java.java](https://github.com/d-bl/GroundForge/blob/119-layout/src/test/scala/dibl/Demo4Java.java)
between the test classes is a simple example that generates diagrams with a plain java main class.
This example uses the code in `src/main` as a Java library. 

In a plain [JVM](https://www.w3schools.com/java/java_getstarted.asp)
environment, you'll need at least on your `classpath`: 
* The `.jar` from the [last release](https://github.com/d-bl/GroundForge/releases)
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
