package com.ksstats.feature.mainbatting.domain.usecase

import com.ksstats.feature.mainbatting.domain.model.Team
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import kotlinx.coroutines.flow.Flow

class GetTeamsForCompetition(private val repository: BattingRepository) {

    operator fun invoke(matchType: String): Flow<List<Team>> {
        return repository.getTeamsForCompetition(matchType)
    }
}

