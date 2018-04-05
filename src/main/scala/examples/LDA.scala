package examples

import client.Client
import conf.APSConfiguration
import deploy.ScalaClient

class LDA extends Client{

  def initial(conf: APSConfiguration): Unit ={
    conf.setAppName("LDA")
    conf.setDataPath("")
    conf.set("LDAClasspath",this.getClass.getName)
    conf.setJarPath("Hello World")
  }

  override def compute: Unit = println("LDA is computing")
  override def getPs: Nothing = super.getPs
  override def putPs: Nothing = super.putPs
  override def init: Unit = println("LDA is inited")
  override def selectOne: String = ???
  override def loadDate(inputPath: String): Unit = ???
  override def saveModel(outputPath: String): Unit = ???

  override def isPs: Boolean = true

  override def merge: Unit = ???
}
