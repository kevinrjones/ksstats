package com.ksstats.di

import com.ksstats.feature.mainbatting.battingrecords.di.battingDetailsModule
import com.ksstats.feature.mainbatting.di.battingRecordsModule
import com.ksstats.feature.showselection.di.selectionModule
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.shared.DatabaseConnection
import org.jooq.SQLDialect
import org.koin.dsl.module
import com.ksstats.core.domain.util.SavedStateStore
import com.ksstats.core.domain.util.MapSavedStateStore
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf


fun appModule() = module {
    factory {
        DatabaseConnection(
            connectionString = "jdbc:sqlite:/Users/kevinjones/sqlite/cricket.sqlite",
            dialect = SQLDialect.SQLITE
        )
    }
    singleOf(::MapSavedStateStore) { bind<SavedStateStore<SearchParameters>>() }
    includes(selectionModule())
    includes(battingRecordsModule())
    includes(battingDetailsModule())
}