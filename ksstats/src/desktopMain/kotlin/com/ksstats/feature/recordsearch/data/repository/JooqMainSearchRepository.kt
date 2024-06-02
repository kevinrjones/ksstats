package com.ksstats.feature.recordsearch.data.repository

import com.ksstats.feature.recordsearch.data.source.RecordSearchDao
import com.ksstats.feature.recordsearch.domain.model.*
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.coroutines.flow.Flow

class JooqMainSearchRepository(private val recordSearchDao: RecordSearchDao) : MainSearchRepository {
    override fun getMatchTypes(): Flow<List<MatchType>> {
        return recordSearchDao.getMatchTypes()
    }

    override fun getCompetitions(matchType: String): Flow<List<Competition>> {
        return recordSearchDao.getCompetitions(matchType)
    }

    override fun getGroundsForCompetitionAndCountry(matchType: String, countryId: Int): Flow<List<Ground>> {
        return recordSearchDao.getGroundsForCompetitionAndCountry(matchType, countryId)
    }

    override fun getCountriesForCompetition(matchType: String): Flow<List<Country>> {
        return recordSearchDao.getCountriesForCompetition(matchType)
    }

    override fun getSeriesDatesForCompetition(matchType: String): Flow<List<String>> {
        return recordSearchDao.getSeriesDateForCompetition(matchType)
    }


    override fun getStartAndEndDatesForCompetition(matchType: String): Flow<StartEndDate> {
        return recordSearchDao.getStartAndEndDatesForCompetition(matchType)
    }

    override fun getTeamsForCompetitionAndCountry(matchType: String, countryId: Int): Flow<List<Team>> {
        return recordSearchDao.getTeamsForCompetitionAndCountry(matchType, countryId)
    }
}