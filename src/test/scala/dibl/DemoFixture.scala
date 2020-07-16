package dibl

import dibl.proto.ProtoDemos.getClass

trait DemoFixture {
  val testDir = new java.io.File(s"target/test/${getClass.getSimpleName}".replace("$",""))
  testDir.mkdirs()

}
