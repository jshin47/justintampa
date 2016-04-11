package com.jshin.jt.client

import com.karasiq.taboverridejs.TabOverride
import org.scalajs.jquery._
import org.widok.moment.Moment
import rx.Ctx

import scala.concurrent.ExecutionContext
import scala.scalajs.concurrent.JSExecutionContext
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

/**
  * Created by justin on 4/11/16.
  */
object Main extends JSApp {
  implicit val ec: ExecutionContext = JSExecutionContext.queue
  implicit val ctx = implicitly[Ctx.Owner]

  @JSExport
  override def main(): Unit = {
    jQuery(() ⇒ {
      TabOverride.tabSize(2)
    })
  }
}
