package dibl

import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by Falkink on 20-3-2016.
  */
class PatternSpec extends FlatSpec with Matchers {
  "get" should "succeed" in {
    println(Pattern.get("2x4",12))
  }
}
