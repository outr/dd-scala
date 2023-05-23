package datadog

import fabric.rw.RW

case class DataDogEvent(aggregationKey: Option[String] = None,
                        alertType: Option[String] = None,
                        dateHappened: Option[Long] = None,
                        deviceName: Option[String] = None,
                        host: Option[String] = None,
                        priority: Option[String] = None,
                        relatedEventId: Option[Long] = None,
                        sourceTypeName: Option[String] = None,
                        tags: List[String] = Nil,
                        text: String,
                        title: String)

object DataDogEvent {
  implicit val rw: RW[DataDogEvent] = RW.gen
}