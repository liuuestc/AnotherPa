package communication.Listener

import akka.actor.Actor

//当使用调度器做参数训练时，用于参数上传和下载的actor
class ParameterListener extends Actor{
  override def receive: Receive = ???
}
