package common

import client.Client

class ApsSlave(className : String){

  val client = Class.forName(className).newInstance().asInstanceOf[Client]
}

object ApsSlave {
  def main(args: Array[String]): Unit = {
    val aps = new ApsSlave(args(0))
  }
}
