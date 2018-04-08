package common

import akka.actor.Props
import client.Client
import communication.ActorSystemUtils
import communication.Listener.WorkerListener
import conf.APSConfiguration
import protobuf.MatrixLong.Matrix

import scala.collection.mutable.ListBuffer
//模型，数据只在worker上，host可以是变参
class ApsSlave(className : String,host: String,outputPath :String){
  val conf = new APSConfiguration()
  val trainData = new ListBuffer[Matrix]         //数据可能比较大，在几个buffer上
  val model = new ListBuffer[Matrix]             //ApsSlave上可能有多个model
  val parameters = new ListBuffer[Matrix]
  //创建actorSystem
  val (actorSystem, boundPort) = ActorSystemUtils("WorkerSystem",host,conf,false).createActorSystem
  val workerListener =  actorSystem.actorOf(Props(classOf[WorkerListener],className,trainData,model,parameters,outputPath),"worker")
}

object ApsSlave {
  def main(args: Array[String]): Unit = {
    val aps = new ApsSlave(args(0),args(1),args(3))
  }
}
