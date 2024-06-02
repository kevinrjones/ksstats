package com.ksstats.feature.recordsearch.domain.repository

import com.ksstats.feature.recordsearch.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MainSearchRepository {
    fun getMatchTypes(): Flow<List<MatchType>>
    fun getCompetitions(matchType: String): Flow<List<Competition>>
    fun getTeamsForCompetitionEx(matchType: String): Flow<List<Team>>
    fun getGroundsForCompetitionAndCountry(matchType: String, countryId: Int): Flow<List<Ground>>
    fun getCountriesForCompetition(matchType: String): Flow<List<Country>>
    fun getSeriesDatesForCompetition(matchType: String): Flow<List<String>>
    fun getStartAndEndDatesForCompetition(matchType: String): Flow<StartEndDate>
    fun getTeamsForCompetitionAndCountry(matchType: String, countryId: Int): Flow<List<Team>>
}