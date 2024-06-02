package com.ksstats.feature.recordsearch.feature.mainbattingsearch.di

import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.BattingRecordsDao
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.JooqBattingRecordsDao
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.presentation.BattingRecordsViewModel
import org.koin.dsl.bind
import org.koin.dsl.module

fun battingRecordsModule() = module {
    factory { JooqBattingRecordsDao(get()) } bind BattingRecordsDao::class
    factory { BattingRecordsViewModel(get()) }
}

