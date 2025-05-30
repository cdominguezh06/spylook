package com.cogu.spylook.model.utils.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateConverters {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss")

    @JvmStatic
    @TypeConverter
    fun fromString(value: String): LocalDate {
        return LocalDate.parse(value, dateFormatter)
    }

    @JvmStatic
    @TypeConverter
    fun toString(date: LocalDate): String {
        return date.format(dateFormatter)
    }

    fun toCustomString(date: LocalDate, dateFormatter: DateTimeFormatter): String {
        return date.format(dateFormatter)
    }

    @JvmStatic
    @TypeConverter
    fun toDateTimeString(dateTime: LocalDateTime): String {
        return dateTime.format(dateTimeFormatter)
    }

    @JvmStatic
    @TypeConverter
    fun fromDateTimeString(value: String): LocalDateTime {
        if (value.contains("|")) {
            return LocalDateTime.parse(value, dateTimeFormatter)
        }
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
