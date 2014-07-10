package at.droelf.backend

import org.joda.time.{DateTime, DateTimeZone, LocalDateTime}


trait DateTimeUtil {
  def utcWithTimeZoneToDateTime(utcLocalDateTime: LocalDateTime, timeZone: DateTimeZone): DateTime = utcLocalDateTime.toDateTime(DateTimeZone.UTC).withZone(timeZone)
  def dateTimeToUtcLocalTime(dateTime: DateTime): LocalDateTime = dateTime.withZone(DateTimeZone.UTC).toLocalDateTime
}
