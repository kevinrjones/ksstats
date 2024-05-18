package com.ksstats.feature.mainbatting.domain.usecase

import com.ksstats.feature.mainbatting.domain.model.Competition
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import kotlinx.coroutines.flow.Flow

class GetCompetitions(private val repository: BattingRepository) {

    operator fun invoke(matchType: String): Flow<List<Competition>> {
        return repository.getCompetitions(matchType)
    }
}

