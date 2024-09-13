package com.ksstats.core.domain.util

import org.jetbrains.compose.resources.StringResource

data class Limits(val limitLabel: StringResource, val limitValues : Map<String,Int>)