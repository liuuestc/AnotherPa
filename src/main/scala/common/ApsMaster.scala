package common

import akka.actor.ActorRef
import client.Client
import common.workerInfo.WorkerId
import communication.ActorSystemUtils
import conf.APSConfiguration
import io.{BlockMangerImpl, ModelInfo}
import ipc.NettyInfo
import ipc.Server.ServerNettyImpl
import protobuf.MatrixLong.Matrix

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
//master上只有block和model的信息
class ApsMaster(className : String, dataPath : String, numberOfContainer: Int, host:String,conf:APSConfiguration) {
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
  val port = nettyServer.initial()
  new Thread(nettyServer).start()
  val blockManagerImpl =new  BlockMangerImpl()
  val blockInfoes = blockManagerImpl.getAndAllocBlockInfo(dataPath,numberOfContainer)
  val paras = if(client.isPs) new ListBuffer[Matrix] else None  //AppMaster
  //创建actorSystem
  val (actorSystem, boundPort) = new ActorSystemUtils().createActorSystem("Master",host,conf,true)

  def start(): Unit ={

  }
}

object ApsMaster{
  def apply(className: String, dataPath: String, numberOfContainer: Int, host: String, conf: APSConfiguration): ApsMaster = new ApsMaster(className, dataPath, numberOfContainer, host, conf)
}
