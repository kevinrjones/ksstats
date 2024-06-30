package com.ksstats.feature.recordsearch.data.source

import com.ksstats.core.types.MatchType
import com.ksstats.db.tables.references.*
import com.ksstats.feature.recordsearch.domain.model.*
import com.ksstats.shared.DatabaseConnections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.DSLContext
import org.jooq.Record1
import org.jooq.SelectSeekStep1
import org.jooq.SortField
import org.jooq.impl.DSL
import org.jooq.impl.DSL.and
import org.jooq.impl.DSL.select
import java.sql.DriverManager

class JooqRecordSearchDao(private val databaseConnections: DatabaseConnections) : RecordSearchDao {
    override fun getMatchTypes(): Flow<List<MatchTypeEntity>> = flow {

        val uniqueConnections = databaseConnections.connections.map { it.value }.distinct()

        val matchTypes = mutableListOf<MatchTypeEntity>()

        uniqueConnections.forEach { databaseConnection ->

            DriverManager.getConnection(
                databaseConnection.connectionString
            ).use { conn ->
                val context = DSL.using(conn, databaseConnection.dialect)
                val result = context.select(
                    MATCHTYPES.ID,
                    MATCHTYPES.MATCHTYPE,
                    MATCHTYPES.DESCRIPTION
                ).from(MATCHTYPES).fetch()


                result.forEach { matchSubType ->
                    val id = matchSubType.getValue("Id", Int::class.java)
                    val type = matchSubType.getValue("MatchType", String::class.java)
                    val description = matchSubType.getValue("Description", String::class.java)

                    matchTypes.add(MatchTypeEntity(id, type, description))
                }


            }
        }
        emit(matchTypes.distinctBy { it.type })
    }

    override fun getCompetitions(matchType: MatchType): Flow<List<Competition>> = flow {
        val databaseConnection = databaseConnections.connections[matchType.value]

        if(databaseConnection == null)
            throw Exception("Database connection for match type $matchType not found")

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
                .where(COMPETITIONS.MATCHTYPE.eq(matchType.value))
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


    override fun getGroundsForCompetitionAndCountry(matchType: MatchType, matchSubType: MatchType, countryId: Int): Flow<List<Ground>> = flow {
        val databaseConnection = databaseConnections.connections[matchType.value]

        if(databaseConnection == null)
            throw Exception("Database connection for match type $matchType not found")

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
                .where(GROUNDSMATCHTYPES.MATCHTYPE.eq(matchSubType.value))
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

    override fun getCountriesForCompetition(matchType: MatchType, matchSubType: MatchType): Flow<List<Country>> = flow {
        val databaseConnection = databaseConnections.connections[matchType.value]

        if(databaseConnection == null)
            throw Exception("Database connection for match type $matchType not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)
            val result = context.selectDistinct(
                GROUNDS.COUNTRYID,
                GROUNDS.COUNTRYNAME,
            ).from(GROUNDS)
                .join(GROUNDSMATCHTYPES).on(GROUNDSMATCHTYPES.GROUNDID.eq(GROUNDS.ID))
                .where(GROUNDSMATCHTYPES.MATCHTYPE.eq(matchSubType.value))
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

    override fun getSeriesDateForCompetition(matchType: MatchType, matchSubType: MatchType): Flow<List<String>> = flow {
        val databaseConnection = databaseConnections.connections[matchType.value]

        if(databaseConnection == null)
            throw Exception("Database connection for match type $matchType not found")

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
                                    matchSubType.value
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


    override fun getStartAndEndDatesForCompetition(matchType: MatchType, matchSubType: MatchType): Flow<StartEndDate> = flow {
        val databaseConnection = databaseConnections.connections[matchType.value]

        if(databaseConnection == null)
            throw Exception("Database connection for match type $matchType not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            var startDate: Long = 9999999999
            var endDate: Long = 9999999999

            val context = DSL.using(conn, databaseConnection.dialect)
            var orderBy = MATCHES.MATCHSTARTDATEASOFFSET.desc()

            var query = getInitialQuery(context, orderBy, matchSubType)

            val endDateQuery = query
                .limit(1)

            var result = endDateQuery.fetch()

            result.forEach { record ->
                endDate = record.getValue(MATCHES.MATCHSTARTDATEASOFFSET, Long::class.java)
            }

            orderBy = MATCHES.MATCHSTARTDATEASOFFSET.asc()
            query = getInitialQuery(context, orderBy, matchSubType)

            val startDateQuery = query
                .limit(1)

            result = startDateQuery.fetch()


            result.forEach { record ->
                startDate = record.getValue(MATCHES.MATCHSTARTDATEASOFFSET, Long::class.java)
            }

            emit(StartEndDate(startDate, endDate))
        }
    }


    override fun getTeamsForCompetitionAndCountry(matchType: MatchType, matchSubType: MatchType, countryId: Int): Flow<List<Team>> = flow {
        val databaseConnection = databaseConnections.connections[matchType.value]

        if(databaseConnection == null)
            throw Exception("Database connection for match type $matchType not found")

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
                                    matchSubType.value
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
        matchSubType: MatchType,
    ): SelectSeekStep1<Record1<Long?>, Long?> {
        val endInitialQuery = context.select(
            MATCHES.MATCHSTARTDATEASOFFSET
        ).from(MATCHES)
            .where(
                MATCHES.ID.`in`(
                    select(MATCHSUBTYPE.MATCHID).from(
                        MATCHSUBTYPE.where(
                            MATCHSUBTYPE.MATCHTYPE.eq(
                                matchSubType.value
                            )
                        )
                    )
                )
            ).and(MATCHES.MATCHSTARTDATEASOFFSET.isNotNull).orderBy(orderBy)
        return endInitialQuery
    }
}