package datadog

import fabric.rw.RW

case class DataDogMetaData(page: DataDogPage, elapsed: Int, requestId: String, status: String)

object DataDogMetaData {
  implicit val rw: RW[DataDogMetaData] = RW.gen
}