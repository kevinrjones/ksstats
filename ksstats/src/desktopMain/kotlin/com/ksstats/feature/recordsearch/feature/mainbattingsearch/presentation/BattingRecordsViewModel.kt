package com.ksstats.feature.recordsearch.feature.mainbattingsearch.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.core.domain.util.SortOrder
import com.ksstats.core.presentation.components.DropDownMenuState
import com.ksstats.feature.recordsearch.domain.model.*
import com.ksstats.feature.recordsearch.domain.usecase.RecordSearchUseCases
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.shared.now
import com.ksstats.shared.toSeconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource

sealed class BattingSearchEvent {

    data class StartDateChangedEvent(val date: LocalDate?) : BattingSearchEvent()
    data class EndDateChangedEvent(val date: LocalDate?) : BattingSearchEvent()
    data class MatchTypeSelectionEvent(val menuState: DropDownMenuState) : BattingSearchEvent()
    data class CompetitionSelectionEvent(val menuState: DropDownMenuState) : BattingSearchEvent()
    data class MinimumValueChangedEvent(val value: String) : BattingSearchEvent()
    data class PageSizeChangedEvent(val value: String) : BattingSearchEvent()
    data class TeamSelectionEvent(val menuState: DropDownMenuState) : BattingSearchEvent()
    data class OpponentSelectionEvent(val menuState: DropDownMenuState) : BattingSearchEvent()
    data class CountrySelectionEvent(val menuState: DropDownMenuState) : BattingSearchEvent()
    data class GroundSelectionEvent(val menuState: DropDownMenuState) : BattingSearchEvent()
    data class SeriesDateSelectionEvent(val menuState: DropDownMenuState) : BattingSearchEvent()
    data class ResultSelectionEvent(val won: Boolean, val lost: Boolean, val drawn: Boolean, val tied: Boolean) :
        BattingSearchEvent()

    data class VenueSelectionEvent(val home: Boolean, val away: Boolean, val neutral: Boolean) : BattingSearchEvent()
    data class SearchViewFormatEvent(val format: SearchViewFormat) : BattingSearchEvent()
    data object SearchBatting : BattingSearchEvent()
    data object Reset : BattingSearchEvent()
}

enum class SearchViewFormat(val format: StringResource) {
    PlayerSummary(Res.string.playerSummary),
    InningsByInnings(Res.string.inningsByInningsList),
    MatchTotals(Res.string.matchTotals),
    SeriesAverages(Res.string.seriesAverages),
    GroundAverages(Res.string.groundAverages),
    ByHostCountry(Res.string.byHostCountry),
    ByOppositionTeam(Res.string.byOppositionTeam),
    ByYearOfMatchStart(Res.string.byYearOfMatchStart),
    BySeason(Res.string.bySeason),
}

class BattingRecordsViewModel(
    val recordSearchUseCases: RecordSearchUseCases,
) : ViewModel() {

    var job: Job? = null

    init {
        job = this.viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _loaded.value = false
            }


            withContext(Dispatchers.IO) {
                recordSearchUseCases.initialiseSearch(Country.defaultCountry()).collect { searchResult ->
                    initialiseStateFromSearchData(searchResult)
                }
            }

            withContext(Dispatchers.Main) {
                _loaded.value = true
            }
        }
    }

    private fun initialiseStateFromSearchData(searchResult: SearchData) {
        _matchTypes.value = searchResult.matchTypes
        _selectedMatchType.value = _matchTypes.value[0]

        _pageSizes.value = searchResult.pageSizes
        _selectedPageSize.value = pageSizes.value[0]

        _competitions.value = searchResult.competitions
        _selectedCompetition.value = _competitions.value[0]

        _teams.value = searchResult.teams
        _selectedTeam.value = _teams.value[0]
        _selectedOpposition.value = _teams.value[0]

        _grounds.value = searchResult.grounds
        _selectedGround.value = _grounds.value[0]

        _countries.value = searchResult.countries
        _selectedCountry.value = _countries.value[0]

        _startDate.value =
            Instant.fromEpochSeconds(searchResult.startAndEndDate.start).toLocalDateTime(TimeZone.UTC).date
        _endDate.value = Instant.fromEpochSeconds(searchResult.startAndEndDate.end).toLocalDateTime(TimeZone.UTC).date


        _seriesDates.value = searchResult.seriesDates
        _selectedSeriesDate.value = searchResult.seriesDates[0]
    }


    fun uiEvent(battingSearchEvent: BattingSearchEvent) {
        when (battingSearchEvent) {
            is BattingSearchEvent.StartDateChangedEvent -> {
                battingSearchEvent.date?.let {
                    _startDate.value = it
                }

            }

            is BattingSearchEvent.EndDateChangedEvent -> {
                battingSearchEvent.date?.let {
                    _endDate.value = it
                }
            }

            is BattingSearchEvent.MatchTypeSelectionEvent -> {
                val matchType =
                    _matchTypes.value.find { it.type == battingSearchEvent.menuState.key }
                        ?: throw Exception("Invalid Match Type: ${battingSearchEvent.menuState}")

                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        _loaded.value = false
                        recordSearchUseCases.getCompetitionsForMatchTypeAndCountry(matchType, _selectedCountry.value)
                            .collect { result ->
                                val searchResult =
                                    result.copy(
                                        matchTypes = _matchTypes.value,
                                        pageSizes = _pageSizes.value,
                                    )

                                // copy everything over but reset the matchtype
                                initialiseStateFromSearchData(searchResult)
                                _selectedMatchType.value = matchType
                            }
                    }
                }

            }

            is BattingSearchEvent.CompetitionSelectionEvent -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val matchType = _selectedMatchType.value
                        val competition =
                            _competitions.value.find { it.subType == battingSearchEvent.menuState.key }
                                ?: throw Exception("Invalid Competition: ${battingSearchEvent.menuState}")
                        _loaded.value = false
                        recordSearchUseCases.getSearchDataForCompetitionAndCountry(competition, selectedCountry.value)
                            .collect { result ->

                                val searchResult =
                                    result.copy(
                                        matchTypes = _matchTypes.value,
                                        pageSizes = _pageSizes.value,
                                        competitions = _competitions.value
                                    )

                                initialiseStateFromSearchData(searchResult)
                                // copy everything over but reset the matchtype and competition
                                _selectedCompetition.value = competition
                                _selectedMatchType.value = matchType
                            }
                        _loaded.value = true
                    }
                }
            }

            is BattingSearchEvent.MinimumValueChangedEvent -> {
                if (battingSearchEvent.value.length == 0) {
                    _minimumRuns.value = 0
                    return
                } else if (battingSearchEvent.value.toIntOrNull() == null) {
                    return
                }

                _minimumRuns.value = battingSearchEvent.value.toInt() // todo: Error handling
            }

            is BattingSearchEvent.CountrySelectionEvent -> {
                viewModelScope.launch {
                    val matchType = _selectedMatchType.value
                    val competition = _selectedCompetition.value

                    withContext(Dispatchers.IO) {
                        recordSearchUseCases.getTeamsAndGroundsForCompetitionAndCountry(
                            _selectedCompetition.value,
                            _selectedCountry.value
                        ).collect { result ->
                            val searchResult =
                                result.copy(
                                    matchTypes = _matchTypes.value,
                                    pageSizes = _pageSizes.value,
                                    competitions = _competitions.value,
                                    countries = _countries.value,
                                    seriesDates = seriesDates.value,
                                    startAndEndDate = StartEndDate(
                                        _startDate.value.toSeconds(),
                                        _endDate.value.toSeconds()
                                    )
                                )

                            initialiseStateFromSearchData(searchResult)
                            // copy everything over but reset the matchtype and competition
                            _selectedCompetition.value = competition
                            _selectedMatchType.value = matchType
                            _selectedCountry.value =
                                Country(battingSearchEvent.menuState.key.toInt(), battingSearchEvent.menuState.value)

                        }

                    }
                }
            }

            is BattingSearchEvent.GroundSelectionEvent -> {
                _selectedGround.value =
                    Ground(battingSearchEvent.menuState.key.toInt(), battingSearchEvent.menuState.value)
            }

            is BattingSearchEvent.TeamSelectionEvent -> {
                _selectedTeam.value = Team(battingSearchEvent.menuState.key.toInt(), battingSearchEvent.menuState.value)
            }

            is BattingSearchEvent.OpponentSelectionEvent -> {
                _selectedOpposition.value =
                    Team(battingSearchEvent.menuState.key.toInt(), battingSearchEvent.menuState.value)
            }

            is BattingSearchEvent.SeriesDateSelectionEvent -> {
                _selectedSeriesDate.value = battingSearchEvent.menuState.value
            }


            is BattingSearchEvent.ResultSelectionEvent -> {
                _matchResult.value = if (battingSearchEvent.won) 1 else 0
                _matchResult.value = if (battingSearchEvent.lost) _matchResult.value + 2 else _matchResult.value + 0
                _matchResult.value = if (battingSearchEvent.drawn) _matchResult.value + 4 else _matchResult.value + 0
                _matchResult.value = if (battingSearchEvent.tied) _matchResult.value + 8 else _matchResult.value + 0

                if (!battingSearchEvent.won
                    && !battingSearchEvent.lost
                    && !battingSearchEvent.drawn
                    && !battingSearchEvent.tied
                )
                    _matchResult.value = 15
            }

            is BattingSearchEvent.VenueSelectionEvent -> {
                _venue.value = if (battingSearchEvent.home) 1 else 0
                _venue.value = if (battingSearchEvent.away) _venue.value + 2 else _venue.value + 0
                _venue.value = if (battingSearchEvent.neutral) _venue.value + 4 else _venue.value + 0

                if (!battingSearchEvent.home
                    && !battingSearchEvent.away
                    && !battingSearchEvent.neutral
                )
                    _venue.value = 7

            }

            is BattingSearchEvent.SearchViewFormatEvent -> _searchViewFormat.value = battingSearchEvent.format
            BattingSearchEvent.SearchBatting -> {
                throw Exception("Should be handled in the view screen")
            }

            BattingSearchEvent.Reset -> TODO()
            is BattingSearchEvent.PageSizeChangedEvent -> _selectedPageSize.value = battingSearchEvent.value.toInt()
        }
    }

    private val _loaded: MutableState<Boolean> = mutableStateOf(false)
    val loaded: State<Boolean> = _loaded

    private val _matchTypes: MutableStateFlow<List<MatchType>> = MutableStateFlow(listOf<MatchType>())
    val matchTypes: StateFlow<List<MatchType>> = _matchTypes.asStateFlow()

    private val _selectedMatchType: MutableStateFlow<MatchType> =
        MutableStateFlow(MatchType(id = 0, type = "", description = "Loading..."))
    val selectedMatchType: StateFlow<MatchType> = _selectedMatchType.asStateFlow()

    private val _competitions: MutableStateFlow<List<Competition>> = MutableStateFlow(listOf<Competition>())
    val competitions: StateFlow<List<Competition>> = _competitions.asStateFlow()

    private val _selectedCompetition: MutableStateFlow<Competition> =
        MutableStateFlow(Competition(id = 0, type = "", subType = "", competition = "Loading..."))
    val selectedCompetition: StateFlow<Competition> = _selectedCompetition.asStateFlow()

    private val _teams: MutableStateFlow<List<Team>> = MutableStateFlow(listOf<Team>())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    private val _selectedTeam: MutableStateFlow<Team> =
        MutableStateFlow(Team(id = 0, name = "Loading..."))
    val selectedTeam: StateFlow<Team> = _selectedTeam.asStateFlow()

    private val _selectedOpposition: MutableStateFlow<Team> =
        MutableStateFlow(Team(id = 0, name = "Loading..."))
    val selectedOpposition: StateFlow<Team> = _selectedOpposition.asStateFlow()

    private val _grounds: MutableStateFlow<List<Ground>> = MutableStateFlow(listOf<Ground>())
    val grounds: StateFlow<List<Ground>> = _grounds.asStateFlow()

    private val _selectedGround: MutableStateFlow<Ground> =
        MutableStateFlow(Ground(id = 0, name = "Loading..."))
    val selectedGround: StateFlow<Ground> = _selectedGround.asStateFlow()

    private val _countries: MutableStateFlow<List<Country>> = MutableStateFlow(listOf<Country>())
    val countries: StateFlow<List<Country>> = _countries.asStateFlow()

    private val _selectedCountry: MutableStateFlow<Country> =
        MutableStateFlow(Country.defaultCountry())
    val selectedCountry: StateFlow<Country> = _selectedCountry.asStateFlow()

    private val _seriesDates: MutableStateFlow<List<String>> = MutableStateFlow(listOf<String>())
    val seriesDates: StateFlow<List<String>> = _seriesDates.asStateFlow()

    private val _selectedSeriesDate: MutableStateFlow<String> =
        MutableStateFlow("Loading...")
    val selectedSeriesDate: StateFlow<String> = _selectedSeriesDate.asStateFlow()

    private val _pageSizes: MutableStateFlow<List<Int>> = MutableStateFlow(listOf<Int>())
    val pageSizes: StateFlow<List<Int>> = _pageSizes.asStateFlow()

    private val _selectedPageSize: MutableStateFlow<Int> =
        MutableStateFlow(50)
    val selectedPageSize: StateFlow<Int> = _selectedPageSize.asStateFlow()

    private val _minimumRuns = MutableStateFlow(100)
    val minimumRuns: StateFlow<Int> = _minimumRuns.asStateFlow()

    private val _matchResult = MutableStateFlow(0)
    val matchResult: StateFlow<Int> = _matchResult.asStateFlow()

    private val _venue = MutableStateFlow(0)
    val venue: StateFlow<Int> = _venue.asStateFlow()

    private val _startDate = MutableStateFlow(LocalDate(1772, 6, 24))
    val startDate = _startDate.asStateFlow()

    // 1772-06-24 is the earliest date of cricket in the database
    private val _endDate = MutableStateFlow(LocalDate.now())
    val endDate = _endDate.asStateFlow()

    private val _searchViewFormat = MutableStateFlow<SearchViewFormat>(SearchViewFormat.PlayerSummary)
    val searchViewFormat = _searchViewFormat.asStateFlow()


    // todo: do I need to get the pages sizes this way
    // if so then I also need to do this in the PageSize composable
    private fun getPageSizes(): Flow<List<Int>> = flow  {
        emit(listOf(50, 100, 150, 200))
    }



    val defaultSortDirection: SortDirection
        get() = SortDirection.Descending
    val defaultSortOrder: SortOrder
        get() {
            return SortOrder.Runs
        }

}