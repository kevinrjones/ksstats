package com.ksstats.feature.mainbatting.data.repository

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.mainbatting.battingrecords.data.BattingSearchResults
import com.ksstats.feature.mainbatting.data.source.BattingRecordsDao
import com.ksstats.feature.mainbatting.domain.model.*
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import kotlinx.coroutines.flow.Flow

class JooqBattingRecordsRepository(private val battingRecordsDao: BattingRecordsDao) : BattingRepository {
    override fun getMatchTypes(): Flow<List<MatchType>> {
        return battingRecordsDao.getMatchTypes()
    }

    override fun getCompetitions(matchType: String): Flow<List<Competition>> {
        return battingRecordsDao.getCompetitions(matchType)
    }

    override fun getTeamsForCompetition(matchType: String): Flow<List<Team>> {
        return battingRecordsDao.getTeamsForCompetition(matchType)
    }

    override fun getGroundsForCompetition(matchType: String): Flow<List<Ground>> {
        return battingRecordsDao.getGroundsForCompetition(matchType)
    }

    override fun getCountriesForCompetition(matchType: String): Flow<List<Country>> {
        return battingRecordsDao.getCountriesForCompetition(matchType)
    }

    override fun getSeriesDatesForCompetition(matchType: String): Flow<List<String>> {
        return battingRecordsDao.getSeriesDateForCompetition(matchType)
    }

    override fun getBattingDetails(searchParameters: SearchParameters): Flow<List<BattingSearchResults>> {
        return battingRecordsDao.getBattingDetails(searchParameters)
    }
}