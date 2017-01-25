package dibl

import java.io.FileReader
import javax.script.{Invocable, ScriptContext, ScriptEngine, ScriptEngineManager}

import jdk.nashorn.api.scripting.ScriptObjectMirror

object Demo extends App {
  private val engine: ScriptEngine = (new ScriptEngineManager).getEngineByName ("nashorn")
  private val engineScope = engine.getBindings(ScriptContext.ENGINE_SCOPE)
  private val invocable = engine.asInstanceOf[Invocable]
  engineScope.put("window", engineScope)
  engine.eval(new FileReader("docs/js/d3.v4.min.js"))
  engine.eval(new FileReader("docs/js/event_loop.js"))
  engine.eval(
    """function nodesToJS(arrayOfMaps) {
      |  var result = new Array(arrayOfMaps.size())
      |  for ( i = arrayOfMaps.size() ; i-- > 0 ;  ){
      |    map = arrayOfMaps.apply(i)
      |    result[i] = {
      |      x: map.get('x').get(),
      |      y: map.get('y').get()
      |    }
      |  }
      |  return result
      |}
      |function linksToJS(nodes, arrayOfMaps) {
      |  var result = new Array(arrayOfMaps.size())
      |  for ( i = arrayOfMaps.size() ; i-- > 0 ;  ){
      |    map = arrayOfMaps.apply(i)
      |    result[i] = {
      |      source: map.get('source').get(),
      |      target: map.get('target').get()
      |    }
      |  }
      |  return result
      |}
      |function applyForce(center, data) {
      |  var nodes = nodesToJS(data.nodes())
      |  var links = linksToJS(nodes, data.links())
      |  d3.forceSimulation(nodes)
      |    .force("charge", d3.forceManyBody().strength(-1000))
      |    .force("link", d3.forceLink(links).strength(50).distance(12).iterations(30))
      |    .force("center", d3.forceCenter(center.x(), center.y()))
      |    .alpha(0.0035)
      |    .on("end", function() {
      |      Java.type("dibl.Demo").onEnd(nodes)
      |    })
      |}
      |print("javascript engine started")
      |""".stripMargin)

  def onEnd(jsNodes: ScriptObjectMirror): Unit = {
    val nodes: Array[Point] = jsNodes
      .values()
      .toArray()
      .map(ps => {
        val props = ps.asInstanceOf[ScriptObjectMirror]
        Point(
          props.get("x").asInstanceOf[Double],
          props.get("y").asInstanceOf[Double]
        )
      })
    println(nodes.mkString(", "))
    println("substitute points in nodes of not yet implemented SVG document")
    // somehow the main thread should wait for this moment
    // perhaps rewrite the event_loop to achieve synchronous execution
    System.exit(0)
  }

  case class Point(x: Double, y: Double)

  def applyForce(center: Point, diagram: Diagram): Unit = {
    println(s"SCALA nodes: ${diagram.nodes}")
    println(s"SCALA links: ${diagram.links}")
    invocable.invokeFunction("applyForce", Point(200,200), diagram)
  }

  ////////////////////////////////////////////////
  applyForce( Point(200,200), PairDiagram(Settings(
    "5-", "bricks", stitches = "ctc", absRows = 3, absCols = 3
  )))
}
