package com.ksstats.feature.battingrecordsdisplay.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ksstats.core.domain.util.*
import com.ksstats.core.presentation.StatsAppScreen
import com.ksstats.core.presentation.components.*
import com.ksstats.feature.battingrecordsdisplay.data.BattingSearchResults
import com.ksstats.feature.battingrecordsdisplay.domain.usecase.BattingDetailsUseCases
import com.ksstats.feature.summary.domain.usecase.SummaryUseCases
import com.ksstats.feature.summary.util.SummarySearchParameters
import com.ksstats.feature.summary.util.buildSummary
import org.koin.compose.koinInject

fun NavGraphBuilder.battingDetailsScreen(navigate: (String) -> Unit) {
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
                "&season={season}" +
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
                defaultValue = 3
            },
            navArgument(name = "sortDirection") {
                type = NavType.StringType
                defaultValue = "DESC"
            },
            navArgument(name = "startDate") {
                type = NavType.LongType
                defaultValue = -8520336000 // 1700-1-1
            },
            navArgument(name = "endDate") {
                type = NavType.LongType
                defaultValue = 253402214400 // 9999-12-31
            },
            navArgument(name = "season") {
                type = NavType.StringType
                defaultValue = "All"
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
    ) { navBackStackEntry ->

        var pagingParameters by remember { mutableStateOf(PagingParameters(startRow = 0, pageSize = 50, limit = 100)) }
        var searchParameters by remember { mutableStateOf(SearchParameters()) }
        var summarySearchParameters by remember {
            mutableStateOf(
                SummarySearchParameters(
                    "",
                    "",
                    0,
                    0,
                    0,
                    0
                )
            )
        }


        val battingUseCases: BattingDetailsUseCases = koinInject()
        val summaryUseCases: SummaryUseCases = koinInject()
        val viewModel: BattingDetailsScreenViewModel = viewModel {
            BattingDetailsScreenViewModel(battingUseCases, summaryUseCases)
        }
        val summary = viewModel.summary.collectAsState()

        LaunchedEffect(Unit) {
            val matchType = navBackStackEntry.arguments?.getString("matchType") ?: "t"
            val matchSubType = navBackStackEntry.arguments?.getString("matchSubType") ?: "t"
            val teamId = navBackStackEntry.arguments?.getInt("teamId") ?: 0
            val opponentsId = navBackStackEntry.arguments?.getInt("opponentsId") ?: 0
            val groundId = navBackStackEntry.arguments?.getInt("groundId") ?: 0
            val hostCountryId = navBackStackEntry.arguments?.getInt("hostCountryId") ?: 0
            val startDate = navBackStackEntry.arguments?.getLong("startDate") ?: -99999999999L
            val endDate = navBackStackEntry.arguments?.getLong("endDate") ?: 999999999999L

            searchParameters = SearchParameters(
                matchType = matchType,
                matchSubType = matchSubType,
                teamId = teamId,
                opponentsId = opponentsId,
                groundId = groundId,
                hostCountryId = hostCountryId,
                venue = navBackStackEntry.arguments?.getInt("venue") ?: 0,
                sortOrder = SortOrder.entries[navBackStackEntry.arguments?.getInt("sortOrder") ?: 3],
                sortDirection = SortDirection.valueOf(
                    navBackStackEntry.arguments?.getString("sortDirection") ?: "DESC"
                ),
                startDate = startDate,
                endDate = endDate,
                result = navBackStackEntry.arguments?.getInt("result") ?: 0,
                season = navBackStackEntry.arguments?.getString("season") ?: "All",
                pageSize = navBackStackEntry.arguments?.getInt("pageSize") ?: pagingParameters.pageSize,
                startRow = navBackStackEntry.arguments?.getInt("startRow") ?: pagingParameters.startRow,
                limit = navBackStackEntry.arguments?.getInt("limit") ?: pagingParameters.limit,
            )

            pagingParameters = pagingParameters.copy(
                startRow = navBackStackEntry.arguments?.getInt("startRow") ?: pagingParameters.startRow,
                pageSize = navBackStackEntry.arguments?.getInt("pageSize") ?: pagingParameters.pageSize,
                limit = navBackStackEntry.arguments?.getInt("limit") ?: pagingParameters.limit
            )

            summarySearchParameters = SummarySearchParameters(
                matchType = matchType,
                matchSubType = matchSubType,
                teamId = teamId,
                opponentsId = opponentsId,
                groundId = groundId,
                hostCountryId = hostCountryId,
            )

            viewModel.getSearchResults(searchParameters)

            viewModel.getSummary(
                searchParameters = SummarySearchParameters(
                    matchType,
                    matchSubType,
                    teamId,
                    opponentsId,
                    groundId,
                    hostCountryId
                ),
                startDate = startDate,
                endDate = endDate
            )

        }

        val searchResults = viewModel.battingSearchResults.collectAsState()

        val count = searchResults.value.firstOrNull()?.count ?: 0
        val startRow = (navBackStackEntry.arguments?.getInt("startRow") ?: 0)
        val displayRecords: List<List<String>> = getDisplayRecords(searchResults.value, startRow)

        val startDate = navBackStackEntry.arguments?.getLong("startDate") ?: -99999999999L
        val endDate = navBackStackEntry.arguments?.getLong("endDate") ?: 999999999999L

        var summaryString: String by remember { mutableStateOf("") }
        summaryString = summary.value.buildSummary(startDate, endDate)

        BattingDetailsScreen(
            displayRecords = displayRecords,
            title = "Player Batting Summary",
            summary = summaryString,
            pageSize = navBackStackEntry.arguments?.getInt("pageSize") ?: 50,
            startRow = navBackStackEntry.arguments?.getInt("startRow") ?: 0,
            limit = navBackStackEntry.arguments?.getInt("limit") ?: 100,
            rowCount = count,
            onPageChanged = {
                pagingParameters = calculatePagingParameters(it, pagingParameters, count)
                searchParameters = searchParameters.copy(
                    pageSize = pagingParameters.pageSize,
                    limit = pagingParameters.limit,
                    startRow = pagingParameters.startRow
                )

                val navUrl = buildNavUrl(StatsAppScreen.BattingDetails.name, searchParameters)
                navigate(navUrl)
            }
        )
    }
}


fun getDisplayRecords(searchResults: List<BattingSearchResults>, startRow: Int): List<List<String>> {
    return searchResults.mapIndexed { index, searchResult ->

        val highestScore = getHighestScoreString(searchResult.highestScore)

        listOf(
            (index + startRow + 1).toString(),
            searchResult.name,
            searchResult.team,
            searchResult.matches.toString(),
            searchResult.innings.toString(),
            searchResult.notOuts.toString(),
            searchResult.runs.toString(),
            highestScore,
            searchResult.hundreds.toString(),
            searchResult.fifties.toString(),
            searchResult.ducks.toString(),
            searchResult.fours.toString(),
            searchResult.sixes.toString(),
            searchResult.balls.toString(),
            searchResult.average.truncate(2),
            searchResult.strikeRate.round(2),
            searchResult.battingImpact.round(3),
        )
    }
}

fun getHighestScoreString(highestScore: Double): String {
    val hsWhole = highestScore.toInt()
    val hsPart = Math.ceil(highestScore).toInt()

    if (hsWhole == hsPart) {
        return hsWhole.toString()
    } else {
        return "${hsWhole}*"
    }
}

fun buildNavUrl(baseUrl: String, searchParameters: SearchParameters): String {


    return "${baseUrl}?" +
            "matchType=${searchParameters.matchType}" +
            "&matchSubType=${searchParameters.matchSubType}" +
            "&teamId=${searchParameters.teamId}" +
            "&opponentsId=${searchParameters.opponentsId}" +
            "&groundId=${searchParameters.groundId}" +
            "&hostCountryId=${searchParameters.hostCountryId}" +
            "&venue=${searchParameters.venue}" +
            "&sortOrder=${searchParameters.sortOrder}" +
            "&sortDirection=${searchParameters.sortDirection}" +
            "&startDate=${searchParameters.startDate}" +
            "&endDate=${searchParameters.endDate}" +
            "&season=${searchParameters.season}" +
            "&result=${searchParameters.result}" +
            "&limit=${searchParameters.limit}" +
            "&startRow=${searchParameters.startRow}" +
            "&pageSize=${searchParameters.pageSize}"

}

fun calculatePagingParameters(
    pageChangedNavigation: PageChangedNavigation,
    pagingParameters: PagingParameters,
    rowCount: Int,
): PagingParameters {

    val previous = pagingParameters.startRow - pagingParameters.pageSize
    val next = pagingParameters.startRow + pagingParameters.pageSize
    val lastRow =
        (pagingParameters.pageSize.calculateMaxPages(rowCount) * pagingParameters.pageSize) - pagingParameters.pageSize

    val newPagingParameters = when (pageChangedNavigation) {
        PageChangedNavigation.First -> pagingParameters.copy(startRow = 0)
        PageChangedNavigation.Previous -> pagingParameters.copy(startRow = previous)
        PageChangedNavigation.Next -> pagingParameters.copy(startRow = next)
        PageChangedNavigation.Last -> pagingParameters.copy(startRow = lastRow)
        is PageChangedNavigation.PageSizeChange -> pagingParameters.copy(pageSize = pageChangedNavigation.pageSize)
    }
    return newPagingParameters
}

@Composable
fun BattingDetailsScreen(
    displayRecords: List<List<String>>,
    summary: String,
    title: String,
    pageSize: Int,
    startRow: Int,
    limit: Int,
    rowCount: Int,
    onPageChanged: (PageChangedNavigation) -> Unit,
) {

    Column(
        // initial height needed as the grid is empty, setting it to 0 is fine (it appears!)
        modifier = Modifier
            .height(0.dp)
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleRow(title = title)
        SummaryRow(summary = summary)
        SearchLimitRow(searchLimit = limit)
        NavigationRow(
            pageNumber = 1,
            pageSize = pageSize,
            maxPages = pageSize.calculateMaxPages(rowCount),
            firstRowNumber = startRow + 1,
            lastRowNumber = rowCount,
            onPageChanged = { onPageChanged(it) }
        )

        Row {
            val metaData = listOf(
                ColumnMetaData("", 60.dp),
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
            Table(
                columnCount = metaData.size,
                rowCount = displayRecords.size,
                cellContent = { column, row ->
                    if (displayRecords.isNotEmpty())
                        displayRecords[row][column]
                    else
                        null
                },
                metaData = metaData
            )
        }
    }
}




