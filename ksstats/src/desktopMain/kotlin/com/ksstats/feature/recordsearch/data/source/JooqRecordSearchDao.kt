package com.ksstats.feature.recordsearch.data.source

import com.ksstats.db.tables.references.*
import com.ksstats.feature.recordsearch.domain.model.*
import com.ksstats.shared.DatabaseConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.DSLContext
import org.jooq.Record1
import org.jooq.SelectSeekStep1
import org.jooq.SortField
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import java.sql.DriverManager

class JooqRecordSearchDao(private val databaseConnection: DatabaseConnection) : RecordSearchDao {
    override fun getMatchTypes(): Flow<List<MatchType>> = flow {
        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)
            val result = context.select(
                MATCHTYPES.ID,
                MATCHTYPES.MATCHTYPE,
                MATCHTYPES.DESCRIPTION
            ).from(MATCHTYPES).fetch()

            val matchTypes = mutableListOf<MatchType>()
            result.forEach { matchSubType ->
                val id = matchSubType.getValue("Id", Int::class.java)
                val type = matchSubType.getValue("MatchType", String::class.java)
                val description = matchSubType.getValue("Description", String::class.java)

                matchTypes.add(MatchType(id, type, description))
            }

            emit(matchTypes)

        }
    }

    override fun getCompetitions(matchType: String): Flow<List<Competition>> = flow {
        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)
            val result = context.select(
                COMPETITIONS.ID,
                COMPETITIONS.MATCHTYPE,
                COMPETITIONS.MATCHSUBTYPE,
                COMPETITIONS.COMPETITION
            ).from(COMPETITIONS)
                .where(COMPETITIONS.MATCHTYPE.eq(matchType))
                .fetch()

            val competitions = mutableListOf<Competition>()
            result.forEach { matchSubType ->
                val id = matchSubType.getValue("Id", Int::class.java)
                val type = matchSubType.getValue("MatchType", String::class.java)
                val subtype = matchSubType.getValue("MatchSubType", String::class.java)
                val description = matchSubType.getValue("Competition", String::class.java)

                competitions.add(Competition(id, type, subtype, description))
            }

            emit(competitions)

        }
    }

    override fun getTeamsForCompetitionEx(matchType: String): Flow<List<Team>> = flow {

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)
            val result = context.select(
                TEAMSMATCHTYPES.teams.ID,
                TEAMSMATCHTYPES.teams.NAME,
            ).from(TEAMSMATCHTYPES)
                .where(TEAMSMATCHTYPES.MATCHTYPE.eq(matchType))
                .orderBy(TEAMSMATCHTYPES.teams.NAME)
                .fetch()

            val teams = mutableListOf<Team>()
            result.forEach { matchSubType ->
                val id = matchSubType.getValue("Id", Int::class.java)
                val name = matchSubType.getValue("Name", String::class.java)

                teams.add(Team(id, name))
            }

            teams.add(0, Team(0, "All"))
            emit(teams)

        }
    }

    override fun getGroundsForCompetitionAndCountry(matchType: String, countryId: Int): Flow<List<Ground>> = flow {


        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)

            val countryClause = if (countryId == 0) {
                and(GROUNDSMATCHTYPES.grounds.COUNTRYID.ne(0))
            } else {
                and(
                    GROUNDSMATCHTYPES.grounds.COUNTRYID
                        .eq(countryId)
                )
            }

            val result = context.select(
                GROUNDSMATCHTYPES.GROUNDID,
                GROUNDSMATCHTYPES.grounds.KNOWNAS,
            ).from(GROUNDSMATCHTYPES)
                .where(GROUNDSMATCHTYPES.MATCHTYPE.eq(matchType))
                .and(countryClause)
                .orderBy(GROUNDSMATCHTYPES.grounds.KNOWNAS)
                .fetch()

            val grounds = mutableListOf<Ground>()
            result.forEach { matchSubType ->
                val id = matchSubType.getValue("GroundId", Int::class.java)
                val name = matchSubType.getValue("KnownAs", String::class.java)

                grounds.add(Ground(id, name))
            }

            grounds.add(0, Ground(0, "All"))
            emit(grounds)

        }
    }

    override fun getCountriesForCompetition(matchType: String): Flow<List<Country>> = flow {

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)
            val result = context.selectDistinct(
                GROUNDS.COUNTRYID,
                GROUNDS.COUNTRYNAME,
            ).from(GROUNDS)
                .join(GROUNDSMATCHTYPES).on(GROUNDSMATCHTYPES.GROUNDID.eq(GROUNDS.ID))
                .where(GROUNDSMATCHTYPES.MATCHTYPE.eq(matchType))
                .orderBy(GROUNDS.COUNTRYNAME)
                .fetch()

            val countries = mutableListOf<Country>()
            result.forEach { matchSubType ->
                val id = matchSubType.getValue("CountryId", Int::class.java)
                val name = matchSubType.getValue("CountryName", String::class.java)

                countries.add(Country(id, name))
            }

            countries.add(0, Country(0, "All"))

            emit(countries)

        }
    }

    override fun getSeriesDateForCompetition(matchType: String): Flow<List<String>> = flow {

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)
            val result = context.selectDistinct(
                MATCHES.SERIESDATE
            ).from(MATCHES)
                .where(
                    MATCHES.ID.`in`(
                        DSL.select(MATCHSUBTYPE.MATCHID).from(
                            MATCHSUBTYPE.where(
                                MATCHSUBTYPE.MATCHTYPE.eq(
                                    matchType
                                )
                            )
                        )
                    )
                )
                .fetch()

            val seriesDates = mutableListOf<String>()
            result.forEach { record ->
                val date = record.getValue("SeriesDate", String::class.java)

                seriesDates.add(date)
            }

            seriesDates.add(0, "All")

            emit(seriesDates)

        }
    }


    override fun getStartAndEndDatesForCompetition(matchType: String): Flow<StartEndDate> = flow {
        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            var startDate: Long = 9999999999
            var endDate: Long = 9999999999

            val context = DSL.using(conn, databaseConnection.dialect)
            var orderBy = MATCHES.MATCHSTARTDATEASOFFSET.desc()

            var query = getInitialQuery(context, orderBy, matchType)

            val endDateQuery = query
                .limit(1)

            var result = endDateQuery.fetch()

            result.forEach { record ->
                endDate = record.getValue(MATCHES.MATCHSTARTDATEASOFFSET, Long::class.java)
            }

            orderBy = MATCHES.MATCHSTARTDATEASOFFSET.asc()
            query = getInitialQuery(context, orderBy, matchType)

            val startDateQuery = query
                .limit(1)

            result = startDateQuery.fetch()


            result.forEach { record ->
                startDate = record.getValue(MATCHES.MATCHSTARTDATEASOFFSET, Long::class.java)
            }

            emit(StartEndDate(startDate, endDate))
        }
    }


    override fun getTeamsForCompetitionAndCountry(matchType: String, countryId: Int): Flow<List<Team>> = flow {

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)

            val countryClause = if (countryId == 0) {
                and(MATCHES.HOMECOUNTRYID.ne(0))
            } else {
                and(
                    MATCHES.HOMECOUNTRYID.eq(countryId)
                )
            }

            val result = context.selectDistinct(TEAMS.ID, TEAMS.NAME)
                .from(MATCHES)
                .join(EXTRAMATCHDETAILS).on(EXTRAMATCHDETAILS.MATCHID.eq(MATCHES.ID))
                .join(TEAMS).on(TEAMS.ID.eq(EXTRAMATCHDETAILS.TEAMID))
                .where(
                    MATCHES.ID.`in`(
                        select(MATCHSUBTYPE.MATCHID).from(
                            MATCHSUBTYPE.where(
                                MATCHSUBTYPE.MATCHTYPE.eq(
                                    matchType
                                )
                            )
                        )
                    )
                )
                .and(countryClause)
                .and(TEAMS.ID.ne(1))
                .orderBy(TEAMS.NAME)
                .fetch()

            val teams = mutableListOf<Team>()
            result.forEach { matchSubType ->
                val id = matchSubType.getValue("Id", Int::class.java)
                val name = matchSubType.getValue("Name", String::class.java)

                teams.add(Team(id, name))
            }

            teams.add(0, Team(0, "All"))
            emit(teams)
        }
    }

    private fun getInitialQuery(
        context: DSLContext,
        orderBy: SortField<Long?>,
        matchType: String,
    ): SelectSeekStep1<Record1<Long?>, Long?> {
        val endInitialQuery = context.select(
            MATCHES.MATCHSTARTDATEASOFFSET
        ).from(MATCHES)
            .where(
                MATCHES.ID.`in`(
                    select(MATCHSUBTYPE.MATCHID).from(
                        MATCHSUBTYPE.where(
                            MATCHSUBTYPE.MATCHTYPE.eq(
                                matchType
                            )
                        )
                    )
                )
            ).and(MATCHES.MATCHSTARTDATEASOFFSET.isNotNull).orderBy(orderBy)
        return endInitialQuery
    }
}