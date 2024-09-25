package com.ksstats.feature.playerfieldingprimarystats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.domain.util.createAbbreviatedEnglishDateFormat
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.types.MatchType
import com.ksstats.feature.playerfieldingprimarystats.data.InningsByInningsFielding
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

class PlayerFieldingInningsByInningsScreenViewModel(
    private val playerFieldingPrimaryStatsUseCases: PlayerFieldingPrimaryStatsUseCases,
    private val summaryUseCase: SummaryUseCases,
    private val screen: StatsAppScreens,

    ) : ViewModel() {

    private val format = createAbbreviatedEnglishDateFormat()

    private val _inningsByInnings: MutableStateFlow<DatabaseResult<InningsByInningsFielding>> = MutableStateFlow(
        DatabaseResult(
            listOf(), 0
        )
    )
    val inningsByInnings: StateFlow<DatabaseResult<InningsByInningsFielding>> = _inningsByInnings.asStateFlow()

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
                when(screen) {
                    StatsAppScreens.FieldingInningsByInnings -> {
                        playerFieldingPrimaryStatsUseCases.getFieldingInningsByInnings(searchParameters)
                            .collect {
                                _inningsByInnings.value = it
                            }
                        _searching.value = false
                    }
                    StatsAppScreens.FieldingMatchTotals -> {
                        playerFieldingPrimaryStatsUseCases.getFieldingGetMatchTotals(searchParameters)
                            .collect {
                                _inningsByInnings.value = it
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