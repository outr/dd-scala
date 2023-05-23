package datadog

import fabric.Json
import fabric.rw.RW

case class DataDogSeries(unit: List[DataDogUnit] = Nil,
                         queryIndex: Int,
                         aggr: String = "",
                         metric: String,
                         tagSet: List[String],
                         expression: String,
                         scope: String,
                         interval: Long,
                         length: Int,
                         start: Long,
                         end: Long,
                         pointlist: List[Point],
                         displayName: String,
                         attributes: Json)

object DataDogSeries {
  implicit val rw: RW[DataDogSeries] = RW.gen
}