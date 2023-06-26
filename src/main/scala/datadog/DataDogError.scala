package datadog

import fabric.rw._

case class DataDogError(message: String, kind: String, stack: String)

object DataDogError {
  implicit val rw: RW[DataDogError] = RW.gen
}