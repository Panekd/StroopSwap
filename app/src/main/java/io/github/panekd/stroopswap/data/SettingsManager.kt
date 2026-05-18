package io.github.panekd.stroopswap.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "Settings")

class SettingsManager(
    private val context: Context
) {
    companion object {
        private val DOUBLETAP_KEY = booleanPreferencesKey("double_tap")
        private val REMINDERS_KEY = booleanPreferencesKey("reminders")
        private val REMINDERS_HOUR_KEY = intPreferencesKey("reminders_hour")
        private val REMINDERS_MINUTE_KEY = intPreferencesKey("reminders_minute")
    }

    val settingsFlow: Flow<Settings> = context.dataStore.data
        .map { settings ->
            val doubleTap = settings[DOUBLETAP_KEY]?: false
            val reminders = settings[REMINDERS_KEY]?: false
            val remindersHour = settings[REMINDERS_HOUR_KEY]?: 17
            val remindersMinute = settings[REMINDERS_MINUTE_KEY]?: 0
            Settings(doubleTap, reminders, remindersHour, remindersMinute)
        }

    suspend fun saveSettings(settings: Settings) {
        context.dataStore.edit { data ->
            data[DOUBLETAP_KEY] = settings.doubleTap
            data[REMINDERS_KEY] = settings.reminders
            data[REMINDERS_HOUR_KEY] = settings.remindersHour
            data[REMINDERS_MINUTE_KEY] = settings.remindersMinute
        }
    }
}