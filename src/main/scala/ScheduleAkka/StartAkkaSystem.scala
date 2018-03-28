package ScheduleAkka

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory



class StartAkkaSystem{
  def start(): Unit = {
    println("Start simpleClusterListener")
    val actorSystem = ActorSystem("apsAkkaSystem",ConfigFactory.load("application.conf"))
    val simpleClusterListenerMaster = actorSystem.actorOf(Props[SimpleClusterListenerMaster],"SimpleClusterListenerMaster")
    //  Thread.sleep(1000)
    simpleClusterListenerMaster ! NettyServerStart(8090)
  }
}