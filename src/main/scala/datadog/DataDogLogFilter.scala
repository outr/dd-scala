package datadog

import fabric.rw._

case class DataDogLogFilter(query: String, indexes: List[String], from: Long, to: Long)

object DataDogLogFilter {
  implicit val rw: RW[DataDogLogFilter] = RW.gen
}