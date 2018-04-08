package communication.Listener

import akka.actor.{Actor, ActorRef}
import communication._
import ipc.Client.{ClientNetty, ClientNettyImpl}
import protobuf.MatrixLong.Matrix

import scala.collection.mutable.ListBuffer

//用于数据传输是使用的Actor，操作Netty Server
class TransactionListener(data: ListBuffer[Matrix]) extends Actor{
  /*def doTran(ref: ActorRef,ip:String,port:Int,data: Matrix){
    var i =0
    var t = new ClientNettyImpl().sendModel(data,ip,port)
    while (i<3 && !t){
      t = new ClientNettyImpl().sendModel(data,ip,port)
    }
    if (i == 3) {
      ref ! TransFailure
      Thread.interrupted()
    }
  }

  override def receive: Receive = {
    case TransData(ip,port) =>
      val ref = sender()
      data.foreach(d => doTran(ref,ip,port,d))
      ref ! TranSuccess
    case TransPara(ip,port) =>
      val ref = sender()
      data.foreach(d => doTran(ref,ip,port,d))
      ref ! TranSuccess
    case NettyId =>
  }*/
  override def receive: Receive = ???
}
