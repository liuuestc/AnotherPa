package ScheduleAkka

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class SlaveAkkaSystem{
  def start(): Unit = {
    println("Start simple Slave")
    val actorSystem = ActorSystem("apsAkkaSystem",ConfigFactory.load("application_slave.conf"))
    val simpleClusterListener = actorSystem.actorOf(Props[SimpleClusterListener],"SimpleClusterListener")
  }
}