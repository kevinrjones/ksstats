package com.ksstats.feature.recordsearch.di

import com.ksstats.feature.recordsearch.data.repository.JooqMainSearchRepository
import com.ksstats.feature.recordsearch.data.source.JooqRecordSearchDao
import com.ksstats.feature.recordsearch.data.source.RecordSearchDao
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import com.ksstats.feature.recordsearch.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


fun mainSearchModule() = module {
    singleOf(::InitialiseSearchUseCase)
    singleOf(::GetSearchDataForCompetitionAndCountryUseCase)
    singleOf(::GetCompetitionsForMatchTypeAndCountryUseCase)
    singleOf(::GetTeamsAndGroundsForCompetitionAndCountryUseCase)
    singleOf(::RecordSearchUseCases)
    singleOf(::JooqMainSearchRepository) bind MainSearchRepository::class
    singleOf(::JooqRecordSearchDao) bind RecordSearchDao::class
}
