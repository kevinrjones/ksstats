package com.ksstats.feature.showselection.domain.usecase

import com.ksstats.core.presentation.StatsAppScreen
import com.ksstats.feature.showselection.domain.model.StatsSelectionItem
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.batting
import com.ksstats.ksstats.generated.resources.bowling
import com.ksstats.ksstats.generated.resources.fielding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetSelection {

    private val statsASelection = listOf(
        StatsSelectionItem(Res.string.batting, StatsAppScreen.BattingSearch),
        StatsSelectionItem(Res.string.bowling, StatsAppScreen.BowingSearch),
        StatsSelectionItem(Res.string.fielding, StatsAppScreen.FieldingSearch),
        StatsSelectionItem(Res.string.partnerships, StatsAppScreen.PartnershipSearch),
        StatsSelectionItem(Res.string.players, StatsAppScreen.PlayersSearch),
        StatsSelectionItem(Res.string.officials, StatsAppScreen.OfficialsSearch),
    )

    operator fun invoke(): Flow<StatsSelectionItem> {
        return flow {
            statsASelection.forEach {
                emit(it)
            }
        }
    }
}