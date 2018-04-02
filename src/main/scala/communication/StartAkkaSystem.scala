package communication

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import communication.Listener.MasterListener



class StartAkkaSystem{
  def start(): Unit = {
    println("Start simpleClusterListener")
    val actorSystem = ActorSystem("apsAkkaSystem",ConfigFactory.load("application.conf"))
    val simpleClusterListenerMaster = actorSystem.actorOf(Props[MasterListener],"SimpleClusterListenerMaster")
    //  Thread.sleep(1000)
    simpleClusterListenerMaster ! NettyServerStart
  }
}