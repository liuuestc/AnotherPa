package common

import client.Client

class apsSlave(className : String){

  val client = Class.forName(className).newInstance().asInstanceOf[Client]
}

object apsSlave {
  def main(args: Array[String]): Unit = {
    val aps = new apsSlave(args(0))
  }
}
