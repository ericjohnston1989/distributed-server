package sample.cluster.simple


import akka.actor.Actor
import collection.mutable.HashMap
//import context.dispatcher
import scala.concurrent.duration._

case class Evict()


class StringCounter extends Actor {

  import context.dispatcher

  private val data = HashMap[String, Int]()

  val tick = context.system.scheduler.schedule(1000 millis, 5000 millis, self, "tick")


  def receive = {
    case "tick" => println("TICK TOCK")
    case x : String => {
      println("TERM RECEIVED: " + x)
      val s = data.getOrElseUpdate(x, 0)
      data.put(x, s+1)
    }
    case ev : Evict => {
      data.foreach(println)
      data.clear()
    }
    case _ => println("unknown message received!!!")
  }
}

