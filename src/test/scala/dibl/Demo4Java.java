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

import static dibl.Force.nudgeNodes;
import static java.util.concurrent.TimeUnit.SECONDS;

@SuppressWarnings("unused")
public class Demo4Java {
    public static void main(String[] args) throws Throwable {
        // No named arguments in java, so you have to specify all default values
        // .apply is a scala-pattern for factory methods and fetching an indexed element

        SheetSVG sheetSVG = SheetSVG.apply(2, "", "GF");
        sheetSVG.add("5-", "bricks");
        String svgPatternSheet = sheetSVG.toSvgDoc();

        Try<Diagram> triedPairDiagram = PairDiagram.create(
                "5-", "bricks",
                3, 3,
                0, 0,
                "ct"
        );
        if (triedPairDiagram.isFailure())
            throw triedPairDiagram
                    .failed()
                    .get();
        Point center = new Point(200, 200);
        Diagram nudgedPairDiagram = nudgeNodes(
                triedPairDiagram.get(), center, 20, SECONDS
        ).get();
        Diagram threadDiagram = nudgeNodes(
                ThreadDiagram.apply(nudgedPairDiagram), center, 20, SECONDS
        ).get();
        String s = D3jsSVG.render(threadDiagram, "2px", true, 744, 1052, 0);
        D3jsSVG.threadsCSS("".split(","));

        System.exit(0); /// required because of Force.simulate to nudge nodes
    }
}
