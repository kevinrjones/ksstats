package com.ksstats.feature.playerbowlingprimarystats.data.source

import com.ksstats.core.data.DatabaseResult
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.feature.playerbowlingprimarystats.data.BowlingSummary
import com.ksstats.feature.playerbowlingprimarystats.data.source.JooqBowlingCareerRecords.createBbm
import com.ksstats.feature.playerbowlingprimarystats.data.source.JooqBowlingCareerRecords.createBbmAll
import com.ksstats.feature.playerbowlingprimarystats.data.source.JooqBowlingCareerRecords.createFives
import com.ksstats.feature.playerbowlingprimarystats.data.source.JooqBowlingCareerRecords.createResultsCte
import com.ksstats.feature.playerbowlingprimarystats.data.source.JooqBowlingCareerRecords.createTemporaryTeamsCte
import com.ksstats.feature.playerbowlingprimarystats.data.source.JooqBowlingCareerRecords.createTens
import com.ksstats.feature.playerbowlingprimarystats.data.source.JooqBowlingCareerRecords.totalCountsCte
import com.ksstats.shared.DatabaseConnections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import java.sql.DriverManager

class JooqBowlingRecordsDao(private val databaseConnections: DatabaseConnections) : BowlingRecordsDao {
    override fun getBowlingSummary(searchParameters: SearchParameters): Flow<DatabaseResult<BowlingSummary>> = flow {
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
            val bocrto_tmp_teams = createTemporaryTeamsCte(searchParameters)
            val bocrto_results = createResultsCte(
                searchParameters,
                cteStep1Name,
                cteStep2Name,
                cteStep5Name,
                cteStep6Name,
                cteStep10Name
            )
            val bocrto_count = totalCountsCte(cteStep3Name)

            val bocrto_fives = createFives(cteStep1Name, searchParameters.fivesLimit)
            val bocrto_tens = createTens(cteStep1Name)
            val bocrto_bbm_all = createBbmAll(cteStep1Name)
            val bocrto_bbm = createBbm(cteStep9Name)

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

            val results = mutableListOf<BowlingSummary>()
            databaseResults.forEach {
                count = it.getValue("count", Int::class.java)
                results.add(
                    BowlingSummary(
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

    override fun getBowlingInningsByInnings(searchParameters: SearchParameters): Flow<DatabaseResult<BowlingSummary>> {
        TODO("Not yet implemented")
    }
}
