package examples

import client.Client
import conf.APSConfiguration
import deploy.ScalaClient

class LDA extends Client{
  override def compute: Unit = ???
  override def getPs: Nothing = super.getPs
  override def putPs: Nothing = super.putPs
  override def init: Unit = println("LDA is inited")
  override def selectOne: String = ???
  override def loadDate(inputPath: String): Unit = ???
  override def saveModel(outputPath: String): Unit = ???
}
object LDA {
  def main(args: Array[String]): Unit = {
    val conf = new APSConfiguration()
    conf.setAppName("LDA")
    conf.set("LDAClasspath",this.getClass.getName)
    conf.setJarPath("Hello World")                          //设置jar包的地址
    new ScalaClient(conf,new LDA,true).run()
  }
}