package com.ksstats.di

import com.ksstats.feature.mainbatting.battingrecords.di.battingDetailsModule
import com.ksstats.feature.mainbatting.di.battingRecordsModule
import com.ksstats.feature.showselection.di.selectionModule
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
    includes(battingRecordsModule())
    includes(battingDetailsModule())
}