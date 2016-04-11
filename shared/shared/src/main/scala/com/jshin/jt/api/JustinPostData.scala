package com.jshin.jt.api

/**
  * Created by justin on 4/11/16.
  */
case class JustinPostData(
                           containerId: Option[Long],
                           parent: Option[String],
                           hash: String,
                           text: String
                         )
