package com.ksstats.core.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.jooq.impl.QOM.Mod

@Composable
fun TextCheckBox(modifier: Modifier = Modifier, text: String, state: Boolean, onStateChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .height(56.dp)
            .toggleable(
                value = state,
                onValueChange = { onStateChange(!state) },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = null // null recommended for accessibility with screenreaders
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}