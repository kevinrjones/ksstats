package com.ksstats.feature.teamrecordspirmarystats.data.repository

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.teamrecordspirmarystats.data.source.JooqTeamRecordsDao
import com.ksstats.feature.teamrecordspirmarystats.data.source.MatchResults
import com.ksstats.feature.teamrecordspirmarystats.data.source.TeamInningsByInnings
import com.ksstats.feature.teamrecordspirmarystats.data.source.TeamSummary
import com.ksstats.feature.teamrecordspirmarystats.domain.repository.TeamRecordsRepository
import kotlinx.coroutines.flow.Flow

class JooqTeamRecordsRepository(private val teamRecordsDao: JooqTeamRecordsDao) : TeamRecordsRepository {
    override fun getTeamSummary(searchParameters: SearchParameters): Flow<DatabaseResult<TeamSummary>> {
        return teamRecordsDao.getTeamSummary(searchParameters)
    }

    override fun getInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<TeamInningsByInnings>> {
        return teamRecordsDao.getInningsByInnings(searchParameters)
    }

    override fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<TeamInningsByInnings>> {
        return teamRecordsDao.getMatchTotals(searchParameters)
    }

    override fun getMatchResults(searchParameters: SearchParameters): Flow<DatabaseResult<MatchResults>> {
        return teamRecordsDao.getMatchResults(searchParameters)
    }
}