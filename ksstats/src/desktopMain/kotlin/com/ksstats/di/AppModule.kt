package com.ksstats.di

import com.ksstats.feature.playersummary.di.playerSummaryModule
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.di.battingRecordsModule
import com.ksstats.feature.recordsearch.di.mainSearchModule
import com.ksstats.feature.showselection.di.selectionModule
import com.ksstats.feature.summary.di.summaryModule
import com.ksstats.shared.DatabaseConnection
import org.jooq.SQLDialect
import org.koin.dsl.module


fun appModule() = module {
    factory {
        DatabaseConnection(
            connectionString = "jdbc:sqlite:/Users/kevinjones/sqlite/cricket.sqlite",
            dialect = SQLDialect.SQLITE
        )
    }
    includes(selectionModule())
    includes(summaryModule())
    includes(battingRecordsModule())
    includes(mainSearchModule())
    includes(playerSummaryModule())
}