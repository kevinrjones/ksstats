package com.ksstats.di

import com.ksstats.feature.playerbattingprimarystats.di.playerBattingSummaryModule
import com.ksstats.feature.playerbowlingprimarystats.di.playerBowlingSummaryModule
import com.ksstats.feature.playerfieldingprimarystats.di.playerFieldingSummaryModule
import com.ksstats.feature.recordsearch.di.mainSearchModule
import com.ksstats.feature.showselection.di.selectionModule
import com.ksstats.feature.summary.di.summaryModule
import com.ksstats.shared.DatabaseConnection
import com.ksstats.shared.DatabaseConnections
import org.jooq.SQLDialect
import org.koin.dsl.module


fun appModule(databaseConnections: DatabaseConnections) = module {
    factory {
        databaseConnections
    }
    factory {
        DatabaseConnection(
            connectionString = databaseConnections.connections["f"]!!.connectionString,
            dialect = SQLDialect.SQLITE
        )
    }
    includes(selectionModule())
    includes(summaryModule())
    includes(mainSearchModule())
    includes(playerBattingSummaryModule())
    includes(playerBowlingSummaryModule())
    includes(playerFieldingSummaryModule())
}