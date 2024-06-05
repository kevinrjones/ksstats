package com.ksstats.feature.playersummary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.shared.data.BattingSearchResults
import com.ksstats.feature.playersummary.domain.usecase.PlayerSummaryUseCases
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

class PlayerSummaryScreenViewModel(
    private val playerSummaryUseCases: PlayerSummaryUseCases,
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

    private val _battingSearchResults: MutableStateFlow<List<BattingSearchResults>> = MutableStateFlow(listOf())
    val battingSearchResults: StateFlow<List<BattingSearchResults>> = _battingSearchResults.asStateFlow()

    private val _searching = MutableStateFlow<Boolean>(false)
    val searching = _searching.asStateFlow()

    private val _summary = MutableStateFlow<SummaryResult>(
        SummaryResult(
            team = "",
            opponents = "",
            ground = "",
            hostCountry = "",
            matchType = "",
            competition = ""
        )
    )
    val summary = _summary.asStateFlow()

    fun getSearchResults(searchParameters: SearchParameters) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _searching.value = true
                playerSummaryUseCases.getPlayerSummary(searchParameters)
                    .collect {
                        _battingSearchResults.value = it
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