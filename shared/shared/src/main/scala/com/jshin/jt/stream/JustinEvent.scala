package com.jshin.jt.stream

import boopickle.CompositePickler
import boopickle.Default._
import com.jshin.jt.api.JustinPostData

/**
  * Created by justin on 4/11/16.
  */

sealed trait JustinEvent

object JustinEvent {

  case class PostAdded(post: JustinPostData, pending: Boolean = false) extends JustinEvent
  case class PostDeleted(hash: String) extends JustinEvent

  implicit val eventPickler: CompositePickler[JustinEvent] = compositePickler[JustinEvent]

  eventPickler
    .addConcreteType[PostAdded]
    .addConcreteType[PostDeleted]

}

