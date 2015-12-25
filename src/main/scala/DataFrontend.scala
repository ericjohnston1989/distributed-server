package sample.cluster.simple


import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.cluster.routing.ClusterRouterGroup
import akka.cluster.routing.ClusterRouterGroupSettings
import akka.routing.FromConfig
import akka.routing.RoundRobinPool
import akka.routing.RoundRobinGroup
import akka.routing.ConsistentHashingGroup
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope

class DataFrontend extends Actor with ActorLogging {

  //Create consistent hashing router here
  //val workerRouter = context.actorOf(
  //  FromConfig.props(Props[StringCounter]),
  //  name = "workerRouter"
  //)

  val s = context.actorOf(Props[StringCounter], name = "stringCounter")

  //println(s.path)

  val paths = List("/user/dataFrontend/stringCounter")
  //val workerRouter = context.actorOf(ConsistentHashingGroup(paths).props(), "router2")
  val workerRouter = context.actorOf(
    ClusterRouterGroup(
      ConsistentHashingGroup(paths),
      ClusterRouterGroupSettings(100, paths, true, None)
    ).props(),
    "router2"
  )
  //val workerRouter = context.actorOf(RoundRobinGroup(paths).props(), "router2")

  def receive = {
    case d : ClusterData => {
      d.data.flatMap(_._2).foreach { term =>
        println("SENDING ENVELOPE: " + term)
        workerRouter ! ConsistentHashableEnvelope(term, term)
      }
    }
  }

}
