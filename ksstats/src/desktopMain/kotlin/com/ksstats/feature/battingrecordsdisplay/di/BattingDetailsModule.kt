package com.ksstats.feature.battingrecordsdisplay.di

import com.ksstats.feature.battingrecordsdisplay.domain.usecase.BattingDetailsUseCases
import com.ksstats.feature.battingrecordsdisplay.domain.usecase.GetBattingDetails
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.repository.JooqBattingRecordsRepository
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.BattingRecordsDao
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.JooqBattingRecordsDao
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.domain.repository.BattingRecordsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun battingDetailsModule() = module {
    singleOf(::BattingDetailsUseCases)
    singleOf(::GetBattingDetails)
    singleOf(::JooqBattingRecordsRepository) bind BattingRecordsRepository::class
    factory { JooqBattingRecordsDao(get()) } bind BattingRecordsDao::class
}

