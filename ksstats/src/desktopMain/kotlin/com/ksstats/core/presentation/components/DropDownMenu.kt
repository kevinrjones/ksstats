package com.ksstats.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class DropDownMenuState(val key: String = "", val value: String = "")
data class AppDropDownParams(
    val options: List<DropDownMenuState> = listOf(),
    val selectedOption: DropDownMenuState = DropDownMenuState(),
    val label: String = "",
)

@Composable
fun AppDropDownMenu(
    modifier: Modifier = Modifier,
    options: List<DropDownMenuState>,
    selectedOption: DropDownMenuState,
    label: String,
    onSelectMenuItem: (DropDownMenuState) -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier

    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption.value,
            onValueChange = {},
            label = { Text(text = label) },
            colors = OutlinedTextFieldDefaults.colors(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            modifier = Modifier,
            onDismissRequest = { expanded = !expanded }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.value) },
                    onClick = {
                        expanded = false
                        onSelectMenuItem(option)
                    }
                )
            }
        }
    }
}

// See this: https://proandroiddev.com/improving-the-compose-dropdownmenu-88469b1ef34
@Composable
fun <T> LargeDropdownMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    notSetLabel: String? = null,
    items: List<T>,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (T, Boolean, Boolean, () -> Unit) -> Unit =
        { item, selected, itemEnabled, onClick ->
            LargeDropdownMenuItem(
                text = item.toString(),
                selected = selected,
                enabled = itemEnabled,
                onClick = onClick,
            )
        },
    filterItem: (T, String) -> Boolean = { item, filter -> true },
) {
    var expanded by remember { mutableStateOf(false) }


    Box(
        modifier =
        modifier.height(IntrinsicSize.Min)

    ) {


        OutlinedTextField(
            label = { Text(label) },
            value = items.getOrNull(selectedIndex)
                ?.let { selectedItemToString(it) } ?: "",
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
                .offset(y = (-9).dp),
            trailingIcon = {
                val icon = expanded.select(Icons.Filled.ArrowDropUp, Icons.Filled.ArrowDropDown)
                Icon(icon, "")
            },
            onValueChange = { },
            readOnly = true,
        )
        // Transparent clickable surface on top of OutlinedTextField
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 1.dp)
                .offset(y = (-9).dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable(enabled = enabled) { expanded = true },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        val focusRequester = remember { FocusRequester() }
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
            ) {
                var filter by remember { mutableStateOf("") }
                var filteredItems by remember { mutableStateOf(items) }


                val listState = rememberLazyListState()

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }

                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .padding(bottom = 2.dp),
                        value = filter,
                        onValueChange = {
                            filter = it
                            if (it.isBlank())
                                filteredItems = items
                            else
                                filteredItems =
                                    items.filter {
                                        filterItem(it, filter)
                                    }
                        },

                    )
                    if (selectedIndex > -1) {
                        LaunchedEffect("ScrollToSelected") {
                            listState.scrollToItem(index = selectedIndex)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        state = listState
                    ) {
                        if (notSetLabel != null) {
                            item {
                                LargeDropdownMenuItem(
                                    text = notSetLabel,
                                    selected = false,
                                    enabled = false,
                                    onClick = { },
                                )
                            }
                        }
                        itemsIndexed(filteredItems) { index, item ->
                            val selectedItem = index == selectedIndex
                            drawItem(
                                item,
                                selectedItem,
                                true
                            ) {
                                val indexInOriginalList = items.indexOf(item)

                                onItemSelected(indexInOriginalList, item)
                                expanded = false
                            }

                            if (index < items.lastIndex) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Boolean.select(arrowDropUp: ImageVector, arrowDropDown: ImageVector): ImageVector {
    if (this) return arrowDropUp else return arrowDropDown
}

@Composable
fun LargeDropdownMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
        selected -> MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.high)
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(enabled) { onClick() }
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}