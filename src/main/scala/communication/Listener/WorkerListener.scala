package communication.Listener

import akka.actor.{ActorSelection, Props}
import akka.cluster.ClusterEvent.{MemberEvent, MemberRemoved, MemberUp, UnreachableMember}
import client.Client
import common.workerInfo.WorkerInfo
import communication._
import io.{BlockMangerImpl, ModelInfo, ParameterInfo}
import ipc.Server.ServerNettyImpl
import protobuf.MatrixLong.Matrix
import util.Utilities

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.util.Success

class WorkerListener(className : String, blockMangerImpl: BlockMangerImpl) extends AkkaCluter {
  var masterRef : ActorSelection = null
  val workerInfo = new WorkerInfo(1L,Utilities.getLocalHost,1)
  //传输model的actor
  val transactionListener = context.actorOf(Props[TransactionListener],"transaction")
  //传输parameter的Actor
  val parameterListener = context.actorOf(Props[ParameterListener],"parameter")
  //模型训练Actor
  val learningListener = context.actorOf(Props[LearningListener], "learning")
  val nettyServer = new ServerNettyImpl
  workerInfo.setNettyPort(nettyServer.initial())

  //当前训练的model和本机的model列表
  var currentModel : ModelInfo = new ModelInfo
  val models = new ListBuffer[ModelInfo]
  models.append(currentModel)

  val parameterInfo = new ParameterInfo

  val clientClass = Class.forName(className).newInstance().asInstanceOf[Client]
  val outputPath = clientClass.outputPath
  override def receive: Receive = {
    case x : MemberUp =>
      log.info("Member is UP : {}", x.member)
    case x : UnreachableMember => log.info("Member detected as unreachable : {}",x.member)
    case x : MemberRemoved => log.info("Member is Removed: {}",x.member)
    case x : MemberEvent =>

      //启动注册后，更新本机的masterRef
    case NodeRef(id,ref) =>
      masterRef = cluster.system.actorSelection(ref)
      workerInfo.setId(id)
      masterRef ! WorkerInfoTrans(workerInfo.getId,workerInfo.getLocalhost,workerInfo.getNettyPort)

    case ReadBlock(blockInfos) =>
      readBlock(blockInfos)
      workerInfo.setBlockInfo(blockInfos)
      masterRef ! ReadStatus(workerInfo.getId,"success")
    case InitialModelAndParam(row,col) =>
      initialModel() //初始化currentModel
      masterRef ! ModelInformation(workerInfo.getId,currentModel.getModelId,currentModel.getMatrixId)
      transParamter() //初始化parameterInfo
      masterRef ! ParamInformation(workerInfo.getId,parameterInfo.getId,parameterInfo.getMatrixId)
      masterRef ! InitialModelSuccess(workerInfo.getId)
    case TrainModelAndParam =>
      trainModelandParams()
      masterRef ! TrainModelAndParamFinish(workerInfo.getId,currentModel.getBias)
    case ModelTrainFinish(outputPath) =>
      doSaveModel()
      masterRef ! ModelFinishAndSaved(workerInfo.getId)
    case TransMode(host,ip,id) => {
      val result = Future{
        tranModel()
      }
      result.onComplete{
        case Success(value) =>
          models -= currentModel
          if (value) masterRef ! TransModeSuccess(workerInfo.getId,currentModel.getMatrixId,models.size,id)
          currentModel =null
      }
    }
    case TransPara(host,ip) => transParamter()
    case test : String => println("HEllo , succeed")
  }

  //将模型存储
  def doSaveModel(){}
  //训练模型
  def trainModelandParams(){}
  //初始化模型
  def initialModel(){}
  //
  def tranModel(): Boolean={true}
  //传输parameters
  def transParamter():Boolean = {true}
  //读取block，并将数据放到TrainData中
  def readBlock(blockInfos: String): Unit ={

  }
}
