package com.ksstats.feature.playerfieldingprimarystats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.domain.util.createAbbreviatedEnglishDateFormat
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.types.MatchType
import com.ksstats.feature.playerfieldingprimarystats.data.PrimaryFielding
import com.ksstats.feature.playerfieldingprimarystats.domain.usecase.PlayerFieldingPrimaryStatsUseCases
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
import kotlinx.datetime.format

class PlayerFieldingSummaryScreenViewModel(
    private val playerFieldingPrimaryStatsUseCases: PlayerFieldingPrimaryStatsUseCases,
    private val summaryUseCase: SummaryUseCases,
    private val screen: StatsAppScreens,

    ) : ViewModel() {

    private val format = createAbbreviatedEnglishDateFormat()

    private val _fieldingSummary: MutableStateFlow<DatabaseResult<PrimaryFielding>> = MutableStateFlow(
        DatabaseResult(
            listOf(), 0
        )
    )
    val fieldingSummary: StateFlow<DatabaseResult<PrimaryFielding>> = _fieldingSummary.asStateFlow()

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
                    StatsAppScreens.FieldingPlayerSummary -> {

                        playerFieldingPrimaryStatsUseCases.getFieldingSummary(searchParameters)
                            .collect {
                                _fieldingSummary.value = it
                            }
                        _searching.value = false
                    }
                    StatsAppScreens.FieldingSeriesAverages -> {

                        playerFieldingPrimaryStatsUseCases.getSeriesAverages(searchParameters)
                            .collect {
                                _fieldingSummary.value = it
                            }
                        _searching.value = false
                    }

                    StatsAppScreens.FieldingGroundAverages -> {
                        playerFieldingPrimaryStatsUseCases.getGroundAverages(searchParameters)
                            .collect {
                                _fieldingSummary.value = it
                            }
                        _searching.value = false
                    }
                    StatsAppScreens.FieldingByHostCountry -> {
                        playerFieldingPrimaryStatsUseCases.getByHostCountry(searchParameters)
                            .collect {
                                _fieldingSummary.value = it
                            }
                        _searching.value = false
                    }
                    StatsAppScreens.FieldingByOppositionTeam -> {
                        playerFieldingPrimaryStatsUseCases.getByOppositionTeam(searchParameters)
                            .collect {
                                _fieldingSummary.value = it
                            }
                        _searching.value = false
                    }
                    StatsAppScreens.FieldingByYear -> {
                        playerFieldingPrimaryStatsUseCases.getByYearOfMatchStart(searchParameters)
                            .collect {
                                _fieldingSummary.value = it
                            }
                        _searching.value = false
                    }
                    StatsAppScreens.FieldingBySeason -> {
                        playerFieldingPrimaryStatsUseCases.getBySeason(searchParameters)
                            .collect {
                                _fieldingSummary.value = it
                            }
                        _searching.value = false
                    }

                    else -> {
                        _searching.value = false
                        // todo: add logging
                        println("Invalid screen")
                    }
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

}