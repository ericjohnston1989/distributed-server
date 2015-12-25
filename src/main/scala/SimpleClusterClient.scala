package sample.cluster.simple

import akka.actor.ActorSystem
import akka.contrib.pattern.ClusterClient


case class ClusterData(
  org : String,
  item : Option[String],
  data : Map[String, Seq[String]],
  format : String
)

//Proof of concept
object SimpleClusterClient {

  def main(args : Array[String]) : Unit = {

    val system = ActorSystem("ClusterSystem")

    val initialContacts = Set(
      system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/receptionist"),
      system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2552/user/receptionist")
    )

    val c = system.actorOf(ClusterClient.props(initialContacts))

    (0 until 10).foreach { x =>
      val k = ClusterData("test", Some("hello-" + x), Map("a" -> Seq("b", "c", "d", "e", "f", "g", "h")), "insert")
      c ! ClusterClient.Send("/user/clusterListener", k, localAffinity = false)
    }
  }


}
