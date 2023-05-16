package datadog

import fabric.rw.RW

object DataDogPage {
  implicit val rw: RW[DataDogPage] = RW.gen
}

case class DataDogPage(after: Option[String])