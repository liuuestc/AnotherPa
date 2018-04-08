package communication.Listener

import akka.actor.{Actor, ActorLogging, ActorPath, ActorRef, ActorSelection}
import akka.cluster.{Cluster, Member}
import akka.cluster.ClusterEvent.{InitialStateAsEvents, MemberEvent, MemberUp, UnreachableMember}
import communication.Registration

import scala.collection.mutable

trait AkkaCluter extends Actor with ActorLogging{
  // 创建一个Cluster实例
  val cluster = Cluster(context.system)
  // 用来缓存下游注册过来的子系统ActorRef
  var workers = mutable.HashMap.empty[Long,ActorSelection]

  override def preStart(): Unit = {
    // 订阅集群事件
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberUp], classOf[UnreachableMember], classOf[MemberEvent])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  /**
    * 下游子系统节点发送注册消息
    */
  def register(member: Member, createPath: (Member) => ActorPath): Unit = {
    val actorPath = createPath(member)
    log.info("Actor path: " + actorPath)
    val actorSelection = context.actorSelection(actorPath)
    actorSelection ! Registration
  }
}
