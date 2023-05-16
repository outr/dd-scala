package datadog

import fabric.rw.RW

case class DataDogMetrics(status: String,
                          resType: String,
                          respVersion: Int,
                          query: String,
                          fromDate: Long,
                          toDate: Long,
                          series: List[DataDogSeries],
                          message: String,
                          groupBy: List[String])

object DataDogMetrics {
  implicit val rw: RW[DataDogMetrics] = RW.gen
}