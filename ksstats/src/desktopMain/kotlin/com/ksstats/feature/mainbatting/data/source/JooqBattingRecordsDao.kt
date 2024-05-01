package com.ksstats.feature.mainbatting.data.source

import com.ksstats.db.tables.references.MATCHES
import com.ksstats.feature.mainbatting.domain.model.MatchTypes
import com.ksstats.shared.DatabaseConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.impl.DSL
import java.sql.DriverManager

class JooqBattingRecordsDao(private val databaseConnection: DatabaseConnection) : BattingRecordsDao {
    override fun getMatchTypes(): Flow<MatchTypes> = flow {
        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)
            val result = context.select(MATCHES.MATCHTYPE).from(MATCHES).fetch()

            result.forEach { matchSubType ->
                val type = matchSubType.getValue("MatchType", String::class.java)
                emit(MatchTypes(1, type))
            }

        }
    }
}