package examples

import client.Client
import conf.APSConfiguration
import io.BlockMangerImpl
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



  override def inputPath: String ="outputPath"

  override def outputPath: String = ???

  override def compute(blockMangerImpl: BlockMangerImpl, id: Long): Boolean = ???

  override def merge(blockMangerImpl: BlockMangerImpl, id: Long): MatrixLong.Matrix = ???

  override def init(blockMangerImpl: BlockMangerImpl): Unit = ???
}
