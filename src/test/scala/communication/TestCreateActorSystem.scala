package communication

import conf.APSConfiguration

object TestCreateActorSystem extends App {
  val conf = new APSConfiguration()
  val (actorSystem,boundPort) = new ActorSystemUtils().createActorSystem("master","127.0.0.1",conf,true)
  val (actorSystem1,boundPort2) = new ActorSystemUtils().createActorSystem("master","127.0.0.1",conf,false)
  println(boundPort)
  println(boundPort2)
}
