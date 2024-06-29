package com.ksstats.feature.recordsearch.domain.usecase

import com.ksstats.feature.recordsearch.domain.model.*
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import com.ksstats.shared.now
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate

class GetSearchDataForCompetitionAndCountryUseCase(private val repository: MainSearchRepository) {
    suspend operator fun invoke(competition: Competition, country: Country): Flow<SearchData> = flow {
        emit(getAllForCompetition(repository, competition, country, SearchData()))
    }
}

class GetCompetitionsForMatchTypeAndCountryUseCase(private val repository: MainSearchRepository) {
    suspend operator fun invoke(matchType: MatchType, country: Country): Flow<SearchData> = flow {
        emit(getCompetitionsForMatchTypeAndCountry(repository, matchType, country, SearchData()))
    }
}

class GetTeamsAndGroundsForCompetitionAndCountryUseCase(private val repository: MainSearchRepository) {
    suspend operator fun invoke(competition: Competition, country: Country): Flow<SearchData> = flow {
        emit(getTeamsAndGroundsForCountry(repository, competition, country))
    }
}

class InitialiseSearchUseCase(private val repository: MainSearchRepository) {
    suspend operator fun invoke(selectedCountry: Country): Flow<SearchData> = flow {

        val matchTypes = mutableListOf<MatchType>()

        repository.getMatchTypes().collect {
            matchTypes.addAll(it)
        }

        val pageSizes = listOf(50, 100, 150, 200)

        val searchData = SearchData(matchTypes = matchTypes, pageSizes = pageSizes)

        emit(getCompetitionsForMatchTypeAndCountry(repository, matchTypes[0], selectedCountry, searchData))
    }
}

private suspend fun getCompetitionsForMatchTypeAndCountry(
    repository: MainSearchRepository,
    matchType: MatchType,
    selectedCountry: Country,
    searchData: SearchData,
): SearchData {
    val competitions = mutableListOf<Competition>()

    repository.getCompetitions(matchType.type).collect { competition ->
        competitions.addAll(competition)
    }
    return getAllForCompetition(
        repository,
        competitions[0],
        selectedCountry,
        searchData.copy(competitions = competitions)
    )
}


private suspend fun getAllForCompetition(
    repository: MainSearchRepository,
    competition: Competition,
    country: Country,
    searchData: SearchData,
): SearchData {
    val teams = getTeamsForCompetition(repository, competition, country)
    val grounds = getGroundsForCompetitionAndCountry(repository, competition, country)
    val countries = getCountriesForCompetition(repository, competition)
    val seriesDates = getSeriesDatesForCompetition(repository, competition)
    val startAndEndDates = getStartAndEndDatesForCompetition(repository, competition)

    return searchData.copy(
        teams = teams,
        grounds = grounds,
        countries = countries,
        seriesDates = seriesDates,
        startAndEndDate = startAndEndDates
    )
}

private suspend fun getTeamsForCompetition(
    repository: MainSearchRepository,
    competition: Competition,
    selectedCountry: Country,
): List<Team> {

    val teams = mutableListOf<Team>()

    repository.getTeamsForCompetitionAndCountry(competition.type, competition.subType, selectedCountry.id).collect { team ->
        teams.addAll(team)
    }
    return teams
}

private suspend fun getCountriesForCompetition(
    repository: MainSearchRepository,
    competition: Competition,
): MutableList<Country> {
    val countries = mutableListOf<Country>()

    repository.getCountriesForCompetition(competition.type, competition.subType).collect { country ->
        countries.addAll(country)
    }
    return countries
}

private suspend fun getSeriesDatesForCompetition(
    repository: MainSearchRepository,
    competition: Competition,
): MutableList<String> {
    val seriesDates = mutableListOf<String>()

    repository.getSeriesDatesForCompetition(competition.type, competition.subType).collect { date ->
        seriesDates.addAll(date)
    }

    return seriesDates
}

private suspend fun getStartAndEndDatesForCompetition(
    repository: MainSearchRepository,
    competition: Competition,
): StartEndDate {
    LocalDate.now()
    var startEndDates = StartEndDate(0, 0)

    repository.getStartAndEndDatesForCompetition(competition.type, competition.subType).collect { date: StartEndDate ->
        startEndDates = date
    }

    return startEndDates
}

private suspend fun getGroundsForCompetitionAndCountry(
    repository: MainSearchRepository,
    competition: Competition,
    country: Country,
): MutableList<Ground> {
    val grounds = mutableListOf<Ground>()

    repository.getGroundsForCompetitionAndCountry(competition.type, competition.subType, country.id).collect { ground ->
        grounds.addAll(ground)
    }

    return grounds
}

private suspend fun getTeamsAndGroundsForCountry(
    repository: MainSearchRepository,
    competition: Competition,
    country: Country,
): SearchData {
    val teams = getTeamsForCompetitionAndCountry(repository, competition, country)
    val grounds = getGroundsForCompetitionAndCountry(repository, competition, country)

    return(SearchData(teams = teams, grounds = grounds))
}

private suspend fun getTeamsForCompetitionAndCountry(
    repository: MainSearchRepository,
    competition: Competition,
    country: Country,
): MutableList<Team> {
    val teams = mutableListOf<Team>()

    repository.getTeamsForCompetitionAndCountry(competition.type, competition.subType, country.id).collect { team ->
        teams.addAll(team)
    }

    return teams
}


