package datadog

import fabric.rw.RW

trait DataDogLogMessage {
  def ddsource: String
  def ddtags: String
  def hostname: String
  def message: String
  def service: String
}