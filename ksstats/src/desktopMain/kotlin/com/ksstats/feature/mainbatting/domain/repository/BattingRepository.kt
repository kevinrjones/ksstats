package com.ksstats.feature.mainbatting.domain.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.mainbatting.battingrecords.data.BattingSearchResults
import com.ksstats.feature.mainbatting.domain.model.*
import kotlinx.coroutines.flow.Flow

interface BattingRepository {
    fun getMatchTypes(): Flow<List<MatchType>>
    fun getCompetitions(matchType: String): Flow<List<Competition>>
    fun getTeamsForCompetition(matchType: String): Flow<List<Team>>
    fun getGroundsForCompetition(matchType: String): Flow<List<Ground>>
    fun getCountriesForCompetition(matchType: String): Flow<List<Country>>
    fun getSeriesDatesForCompetition(matchType: String): Flow<List<String>>
    fun getBattingDetails(searchParameters: SearchParameters): Flow<List<BattingSearchResults>>
}