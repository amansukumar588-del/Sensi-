package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class NavigationTab(val label: String, val iconDesc: String) {
    SENSITIVITY("Sensi HUD", "Aim sliders & presets"),
    DPI_GUIDE("DPI & Screen", "Display & Fire button specs"),
    AIM_WARMUP("Aim Sandbox", "Flick & reaction warmup")
}

class SensitivityViewModel(private val repository: SensitivityRepository) : ViewModel() {

    val settings: StateFlow<SensitivitySettings> = repository.settings
    val customProfiles: StateFlow<List<PresetProfile>> = repository.customProfiles

    private val _currentTab = MutableStateFlow(NavigationTab.SENSITIVITY)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _activePresetId = MutableStateFlow<String?>("drag_headshot")
    val activePresetId: StateFlow<String?> = _activePresetId.asStateFlow()

    fun setTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun updateValue(type: SensitivityType, value: Int) {
        repository.updateValue(type, value)
        _activePresetId.value = null // clear preset highlight if manually edited
    }

    fun applyPreset(preset: PresetProfile) {
        repository.applyPreset(preset.settings)
        _activePresetId.value = preset.id
        showNotification("Applied: ${preset.title}")
    }

    fun saveSettings() {
        repository.saveSettings(settings.value)
        showNotification("✓ Settings saved successfully!")
    }

    fun copySettings(context: Context) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        if (cm != null) {
            val clip = ClipData.newPlainText("Sensitivity", settings.value.toFormattedString())
            cm.setPrimaryClip(clip)
            showNotification("✓ Copied sensitivity to clipboard!")
        }
    }

    fun shareSettings(context: Context) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, settings.value.toFormattedString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Sensi Config")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun resetToDefaults() {
        repository.resetToDefaults()
        _activePresetId.value = null
        showNotification("Reset to default sensitivities")
    }

    fun saveCustomPreset(name: String) {
        if (name.isBlank()) return
        repository.saveCustomProfile(name, settings.value)
        showNotification("Saved custom profile: $name")
    }

    fun deleteCustomPreset(id: String) {
        repository.deleteCustomProfile(id)
        showNotification("Custom profile removed")
    }

    private fun showNotification(msg: String) {
        viewModelScope.launch {
            _userMessage.value = msg
            delay(2500)
            if (_userMessage.value == msg) {
                _userMessage.value = null
            }
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}
