package com.jshin.jt.ca

import java.nio.file._
import java.time.LocalDate
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.{Attributes, _}
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.jshin.jt.ca.dispatch.JustinTampaSlickDispatcher
import com.typesafe.config.ConfigFactory
import slick.driver.H2Driver.api._
import slick.jdbc.meta.MTable

import scala.concurrent._
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util._

/**
  * Created by justin on 4/11/16.
  */
object Main extends App {
  // Initialize configuration
  val config = {
    val default = ConfigFactory.load()
    val external = Paths.get(default.getString("justintampa.external-config-file"))
    if (Files.isRegularFile(external)) {
      ConfigFactory.parseFile(external.toFile)
        .withFallback(default)
        .resolve()
    } else {
      default
    }
  }

  implicit val actorSystem = ActorSystem("justintampa-server", config)
  implicit val executionContext = actorSystem.dispatcher
  implicit val actorMaterializer = ActorMaterializer(ActorMaterializerSettings(actorSystem))
  val db = Database.forConfig("justintampa.database", config)

  val dispatcher = JustinTampaSlickDispatcher(db, config, Sink.foreach { event ⇒
    actorSystem.eventStream.publish(event)

    event match {
      case _ ⇒ // nothin
    }
  })

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run(): Unit = {
      actorSystem.log.info("Shutting down justintampa-server")
      Await.result(actorSystem.terminate(), Duration.Inf)
    }
  }))

  // REST server
  val server = JustinTampaServer(dispatcher)
  val host = config.getString("justintampa.server.host")
  val port = config.getInt("justintampa.server.port")
  Http().bindAndHandle(server.route, host, port).onComplete {
    case Success(ServerBinding(address)) ⇒
      actorSystem.log.info("justintampa server listening at {}", address)

    case Failure(exc) ⇒
      actorSystem.log.error(exc, "Port binding failure")
  }

}
