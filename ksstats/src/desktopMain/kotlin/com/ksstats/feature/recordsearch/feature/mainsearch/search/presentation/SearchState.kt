package com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation

import com.ksstats.feature.recordsearch.domain.model.*
import com.ksstats.shared.now
import kotlinx.datetime.LocalDate

data class SearchState(
    val loaded: Boolean = false,
    val matchTypes: List<MatchTypeEntity> = emptyList(),
    val selectedMatchType: MatchTypeEntity = MatchTypeEntity(id = 0, type = "", description = "Loading..."),
    val competitions: List<Competition> = emptyList(),
    val selectedCompetition: Competition = Competition(id = 0, type = "", subType = "", competition = "Loading..."),
    val teams: List<Team> = emptyList(),
    val selectedTeam: Team = Team(id = 0, name = "Loading..."),
    val selectedOpposition: Team = Team(id = 0, name = "Loading..."),
    val grounds: List<Ground> = emptyList(),
    val selectedGround: Ground = Ground(id = 0, name = "Loading..."),
    val countries: List<Country> = emptyList(),
    val selectedCountry: Country = Country(id = 0, name = "Loading..."),
    val seriesDates: List<String> = emptyList(),
    val selectedSeriesDate: String = "Loading...",
    val pageSizes: List<Int> = emptyList(),
    val selectedPageSize: Int = 50,
    val minimumValue: Int = 100,
    val matchResult: Int = 0,
    val venue: Int = 0,
    val startDate: LocalDate = LocalDate(1772, 6, 24),
    val endDate: LocalDate = LocalDate.now(),
    val searchViewFormat: SearchViewFormat = SearchViewFormat.PlayerSummary,
    val isBattingTeamSearch: Boolean = true,
)