package datadog

import fabric.rw.RW

case class DataDogMetricsSeries(metric: String,
                                points: List[MetricPoint],
                                `type`: MetricType = MetricType.Unspecified,
                                interval: Option[Long] = None,
                                sourceTypeName: Option[String] = None,
                                tags: List[String] = Nil,
                                unit: Option[String] = None,
                                resources: List[MetricResource] = Nil)

object DataDogMetricsSeries {
  implicit val rw: RW[DataDogMetricsSeries] = RW.gen
}