package com.ksstats.feature.battingrecordsdisplay.di

import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.BattingRecordsDao
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.JooqBattingRecordsDao
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


fun mainSearchModule() = module {
    singleOf(::JooqBattingRecordsDao) bind BattingRecordsDao::class
    factory { JooqBattingRecordsDao(get()) } bind BattingRecordsDao::class
}
