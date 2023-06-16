package datadog

import fabric.rw._

case class DataDogLogData(id: String,
                          `type`: String,
                          attributes: DataDogLogAttributes)

object DataDogLogData {
  implicit val rw: RW[DataDogLogData] = RW.gen
}