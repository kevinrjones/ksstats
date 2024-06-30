package com.ksstats.feature.recordsearch.data.source

import com.ksstats.core.types.MatchType
import com.ksstats.feature.recordsearch.domain.model.*
import kotlinx.coroutines.flow.Flow

interface RecordSearchDao {
    fun getMatchTypes() : Flow<List<MatchTypeEntity>>
    fun getCompetitions(matchType: MatchType): Flow<List<Competition>>
    fun getGroundsForCompetitionAndCountry(matchType: MatchType, matchSubType: MatchType, countryId: Int): Flow<List<Ground>>
    fun getCountriesForCompetition(matchType: MatchType, matchSubType: MatchType): Flow<List<Country>>
    fun getSeriesDateForCompetition(matchType: MatchType, matchSubType: MatchType): Flow<List<String>>
    fun getStartAndEndDatesForCompetition(matchType: MatchType, matchSubType: MatchType): Flow<StartEndDate>
    fun getTeamsForCompetitionAndCountry(matchType: MatchType, matchSubType: MatchType, countryId: Int): Flow<List<Team>>
}