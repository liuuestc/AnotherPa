package common

import akka.actor.ActorRef
import common.workerInfo.WorkerId
import io.{BlockManger, ModelInfo}
import ipc.NettyInfo

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
class appMaster[T] {
  val nettys = new ListBuffer[NettyInfo]()
  val actorRefs = new ListBuffer[ActorRef]()
  val workerIds = new ListBuffer[WorkerId]()
  val models = new ListBuffer[ModelInfo]()
  val blockManger = new BlockManger();
  var times = 0     //训练的次数
  var bias : Double = 0
  val partBias =  new mutable.HashMap[String,Double]()   // 每个worker上传的误差
}

object appMaster{

}
