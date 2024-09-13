package com.ksstats.feature.playerfieldingprimarystats.data.repository

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerfieldingprimarystats.data.InningsByInningsFielding
import com.ksstats.feature.playerfieldingprimarystats.data.PrimaryFielding
import com.ksstats.feature.playerfieldingprimarystats.data.source.FieldingRecordsDao
import com.ksstats.feature.playerfieldingprimarystats.domain.repository.FieldingRecordsRepository
import kotlinx.coroutines.flow.Flow

class JooqFieldingRecordsRepository(private val fieldingRecordsDao: FieldingRecordsDao) : FieldingRecordsRepository {
    override fun getFieldingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return fieldingRecordsDao.getFieldingSummary(searchParameters)
    }

    override fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return fieldingRecordsDao.getSeriesAverages(searchParameters)
    }

    override fun getFieldingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsFielding>> {
        return fieldingRecordsDao.getFieldingInningsByInnings(searchParameters)
    }

    override fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsFielding>> {
        return fieldingRecordsDao.getMatchTotals(searchParameters)
    }

    override fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return fieldingRecordsDao.getGroundAverages(searchParameters)
    }

    override fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return fieldingRecordsDao.getByHostCountry(searchParameters)
    }

    override fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return fieldingRecordsDao.getByOppositionTeam(searchParameters)
    }

    override fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return fieldingRecordsDao.getByYearOfMatchStart(searchParameters)
    }

    override fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return fieldingRecordsDao.getBySeason(searchParameters)
    }

}