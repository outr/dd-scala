package datadog

import fabric._
import fabric.define.DefType
import fabric.rw._

sealed trait MetricType

object MetricType {
  lazy val all: Vector[MetricType] = Vector(Unspecified, Count, Rate, Gauge)
  implicit val rw: RW[MetricType] = RW.from[MetricType](
    r = (t: MetricType) => all.indexOf(t),
    w = (json: Json) => all(json.asInt),
    d = DefType.Int
  )

  case object Unspecified extends MetricType
  case object Count extends MetricType
  case object Rate extends MetricType
  case object Gauge extends MetricType
}