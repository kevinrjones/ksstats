package com.ksstats.feature.playerbattingprimarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.feature.playerbattingprimarystats.data.InningsByInningsBatting
import com.ksstats.feature.playerbattingprimarystats.data.PrimaryBatting
import com.ksstats.shared.DatabaseConnections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import java.sql.DriverManager

class JooqBattingRecordsDao(private val databaseConnections: DatabaseConnections) : BattingRecordsDao {
    override fun getBattingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteStep1Name = "bcrto_tmp_bat"
            val cteStep2Name = "bcrto_tmp_teams"
            val cteStep3Name = "bcrto_results"
            val cteStepCountName = "total_counts"
            val bcrto_tmp_bat =
                JooqBattingCareerRecords.createTemporaryBattingCte(searchParameters)
            val bcrto_tmp_teams = JooqBattingCareerRecords.createTemporaryTeamsCte(searchParameters)
            val bcrto_results = JooqBattingCareerRecords.createResultsCte(searchParameters, cteStep1Name, cteStep2Name)
            val count_cte = JooqBattingCareerRecords.totalCountsCte(cteStep3Name)

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteStep1Name).`as`(bcrto_tmp_bat)
                .with(cteStep2Name).`as`(bcrto_tmp_teams)
                .with(cteStep3Name).`as`(bcrto_results)
                .with(cteStepCountName).`as`(count_cte)
                .select().from("$cteStep3Name, $cteStepCountName")
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize).fetch()

            var count = 0

            val results = mutableListOf<PrimaryBatting>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                results.add(
                    PrimaryBatting(
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

    override fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteStep1Name = "tmp_bat"
            val cteStep2Name = "results"
            val cteStepCountName = "total_counts"
            val bcrto_tmp_bat =
                JooqBattingSeriesRecords.createTemporaryBattingCte(searchParameters)
            val bcrto_results = JooqBattingSeriesRecords.createResultsCte(searchParameters, cteStep1Name)
            val count_cte = JooqBattingSeriesRecords.totalCountsCte(cteStep2Name)

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteStep1Name).`as`(bcrto_tmp_bat)
                .with(cteStep2Name).`as`(bcrto_results)
                .with(cteStepCountName).`as`(count_cte)
                .select().from("$cteStep2Name, $cteStepCountName")
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize).fetch()

            var count = 0

            val results = mutableListOf<PrimaryBatting>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                results.add(
                    PrimaryBatting(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("team", String::class.java),
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
                        opponents = it.get("opponents", String::class.java),
                        year = it.get("year", String::class.java),
                        ground = "",
                        countryName = "",
                    )
                )
            }

            val databaseResult = DatabaseResult(results, count)

            emit(databaseResult)
        }
    }

    override fun getBattingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBatting>> =
        flow {
            val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]

            if (databaseConnection == null)
                throw Exception("Database connection for match type ${searchParameters.matchType} not found")

            DriverManager.getConnection(
                databaseConnection.connectionString
            ).use { conn ->

                val context = DSL.using(conn, databaseConnection.dialect)

                val cteStep1Name = "tmp_bat"
                val cteStepCountName = "total_counts"

                val tmp_bat =
                    JooqBattingInningsByInningsRecords.createTemporaryBattingCte(searchParameters)

                val count_cte = JooqBattingInningsByInningsRecords.totalCountsCte(cteStep1Name)

                val sortSpecification = when (searchParameters.sortDirection) {
                    SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                    SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
                }


                val databaseResults = context
                    .with(cteStep1Name).`as`(tmp_bat)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStep1Name)
                    .orderBy(sortSpecification, field("SortNamePart"))
                    .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                    .fetch()

                val countResult = context
                    .with(cteStep1Name).`as`(tmp_bat)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStepCountName)
                    .fetch()

                val count = countResult.get(0).getValue("count", Int::class.java)

                val results = mutableListOf<InningsByInningsBatting>()
                databaseResults.forEach {
                    results.add(
                        InningsByInningsBatting(
                            playerId = it.getValue("playerid", Int::class.java),
                            name = it.getValue("FullName", String::class.java),
                            score = it.getValue("score", Int::class.java),
                            notOut = it.getValue("notOut", Int::class.java),
                            position = it.getValue("position", Int::class.java),
                            team = it.getValue("team", String::class.java),
                            matchDate = it.getValue("matchStartDateAsOffset", Long::class.java),
                            ground = it.getValue("ground", String::class.java),
                            opponents = it.getValue("opponents", String::class.java),
                            innings = it.getValue("inningsNumber", Int::class.java),
                            strikeRate = it.getValue("sr", Double::class.java),
                            sixes = it.getValue("sixes", Int::class.java),
                            fours = it.getValue("fours", Int::class.java),
                            balls = it.getValue("balls", Int::class.java),
                            minutes = it.getValue("minutes", Int::class.java),
                        )
                    )
                }

                val databaseResult = DatabaseResult(results, count)


                emit(databaseResult)
            }
        }

    override fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsBatting>> =
        flow {
            val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]

            if (databaseConnection == null)
                throw Exception("Database connection for match type ${searchParameters.matchType} not found")

            DriverManager.getConnection(
                databaseConnection.connectionString
            ).use { conn ->

                val context = DSL.using(conn, databaseConnection.dialect)

                val cteStep1Name = "tmp_bat"
                val cteStep2Name = "tmp_bat_details"
                val cteStepCountName = "total_counts"

                val tmp_bat =
                    JooqBattingMatchTotalsRecords.createTemporaryBattingCte(searchParameters)
                val tmp_bat_details =
                    JooqBattingMatchTotalsRecords.createTemporaryBattingDetailsCte(cteStep1Name, searchParameters)

                val count_cte = JooqBattingMatchTotalsRecords.totalCountsCte(cteStep2Name)

                val sortSpecification = when (searchParameters.sortDirection) {
                    SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                    SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
                }


                val databaseResults = context
                    .with(cteStep1Name).`as`(tmp_bat)
                    .with(cteStep2Name).`as`(tmp_bat_details)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStep2Name)
                    .orderBy(sortSpecification, field("SortNamePart"))
                    .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                    .fetch()

                val countResult = context
                    .with(cteStep1Name).`as`(tmp_bat)
                    .with(cteStep2Name).`as`(tmp_bat_details)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStepCountName)
                    .fetch()

                val count = countResult.get(0).getValue("count", Int::class.java)

                val results = mutableListOf<InningsByInningsBatting>()
                databaseResults.forEach {
                    results.add(
                        InningsByInningsBatting(
                            playerId = it.getValue("playerid", Int::class.java),
                            name = it.getValue("FullName", String::class.java),
                            score = it.getValue("score", Int::class.java),
                            notOut = 0,
                            position = 0,
                            matchDate = it.getValue("matchStartDateAsOffset", Long::class.java),
                            ground = it.getValue("ground", String::class.java),
                            opponents = it.getValue("opponents", String::class.java),
                            innings = 0,
                            strikeRate = it.getValue("sr", Double::class.java),
                            sixes = it.getValue("sixes", Int::class.java),
                            fours = it.getValue("fours", Int::class.java),
                            balls = it.getValue("balls", Int::class.java),
                            minutes = it.getValue("minutes", Int::class.java),
                            team = it.getValue("team", String::class.java)
                        )
                    )
                }

                val databaseResult = DatabaseResult(results, count)


                emit(databaseResult)
            }
        }

    override fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteStepBatName = "tmp_bat"
            val cteStepTeamsName = "bcrto_tmp_teams"
            val cteStepDetailsName = "tmp_bat_details"
            val cteStepCountName = "total_counts"

            val tmp_bat =
                JooqBattingGroundsRecords.createTemporaryBattingCte(searchParameters)
            val tmp_teams = JooqBattingGroundsRecords.createTemporaryTeamsCte(searchParameters)
            val tmp_bat_details =
                JooqBattingGroundsRecords.createResultsCte(cteStepBatName, cteStepTeamsName, searchParameters)

            val count_cte = JooqBattingGroundsRecords.totalCountsCte(cteStepDetailsName)

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }


            val databaseResults = context
                .with(cteStepBatName).`as`(tmp_bat)
                .with(cteStepTeamsName).`as`(tmp_teams)
                .with(cteStepDetailsName).`as`(tmp_bat_details)
                .with(cteStepCountName).`as`(count_cte)
                .select().from(cteStepDetailsName)
                .orderBy(sortSpecification, field("SortNamePart"))
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            val countResult = context
                .with(cteStepBatName).`as`(tmp_bat)
                .with(cteStepTeamsName).`as`(tmp_teams)
                .with(cteStepDetailsName).`as`(tmp_bat_details)
                .with(cteStepCountName).`as`(count_cte)
                .select().from(cteStepCountName)
                .fetch()

            val count = countResult.get(0).getValue("count", Int::class.java)

            val results = mutableListOf<PrimaryBatting>()
            databaseResults.forEach {

                results.add(
                    PrimaryBatting(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("team", String::class.java),
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
                        opponents = "",
                        year = "",
                        ground = it.get("KnownAs", String::class.java),
                        countryName = it.get("CountryName", String::class.java),
                    )
                )
            }

            val databaseResult = DatabaseResult(results, count)

            emit(databaseResult)
        }
    }

    override fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteStepBatName = "tmp_bat"
            val cteStepTeamsName = "bcrto_tmp_teams"
            val cteStepDetailsName = "tmp_bat_details"
            val cteStepCountName = "total_counts"

            val tmp_bat =
                JooqBattingByHostRecords.createTemporaryBattingCte(searchParameters)
            val tmp_teams = JooqBattingByHostRecords.createTemporaryTeamsCte(searchParameters)
            val tmp_bat_details =
                JooqBattingByHostRecords.createResultsCte(cteStepBatName, cteStepTeamsName, searchParameters)

            val count_cte = JooqBattingByHostRecords.totalCountsCte(cteStepDetailsName)

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }


            val databaseResults = context
                .with(cteStepBatName).`as`(tmp_bat)
                .with(cteStepTeamsName).`as`(tmp_teams)
                .with(cteStepDetailsName).`as`(tmp_bat_details)
                .with(cteStepCountName).`as`(count_cte)
                .select().from(cteStepDetailsName)
                .orderBy(sortSpecification, field("Runs").desc(), field("SortNamePart"))
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            val countResult = context
                .with(cteStepBatName).`as`(tmp_bat)
                .with(cteStepTeamsName).`as`(tmp_teams)
                .with(cteStepDetailsName).`as`(tmp_bat_details)
                .with(cteStepCountName).`as`(count_cte)
                .select().from(cteStepCountName)
                .fetch()

            val count = countResult.get(0).getValue("count", Int::class.java)

            val results = mutableListOf<PrimaryBatting>()
            databaseResults.forEach {

                results.add(
                    PrimaryBatting(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("team", String::class.java),
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
                        opponents = it.getValue("opponents", String::class.java) ?: "",
                        year = "",
                        ground = "",
                        countryName = it.get("CountryName", String::class.java),
                    )
                )
            }

            val databaseResult = DatabaseResult(results, count)

            emit(databaseResult)
        }
    }

    override fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteStepBatName = "tmp_bat"
            val cteStepTeamsName = "bcrto_tmp_teams"
            val cteStepDetailsName = "tmp_bat_details"
            val cteStepCountName = "total_counts"

            val tmp_bat =
                JooqBattingByOppositionTeamRecords.createTemporaryBattingCte(searchParameters)
            val tmp_teams = JooqBattingByOppositionTeamRecords.createTemporaryTeamsCte(searchParameters)
            val tmp_bat_details =
                JooqBattingByOppositionTeamRecords.createResultsCte(cteStepBatName, cteStepTeamsName, searchParameters)

            val count_cte = JooqBattingByOppositionTeamRecords.totalCountsCte(cteStepDetailsName)

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }


            val databaseResults = context
                .with(cteStepBatName).`as`(tmp_bat)
                .with(cteStepTeamsName).`as`(tmp_teams)
                .with(cteStepDetailsName).`as`(tmp_bat_details)
                .with(cteStepCountName).`as`(count_cte)
                .select().from(cteStepDetailsName)
                .orderBy(sortSpecification, field("Runs").desc(), field("SortNamePart"))
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            val countResult = context
                .with(cteStepBatName).`as`(tmp_bat)
                .with(cteStepTeamsName).`as`(tmp_teams)
                .with(cteStepDetailsName).`as`(tmp_bat_details)
                .with(cteStepCountName).`as`(count_cte)
                .select().from(cteStepCountName)
                .fetch()

            val count = countResult.get(0).getValue("count", Int::class.java)

            val results = mutableListOf<PrimaryBatting>()
            databaseResults.forEach {

                results.add(
                    PrimaryBatting(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("team", String::class.java),
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
                        opponents = it.getValue("opponents", String::class.java) ?: "",
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

    override fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> =
        flow {
            val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


            if (databaseConnection == null)
                throw Exception("Database connection for match type ${searchParameters.matchType} not found")

            DriverManager.getConnection(
                databaseConnection.connectionString
            ).use { conn ->

                val context = DSL.using(conn, databaseConnection.dialect)

                val cteStepBatName = "tmp_bat"
                val cteStepTeamsName = "bcrto_tmp_teams"
                val cteStepDetailsName = "tmp_bat_details"
                val cteStepCountName = "total_counts"

                val tmp_bat =
                    JooqBattingByYearOfMatchStartRecords.createTemporaryBattingCte(searchParameters)
                val tmp_teams = JooqBattingByYearOfMatchStartRecords.createTemporaryTeamsCte(searchParameters)
                val tmp_bat_details =
                    JooqBattingByYearOfMatchStartRecords.createResultsCte(cteStepBatName, cteStepTeamsName, searchParameters)

                val count_cte = JooqBattingByYearOfMatchStartRecords.totalCountsCte(cteStepDetailsName)
                val sortSpecification = when (searchParameters.sortDirection) {
                    SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                    SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
                }


                val databaseResults = context
                    .with(cteStepBatName).`as`(tmp_bat)
                    .with(cteStepTeamsName).`as`(tmp_teams)
                    .with(cteStepDetailsName).`as`(tmp_bat_details)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStepDetailsName)
                    .orderBy(sortSpecification, field("Runs").desc(), field("SortNamePart"))
                    .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                    .fetch()

                val countResult = context
                    .with(cteStepBatName).`as`(tmp_bat)
                    .with(cteStepTeamsName).`as`(tmp_teams)
                    .with(cteStepDetailsName).`as`(tmp_bat_details)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStepCountName)
                    .fetch()

                val count = countResult.get(0).getValue("count", Int::class.java)

                val results = mutableListOf<PrimaryBatting>()
                databaseResults.forEach {

                    results.add(
                        PrimaryBatting(
                            playerId = it.getValue("playerid", Int::class.java),
                            name = it.getValue("name", String::class.java),
                            team = it.getValue("team", String::class.java),
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
                            opponents = "",
                            year = it.getValue("MatchStartYear", String::class.java) ?: "",
                            ground = "",
                            countryName = "",
                        )
                    )
                }

                val databaseResult = DatabaseResult(results, count)

                emit(databaseResult)

            }
        }

    override fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryBatting>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteStepBatName = "tmp_bat"
            val cteStepTeamsName = "bcrto_tmp_teams"
            val cteStepDetailsName = "tmp_bat_details"
            val cteStepCountName = "total_counts"

            val tmp_bat =
                JooqBattingBySeasonStartRecords.createTemporaryBattingCte(searchParameters)
            val tmp_teams = JooqBattingBySeasonStartRecords.createTemporaryTeamsCte(searchParameters)
            val tmp_bat_details =
                JooqBattingBySeasonStartRecords.createResultsCte(cteStepBatName, cteStepTeamsName, searchParameters)

            val count_cte = JooqBattingBySeasonStartRecords.totalCountsCte(cteStepDetailsName)
            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }


            val databaseResults = context
                .with(cteStepBatName).`as`(tmp_bat)
                .with(cteStepTeamsName).`as`(tmp_teams)
                .with(cteStepDetailsName).`as`(tmp_bat_details)
                .with(cteStepCountName).`as`(count_cte)
                .select().from(cteStepDetailsName)
                .orderBy(sortSpecification, field("Runs").desc(), field("SortNamePart"))
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            val countResult = context
                .with(cteStepBatName).`as`(tmp_bat)
                .with(cteStepTeamsName).`as`(tmp_teams)
                .with(cteStepDetailsName).`as`(tmp_bat_details)
                .with(cteStepCountName).`as`(count_cte)
                .select().from(cteStepCountName)
                .fetch()

            val count = countResult.get(0).getValue("count", Int::class.java)

            val results = mutableListOf<PrimaryBatting>()
            databaseResults.forEach {

                results.add(
                    PrimaryBatting(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("team", String::class.java),
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
                        opponents = "",
                        year = it.getValue("SeriesDate", String::class.java) ?: "",
                        ground = "",
                        countryName = "",
                    )
                )
            }

            val databaseResult = DatabaseResult(results, count)

            emit(databaseResult)
        }

    }
}
