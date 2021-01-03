package dibl

import dibl.PatternQueries.{ gwPatterns, maePatterns, tlPatterns }
import dibl.proto.PairParams
import org.scalatest.{ FlatSpec, Matchers }

import scala.util.Try

class InkscapeTemplateSpec extends FlatSpec with Matchers {
  val gwOverlap = Seq("gw-B6", "gw-A2", "gw-E6", "gw-H14b", "gw-B2", "gw-C9", "gw-D6", "gw-C6")
  val maeOverlap = Seq("MAE-G54", "MAE-grond-12**", "MAE-G64", "MAE-G-02-YQ4b", "MAE-grond-12***", "MAE-G-02-Y1", "MAE-G-12")
  "fromUrl" should "render a vertical brick" in {
    val q = gwPatterns.toMap.getOrElse("gw-D2", fail("no gw-D2"))
    InkscapeTemplate.fromUrl(q).split("\n") should contain allElementsOf
      """CHECKER	6	10
        |[15.0,9.0,14.0,10.0,16.0,10.0]
        |[14.0,6.0,13.0,7.0,15.0,7.0]
        |[17.0,7.0,18.0,8.0,17.0,9.0]
        |[15.0,7.0,17.0,7.0,14.0,8.0]
        |[12.0,6.0,10.0,6.0,13.0,7.0]
        |[10.0,8.0,9.0,9.0,11.0,9.0]
        |[15.0,11.0,14.0,12.0,16.0,12.0]
        |[14.0,10.0,13.0,11.0,15.0,11.0]
        |[13.0,11.0,12.0,12.0,14.0,12.0]
        |[13.0,9.0,12.0,10.0,14.0,10.0]
        |[14.0,8.0,13.0,9.0,15.0,9.0]
        |[16.0,10.0,15.0,11.0,16.0,12.0]
        |[12.0,10.0,13.0,11.0,12.0,12.0]
        |[19.0,9.0,18.0,10.0,20.0,10.0]
        |[18.0,8.0,17.0,9.0,19.0,9.0]
        |[18.0,6.0,17.0,7.0,19.0,7.0]
        |[18.0,10.0,16.0,10.0,19.0,11.0]
        |[10.0,10.0,12.0,10.0,9.0,11.0]
        |[11.0,7.0,10.0,8.0,11.0,9.0]
        |[16.0,6.0,18.0,6.0,15.0,7.0]
        |[19.0,7.0,18.0,8.0,20.0,8.0]
        |[17.0,9.0,15.0,9.0,18.0,10.0]
        |[10.0,6.0,9.0,7.0,11.0,7.0]
        |[13.0,7.0,11.0,7.0,14.0,8.0]
        |[19.0,11.0,18.0,12.0,20.0,12.0]
        |[11.0,9.0,13.0,9.0,10.0,10.0]""".stripMargin.split("\n")
  }
  it should "render a horizontal brick" in {
    val q = "rose&patchWidth=12&patchHeight=16&d1=ct&c1=ctct&b1=ct&a1=ctct&d2=ctct&b2=ctct&tile=5831,-4-7&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2"
    InkscapeTemplate.fromUrl(q).split("\n") should contain allElementsOf
      """CHECKER	4	4
        |[5.0,5.0,5.0,6.0,6.0,6.0]
        |[7.0,6.0,8.0,6.0,7.0,7.0]
        |[7.0,5.0,6.0,6.0,7.0,6.0]
        |[4.0,4.0,3.0,4.0,5.0,4.0]
        |[7.0,7.0,7.0,8.0,8.0,8.0]
        |[6.0,6.0,5.0,6.0,7.0,6.0]
        |[4.0,6.0,3.0,7.0,5.0,7.0]
        |[5.0,4.0,6.0,4.0,5.0,5.0]
        |[6.0,4.0,5.0,5.0,7.0,5.0]
        |[5.0,7.0,4.0,8.0,5.0,8.0]
        |[5.0,6.0,4.0,6.0,5.0,7.0]
        |[7.0,4.0,6.0,4.0,7.0,5.0]""".stripMargin.split("\n")
  }
  it should "render overlapping tile layouts" in pendingUntilFixed {
    val q = "rose&patchWidth=9&patchHeight=10&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2"
    InkscapeTemplate.fromUrl(q).split("\n") should contain allElementsOf
      """CHECKER\t4\t4
        |""".stripMargin.split("\n")

    val patterns = (gwPatterns ++ maePatterns)
      .filter { case (n, _) => (gwOverlap ++ maeOverlap).contains(n) }
    val idToTemplate = urlQueryToTemplate(patterns)
    idToTemplate.filter(_._2.isFailure).keys should contain allElementsOf gwOverlap
  }
  it should "show used overlapping tile layouts" in {
    val overlap = gwOverlap ++ maeOverlap
    val params = (gwPatterns ++ maePatterns)
      .withFilter { case (n, _) => overlap.contains(n) }
      .map { case (_, q) =>
        val pp = PairParams(q)
        s"${pp.centerMatrixCols},${pp.centerMatrixRows};${pp.shiftColsSE},${pp.shiftColsSW},${pp.shiftRowsSE},${pp.shiftRowsSW}"
      }.distinct.sortBy(identity)
    println(params.mkString("", "\n", ""))
  }
  it should "render most of gw" in {
    val idToTemplate = urlQueryToTemplate(gwPatterns)
    idToTemplate.filter(_._2.isFailure).keys should contain allElementsOf gwOverlap
  }
  it should "render most of mae" in {
    val idToTemplate = urlQueryToTemplate(maePatterns)
    idToTemplate.filter(_._2.isFailure).keys should contain allElementsOf maeOverlap
  }
  it should "render all tl" in {
    val idToTemplate = urlQueryToTemplate(tlPatterns)
    idToTemplate.values.foreach(_.get should startWith("CHECKER"))
  }

  private def urlQueryToTemplate(patterns: Seq[(String, String)]) = {
    patterns.toMap.mapValues(q => Try(InkscapeTemplate.fromUrl(q)))
  }
}