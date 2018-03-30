package communication

import java.util.concurrent.{Callable, FutureTask}

import akka.actor.{Actor, ActorSelection}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{MemberEvent, MemberRemoved, MemberUp, UnreachableMember}
import akka.event.Logging
import ipc.Client.EchoClient
import ipc.Server.EchoServer
import org.apache.hadoop.net.NetUtils

import scala.collection.mutable

class SimpleClusterListenerMaster extends Actor{
  val log = Logging(context.system,this)
  val cluster = Cluster.get(context.system)
  val masterhost =NetUtils.getHostname.split("/")(1)
  val nettyServer = new Thread(new EchoServer())


  //每一个executor要训练到全部参数的批数
  val refToNum = new mutable.HashMap[ActorSelection,Int]()


  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent],classOf[UnreachableMember])
  }

  override def postStop() = {
    cluster.unsubscribe(self)
  }


  override def receive: Receive = {

    case x : MemberUp =>
      log.info("Member is UP : {}", x.member)
      println("HELlo , LOGINFO")
      memberUped(x)


    case x : UnreachableMember => log.info("Member detected as unreachable : {}",x.member)
    case x : MemberRemoved => log.info("Member is Removed: {}",x.member)
      val slaveAS = cluster.system.actorSelection(x.member.address.toString+"""/user/SimpleClusterListener""")
      refToNum.remove(slaveAS)


    case x : MemberEvent =>

    case x : String =>
      println("Succeed i have received!")
      println(masterhost)


    case NettyServerStart(port) =>
      nettyServer.start()
    //netty客户端传完数据停止
    case NettyClientStart(host,port) =>
      println("Start Netty")
      val echoClient=  new EchoClient()
      echoClient.setHost(host)
      echoClient.setPort(port)
      echoClient.start()
  }

  //查找refToNum中批数最少的actorSelection
  def getMinactorSelection() : ActorSelection ={
    if (!refToNum.isEmpty)
       refToNum.toSeq.sortBy(_._2).head._1
     null
  }


  //当有新的节点加入是做如下处理
  def memberUped(x: MemberUp): Unit ={
    val slaveAS = cluster.system.actorSelection(x.member.address.toString+"""/user/SimpleClusterListener""")
    log.info(x.member.address.toString)
    slaveAS ! NodeRef(context.self)      //注册后将AM的actorRef返回给slave
    refToNum += (slaveAS -> 0)
    slaveAS ! NettyServerStart(8091)
  }

}
