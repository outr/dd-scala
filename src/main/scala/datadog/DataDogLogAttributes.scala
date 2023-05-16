package datadog

import fabric.Json
import fabric.rw.RW

case class DataDogLogAttributes(attributes: Json,
                                service: String,
                                host: String,
                                message: String,
                                status: String,
                                timestamp: String,
                                tags: List[String])

object DataDogLogAttributes {
  implicit val rw: RW[DataDogLogAttributes] = RW.gen
}