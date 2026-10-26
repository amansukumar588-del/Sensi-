package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class SensitivityRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("sensi", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<SensitivitySettings> = _settings.asStateFlow()

    private val _customProfiles = MutableStateFlow(loadCustomProfiles())
    val customProfiles: StateFlow<List<PresetProfile>> = _customProfiles.asStateFlow()

    fun loadSettings(): SensitivitySettings {
        return SensitivitySettings(
            general = prefs.getInt("General", 190),
            redDot = prefs.getInt("Red Dot", 180),
            scope2x = prefs.getInt("2x Scope", 170),
            scope4x = prefs.getInt("4x Scope", 160),
            sniper = prefs.getInt("Sniper Scope", 100),
            freeLook = prefs.getInt("Free Look", 150)
        )
    }

    fun saveSettings(newSettings: SensitivitySettings) {
        prefs.edit().apply {
            putInt("General", newSettings.general)
            putInt("Red Dot", newSettings.redDot)
            putInt("2x Scope", newSettings.scope2x)
            putInt("4x Scope", newSettings.scope4x)
            putInt("Sniper Scope", newSettings.sniper)
            putInt("Free Look", newSettings.freeLook)
            apply()
        }
        _settings.value = newSettings
    }

    fun updateValue(type: SensitivityType, value: Int) {
        val clamped = value.coerceIn(0, 200)
        val current = _settings.value
        val updated = when (type) {
            SensitivityType.GENERAL -> current.copy(general = clamped)
            SensitivityType.RED_DOT -> current.copy(redDot = clamped)
            SensitivityType.SCOPE_2X -> current.copy(scope2x = clamped)
            SensitivityType.SCOPE_4X -> current.copy(scope4x = clamped)
            SensitivityType.SNIPER -> current.copy(sniper = clamped)
            SensitivityType.FREE_LOOK -> current.copy(freeLook = clamped)
        }
        _settings.value = updated
    }

    fun applyPreset(preset: SensitivitySettings) {
        _settings.value = preset
    }

    fun resetToDefaults() {
        val def = SensitivitySettings.Default
        saveSettings(def)
    }

    private fun loadCustomProfiles(): List<PresetProfile> {
        val jsonString = prefs.getString("custom_profiles_json", null) ?: return emptyList()
        val list = mutableListOf<PresetProfile>()
        try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val sObj = obj.getJSONObject("settings")
                list.add(
                    PresetProfile(
                        id = obj.getString("id"),
                        title = obj.getString("title"),
                        subtitle = obj.optString("subtitle", "Custom sensitivity profile"),
                        badge = "CUSTOM",
                        settings = SensitivitySettings(
                            general = sObj.getInt("general"),
                            redDot = sObj.getInt("redDot"),
                            scope2x = sObj.getInt("scope2x"),
                            scope4x = sObj.getInt("scope4x"),
                            sniper = sObj.getInt("sniper"),
                            freeLook = sObj.getInt("freeLook")
                        ),
                        isCustom = true
                    )
                )
            }
        } catch (_: Exception) {
            // fallback
        }
        return list
    }

    fun saveCustomProfile(name: String, settings: SensitivitySettings) {
        val existing = _customProfiles.value.toMutableList()
        val id = "custom_" + System.currentTimeMillis()
        val profile = PresetProfile(
            id = id,
            title = name.ifBlank { "Custom Config" },
            subtitle = "Saved by user",
            badge = "CUSTOM",
            settings = settings,
            isCustom = true
        )
        existing.add(0, profile)
        persistProfiles(existing)
        _customProfiles.value = existing
    }

    fun deleteCustomProfile(id: String) {
        val existing = _customProfiles.value.filter { it.id != id }
        persistProfiles(existing)
        _customProfiles.value = existing
    }

    private fun persistProfiles(list: List<PresetProfile>) {
        val array = JSONArray()
        list.forEach { p ->
            val obj = JSONObject()
            obj.put("id", p.id)
            obj.put("title", p.title)
            obj.put("subtitle", p.subtitle)
            val s = JSONObject()
            s.put("general", p.settings.general)
            s.put("redDot", p.settings.redDot)
            s.put("scope2x", p.settings.scope2x)
            s.put("scope4x", p.settings.scope4x)
            s.put("sniper", p.settings.sniper)
            s.put("freeLook", p.settings.freeLook)
            obj.put("settings", s)
            array.put(obj)
        }
        prefs.edit().putString("custom_profiles_json", array.toString()).apply()
    }
}
