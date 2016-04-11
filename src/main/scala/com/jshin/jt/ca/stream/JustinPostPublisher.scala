package com.jshin.jt.ca.stream

import akka.actor.Actor.Receive
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage._
import com.jshin.jt.stream.JustinEvent

import scala.annotation.tailrec

/**
  * Created by justin on 4/11/16.
  */
class JustinPostPublisher extends ActorPublisher[JustinEvent] {

  val maxBufferSize = 20
  var messageBuffer = Vector.empty[JustinEvent]

  override def preStart(): Unit = {
    super.preStart()
    context.system.eventStream.subscribe(self, classOf[JustinEvent])
  }

  override def postStop(): Unit = {
    context.system.eventStream.unsubscribe(self)
    super.postStop()
  }

  override def receive: Receive = {
    case e: JustinEvent =>
      if (messageBuffer.isEmpty && totalDemand > 0) {
        onNext(e)
      } else {
        if (messageBuffer.length >= maxBufferSize) {
          messageBuffer = messageBuffer.tail :+ e
        } else {
          messageBuffer :+= e
        }
        deliverBuffer()
      }
    case Request(_) => deliverBuffer()
    case Cancel => context.stop(self)
  }

  @tailrec final def deliverBuffer(): Unit = {
    if (totalDemand > 0) {
      if (totalDemand <= Int.MaxValue) {
        val (use, keep) = messageBuffer.splitAt(totalDemand.toInt)
        messageBuffer = keep
        use foreach onNext
      } else {
        val (use, keep) = messageBuffer.splitAt(Int.MaxValue)
        messageBuffer = keep
        use foreach onNext
        deliverBuffer()
      }
    }
  }
}
