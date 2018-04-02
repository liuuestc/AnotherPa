package communication.Listener

import akka.actor.Actor
import ipc.server.NettyServer

//当使用调度器做参数训练时，用于参数上传和下载的actor
class ParameterListener(nettyServer: NettyServer) extends Actor{
  override def receive: Receive = ???
}
