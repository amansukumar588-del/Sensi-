package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AimPresets
import com.example.data.SensitivityRepository
import com.example.data.SensitivitySettings
import com.example.data.SensitivityType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Sensi App", appName)
    }

    @Test
    fun `sensitivity repository saves and loads values correctly`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = SensitivityRepository(context)

        val custom = SensitivitySettings(
            general = 195,
            redDot = 185,
            scope2x = 175,
            scope4x = 165,
            sniper = 110,
            freeLook = 150
        )
        repo.saveSettings(custom)

        val loaded = repo.loadSettings()
        assertEquals(195, loaded.general)
        assertEquals(185, loaded.redDot)
        assertEquals(175, loaded.scope2x)
        assertEquals(165, loaded.scope4x)
        assertEquals(110, loaded.sniper)
        assertEquals(150, loaded.freeLook)
    }

    @Test
    fun `built in presets contain drag headshot config`() {
        val dragPreset = AimPresets.BUILT_IN_PRESETS.find { it.id == "drag_headshot" }
        assertNotNull(dragPreset)
        assertEquals(195, dragPreset!!.settings.general)
        assertEquals(185, dragPreset.settings.redDot)
        assertEquals(175, dragPreset.settings.scope2x)
    }

    @Test
    fun `update individual sensitivity value`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = SensitivityRepository(context)
        repo.updateValue(SensitivityType.GENERAL, 200)
        assertEquals(200, repo.settings.value.general)
    }
}
