package datadog

import fabric.rw._

case class DataDogLogSearchResponse(data: List[DataDogLogData],
                                    links: DataDogLogLinks = DataDogLogLinks(None),
                                    meta: DataDogMetaData)

object DataDogLogSearchResponse {
  implicit val rw: RW[DataDogLogSearchResponse] = RW.gen
}