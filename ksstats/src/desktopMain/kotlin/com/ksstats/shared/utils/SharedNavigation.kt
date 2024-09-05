package com.ksstats.shared.utils

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ksstats.core.domain.util.SearchParameters
import com.ksstats.core.presentation.StatsAppScreens

fun buildDetailsScreenNavUrl(baseUrl: String, searchParameters: SearchParameters): String {

    val sortOrder = searchParameters.sortOrder.ordinal

    return "${baseUrl}?" +
            "matchType=${searchParameters.matchType}" +
            "&matchSubType=${searchParameters.matchSubType}" +
            "&teamId=${searchParameters.teamId}" +
            "&opponentsId=${searchParameters.opponentsId}" +
            "&groundId=${searchParameters.groundId}" +
            "&hostCountryId=${searchParameters.hostCountryId}" +
            "&venue=${searchParameters.venue}" +
            "&sortOrder=${sortOrder}" +
            "&sortDirection=${searchParameters.sortDirection}" +
            "&startDate=${searchParameters.startDate}" +
            "&endDate=${searchParameters.endDate}" +
            "&season=${searchParameters.season}" +
            "&result=${searchParameters.result}" +
            "&limit=${searchParameters.pagingParameters.limit}" +
            "&startRow=${searchParameters.pagingParameters.startRow}" +
            "&pageSize=${searchParameters.pagingParameters.pageSize}"

}

fun buildSummaryScreenRoute(screen: StatsAppScreens): String {
    return screen.name +
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
            "&pageSize={pageSize}"
}

fun buildRecordsScreenNavArguments(limit: Int): List<NamedNavArgument> {
    return listOf(
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
            defaultValue = -1
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
            defaultValue = limit
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
}