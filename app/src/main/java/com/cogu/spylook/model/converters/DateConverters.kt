package com.cogu.spylook.model.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateConverters {
    private val formatter: DateTimeFormatter? = DateTimeFormatter.ISO_LOCAL_DATE

    @JvmStatic
    @TypeConverter
    fun fromString(value: String?): LocalDate? {
        return if (value == null) null else LocalDate.parse(value, formatter)
    }

    @JvmStatic
    @TypeConverter
    fun toString(date: LocalDate?): String? {
        return if (date == null) null else date.format(formatter)
    }
}
