package datadog

import fabric.rw.RW

case class DataDogUnit(family: String, id: Int, name: String, shortName: String, plural: String, scaleFactor: Double)

object DataDogUnit {
  implicit val rw: RW[DataDogUnit] = RW.gen
}