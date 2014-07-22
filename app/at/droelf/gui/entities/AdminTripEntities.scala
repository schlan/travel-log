package at.droelf.gui.entities


import org.joda.time.LocalDate

case class AdminDayTour(date: LocalDate, startPoint: Long, endPoint: Long, description: String, weatherCond: String, roadCond: String)
