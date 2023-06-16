package datadog

import fabric._
import fabric.define.DefType
import fabric.rw._

case class MetricPoint(timestamp: Long, value: Double)

object MetricPoint {
  implicit val rw: RW[MetricPoint] = RW.from[MetricPoint](
    r = (p: MetricPoint) => obj(
      "timestamp" -> p.timestamp / 1000L,
      "value" -> p.value
    ),
    w = (json: Json) => MetricPoint(
      timestamp = json("timestamp").asLong * 1000L,
      value = json("value").asDouble
    ),
    d = DefType.Obj(
      "timestamp" -> DefType.Int,
      "value" -> DefType.Dec
    )
  )
}