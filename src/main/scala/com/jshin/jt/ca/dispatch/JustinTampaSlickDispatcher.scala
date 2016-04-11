package com.jshin.jt.ca.dispatch

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.jshin.jt.api.JustinPostData
import com.jshin.jt.stream.JustinEvent
import com.typesafe.config._
import slick.driver.H2Driver.api._

import scala.concurrent._

/**
  * Created by justin on 4/11/16.
  */

object JustinTampaSlickDispatcher {
  def apply(db: Database, config: Config = ConfigFactory.load(), eventSink: Sink[JustinEvent, _] = Sink.ignore)(implicit executionContext: ExecutionContext, actorSystem: ActorSystem, actorMaterializer: ActorMaterializer): JustinTampaSlickDispatcher = {
    new JustinTampaSlickDispatcher(db, config, eventSink)
  }
}

private[dispatch] class JustinTampaSlickDispatcher(db: Database, config: Config, eventSink: Sink[JustinEvent, _])(implicit executionContext: ExecutionContext, actorSystem: ActorSystem, actorMaterializer: ActorMaterializer) extends JustinTampaDispatcher {
  override def recent(offset: Long, count: Long): Future[Seq[JustinPostData]] = {
    Future(
      Seq(
        JustinPostData(Some(1), None, "nfewfewfwfew", "ewfewfwfewfe")
      )
    )
  }
}
