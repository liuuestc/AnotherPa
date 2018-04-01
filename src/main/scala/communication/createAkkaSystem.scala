package communication

import akka.actor.{ActorSystem, ExtendedActorSystem}
import com.typesafe.config.ConfigFactory
import conf.APSConfiguration
import org.apache.log4j.{Level, Logger}

class createAkkaSystem {
  private def doCreateActorSystem(name: String, host: String, port: Int ,port1:Int,conf: APSConfiguration): (ActorSystem, Int) = {
    //对conf中的一些参数进行设置
    val akkaThreads = conf.getInt("aps.akka.threads", 4)
    val akkaBatchSize = conf.getInt("aps.akka.batchSize", 15)
    val akkaLogLifecycleEvents = conf.getBoolean("aps.akka.logLifecycleEvents", false)
    val lifecycleEvents = if (akkaLogLifecycleEvents) "on" else "off"
    if (!akkaLogLifecycleEvents) {
      // As a workaround for Akka issue #3787, we coerce the "EndpointWriter" log to be silent.
      // See: https://www.assembla.com/spaces/akka/tickets/3787#/
      Option(Logger.getLogger("akka.cluster.EndpointWriter")).map(l => l.setLevel(Level.FATAL))
    }
    val akkaTimeoutS = conf.getTimeAsSeconds("aps.akka.timeout",
      conf.get("aps.network.timeout", "120s"))
    val logAkkaConfig = if (conf.getBoolean("aps.akka.logAkkaConfig", false)) "on" else "off"

    val akkaHeartBeatPausesS = conf.getTimeAsSeconds("aps.akka.heartbeat.pauses", "6000s")
    val akkaHeartBeatIntervalS = conf.getTimeAsSeconds("aps.akka.heartbeat.interval", "1000s")


    val akkaConf = ConfigFactory.parseString(
      s"""
         |akka.daemonic = on
         |akka.loggers = [""akka.event.slf4j.Slf4jLogger""]
         |akka.stdout-loglevel = "ERROR"
         |akka.jvm-exit-on-fatal-error = off
         |akka.cluster.transport-failure-detector.heartbeat-interval = $akkaHeartBeatIntervalS s
         |akka.cluster.transport-failure-detector.acceptable-heartbeat-pause = $akkaHeartBeatPausesS s
         ||akka.cluster.netty.tcp.connection-timeout = $akkaTimeoutS s
         |akka.actor.provider = "akka.cluster.ClusterActorRefProvider"
         |akka.cluster.netty.tcp.transport-class = "akka.cluster.transport.netty.NettyTransport"
         |akka.cluster.seed-nodes=["akka.tcp://apsAkkaSystem@$host:$port","akka.tcp://apsAkkaSystem@$host:$port1"]
         |akka.cluster.netty.tcp.tcp-nodelay = on
         |akka.cluster.netty.tcp.execution-pool-size = $akkaThreads
         |akka.actor.default-dispatcher.throughput = $akkaBatchSize
         |akka.log-config-on-start = $logAkkaConfig
         |akka.cluster.log-cluster-lifecycle-events = $lifecycleEvents
         |akka.log-dead-letters = $lifecycleEvents
         |akka.log-dead-letters-during-shutdown = $lifecycleEvents
      """.stripMargin)
    //创建ActorSystem对象
    val actorSystem = ActorSystem(name, akkaConf)
    val provider = actorSystem.asInstanceOf[ExtendedActorSystem].provider
    val boundPort = provider.getDefaultAddress.port.get
    //返回（ActorSystem，int）元组
    (actorSystem, boundPort) }
}
