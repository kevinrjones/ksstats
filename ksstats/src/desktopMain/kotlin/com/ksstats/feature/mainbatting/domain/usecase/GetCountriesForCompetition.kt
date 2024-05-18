package com.ksstats.feature.mainbatting.domain.usecase

import com.ksstats.feature.mainbatting.domain.model.Country
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import kotlinx.coroutines.flow.Flow

class GetCountriesForCompetition(private val repository: BattingRepository) {

    operator fun invoke(matchType: String): Flow<List<Country>> {
        return repository.getCountriesForCompetition(matchType)
    }
}

