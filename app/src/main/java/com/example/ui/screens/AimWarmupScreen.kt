package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AimWarmupScreen(modifier: Modifier = Modifier) {
    val haptic = LocalHapticFeedback.current

    var isPlaying by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var hits by remember { mutableStateOf(0) }
    var misses by remember { mutableStateOf(0) }
    var lastReactionTimeMs by remember { mutableStateOf<Long?>(null) }
    var bestReactionTimeMs by remember { mutableStateOf<Long?>(null) }
    var targetSpawnTime by remember { mutableStateOf(0L) }

    // Target position normalized 0.15f to 0.85f inside arena
    var targetX by remember { mutableStateOf(0.5f) }
    var targetY by remember { mutableStateOf(0.5f) }
    var targetActive by remember { mutableStateOf(false) }

    fun spawnNewTarget() {
        targetX = Random.nextFloat() * 0.7f + 0.15f
        targetY = Random.nextFloat() * 0.7f + 0.15f
        targetSpawnTime = System.currentTimeMillis()
        targetActive = true
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            score = 0
            hits = 0
            misses = 0
            lastReactionTimeMs = null
            spawnNewTarget()
        } else {
            targetActive = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("aim_warmup_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "⚡ AIM WARM-UP SANDBOX",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = NeonCyan,
                fontWeight = FontWeight.Black,
                fontSize = 22.sp
            )
        )
        Text(
            text = "Calibrate your flick speed and warm up drag reflex",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 12.sp
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // HUD Stats Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScorePill(
                label = "SCORE",
                value = score.toString(),
                color = NeonCyan,
                modifier = Modifier.weight(1f)
            )
            ScorePill(
                label = "LAST MS",
                value = lastReactionTimeMs?.let { "${it}ms" } ?: "--",
                color = NeonOrange,
                modifier = Modifier.weight(1f)
            )
            ScorePill(
                label = "BEST MS",
                value = bestReactionTimeMs?.let { "${it}ms" } ?: "--",
                color = NeonGreen,
                modifier = Modifier.weight(1f)
            )
            ScorePill(
                label = "ACCURACY",
                value = if (hits + misses > 0) "${(hits * 100) / (hits + misses)}%" else "100%",
                color = NeonPurple,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive Target Arena
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(CyberSurface)
                .border(BorderStroke(1.5.dp, if (isPlaying) NeonCyan else CyberBorder), RoundedCornerShape(16.dp))
                .pointerInput(isPlaying, targetActive) {
                    detectTapGestures { offset ->
                        if (!isPlaying || !targetActive) return@detectTapGestures

                        val targetPxX = targetX * size.width
                        val targetPxY = targetY * size.height
                        val dist = kotlin.math.hypot(offset.x - targetPxX, offset.y - targetPxY)
                        val targetRadius = 38.dp.toPx()

                        if (dist <= targetRadius) {
                            // Hit!
                            val reaction = System.currentTimeMillis() - targetSpawnTime
                            lastReactionTimeMs = reaction
                            if (bestReactionTimeMs == null || reaction < bestReactionTimeMs!!) {
                                bestReactionTimeMs = reaction
                            }
                            hits++
                            score += (250 - reaction.coerceIn(50, 250)).toInt().coerceAtLeast(10)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            spawnNewTarget()
                        } else {
                            // Miss
                            misses++
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        }
                    }
                }
                .testTag("target_arena")
        ) {
            val arenaWidth = maxWidth
            val arenaHeight = maxHeight

            // Cyber reticle grid background
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                // Grid lines
                drawLine(
                    color = CyberBorder.copy(alpha = 0.5f),
                    start = Offset(cx, 0f),
                    end = Offset(cx, size.height),
                    strokeWidth = 1f
                )
                drawLine(
                    color = CyberBorder.copy(alpha = 0.5f),
                    start = Offset(0f, cy),
                    end = Offset(size.width, cy),
                    strokeWidth = 1f
                )

                // Concentric circles
                drawCircle(
                    color = CyberBorder.copy(alpha = 0.35f),
                    radius = size.width * 0.25f,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1f)
                )
                drawCircle(
                    color = CyberBorder.copy(alpha = 0.2f),
                    radius = size.width * 0.45f,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1f)
                )
            }

            if (isPlaying && targetActive) {
                // Render Target Disc
                Box(
                    modifier = Modifier
                        .offset(
                            x = (arenaWidth * targetX) - 34.dp,
                            y = (arenaHeight * targetY) - 34.dp
                        )
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(NeonPurple.copy(alpha = 0.25f))
                        .border(BorderStroke(2.dp, NeonCyan), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(NeonOrange)
                            .border(BorderStroke(1.5.dp, Color.White), CircleShape)
                    )
                }
            } else if (!isPlaying) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🎯 READY TO FLICK?",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap START to spawn targets. Tap the red center bullseye as fast as you can to measure reaction latency and muscle memory.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Start / Stop Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { isPlaying = !isPlaying },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPlaying) NeonOrange else NeonCyan,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_toggle_warmup")
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Refresh else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isPlaying) "STOP WARMUP" else "START AIM WARMUP",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }

            if (hits > 0 || misses > 0) {
                OutlinedButton(
                    onClick = {
                        isPlaying = false
                        score = 0
                        hits = 0
                        misses = 0
                        lastReactionTimeMs = null
                    },
                    border = BorderStroke(1.dp, CyberBorder),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("RESET", color = TextSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ScorePill(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, color.copy(alpha = 0.4f)), RoundedCornerShape(10.dp)),
        color = CyberSurfaceVariant,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = color,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
            )
        }
    }
}
