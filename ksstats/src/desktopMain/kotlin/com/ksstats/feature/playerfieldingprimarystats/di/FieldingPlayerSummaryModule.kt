package com.ksstats.feature.playerfieldingprimarystats.di

import com.ksstats.feature.playerfieldingprimarystats.data.repository.JooqFieldingRecordsRepository
import com.ksstats.feature.playerfieldingprimarystats.data.source.FieldingRecordsDao
import com.ksstats.feature.playerfieldingprimarystats.data.source.JooqFieldingRecordsDao
import com.ksstats.feature.playerfieldingprimarystats.domain.repository.FieldingRecordsRepository
import com.ksstats.feature.playerfieldingprimarystats.domain.usecase.*
import com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation.MainSearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playerFieldingSummaryModule() = module {
    singleOf(::PlayerFieldingPrimaryStatsUseCases)
    singleOf(::GetFieldingSummary)
    singleOf(::GetFieldingInningsByInnings)
    singleOf(::GetMatchTotals)
    singleOf(::GetSeriesAverages)
    singleOf(::GetGroundAverages)
    singleOf(::GetByHostCountry)
    singleOf(::GetByOppositionTeam)
    singleOf(::GetByYearOfMatchStart)
    singleOf(::GetBySeason)
    singleOf(::JooqFieldingRecordsRepository) bind FieldingRecordsRepository::class
    factory { JooqFieldingRecordsDao(get()) } bind FieldingRecordsDao::class
    factory { MainSearchViewModel(get()) }
}

