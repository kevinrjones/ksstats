package com.ksstats.feature.teamrecordspirmarystats.domain.repository

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.teamrecordspirmarystats.data.source.MatchResults
import com.ksstats.feature.teamrecordspirmarystats.data.source.TeamInningsByInnings

import com.ksstats.feature.teamrecordspirmarystats.data.source.TeamSummary
import kotlinx.coroutines.flow.Flow

interface TeamRecordsRepository {
    fun getTeamSummary(searchParameters: SearchParameters): Flow<DatabaseResult<TeamSummary>>
    fun getInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<TeamInningsByInnings>>
    fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<TeamInningsByInnings>>
    fun getMatchResults(searchParameters: SearchParameters): Flow<DatabaseResult<MatchResults>>
    fun getSeriesRecords(searchParameters: SearchParameters): Flow<DatabaseResult<TeamSummary>>
}