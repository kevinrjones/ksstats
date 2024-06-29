package com.ksstats.feature.recordsearch.feature.mainsearch.search.utils

import com.ksstats.core.presentation.StatsAppScreens

sealed class MainSearchType(val screen: StatsAppScreens) {
    data object Batting : MainSearchType(StatsAppScreens.BattingSearch)
    data object Bowling : MainSearchType(StatsAppScreens.BowlingSearch)
}