package com.ksstats.core.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleRow(modifier: Modifier = Modifier, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.TopStart)
            .padding(top = 10.dp)
            .then(modifier)

    ) {
        Text(title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
    }
}

@Composable
fun SummaryRow(modifier: Modifier = Modifier, summary: String = "") {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.TopStart)
            .then(modifier)
    ) {
        Text(summary)
    }
}

@Composable
fun SearchLimitRow(modifier: Modifier = Modifier, searchLimit: Int, postFix: String = "Runs") {
    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.TopStart)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = buildAnnotatedString {
                append("Search limit is ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$searchLimit ")
                }
                append(postFix)
            }
        )

    }
}

@Composable
fun NavigationRow(
    modifier: Modifier = Modifier,
    pageNumber: Int,
    pageSize: Int,
    maxPages: Int,
    firstRowNumber: Int,
    lastRowNumber: Int,
    onPageChanged: (PageChangedNavigation) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .wrapContentSize(align = Alignment.TopStart)
            .padding(bottom = 10.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(

            text = buildAnnotatedString {
                append("Page ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(pageNumber.toString())
                }
                append(" of ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(maxPages.toString())
                }
            }
        )
        Text(
            text = buildAnnotatedString {
                append("Showing ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(firstRowNumber.toString())
                }
                append(" to ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(lastRowNumber.toString())
                }

            }
        )
        NavigateButtons(
            modifier = Modifier.padding(start = 30.dp),
            onPageChanged = { onPageChanged(it) }
        )
        PageSize(
            maxRows = pageSize,
            onChange = { onPageChanged(it) }
        )

    }
}

@Composable
fun PageSize(modifier: Modifier = Modifier, maxRows: Int, onChange: (PageChangedNavigation.PageSizeChange) -> Unit) {

    val options = listOf<DropDownMenuState>(
        DropDownMenuState(key = "50", value = "50"),
        DropDownMenuState(key = "100", value = "100"),
        DropDownMenuState(key = "150", value = "150"),
        DropDownMenuState(key = "200", value = "200"),
    )

    var selectedOption by rememberSaveable {
        mutableStateOf(
            options.filter { it.key.toInt() == maxRows }.firstOrNull() ?: options[0]
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "Page Size"
        )
        AppDropDownMenu(
            modifier = Modifier.width(120.dp).padding(start = 10.dp),
            options = options,
            selectedOption = selectedOption,
            label = "",
            onSelectMenuItem = { selectedOption = it
            onChange(PageChangedNavigation.PageSizeChange(selectedOption.value.toInt()))}
        )
    }
}

sealed class PageChangedNavigation {
    data object First : PageChangedNavigation()
    data object Previous : PageChangedNavigation()
    data object Next : PageChangedNavigation()
    data object Last : PageChangedNavigation()
    data class PageSizeChange(val pageSize: Int) : PageChangedNavigation()
}

@Composable
fun NavigateButtons(modifier: Modifier = Modifier, onPageChanged: (PageChangedNavigation) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TextButton(onClick = { onPageChanged(PageChangedNavigation.First) }) {
            Text(text = "<< First")
        }
        TextButton(onClick = { onPageChanged(PageChangedNavigation.Previous) }) {
            Text(text = "< Previous")
        }
        TextButton(onClick = { onPageChanged(PageChangedNavigation.Next) }) {
            Text(text = "Next >")
        }
        TextButton(onClick = { onPageChanged(PageChangedNavigation.Last) }) {
            Text(text = "Last >>")
        }
    }
}
