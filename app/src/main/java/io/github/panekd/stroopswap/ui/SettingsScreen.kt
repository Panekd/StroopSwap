package io.github.panekd.stroopswap.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale
import io.github.panekd.stroopswap.data.Settings
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import io.github.panekd.stroopswap.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(toHome: () -> Unit) {
    val model: SettingsViewModel = viewModel()
    val settings by model.settings.observeAsState()

    if (settings == null) return

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(
                        onClick = toHome
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.to_menu))
                    }
                }
            )
        }
    ) {innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            DoubleTapModeSetting(model, settings!!)
            RemindersSetting(model, settings!!)
        }
    }
}

@Composable
fun DoubleTapModeSetting(model: SettingsViewModel, settings: Settings) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Heading(stringResource(R.string.double_tap_mode))
            Switch(
                checked = settings.doubleTap,
                onCheckedChange = {
                    model.saveSettings(settings.copy(doubleTap = it))
                }
            )
        }
        Description(stringResource(R.string.double_tap_mode_desc))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersSetting(model: SettingsViewModel, settings: Settings) {
    val context = LocalContext.current

    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState(
        initialHour = settings.remindersHour,
        initialMinute = settings.remindersMinute,
        is24Hour = true
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        model.saveSettings(settings.copy(reminders = granted))
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Heading(stringResource(R.string.reminders))
            Switch(
                checked = settings.reminders,
                onCheckedChange = {
                    if (!it) model.saveSettings(settings.copy(reminders = false))
                    else {
                        if (
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            model.saveSettings(settings.copy(reminders = true))
                        }
                    }
                }
            )
        }
        Description(stringResource(R.string.reminders_desc))
        if (settings.reminders) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Description(stringResource(R.string.reminders_at))
                Button(onClick = {showTimePicker = true}) {
                    Text(String.format(
                        Locale.UK,
                        stringResource(R.string.time_format),
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
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(stringResource(R.string.ok))
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
