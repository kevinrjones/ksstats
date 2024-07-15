package com.ksstats.feature.playerbattingprimarystats.di

import com.ksstats.feature.playerbattingprimarystats.domain.usecase.PlayerBattingPrimaryStatsUseCases
import com.ksstats.feature.playerbattingprimarystats.domain.usecase.GetBattingSummary
import com.ksstats.feature.playerbattingprimarystats.domain.usecase.GetBattingInningsByInnings
import com.ksstats.feature.playerbattingprimarystats.domain.usecase.GetMatchTotals
import com.ksstats.feature.playerbattingprimarystats.data.repository.JooqBattingRecordsRepository
import com.ksstats.feature.playerbattingprimarystats.data.source.BattingRecordsDao
import com.ksstats.feature.playerbattingprimarystats.data.source.JooqBattingRecordsDao
import com.ksstats.feature.playerbattingprimarystats.domain.repository.BattingRecordsRepository
import com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation.MainSearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playerBattingSummaryModule() = module {
    singleOf(::PlayerBattingPrimaryStatsUseCases)
    singleOf(::GetBattingSummary)
    singleOf(::GetBattingInningsByInnings)
    singleOf(::GetMatchTotals)
    singleOf(::JooqBattingRecordsRepository) bind BattingRecordsRepository::class
    factory { JooqBattingRecordsDao(get()) } bind BattingRecordsDao::class
    factory { MainSearchViewModel(get()) }
}

