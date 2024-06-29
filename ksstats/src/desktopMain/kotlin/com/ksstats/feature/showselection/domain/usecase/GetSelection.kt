package com.ksstats.feature.showselection.domain.usecase

import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.feature.showselection.domain.model.StatsSelectionItem
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.batting
import com.ksstats.ksstats.generated.resources.bowling
import com.ksstats.ksstats.generated.resources.fielding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetSelection {

    private val statsSelection = listOf(
        StatsSelectionItem(Res.string.batting, StatsAppScreens.BattingSearch),
        StatsSelectionItem(Res.string.bowling, StatsAppScreens.BowlingSearch),
        StatsSelectionItem(Res.string.fielding, StatsAppScreens.FieldingSearch),
        StatsSelectionItem(Res.string.partnerships, StatsAppScreens.PartnershipSearch),
        StatsSelectionItem(Res.string.players, StatsAppScreens.PlayersSearch),
        StatsSelectionItem(Res.string.officials, StatsAppScreens.OfficialsSearch),
    )

    operator fun invoke(): Flow<StatsSelectionItem> {
        return flow {
            statsSelection.forEach {
                emit(it)
            }
        }
    }
}