package common

import client.Client
import communication.ActorSystemUtils
import conf.APSConfiguration
import protobuf.MatrixLong.Matrix

import scala.collection.mutable.ListBuffer
//模型，数据只在worker上
class ApsSlave(className : String,host: String){
  val conf = new APSConfiguration()
  val trainData = new ListBuffer[Matrix]         //数据可能比较大，在几个buffer上
  val client = Class.forName(className).newInstance().asInstanceOf[Client]
  val model = new ListBuffer[Matrix]             //ApsSlave上可能有多个model
  //创建actorSystem
  val (actorSystem, boundPort) = new ActorSystemUtils().createActorSystem("Master",host,conf,false)


}

object ApsSlave {
  def main(args: Array[String]): Unit = {
    val aps = new ApsSlave(args(0),args(1))
  }
}
