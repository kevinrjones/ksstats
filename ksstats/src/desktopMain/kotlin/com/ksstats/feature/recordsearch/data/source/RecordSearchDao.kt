package com.ksstats.feature.recordsearch.data.source

import com.ksstats.feature.recordsearch.domain.model.*
import kotlinx.coroutines.flow.Flow

interface RecordSearchDao {
    fun getMatchTypes() : Flow<List<MatchType>>
    fun getCompetitions(matchType: String): Flow<List<Competition>>
    fun getGroundsForCompetitionAndCountry(matchType: String, countryId: Int): Flow<List<Ground>>
    fun getCountriesForCompetition(matchType: String): Flow<List<Country>>
    fun getSeriesDateForCompetition(matchType: String): Flow<List<String>>
    fun getStartAndEndDatesForCompetition(matchType: String): Flow<StartEndDate>
    fun getTeamsForCompetitionAndCountry(matchType: String, countryId: Int): Flow<List<Team>>
}