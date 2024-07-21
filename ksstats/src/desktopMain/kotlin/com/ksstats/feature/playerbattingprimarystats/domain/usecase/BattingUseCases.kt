package com.ksstats.feature.playerbattingprimarystats.domain.usecase

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerbattingprimarystats.data.InningsByInningsBatting
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import com.ksstats.feature.playerbattingprimarystats.domain.repository.BattingRecordsRepository
import kotlinx.coroutines.flow.Flow

class GetBattingSummary(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return repository.getBattingSummary(searchParameters)
    }
}

class GetSeriesAverages(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return repository.getSeriesAverages(searchParameters)
    }
}

class GetGroundAverages(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return repository.getGroundAverages(searchParameters)
    }
}

class GetByHostCountry(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return repository.getByHostCountry(searchParameters)
    }
}

class GetByOppositionTeam(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return repository.getByOppositionTeam(searchParameters)
    }
}

class GetByYearOfMatchStart(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return repository.getByYearOfMatchStart(searchParameters)
    }
}

class GetBySeason(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> {
        return repository.getBySeason(searchParameters)
    }
}

class GetBattingInningsByInnings(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBatting>> {
        return repository.getBattingInningsByInnings(searchParameters)
    }
}

class GetMatchTotals(val repository: BattingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBatting>> {
        return repository.getMatchTotals(searchParameters)
    }
}