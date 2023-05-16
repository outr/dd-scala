package datadog

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import fabric.Json
import fabric.filter.SnakeToCamelFilter
import fabric.rw._
import scribe.LogRecord
import scribe.data.MDC
import scribe.handler.LogHandler
import spice.http.client.HttpClient
import spice.net.URL

import scala.concurrent.duration.DurationInt

case class Logs(ddc: DataDogClient) {
  private def client: HttpClient = ddc.client
  private def region: String = ddc.region

  private val sendURL: URL = URL.parse(s"https://http-intake.logs.$region.datadoghq.com/api/v2/logs")
  private val searchURL: URL = URL.parse(s"https://api.$region.datadoghq.com/api/v2/logs/events/search")

  def apply[Message <: DataDogLogMessage](message: Message)
                                         (implicit rw: RW[Message]): IO[Unit] = client
    .url(sendURL)
    .restful[Message, Json](message)
    .map(_ => ())

  def apply(record: LogRecord): IO[Unit] = {
    val mdc = MDC.map.map {
      case (key, function) => key -> function().toString
    }
    val data = record.data.map {
      case (key, function) => key -> function().toString
    }
    val tags = (mdc ++ data).map {
      case (key, value) => s"$key:$value"
    }.mkString(",")
    val message = StandardDataDogLogMessage(
      ddsource = "dd-scala",
      ddtags = tags,
      hostname = ddc.hostName,
      message = record.logOutput.plainText,
      service = ddc.applicationName,
      messages = record.messages.map(_.logOutput.plainText),
      level = record.level.name,
      value = record.levelValue,
      fileName = record.fileName,
      className = record.className,
      methodName = record.methodName,
      line = record.line,
      column = record.column,
      thread = record.thread.getName,
      timestamp = record.timeStamp,
      mdc = mdc,
      data = data
    )
    apply(message)
  }

  def search(query: String = "*",
             from: Long = System.currentTimeMillis() - 24.hours.toMillis,
             to: Long = System.currentTimeMillis(),
             indexes: List[String] = List("main"),
             reverseSort: Boolean = false,
             cursor: Option[String] = None,
             limit: Int = 100): IO[DataDogLogSearchResponse] = client
    .url(searchURL)
    .restful[DataDogLogSearchRequest, Json](DataDogLogSearchRequest(
      filter = DataDogLogFilter(
        query = query,
        indexes = indexes,
        from = from,
        to = to
      ),
      sort = if (reverseSort) "-timestamp" else "timestamp",
      page = Page(
        cursor = cursor,
        limit = limit
      )
    ))
    .map { json =>
      json.filterOne(SnakeToCamelFilter).as[DataDogLogSearchResponse]
    }

  def logHandler()(implicit runtime: IORuntime): LogHandler = (record: LogRecord) =>
    apply(record).unsafeRunAndForget()
}