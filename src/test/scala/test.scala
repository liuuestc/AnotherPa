package communication

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import communication.Listener.WorkerListener
import conf.APSConfiguration
import io.BlockMangerImpl
import util.Utilities

class test{

}

object Slave extends App{
  println("Start simple Slave")
  val actorSystem = ActorSystem("apsAkkaSystem",ConfigFactory.load("application_slave.conf"))
  val simpleClusterListener = actorSystem.actorOf(Props[WorkerListener],"SimpleClusterListener")
}

object tess extends App{
  val t = new BlockMangerImpl()

  println(t.getClass.getSimpleName)
  println(Utilities.getLocalPort)
  val conf = new APSConfiguration()
  println(conf.getInt("aps.worker.num",1))
}

trait Hello {
  def sayBye(name : String) = println("Bye "+ name)
  def sayHello(name: String)
}

class SayHello extends Hello{
  override def sayHello(name: String): Unit = println("Hello "+ name)
}

object testReflect extends App{
  val t = new SayHello
  val class1 = t.getClass.getName
  val clazz = Class.forName(class1).newInstance().asInstanceOf[Hello]
  clazz.sayBye("xiaoming")
  clazz.sayHello("xiaoming")
}