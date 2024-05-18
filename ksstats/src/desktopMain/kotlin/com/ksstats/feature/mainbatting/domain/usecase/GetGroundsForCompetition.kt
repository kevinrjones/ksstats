package com.ksstats.feature.mainbatting.domain.usecase

import com.ksstats.feature.mainbatting.domain.model.Ground
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import kotlinx.coroutines.flow.Flow

class GetGroundsForCompetition(private val repository: BattingRepository) {

    operator fun invoke(matchType: String): Flow<List<Ground>> {
        return repository.getGroundsForCompetition(matchType)
    }
}

