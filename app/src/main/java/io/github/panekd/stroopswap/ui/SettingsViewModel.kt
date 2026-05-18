package io.github.panekd.stroopswap.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.github.panekd.stroopswap.data.Settings
import io.github.panekd.stroopswap.data.SettingsManager
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsManager = SettingsManager(application)

    val settings: LiveData<Settings> = settingsManager.settingsFlow.asLiveData()

    fun saveSettings(newSettings: Settings) {
        viewModelScope.launch {
            settingsManager.saveSettings(newSettings)
        }
    }
}