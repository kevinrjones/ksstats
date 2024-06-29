package com.ksstats.feature.recordsearch.domain.repository

import com.ksstats.feature.recordsearch.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MainSearchRepository {
    fun getMatchTypes(): Flow<List<MatchType>>
    fun getCompetitions(matchType: String): Flow<List<Competition>>
    fun getGroundsForCompetitionAndCountry(matchType: String, matchSubType: String, countryId: Int): Flow<List<Ground>>
    fun getCountriesForCompetition(matchType: String, matchSubType: String): Flow<List<Country>>
    fun getSeriesDatesForCompetition(matchType: String, matchSubType: String): Flow<List<String>>
    fun getStartAndEndDatesForCompetition(matchType: String, matchSubType: String): Flow<StartEndDate>
    fun getTeamsForCompetitionAndCountry(matchType: String, matchSubType: String, countryId: Int): Flow<List<Team>>
}