package datadog

import fabric.rw.RW

case class DataDogLogSearchRequest(filter: DataDogLogFilter, sort: String, page: Page)

object DataDogLogSearchRequest {
  implicit val rw: RW[DataDogLogSearchRequest] = RW.gen
}