package datadog

import cats.effect.IO
import fabric.Json
import fabric.filter.{RemoveNullsFilter, SnakeToCamelFilter}
import fabric.rw._
import spice.http.client.HttpClient
import spice.net.URL

case class Metrics(ddc: DataDogClient) {
  private def client: HttpClient = ddc.client
  private def region: String = ddc.region
  private val listURL: URL = URL.parse(s"https://api.$region.datadoghq.com/api/v2/metrics")
  private val queryURL: URL = URL.parse(s"https://api.$region.datadoghq.com/api/v1/query")

  def list(): IO[List[String]] = client.url(listURL).call[Json].map { json =>
    json("data").asVector.toList.map(_("id").asString)
  }

  def query(from: Long, to: Long, query: String): IO[DataDogMetrics] = {
    val url = queryURL
      .withParam("from", (from / 1000L).toString)
      .withParam("to", (to / 1000L).toString)
      .withParam("query", query)
    client.url(url).call[Json].map { json =>
      json
        .filterOne(RemoveNullsFilter)
        .filterOne(SnakeToCamelFilter)
        .as[DataDogMetrics]
    }
  }
}