package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AimPresets
import com.example.data.PresetProfile
import com.example.data.SensitivitySettings
import com.example.data.SensitivityType
import com.example.ui.SensitivityViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun SensitivityScreen(
    viewModel: SensitivityViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val customProfiles by viewModel.customProfiles.collectAsState()
    val activePresetId by viewModel.activePresetId.collectAsState()

    var showSaveDialog by remember { mutableStateOf(false) }
    var newProfileName by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("sensitivity_screen_scroll"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Hero Banner & Header
        item {
            HeaderSection(
                onSave = { viewModel.saveSettings() },
                onCopy = { viewModel.copySettings(context) },
                onShare = { viewModel.shareSettings(context) },
                onReset = { viewModel.resetToDefaults() }
            )
        }

        // Section Title: Headshot Sensitivity Sliders
        item {
            SectionHeader(
                icon = "🎯",
                title = "HEADSHOT SENSITIVITY",
                subtitle = "Adjust values from 0 to 200 for micro-aim precision"
            )
        }

        // 6 Core Sliders
        item {
            SensitivitySliderItem(
                title = SensitivityType.GENERAL.label,
                hint = SensitivityType.GENERAL.hint,
                value = settings.general,
                onValueChange = { viewModel.updateValue(SensitivityType.GENERAL, it) },
                testTagPrefix = "slider_general"
            )
        }

        item {
            SensitivitySliderItem(
                title = SensitivityType.RED_DOT.label,
                hint = SensitivityType.RED_DOT.hint,
                value = settings.redDot,
                onValueChange = { viewModel.updateValue(SensitivityType.RED_DOT, it) },
                testTagPrefix = "slider_red_dot"
            )
        }

        item {
            SensitivitySliderItem(
                title = SensitivityType.SCOPE_2X.label,
                hint = SensitivityType.SCOPE_2X.hint,
                value = settings.scope2x,
                onValueChange = { viewModel.updateValue(SensitivityType.SCOPE_2X, it) },
                testTagPrefix = "slider_2x_scope"
            )
        }

        item {
            SensitivitySliderItem(
                title = SensitivityType.SCOPE_4X.label,
                hint = SensitivityType.SCOPE_4X.hint,
                value = settings.scope4x,
                onValueChange = { viewModel.updateValue(SensitivityType.SCOPE_4X, it) },
                testTagPrefix = "slider_4x_scope"
            )
        }

        item {
            SensitivitySliderItem(
                title = SensitivityType.SNIPER.label,
                hint = SensitivityType.SNIPER.hint,
                value = settings.sniper,
                onValueChange = { viewModel.updateValue(SensitivityType.SNIPER, it) },
                testTagPrefix = "slider_sniper"
            )
        }

        item {
            SensitivitySliderItem(
                title = SensitivityType.FREE_LOOK.label,
                hint = SensitivityType.FREE_LOOK.hint,
                value = settings.freeLook,
                onValueChange = { viewModel.updateValue(SensitivityType.FREE_LOOK, it) },
                testTagPrefix = "slider_free_look"
            )
        }

        // Section Title: Aim Presets
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeader(
                    icon = "⚡",
                    title = "AIM PRESETS",
                    subtitle = "One-tap load battle-tested sensitivity configs"
                )
                TextButton(
                    onClick = { showSaveDialog = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = NeonCyan)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Save Current", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Built-in presets
        items(AimPresets.BUILT_IN_PRESETS) { preset ->
            val summary = "Gen: ${preset.settings.general} • Red: ${preset.settings.redDot} • 2x: ${preset.settings.scope2x} • 4x: ${preset.settings.scope4x} • Snip: ${preset.settings.sniper}"
            PresetItemCard(
                title = preset.title,
                subtitle = preset.subtitle,
                badge = preset.badge,
                valuesSummary = summary,
                isActive = activePresetId == preset.id,
                onApply = { viewModel.applyPreset(preset) },
                testTag = "preset_${preset.id}"
            )
        }

        // Custom saved profiles if any
        if (customProfiles.isNotEmpty()) {
            item {
                Text(
                    text = "YOUR SAVED PROFILES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = NeonPurple,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(customProfiles) { profile ->
                CustomProfileCard(
                    profile = profile,
                    isActive = activePresetId == profile.id,
                    onApply = { viewModel.applyPreset(profile) },
                    onDelete = { viewModel.deleteCustomPreset(profile.id) }
                )
            }
        }

        // Section Title: Settings & Actions
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                icon = "💾",
                title = "SETTINGS & EXPORT",
                subtitle = "Save directly to device or copy to paste anywhere"
            )
        }

        // Main Action Buttons
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                CyberActionButton(
                    text = "SAVE SETTINGS",
                    icon = Icons.Default.Save,
                    gradientColors = listOf(NeonCyan, NeonPurple),
                    onClick = { viewModel.saveSettings() },
                    testTag = "btn_save_settings"
                )

                CyberActionButton(
                    text = "COPY SETTINGS",
                    icon = Icons.Default.ContentCopy,
                    gradientColors = listOf(NeonPurple, Color(0xFF4A148C)),
                    onClick = { viewModel.copySettings(context) },
                    testTag = "btn_copy_settings"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CyberSecondaryButton(
                        text = "Share Squad",
                        icon = Icons.Default.Share,
                        onClick = { viewModel.shareSettings(context) },
                        modifier = Modifier.weight(1f),
                        testTag = "btn_share"
                    )
                    CyberSecondaryButton(
                        text = "Reset Default",
                        icon = Icons.Default.Refresh,
                        onClick = { viewModel.resetToDefaults() },
                        modifier = Modifier.weight(1f),
                        testTag = "btn_reset"
                    )
                }
            }
        }

        // Legal & Safety Disclaimer Card
        item {
            DisclaimerCard()
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = {
                Text(
                    text = "Save Custom Preset",
                    style = MaterialTheme.typography.titleMedium.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter a name for your current sensitivity profile:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newProfileName,
                        onValueChange = { newProfileName = it },
                        placeholder = { Text("e.g. Ranked Rush MP40", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_preset_name")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newProfileName.isNotBlank()) {
                            viewModel.saveCustomPreset(newProfileName.trim())
                            newProfileName = ""
                            showSaveDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
                ) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CyberSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun HeaderSection(
    onSave: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cyber Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(NeonCyan.copy(alpha = 0.2f), NeonPurple.copy(alpha = 0.2f))
                    )
                )
                .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(NeonGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "NO ROOT • PRO AIM SYSTEM",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        fontSize = 11.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Main Title
        Text(
            text = "🎧 SENSI APP",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = NeonCyan,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                fontSize = 30.sp
            ),
            textAlign = TextAlign.Center
        )

        Text(
            text = "BATTLE ROYALE AIM & SENSITIVITY COMPANION",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp,
                fontSize = 11.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Quick action pill row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickPillButton(icon = Icons.Default.Save, label = "Save", onClick = onSave)
            Spacer(modifier = Modifier.width(8.dp))
            QuickPillButton(icon = Icons.Default.ContentCopy, label = "Copy", onClick = onCopy)
            Spacer(modifier = Modifier.width(8.dp))
            QuickPillButton(icon = Icons.Default.Share, label = "Share", onClick = onShare)
            Spacer(modifier = Modifier.width(8.dp))
            QuickPillButton(icon = Icons.Default.Refresh, label = "Reset", onClick = onReset)
        }
    }
}

@Composable
private fun QuickPillButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = CyberSurfaceVariant.copy(alpha = 0.8f),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
        modifier = Modifier.height(32.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = NeonCyan, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SectionHeader(
    icon: String,
    title: String,
    subtitle: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    fontSize = 17.sp
                )
            )
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextMuted,
                fontSize = 12.sp
            ),
            modifier = Modifier.padding(start = 26.dp)
        )
    }
}

@Composable
private fun CustomProfileCard(
    profile: PresetProfile,
    isActive: Boolean,
    onApply: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                androidx.compose.foundation.BorderStroke(
                    if (isActive) 1.5.dp else 1.dp,
                    if (isActive) NeonGreen else CyberBorder
                ),
                RoundedCornerShape(14.dp)
            ),
        color = CyberSurface,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Gen: ${profile.settings.general} • Red: ${profile.settings.redDot} • 2x: ${profile.settings.scope2x} • 4x: ${profile.settings.scope4x}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = NeonCyan.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Button(
                    onClick = onApply,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isActive) NeonGreen else NeonCyan,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(text = if (isActive) "ACTIVE" else "APPLY", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DisclaimerCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                androidx.compose.foundation.BorderStroke(1.dp, CyberBorder.copy(alpha = 0.6f)),
                RoundedCornerShape(14.dp)
            ),
        color = CyberSurface.copy(alpha = 0.6f),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SAFE & NON-INVASIVE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "NO ROOT REQUIRED\nSettings are recommendations to manually enter in your game's sensitivity menu. This app does not modify internal game files, inject memory, or violate game policies.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    fontSize = 12.sp
                )
            )
        }
    }
}
