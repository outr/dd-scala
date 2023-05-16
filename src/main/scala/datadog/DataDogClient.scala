package datadog

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import fabric.Json
import fabric.rw.RW
import scribe.LogRecord
import scribe.data.MDC
import scribe.handler.LogHandler
import spice.http.client.HttpClient
import spice.net.URL

import java.net.InetAddress
import scala.concurrent.duration.DurationInt
import scala.util.Try

case class DataDogClient(applicationName: String,
                         region: String,
                         apiKey: String,
                         hostName: String = DataDogClient.getHostName) { ddClient =>
  private val client: HttpClient = HttpClient
    .header("DD-API-KEY", apiKey)
    .retries(4)
    .retryDelay(250.millis)

  private val sendEventURL: URL = URL.parse(s"https://api.$region.datadoghq.com/api/v1/events")
  private val sendLogsURL: URL = URL.parse(s"https://http-intake.logs.$region.datadoghq.com/api/v2/logs")
  private val getMetricsURL: URL = URL.parse(s"https://api.$region.datadoghq.com/api/v2/metrics")

  def event(event: DataDogEvent): IO[Unit] = client
    .url(sendEventURL)
    .restful[DataDogEvent, Json](event)
    .map(_ => ())

  def log[Message <: DataDogLogMessage](message: Message)
                                       (implicit rw: RW[Message]): IO[Unit] = client
    .url(sendLogsURL)
    .restful[Message, Json](message)
    .map(_ => ())

  def log(record: LogRecord): IO[Unit] = {
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
      hostname = hostName,
      message = record.logOutput.plainText,
      service = applicationName,
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
    log(message)
  }
  def createLogHandler()(implicit runtime: IORuntime): LogHandler = (record: LogRecord) =>
    ddClient.log(record).unsafeRunAndForget()

  def getMetrics(dataDogMetricRequest: DataDogMetricRequest): IO[Json] = {
    val metricsURL: URL = dataDogMetricRequest.appendQueryParams(getMetricsURL)

    client
      .url(metricsURL)
      .call[Json]
  }
}

object DataDogClient {
  private def getHostName: String = Try(Option(InetAddress.getLocalHost.getHostName))
    .toOption
    .flatten
    .getOrElse("<unknown>")
}