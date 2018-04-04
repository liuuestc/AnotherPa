package common

import akka.actor.ActorRef
import client.Client
import common.workerInfo.WorkerId
import io.{BlockManger, ModelInfo}
import ipc.NettyInfo

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
class apsMaster(className : String) {
  val nettys = new ListBuffer[NettyInfo]()
  val actorRefs = new ListBuffer[ActorRef]()
  val workerIds = new ListBuffer[WorkerId]()
  val models = new ListBuffer[ModelInfo]()
  val blockManger = new BlockManger()
  var times = 0     //训练的次数
  var bias : Double = 0
  val partBias =  new mutable.HashMap[String,Double]()   // 每个worker上传的误差
  val client = Class.forName(className).newInstance().asInstanceOf[Client]
  def start() = {

  }
}

object apsMaster{
  def apply(className: String): apsMaster = new apsMaster(className)
}
