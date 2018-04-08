package communication

import akka.actor.ActorRef
import io.BlockInfo

//
case class Registration()

case class NodeRef(id:Long,ref: String)

//传输给worker，使之读取String
case class ReadBlock(blockInfos: String)
case class ReadStatus(id : Long, status: String)     //success or failure


case class NettyServerStart()
case class NettyId(host: String, port : Int) //返回

case class InitialModelAndParam(row:Int,col:Int)              //所有的参数初始化
case class InitialModelSuccess(id : Long)


case class TrainModelAndParam()
case class TrainModelAndParamFinish(id : Long , bias: Double)
case class ModelTrainFinish(outputPath : String)    //该模型训练结束并存储
case class ModelFinishAndSaved(id: Long)
case class ModelCache()          //将模型缓存
case class ModelSaved()          //返回模型存储成功
case class ModelInformation(id: Long, modelId: Long , matrixId : Long)     //模型初始化后，向master传递的信息
case class ParamInformation(id: Long, paramId: Long , matrixId : Long)     //参数初始化后，向master传递的信息

case class TransMode(host: String, port: Int, id : Long)
case class TransPara(host: String, port: Int)
case class TransModeSuccess(id:Long, modeId:Long, modelSize: Long,nextId: Long)


case class TransFailure()
case class TranSuccess()
case class WorkerInfoTrans(id: Long, host:String, nettyPort: Int)