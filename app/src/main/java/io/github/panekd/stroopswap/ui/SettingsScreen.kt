package io.github.panekd.stroopswap.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(toHome: () -> Unit) {
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
        DoubleTapModeSetting()
        RemindersSetting()
    }
}

@Composable
fun DoubleTapModeSetting() {
    var checked by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Heading("Double tap mode")
            Switch(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
        Description("Require a double tap to confirm answer to prevent accidental inputs.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersSetting() {
    var checked by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
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
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
        Description("Get daily training reminders")
        if (checked) {
            Row {
                Description("At:")
                TimeInput(state = timePickerState)
            }
        }
    }
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
