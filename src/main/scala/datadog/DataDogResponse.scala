package datadog

import fabric.rw._

case class DataDogResponse(errors: List[String])

object DataDogResponse {
  implicit val rw: RW[DataDogResponse] = RW.gen
}