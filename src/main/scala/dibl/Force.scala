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
package dibl

import java.io.FileReader
import java.util.concurrent.{CyclicBarrier, TimeUnit}

//import javax.script.{Invocable, ScriptEngine, ScriptEngineManager}
//import jdk.nashorn.api.scripting.ScriptObjectMirror

import scala.util.Try

/** A dedicated JavaScript engine with a predefined D3js force simulation,
  * for batch execution in a JVM environment.
  */
object Force {

  case class Point(x: Double, y: Double)

}
