package com.ksstats.feature.teamrecordspirmarystats.di

import com.ksstats.feature.teamrecordspirmarystats.data.repository.JooqTeamRecordsRepository
import com.ksstats.feature.teamrecordspirmarystats.data.source.TeamRecordsDao
import com.ksstats.feature.teamrecordspirmarystats.data.source.JooqTeamRecordsDao
import com.ksstats.feature.teamrecordspirmarystats.domain.repository.TeamRecordsRepository
import com.ksstats.feature.teamrecordspirmarystats.domain.usecase.*
import com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation.MainSearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun teamSummaryModule() = module {
    singleOf(::TeamPrimaryStatsUseCases)
    singleOf(::GetTeamSummary)
    singleOf(::GetTeamInningsByInnings)
    singleOf(::GetMatchTotals)
    singleOf(::GetMatchResults)
    singleOf(::JooqTeamRecordsRepository) bind TeamRecordsRepository::class
    factory { JooqTeamRecordsDao(get()) } bind TeamRecordsDao::class
    factory { MainSearchViewModel(get(), get(), get()) }
}

