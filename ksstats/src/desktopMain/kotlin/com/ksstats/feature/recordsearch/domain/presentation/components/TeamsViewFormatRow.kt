package com.ksstats.feature.recordsearch.domain.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ksstats.core.presentation.components.HorizontalTextRadioButton
import com.ksstats.core.types.MatchType
import com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation.MainSearchEvent
import com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation.SearchViewFormat
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.viewFormatLabel
import org.jetbrains.compose.resources.stringResource


@Composable
fun TeamsViewFormatRow(
    searchViewFormat: SearchViewFormat,
    selectedMatchType: MatchType,
    onChangeFormatEvent: (MainSearchEvent) -> Unit = {},
) {
    Row(modifier = Modifier.padding(bottom = 10.dp)) {
        Box {
            Text(stringResource(Res.string.viewFormatLabel))
        }
        Column(modifier = Modifier.weight(1f)) {
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.TeamSummary,
                text = stringResource(SearchViewFormat.TeamSummary.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.TeamSummary))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.InningsByInnings,
                text = stringResource(SearchViewFormat.InningsByInnings.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.InningsByInnings))
                })
            if(selectedMatchType.isMultiInningsType()) {
                HorizontalTextRadioButton(
                    selected = searchViewFormat == SearchViewFormat.MatchTotals,
                    text = stringResource(SearchViewFormat.MatchTotals.format),
                    onOptionSelected = {
                        onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.MatchTotals))
                    })
            }
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.MatchResults,
                text = stringResource(SearchViewFormat.MatchResults.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.MatchResults))
                })
        }
        Column(modifier = Modifier.weight(1f)) {
            if(selectedMatchType.isInternationalType()) {
                HorizontalTextRadioButton(
                    selected = searchViewFormat == SearchViewFormat.SeriesAverages,
                    text = stringResource(SearchViewFormat.SeriesAverages.format),
                    onOptionSelected = {
                        onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.SeriesAverages))
                    })
            }
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.GroundAverages,
                text = stringResource(SearchViewFormat.GroundAverages.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.GroundAverages))
                })
            if(selectedMatchType.isInternationalType()) {
                HorizontalTextRadioButton(
                    selected = searchViewFormat == SearchViewFormat.ByHostCountry,
                    text = stringResource(SearchViewFormat.ByHostCountry.format),
                    onOptionSelected = {
                        onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.ByHostCountry))
                    })
            }
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.ByOppositionTeam,
                text = stringResource(SearchViewFormat.ByOppositionTeam.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.ByOppositionTeam))
                })
        }
        Column(modifier = Modifier.weight(1f)) {
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.ByYearOfMatchStart,
                text = stringResource(SearchViewFormat.ByYearOfMatchStart.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.ByYearOfMatchStart))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.BySeason,
                text = stringResource(SearchViewFormat.BySeason.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.BySeason))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.OverallExtras,
                text = stringResource(SearchViewFormat.OverallExtras.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.OverallExtras))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.ExtrasByInnings,
                text = stringResource(SearchViewFormat.ExtrasByInnings.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.ExtrasByInnings))
                })
        }
        Column(modifier = Modifier.weight(1f)) {
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.HighestTargetChased,
                text = stringResource(SearchViewFormat.HighestTargetChased.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.HighestTargetChased))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.SuccessfullyDefendedLowestTarget,
                text = stringResource(SearchViewFormat.SuccessfullyDefendedLowestTarget.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.SuccessfullyDefendedLowestTarget))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.SuccessfullyDefendedInUnReducedMatch,
                text = stringResource(SearchViewFormat.SuccessfullyDefendedInUnReducedMatch.format),
                onOptionSelected = {
                    onChangeFormatEvent(MainSearchEvent.SearchViewFormatEvent(SearchViewFormat.SuccessfullyDefendedInUnReducedMatch))
                })
        }

    }
}