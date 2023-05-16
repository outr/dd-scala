package datadog

import fabric._
import fabric.define.DefType
import fabric.rw.RW

case class Point(x: Double, y: Double)

object Point {
  implicit val rw: RW[Point] = RW.from[Point](
    r = (point: Point) => arr(point.x, point.y),
    w = (json: Json) => {
      val v = json.asVector
      Point(v(0).asDouble, v(1).asDouble)
    },
    d = DefType.Arr(DefType.Dec)
  )
}