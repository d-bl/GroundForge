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
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html
*/
package dibl;

import dibl.Force.Point;
import scala.util.Try;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Demo4Java {
    public static void main(String[] args) throws Throwable {
        // No named arguments in java, so you have to specify all default values
        // .apply is a scala-pattern for factory methods and indexing

        // some calls from Pattern.scala
        PatternSheet ps = PatternSheet.apply(2,"");
        ps.add("5-","bricks");
        String svgPatternSheet = ps.toSvgDoc();

        // some calls from ForceDemo.scala
        Try<Diagram> triedDiagram = PairDiagram.apply("5-", "bricks", 3, 3, 0, 0, "ct");
        if (triedDiagram.isFailure()) throw triedDiagram.failed().get();
        Diagram pairDiagram1 = triedDiagram.get(); // might be a single dot with an error as tooltip
        Try<Point[]> positions = Force.simulate(pairDiagram1, new Point(200, 200), 20, SECONDS);
        Diagram threadDiagram1 = ThreadDiagram.apply(pairDiagram1, positions);
        String svgDiagram = SVG.render(pairDiagram1);

        System.exit(0); /// required because of Force.simulate
    }
}
