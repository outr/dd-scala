package datadog

import fabric.rw._

case class MetricResource(name: String, `type`: String)

object MetricResource {
  implicit val rw: RW[MetricResource] = RW.gen
}