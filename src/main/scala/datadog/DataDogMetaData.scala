package datadog

import fabric.rw.RW

case class DataDogMetaData(page: DataDogPage = DataDogPage(None), elapsed: Int, requestId: String, status: String)

object DataDogMetaData {
  implicit val rw: RW[DataDogMetaData] = RW.gen
}