package com.ksstats.feature.recordsearch.domain.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.ksstats.core.presentation.components.HorizontalTextRadioButton
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.batting
import com.ksstats.ksstats.generated.resources.bowling
import org.jetbrains.compose.resources.stringResource

@Composable
fun BattingOrBowlingTeamRow(battingSelected: Boolean, onTeamTypeChanged: () -> Unit = {},) {
    Row(horizontalArrangement = Arrangement.Start,) {
        HorizontalTextRadioButton(
            selected = battingSelected,
            text = stringResource(Res.string.batting),
            onOptionSelected = {
                onTeamTypeChanged()
            })
        HorizontalTextRadioButton(
            selected = !battingSelected,
            text = stringResource(Res.string.bowling),
            onOptionSelected = {
                onTeamTypeChanged()
            })

    }
}