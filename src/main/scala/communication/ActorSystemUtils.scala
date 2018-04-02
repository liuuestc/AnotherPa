package communication

import akka.actor.{ActorSystem, ExtendedActorSystem}
import com.typesafe.config.ConfigFactory
import conf.APSConfiguration


 class ActorSystemRef()

case class MasterRef(name: String,ip: String,port : Int) extends ActorSystemRef
case class TransactionRef(name: String,ip: String,port : Int) extends ActorSystemRef
case class LearningRef(name: String, ip: String,port : Int) extends ActorSystemRef


class ActorSystemUtils {

  //创建actorSystem并返回（ActorSystem，port）
  def createActorSystem(name: String, host: String, conf: APSConfiguration, isMaster: Boolean): (ActorSystem, Int) = {
      doCreateActorSystem(name, host, conf, isMaster)
  }

  private def doCreateActorSystem(name: String, host: String,conf: APSConfiguration ,isMaster: Boolean): (ActorSystem, Int) = {
    val port = if(isMaster) conf.getInt("aps.akka.cluster.master.port",0) else 0
    val ports = conf.getStrings("aps.actorSystemSeeds.port")
    val port1 = Integer.valueOf(ports(0))
    //对conf中的一些参数进行设置
    val akkaLogLifecycleEvents = conf.getBoolean("aps.akka.logLifecycleEvents", false)
    val lifecycleEvents = if (akkaLogLifecycleEvents) "on" else "off"
    val akkaConf = ConfigFactory.parseString(
      s"""
         |akka.actor.provider = "akka.cluster.ClusterActorRefProvider"
         |akka.remote.netty.tcp.hostname="127.0.0.1"
         |akka.remote.netty.tcp.port=$port
         |akka.cluster.seed-nodes=["akka.tcp://$name@$host:$port1","akka.tcp://$name@$host:$port1"]
         |akka.cluster.log-cluster-lifecycle-events = $lifecycleEvents
         |akka.log-dead-letters = $lifecycleEvents
         |akka.log-dead-letters-during-shutdown = $lifecycleEvents
      """.stripMargin)
    //创建ActorSystem对象
    val actorSystem = ActorSystem(name, akkaConf)
    val provider = actorSystem.asInstanceOf[ExtendedActorSystem].provider
    val boundPort = provider.getDefaultAddress.port.get
    //返回（ActorSystem，int）元组
    (actorSystem, boundPort)
  }

  //根据根据名字查找对应的akka Address，用于actorSelection
  def getAkkaAddress(actorSystemRef: ActorSystemRef): String = actorSystemRef match {
    case LearningRef(name,ip,port) => s"akka://$name@$ip:$port/user/worker/learning"
    case TransactionRef(name,ip,port) =>  s"akka://$name@$ip:$port/user/worker/transaction"
  }
}

