package com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source

import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.feature.battingrecordsdisplay.data.BattingSearchResults
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.JooqBattingCareerRecords.createResultsCte
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.JooqBattingCareerRecords.createTemporaryTeamsCte
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.data.source.JooqBattingCareerRecords.totalCountsCte
import com.ksstats.shared.DatabaseConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import java.sql.DriverManager
class JooqBattingRecordsDao(private val databaseConnection: DatabaseConnection) : BattingRecordsDao {
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
                .limit(searchParameters.startRow, searchParameters.pageSize).fetch()

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
                        count = it.getValue("count", Int::class.java),
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