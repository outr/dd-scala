package datadog

import fabric.rw.RW
import spice.net.URL

import scala.concurrent.duration.FiniteDuration

case class DataDogMetricRequest(metricType: Option[String],
                                includePercentiles: Option[Boolean],
                                window: Option[FiniteDuration],
                                tags: Option[String],
                        ) {

  def appendQueryParams(url: URL) : URL = {
    metricType match {
      case Some(t) =>
        url.withParam("filter[metric_type]", t, true)
    }

    includePercentiles match {
      case Some(p) =>
        url.withParam("filter[include_percentiles]", p.toString, true)
    }

    tags match {
      case Some(t) =>
        url.withParam("filter[include_percentiles]", t, true)
        url.withParam("filter[query]", "true", true)
    }

    window match {
      case Some(w) =>
        url.withParam("window[seconds]", w.toString(), true)
    }

    url
  }
}



object DataDogMetricRequest {}