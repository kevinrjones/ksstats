package com.ksstats.core.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ksstats.core.domain.util.DisplaySortDirection
import com.ksstats.core.domain.util.SortOrder


// see:https://stackoverflow.com/questions/68143308/how-do-i-create-a-table-in-jetpack-compose

@Composable
fun CellText(
    text: String,
    cellBackgroundColor: Color = Color.Gray,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign = TextAlign.Start,
    width: Dp,
    color: Color = Color.Black,
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = style,
        color = color,
        textAlign = textAlign,
        modifier = Modifier
//            .border(1.dp, Color.Black)
            .width(width)
            .background(cellBackgroundColor)
            .padding(8.dp)

    )
}

@Composable
fun HeaderCellText(
    text: String,
    cellBackgroundColor: Color = Color.Gray,
    cellHoverColor: Color = Color.LightGray,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign = TextAlign.Start,
    width: Dp,
    color: Color = Color.Black,
    sortDirection: DisplaySortDirection,
    sortOrder: SortOrder,
    onSort: (SortOrder) -> Unit,
) {

    var backgroundColor by remember { mutableStateOf(cellBackgroundColor) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        interactionSource.interactions.collect { interaction: Interaction ->
            when (interaction) {
                is HoverInteraction.Enter -> {
                    backgroundColor = cellHoverColor
                }

                is HoverInteraction.Exit -> {
                    backgroundColor = cellBackgroundColor
                }
            }
        }
    }

    TextButton(
        modifier = Modifier
            .width(width)
            .background(backgroundColor),
        contentPadding = PaddingValues(3.dp),
        interactionSource = interactionSource,
        onClick = {
            onSort(sortOrder)
        }
    ) {
        val myId = "inlineContent"

        val inlineContent = createInlineContent(myId, sortDirection)

        val annotatedText = createAnnotatedText(sortDirection, text, myId)

        Text(
            text = annotatedText,
            inlineContent = inlineContent,
            textAlign = textAlign,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = style,
            color = color,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 6.dp, top = 8.dp, end = 6.dp, bottom = 8.dp)
        )
    }
}

private fun createAnnotatedText(
    sortDirection: DisplaySortDirection,
    text: String,
    myId: String,
): AnnotatedString {
    val annotatedText = when (sortDirection) {
        DisplaySortDirection.None -> buildAnnotatedString {
            append(text)
        }

        else -> buildAnnotatedString {
            append(text)
            appendInlineContent(myId, "[icon]")
        }

    }
    return annotatedText
}

private fun createInlineContent(
    myId: String,
    sortDirection: DisplaySortDirection,
): Map<String, InlineTextContent> {
    val inlineContent = mapOf(
        Pair(
            myId,
            InlineTextContent(
                Placeholder(
                    width = 20.sp,
                    height = 20.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
                )
            ) {
                when (sortDirection) {
                    DisplaySortDirection.None -> {}
                    DisplaySortDirection.Ascending -> Icon(
                        Icons.Filled.ArrowUpward,
                        "",
                        tint = Color.Black
                    )

                    DisplaySortDirection.Descending -> Icon(
                        Icons.Filled.ArrowDownward,
                        "",
                        tint = Color.Black
                    )
                }
            }
        )
    )
    return inlineContent
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RowScope.TableCell(
    text: String,
    showTooltip: Boolean = false,
    color: Color = Color.Black,
    cellBackgroundColor: Color = Color.White,
    textAlign: TextAlign = TextAlign.Start,
    weight: Float = 0f,
    style: TextStyle = LocalTextStyle.current,
    width: Dp,
) {
    if (showTooltip) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            modifier = Modifier.background(Color.White),
            tooltip = {
                PlainTooltip {
                    Text(
                        text = text,
                        color = Color.White,
                        modifier = Modifier.background(color = Color.Black),
                    )
                }
            },
            state = rememberTooltipState()
        ) {
            CellText(
                text = text,
                textAlign = textAlign,
                color = color,
                style = style,
                cellBackgroundColor = cellBackgroundColor,
                width = width
            )
        }
    } else {
        CellText(
            text = text,
            color = color,
            style = style,
            textAlign = textAlign,
            cellBackgroundColor = cellBackgroundColor,
            width = width
        )
    }
}

@Composable
fun RowScope.TableHeaderCell(
    text: String,
    color: Color = Color.Black,
    cellBackgroundColor: Color = Color.White,
    textAlign: TextAlign = TextAlign.Start,
    weight: Float = 0f,
    style: TextStyle = LocalTextStyle.current,
    width: Dp,
    sortDirection: DisplaySortDirection,
    sortOrder: SortOrder,
    onSort: (SortOrder) -> Unit,
) {
    HeaderCellText(
        text = text,
        textAlign = textAlign,
        color = color,
        style = style,
        cellBackgroundColor = cellBackgroundColor,
        width = width,
        sortOrder = sortOrder,
        sortDirection = sortDirection,
        onSort = { order ->
            onSort(order)
        })
}

data class ColumnMetaData(
    val name: String,
    val width: Dp,
    val weight: Float = 0f,
    val align: TextAlign = TextAlign.Start,
    val sortOrder: SortOrder = SortOrder.None,
    val sortDirection: DisplaySortDirection = DisplaySortDirection.None,
    val visible: Boolean = true,
    val replaceZero: Boolean = true,
)


@Composable
fun Table(
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    rowCount: Int,
    metaData: Map<String, ColumnMetaData?>,
    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    cellContent: @Composable (metaDataKey: String, rowIndex: Int) -> String?,
    onSort: (SortOrder) -> Unit,
) {

    Box(modifier = modifier.then(Modifier.horizontalScroll(horizontalScrollState))) {
        LazyColumn(state = verticalLazyListState) {
            stickyHeader {
                Row(Modifier.background(Color.Gray)) {
                    metaData.values.filterNotNull().forEach { header ->

                        if (header.visible) {
                            TableHeaderCell(
                                text = header.name,
                                textAlign = header.align,
                                width = header.width,
                                style = TextStyle(fontSize = 12.sp),
                                sortOrder = header.sortOrder,
                                sortDirection = header.sortDirection,
                                onSort = { order ->
                                    onSort(order)
                                }
                            )
                        }
                    }
                }
            }

            items(rowCount) { rowIndex ->
                Column {
                    beforeRow?.invoke(rowIndex)

                    Row(modifier = rowModifier) {
                        metaData.forEach { (key, value) ->
                            if (value != null) {
                                Box {
                                    cellContent(key, rowIndex)?.let { content ->
                                        val metaDatum = metaData[key]
                                        if (metaDatum != null) {
                                            if (metaDatum.visible) {
                                                if (rowIndex % 2 == 0)
                                                    this@Row.TableCell(
                                                        textAlign = metaDatum.align,
                                                        showTooltip = content.length > 10,
                                                        text = content,
                                                        cellBackgroundColor = Color.LightGray,
                                                        width = metaDatum.width
                                                    )
                                                else
                                                    this@Row.TableCell(
                                                        textAlign = metaDatum.align,
                                                        text = content,
                                                        showTooltip = content.length > 10,
                                                        width = metaDatum.width
                                                    )
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }

                    afterRow?.invoke(rowIndex)
                }
            }
        }
    }
}

fun getContent(
    displayRecords: List<Map<String, String>>,
    key: String,
    row: Int,
    allMetaData: Map<String, ColumnMetaData?>,
): String {
    val records = displayRecords[row]
    val metaData = allMetaData[key] ?: throw Exception("Invalid key: $key")
    val content = records[key] ?: throw Exception("Invalid key: $key")

    if (metaData.replaceZero) {
        if (content.trim() == "0" || content.trim() == "0.00")
            return "-"
    }
    return content
}

@Composable
fun TableOld(
    data: List<List<String>>,
    metaData: List<ColumnMetaData>,
) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        // Here is the header
        stickyHeader {
            Row(Modifier.background(Color.Gray)) {
                metaData.forEach { header ->

                    TableHeaderCell(
                        text = header.name,
                        cellBackgroundColor = Color.Gray,
                        weight = header.weight,
                        style = MaterialTheme.typography.titleSmall,
                        width = 100.dp,
                        sortOrder = header.sortOrder,
                        onSort = { order ->

                        },
                        sortDirection = header.sortDirection
                    )
                }

            }
        }
        // Here are all the lines of your table.
        items(data) { rows ->

            Row(Modifier.fillMaxWidth()) {
                rows.forEachIndexed { index, dataItem ->
                    TableCell(
                        text = dataItem,
                        cellBackgroundColor = Color.White,
                        weight = metaData[index].weight,
                        width = 100.dp
                    )
                }
            }
        }
    }
}