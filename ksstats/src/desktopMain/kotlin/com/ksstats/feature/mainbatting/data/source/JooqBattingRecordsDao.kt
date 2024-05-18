package com.ksstats.feature.mainbatting.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.db.tables.references.*
import com.ksstats.feature.mainbatting.battingrecords.data.BattingSearchResults
import com.ksstats.feature.mainbatting.data.source.JooqBattingCareerRecords.createResultsCte
import com.ksstats.feature.mainbatting.data.source.JooqBattingCareerRecords.createTemporaryTeamsCte
import com.ksstats.feature.mainbatting.data.source.JooqBattingCareerRecords.totalCountsCte
import com.ksstats.feature.mainbatting.domain.model.*
import com.ksstats.shared.DatabaseConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.select
import java.sql.DriverManager

class JooqBattingRecordsDao(private val databaseConnection: DatabaseConnection) : BattingRecordsDao {
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

    override fun getTeamsForCompetition(matchType: String): Flow<List<Team>> = flow {

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

    override fun getGroundsForCompetition(matchType: String): Flow<List<Ground>> = flow {

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->
            val context = DSL.using(conn, databaseConnection.dialect)
            val result = context.select(
                GROUNDSMATCHTYPES.ID,
                GROUNDSMATCHTYPES.grounds.KNOWNAS,
            ).from(GROUNDSMATCHTYPES)
                .where(GROUNDSMATCHTYPES.MATCHTYPE.eq(matchType))
                .orderBy(GROUNDSMATCHTYPES.grounds.KNOWNAS)
                .fetch()

            val grounds = mutableListOf<Ground>()
            result.forEach { matchSubType ->
                val id = matchSubType.getValue("Id", Int::class.java)
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
                        select(MATCHSUBTYPE.MATCHID).from(
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

    override fun getBattingDetails(searchParameters: SearchParameters): Flow<List<BattingSearchResults>> = flow {
        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteStep1Name = "bcrto_tmp_bat"
            val cteStep2Name = "bcrto_tmp_teams"
            val cteStep3Name = "bcrto_results"
            val cteStep4Name = "bcrto_total_counts"
            val bcrto_tmp_bat =
                JooqBattingCareerRecords.createTemporaryBattingCte(searchParameters)
            val bcrto_tmp_teams = createTemporaryTeamsCte(searchParameters)
            val bcrto_results = createResultsCte(searchParameters, cteStep1Name, cteStep2Name)
            val bcrto_count = totalCountsCte(cteStep3Name)

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteStep1Name).`as`(bcrto_tmp_bat)
                .with(cteStep2Name).`as`(bcrto_tmp_teams)
                .with(cteStep3Name).`as`(bcrto_results)
                .with(cteStep4Name).`as`(bcrto_count)
                .select().from(cteStep3Name).join(cteStep4Name).on()
                .orderBy(sortSpecification)
                .limit(0, 50).fetch()

            val results = mutableListOf<BattingSearchResults>()
            databaseResults.forEach {
                results.add(
                    BattingSearchResults(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("teams", String::class.java),
                        sortNamePart = it.getValue("SortNamePart", String::class.java),
                        matches = it.getValue("matches", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        notOuts = it.getValue("notouts", Int::class.java),
                        runs = it.getValue("runs", Int::class.java),
                        highestScore = it.getValue("highestscore", Double::class.java),
                        hundreds = it.getValue("hundreds", Int::class.java),
                        fifties = it.getValue("fifties", Int::class.java),
                        ducks = it.getValue("ducks", Int::class.java),
                        fours = it.getValue("fours", Int::class.java),
                        sixes = it.getValue("sixes", Int::class.java),
                        balls = it.getValue("balls", Int::class.java),
                        average = it.getValue("avg", Double::class.java),
                        strikeRate = it.getValue("sr", Double::class.java),
                        battingImpact = it.getValue("bi", Double::class.java),
                        count = it.getValue("balls", Int::class.java),
                        opponents = "",
                        year = "",
                        ground = "",
                        countryName = "",
                    )
                )
            }

            emit(results)
        }
    }
}
