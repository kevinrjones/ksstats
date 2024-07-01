package com.ksstats.core.types

import com.ksstats.feature.recordsearch.domain.model.MatchTypeEntity


@JvmInline
value class MatchType(val value: String) {
    fun isMultiInningsType(): Boolean {
        val multiInningsMatchTypes = listOf("t", "f", "wt", "wf", "sec", "minc", "ut")

        return multiInningsMatchTypes.contains(this.value)
    }

    companion object {
        fun default(): MatchType = MatchType("")
    }

    override fun toString(): String = value
}

fun String.toMatchType(): MatchType  = MatchType(this)

fun MatchTypeEntity.toMatchType() : MatchType = MatchType(this.type)

