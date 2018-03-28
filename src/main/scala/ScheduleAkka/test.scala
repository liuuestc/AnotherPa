package ScheduleAkka

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class test{

}
object Main extends App{
  println("Start simpleClusterListener")
  val actorSystem = ActorSystem("apsAkkaSystem",ConfigFactory.load("application.conf"))
  val simpleClusterListenerMaster = actorSystem.actorOf(Props[SimpleClusterListenerMaster],"SimpleClusterListenerMaster")
  //  Thread.sleep(1000)
  simpleClusterListenerMaster ! NettyServerStart(8090)
}

object Slave extends App{
  println("Start simple Slave")
  val actorSystem = ActorSystem("apsAkkaSystem",ConfigFactory.load("application_slave.conf"))
  val simpleClusterListener = actorSystem.actorOf(Props[SimpleClusterListener],"SimpleClusterListener")
}
