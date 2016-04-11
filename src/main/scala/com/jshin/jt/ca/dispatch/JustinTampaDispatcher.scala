package com.jshin.jt.ca.dispatch

import com.jshin.jt.api.JustinPostData

import scala.concurrent.Future

/**
  * Created by justin on 4/11/16.
  */
trait JustinTampaDispatcher {

  def recent(offset: Long, count: Long): Future[Seq[JustinPostData]]

}
