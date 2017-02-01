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

import java.util.concurrent.TimeUnit;

public class ForceDemoJ {
    public void main(String[] args) {
        Diagram pairDiagram1 = PairDiagram.apply("5-", "bricks", 3, 3, 0, 0, "ct").get();
        Diagram threadDiagram1 = ThreadDiagram.apply(pairDiagram1, Force.simulate(pairDiagram1, new Point(200,200), 20, TimeUnit.SECONDS));
        String svg = SVG.render(pairDiagram1);
    }
}
