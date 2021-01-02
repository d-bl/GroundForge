package dibl

package object proto {
  def toThreadsSimple3x2(urlQuery: String): String = {
    PairParams(urlQuery)
      .toString +
      urlQuery
        .split("&")
        .filter(_.toLowerCase.matches(".*=[ctrlp-]+"))
        .mkString("&")
  }

  private val regExp = "[^0-9a-z-]+"
  implicit class KeyValuePairs(val queryFields: Map[String, String]) extends AnyVal {
    def matrixLines(key: String): Seq[String] = queryFields
      .getOrElse(key, "")
      .toLowerCase
      .split(regExp)
    // TODO make lengths equal with dashes?
  }
}
