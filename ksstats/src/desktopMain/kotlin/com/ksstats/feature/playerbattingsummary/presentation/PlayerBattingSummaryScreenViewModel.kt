package com.ksstats.feature.playerbattingsummary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.types.MatchType
import com.ksstats.feature.playerbattingsummary.data.BattingSummary
import com.ksstats.feature.playerbattingsummary.domain.usecase.PlayerBattingSummaryUseCases
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

class PlayerBattingSummaryScreenViewModel(
    private val playerBattingSummaryUseCases: PlayerBattingSummaryUseCases,
    private val summaryUseCase: SummaryUseCases,

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

    private val _battingSummary: MutableStateFlow<List<BattingSummary>> = MutableStateFlow(listOf())
    val battingSummary: StateFlow<List<BattingSummary>> = _battingSummary.asStateFlow()

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
                playerBattingSummaryUseCases.getBattingSummary(searchParameters)
                    .collect {
                        _battingSummary.value = it
                    }
                _searching.value = false
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