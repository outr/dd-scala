package datadog

import fabric.rw.RW

case class Page(cursor: Option[String], limit: Int)

object Page {
  implicit val rw: RW[Page] = RW.gen
}