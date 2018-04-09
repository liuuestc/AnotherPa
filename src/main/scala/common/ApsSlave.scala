package common

import akka.actor.Props
import communication.ActorSystemUtils
import communication.Listener.WorkerListener
import conf.APSConfiguration
import io.BlockMangerImpl
import protobuf.MatrixLong.Matrix
import scala.collection.JavaConversions._
//模型，数据只在worker上，host可以是变参
class ApsSlave(className : String,host: String){
  val conf = new APSConfiguration()
  val blockMangerImpl = new BlockMangerImpl
  //创建actorSystem
  val (actorSystem, boundPort) = ActorSystemUtils("WorkerSystem",host,conf,false).createActorSystem
  val workerListener =  actorSystem.actorOf(Props(classOf[WorkerListener],className,blockMangerImpl),"worker")
}

object ApsSlave {
  def main(args: Array[String]): Unit = {
    val aps = new ApsSlave(args(0),args(1))
  }
}
