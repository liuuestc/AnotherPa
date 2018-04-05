package communication.Listener

import akka.actor.{Actor, ActorSelection, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{MemberEvent, MemberRemoved, MemberUp, UnreachableMember}
import akka.event.Logging
import communication.{NettyServerStart, NodeRef}
import ipc.Server.ServerNettyImpl
import util.Utilities

import scala.collection.mutable

class MasterListener extends Actor{
  val log = Logging(context.system,this)
  val cluster = Cluster.get(context.system)
  val masterHost =Utilities.getLocalHost
  val nettyServer = new Thread(new ServerNettyImpl())

  val parameterListener = context.actorOf(Props[ParameterListener],"parameter")
  val transactionListener = context.actorOf(Props[TransactionListener],"transaction")

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
      memberUped(x)


    case x : UnreachableMember => log.info("Member detected as unreachable : {}",x.member)
    case x : MemberRemoved => log.info("Member is Removed: {}",x.member)
      val slaveAS = cluster.system.actorSelection(x.member.address.toString+"""/user/SimpleClusterListener""")
      refToNum.remove(slaveAS)


    case x : MemberEvent =>

    case x : String =>
      println("Succeed i have received!")
      println(masterHost)


    case NettyServerStart =>
      nettyServer.start()
    //netty客户端传完数据停止
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
  }
}
