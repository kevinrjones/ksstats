package com.ksstats.feature.playerbowlingprimarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.feature.playerbowlingprimarystats.data.InningsByInningsBowling
import com.ksstats.feature.playerbowlingprimarystats.data.PrimaryBowling
import com.ksstats.shared.DatabaseConnections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import java.sql.DriverManager

class JooqBowlingRecordsDao(private val databaseConnections: DatabaseConnections) : BowlingRecordsDao {
    override fun getBowlingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]

        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteStep1Name = "bocrto_tmp_bowl"
            val cteStep2Name = "bocrto_tmp_teams"
            val cteStep3Name = "bocrto_results"
            val cteStep4Name = "bocrto_total_counts"
            val cteStep5Name = "bocrto_fives"
            val cteStep6Name = "bocrto_tens"
            val cteStep9Name = "bocrto_bbm_all"
            val cteStep10Name = "bocrto_bbm"
            val bocrto_tmp_bowl =
                JooqBowlingCareerRecords.createTemporaryBowlingCte(searchParameters)
            val bocrto_tmp_teams = JooqBowlingCareerRecords.createTemporaryTeamsCte(searchParameters)
            val bocrto_results = JooqBowlingCareerRecords.createResultsCte(
                searchParameters,
                cteStep1Name,
                cteStep2Name,
                cteStep5Name,
                cteStep6Name,
                cteStep10Name
            )
            val bocrto_count = JooqBowlingCareerRecords.totalCountsCte(cteStep3Name)

            val bocrto_fives = JooqBowlingCareerRecords.createFives(cteStep1Name, searchParameters.fivesLimit)
            val bocrto_tens = JooqBowlingCareerRecords.createTens(cteStep1Name)
            val bocrto_bbm_all = JooqBowlingCareerRecords.createBbmAll(cteStep1Name)
            val bocrto_bbm = JooqBowlingCareerRecords.createBbm(cteStep9Name)

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteStep1Name).`as`(bocrto_tmp_bowl)
                .with(cteStep2Name).`as`(bocrto_tmp_teams)
                .with(cteStep3Name).`as`(bocrto_results)
                .with(cteStep4Name).`as`(bocrto_count)
                .with(cteStep5Name).`as`(bocrto_fives)
                .with(cteStep6Name).`as`(bocrto_tens)
                .with(cteStep9Name).`as`(bocrto_bbm_all)
                .with(cteStep10Name).`as`(bocrto_bbm)
                .select().from(cteStep3Name).join(cteStep4Name).on()
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize).fetch()
            var count = 0

            val results = mutableListOf<PrimaryBowling>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                results.add(
                    PrimaryBowling(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("teams", String::class.java),
                        sortNamePart = it.getValue("SortNamePart", String::class.java),
                        matches = it.getValue("matches", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        balls = it.getValue("balls", Int::class.java),
                        maidens = it.getValue("maidens", Int::class.java),
                        runs = it.getValue("runs", Int::class.java),
                        wickets = it.getValue("wickets", Int::class.java),
                        average = it.getValue("avg", Double::class.java),
                        economy = it.getValue("econ", Double::class.java),
                        strikeRate = it.getValue("sr", Double::class.java),
                        bowlingImpact = it.getValue("bi", Double::class.java),
                        noballs = it.getValue("noballs", Int::class.java),
                        wides = it.getValue("wides", Int::class.java),
                        dots = it.getValue("dots", Int::class.java),
                        fours = it.getValue("fours", Int::class.java),
                        sixes = it.getValue("sixes", Int::class.java),
                        count = it.getValue("count", Int::class.java),
                        fivefor = it.getValue("fivefor", Int::class.java),
                        tenfor = it.getValue("tenfor", Int::class.java),
                        bestBowlingInnings = it.getValue("bbi", Double::class.java),
                        bestBowlingMatch = it.getValue("bbm", Double::class.java),
                        opponents = "",
                        year = "",
                        ground = "",
                        countryName = "",
                    )
                )
            }
            val databaseResult = DatabaseResult(results, count)
            emit(databaseResult)
        }
    }

    override fun getBowlingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>> =
        flow {


            val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]

            if (databaseConnection == null)
                throw Exception("Database connection for match type ${searchParameters.matchType} not found")

            DriverManager.getConnection(
                databaseConnection.connectionString
            ).use { conn ->

                val context = DSL.using(conn, databaseConnection.dialect)

                val cteStep1Name = "tmp_bowl"
                val cteStepCountName = "total_counts"

                val tmp_bowl = JooqBowlingInningsByInningsRecords.createTemporaryBowlingCte(searchParameters)
                val count_cte = JooqBowlingInningsByInningsRecords.totalCountsCte(cteStep1Name)

                // playerwickets desc, playerruns, MatchStartDateAsOffset
                val sortSpecification = when (searchParameters.sortDirection) {
                    SortDirection.Ascending -> listOf(
                        field(searchParameters.sortOrder.name).asc(),
                        field("wickets").desc(),
                        field("runs"),
                        field("MatchStartDateAsOffset"),
                    )

                    SortDirection.Descending -> listOf(
                        field(searchParameters.sortOrder.name).desc(),
                        field("wickets").desc(),
                        field("runs").desc(),
                        field("MatchStartDateAsOffset").desc(),
                    )
                }
                val databaseResults = context
                    .with(cteStep1Name).`as`(tmp_bowl)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStep1Name)
                    .orderBy(sortSpecification)
                    .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                    .fetch()

                val countResult = context
                    .with(cteStep1Name).`as`(tmp_bowl)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStepCountName)
                    .fetch()

                val count = countResult.get(0).getValue("count", Int::class.java)

                val results = mutableListOf<InningsByInningsBowling>()
                databaseResults.forEach {
                    results.add(
                        InningsByInningsBowling(
                            playerId = it.getValue("Id", Int::class.java),
                            name = it.getValue("FullName", String::class.java),
                            team = it.getValue("Team", String::class.java),
                            matchDate = it.getValue("MatchStartDateAsOffset", Long::class.java),
                            ground = it.getValue("Ground", String::class.java),
                            opponents = it.getValue("Opponents", String::class.java),
                            ballsPerOver = it.getValue("BallsPerOver", Int::class.java),
                            balls = it.getValue("Balls", Int::class.java),
                            maidens = it.getValue("Maidens", Int::class.java),
                            dots = it.getValue("Dots", Int::class.java),
                            runs = it.getValue("Runs", Int::class.java),
                            wickets = it.getValue("Wickets", Int::class.java),
                            inningsNumber = it.getValue("InningsNumber", Int::class.java),
                            seriesDate = it.getValue("SeriesDate", String::class.java),
                            economy = it.getValue("econ", Double::class.java),
                            average = 0.0
                        )
                    )
                }
                val databaseResult = DatabaseResult(results, count)
                emit(databaseResult)
            }

        }

    override fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBowling>> =
        flow {


            val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]

            if (databaseConnection == null)
                throw Exception("Database connection for match type ${searchParameters.matchType} not found")

            DriverManager.getConnection(
                databaseConnection.connectionString
            ).use { conn ->

                val context = DSL.using(conn, databaseConnection.dialect)

                val cteStep1Name = "tmp_bowl"
                val cteStepCountName = "total_counts"

                val tmp_bowl = JooqBowlingMatchTotalsRecords.createTemporaryBowlingCte(searchParameters)
                val count_cte = JooqBowlingMatchTotalsRecords.totalCountsCte(cteStep1Name)

                // playerwickets desc, playerruns, MatchStartDateAsOffset
                val sortSpecification = when (searchParameters.sortDirection) {
                    SortDirection.Ascending -> listOf(
                        field(searchParameters.sortOrder.name).asc(),
                        field("wickets").desc(),
                        field("runs"),
                        field("MatchStartDateAsOffset"),
                    )

                    SortDirection.Descending -> listOf(
                        field(searchParameters.sortOrder.name).desc(),
                        field("wickets").desc(),
                        field("runs"),
                        field("MatchStartDateAsOffset").desc(),
                    )
                }
                val databaseResults = context
                    .with(cteStep1Name).`as`(tmp_bowl)
                    .with(cteStepCountName).`as`(count_cte)
                    .select(
                        asterisk(),
                        iif(
                            field("balls", Int::class.java).eq(0),
                            0,
                            field("runs", Int::class.java).div(
                                cast(
                                    field("balls", Int::class.java),
                                    Double::class.java
                                )
                            )
                                .mul(6),
                        ).`as`("econ"),
                    )
                    .from(cteStep1Name)
                    .where(field("match_synbb").isNotNull)
                    .and(field("rn", Int::class.java).eq(1))
                    .and(field("balls").gt(0))
                    .and(field("wickets").gt(searchParameters.wicketsLimit))
                    .orderBy(sortSpecification)
                    .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                    .fetch()

                val countResult = context
                    .with(cteStep1Name).`as`(tmp_bowl)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStepCountName)
                    .fetch()

                val count = countResult.get(0).getValue("count", Int::class.java)

                val results = mutableListOf<InningsByInningsBowling>()
                databaseResults.forEach {
                    results.add(
                        InningsByInningsBowling(
                            playerId = it.getValue("Id", Int::class.java),
                            name = it.getValue("FullName", String::class.java),
                            team = it.getValue("Team", String::class.java),
                            matchDate = it.getValue("MatchStartDateAsOffset", Long::class.java),
                            ground = it.getValue("Ground", String::class.java),
                            opponents = it.getValue("Opponents", String::class.java),
                            ballsPerOver = it.getValue("BallsPerOver", Int::class.java),
                            balls = it.getValue("Balls", Int::class.java),
                            maidens = it.getValue("Maidens", Int::class.java),
                            dots = it.getValue("Dots", Int::class.java),
                            runs = it.getValue("Runs", Int::class.java),
                            wickets = it.getValue("Wickets", Int::class.java),
                            inningsNumber = it.getValue("InningsNumber", Int::class.java),
                            seriesDate = it.getValue("SeriesDate", String::class.java),
                            economy = it.getValue("econ", Double::class.java),
                            average = 0.0
                        )
                    )
                }
                val databaseResult = DatabaseResult(results, count)
                emit(databaseResult)
            }

        }


    override fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]

        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteStep1Name = "bocrto_tmp_bowl"
            val cteStep3Name = "bocrto_results"
            val cteStep4Name = "bocrto_total_counts"
            val cteStep5Name = "bocrto_fives"
            val cteStep6Name = "bocrto_tens"
            val cteStep9Name = "bocrto_bbm_all"
            val cteStep10Name = "bocrto_bbm"
            val bocrto_tmp_bowl =
                JooqBowlingSeriesRecords.createTemporaryBowlingCte(searchParameters)
            val bocrto_results = JooqBowlingSeriesRecords.createResultsCte(
                searchParameters,
                cteStep1Name,
                cteStep5Name,
                cteStep6Name,
                cteStep10Name
            )
            val bocrto_count = JooqBowlingSeriesRecords.totalCountsCte(cteStep3Name)

            val bocrto_fives = JooqBowlingSeriesRecords.createFives(cteStep1Name, searchParameters.fivesLimit)
            val bocrto_tens = JooqBowlingSeriesRecords.createTens(cteStep1Name)
            val bocrto_bbm_all = JooqBowlingSeriesRecords.createBbmAll(cteStep1Name)
            val bocrto_bbm = JooqBowlingSeriesRecords.createBbm(cteStep9Name)

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteStep1Name).`as`(bocrto_tmp_bowl)
                .with(cteStep3Name).`as`(bocrto_results)
                .with(cteStep4Name).`as`(bocrto_count)
                .with(cteStep5Name).`as`(bocrto_fives)
                .with(cteStep6Name).`as`(bocrto_tens)
                .with(cteStep9Name).`as`(bocrto_bbm_all)
                .with(cteStep10Name).`as`(bocrto_bbm)
                .select().from(cteStep3Name).join(cteStep4Name).on()
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize).fetch()
            var count = 0

            val results = mutableListOf<PrimaryBowling>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                results.add(
                    PrimaryBowling(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("team", String::class.java),
                        sortNamePart = it.getValue("SortNamePart", String::class.java),
                        matches = it.getValue("matches", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        balls = it.getValue("balls", Int::class.java),
                        maidens = it.getValue("maidens", Int::class.java),
                        runs = it.getValue("runs", Int::class.java),
                        wickets = it.getValue("wickets", Int::class.java),
                        average = it.getValue("avg", Double::class.java),
                        economy = it.getValue("econ", Double::class.java),
                        strikeRate = it.getValue("sr", Double::class.java),
                        bowlingImpact = it.getValue("bi", Double::class.java),
                        noballs = it.getValue("noballs", Int::class.java),
                        wides = it.getValue("wides", Int::class.java),
                        dots = it.getValue("dots", Int::class.java),
                        fours = it.getValue("fours", Int::class.java),
                        sixes = it.getValue("sixes", Int::class.java),
                        count = it.getValue("count", Int::class.java),
                        fivefor = it.getValue("fivefor", Int::class.java),
                        tenfor = it.getValue("tenfor", Int::class.java),
                        bestBowlingInnings = it.getValue("bbi", Double::class.java),
                        bestBowlingMatch = it.getValue("bbm", Double::class.java),
                        opponents = "",
                        year = it.getValue("year", String::class.java),
                        ground = "",
                        countryName = "",
                    )
                )
            }
            val databaseResult = DatabaseResult(results, count)
            emit(databaseResult)
        }
    }

    override fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        TODO("Not yet implemented")
    }

    override fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        TODO("Not yet implemented")
    }

    override fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        TODO("Not yet implemented")
    }

    override fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        TODO("Not yet implemented")
    }

    override fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBowling>> {
        TODO("Not yet implemented")
    }
}
