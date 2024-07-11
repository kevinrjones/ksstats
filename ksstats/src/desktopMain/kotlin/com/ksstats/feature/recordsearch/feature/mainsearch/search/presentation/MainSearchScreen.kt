package com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ksstats.core.domain.util.SortDirection
import com.ksstats.core.domain.util.SortOrder
import com.ksstats.core.presentation.StatsAppScreens
import com.ksstats.core.presentation.components.AppDropDownParams
import com.ksstats.core.presentation.components.DropDownMenuState
import com.ksstats.core.types.MatchType
import com.ksstats.feature.recordsearch.domain.presentation.components.*
import com.ksstats.feature.recordsearch.domain.usecase.RecordSearchUseCases
import com.ksstats.feature.recordsearch.feature.mainsearch.search.utils.MainSearchType
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.shared.now
import com.ksstats.shared.toSeconds
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(FormatStringsInDatetimeFormats::class)
fun NavGraphBuilder.mainSearchScreen(
    searchType: MainSearchType,
    limitLabel: StringResource,
    navigate: (String) -> Unit,
) {
    composable(
        route = searchType.screen.name
                + "?matchType={matchType}",
        arguments = listOf(
            navArgument(name = "matchType") {
                type = NavType.StringType
                nullable = true
            },
        )
    ) {

        val recordSearchUseCases: RecordSearchUseCases = koinInject()
        val viewModel: MainSearchViewModel = viewModel {
            MainSearchViewModel(recordSearchUseCases)
        }

        val dateTimeFormat = LocalDate.Format {
            byUnicodePattern("dd/MM/yyyy")
        }


        MainSearchScreen(
            matchTypeParams = AppDropDownParams(
                viewModel.state.value.matchTypes.map { DropDownMenuState(it.type, it.description) },
                DropDownMenuState(
                    viewModel.state.value.selectedMatchType.type,
                    viewModel.state.value.selectedMatchType.description
                ),
                stringResource(Res.string.matchTypeLabel)
            ),
            competitionParams = AppDropDownParams(
                viewModel.state.value.competitions.map { DropDownMenuState(it.subType, it.competition) },
                DropDownMenuState(
                    viewModel.state.value.selectedCompetition.type,
                    viewModel.state.value.selectedCompetition.competition
                ),
                stringResource(Res.string.competitionLabel)
            ),
            teamParams = AppDropDownParams(
                viewModel.state.value.teams.map { DropDownMenuState(it.id.toString(), it.name) },
                DropDownMenuState(
                    viewModel.state.value.selectedTeam.id.toString(),
                    viewModel.state.value.selectedTeam.name
                ),
                stringResource(Res.string.teamsLabel)
            ),

            oppositionParams = AppDropDownParams(
                viewModel.state.value.teams.map { DropDownMenuState(it.id.toString(), it.name) },
                DropDownMenuState(
                    viewModel.state.value.selectedOpposition.id.toString(),
                    viewModel.state.value.selectedOpposition.name
                ),
                stringResource(Res.string.opponentsLabel)
            ),
            groundsParams = AppDropDownParams(
                viewModel.state.value.grounds.map { DropDownMenuState(it.id.toString(), it.name) },
                DropDownMenuState(
                    viewModel.state.value.selectedGround.id.toString(),
                    viewModel.state.value.selectedGround.name
                ),
                stringResource(Res.string.groundsLabel)
            ),
            countriesParams = AppDropDownParams(
                viewModel.state.value.countries.map { DropDownMenuState(it.id.toString(), it.name) },
                DropDownMenuState(
                    viewModel.state.value.selectedCountry.id.toString(),
                    viewModel.state.value.selectedCountry.name
                ),
                stringResource(Res.string.countriesLabel)
            ),
            seriesDatesParams = AppDropDownParams(
                viewModel.state.value.seriesDates.map { DropDownMenuState(it, it) },
                DropDownMenuState(viewModel.state.value.selectedSeriesDate, viewModel.state.value.selectedSeriesDate),
                stringResource(Res.string.seriesDatesLabel)
            ),
            pageSizesParams = AppDropDownParams(
                viewModel.state.value.pageSizes.map { DropDownMenuState(it.toString(), it.toString()) },
                DropDownMenuState(
                    viewModel.state.value.selectedPageSize.toString(),
                    viewModel.state.value.selectedPageSize.toString()
                ),
                stringResource(Res.string.pageSizeLabel)
            ),
            limit = viewModel.state.value.minimumRuns,
            limitLabel = stringResource(limitLabel),
            startDateLabel = viewModel.state.value.startDate.format(dateTimeFormat), // displayLabel = ldt?.format(dateTimeFormat)!!
            startDate = viewModel.state.value.startDate,
            endDateLabel = viewModel.state.value.endDate.format(dateTimeFormat),
            endDate = viewModel.state.value.endDate,
            searchViewFormat = viewModel.state.value.searchViewFormat,
            isLoaded = viewModel.state.value.loaded,
            mainSearchType = searchType,
            onSearchEvent = { evt: MainSearchEvent ->
                viewModel.uiEvent(evt)
            },
            navigate = { baseUrl, sortOrder, sortDirection ->
                val navUrl = buildNavUrl(baseUrl, viewModel, sortOrder, sortDirection)
                navigate(navUrl)
            }
        )
    }
}

fun buildNavUrl(baseUrl: String, viewModel: MainSearchViewModel, sortOrder: SortOrder, sortDirection: SortDirection): String {

    return "${baseUrl}?" +
            "matchType=${viewModel.state.value.selectedMatchType.type}" +
            "&matchSubType=${viewModel.state.value.selectedCompetition.subType}" +
            "&teamId=${viewModel.state.value.selectedTeam.id}" +
            "&opponentsId=${viewModel.state.value.selectedOpposition.id}" +
            "&groundId=${viewModel.state.value.selectedGround.id}" +
            "&hostCountryId=${viewModel.state.value.selectedCountry.id}" +
            "&venue=${viewModel.state.value.venue}" +
            "&sortOrder=${sortOrder.ordinal}" +
            "&sortDirection=${sortDirection.name}" +
            "&startDate=${viewModel.state.value.startDate.toSeconds()}" +
            "&endDate=${viewModel.state.value.endDate.toSeconds()}" +
            "&season=${viewModel.state.value.selectedSeriesDate}" +
            "&result=${viewModel.state.value.matchResult}" +
            "&limit=${viewModel.state.value.minimumRuns}" +
            "&startRow=0" +
            "&pageSize=${viewModel.state.value.selectedPageSize}"

}


@Composable
fun MainSearchScreen(
    matchTypeParams: AppDropDownParams,
    competitionParams: AppDropDownParams,
    teamParams: AppDropDownParams,
    oppositionParams: AppDropDownParams,
    groundsParams: AppDropDownParams,
    countriesParams: AppDropDownParams,
    seriesDatesParams: AppDropDownParams,
    pageSizesParams: AppDropDownParams,
    limit: Int,
    limitLabel: String,
    startDateLabel: String,
    startDate: LocalDate,
    endDateLabel: String,
    endDate: LocalDate,
    searchViewFormat: SearchViewFormat,
    isLoaded: Boolean,
    mainSearchType: MainSearchType,
    onSearchEvent: (MainSearchEvent) -> Unit = {},
    navigate: (String, SortOrder, SortDirection) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        val selectedMatchType = MatchType(matchTypeParams.selectedOption.key)

        MatchTypeRow(matchTypeParams, competitionParams, pageSizesParams, limit, limitLabel, onSearchEvent)
        TeamsRow(teamParams, oppositionParams, onSearchEvent)
        VenueRow(onSearchEvent)
        CountriesGroundsRow(groundsParams, countriesParams, onSearchEvent)
        DateRangeRow(seriesDatesParams, startDateLabel, startDate, endDateLabel, endDate, onSearchEvent)
        ResultRow(onSearchEvent)
        ViewFormatRow(searchViewFormat, selectedMatchType, onSearchEvent)
        ButtonRow(isLoaded, onBattingEvent = {
            when (it) {
                is MainSearchEvent.SearchMain -> {
                    val navigateTo = getNavigateTo(mainSearchType, searchViewFormat)
                    val (sortOrder, sortDirection) = getDefaultSortParameters(mainSearchType, searchViewFormat)
                    navigate(navigateTo, sortOrder, sortDirection)
                }

                else -> {
                    onSearchEvent(it)
                }
            }
        })
    }
}

fun getNavigateTo(mainSearchType: MainSearchType, searchViewFormat: SearchViewFormat): String {
    return when (mainSearchType) {

        MainSearchType.Batting -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> StatsAppScreens.BattingPlayerSummary.name
            SearchViewFormat.InningsByInnings -> StatsAppScreens.BattingInningsByInnings.name
            SearchViewFormat.MatchTotals -> StatsAppScreens.BattingMatchTotals.name
            SearchViewFormat.SeriesAverages -> StatsAppScreens.BattingSeriesAverages.name
            SearchViewFormat.GroundAverages -> StatsAppScreens.BattingGroundAverages.name
            SearchViewFormat.ByHostCountry -> StatsAppScreens.BattingByHostCuntry.name
            SearchViewFormat.ByOppositionTeam -> StatsAppScreens.BattingByOppositionTeam.name
            SearchViewFormat.ByYearOfMatchStart -> StatsAppScreens.BattingByYear.name
            SearchViewFormat.BySeason -> StatsAppScreens.BattingBySeason.name
        }

        MainSearchType.Bowling -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> StatsAppScreens.BowlingPlayerSummary.name
            SearchViewFormat.InningsByInnings -> TODO()
            SearchViewFormat.MatchTotals -> TODO()
            SearchViewFormat.SeriesAverages -> TODO()
            SearchViewFormat.GroundAverages -> TODO()
            SearchViewFormat.ByHostCountry -> TODO()
            SearchViewFormat.ByOppositionTeam -> TODO()
            SearchViewFormat.ByYearOfMatchStart -> TODO()
            SearchViewFormat.BySeason -> TODO()
        }
    }
}

fun getDefaultSortParameters(mainSearchType: MainSearchType, searchViewFormat: SearchViewFormat): Pair<SortOrder, SortDirection> {
    return when (mainSearchType) {

        MainSearchType.Batting -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> Pair(SortOrder.Runs, SortDirection.Descending)
            SearchViewFormat.InningsByInnings -> Pair(SortOrder.MatchStartDateAsOffset, SortDirection.Ascending)
            else -> TODO()
        }

        MainSearchType.Bowling -> when (searchViewFormat) {
            SearchViewFormat.PlayerSummary -> Pair(SortOrder.Wickets, SortDirection.Descending)
            SearchViewFormat.InningsByInnings -> Pair(SortOrder.MatchStartDateAsOffset, SortDirection.Descending)
            else -> TODO()
        }
    }
}

@Preview
@Composable
fun MainBattingSearchScreenPreview() {
    MainSearchScreen(
        matchTypeParams = AppDropDownParams(),
        competitionParams = AppDropDownParams(),
        teamParams = AppDropDownParams(),
        oppositionParams = AppDropDownParams(),
        groundsParams = AppDropDownParams(),
        countriesParams = AppDropDownParams(),
        seriesDatesParams = AppDropDownParams(),
        pageSizesParams = AppDropDownParams(),
        limit = 100,
        limitLabel = stringResource(Res.string.minimumRunsLabel),
        startDateLabel = "Start Date",
        startDate = LocalDate.now(),
        endDateLabel = "End Date",
        endDate = LocalDate.now(),
        searchViewFormat = SearchViewFormat.PlayerSummary,
        isLoaded = true,
        mainSearchType = MainSearchType.Batting,
        onSearchEvent = {},
        navigate = {url, sortOrder, sortDirection ->}
    )
}
