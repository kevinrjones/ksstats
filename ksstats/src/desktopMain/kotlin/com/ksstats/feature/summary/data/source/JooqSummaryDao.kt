package com.ksstats.feature.summary.data.source

import com.ksstats.db.tables.references.*
import com.ksstats.feature.summary.domain.model.SummaryResult
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.shared.DatabaseConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.impl.DSL
import java.sql.DriverManager

class JooqSummaryDao(private val databaseConnection: DatabaseConnection) : SummaryDao {
    override fun getSearchSummary(searchParameters: SummarySearchParameters): Flow<SummaryResult> = flow {
        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)
            val teamName = if (searchParameters.teamId == 0) {
                "All"
            } else {
                context
                    .select(TEAMS.NAME)
                    .from(TEAMS)
                    .where(TEAMS.ID.eq(searchParameters.teamId))
                    .limit(1)
                    .fetch()
                    .map { it -> it.get(TEAMS.NAME, String::class.java) }.first()
            }
            val opponentsName =
                if (searchParameters.opponentsId == 0) {
                    "All"
                } else {
                    context
                        .select(TEAMS.NAME)
                        .from(TEAMS)
                        .where(TEAMS.ID.eq(searchParameters.opponentsId))
                        .limit(1)
                        .fetch()
                        .map { it -> it.get(TEAMS.NAME, String::class.java) }.first()
                }
            val hostCountry = if (searchParameters.hostCountryId == 0) {
                "All"
            } else {
                context
                    .select(GROUNDS.COUNTRYNAME, GROUNDSMATCHTYPES.MATCHTYPE)
                    .from(GROUNDS)
                    .join(GROUNDSMATCHTYPES).on(GROUNDS.ID.eq(GROUNDSMATCHTYPES.GROUNDID))
                    .where(GROUNDS.COUNTRYID.eq(searchParameters.hostCountryId))
                    .limit(1)
                    .fetch()
                    .map { it -> it.get(GROUNDS.COUNTRYNAME, String::class.java) }.first()
            }

            val ground = if (searchParameters.groundId == 0) {
                "All"
            } else {
                /*
                @"SELECT Id, GroundId, KnownAs, CountryId, CountryName from grounds where id=@id";
                */
                context
                    .select(GROUNDS.KNOWNAS)
                    .from(GROUNDS)
                    .where(GROUNDS.ID.eq(searchParameters.groundId))
                    .fetch()
                    .map { it -> it.get(GROUNDS.KNOWNAS, String::class.java) }.first()
            }
            val matchType = context
                .select(MATCHTYPES.DESCRIPTION)
                .from(MATCHTYPES.where(MATCHTYPES.MATCHTYPE.eq(searchParameters.matchType)))
                .map { it -> it.get(MATCHTYPES.DESCRIPTION, String::class.java) }.first()
            val competition = context
                .select(COMPETITIONS.COMPETITION)
                .from(COMPETITIONS.where(COMPETITIONS.MATCHSUBTYPE.eq(searchParameters.matchSubType)))
                .map { it -> it.get(COMPETITIONS.COMPETITION, String::class.java) }.first()

            emit(SummaryResult(
                team = teamName,
                opponents = opponentsName,
                matchType = matchType,
                competition = competition,
                ground = ground,
                hostCountry = hostCountry
            ))
        }

    }

}