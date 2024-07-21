package com.ksstats.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.cancel
import com.ksstats.ksstats.generated.resources.ok
import com.ksstats.shared.fromMilliseconds
import com.ksstats.shared.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toJavaLocalDate
import org.jetbrains.compose.resources.stringResource
import java.time.LocalTime
import java.time.ZoneOffset


@Composable
fun PopupDatePicker(
    label: String,
    date: LocalDate = LocalDate.now(),
    modifier: Modifier = Modifier.height(56.dp),
    buttonConfirmationText: String = stringResource(Res.string.ok),
    buttonCancelText: String = stringResource(Res.string.cancel),
    onConfirm: (LocalDate?) -> Unit,
) {

    var showDialog by remember { mutableStateOf(false) }

    val today = LocalDate.now().year


    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = date.toJavaLocalDate().toEpochSecond(LocalTime.now(), ZoneOffset.UTC) * 1000,
            yearRange = 1772..today
        )

    datePickerState.selectedDateMillis = date.toJavaLocalDate().toEpochSecond(LocalTime.now(), ZoneOffset.UTC) * 1000


    var displayLabel by remember { mutableStateOf(label) }
    displayLabel = label

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false

                        val ldt = datePickerState.selectedDateMillis?.fromMilliseconds()

                        val formatPattern = "dd/MM/yyyy"

                        @OptIn(FormatStringsInDatetimeFormats::class)
                        val dateTimeFormat = LocalDate.Format {
                            byUnicodePattern(formatPattern)
                        }
                        displayLabel = ldt?.format(dateTimeFormat)!!
                        onConfirm(datePickerState.selectedDateMillis?.fromMilliseconds())
                    }

                ) {
                    Text(text = buttonConfirmationText)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text(text = buttonCancelText)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = true,

            )
        }
    }

    Box {
        OutlinedTextField(
            value = displayLabel,
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            onValueChange = {},
            enabled = false,
            singleLine = true,
            trailingIcon = {
                Icon(Icons.Filled.CalendarMonth, "")
            }
        )
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .then(modifier),
            onClick = { showDialog = true },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),

        ) {

        }
    }
}