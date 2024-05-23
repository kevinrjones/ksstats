package com.ksstats.feature.mainbatting.search.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ksstats.feature.mainbatting.presentation.BattingSearchEvent
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.mainDisplayLabel
import com.ksstats.ksstats.generated.resources.resetLabel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ButtonRow(
    isReady: Boolean,
    onBattingEvent: (BattingSearchEvent) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp, bottom = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = { onBattingEvent(BattingSearchEvent.SearchBatting) },
            modifier = Modifier.width(200.dp),
            enabled = isReady,
            ) {
            Text(text = stringResource(Res.string.mainDisplayLabel))
        }
        Button(
            onClick = { onBattingEvent(BattingSearchEvent.Reset) },
            modifier = Modifier.width(200.dp),
            enabled = isReady,
            ) {
            Text(text = stringResource(Res.string.resetLabel))
        }
    }
}