package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.ApaarViewModel
import com.example.ui.components.AcademicSummaryModal
import com.example.ui.components.ApaarLogo
import com.example.ui.components.DashboardTourOverlay
import com.example.ui.components.SyntheticDataBanner
import com.example.ui.components.VerificationReportModal
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ApaarOneViewApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApaarOneViewApp(
    viewModel: ApaarViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val searchReport by viewModel.searchReport.collectAsState()
    val selectedStudent by viewModel.selectedStudent.collectAsState()
    val showVerificationDialog by viewModel.showVerificationDialog.collectAsState()
    val showPrintSummaryDialog by viewModel.showPrintSummaryDialog.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Handle back button for secondary screens
    BackHandler(enabled = currentScreen != AppScreen.DASHBOARD) {
        viewModel.navigateTo(AppScreen.DASHBOARD)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = LightSurface,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 20.dp)
                ) {
                    ApaarLogo()
                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = LightOutline)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "NAVIGATION CONSOLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )

                    // Navigation Destinations
                    val destinations = listOf(
                        AppScreen.DASHBOARD to Icons.Default.Dashboard,
                        AppScreen.GUIDED_TOUR to Icons.Default.OndemandVideo,
                        AppScreen.SEARCH_VERIFICATION to Icons.Default.VerifiedUser,
                        AppScreen.STUDENT_DIRECTORY to Icons.Default.FolderShared,
                        AppScreen.STUDENT_PROFILE to Icons.Default.School,
                        AppScreen.RECORD_UPLOAD to Icons.Default.CloudUpload,
                        AppScreen.DUPLICATE_REVIEW to Icons.Default.Warning,
                        AppScreen.ACTIVITY_LOGS to Icons.Default.History,
                        AppScreen.INSTITUTION_MANAGEMENT to Icons.Default.Apartment,
                        AppScreen.REPORTS to Icons.Default.Description,
                        AppScreen.SETTINGS to Icons.Default.Settings
                    )

                    destinations.forEach { (screen, icon) ->
                        val isSelected = currentScreen == screen
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = screen.title,
                                    tint = if (isSelected) CyanDark else Navy700
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (isSelected) CyanDark else Navy900
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                viewModel.navigateTo(screen)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = CyanSurface,
                                unselectedContainerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .testTag("nav_item_${screen.name.lowercase()}")
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Bottom Drawer Active Persona
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = Navy900
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Active Role: ${currentRole.label}",
                                color = CyanBright,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentRole.email,
                                color = Navy100,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ApaarLogo(compact = true)
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } },
                                modifier = Modifier.testTag("nav_drawer_button")
                            ) {
                                Icon(Icons.Default.Menu, contentDescription = "Open Navigation Menu", tint = Navy900)
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { viewModel.navigateTo(AppScreen.GUIDED_TOUR) },
                                modifier = Modifier.testTag("top_guided_tour_btn")
                            ) {
                                Icon(Icons.Default.OndemandVideo, contentDescription = "App Tour & Guide", tint = CyanDark)
                            }

                            IconButton(
                                onClick = { viewModel.setShowResetDialog(true) },
                                modifier = Modifier.testTag("top_reset_data_btn")
                            ) {
                                Icon(Icons.Default.RestartAlt, contentDescription = "Reset Demo Data", tint = StatusError)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = LightSurface,
                            titleContentColor = Navy900
                        )
                    )

                    // Persistent Synthetic Data Notice & Role Switcher Banner
                    SyntheticDataBanner(
                        currentRole = currentRole,
                        onRoleSelect = { viewModel.switchRole(it) }
                    )
                }
            },
            bottomBar = {
                // High-Contrast Institutional Bottom Navigation Bar with Accent Divider
                Column {
                    HorizontalDivider(
                        thickness = 1.5.dp,
                        color = CyanPrimary.copy(alpha = 0.5f)
                    )
                    NavigationBar(
                        containerColor = Navy900,
                        contentColor = Color.White,
                        tonalElevation = 8.dp,
                        windowInsets = WindowInsets.navigationBars
                    ) {
                        val navItemColors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = CyanBright,
                            indicatorColor = CyanPrimary,
                            unselectedIconColor = Navy100,
                            unselectedTextColor = Navy100.copy(alpha = 0.9f)
                        )

                        // Role-aware primary tabs for intuitive navigation
                        val navItems = when (currentRole) {
                            UserRole.STUDENT -> listOf(
                                Triple(AppScreen.DASHBOARD, Icons.Default.Dashboard, "Overview"),
                                Triple(AppScreen.STUDENT_PROFILE, Icons.Default.School, "My Profile"),
                                Triple(AppScreen.SEARCH_VERIFICATION, Icons.Default.VerifiedUser, "Verify"),
                                Triple(AppScreen.REPORTS, Icons.Default.Description, "Transcripts"),
                                Triple(AppScreen.GUIDED_TOUR, Icons.Default.OndemandVideo, "App Guide")
                            )
                            UserRole.AUTHORIZED_VERIFIER -> listOf(
                                Triple(AppScreen.DASHBOARD, Icons.Default.Dashboard, "Dashboard"),
                                Triple(AppScreen.SEARCH_VERIFICATION, Icons.Default.VerifiedUser, "Verify"),
                                Triple(AppScreen.DUPLICATE_REVIEW, Icons.Default.Warning, "Duplicates"),
                                Triple(AppScreen.ACTIVITY_LOGS, Icons.Default.History, "Audit Logs"),
                                Triple(AppScreen.GUIDED_TOUR, Icons.Default.OndemandVideo, "App Guide")
                            )
                            UserRole.INSTITUTION_ADMIN -> listOf(
                                Triple(AppScreen.DASHBOARD, Icons.Default.Dashboard, "Dashboard"),
                                Triple(AppScreen.RECORD_UPLOAD, Icons.Default.CloudUpload, "Upload"),
                                Triple(AppScreen.SEARCH_VERIFICATION, Icons.Default.VerifiedUser, "Verify"),
                                Triple(AppScreen.DUPLICATE_REVIEW, Icons.Default.Warning, "Duplicates"),
                                Triple(AppScreen.GUIDED_TOUR, Icons.Default.OndemandVideo, "App Guide")
                            )
                        }

                        navItems.forEach { (screen, icon, label) ->
                            val isSelected = currentScreen == screen
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(screen) },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        maxLines = 1
                                    )
                                },
                                colors = navItemColors,
                                modifier = Modifier.testTag("bottom_nav_${screen.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    AppScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                    AppScreen.STUDENT_DIRECTORY -> StudentDirectoryScreen(viewModel = viewModel)
                    AppScreen.STUDENT_PROFILE -> StudentProfileScreen(viewModel = viewModel)
                    AppScreen.RECORD_UPLOAD -> StudentRecordUploadScreen(viewModel = viewModel)
                    AppScreen.SEARCH_VERIFICATION -> SearchVerificationScreen(viewModel = viewModel)
                    AppScreen.DUPLICATE_REVIEW -> DuplicateReviewScreen(viewModel = viewModel)
                    AppScreen.ACTIVITY_LOGS -> ActivityLogsScreen(viewModel = viewModel)
                    AppScreen.INSTITUTION_MANAGEMENT -> InstitutionManagementScreen(viewModel = viewModel)
                    AppScreen.REPORTS -> ReportsScreen(viewModel = viewModel)
                    AppScreen.GUIDED_TOUR -> UnifiedAppGuideScreen(viewModel = viewModel)
                    AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }

                if (currentScreen == AppScreen.DASHBOARD) {
                    DashboardTourOverlay(viewModel = viewModel)
                }
            }
        }
    }

    // Modals
    if (showVerificationDialog) {
        VerificationReportModal(
            report = searchReport,
            onDismiss = { viewModel.setShowVerificationDialog(false) }
        )
    }

    if (showPrintSummaryDialog) {
        AcademicSummaryModal(
            student = selectedStudent,
            onDismiss = { viewModel.setShowPrintSummaryDialog(false) }
        )
    }
}
