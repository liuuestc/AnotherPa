import client.Client
import conf.APSConfiguration
import examples.LDA
import protobuf.MatrixDouble.DMatrix
import protobuf.{MatrixDouble, testMatrixDouble}

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

object testUtils extends App {
  val conf = new APSConfiguration()
  val client = new LDA()
  val lda = Class.forName(client.getClass.getName).newInstance().asInstanceOf[Client]
  print(lda.inputPath)
}

object testP extends App{
  val list = new ListBuffer[DMatrix]
  list += new testMatrixDouble().getMatrix
  println(list.size)
  list.foreach(matrix => {
    for (row <- matrix.getRowList){
      for (d <- row.getNumList)
        print(d + " ")
    }
    println()
  })
}
