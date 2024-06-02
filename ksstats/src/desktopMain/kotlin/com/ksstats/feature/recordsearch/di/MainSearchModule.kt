package com.ksstats.feature.recordsearch.di

import com.ksstats.core.domain.usecases.GetSearchRecordsPageSizes
import com.ksstats.feature.recordsearch.data.repository.JooqMainSearchRepository
import com.ksstats.feature.recordsearch.data.source.JooqRecordSearchDao
import com.ksstats.feature.recordsearch.data.source.RecordSearchDao
import com.ksstats.feature.recordsearch.domain.repository.MainSearchRepository
import com.ksstats.feature.recordsearch.domain.usecase.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


fun mainSearchModule() = module {
    singleOf(::GetMatchTypes)
    singleOf(::GetCompetitions)
    singleOf(::GetTeamsForCompetitionAndCountry)
    singleOf(::GetGroundsForCompetitionAndCountry)
    singleOf(::GetCountriesForCompetition)
    singleOf(::GetSeriesDatesForCompetition)
    singleOf(::GetStartAndEndDatesForCompetition)
    singleOf(::GetSearchRecordsPageSizes)
    singleOf(::GetTeamsForCountry)
    singleOf(::RecordSearchUseCases)
    singleOf(::JooqMainSearchRepository) bind MainSearchRepository::class
    singleOf(::JooqRecordSearchDao) bind RecordSearchDao::class
}
