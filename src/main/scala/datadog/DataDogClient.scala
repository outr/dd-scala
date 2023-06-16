package datadog

import cats.effect.IO
import fabric._
import fabric.rw._
import spice.http.client.{HttpClient, RetryManager}
import spice.net.URL

import java.net.InetAddress
import scala.concurrent.duration.DurationInt
import scala.util.Try

case class DataDogClient(applicationName: String,
                         region: String,
                         apiKey: String,
                         applicationKey: String,
                         hostName: String = DataDogClient.getHostName) { ddClient =>
  private[datadog] val client: HttpClient = HttpClient
    .header("DD-API-KEY", apiKey)
    .header("DD-APPLICATION-KEY", applicationKey)
    .retryManager(RetryManager.delays(
      warnRetries = false,
      250.millis, 1.second, 5.seconds, 10.seconds, 1.minute, 5.minutes, 30.minutes
    ))

  private val sendEventURL: URL = URL.parse(s"https://api.$region.datadoghq.com/api/v1/events")

  def event(event: DataDogEvent): IO[Unit] = client
    .url(sendEventURL)
    .restful[DataDogEvent, Json](event)
    .map(_ => ())

  lazy val log: Logs = Logs(this)
  lazy val metrics: Metrics = Metrics(this)
}

object DataDogClient {
  private def getHostName: String = Try(Option(InetAddress.getLocalHost.getHostName))
    .toOption
    .flatten
    .getOrElse("<unknown>")
}