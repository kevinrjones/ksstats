package com.ksstats.feature.mainbatting.search.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ksstats.core.presentation.components.TextCheckBox
import com.ksstats.feature.mainbatting.presentation.BattingSearchEvent
import com.ksstats.ksstats.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun VenueRow(onBattingEvent: (BattingSearchEvent) -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp, start = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val (home, onHomeStateChange) = remember { mutableStateOf(false) }
        val (away, onAwayStateChange) = remember { mutableStateOf(false) }
        val (neutral, onNeutralStateChange) = remember { mutableStateOf(false) }

        Text(text = stringResource(Res.string.venueLabel), modifier = Modifier)

        TextCheckBox(text = stringResource(Res.string.homeLabel), state = home, onStateChange = {
            onHomeStateChange(it)
            onBattingEvent(BattingSearchEvent.VenueSelectionEvent(!home, away, neutral))
        })
        TextCheckBox(text = stringResource(Res.string.awayLabel), state = away, onStateChange = {
            onAwayStateChange(it)
            onBattingEvent(BattingSearchEvent.VenueSelectionEvent(home, !away, neutral))
        })
        TextCheckBox(text = stringResource(Res.string.neutralLabel), state = neutral, onStateChange = {
            onNeutralStateChange(it)
            onBattingEvent(BattingSearchEvent.VenueSelectionEvent(home, away, !neutral))
        })
    }

}