package com.ksstats.shared

import kotlinx.datetime.*

fun LocalDate.Companion.now(): LocalDate {
    val now = Clock.System.now()
    val tz = TimeZone.currentSystemDefault()
    return now.toLocalDateTime(tz).date
}

fun Long.fromMilliseconds(): LocalDate {
    val instant = Instant.fromEpochMilliseconds(this)
    val tz = TimeZone.currentSystemDefault()
    return instant.toLocalDateTime(tz).date
}

fun LocalDate.toSeconds(): Long = this.atTime(0,0,0).toInstant(TimeZone.UTC).toEpochMilliseconds() / 1000
