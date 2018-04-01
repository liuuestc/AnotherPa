package communication

import akka.actor.Actor

//训练时，所使用的
class LearningRef extends Actor with ActorInterface {
  override def receive: Receive = ???
}
