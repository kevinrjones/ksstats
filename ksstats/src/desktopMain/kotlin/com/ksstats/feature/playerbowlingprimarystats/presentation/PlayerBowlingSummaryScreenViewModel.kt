package com.ksstats.feature.playerbowlingprimarystats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.types.MatchType
import com.ksstats.feature.playerbowlingprimarystats.data.PrimaryBowling
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.PlayerBowlingPrimaryStatsUseCases
import com.ksstats.feature.summary.domain.model.SummaryResult
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.shared.fromSeconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

class PlayerBowlingSummaryScreenViewModel(
    private val playerBowlingPrimaryStatsUseCases: PlayerBowlingPrimaryStatsUseCases,
    private val summaryUseCase: SummaryUseCases,
    private val screen: StatsAppScreens,

    ) : ViewModel() {

    private val format = LocalDate.Format {
        date(
            LocalDate.Format {
                monthName(MonthNames.ENGLISH_ABBREVIATED)
                char(' ')
                dayOfMonth()
                chars(" ")
                year()
            }
        )
    }

    private val _primaryBowling: MutableStateFlow<DatabaseResult<PrimaryBowling>> = MutableStateFlow(
        DatabaseResult(listOf(), 0)
    )
    val primaryBowling: StateFlow<DatabaseResult<PrimaryBowling>> = _primaryBowling.asStateFlow()

    private val _searching = MutableStateFlow<Boolean>(false)
    val searching = _searching.asStateFlow()

    private val _summary = MutableStateFlow<SummaryResult>(
        SummaryResult(
            team = "",
            opponents = "",
            ground = "",
            hostCountry = "",
            matchType = MatchType.default(),
            competition = MatchType.default()
        )
    )
    val summary = _summary.asStateFlow()

    fun getSearchResults(searchParameters: SearchParameters) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _searching.value = true
                when (screen) {
                    StatsAppScreens.BowlingPlayerSummary -> {

                        playerBowlingPrimaryStatsUseCases.getBowlingSummary(searchParameters)
                            .collect {
                                _primaryBowling.value = it
                            }
                        _searching.value = false
                    }

                    StatsAppScreens.BowlingSeriesAverages -> {
                        playerBowlingPrimaryStatsUseCases.getSeriesAverages(searchParameters)
                            .collect {
                                _primaryBowling.value = it
                            }
                        _searching.value = false
                    }

                    StatsAppScreens.BowlingGroundAverages -> TODO()
                    StatsAppScreens.BowlingByHostCountry -> TODO()
                    StatsAppScreens.BowlingByOppositionTeam -> TODO()
                    StatsAppScreens.BowlingByYear -> TODO()
                    StatsAppScreens.BowlingBySeason -> TODO()
                    else -> TODO()
                }
            }
        }
    }

    fun getSummary(searchParameters: SummarySearchParameters, startDate: Long, endDate: Long) {
        val sd = startDate.fromSeconds().format(format)
        val ed = endDate.fromSeconds().format(format)

        viewModelScope.launch {
            summaryUseCase.getSummaryUseCase(searchParameters).collect {
                _summary.value = it
            }
        }
    }

    fun getFivesLimitForMatchType(matchType: MatchType): Int = if (matchType.isMultiInningsType()) 4 else 5


}