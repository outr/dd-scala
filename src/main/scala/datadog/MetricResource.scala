package datadog

import fabric.rw.RW

case class MetricResource(name: String, `type`: String)

object MetricResource {
  implicit val rw: RW[MetricResource] = RW.gen
}