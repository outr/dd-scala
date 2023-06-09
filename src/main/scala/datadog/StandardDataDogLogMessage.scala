package datadog

import fabric.rw._

case class StandardDataDogLogMessage(ddsource: String,
                                     ddtags: String,
                                     hostname: String,
                                     message: String,
                                     error: Option[DataDogError],
                                     service: String,
                                     messages: List[String],
                                     level: String,
                                     value: Double,
                                     fileName: String,
                                     className: String,
                                     methodName: Option[String],
                                     line: Option[Int],
                                     column: Option[Int],
                                     thread: String,
                                     timestamp: Long,
                                     mdc: Map[String, String],
                                     data: Map[String, String]) extends DataDogLogMessage

object StandardDataDogLogMessage {
  implicit val rw: RW[StandardDataDogLogMessage] = RW.gen
}