package com.ksstats.feature.teamrecordspirmarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import kotlinx.coroutines.flow.Flow

interface TeamRecordsDao {
    fun getTeamSummary(searchParameters: SearchParameters): Flow<DatabaseResult<TeamSummary>>
    fun getInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<TeamInningsByInnings>>
    fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<TeamInningsByInnings>>
    fun getMatchResults(searchParameters: SearchParameters): Flow<DatabaseResult<MatchResults>>
}

