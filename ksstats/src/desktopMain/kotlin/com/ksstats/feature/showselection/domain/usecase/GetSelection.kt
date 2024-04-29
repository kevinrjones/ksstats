package com.ksstats.feature.showselection.domain.usecase

import com.ksstats.feature.showselection.domain.model.StatsSelectionItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetSelection {

    private val statsASelection = listOf(
        StatsSelectionItem("Batting"),
        StatsSelectionItem("Bowling"),
        StatsSelectionItem("Fielding"),
        StatsSelectionItem("Partnerships"),
        StatsSelectionItem("Players"),
        StatsSelectionItem("Officials"),
    )

    operator fun invoke(): Flow<StatsSelectionItem> {
        return flow {
            statsASelection.forEach {
                emit(it)
            }
        }
    }
}