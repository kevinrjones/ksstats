package com.ksstats.feature.teamrecordspirmarystats.domain.usecase

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.teamrecordspirmarystats.data.source.MatchResults
import com.ksstats.feature.teamrecordspirmarystats.data.source.TeamInningsByInnings
import com.ksstats.feature.teamrecordspirmarystats.data.source.TeamSummary
import com.ksstats.feature.teamrecordspirmarystats.domain.repository.TeamRecordsRepository
import kotlinx.coroutines.flow.Flow

class GetTeamSummary(val repository: TeamRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<TeamSummary>> {
        return repository.getTeamSummary(searchParameters)
    }
}

class GetTeamInningsByInnings(val repository: TeamRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<TeamInningsByInnings>> {
        return repository.getInningsByInnings(searchParameters)
    }
}

class GetMatchTotals(val repository: TeamRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<TeamInningsByInnings>> {
        return repository.getMatchTotals(searchParameters)
    }
}

class GetMatchResults(val repository: TeamRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<MatchResults>> {
        return repository.getMatchResults(searchParameters)
    }
}

class GetSeriesRecords(val repository: TeamRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<TeamSummary>> {
        return repository.getSeriesRecords(searchParameters)
    }
}

