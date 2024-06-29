package com.ksstats.feature.playerbattingsummary.di

import com.ksstats.feature.playerbattingsummary.domain.usecase.PlayerBattingSummaryUseCases
import com.ksstats.feature.playerbattingsummary.domain.usecase.GetBattingSummary
import com.ksstats.feature.playerbattingsummary.data.repository.JooqBattingRecordsRepository
import com.ksstats.feature.playerbattingsummary.data.source.BattingRecordsDao
import com.ksstats.feature.playerbattingsummary.data.source.JooqBattingRecordsDao
import com.ksstats.feature.playerbattingsummary.domain.repository.BattingRecordsRepository
import com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation.MainSearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playerBattingSummaryModule() = module {
    singleOf(::PlayerBattingSummaryUseCases)
    singleOf(::GetBattingSummary)
    singleOf(::JooqBattingRecordsRepository) bind BattingRecordsRepository::class
    factory { JooqBattingRecordsDao(get()) } bind BattingRecordsDao::class
    factory { MainSearchViewModel(get()) }
}

