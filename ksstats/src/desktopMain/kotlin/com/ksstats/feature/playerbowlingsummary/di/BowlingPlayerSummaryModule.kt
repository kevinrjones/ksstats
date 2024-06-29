package com.ksstats.feature.playerbowlingsummary.di

import com.ksstats.feature.playerbowlingsummary.domain.usecase.PlayerBowlingSummaryUseCases
import com.ksstats.feature.playerbowlingsummary.domain.usecase.GetBowlingSummary
import com.ksstats.feature.playerbowlingsummary.data.repository.JooqBowlingRecordsRepository
import com.ksstats.feature.playerbowlingsummary.data.source.BowlingRecordsDao
import com.ksstats.feature.playerbowlingsummary.data.source.JooqBowlingRecordsDao
import com.ksstats.feature.playerbowlingsummary.domain.repository.BowlingRecordsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playerBowlingSummaryModule() = module {
    singleOf(::PlayerBowlingSummaryUseCases)
    singleOf(::GetBowlingSummary)
    singleOf(::JooqBowlingRecordsRepository) bind BowlingRecordsRepository::class
    factory { JooqBowlingRecordsDao(get()) } bind BowlingRecordsDao::class
}

