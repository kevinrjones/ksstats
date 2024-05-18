package com.ksstats.core.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


// see:https://stackoverflow.com/questions/68143308/how-do-i-create-a-table-in-jetpack-compose

@Composable
fun CellText(
    text: String,
    cellBackgroundColor: Color = Color.White,
    style: TextStyle = LocalTextStyle.current,
    width: Dp,
    color: Color = Color.Black,
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = style,
        color = color,
        modifier = Modifier
            .border(1.dp, Color.Black)
            .width(width)
            .background(cellBackgroundColor)
            .padding(8.dp)
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RowScope.TableCell(
    text: String,
    showTooltip: Boolean = false,
    color: Color = Color.Black,
    cellBackgroundColor: Color = Color.White,
    weight: Float = 0f,
    style: TextStyle = LocalTextStyle.current,
    width: Dp
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
            CellText(text = text, color = color, style = style, cellBackgroundColor = cellBackgroundColor, width = width)
        }
    } else {
        CellText(text = text, color = color, style = style, cellBackgroundColor = cellBackgroundColor, width = width)
    }

}

data class ColumnMetaData(val name: String, val width: Dp, val weight: Float = 0f)

@Composable
fun Table(
    data: List<List<String>>,
    metaData: List<ColumnMetaData>
) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        // Here is the header
        stickyHeader {
            Row(Modifier.background(Color.Gray)) {
                metaData.forEachIndexed { index, header ->

                    TableCell(
                        text = header.name,
                        color = Color.White,
                        cellBackgroundColor = Color.Gray,
                        weight = header.weight,
                        style = MaterialTheme.typography.titleSmall,
                        width = 100.dp
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

@Composable
fun TableEx(
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    columnCount: Int,
    rowCount: Int,
    metaData: List<ColumnMetaData>,
    beforeRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    afterRow: (@Composable (rowIndex: Int) -> Unit)? = null,
    cellContent: @Composable (columnIndex: Int, rowIndex: Int) -> String?
) {
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }

    Box(modifier = modifier.then(Modifier.horizontalScroll(horizontalScrollState))) {
        LazyColumn(state = verticalLazyListState) {
            stickyHeader {
                Row(Modifier.background(Color.Gray)) {
                    metaData.forEachIndexed { index, header ->

                        TableCell(
                            text = header.name,
                            color = Color.White,
                            cellBackgroundColor = Color.Gray,
                            width = header.width,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }

            items(rowCount) { rowIndex ->
                Column {
                    beforeRow?.invoke(rowIndex)

                    Row(modifier = rowModifier) {

                        (0 until columnCount).forEach { columnIndex ->
                            Box(modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)

                                val existingWidth = columnWidths[columnIndex] ?: 0
                                val maxWidth = maxOf(existingWidth, placeable.width)

                                if (maxWidth > existingWidth) {
                                    columnWidths[columnIndex] = maxWidth
                                }

                                layout(width = maxWidth, height = placeable.height) {
                                    placeable.placeRelative(0, 0)
                                }
                            }) {
                                cellContent(columnIndex, rowIndex)?.let { content ->
                                    if (rowIndex % 2 == 0)
                                        this@Row.TableCell(
                                            text = content,
                                            showTooltip = content.length > 10,
                                            width = metaData[columnIndex].width
                                        )
                                    else
                                        this@Row.TableCell(
                                            showTooltip = content.length > 10,
                                            text = content,
                                            cellBackgroundColor = Color.LightGray,
                                            width = metaData[columnIndex].width
                                        )
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