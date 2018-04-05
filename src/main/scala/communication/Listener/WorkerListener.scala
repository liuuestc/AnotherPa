package communication.Listener

import akka.actor.{Actor, ActorRef}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{MemberEvent, MemberRemoved, MemberUp, UnreachableMember}
import akka.event.Logging
import communication.{NettyServerStart, NodeRef}

class WorkerListener extends Actor {
  val log = Logging(context.system,this)
  val cluster = Cluster.get(context.system)
  var masterRef : ActorRef = null



  override def preStart(): Unit = {
    cluster.subscribe(self, classOf[MemberEvent],classOf[UnreachableMember])
  }

  override def postStop() = {
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {
    case x : MemberUp =>
      log.info("Member is UP : {}", x.member)
    case x : UnreachableMember => log.info("Member detected as unreachable : {}",x.member)
    case x : MemberRemoved => log.info("Member is Removed: {}",x.member)
    case x : MemberEvent =>

    case NodeRef(ref) =>
      masterRef = ref
      masterRef ! "HEllo"

    //netty 服务器启动
    case NettyServerStart =>
    //netty客户端传完数据停止


    case test : String => println("HEllo , succeed")
  }
}
