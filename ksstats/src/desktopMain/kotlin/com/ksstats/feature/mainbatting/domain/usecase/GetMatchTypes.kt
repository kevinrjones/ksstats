package com.ksstats.feature.mainbatting.domain.usecase

import com.ksstats.feature.mainbatting.domain.model.MatchType
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import kotlinx.coroutines.flow.Flow

class GetMatchTypes(private val repository: BattingRepository) {

    operator fun invoke(): Flow<List<MatchType>> {
        return repository.getMatchTypes()
    }
}
