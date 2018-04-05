package common

import akka.actor.ActorRef
import client.Client
import common.workerInfo.WorkerId
import io.{BlockMangerImpl, ModelInfo}
import ipc.{NettyInfo, ServerNettyImpl}

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
class ApsMaster(className : String) {
  val nettys = new ListBuffer[NettyInfo]()
  val actorRefs = new ListBuffer[ActorRef]()
  val workerIds = new ListBuffer[WorkerId]()
  val models = new ListBuffer[ModelInfo]()
  val blockManger = new BlockMangerImpl()
  var times = 0     //训练的次数
  var bias : Double = 0
  val partBias =  new mutable.HashMap[String,Double]()   // 每个worker上传的误差
  val client = Class.forName(className).newInstance().asInstanceOf[Client]
  val nettyServer = new ServerNettyImpl()
  new Thread(nettyServer).start()
  def start(): Unit ={

  }
}

object ApsMaster{
  def apply(className: String): ApsMaster = new ApsMaster(className)

}
