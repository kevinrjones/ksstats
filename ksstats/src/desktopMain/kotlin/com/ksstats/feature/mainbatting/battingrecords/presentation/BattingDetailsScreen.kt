package com.ksstats.feature.mainbatting.battingrecords.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ksstats.core.domain.util.*
import com.ksstats.core.presentation.StatsAppScreen
import com.ksstats.core.presentation.components.ColumnMetaData
import com.ksstats.core.presentation.components.TableEx
import com.ksstats.shared.toSeconds
import org.koin.compose.koinInject

fun NavGraphBuilder.battingDetailsScreen() {
    composable(
        route = StatsAppScreen.BattingDetails.name +
                "?matchType={matchType}" +
                "&matchSubType={matchSubType}" +
                "&teamId={teamId}" +
                "&opponentsId={opponentsId}" +
                "&groundId={groundId}" +
                "&hostCountryId={hostCountryId}" +
                "&venue={venue}" +
                "&sortOrder={sortOrder}" +
                "&sortDirection={sortDirection}" +
                "&startDate={startDate}" +
                "&endDate={endDate}" +
                "&result={result}" +
                "&limit={limit}" +
                "&startRow={startRow}" +
                "&pageSize={pageSize}",
        arguments = listOf(
            navArgument(name = "matchType") {
                type = NavType.StringType
                defaultValue = "t"
            },
            navArgument(name = "matchSubType") {
                type = NavType.StringType
                defaultValue = "t"
            },
            navArgument(name = "teamId") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "opponentsId") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "groundId") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "hostCountryId") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "venue") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "sortOrder") {
                type = NavType.IntType
                defaultValue = 4
            },
            navArgument(name = "sortDirection") {
                type = NavType.StringType
                defaultValue = "DESC"
            },
            navArgument(name = "startDate") {
                type = NavType.LongType
                defaultValue = -8520336000
            },
            navArgument(name = "endDate") {
                type = NavType.LongType
                defaultValue = 253402214400
            },
            navArgument(name = "result") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "limit") {
                type = NavType.IntType
                defaultValue = 100
            },
            navArgument(name = "startRow") {
                type = NavType.IntType
                defaultValue = 0
            },
            navArgument(name = "pageSize") {
                type = NavType.IntType
                defaultValue = 50
            },
        )
    ) {
        val viewModel: BattingDetailsScreenViewModel = koinInject()
        var hasNavigated by remember { mutableStateOf(false) }

        if (!hasNavigated) {
            hasNavigated = true
            println("Navigated")

            val searchParameters = SearchParameters(
                matchType = it.arguments?.getString("matchType") ?: "t",
                matchSubType = it.arguments?.getString("matchSubType") ?: "t",
                teamId = it.arguments?.getInt("teamId") ?: 0,
                opponentsId = it.arguments?.getInt("opponentsId") ?: 0,
                groundId = it.arguments?.getInt("groundId") ?: 0,
                hostCountryId = it.arguments?.getInt("hostCountryId") ?: 0,
                venue = it.arguments?.getInt("venue") ?: 0,
                sortOrder = SortOrder.entries[it.arguments?.getInt("sortOrder") ?: 4],
                sortDirection = SortDirection.valueOf(it.arguments?.getString("sortDirection") ?: "DESC"),
                startDate = it.arguments?.getLong("startDate") ?: -99999999999L,
                endDate = it.arguments?.getLong("endDate") ?: 999999999999L,
                result = it.arguments?.getInt("result") ?: 0,
                season = it.arguments?.getString("season") ?: "All",
                pageSize = it.arguments?.getInt("pageSize") ?: 50,
                startRow = it.arguments?.getInt("startRow") ?: 0,
                limit = it.arguments?.getInt("limit") ?: 100,
            )

            viewModel.getSearchResults(searchParameters)
        }

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

        BattingDetailsScreen(displayRecords)
    }
}

@Composable
fun BattingDetailsScreen(displayRecords: List<List<String>>) {
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


