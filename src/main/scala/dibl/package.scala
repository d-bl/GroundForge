/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/

import scala.annotation.tailrec
import scala.language.postfixOps
import scala.collection.immutable.HashMap

package object dibl {

    // as in http://stackoverflow.com/questions/15783837/beginner-scala-type-alias-in-scala-2-10

    type SrcNodes = Array[(Int,Int)]
    def SrcNodes(xs: (Int,Int)*) = Array(xs: _*)

    type R = Array[SrcNodes]
    def R(xs: SrcNodes*) = Array(xs: _*)

    type M = Array[R]
    def M(xs: R*) = Array(xs: _*) 

    type Props = HashMap[String,Any]
    def Props(xs: (String, Any)*) = HashMap(xs: _*) 

    def sourceOf(p: Props): Int = p.get("source").get.asInstanceOf[Int]
    def targetOf(p: Props): Int = p.get("target").get.asInstanceOf[Int]

    @tailrec
    def transparentLinks (nodes: Seq[Int],
                                  links: Seq[Props] = Seq[Props]()
                                 ): Seq[Props] =
        if (nodes.length < 2) links
        else transparentLinks(nodes.tail,links :+ Props(
            "source" -> nodes.head,
            "target" -> nodes.tail.head,
            "border" -> true
        ))
}
