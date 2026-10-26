package com.example.ui.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberCard
import com.example.ui.theme.*

@Composable
fun DpiGuideScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = (config.screenWidthDp * density.density).toInt()
    val screenHeightPx = (config.screenHeightDp * density.density).toInt()
    val currentDpi = config.densityDpi

    // Optimal DPI recommendations based on screen class
    val recommendedDpi = when {
        config.screenWidthDp <= 360 -> "411 DPI"
        config.screenWidthDp <= 400 -> "480 DPI"
        else -> "520 DPI"
    }

    val recommendedFireButton = when {
        config.screenWidthDp <= 360 -> "48%"
        config.screenWidthDp <= 400 -> "52%"
        else -> "55%"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dpi_guide_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📐 DPI & DEVICE SPECS",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    )
                )
                Text(
                    text = "Hardware-tailored recommendations for zero drag friction",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                )
            }
        }

        // Detected Hardware Card
        item {
            CyberCard(glow = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Smartphone,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "DETECTED HARDWARE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                HardwareMetricRow(label = "Device Model", value = "${Build.MANUFACTURER.uppercase()} ${Build.MODEL}")
                HardwareMetricRow(label = "Android Version", value = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
                HardwareMetricRow(label = "Display Resolution", value = "$screenWidthPx × $screenHeightPx px")
                HardwareMetricRow(label = "Default Screen Density", value = "$currentDpi DPI")
                HardwareMetricRow(label = "Viewport Width", value = "${config.screenWidthDp} dp")
            }
        }

        // Recommended Settings Grid
        item {
            Text(
                text = "⚡ RECOMMENDED AIM TUNING",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = NeonPurple,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RecommendationBadgeCard(
                    title = "Optimal DPI",
                    value = recommendedDpi,
                    hint = "Developer Options Smallest Width",
                    icon = Icons.Default.Tune,
                    color = NeonCyan,
                    modifier = Modifier.weight(1f)
                )

                RecommendationBadgeCard(
                    title = "Fire Button",
                    value = recommendedFireButton,
                    hint = "Optimal drag headshot area",
                    icon = Icons.Default.RadioButtonChecked,
                    color = NeonOrange,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RecommendationBadgeCard(
                    title = "Pointer Speed",
                    value = "100% MAX",
                    hint = "System Language & Input setting",
                    icon = Icons.Default.Speed,
                    color = NeonGreen,
                    modifier = Modifier.weight(1f)
                )

                RecommendationBadgeCard(
                    title = "Graphic & FPS",
                    value = "HIGH FPS",
                    hint = "Smooth graphics + High FPS toggle",
                    icon = Icons.Default.VideogameAsset,
                    color = NeonPurple,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Fire Button Placement Guide
        item {
            CyberCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = null,
                        tint = NeonOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FIRE BUTTON PLACEMENT TIPS",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                PlacementTip(
                    number = "1",
                    title = "Leave Vertical Swipe Room",
                    description = "Position the shoot button slightly lower in your HUD (around 15-20% from screen bottom). This gives your thumb ample room to flick upwards for drag headshots."
                )

                Spacer(modifier = Modifier.height(8.dp))

                PlacementTip(
                    number = "2",
                    title = "Button Size Calibration",
                    description = "A fire button that is too large (above 65%) causes accidental clicks and limits swipe distance. 48% - 55% provides ideal target acquisition without sacrificing screen space."
                )
            }
        }

        // How to adjust DPI Safely
        item {
            CyberCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HOW TO APPLY DPI IN ANDROID",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "1. Open Android Settings > About Phone\n" +
                            "2. Tap 'Build Number' 7 times to unlock Developer Options\n" +
                            "3. Go to System > Developer Options > Smallest Width\n" +
                            "4. Note your current default value (e.g. $currentDpi) so you can revert anytime\n" +
                            "5. Set the new value to your recommendation ($recommendedDpi)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 20.sp,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun HardwareMetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 12.sp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
private fun RecommendationBadgeCard(
    title: String,
    value: String,
    hint: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f)), RoundedCornerShape(14.dp)),
        color = CyberSurface,
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = color,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 10.sp),
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun PlacementTip(number: String, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(NeonPurple.copy(alpha = 0.3f))
                .border(1.dp, NeonPurple, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
            )
        }
    }
}
