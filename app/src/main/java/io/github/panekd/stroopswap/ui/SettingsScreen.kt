package io.github.panekd.stroopswap.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.panekd.stroopswap.notifications.DailyAlarmManager
import java.util.Locale
import io.github.panekd.stroopswap.data.Settings

@Composable
fun SettingsScreen(toHome: () -> Unit) {
    val model: SettingsViewModel = viewModel()
    val settings by model.settings.observeAsState()

    if (settings == null) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            "Settings",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        DoubleTapModeSetting(model, settings!!)
        RemindersSetting(model, settings!!)
    }
}

@Composable
fun DoubleTapModeSetting(model: SettingsViewModel, settings: Settings) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Heading("Double tap mode")
            Switch(
                checked = settings.doubleTap,
                onCheckedChange = {
                    model.saveSettings(settings.copy(doubleTap = it))
                }
            )
        }
        Description("Require a double tap to confirm answer to prevent accidental inputs.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersSetting(model: SettingsViewModel, settings: Settings) {
    val context = LocalContext.current

    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState(
        initialHour = 17,
        initialMinute = 0,
        is24Hour = true
    )

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Heading("Reminders")
            Switch(
                checked = settings.reminders,
                onCheckedChange = {
                    model.saveSettings(settings.copy(reminders = it))
                }
            )
        }
        Description("Get daily training reminders")
        if (settings.reminders) {
            Row {
                Description("At:")
                Button(onClick = {showTimePicker = true}) {
                    Text(String.format(
                        Locale.UK,
                        "%02d:%02d",
                        settings.remindersHour,
                        settings.remindersMinute
                    ))
                }
            }
        }
        if (showTimePicker) {
            TimePickerDialog(
                onCancel = { showTimePicker = false },
                onConfirm = {
                    model.saveSettings(settings.copy(
                        remindersHour = state.hour,
                        remindersMinute = state.minute
                    ))
                    DailyAlarmManager(context).set(settings.remindersHour, settings.remindersMinute)
                    showTimePicker = false
                }
            ) {
                TimeInput(state)
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        dismissButton = {
            TextButton(onClick = { onCancel() }) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@Composable
fun Heading(text: String) {
    Text(
        text = text,
        fontSize = 20.sp
    )
}

@Composable
fun Description(text: String) {
    Text(
        text = text,
        fontSize = 15.sp,
        color = MaterialTheme.colorScheme.secondary
    )
}
