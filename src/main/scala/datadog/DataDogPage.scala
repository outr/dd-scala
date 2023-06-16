package datadog

import fabric.rw._

object DataDogPage {
  implicit val rw: RW[DataDogPage] = RW.gen
}

case class DataDogPage(after: Option[String])