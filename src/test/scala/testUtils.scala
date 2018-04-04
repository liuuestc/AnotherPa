import client.Client
import examples.LDA


object testUtils extends App {
  val client = new LDA()
  val lda = Class.forName(client.getClass.getName).newInstance().asInstanceOf[Client]
  lda.init
}
