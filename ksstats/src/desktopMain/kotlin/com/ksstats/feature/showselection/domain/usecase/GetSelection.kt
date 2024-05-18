package com.ksstats.feature.showselection.domain.usecase

import com.ksstats.feature.showselection.domain.model.StatsSelectionItem
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.batting
import com.ksstats.ksstats.generated.resources.bowling
import com.ksstats.ksstats.generated.resources.fielding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.compose.resources.stringResource


class GetSelection {

    private val statsASelection = listOf(
        StatsSelectionItem(Res.string.batting ),
        StatsSelectionItem(Res.string.bowling),
        StatsSelectionItem(Res.string.fielding),
        StatsSelectionItem(Res.string.partnerships),
        StatsSelectionItem(Res.string.players),
        StatsSelectionItem(Res.string.officials),
    )

    operator fun invoke(): Flow<StatsSelectionItem> {
        return flow {
            statsASelection.forEach {
                emit(it)
            }
        }
    }
}