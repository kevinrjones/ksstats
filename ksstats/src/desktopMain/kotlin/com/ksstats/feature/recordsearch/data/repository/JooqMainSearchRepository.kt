package com.ksstats.feature.recordsearch.data.repository

import com.ksstats.core.types.MatchType
import com.ksstats.feature.recordsearch.data.source.RecordSearchDao
import com.ksstats.feature.recordsearch.domain.model.*
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import kotlinx.coroutines.flow.Flow

class JooqMainSearchRepository(private val recordSearchDao: RecordSearchDao) : MainSearchRepository {
    override fun getMatchTypes(): Flow<List<MatchTypeEntity>> {
        return recordSearchDao.getMatchTypes()
    }

    override fun getCompetitions(matchType: MatchType): Flow<List<Competition>> {
        return recordSearchDao.getCompetitions(matchType)
    }

    override fun getGroundsForCompetitionAndCountry(matchType: MatchType, matchSubType: MatchType, countryId: Int): Flow<List<Ground>> {
        return recordSearchDao.getGroundsForCompetitionAndCountry(matchType, matchSubType, countryId)
    }

    override fun getCountriesForCompetition(matchType: MatchType, matchSubType: MatchType): Flow<List<Country>> {
        return recordSearchDao.getCountriesForCompetition(matchType, matchSubType)
    }

    override fun getSeriesDatesForCompetition(matchType: MatchType, matchSubType: MatchType): Flow<List<String>> {
        return recordSearchDao.getSeriesDateForCompetition(matchType, matchSubType)
    }


    override fun getStartAndEndDatesForCompetition(matchType: MatchType, matchSubType: MatchType): Flow<StartEndDate> {
        return recordSearchDao.getStartAndEndDatesForCompetition(matchType, matchSubType)
    }

    override fun getTeamsForCompetitionAndCountry(matchType: MatchType, matchSubType: MatchType, countryId: Int): Flow<List<Team>> {
        return recordSearchDao.getTeamsForCompetitionAndCountry(matchType, matchSubType, countryId)
    }
}