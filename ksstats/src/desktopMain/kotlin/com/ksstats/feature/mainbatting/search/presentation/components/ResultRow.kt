package com.ksstats.feature.mainbatting.search.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ksstats.core.presentation.components.TextCheckBox
import com.ksstats.feature.mainbatting.presentation.BattingSearchEvent
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.drawnLabel
import com.ksstats.ksstats.generated.resources.tiedLabel
import com.ksstats.ksstats.generated.resources.wonLabel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResultRow(
    onBattingEvent: (BattingSearchEvent) -> Unit = {},
) {
    Row(modifier = Modifier.padding(bottom = 10.dp)) {
        val (wonState, onWonStateChange) = remember { mutableStateOf(false) }
        val (lostState, onLostStateChange) = remember { mutableStateOf(false) }
        val (drawnState, onDrawnStateChange) = remember { mutableStateOf(false) }
        val (tiedState, onTiedStateChange) = remember { mutableStateOf(false) }

        Text(stringResource(Res.string.resultsLabel), modifier = Modifier.padding(start = 5.dp, top = 16.dp))
        TextCheckBox(text = stringResource(Res.string.wonLabel), state = wonState, onStateChange = {
            onWonStateChange(it)
            onBattingEvent(BattingSearchEvent.ResultSelectionEvent(!wonState, lostState, drawnState, tiedState))
        })
        TextCheckBox(text = stringResource(Res.string.lostLabel), state = lostState, onStateChange = {
            onLostStateChange(it)
            onBattingEvent(BattingSearchEvent.ResultSelectionEvent(wonState, !lostState, drawnState, tiedState))
        })
        TextCheckBox(text = stringResource(Res.string.drawnLabel), state = drawnState, onStateChange = {
            onDrawnStateChange(it)
            onBattingEvent(BattingSearchEvent.ResultSelectionEvent(wonState, lostState, !drawnState, tiedState))
        })
        TextCheckBox(text = stringResource(Res.string.tiedLabel), state = tiedState, onStateChange = {
            onTiedStateChange(it)
            onBattingEvent(BattingSearchEvent.ResultSelectionEvent(wonState, lostState, drawnState, !tiedState))
        })


    }
}