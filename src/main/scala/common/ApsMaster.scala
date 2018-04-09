package common

import java.util.UUID

import akka.actor.{ActorRef, ActorSelection, Props}
import akka.cluster.ClusterEvent.{MemberEvent, MemberRemoved, MemberUp, UnreachableMember}
import client.Client
import common.workerInfo.{WorkerId, WorkerInfo}
import communication._
import communication.Listener.{AkkaCluter, ParameterListener}
import conf.APSConfiguration
import io.{BlockMangerImpl, ModelInfo, ParameterInfo}
import ipc.Client.ClientNettyImpl
import ipc.Server.ServerNettyImpl
import protobuf.MatrixLong.Matrix
import util.Utilities

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.util.{Failure, Success}
//master上只有block和model的信息
class ApsMaster(className : String, dataPath : String, numberOfContainer: Int, host:String,conf:APSConfiguration, outputPath: String) extends AkkaCluter{
  val workerIds = new ListBuffer[WorkerInfo]()
  val models = new ListBuffer[ModelInfo]()


  val blockManger = new BlockMangerImpl()


  var totalBias : Double = 0         //目前所有的误差和
  val partBias =  new mutable.HashMap[ModelInfo,Double]()   // 每个worker上传的误差
  var finished =0


  val masterParamterInfo = new ParameterInfo()
  val listBuffer = new ListBuffer[ParamInformation]
  //训练的模型
  val client = Class.forName(className).newInstance().asInstanceOf[Client]

  val nettyServer = new ServerNettyImpl()
  new Thread(nettyServer).start()
  val port = nettyServer.initial()
  val localHost = Utilities.getLocalHost
  val masterNetty = NettyId(localHost,port)

  //获取inputData的blockInfo,等待分发blockInfo（将blockInfo转换成String）
  val blockManagerImpl =new  BlockMangerImpl()
  val blockInfoes = blockManagerImpl.getAndAllocBlockInfo(dataPath,numberOfContainer)
  val parameterListener = context.actorOf(Props[ParameterListener],"parameter")

  val workRef ="""/user/worker"""

  //创建actorSystem
  val (actorSystem, boundPort) = new ActorSystemUtils("actorSystem",host,conf,true).createActorSystem

  def start(): Unit ={
    val masterListener = actorSystem.actorOf(Props(new ApsMaster(className,dataPath,numberOfContainer,host,conf,outputPath)),"master")
  }

  override def receive: Receive = {

    //收到注册消息后，将worker添加到workers中，并发送master的ActorRef
    case x : MemberUp =>
      log.info("Member is UP : {}", x.member)
      memberUped(x)
      //添加恢复的代码
    case x : UnreachableMember => {
      log.info("Member detected as unreachable : {}",x.member)
      recoverEnvironment()
    }
    case x : MemberRemoved => log.info("Member is Removed: {}",x.member)
    case x : MemberEvent =>

      //将收到的Worker信息添加到workerIds
    case WorkerInfoTrans(id,host,nettyPort) =>
      workerIds.append(new WorkerInfo(id,host,nettyPort))
      val blockinfors = getblockInfos                  //需要完善
      workerIds.find(_.getId == id).get.setBlockInfo(blockinfors)
      workers.get(id).get ! ReadBlock("")
      //等待所有的worker读取完数据后开始初始化模型
    case ReadStatus(id,status) =>
      if (status == "success"){
        workerIds.find(_.getId == id).get.setLoaded(true)
        if (numberOfContainer == workerIds.count(_.isLoaded)) {
          log.info("start Initial")
          workers.foreach(worker =>{
            worker._2 ! InitialModelAndParam(masterParamterInfo.getRow,masterParamterInfo.getColumn)
          })
          workerIds.foreach(worker =>{
            worker.setRunning(true)
          })
        }
      }
    case InitialModelSuccess(id) =>
      workerIds.find(_.getId == id).get.setInited(true)
      if(workerIds.count(_.isInited) == numberOfContainer){
        initialPara()
          workers.foreach(worker =>{
            worker._2 ! TrainModelAndParam
          })
      }

    case TrainModelAndParamFinish(iid,bias) =>{
      val ref = sender()
      //向worker发送结束消息
      if(computeBias() || iteratorFinish()) {
        models.find(_.getWorkerId ==iid).get.setFinished(true)
        ref ! ModelTrainFinish("")
      }
        //没有结束则将本机的model和parameter传输并将运行状态设置为false
      else {
        workerIds.find(_.getId == iid).get.setRunning(false)
        ref ! TransPara(localHost,port)
        val (h,p,id) = findNextWorker()
        ref ! TransMode(h,p,id)
      }
    }

    case TransModeSuccess(id,modeId,modelSize,nextId) =>
      var ref = sender()
      models.find(_.getModelId==modeId).get.setWorkerId(nextId)
      val worker = workerIds.find(_.getId == id).get
        worker.getModelIds.remove(id)
      val nextWorker = workerIds.find(_.getId == nextId).get
        nextWorker.addModelIds(modeId)
      if (modelSize > 0) {
        //如果worker有等待训练的模型则先将master的参数发给worker之后让他开始训练
        val f = Future{
          new ClientNettyImpl().sendParameter(null,worker.getLocalhost,worker.getNettyPort)
        }
        f.onComplete{
          case Success(value) =>
            ref ! TrainModelAndParam
            worker.setRunning(true)
          case Failure(e) => e.printStackTrace()   //可以进行重试
        }
      }
      if (nextWorker.isRunning == false){
        nextWorker.setRunning(true)
        val f = Future{
          new ClientNettyImpl().sendParameter(null,nextWorker.getLocalhost,nextWorker.getNettyPort)
        }
        f.onComplete{
          case Success(value) =>
            workers.get(nextId).get ! TrainModelAndParam
            worker.setRunning(true)
          case Failure(e) => e.printStackTrace()   //可以进行重试
        }
      }

    //初始化每个modelInfo
    case ModelInformation(id,modelId,matrixId) =>
      val modelInfo = new ModelInfo
      modelInfo.setCachePath(outputPath+modelId)
      modelInfo.setMatrixId(matrixId)
      modelInfo.setModelId(modelId)
      modelInfo.setWorkerId(id)
      models.append(modelInfo)
    //接受worker结束信号并统计model训练完成的计数
    case ModelFinishAndSaved(id) => if (models.count(_.isFinished) == numberOfContainer)  closeSystem()
  }

  //
  def updateModelInfo(){}
    //判断model是否已经迭代完成
    def iteratorFinish():Boolean ={true}
  //查找模型需要发送的worker地址
    def findNextWorker():(String,Int,Long) ={("",1,1L)}
  //关闭整个系统
  def closeSystem(){}
  //判断误差大小,如果符合标准则返回true
  def computeBias(): Boolean = {false}
  //将收到的所有接收到初始化的参数合并后将合并后的参数传输出去
  def initialPara(){}
  //查找refToNum中批数最少的actorSelection
  def getMinactorSelection() : ActorSelection ={

    null
  }

  //获取每台机器的blockInfos
  def getblockInfos: String = ""
  //当有新的节点加入是做如下处理
  def memberUped(x: MemberUp): Unit ={
    val slaveAS = cluster.system.actorSelection(x.member.address.toString+workRef)
    val id = UUID.randomUUID().getLeastSignificantBits.hashCode()
    workers.put(id,slaveAS)
    log.info(x.member.address.toString)
    slaveAS ! NodeRef(id,s"akka://actorSystem@$localHost:$port/user/master/")      //注册后将AM的actorRef返回给slave
  }
  //当某个actorSystem的有故障关闭后，重新启动某个actorSystem后的恢复工作
  def recoverEnvironment() = ???
}



object ApsMaster{
  def apply(className: String, dataPath: String, numberOfContainer: Int, host: String, conf: APSConfiguration, outputPath: String): ApsMaster = new ApsMaster(className, dataPath, numberOfContainer, host, conf, outputPath)
  def main(args: Array[String]): Unit = {

  }

}
