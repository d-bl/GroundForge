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
    
    def column[A, M[A]](matrix: M[M[A]], colIdx: Int)
      (implicit v1: M[M[A]] => Seq[M[A]], v2: M[A] => Seq[A]): Seq[A] =
      matrix.map(_(colIdx))
}
