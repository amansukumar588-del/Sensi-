package com.example.data

data class SensitivitySettings(
    val general: Int = 190,
    val redDot: Int = 180,
    val scope2x: Int = 170,
    val scope4x: Int = 160,
    val sniper: Int = 100,
    val freeLook: Int = 150
) {
    fun toFormattedString(): String {
        return buildString {
            appendLine("🎧 SENSI APP • AIM CONFIG")
            appendLine("━━━━━━━━━━━━━━━━━━━")
            appendLine("🎯 General: $general")
            appendLine("🔴 Red Dot: $redDot")
            appendLine("🔍 2x Scope: $scope2x")
            appendLine("🔭 4x Scope: $scope4x")
            appendLine("⚡ Sniper Scope: $sniper")
            appendLine("👁️ Free Look: $freeLook")
            appendLine("━━━━━━━━━━━━━━━━━━━")
            appendLine("NO ROOT • Safe in-game aim guide")
        }
    }

    companion object {
        val Default = SensitivitySettings()
    }
}

data class PresetProfile(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val settings: SensitivitySettings,
    val isCustom: Boolean = false
)

object AimPresets {
    val BUILT_IN_PRESETS = listOf(
        PresetProfile(
            id = "drag_headshot",
            title = "DRAG HEADSHOT",
            subtitle = "High speed flick for vertical recoil drag",
            badge = "POPULAR",
            settings = SensitivitySettings(
                general = 195,
                redDot = 185,
                scope2x = 175,
                scope4x = 165,
                sniper = 110,
                freeLook = 150
            )
        ),
        PresetProfile(
            id = "safe_balanced",
            title = "SAFE BALANCED AIM",
            subtitle = "Smooth crosshair control without overshooting",
            badge = "RECOMMENDED",
            settings = SensitivitySettings(
                general = 180,
                redDot = 170,
                scope2x = 155,
                scope4x = 145,
                sniper = 90,
                freeLook = 130
            )
        ),
        PresetProfile(
            id = "smooth_aim",
            title = "SMOOTH AIM",
            subtitle = "Ultra steady tracking for close & mid range",
            badge = "STABLE",
            settings = SensitivitySettings(
                general = 165,
                redDot = 150,
                scope2x = 140,
                scope4x = 125,
                sniper = 80,
                freeLook = 120
            )
        ),
        PresetProfile(
            id = "one_tap_sniper",
            title = "ONE-TAP SNIPER PRO",
            subtitle = "Maxed reaction for quickscope flicks & sniper switch",
            badge = "EXPERT",
            settings = SensitivitySettings(
                general = 200,
                redDot = 190,
                scope2x = 180,
                scope4x = 170,
                sniper = 135,
                freeLook = 160
            )
        ),
        PresetProfile(
            id = "low_ram",
            title = "LOW RAM / 60HZ",
            subtitle = "Tuned for smooth touch response on budget devices",
            badge = "LIGHT",
            settings = SensitivitySettings(
                general = 175,
                redDot = 165,
                scope2x = 150,
                scope4x = 140,
                sniper = 85,
                freeLook = 120
            )
        )
    )
}

enum class SensitivityType(val label: String, val hint: String) {
    GENERAL("General", "Camera rotation speed & drag response"),
    RED_DOT("Red Dot", "Holo and red-dot sight aim tracking"),
    SCOPE_2X("2x Scope", "Medium range zoom sensitivity"),
    SCOPE_4X("4x Scope", "Long range rifles and assault scopes"),
    SNIPER("Sniper Scope", "High magnification precision crosshair"),
    FREE_LOOK("Free Look", "Eye button surrounding awareness")
}
