package datadog

import fabric.rw.RW

case class DataDogResponse(errors: List[String])

object DataDogResponse {
  implicit val rw: RW[DataDogResponse] = RW.gen
}