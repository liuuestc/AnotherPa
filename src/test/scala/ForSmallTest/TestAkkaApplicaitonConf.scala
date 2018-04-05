package ForSmallTest

import com.typesafe.config.ConfigFactory
import conf.APSConfiguration

object TestAkkaApplicaitonConf extends App {
  val conf = ConfigFactory.parseString("""akka.cluster.seed-nodes=["akka.tcp://apsAkkaSystem@127.0.0.1:2551","akka.tcp://apsAkkaSystem@127.0.0.1:2552","akka.tcp://apsAkkaSystem@127.0.0.1:2553"]""").withFallback(ConfigFactory.load())
  println("Hello")
  println (conf.getString("akka.remote.netty.tcp.hostname"))
  println(conf.getStringList("akka.cluster.seed-nodes").size())
}
object TestConfiguration extends App{
  val conf = new APSConfiguration()
  println(conf.getStrings("aps.actorSystemSeeds.port").size)
  println(conf.get("aps.am.memory"))
}

object TestClient extends App{
}

object TestServer extends App{
}
