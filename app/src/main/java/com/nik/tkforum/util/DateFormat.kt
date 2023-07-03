package com.nik.tkforum.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateFormat {

    private const val DATE_YEAR_MONTH_DAY_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"

    private val locale
        get() = SystemConfiguration.currentLocale

    private fun convertToDate(dateString: String): Date? {
        return SimpleDateFormat(DATE_YEAR_MONTH_DAY_TIME_PATTERN, locale).parse(dateString)
    }

    fun convertToDisplayDate(dateString: String): String? {
        return try {
            val date = convertToDate(dateString) ?: return null
            SimpleDateFormat("yyyy.MM.dd", locale).format(date)
        } catch (e: ParseException) {
            null
        }
    }
}