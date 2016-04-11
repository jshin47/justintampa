package com.jshin.jt.ca

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.jshin.jt.ca.dispatch.JustinTampaDispatcher
import akka.actor.ActorSystem
import akka.http.scaladsl.model.MediaType.Compressible
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{CacheDirectives, `Cache-Control`}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import boopickle.Default._

import scala.concurrent.ExecutionContext

/**
  * Created by justin on 4/11/16.
  */

object JustinTampaServer {

  def apply(dispatcher: JustinTampaDispatcher)(implicit actorSystem: ActorSystem, actorMaterializer: ActorMaterializer): JustinTampaServer = {
    new JustinTampaServer(dispatcher)
  }

}

private[ca] final class JustinTampaServer(dispatcher: JustinTampaDispatcher)
                                         (
                                           implicit actorSystem: ActorSystem,
                                           actorMaterializer: ActorMaterializer
                                         )
  extends BinaryMarshaller {

  private implicit def ec: ExecutionContext = actorSystem.dispatcher

  private val maxPostSize = actorSystem.settings.config.getMemorySize("justintampa.max-post-size").toBytes

  val route = {
    get {
      encodeResponse(pathEndOrSingleSlash(getFromResource("webapp/index.html")) ~ getFromResourceDirectory("webapp"))
    }
  }

}
