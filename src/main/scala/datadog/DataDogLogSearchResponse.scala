package datadog

import fabric.rw.RW

case class DataDogLogSearchResponse(data: List[DataDogLogData],
                                    links: DataDogLogLinks,
                                    meta: DataDogMetaData)

object DataDogLogSearchResponse {
  implicit val rw: RW[DataDogLogSearchResponse] = RW.gen
}