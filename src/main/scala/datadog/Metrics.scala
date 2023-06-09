package datadog

import cats.effect.IO
import fabric._
import fabric.filter.{CamelToSnakeFilter, RemoveNullsFilter, SnakeToCamelFilter}
import fabric.io.JsonFormatter
import fabric.rw._
import spice.http.client.HttpClient
import spice.net._
import spice.util.{BufferManager, BufferQueue}
import scribe.cats.{io => logger}

case class Metrics(ddc: DataDogClient) {
  private def client: HttpClient = ddc.client
  private def region: String = ddc.region
  private val listURL: URL = URL.parse(s"https://api.$region.datadoghq.com/api/v2/metrics")
  private val queryURL: URL = URL.parse(s"https://api.$region.datadoghq.com/api/v1/query")
  private val submitURL: URL = URL.parse(s"https://api.$region.datadoghq.com/api/v2/series")

  def list(): IO[List[String]] = client.url(listURL).call[Json].map { json =>
    json("data").asVector.toList.map(_("id").asString)
  }

  def query(from: Long, to: Long, query: String): IO[DataDogMetrics] = {
    val url = queryURL
      .withParam("from", (from / 1000L).toString)
      .withParam("to", (to / 1000L).toString)
      .withParam("query", query)
    client.url(url).call[Json].map { json =>
      scribe.info(s"Metrics Results: ${JsonFormatter.Default(json)}")
      json
        .filterOne(RemoveNullsFilter)
        .filterOne(SnakeToCamelFilter)
        .as[DataDogMetrics]
    }
  }

  def submit(series: List[DataDogMetricsSeries]): IO[DataDogResponse] = {
    val request = obj(
      "series" -> series.json.filterOne(CamelToSnakeFilter)
    )
    client
      .url(submitURL)
      .restful[Json, DataDogResponse](request)
  }

  def monitor(buffer: BufferManager): BufferQueue[DataDogMetricsSeries] = buffer.create[DataDogMetricsSeries](list => {
    submit(list)
      .flatMap { response =>
        if (response.errors.nonEmpty) {
          logger.error(s"Error response while sending metrics to DataDog: ${response.errors.mkString(", ")}")
        } else {
          IO.unit
        }
      }
  })
}