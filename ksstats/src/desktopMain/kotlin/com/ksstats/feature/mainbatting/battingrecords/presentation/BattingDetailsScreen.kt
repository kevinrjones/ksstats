package com.ksstats.feature.mainbatting.battingrecords.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ksstats.core.domain.util.round
import com.ksstats.core.domain.util.truncate
import com.ksstats.core.presentation.components.ColumnMetaData
import com.ksstats.core.presentation.components.TableEx
import org.koin.compose.koinInject


data class BattingDetailsScreenRecord(
    val name: String,
    val team: String,
    val matches: Int,
    val innings: Int,
    val notOuts: Int,
    val runs: Int,
    val highestScore: Double,
    val hundreds: Int,
    val fifties: Int,
    val ducks: Int,
    val fours: Int,
    val sixes: Int,
    val balls: Int,
    val average: Double,
    val strikeRate: Double,
    val battingImpact: Double,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BattingDetailsScreen(
    matchType: String,
    matchSubType: String,
    teamId: Int?,
    opponentsId: Int?,
    groundId: Int?,
    hostCountryId: Int?,
    venue: Int?,
    sortOrder: Int?,
    sortDirection: String?,
    startDate: Long?,
    endDate: Long?,
    result: Int?,
    limit: Int?,
    startRow: Int?,
    pageSize: Int?
) {
    val viewModel: BattingDetailsScreenViewModel = koinInject()
    val searchResults = viewModel.battingSearchResults.collectAsState()

    val displayRecords = searchResults.value.map {
        listOf(
            it.name,
            it.team,
            it.matches.toString(),
            it.innings.toString(),
            it.notOuts.toString(),
            it.runs.toString(),
            it.highestScore.toString(),
            it.hundreds.toString(),
            it.fifties.toString(),
            it.ducks.toString(),
            it.fours.toString(),
            it.sixes.toString(),
            it.balls.toString(),
            it.average.truncate(2),
            it.strikeRate.round(2),
            it.battingImpact.round(3)
        )
    }

    Box(
        // initial height needed as the grid is empty, setting it to 0 is fine (it appears!)
        modifier = Modifier
            .height(0.dp)
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        TableEx(
            columnCount = 16,
            rowCount = 50,
            cellContent = { column, row ->
                if (displayRecords.isNotEmpty())
                    displayRecords[row][column]
                else
                    null
            },
            metaData = listOf(
                ColumnMetaData("Name", 100.dp),
                ColumnMetaData("Teams", 200.dp),
                ColumnMetaData("Matches", 100.dp),
                ColumnMetaData("Innings", 100.dp),
                ColumnMetaData("Not Outs", 100.dp),
                ColumnMetaData("Runs", 100.dp),
                ColumnMetaData("Highest Score", 100.dp),
                ColumnMetaData("Hundreds", 100.dp),
                ColumnMetaData("Fifties", 100.dp),
                ColumnMetaData("Ducks", 100.dp),
                ColumnMetaData("Fours", 100.dp),
                ColumnMetaData("Sixes", 100.dp),
                ColumnMetaData("Balls", 100.dp),
                ColumnMetaData("Avg", 100.dp),
                ColumnMetaData("SR", 100.dp),
                ColumnMetaData("BI", 100.dp),
            )
        )

    }
}