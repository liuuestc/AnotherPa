package ScheduleAkka

import akka.actor.ActorRef

//
case class NodeRef(ref: ActorRef)
case class NettyServerStart(port: Int)
case class NettyClientStart(host: String, port : Int)
case class TransData(host: String = "", port: Int = 8090)
