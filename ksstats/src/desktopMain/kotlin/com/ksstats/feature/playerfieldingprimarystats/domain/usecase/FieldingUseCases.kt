package com.ksstats.feature.playerfieldingprimarystats.domain.usecase

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.feature.playerfieldingprimarystats.data.InningsByInningsFielding
import com.ksstats.feature.playerfieldingprimarystats.data.PrimaryFielding
import com.ksstats.feature.playerfieldingprimarystats.domain.repository.FieldingRecordsRepository
import kotlinx.coroutines.flow.Flow

class GetFieldingSummary(val repository: FieldingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return repository.getFieldingSummary(searchParameters)
    }
}

class GetSeriesAverages(val repository: FieldingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return repository.getSeriesAverages(searchParameters)
    }
}

class GetGroundAverages(val repository: FieldingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return repository.getGroundAverages(searchParameters)
    }
}

class GetByHostCountry(val repository: FieldingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return repository.getByHostCountry(searchParameters)
    }
}

class GetByOppositionTeam(val repository: FieldingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return repository.getByOppositionTeam(searchParameters)
    }
}

class GetByYearOfMatchStart(val repository: FieldingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return repository.getByYearOfMatchStart(searchParameters)
    }
}

class GetBySeason(val repository: FieldingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> {
        return repository.getBySeason(searchParameters)
    }
}

class GetFieldingInningsByInnings(val repository: FieldingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsFielding>> {
        return repository.getFieldingInningsByInnings(searchParameters)
    }
}

class GetMatchTotals(val repository: FieldingRecordsRepository) {
    operator fun invoke(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsFielding>> {
        return repository.getMatchTotals(searchParameters)
    }
}