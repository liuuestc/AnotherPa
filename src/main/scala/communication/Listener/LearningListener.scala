package communication.Listener

import akka.actor.Actor
import communication._
import ipc.server.NettyServer

//model训练是使用的Actor
class LearningListener extends Actor {
  override def receive: Receive = {
    case InitialModel =>
    case TrainModel =>
    case ModelTrainFinish =>
    case ModelCache =>
    case ModelSaved =>
  }
}
