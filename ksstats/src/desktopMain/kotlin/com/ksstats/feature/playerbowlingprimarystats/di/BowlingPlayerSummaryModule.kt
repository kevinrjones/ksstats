package com.ksstats.feature.playerbowlingprimarystats.di

import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.PlayerBowlingSummaryUseCases
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetBowlingSummary
import com.ksstats.feature.playerbowlingprimarystats.domain.usecase.GetBowlingInningsByInnings
import com.ksstats.feature.playerbowlingprimarystats.data.repository.JooqBowlingRecordsRepository
import com.ksstats.feature.playerbowlingprimarystats.data.source.BowlingRecordsDao
import com.ksstats.feature.playerbowlingprimarystats.data.source.JooqBowlingRecordsDao
import com.ksstats.feature.playerbowlingprimarystats.domain.repository.BowlingRecordsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playerBowlingSummaryModule() = module {
    singleOf(::PlayerBowlingSummaryUseCases)
    singleOf(::GetBowlingSummary)
    singleOf(::GetBowlingInningsByInnings)
    singleOf(::JooqBowlingRecordsRepository) bind BowlingRecordsRepository::class
    factory { JooqBowlingRecordsDao(get()) } bind BowlingRecordsDao::class
}

