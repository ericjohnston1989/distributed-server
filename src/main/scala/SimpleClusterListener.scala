package sample.cluster.simple
 
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import akka.contrib.pattern.ClusterReceptionistExtension

class SimpleClusterListener extends Actor with ActorLogging {
 
  val cluster = Cluster(context.system)

  //Register cluster so that it can be reached from a client
  ClusterReceptionistExtension(context.system).registerService(self)

  //Create a worker actor
  //context.system.actorOf(Props[StringCounter], name = "stringCounter")

  //Create a data frontend
  val frontend = context.system.actorOf(Props[DataFrontend], name = "dataFrontend")

  // subscribe to cluster changes, re-subscribe when restart 
  override def preStart(): Unit = {
    //#subscribe
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
    //#subscribe
  }
  override def postStop(): Unit = cluster.unsubscribe(self)
 
  def receive = {
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)
    case x : ClusterData => frontend ! x
    case x : String => println("It's a string: " + x)
    case _: MemberEvent => // ignore
  }
}

