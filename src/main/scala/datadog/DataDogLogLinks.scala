package datadog

import fabric.rw._
import spice.net.URL

case class DataDogLogLinks(next: Option[URL])

object DataDogLogLinks {
  implicit val rw: RW[DataDogLogLinks] = RW.gen
}