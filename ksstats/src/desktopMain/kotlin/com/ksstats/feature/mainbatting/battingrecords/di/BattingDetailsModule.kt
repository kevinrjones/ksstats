package com.ksstats.feature.mainbatting.battingrecords.di

import com.ksstats.feature.mainbatting.battingrecords.domain.usecase.BattingDetailsUseCases
import com.ksstats.feature.mainbatting.battingrecords.domain.usecase.GetBattingDetails
import com.ksstats.feature.mainbatting.battingrecords.presentation.BattingDetailsScreenViewModel
import com.ksstats.feature.mainbatting.data.repository.JooqBattingRecordsRepository
import com.ksstats.feature.mainbatting.data.source.BattingRecordsDao
import com.ksstats.feature.mainbatting.data.source.JooqBattingRecordsDao
import com.ksstats.feature.mainbatting.domain.repository.BattingRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun battingDetailsModule() = module {
    singleOf(::BattingDetailsUseCases)
    singleOf(::GetBattingDetails)
    singleOf(::JooqBattingRecordsRepository) bind BattingRepository::class
    singleOf(::JooqBattingRecordsDao) bind BattingRecordsDao::class
    factory { JooqBattingRecordsDao(get()) } bind BattingRecordsDao::class
    factory { BattingDetailsScreenViewModel(get(), get()) }
}
