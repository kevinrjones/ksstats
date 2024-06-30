package com.ksstats.feature.playerbowlingsummary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.types.MatchType
import com.ksstats.feature.playerbowlingsummary.data.BowlingSummary
import com.ksstats.feature.playerbowlingsummary.domain.usecase.PlayerBowlingSummaryUseCases
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
    private val playerBowlingSummaryUseCases: PlayerBowlingSummaryUseCases,
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

    private val _bowlingSummary: MutableStateFlow<List<BowlingSummary>> = MutableStateFlow(listOf())
    val bowlingSummary: StateFlow<List<BowlingSummary>> = _bowlingSummary.asStateFlow()

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
                playerBowlingSummaryUseCases.getBowlingSummary(searchParameters)
                    .collect {
                        _bowlingSummary.value = it
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

    fun getFivesLimitForMatchType(matchType: MatchType): Int
        = if(matchType.isMultiInningsType()) 4 else 5


    fun calculatePageNumber(startRow: Int, pageSize: Int): Int {
        return (startRow/pageSize) + 1
    }


}