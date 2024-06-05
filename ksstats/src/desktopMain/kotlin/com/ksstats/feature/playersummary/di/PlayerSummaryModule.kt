package com.ksstats.feature.playersummary.di

import com.ksstats.feature.playersummary.domain.usecase.PlayerSummaryUseCases
import com.ksstats.feature.playersummary.domain.usecase.GetPlayerSummary
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.repository.JooqBattingRecordsRepository
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.BattingRecordsDao
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.JooqBattingRecordsDao
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.domain.repository.BattingRecordsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playerSummaryModule() = module {
    singleOf(::PlayerSummaryUseCases)
    singleOf(::GetPlayerSummary)
    singleOf(::JooqBattingRecordsRepository) bind BattingRecordsRepository::class
    factory { JooqBattingRecordsDao(get()) } bind BattingRecordsDao::class
}

