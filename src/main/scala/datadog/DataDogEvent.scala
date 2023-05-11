package datadog

import fabric.rw.RW

case class DataDogEvent(aggregationKey: Option[String],
                        alertType: Option[String],
                        dateHappened: Option[Long],
                        deviceName: Option[String],
                        host: Option[String],
                        priority: Option[String],
                        relatedEventId: Option[Long],
                        sourceTypeName: Option[String],
                        tags: List[String],
                        text: String,
                        title: String)

object DataDogEvent {
  implicit val rw: RW[DataDogEvent] = RW.gen
}