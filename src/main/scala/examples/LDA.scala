package examples

import client.Client
import conf.APSConfiguration
import deploy.ScalaClient
import protobuf.MatrixLong

class LDA extends Client{

  def initial(conf: APSConfiguration): Unit ={
    conf.setAppName("LDA")
    conf.setDataPath("")
    conf.set("LDAClasspath",this.getClass.getName)
    conf.setJarPath("Hello World")
  }

  override def selectOne: String = ???

  override def isPs: Boolean = true

  override def compute(matrix: MatrixLong.Matrix): MatrixLong.Matrix = ???

  override def init(matrix: MatrixLong.Matrix): MatrixLong.Matrix = ???

  override def merge(matrix: MatrixLong.Matrix): MatrixLong.Matrix = ???

  override def inputPath: String ="outputPath"

  override def outputPath: String = ???
}
