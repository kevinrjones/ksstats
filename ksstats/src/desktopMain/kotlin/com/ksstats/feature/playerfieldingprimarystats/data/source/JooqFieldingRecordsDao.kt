package com.ksstats.feature.playerfieldingprimarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.feature.playerfieldingprimarystats.data.InningsByInningsFielding
import com.ksstats.feature.playerfieldingprimarystats.data.PrimaryFielding
import com.ksstats.shared.DatabaseConnections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import java.sql.DriverManager

class JooqFieldingRecordsDao(private val databaseConnections: DatabaseConnections) : FieldingRecordsDao {
    override fun getFieldingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteFieldingName = "tmp_fielding"
            val cteTeamsName = "tmp_teams"
            val cteResultsName = "bcrto_results"
            val cteFieldingCountsName = "tmp_fielding_counts"
            val cteFieldingBestName = "tmp_best"
            val ctePlayedName = "tmp_played"
            val cteInningsName = "tmp_innings"
            val cteTotalCountsName = "total_counts"

            val cteFielding =
                JooqFieldingCareerRecords.createTemporaryFieldingCte(searchParameters)

            val cteTeams = JooqFieldingCareerRecords.createTemporaryTeamsCte(searchParameters)

            val cteResults = JooqFieldingCareerRecords.createResultsCte(
                searchParameters = searchParameters,
                teamsCteName = cteTeamsName,
                fieldingCountsName = cteFieldingCountsName,
                fieldingBestName = cteFieldingBestName,
                fieldingPlayedName = ctePlayedName,
                fieldingInningsName = cteInningsName
            )

            val cteFieldingCounts =
                JooqFieldingCareerRecords.createTemporaryFieldingCounts(fieldingCteName = cteFieldingName)
            val cteBest = JooqFieldingCareerRecords.createTemporaryFieldingBest(fieldingCteName = cteFieldingName)
            val ctePlayed = JooqFieldingCareerRecords.createTemporaryPlayed(fieldingCteName = cteFieldingName)
            val cteInnings = JooqFieldingCareerRecords.createTemporaryInnings(
                matchType = searchParameters.matchType.value,
                fieldingCteName = cteFieldingName
            )

            val cteTotalCounts = JooqFieldingCareerRecords.totalCountsCte(
                resultsCteName = cteResultsName
            )

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteFieldingName).`as`(cteFielding)
                .with(cteFieldingCountsName).`as`(cteFieldingCounts)
                .with(cteTeamsName).`as`(cteTeams)
                .with(cteFieldingBestName).`as`(cteBest)
                .with(ctePlayedName).`as`(ctePlayed)
                .with(cteInningsName).`as`(cteInnings)
                .with(cteResultsName).`as`(cteResults)
                .with(cteTotalCountsName).`as`(cteTotalCounts)
                .select().from("$cteResultsName, $cteTotalCountsName")
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            var count = 0

            val primaryFieldingResults = mutableListOf<PrimaryFielding>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                primaryFieldingResults.add(
                    PrimaryFielding(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("teams", String::class.java),
                        sortNamePart = it.getValue("SortNamePart", String::class.java),
                        matches = it.getValue("matches", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        dismissals = it.getValue("dismissals", Int::class.java),
                        stumped = it.getValue("stumped", Int::class.java),
                        caughtWk = it.getValue("caughtwk", Int::class.java),
                        caughtF = it.getValue("caughtF", Int::class.java),
                        caught = it.getValue("caught", Int::class.java),
                        wicketKeeperDismissals = it.getValue("wicketkeeperdismissals", Int::class.java),
                        bestDismissals = it.getValue("bestDismissals", Int::class.java),
                        bestCaughtF = it.getValue("bestCaughtFielder", Int::class.java),
                        bestCaughtWk = it.getValue("bestCaughtKeeper", Int::class.java),
                        bestStumpings = it.getValue("bestStumpings", Int::class.java),
                        opponents = "",
                        year = "",
                        ground = "",
                        countryName = "",
                    )
                )
            }

            val databaseResult = DatabaseResult(primaryFieldingResults, count)

            emit(databaseResult)
        }
    }

    override fun getSeriesAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteFieldingName = "tmp_fielding"
            val cteTeamsName = "tmp_teams"
            val cteResultsName = "bcrto_results"
            val cteFieldingCountsName = "tmp_fielding_counts"
            val cteFieldingBestName = "tmp_best"
            val ctePlayedName = "tmp_played"
            val cteInningsName = "tmp_innings"
            val cteTotalCountsName = "total_counts"

            val cteFielding =
                JooqFieldingSeriesRecords.createTemporaryFieldingCte(searchParameters)

            val cteTeams = JooqFieldingSeriesRecords.createTemporaryTeamsCte(searchParameters)

            val cteResults = JooqFieldingSeriesRecords.createResultsCte(
                searchParameters = searchParameters,
                teamsCteName = cteTeamsName,
                fieldingCountsName = cteFieldingCountsName,
                fieldingBestName = cteFieldingBestName,
                fieldingPlayedName = ctePlayedName,
                fieldingInningsName = cteInningsName
            )

            val cteFieldingCounts =
                JooqFieldingSeriesRecords.createTemporaryFieldingCounts(fieldingCteName = cteFieldingName)
            val cteBest = JooqFieldingSeriesRecords.createTemporaryFieldingBest(fieldingCteName = cteFieldingName)
            val ctePlayed = JooqFieldingSeriesRecords.createTemporaryPlayed(fieldingCteName = cteFieldingName)
            val cteInnings = JooqFieldingSeriesRecords.createTemporaryInnings(
                matchType = searchParameters.matchType.value,
                fieldingCteName = cteFieldingName
            )

            val cteTotalCounts = JooqFieldingSeriesRecords.totalCountsCte(
                resultsCteName = cteResultsName
            )

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteFieldingName).`as`(cteFielding)
                .with(cteFieldingCountsName).`as`(cteFieldingCounts)
                .with(cteTeamsName).`as`(cteTeams)
                .with(cteFieldingBestName).`as`(cteBest)
                .with(ctePlayedName).`as`(ctePlayed)
                .with(cteInningsName).`as`(cteInnings)
                .with(cteResultsName).`as`(cteResults)
                .with(cteTotalCountsName).`as`(cteTotalCounts)
                .select().from("$cteResultsName, $cteTotalCountsName")
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            var count = 0

            val primaryFieldingResults = mutableListOf<PrimaryFielding>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                primaryFieldingResults.add(
                    PrimaryFielding(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("teams", String::class.java),
                        sortNamePart = it.getValue("SortNamePart", String::class.java),
                        matches = it.getValue("matches", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        dismissals = it.getValue("dismissals", Int::class.java),
                        stumped = it.getValue("stumped", Int::class.java),
                        caughtWk = it.getValue("caughtwk", Int::class.java),
                        caughtF = it.getValue("caughtF", Int::class.java),
                        caught = it.getValue("caught", Int::class.java),
                        wicketKeeperDismissals = it.getValue("wicketkeeperdismissals", Int::class.java),
                        bestDismissals = it.getValue("bestDismissals", Int::class.java),
                        bestCaughtF = it.getValue("bestCaughtFielder", Int::class.java),
                        bestCaughtWk = it.getValue("bestCaughtKeeper", Int::class.java),
                        bestStumpings = it.getValue("bestStumpings", Int::class.java),
                        opponents = it.getValue("opponents", String::class.java),
                        year = it.getValue("year", String::class.java),
                        ground = "",
                        countryName = "",
                    )
                )
            }

            val databaseResult = DatabaseResult(primaryFieldingResults, count)

            emit(databaseResult)
        }
    }

    override fun getFieldingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsFielding>> =
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
                    JooqFieldingInningsByInningsRecords.createTemporaryFieldingCte(searchParameters)

                val count_cte = JooqFieldingInningsByInningsRecords.totalCountsCte(cteStep1Name)

                val sortSpecification = when (searchParameters.sortDirection) {
                    SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                    SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
                }


                val databaseResults = context
                    .with(cteStep1Name).`as`(tmp_bat)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStep1Name)
                    .orderBy(sortSpecification, field("dismissals").desc(), field("matchDate"))
                    .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                    .fetch()

                val countResult = context
                    .with(cteStep1Name).`as`(tmp_bat)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStepCountName)
                    .fetch()

                val count = countResult.get(0).getValue("count", Int::class.java)

                val results = mutableListOf<InningsByInningsFielding>()
                databaseResults.forEach {
                    results.add(
                        InningsByInningsFielding(
                            playerId = it.getValue("playerid", Int::class.java),
                            name = it.getValue("FullName", String::class.java),
                            team = it.getValue("team", String::class.java),
                            innings = it.getValue("innings", Int::class.java),
                            matchDate = it.getValue("matchStartDateAsOffset", Long::class.java),
                            ground = it.getValue("ground", String::class.java),
                            opponents = it.getValue("opponents", String::class.java),
                            dismissals = it.getValue("dismissals", Int::class.java),
                            wicketKeepingDismissals = it.getValue("wicketKeepingDismissals", Int::class.java),
                            caughtKeeper = it.getValue("caughtkeeper", Int::class.java),
                            caughtFielder = it.getValue("caughtfielder", Int::class.java),
                            caught = it.getValue("caught", Int::class.java),
                            stumpings = it.getValue("stumpings", Int::class.java)
                        )
                    )
                }

                val databaseResult = DatabaseResult(results, count)


                emit(databaseResult)
            }
        }

    override fun getMatchTotals(searchParameters: SearchParameters): Flow<DatabaseResult<InningsByInningsFielding>> =
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
                    JooqFieldingMatchTotalsRecords.createTemporaryFieldingCte(searchParameters)

                val count_cte = JooqFieldingMatchTotalsRecords.totalCountsCte(cteStep1Name)

                val sortSpecification = when (searchParameters.sortDirection) {
                    SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                    SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
                }


                val databaseResults = context
                    .with(cteStep1Name).`as`(tmp_bat)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStep1Name)
                    .orderBy(sortSpecification, field("dismissals").desc(), field("matchDate"))
                    .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                    .fetch()

                val countResult = context
                    .with(cteStep1Name).`as`(tmp_bat)
                    .with(cteStepCountName).`as`(count_cte)
                    .select().from(cteStepCountName)
                    .fetch()

                val count = countResult.get(0).getValue("count", Int::class.java)

                val results = mutableListOf<InningsByInningsFielding>()
                databaseResults.forEach {
                    results.add(
                        InningsByInningsFielding(
                            playerId = it.getValue("playerid", Int::class.java),
                            name = it.getValue("FullName", String::class.java),
                            team = it.getValue("team", String::class.java),
                            innings = it.getValue("innings", Int::class.java),
                            matchDate = it.getValue("matchStartDateAsOffset", Long::class.java),
                            ground = it.getValue("ground", String::class.java),
                            opponents = it.getValue("opponents", String::class.java),
                            dismissals = it.getValue("dismissals", Int::class.java),
                            wicketKeepingDismissals = it.getValue("wicketKeepingDismissals", Int::class.java),
                            caughtKeeper = it.getValue("caughtkeeper", Int::class.java),
                            caughtFielder = it.getValue("caughtfielder", Int::class.java),
                            caught = it.getValue("caught", Int::class.java),
                            stumpings = it.getValue("stumpings", Int::class.java)
                        )
                    )
                }

                val databaseResult = DatabaseResult(results, count)


                emit(databaseResult)
            }
        }

    override fun getGroundAverages(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteFieldingName = "tmp_fielding"
            val cteTeamsName = "tmp_teams"
            val cteResultsName = "bcrto_results"
            val cteFieldingCountsName = "tmp_fielding_counts"
            val cteFieldingBestName = "tmp_best"
            val ctePlayedName = "tmp_played"
            val cteInningsName = "tmp_innings"
            val cteTotalCountsName = "total_counts"

            val cteFielding =
                JooqFieldingGroundsRecords.createTemporaryFieldingCte(searchParameters)

            val cteTeams = JooqFieldingGroundsRecords.createTemporaryTeamsCte(searchParameters)

            val cteResults = JooqFieldingGroundsRecords.createResultsCte(
                searchParameters = searchParameters,
                teamsCteName = cteTeamsName,
                fieldingCountsName = cteFieldingCountsName,
                fieldingBestName = cteFieldingBestName,
                fieldingPlayedName = ctePlayedName,
                fieldingInningsName = cteInningsName
            )

            val cteFieldingCounts =
                JooqFieldingGroundsRecords.createTemporaryFieldingCounts(fieldingCteName = cteFieldingName)
            val cteBest =
                JooqFieldingGroundsRecords.createTemporaryFieldingBest(fieldingCteName = cteFieldingName)
            val ctePlayed =
                JooqFieldingGroundsRecords.createTemporaryPlayed(fieldingCteName = cteFieldingName)
            val cteInnings = JooqFieldingGroundsRecords.createTemporaryInnings(
                matchType = searchParameters.matchType.value,
                fieldingCteName = cteFieldingName
            )

            val cteTotalCounts = JooqFieldingGroundsRecords.totalCountsCte(
                resultsCteName = cteResultsName
            )

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteFieldingName).`as`(cteFielding)
                .with(cteFieldingCountsName).`as`(cteFieldingCounts)
                .with(cteTeamsName).`as`(cteTeams)
                .with(cteFieldingBestName).`as`(cteBest)
                .with(ctePlayedName).`as`(ctePlayed)
                .with(cteInningsName).`as`(cteInnings)
                .with(cteResultsName).`as`(cteResults)
                .with(cteTotalCountsName).`as`(cteTotalCounts)
                .select().from("$cteResultsName, $cteTotalCountsName")
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            var count = 0

            val primaryFieldingResults = mutableListOf<PrimaryFielding>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                primaryFieldingResults.add(
                    PrimaryFielding(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("teams", String::class.java),
                        sortNamePart = it.getValue("SortNamePart", String::class.java),
                        matches = it.getValue("matches", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        dismissals = it.getValue("dismissals", Int::class.java),
                        stumped = it.getValue("stumped", Int::class.java),
                        caughtWk = it.getValue("caughtwk", Int::class.java),
                        caughtF = it.getValue("caughtF", Int::class.java),
                        caught = it.getValue("caught", Int::class.java),
                        wicketKeeperDismissals = it.getValue("wicketkeeperdismissals", Int::class.java),
                        bestDismissals = it.getValue("bestDismissals", Int::class.java),
                        bestCaughtF = it.getValue("bestCaughtFielder", Int::class.java),
                        bestCaughtWk = it.getValue("bestCaughtKeeper", Int::class.java),
                        bestStumpings = it.getValue("bestStumpings", Int::class.java),
                        opponents = "",
                        year = "",
                        ground = it.getValue("Ground", String::class.java),
                        countryName = "",
                    )
                )
            }

            val databaseResult = DatabaseResult(primaryFieldingResults, count)

            emit(databaseResult)
        }
    }

    override fun getByHostCountry(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteFieldingName = "tmp_fielding"
            val cteTeamsName = "tmp_teams"
            val cteResultsName = "bcrto_results"
            val cteFieldingCountsName = "tmp_fielding_counts"
            val cteFieldingBestName = "tmp_best"
            val ctePlayedName = "tmp_played"
            val cteInningsName = "tmp_innings"
            val cteTotalCountsName = "total_counts"

            val cteFielding =
                JooqFieldingByHostRecords.createTemporaryFieldingCte(searchParameters)

            val cteTeams = JooqFieldingByHostRecords.createTemporaryTeamsCte(searchParameters)

            val cteResults = JooqFieldingByHostRecords.createResultsCte(
                searchParameters = searchParameters,
                teamsCteName = cteTeamsName,
                fieldingCountsName = cteFieldingCountsName,
                fieldingBestName = cteFieldingBestName,
                fieldingPlayedName = ctePlayedName,
                fieldingInningsName = cteInningsName
            )

            val cteFieldingCounts =
                JooqFieldingByHostRecords.createTemporaryFieldingCounts(fieldingCteName = cteFieldingName)
            val cteBest =
                JooqFieldingByHostRecords.createTemporaryFieldingBest(fieldingCteName = cteFieldingName)
            val ctePlayed =
                JooqFieldingByHostRecords.createTemporaryPlayed(fieldingCteName = cteFieldingName)
            val cteInnings = JooqFieldingByHostRecords.createTemporaryInnings(
                matchType = searchParameters.matchType.value,
                fieldingCteName = cteFieldingName
            )

            val cteTotalCounts = JooqFieldingByHostRecords.totalCountsCte(
                resultsCteName = cteResultsName
            )

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteFieldingName).`as`(cteFielding)
                .with(cteFieldingCountsName).`as`(cteFieldingCounts)
                .with(cteTeamsName).`as`(cteTeams)
                .with(cteFieldingBestName).`as`(cteBest)
                .with(ctePlayedName).`as`(ctePlayed)
                .with(cteInningsName).`as`(cteInnings)
                .with(cteResultsName).`as`(cteResults)
                .with(cteTotalCountsName).`as`(cteTotalCounts)
                .select().from("$cteResultsName, $cteTotalCountsName")
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            var count = 0

            val primaryFieldingResults = mutableListOf<PrimaryFielding>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                primaryFieldingResults.add(
                    PrimaryFielding(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("teams", String::class.java),
                        sortNamePart = it.getValue("SortNamePart", String::class.java),
                        matches = it.getValue("matches", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        dismissals = it.getValue("dismissals", Int::class.java),
                        stumped = it.getValue("stumped", Int::class.java),
                        caughtWk = it.getValue("caughtwk", Int::class.java),
                        caughtF = it.getValue("caughtF", Int::class.java),
                        caught = it.getValue("caught", Int::class.java),
                        wicketKeeperDismissals = it.getValue("wicketkeeperdismissals", Int::class.java),
                        bestDismissals = it.getValue("bestDismissals", Int::class.java),
                        bestCaughtF = it.getValue("bestCaughtFielder", Int::class.java),
                        bestCaughtWk = it.getValue("bestCaughtKeeper", Int::class.java),
                        bestStumpings = it.getValue("bestStumpings", Int::class.java),
                        opponents = "",
                        year = "",
                        ground = "",
                        countryName = it.getValue("CountryName", String::class.java),
                    )
                )
            }

            val databaseResult = DatabaseResult(primaryFieldingResults, count)

            emit(databaseResult)
        }
    }

    override fun getByOppositionTeam(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>>  = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteFieldingName = "tmp_fielding"
            val cteTeamsName = "tmp_teams"
            val cteResultsName = "bcrto_results"
            val cteFieldingCountsName = "tmp_fielding_counts"
            val cteFieldingBestName = "tmp_best"
            val ctePlayedName = "tmp_played"
            val cteInningsName = "tmp_innings"
            val cteTotalCountsName = "total_counts"

            val cteFielding =
                JooqFieldingByOppositionTeamRecords.createTemporaryFieldingCte(searchParameters)

            val cteTeams = JooqFieldingByOppositionTeamRecords.createTemporaryTeamsCte(searchParameters)

            val cteResults = JooqFieldingByOppositionTeamRecords.createResultsCte(
                searchParameters = searchParameters,
                teamsCteName = cteTeamsName,
                fieldingCountsName = cteFieldingCountsName,
                fieldingBestName = cteFieldingBestName,
                fieldingPlayedName = ctePlayedName,
                fieldingInningsName = cteInningsName
            )

            val cteFieldingCounts =
                JooqFieldingByOppositionTeamRecords.createTemporaryFieldingCounts(fieldingCteName = cteFieldingName)
            val cteBest =
                JooqFieldingByOppositionTeamRecords.createTemporaryFieldingBest(fieldingCteName = cteFieldingName)
            val ctePlayed =
                JooqFieldingByOppositionTeamRecords.createTemporaryPlayed(fieldingCteName = cteFieldingName)
            val cteInnings = JooqFieldingByOppositionTeamRecords.createTemporaryInnings(
                matchType = searchParameters.matchType.value,
                fieldingCteName = cteFieldingName
            )

            val cteTotalCounts = JooqFieldingByOppositionTeamRecords.totalCountsCte(
                resultsCteName = cteResultsName
            )

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteFieldingName).`as`(cteFielding)
                .with(cteFieldingCountsName).`as`(cteFieldingCounts)
                .with(cteTeamsName).`as`(cteTeams)
                .with(cteFieldingBestName).`as`(cteBest)
                .with(ctePlayedName).`as`(ctePlayed)
                .with(cteInningsName).`as`(cteInnings)
                .with(cteResultsName).`as`(cteResults)
                .with(cteTotalCountsName).`as`(cteTotalCounts)
                .select().from("$cteResultsName, $cteTotalCountsName")
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            var count = 0

            val primaryFieldingResults = mutableListOf<PrimaryFielding>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                primaryFieldingResults.add(
                    PrimaryFielding(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("teams", String::class.java),
                        sortNamePart = it.getValue("SortNamePart", String::class.java),
                        matches = it.getValue("matches", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        dismissals = it.getValue("dismissals", Int::class.java),
                        stumped = it.getValue("stumped", Int::class.java),
                        caughtWk = it.getValue("caughtwk", Int::class.java),
                        caughtF = it.getValue("caughtF", Int::class.java),
                        caught = it.getValue("caught", Int::class.java),
                        wicketKeeperDismissals = it.getValue("wicketkeeperdismissals", Int::class.java),
                        bestDismissals = it.getValue("bestDismissals", Int::class.java),
                        bestCaughtF = it.getValue("bestCaughtFielder", Int::class.java),
                        bestCaughtWk = it.getValue("bestCaughtKeeper", Int::class.java),
                        bestStumpings = it.getValue("bestStumpings", Int::class.java),
                        opponents = it.getValue("opponents", String::class.java),
                        year = "",
                        ground = "",
                        countryName = "",
                    )
                )
            }

            val databaseResult = DatabaseResult(primaryFieldingResults, count)

            emit(databaseResult)
        }
    }

    override fun getByYearOfMatchStart(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> =
        flow {
            val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


            if (databaseConnection == null)
                throw Exception("Database connection for match type ${searchParameters.matchType} not found")

            DriverManager.getConnection(
                databaseConnection.connectionString
            ).use { conn ->

                val context = DSL.using(conn, databaseConnection.dialect)

                val cteFieldingName = "tmp_fielding"
                val cteTeamsName = "tmp_teams"
                val cteResultsName = "bcrto_results"
                val cteFieldingCountsName = "tmp_fielding_counts"
                val cteFieldingBestName = "tmp_best"
                val ctePlayedName = "tmp_played"
                val cteInningsName = "tmp_innings"
                val cteTotalCountsName = "total_counts"

                val cteFielding =
                    JooqFieldingByYearOfMatchStartRecords.createTemporaryFieldingCte(searchParameters)

                val cteTeams = JooqFieldingByYearOfMatchStartRecords.createTemporaryTeamsCte(searchParameters)

                val cteResults = JooqFieldingByYearOfMatchStartRecords.createResultsCte(
                    searchParameters = searchParameters,
                    teamsCteName = cteTeamsName,
                    fieldingCountsName = cteFieldingCountsName,
                    fieldingBestName = cteFieldingBestName,
                    fieldingPlayedName = ctePlayedName,
                    fieldingInningsName = cteInningsName
                )

                val cteFieldingCounts =
                    JooqFieldingByYearOfMatchStartRecords.createTemporaryFieldingCounts(fieldingCteName = cteFieldingName)
                val cteBest =
                    JooqFieldingByYearOfMatchStartRecords.createTemporaryFieldingBest(fieldingCteName = cteFieldingName)
                val ctePlayed =
                    JooqFieldingByYearOfMatchStartRecords.createTemporaryPlayed(fieldingCteName = cteFieldingName)
                val cteInnings = JooqFieldingByYearOfMatchStartRecords.createTemporaryInnings(
                    matchType = searchParameters.matchType.value,
                    fieldingCteName = cteFieldingName
                )

                val cteTotalCounts = JooqFieldingByYearOfMatchStartRecords.totalCountsCte(
                    resultsCteName = cteResultsName
                )

                val sortSpecification = when (searchParameters.sortDirection) {
                    SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                    SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
                }

                val databaseResults = context
                    .with(cteFieldingName).`as`(cteFielding)
                    .with(cteFieldingCountsName).`as`(cteFieldingCounts)
                    .with(cteTeamsName).`as`(cteTeams)
                    .with(cteFieldingBestName).`as`(cteBest)
                    .with(ctePlayedName).`as`(ctePlayed)
                    .with(cteInningsName).`as`(cteInnings)
                    .with(cteResultsName).`as`(cteResults)
                    .with(cteTotalCountsName).`as`(cteTotalCounts)
                    .select().from("$cteResultsName, $cteTotalCountsName")
                    .orderBy(sortSpecification)
                    .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                    .fetch()

                var count = 0

                val primaryFieldingResults = mutableListOf<PrimaryFielding>()
                databaseResults.forEach {
                    count = it.getValue("count", Int::class.java)
                    primaryFieldingResults.add(
                        PrimaryFielding(
                            playerId = it.getValue("playerid", Int::class.java),
                            name = it.getValue("name", String::class.java),
                            team = it.getValue("teams", String::class.java),
                            sortNamePart = it.getValue("SortNamePart", String::class.java),
                            matches = it.getValue("matches", Int::class.java),
                            innings = it.getValue("innings", Int::class.java),
                            dismissals = it.getValue("dismissals", Int::class.java),
                            stumped = it.getValue("stumped", Int::class.java),
                            caughtWk = it.getValue("caughtwk", Int::class.java),
                            caughtF = it.getValue("caughtF", Int::class.java),
                            caught = it.getValue("caught", Int::class.java),
                            wicketKeeperDismissals = it.getValue("wicketkeeperdismissals", Int::class.java),
                            bestDismissals = it.getValue("bestDismissals", Int::class.java),
                            bestCaughtF = it.getValue("bestCaughtFielder", Int::class.java),
                            bestCaughtWk = it.getValue("bestCaughtKeeper", Int::class.java),
                            bestStumpings = it.getValue("bestStumpings", Int::class.java),
                            opponents = "",
                            year = it.getValue("year", String::class.java),
                            ground = "",
                            countryName = "",
                        )
                    )
                }

                val databaseResult = DatabaseResult(primaryFieldingResults, count)

                emit(databaseResult)
            }
        }

    override fun getBySeason(searchParameters: SearchParameters): Flow<DatabaseResult<PrimaryFielding>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)

            val cteFieldingName = "tmp_fielding"
            val cteTeamsName = "tmp_teams"
            val cteResultsName = "bcrto_results"
            val cteFieldingCountsName = "tmp_fielding_counts"
            val cteFieldingBestName = "tmp_best"
            val ctePlayedName = "tmp_played"
            val cteInningsName = "tmp_innings"
            val cteTotalCountsName = "total_counts"

            val cteFielding =
                JooqFieldingBySeasonStartRecords.createTemporaryFieldingCte(searchParameters)

            val cteTeams = JooqFieldingBySeasonStartRecords.createTemporaryTeamsCte(searchParameters)

            val cteResults = JooqFieldingBySeasonStartRecords.createResultsCte(
                searchParameters = searchParameters,
                teamsCteName = cteTeamsName,
                fieldingCountsName = cteFieldingCountsName,
                fieldingBestName = cteFieldingBestName,
                fieldingPlayedName = ctePlayedName,
                fieldingInningsName = cteInningsName
            )

            val cteFieldingCounts =
                JooqFieldingBySeasonStartRecords.createTemporaryFieldingCounts(fieldingCteName = cteFieldingName)
            val cteBest =
                JooqFieldingBySeasonStartRecords.createTemporaryFieldingBest(fieldingCteName = cteFieldingName)
            val ctePlayed = JooqFieldingBySeasonStartRecords.createTemporaryPlayed(fieldingCteName = cteFieldingName)
            val cteInnings = JooqFieldingBySeasonStartRecords.createTemporaryInnings(
                matchType = searchParameters.matchType.value,
                fieldingCteName = cteFieldingName
            )

            val cteTotalCounts = JooqFieldingBySeasonStartRecords.totalCountsCte(
                resultsCteName = cteResultsName
            )

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }

            val databaseResults = context
                .with(cteFieldingName).`as`(cteFielding)
                .with(cteFieldingCountsName).`as`(cteFieldingCounts)
                .with(cteTeamsName).`as`(cteTeams)
                .with(cteFieldingBestName).`as`(cteBest)
                .with(ctePlayedName).`as`(ctePlayed)
                .with(cteInningsName).`as`(cteInnings)
                .with(cteResultsName).`as`(cteResults)
                .with(cteTotalCountsName).`as`(cteTotalCounts)
                .select().from("$cteResultsName, $cteTotalCountsName")
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            var count = 0

            val primaryFieldingResults = mutableListOf<PrimaryFielding>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                primaryFieldingResults.add(
                    PrimaryFielding(
                        playerId = it.getValue("playerid", Int::class.java),
                        name = it.getValue("name", String::class.java),
                        team = it.getValue("teams", String::class.java),
                        sortNamePart = it.getValue("SortNamePart", String::class.java),
                        matches = it.getValue("matches", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        dismissals = it.getValue("dismissals", Int::class.java),
                        stumped = it.getValue("stumped", Int::class.java),
                        caughtWk = it.getValue("caughtwk", Int::class.java),
                        caughtF = it.getValue("caughtF", Int::class.java),
                        caught = it.getValue("caught", Int::class.java),
                        wicketKeeperDismissals = it.getValue("wicketkeeperdismissals", Int::class.java),
                        bestDismissals = it.getValue("bestDismissals", Int::class.java),
                        bestCaughtF = it.getValue("bestCaughtFielder", Int::class.java),
                        bestCaughtWk = it.getValue("bestCaughtKeeper", Int::class.java),
                        bestStumpings = it.getValue("bestStumpings", Int::class.java),
                        opponents = "",
                        year = it.getValue("year", String::class.java),
                        ground = "",
                        countryName = "",
                    )
                )
            }

            val databaseResult = DatabaseResult(primaryFieldingResults, count)

            emit(databaseResult)
        }
    }
}
