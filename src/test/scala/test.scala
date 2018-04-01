package communication

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import io.BlockManger

class test{

}
object Main extends App{
  println("Start simpleClusterListener")
  val actorSystem = ActorSystem("apsAkkaSystem",ConfigFactory.load("application.conf"))
  val simpleClusterListenerMaster = actorSystem.actorOf(Props[SimpleClusterListenerMaster],"SimpleClusterListenerMaster")
  //  Thread.sleep(1000)
  simpleClusterListenerMaster ! NettyServerStart
}

object Slave extends App{
  println("Start simple Slave")
  val actorSystem = ActorSystem("apsAkkaSystem",ConfigFactory.load("application_slave.conf"))
  val simpleClusterListener = actorSystem.actorOf(Props[SimpleClusterListener],"SimpleClusterListener")
}

object tess extends App{
  val t = new BlockManger()
  println(t.getClass)
}
