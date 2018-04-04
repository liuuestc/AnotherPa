package communication.Listener

import akka.actor.Actor
import communication.{NettyId, TransData}
import ipc.server.NettyServer

//用于数据传输是使用的Actor，操作Netty Server
class TransactionListener extends Actor{
  override def receive: Receive = {
    case TransData(ip,port) =>
    case NettyId =>
  }
}
