package communication

import akka.actor.ActorRef
import io.BlockInfo

//
case class NodeRef(ref: ActorRef)

case class APSSuccess()
case class APSFailure()
case class ReadBlock(blockInfo: BlockInfo)


case class NettyServerStart()
case class NettyId(host: String, port : Int) //返回

case class InitialModel()
case class TrainModel()
case class ModelTrainFinish()    //该模型训练结束并存储
case class ModelCache()          //将模型缓存
case class ModelSaved()          //返回模型存储成功
case class ModelId()

case class TransData(host: String, port: Int)
