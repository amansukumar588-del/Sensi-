package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.SensitivityRepository
import com.example.ui.NavigationTab
import com.example.ui.SensitivityViewModel
import com.example.ui.screens.AimWarmupScreen
import com.example.ui.screens.DpiGuideScreen
import com.example.ui.screens.SensitivityScreen
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = SensitivityRepository(applicationContext)

        setContent {
            SensiAppTheme {
                val viewModel = remember { SensitivityViewModel(repository) }
                SensiAppMainScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SensiAppMainScreen(
    viewModel: SensitivityViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    // Handle back button when not on main tab
    if (currentTab != NavigationTab.SENSITIVITY) {
        BackHandler {
            viewModel.setTab(NavigationTab.SENSITIVITY)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground),
        containerColor = CyberBackground,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            SensiBottomNavBar(
                currentTab = currentTab,
                onTabSelect = { viewModel.setTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                NavigationTab.SENSITIVITY -> {
                    SensitivityScreen(viewModel = viewModel)
                }
                NavigationTab.DPI_GUIDE -> {
                    DpiGuideScreen()
                }
                NavigationTab.AIM_WARMUP -> {
                    AimWarmupScreen()
                }
            }

            // Animated HUD Toast / Banner
            AnimatedVisibility(
                visible = userMessage != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            ) {
                userMessage?.let { msg ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.5.dp, NeonCyan, RoundedCornerShape(24.dp)),
                        color = CyberSurfaceVariant.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(24.dp),
                        tonalElevation = 6.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(NeonGreen)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SensiBottomNavBar(
    currentTab: NavigationTab,
    onTabSelect: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .border(
                androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        containerColor = CyberSurface,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars
    ) {
        val tabs = listOf(
            Triple(NavigationTab.SENSITIVITY, "Sensitivity", Icons.Default.Tune),
            Triple(NavigationTab.DPI_GUIDE, "DPI & Device", Icons.Default.GpsFixed),
            Triple(NavigationTab.AIM_WARMUP, "Aim Warmup", Icons.Default.Speed)
        )

        tabs.forEach { (tab, title, icon) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelect(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = TextSecondary,
                    selectedTextColor = NeonCyan,
                    unselectedTextColor = TextMuted,
                    indicatorColor = NeonCyan
                ),
                modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
            )
        }
    }
}
