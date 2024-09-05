package com.ksstats.core.types

import kotlinx.datetime.Clock

@JvmInline
value class EpochSeconds(val value: Long)  {

    companion object {
        fun minimum() : EpochSeconds =
            EpochSeconds(-8520336000)

        fun now(): EpochSeconds = EpochSeconds(Clock.System.now().toEpochMilliseconds()/1000)
    }

}

fun Long.toEpochSeconds(): EpochSeconds = EpochSeconds(this)
