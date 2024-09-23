package com.ksstats.feature.teamrecordspirmarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.core.presentation.getOvers
import com.ksstats.shared.DatabaseConnections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import java.sql.DriverManager

class JooqTeamRecordsDao(private val databaseConnections: DatabaseConnections) : TeamRecordsDao {
    override fun getTeamSummary(searchParameters: SearchParameters): Flow<DatabaseResult<TeamSummary>> = flow {
        val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


        if (databaseConnection == null)
            throw Exception("Database connection for match type ${searchParameters.matchType} not found")

        DriverManager.getConnection(
            databaseConnection.connectionString
        ).use { conn ->

            val context = DSL.using(conn, databaseConnection.dialect)
            val cteScoresName = "cte_scores"
            val cteTotalsName = "cte_totals"
            val cteSummaryName = "cte_summary"
            val cteCountsName = "cte_counts"

            val cteScores = JooqMatchSummaryRecords.createScoresCte(searchParameters)
            val cteTotals = JooqMatchSummaryRecords.createTotalsCte(cteScoresName)
            val cteSummary = JooqMatchSummaryRecords.createSummaryCte(searchParameters, cteScoresName, cteTotalsName)

            val cteCounts =
                JooqMatchSummaryRecords.createCounts(cteScoresName)

            val sortSpecification = when (searchParameters.sortDirection) {
                SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
            }


            val databaseResults = context
                .with(cteScoresName).`as`(cteScores)
                .with(cteTotalsName).`as`(cteTotals)
                .with(cteCountsName).`as`(cteCounts)
                .with(cteSummaryName).`as`(cteSummary)
                .select().from("$cteSummaryName, $cteCountsName")
                .orderBy(sortSpecification)
                .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                .fetch()

            var count = 0

            val teamResults = mutableListOf<TeamSummary>()

            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                teamResults.add(
                    TeamSummary(
                        teamName = it.getValue("Team", String::class.java),
                        matches = it.getValue("played", Int::class.java),
                        innings = it.getValue("innings", Int::class.java),
                        won = it.getValue("won", Int::class.java),
                        lost = it.getValue("lost", Int::class.java),
                        tied = it.getValue("tied", Int::class.java),
                        drawn = it.getValue("drawn", Int::class.java),
                        runs = it.getValue("runs", Int::class.java),
                        wickets = it.getValue("wickets", Int::class.java),
                        average = it.getValue("average", Double::class.java),
                        runsPerOver = it.getValue("runsPerOver", Double::class.java),
                        strikeRate = it.getValue("strikeRate", Double::class.java),
                    )
                )
            }
            val databaseResult = DatabaseResult(teamResults, count)

            emit(databaseResult)
        }
    }

    override fun getInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<TeamInningsByInnings>> =
        flow {

            val databaseConnection = databaseConnections.connections[searchParameters.matchType.value]


            if (databaseConnection == null)
                throw Exception("Database connection for match type ${searchParameters.matchType} not found")

            DriverManager.getConnection(
                databaseConnection.connectionString
            ).use { conn ->

                val context = DSL.using(conn, databaseConnection.dialect)
                val cteScoresName = "cte_scores"
                val cteCountsName = "cte_counts"

                val cteScores = JooqInningsByInningsRecords.createScoresCte(searchParameters)


                val cteCounts =
                    JooqInningsByInningsRecords.createCounts(cteScoresName)

                val sortSpecification = when (searchParameters.sortDirection) {
                    SortDirection.Ascending -> field(searchParameters.sortOrder.name).asc()
                    SortDirection.Descending -> field(searchParameters.sortOrder.name).desc()
                }

                val databaseResults = context
                    .with(cteScoresName).`as`(cteScores)
                    .with(cteCountsName).`as`(cteCounts)
                    .select().from("$cteScoresName, $cteCountsName")
                    .orderBy(sortSpecification)
                    .limit(searchParameters.pagingParameters.startRow, searchParameters.pagingParameters.pageSize)
                    .fetch()

                var count = 0


                val teamResults = mutableListOf<TeamInningsByInnings>()

                databaseResults.forEach {
                    count = it.getValue("count", Int::class.java)
                    teamResults.add(
                        TeamInningsByInnings(
                            team = it.getValue("team", String::class.java),
                            opponents = it.getValue("opponents", String::class.java),
                            innings = it.getValue("InningsOrder", Int::class.java),
                            score = it.getValue("Total", Int::class.java),
                            overs = getOvers(it.getValue("BallsBowled", Int::class.java),
                                it.getValue("BallsPerOver", Int::class.java)),
                            result = it.getValue("ResultString", String::class.java),
                            ground = it.getValue("Location", String::class.java),
                            startDate = it.getValue("MatchDate", String::class.java),
                            runsPerOver = it.getValue("rpo", Double::class.java),
                        )
                    )
                }

            val databaseResult = DatabaseResult(teamResults, count)

            emit(databaseResult)
        }
}
}