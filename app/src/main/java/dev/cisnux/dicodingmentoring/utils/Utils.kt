package dev.cisnux.dicodingmentoring.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val HTTP_BASE_URL = "https://www.mentoring.cisnux.xyz/"
const val WS_BASE_URL = "wss://www.mentoring.cisnux.xyz/ws"

fun String.isEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"
    return Regex(emailRegex).matches(this)
}

fun String.isPasswordSecure(): Boolean = this.trim().length >= 8

fun String.isValidAbout(maxLength: Int) = length <= maxLength

fun String.asList(): List<String> = trimIndent()
    .lines().map { it.trim() }
    .filter { it.isNotBlank() }

fun Long.withDateFormat(): String {
    val date = Date(this)
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}

fun Long.withTimeFormat(): String {
    val date = Date(this)
    return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, Locale.getDefault())
        .format(date)
}

fun combineDateAndTime(date: Long, time: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date

    val dateDay = calendar.get(Calendar.DAY_OF_MONTH)
    val dateMonth = calendar.get(Calendar.MONTH)
    val dateYear = calendar.get(Calendar.YEAR)

    calendar.timeInMillis = time
    calendar.set(Calendar.DAY_OF_MONTH, dateDay)
    calendar.set(Calendar.MONTH, dateMonth)
    calendar.set(Calendar.YEAR, dateYear)

    return calendar.time.time
}
