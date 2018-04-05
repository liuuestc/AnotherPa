import client.Client
import conf.APSConfiguration
import examples.LDA


object testUtils extends App {
  val conf = new APSConfiguration()
  val client = new LDA()
  val lda = Class.forName(client.getClass.getName).newInstance().asInstanceOf[Client]
  lda.compute
}
