package com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.core.types.toMatchType
import com.ksstats.feature.recordsearch.domain.model.*
import com.ksstats.feature.recordsearch.domain.usecase.RecordSearchUseCases
import com.ksstats.shared.toSeconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class MainSearchViewModel(
    val recordSearchUseCases: RecordSearchUseCases,
    val limits: Map<String, Int>,
) : ViewModel() {

    var job: Job? = null
    var _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    init {
        job = this.viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    loaded = false,
                    minimumValue = limits.getOrDefault(SearchViewFormat.PlayerSummary.name, 100)
                )
            }

            withContext(Dispatchers.IO) {
                recordSearchUseCases.initialiseSearch(Country.defaultCountry()).collect { searchResult ->
                    initialiseStateFromSearchData(searchResult)
                }
            }

            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(loaded = true)
            }
        }
    }

    private fun initialiseStateFromSearchData(searchResult: SearchData) {
        _state.value = _state.value.copy(
            matchTypes = searchResult.matchTypes,
            selectedMatchType = searchResult.matchTypes[0],
            pageSizes = searchResult.pageSizes,
            selectedPageSize = searchResult.pageSizes[0],
            competitions = searchResult.competitions,
            selectedCompetition = searchResult.competitions[0],
            teams = searchResult.teams,
            selectedTeam = searchResult.teams[0],
            selectedOpposition = searchResult.teams[0],
            grounds = searchResult.grounds,
            selectedGround = searchResult.grounds[0],

            countries = searchResult.countries,
            selectedCountry = searchResult.countries[0],
            startDate = Instant.fromEpochSeconds(searchResult.startAndEndDate.start).toLocalDateTime(TimeZone.UTC).date,
            endDate = Instant.fromEpochSeconds(searchResult.startAndEndDate.end).toLocalDateTime(
                TimeZone.UTC
            ).date,
            seriesDates = searchResult.seriesDates,
            selectedSeriesDate = searchResult.seriesDates[0]
        )
    }


    fun uiEvent(mainSearchEvent: MainSearchEvent) {
        when (mainSearchEvent) {
            is MainSearchEvent.StartDateChangedEvent -> {
                mainSearchEvent.date?.let {
                    _state.value = _state.value.copy(startDate = it)
                }

            }

            is MainSearchEvent.EndDateChangedEvent -> {
                mainSearchEvent.date?.let {
                    _state.value = _state.value.copy(endDate = it)
                }
            }

            is MainSearchEvent.MatchTypeSelectionEvent -> {
                val matchType =
                    state.value.matchTypes.find { it.type == mainSearchEvent.menuState.key }
                        ?: throw Exception("Invalid Match Type: ${mainSearchEvent.menuState}")

                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        _state.value = _state.value.copy(loaded = false)
                        recordSearchUseCases.getCompetitionsForMatchTypeAndCountry(
                            matchType.toMatchType(),
                            state.value.selectedCountry
                        )
                            .collect { result ->
                                val searchResult =
                                    result.copy(
                                        matchTypes = state.value.matchTypes,
                                        pageSizes = state.value.pageSizes,
                                    )

                                // copy everything over but reset the matchtype
                                initialiseStateFromSearchData(searchResult)
                                _state.value = _state.value.copy(selectedMatchType = matchType)
                            }
                        _state.value = _state.value.copy(loaded = true)
                    }
                }

            }

            is MainSearchEvent.CompetitionSelectionEvent -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val matchType = state.value.selectedMatchType
                        val competition =
                            state.value.competitions.find { it.subType == mainSearchEvent.menuState.key }
                                ?: throw Exception("Invalid Competition: ${mainSearchEvent.menuState}")
                        _state.value = _state.value.copy(loaded = false)

                        recordSearchUseCases.getSearchDataForCompetitionAndCountry(
                            competition,
                            state.value.selectedCountry
                        )
                            .collect { result ->

                                val searchResult =
                                    result.copy(
                                        matchTypes = state.value.matchTypes,
                                        pageSizes = state.value.pageSizes,
                                        competitions = state.value.competitions
                                    )

                                initialiseStateFromSearchData(searchResult)
                                // copy everything over but reset the matchtype and competition
                                _state.value = _state.value.copy(
                                    selectedCompetition = competition,
                                    selectedMatchType = matchType,
                                    loaded = true
                                )
                            }
                    }
                }
            }

            is MainSearchEvent.MinimumValueChangedEvent -> {
                if (mainSearchEvent.value.isEmpty()) {
                    _state.value = _state.value.copy(minimumValue = 0)
                    return
                } else if (mainSearchEvent.value.toIntOrNull() == null) {
                    return
                }
                // todo: Error handling
                _state.value = _state.value.copy(minimumValue = mainSearchEvent.value.toInt())

            }

            is MainSearchEvent.CountrySelectionEvent -> {
                viewModelScope.launch {

                    val matchType = _state.value.selectedMatchType
                    val competition = _state.value.selectedCompetition

                    withContext(Dispatchers.IO) {
                        recordSearchUseCases.getTeamsAndGroundsForCompetitionAndCountry(
                            competition,
                            _state.value.selectedCountry
                        ).collect { result ->
                            val searchResult =
                                result.copy(
                                    matchTypes = _state.value.matchTypes,
                                    pageSizes = _state.value.pageSizes,
                                    competitions = _state.value.competitions,
                                    countries = _state.value.countries,
                                    seriesDates = _state.value.seriesDates,
                                    startAndEndDate = StartEndDate(
                                        _state.value.startDate.toSeconds(),
                                        _state.value.endDate.toSeconds()
                                    )
                                )

                            initialiseStateFromSearchData(searchResult)
                            // copy everything over but reset the matchtype and competition
                            _state.value = _state.value.copy(
                                selectedMatchType = matchType,
                                selectedCompetition = competition,
                                selectedCountry = Country(
                                    mainSearchEvent.menuState.key.toInt(),
                                    mainSearchEvent.menuState.value
                                )
                            )

                        }

                    }
                }
            }

            is MainSearchEvent.GroundSelectionEvent -> {
                _state.value = _state.value.copy(
                    selectedGround =
                    Ground(mainSearchEvent.menuState.key.toInt(), mainSearchEvent.menuState.value)
                )
            }

            is MainSearchEvent.TeamSelectionEvent -> {
                _state.value = _state.value.copy(
                    selectedTeam = Team(mainSearchEvent.menuState.key.toInt(), mainSearchEvent.menuState.value)
                )
            }

            is MainSearchEvent.OpponentSelectionEvent -> {
                _state.value = _state.value.copy(
                    selectedOpposition =
                    Team(mainSearchEvent.menuState.key.toInt(), mainSearchEvent.menuState.value)
                )
            }

            is MainSearchEvent.SeriesDateSelectionEvent -> {
                _state.value = _state.value.copy(
                    selectedSeriesDate = mainSearchEvent.menuState.value
                )
            }


            is MainSearchEvent.ResultSelectionEvent -> {
                var matchResult = if (mainSearchEvent.won) 1 else 0
                matchResult = if (mainSearchEvent.lost) matchResult + 2 else matchResult
                matchResult = if (mainSearchEvent.drawn) matchResult + 4 else matchResult
                matchResult = if (mainSearchEvent.tied) matchResult + 8 else matchResult

                _state.value = _state.value.copy(matchResult = matchResult)

                if (!mainSearchEvent.won
                    && !mainSearchEvent.lost
                    && !mainSearchEvent.drawn
                    && !mainSearchEvent.tied
                )
                    _state.value = _state.value.copy(matchResult = 15)

            }

            is MainSearchEvent.VenueSelectionEvent -> {
                var venue = if (mainSearchEvent.home) 1 else 0
                venue = if (mainSearchEvent.away) venue + 2 else venue
                venue = if (mainSearchEvent.neutral) venue + 4 else venue

                _state.value = _state.value.copy(venue = venue)

                if (!mainSearchEvent.home
                    && !mainSearchEvent.away
                    && !mainSearchEvent.neutral
                )
                    _state.value = _state.value.copy(venue = 7)
            }

            is MainSearchEvent.SearchViewFormatEvent -> {
                _state.value = _state.value.copy(searchViewFormat = mainSearchEvent.format)
                setMinimumValue(mainSearchEvent)
            }

            MainSearchEvent.SearchMain -> {
                throw Exception("Should be handled in the view screen")
            }

            MainSearchEvent.Reset -> TODO()
            is MainSearchEvent.PageSizeChangedEvent -> _state.value =
                _state.value.copy(selectedPageSize = mainSearchEvent.value.toInt())
        }
    }

    private fun setMinimumValue(evt: MainSearchEvent.SearchViewFormatEvent) {
        _state.value = _state.value.copy(minimumValue = limits.getOrDefault(evt.format.name, 100))
    }


    val defaultSortDirection: SortDirection
        get() = SortDirection.Descending

}