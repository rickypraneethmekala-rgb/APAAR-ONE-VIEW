package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.ApaarViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay

data class AppTourSection(
    val stepNumber: Int,
    val title: String,
    val screenCategory: String,
    val targetRole: UserRole,
    val targetScreen: AppScreen,
    val purpose: String,
    val howItWorks: String,
    val keyFeatures: List<String>,
    val actionText: String
)

data class VideoSceneTour(
    val title: String,
    val sectionNumber: Int,
    val timestamp: String,
    val screenName: String,
    val narration: String,
    val highlights: List<String>,
    val targetRole: UserRole,
    val targetScreen: AppScreen,
    val actionLabel: String
)

@Composable
fun UnifiedAppGuideScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Video Tour Simulator, 1: Full App Tour, 2: Role Handbooks, 3: Key Concepts

    val tourSections = listOf(
        AppTourSection(
            stepNumber = 1,
            title = "Dashboard & Real-Time Metrics",
            screenCategory = "Overview Console",
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.DASHBOARD,
            purpose = "Serves as the mission control console displaying real-time repository health and dynamic statistics.",
            howItWorks = "All numbers (total records, missing end years, duplicate collision alerts) are computed dynamically from the active repository without hardcoded data. Switching roles in the top banner instantly personalizes this dashboard.",
            keyFeatures = listOf(
                "Calculated metrics across 12 synthetic student records",
                "Instant 1-tap persona chips (Admin, Student, Verifier)",
                "Action buttons for Record Upload and Candidate Search",
                "Flagged duplicate cases requiring immediate coordinator review"
            ),
            actionText = "Open Live Dashboard"
        ),
        AppTourSection(
            stepNumber = 2,
            title = "Student Academic Profile & Transcripts",
            screenCategory = "Academic Records",
            targetRole = UserRole.STUDENT,
            targetScreen = AppScreen.STUDENT_PROFILE,
            purpose = "Empowers students to inspect their registered Demo APAAR ID, course progression, and validated semester transcripts.",
            howItWorks = "In Student mode (Rahul Kumar), users view complete term-by-term coursework: Fall 2023 (SGPA 8.85), Spring 2024 (SGPA 9.12), and Fall 2024 (SGPA 8.94). Students cannot view private peer records.",
            keyFeatures = listOf(
                "Verified Demo APAAR ID: DEMO-APAAR-1001",
                "69 total earned credits across 3 validated terms",
                "Detailed breakdown of course codes, subjects, and grades",
                "One-click 'View Print-Friendly Academic Summary' certificate"
            ),
            actionText = "Open Student Profile & Transcript"
        ),
        AppTourSection(
            stepNumber = 3,
            title = "Student Academic Directory",
            screenCategory = "Directory & Roster",
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.STUDENT_DIRECTORY,
            purpose = "A searchable repository roster showing all 12 student records across the 3 partner AICTE institutions.",
            howItWorks = "Administrators and authorized users can filter by student name, roll ID, course, status (Validated, Duplicate, Incomplete), or college campus.",
            keyFeatures = listOf(
                "Unified directory across ABC Tech, Sunrise College, and Greenfield",
                "Instant status badges: Validated, Duplicate, Incomplete",
                "Tap any student card to inspect their complete profile",
                "Fast text search filtering in real time"
            ),
            actionText = "Open Student Directory"
        ),
        AppTourSection(
            stepNumber = 4,
            title = "Record Upload & Validation Engine",
            screenCategory = "Data Synchronization",
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.RECORD_UPLOAD,
            purpose = "Enables institutions to batch-upload structured student admissions via CSV with automated error detection.",
            howItWorks = "The engine parses incoming rows and enforces a 9-column AICTE schema. It validates required fields, catches blank student IDs, flags missing study-end years, and detects repeated APAAR IDs before database commit.",
            keyFeatures = listOf(
                "Downloadable / inspectable 9-column AICTE CSV schema",
                "Pre-built test loaders: 'Load Valid Batch' & 'Load Batch with Collisions'",
                "Row-by-row validation table with understandable guidance",
                "Import summary: rows processed, accepted, flagged, and rejected",
                "Persistent upload history logging date, file name, and uploader"
            ),
            actionText = "Open Record Upload Console"
        ),
        AppTourSection(
            stepNumber = 5,
            title = "Search & Verification Review",
            screenCategory = "Identity & Record Verification",
            targetRole = UserRole.AUTHORIZED_VERIFIER,
            targetScreen = AppScreen.SEARCH_VERIFICATION,
            purpose = "Allows authorized verifiers to evaluate candidate credentials against multi-state validation rules.",
            howItWorks = "Users enter a Demo APAAR ID, Student ID, or Name. The system evaluates the query against known collision rules and produces one of four distinct states with clear explanations.",
            keyFeatures = listOf(
                "State 1: 'Demo record found' (e.g. DEMO-APAAR-1001 - Rahul Kumar)",
                "State 2: 'Possible duplicate — review required' (e.g. DEMO-APAAR-1002 across colleges)",
                "State 3: 'Incomplete information — review required' (e.g. DEMO-APAAR-1003 missing end year)",
                "State 4: 'No matching demo record' (e.g. DEMO-APAAR-9999 non-existent ID)",
                "Explanation of how the rule-based result was produced"
            ),
            actionText = "Open Search & Verification"
        ),
        AppTourSection(
            stepNumber = 6,
            title = "Verifiable Report Generator",
            screenCategory = "Certificates & Audits",
            targetRole = UserRole.AUTHORIZED_VERIFIER,
            targetScreen = AppScreen.SEARCH_VERIFICATION,
            purpose = "Generates an official Verification-Review Certificate detailing record authenticity and disclaimers.",
            howItWorks = "After executing a verification search, tapping 'Generate Verification-Review Report' launches an exportable modal with full candidate details and institutional metadata.",
            keyFeatures = listOf(
                "Unique Report Tracking Identifier (e.g. VER-REP-XXXXXX)",
                "Candidate credential summary and evaluated result state",
                "Collision and missing field warnings prominently called out",
                "Official demonstration disclaimer explaining local prototype boundaries"
            ),
            actionText = "Preview Verification Certificate"
        ),
        AppTourSection(
            stepNumber = 7,
            title = "Duplicate & Collision Triage Console",
            screenCategory = "Collision Resolution",
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.DUPLICATE_REVIEW,
            purpose = "Provides a fair due-process workflow to investigate and resolve cross-institutional data collisions.",
            howItWorks = "When an APAAR ID is associated with multiple institutions (e.g. Priya Sharma enrolled in both Sunrise College and Greenfield Institute), the system alerts coordinators without deleting either record.",
            keyFeatures = listOf(
                "Never deletes candidate records automatically",
                "Case triage statuses: Open, Under review, Resolved, False positive",
                "Coordinators document review notes and investigation findings",
                "Non-destructive audit trail preserves full historical accountability"
            ),
            actionText = "Open Duplicate Triage Console"
        ),
        AppTourSection(
            stepNumber = 8,
            title = "AICTE Institutions Registry",
            screenCategory = "Institutional Partners",
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.INSTITUTION_MANAGEMENT,
            purpose = "Displays participating AICTE institutions engaged in unified academic data exchange.",
            howItWorks = "Shows detailed records for ABC Institute of Technology (INST-DEMO-001), Sunrise College of Engineering (INST-DEMO-002), and Greenfield Institute of Technology (INST-DEMO-003).",
            keyFeatures = listOf(
                "Campus location and coordinator email contacts",
                "Institutional code and active batch synchronization counts",
                "Live count of affiliated student records for each campus"
            ),
            actionText = "Open Institutions Registry"
        ),
        AppTourSection(
            stepNumber = 9,
            title = "System Activity & Immutable Audit Logs",
            screenCategory = "Security & Traceability",
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.ACTIVITY_LOGS,
            purpose = "Chronological audit ledger recording every operational event for complete transparency.",
            howItWorks = "Every login, batch upload, validation check, duplicate triage status change, and verification query is written to an immutable append-only activity log.",
            keyFeatures = listOf(
                "Immutable entries with timestamp, actor role, action, and outcome",
                "Search and filter by outcome (Success, Flagged, Warning)",
                "Security policy: Entries cannot be modified or deleted via UI"
            ),
            actionText = "Open Activity Logs"
        ),
        AppTourSection(
            stepNumber = 10,
            title = "Settings & Synthetic Demo Reset",
            screenCategory = "Configuration & Reset",
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.SETTINGS,
            purpose = "Configuration center and safe demonstration dataset reset mechanism.",
            howItWorks = "Tapping 'Reset Demo Data to Initial State' brings up a confirmation dialog. Confirming restores the original 12 student records, 3 duplicate cases, and initial batches without affecting real data.",
            keyFeatures = listOf(
                "One-click restoration of sample test cases",
                "Active persona switcher with credential overview",
                "System architecture notes and technical specifications"
            ),
            actionText = "Open Settings & Reset"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Main Screen Header
        item {
            Column {
                Text(
                    text = "App Tour & Comprehensive Guide",
                    color = Navy900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Complete interactive walkthrough and screen-by-screen guide for APAAR OneView",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Mode Navigation Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = LightSurface,
                contentColor = CyanPrimary,
                edgePadding = 0.dp
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayCircleFilled, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Video Tour Simulator", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Complete App Walkthrough", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Role Handbooks", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Key Concepts", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Video Tour Simulator Tab
                item {
                    FullAppVideoSimulator(viewModel = viewModel)
                }

                item {
                    Text(
                        text = "Quick Persona Switcher",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                }

                item {
                    RoleQuickCards(viewModel = viewModel)
                }
            }
            1 -> {
                // Complete App Walkthrough (10 Sections)
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = CyanSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Explore, contentDescription = null, tint = CyanDark, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "10-Step Full Platform Walkthrough",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = CyanDark
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Explore how every screen and component in APAAR OneView works together to deliver a secure, centralized academic verification workflow.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                itemsIndexed(tourSections) { _, section ->
                    DetailedSectionCard(
                        section = section,
                        onOpenScreen = {
                            viewModel.switchRole(section.targetRole)
                            if (section.stepNumber == 6) {
                                viewModel.setShowVerificationDialog(true)
                            } else {
                                viewModel.navigateTo(section.targetScreen)
                            }
                        }
                    )
                }
            }
            2 -> {
                // Role Handbooks Tab (Student, Admin, Verifier)
                item {
                    RoleHandbookSection(viewModel = viewModel)
                }
            }
            3 -> {
                // Key Concepts & FAQ Tab
                item {
                    KeyConceptsSection()
                }
            }
        }
    }
}

@Composable
private fun FullAppVideoSimulator(viewModel: ApaarViewModel) {
    val scenes = listOf(
        VideoSceneTour(
            title = "1. Dashboard & Persona Console",
            sectionNumber = 1,
            timestamp = "00:00 / 02:30",
            screenName = "Dashboard",
            narration = "Welcome to APAAR OneView. The institutional dashboard displays dynamic repository metrics, quick actions for upload and verification, and active persona chips.",
            highlights = listOf("Real-time statistics across 12 synthetic student records", "1-tap persona chips for Admin, Student, and Verifier", "Direct action shortcuts for upload and search"),
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.DASHBOARD,
            actionLabel = "Open Live Dashboard"
        ),
        VideoSceneTour(
            title = "2. Student Profile & Semester Transcripts",
            sectionNumber = 2,
            timestamp = "00:15 / 02:30",
            screenName = "Academic Profile",
            narration = "Students like Rahul Kumar (DEMO-APAAR-1001) access their verified academic transcript showing 3 completed terms (SGPA 8.85, 9.12, 8.94) and 69 earned credits.",
            highlights = listOf("Personal Demo APAAR ID: DEMO-APAAR-1001", "Fall 2023, Spring 2024, Fall 2024 validated terms", "Printable academic summary certificate"),
            targetRole = UserRole.STUDENT,
            targetScreen = AppScreen.STUDENT_PROFILE,
            actionLabel = "Open Student Profile"
        ),
        VideoSceneTour(
            title = "3. Student Directory & Search Filtering",
            sectionNumber = 3,
            timestamp = "00:30 / 02:30",
            screenName = "Student Directory",
            narration = "The student directory offers multi-filter search across institutions, degrees, and record statuses (Validated, Possible Duplicate, Incomplete).",
            highlights = listOf("Search by name, student roll ID, or APAAR ID", "Filters for Validated, Duplicate, and Incomplete records", "Campus filters for ABC Tech, Sunrise, and Greenfield"),
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.STUDENT_DIRECTORY,
            actionLabel = "Open Student Directory"
        ),
        VideoSceneTour(
            title = "4. Batch CSV Upload & Validation",
            sectionNumber = 4,
            timestamp = "00:45 / 02:30",
            screenName = "Record Upload",
            narration = "Institutions stage CSV admissions batches. The validation engine checks 9 required columns, catches blank fields, and flags repeated APAAR IDs.",
            highlights = listOf("Pre-built test batches with deliberate collisions", "Row-by-row error feedback with suggested fixes", "Import summary of accepted, flagged, and rejected rows"),
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.RECORD_UPLOAD,
            actionLabel = "Open Record Upload"
        ),
        VideoSceneTour(
            title = "5. Search & Identity Verification Review",
            sectionNumber = 5,
            timestamp = "01:05 / 02:30",
            screenName = "Search & Verify",
            narration = "Verifiers search candidate identifiers. The system produces 4 distinct states: Record Found, Possible Duplicate, Incomplete Info, or No Matching Record.",
            highlights = listOf("Query clean record DEMO-APAAR-1001 (Record Found)", "Query collision DEMO-APAAR-1002 (Duplicate Flag)", "Query incomplete DEMO-APAAR-1003 (Missing End Year)"),
            targetRole = UserRole.AUTHORIZED_VERIFIER,
            targetScreen = AppScreen.SEARCH_VERIFICATION,
            actionLabel = "Open Search & Verification"
        ),
        VideoSceneTour(
            title = "6. Verifiable Review Report Certificate",
            sectionNumber = 6,
            timestamp = "01:25 / 02:30",
            screenName = "Verification Report",
            narration = "Generate an exportable Verification-Review Certificate detailing verified term counts, campus origin, and statutory non-governmental disclaimers.",
            highlights = listOf("Unique Verification Report ID (VER-REP-XXXXXX)", "Full candidate summary and match explanation", "Mandatory demonstration disclaimer"),
            targetRole = UserRole.AUTHORIZED_VERIFIER,
            targetScreen = AppScreen.SEARCH_VERIFICATION,
            actionLabel = "Preview Verification Certificate"
        ),
        VideoSceneTour(
            title = "7. Duplicate & Collision Triage Console",
            sectionNumber = 7,
            timestamp = "01:45 / 02:30",
            screenName = "Duplicate Review",
            narration = "Review collision case DUP-DEMO-001 (Priya Sharma in two colleges). Triage status: Open, Under review, Resolved, or False positive with coordinator notes.",
            highlights = listOf("Never deletes candidate records automatically", "Documents reasons for multi-institution collision", "Coordinators save notes and resolution status"),
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.DUPLICATE_REVIEW,
            actionLabel = "Open Duplicate Triage"
        ),
        VideoSceneTour(
            title = "8. Institutions Registry & Activity Logs",
            sectionNumber = 8,
            timestamp = "02:05 / 02:30",
            screenName = "Institutions & Logs",
            narration = "Explore participating partner colleges and inspect the immutable audit ledger tracking all uploads, searches, and triage status updates.",
            highlights = listOf("3 partner AICTE campus coordinators and codes", "Chronological audit ledger with actor roles", "Zero-tamper immutable security policy"),
            targetRole = UserRole.INSTITUTION_ADMIN,
            targetScreen = AppScreen.ACTIVITY_LOGS,
            actionLabel = "Open Activity Logs"
        )
    )

    var currentSceneIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying, currentSceneIndex) {
        if (isPlaying) {
            delay(5000)
            currentSceneIndex = (currentSceneIndex + 1) % scenes.size
        }
    }

    val scene = scenes[currentSceneIndex]

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Navy700, RoundedCornerShape(14.dp)),
        color = Navy900
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Player Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isPlaying) StatusSuccess else StatusWarning)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPlaying) "SIMULATED VIDEO TOUR PLAYING" else "SIMULATED VIDEO TOUR PAUSED",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CyanDark
                ) {
                    Text(
                        text = scene.screenName,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Simulated Video Display Canvas Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Navy800, Color(0xFF0F2642))
                        )
                    )
                    .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = scene.title,
                            color = CyanBright,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = scene.timestamp,
                            color = Navy100,
                            fontSize = 10.sp
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Navy900.copy(alpha = 0.75f), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Key Demonstration Highlights:",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        scene.highlights.forEach { h ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyanBright, modifier = Modifier.size(11.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = h, color = Navy100, fontSize = 10.sp)
                            }
                        }
                    }

                    Text(
                        text = "Scene ${currentSceneIndex + 1} of ${scenes.size} • Tap Next or Play to advance",
                        color = TextMuted,
                        fontSize = 9.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Player Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        currentSceneIndex = if (currentSceneIndex > 0) currentSceneIndex - 1 else scenes.size - 1
                    }
                ) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Previous Scene", tint = Color.White)
                }

                FilledIconButton(
                    onClick = { isPlaying = !isPlaying },
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = CyanPrimary)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        currentSceneIndex = (currentSceneIndex + 1) % scenes.size
                    }
                ) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next Scene", tint = Color.White)
                }

                // Progress Step Dots
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    scenes.forEachIndexed { i, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (i == currentSceneIndex) 14.dp else 7.dp, 7.dp)
                                .clip(CircleShape)
                                .background(if (i == currentSceneIndex) CyanBright else Navy700)
                                .clickable { currentSceneIndex = i }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Narration Text Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Navy800
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = CyanBright, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = scene.narration,
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Instant Live Jump Button
            Button(
                onClick = {
                    viewModel.switchRole(scene.targetRole)
                    if (scene.sectionNumber == 6) {
                        viewModel.setShowVerificationDialog(true)
                    } else {
                        viewModel.navigateTo(scene.targetScreen)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
            ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${scene.actionLabel} (Live App)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun DetailedSectionCard(
    section: AppTourSection,
    onOpenScreen: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, LightOutline, RoundedCornerShape(12.dp)),
        color = LightSurface,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Navy800),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${section.stepNumber}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = section.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Navy900
                        )
                        Text(
                            text = section.screenCategory,
                            fontSize = 10.sp,
                            color = CyanDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when (section.targetRole) {
                        UserRole.INSTITUTION_ADMIN -> CyanSurface
                        UserRole.STUDENT -> StatusSuccessBg
                        UserRole.AUTHORIZED_VERIFIER -> StatusWarningBg
                    }
                ) {
                    Text(
                        text = section.targetRole.label.split(" ").first().uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = when (section.targetRole) {
                            UserRole.INSTITUTION_ADMIN -> CyanDark
                            UserRole.STUDENT -> StatusSuccess
                            UserRole.AUTHORIZED_VERIFIER -> StatusWarning
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = LightOutline)
            Spacer(modifier = Modifier.height(8.dp))

            // Purpose
            Text(
                text = "Purpose & Objective:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Navy900
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = section.purpose,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // How It Works
            Text(
                text = "How It Works:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Navy900
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = section.howItWorks,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Key Features Checklist
            Text(
                text = "Key Interactive Capabilities:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Navy900
            )
            Spacer(modifier = Modifier.height(4.dp))
            section.keyFeatures.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = feature, fontSize = 11.sp, color = Navy900)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Direct Jump Button
            Button(
                onClick = onOpenScreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = section.actionText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RoleHandbookSection(viewModel: ApaarViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Student Guide
        RoleGuideCard(
            roleTitle = "Student Handbook (Rahul Kumar)",
            icon = Icons.Default.School,
            accentColor = StatusSuccess,
            badge = "Student",
            instructions = listOf(
                "Switch to Student mode using the top banner or credentials: rahul@demo.apaaroneview.test / Demo@123.",
                "Go to 'Academic Profile' to inspect your verified Demo APAAR ID: DEMO-APAAR-1001.",
                "Review completed semesters: Fall 2023 (SGPA 8.85), Spring 2024 (SGPA 9.12), Fall 2024 (SGPA 8.94).",
                "Tap 'View Print-Friendly Academic Summary' to launch your exportable coursework certificate.",
                "Notice student privacy rules: peer directory records are restricted."
            ),
            ctaText = "Switch to Student & Open Profile",
            onAction = {
                viewModel.switchRole(UserRole.STUDENT)
                viewModel.navigateTo(AppScreen.STUDENT_PROFILE)
            }
        )

        // Institution Admin Guide
        RoleGuideCard(
            roleTitle = "Institution Administrator Handbook",
            icon = Icons.Default.Apartment,
            accentColor = CyanPrimary,
            badge = "Institution Admin",
            instructions = listOf(
                "Log in as Admin (admin@demo.apaaroneview.test / Demo@123).",
                "Review Dashboard: 12 total demo records, 1 missing field, 2 duplicate collisions.",
                "Go to 'Upload Records' and click 'Load Batch with Collisions' to simulate CSV validation.",
                "Inspect the row-by-row error table: blank fields, repeated APAAR IDs, and unknown colleges.",
                "Go to 'Duplicate Review' to triage case DUP-DEMO-001 (Priya Sharma across two colleges) without data loss.",
                "Open 'Activity Logs' to verify immutable audit logging."
            ),
            ctaText = "Switch to Admin & Open Dashboard",
            onAction = {
                viewModel.switchRole(UserRole.INSTITUTION_ADMIN)
                viewModel.navigateTo(AppScreen.DASHBOARD)
            }
        )

        // Verifier Guide
        RoleGuideCard(
            roleTitle = "Authorized Verifier Handbook",
            icon = Icons.Default.VerifiedUser,
            accentColor = Navy800,
            badge = "Verifier",
            instructions = listOf(
                "Switch to Verifier mode (verifier@demo.apaaroneview.test / Demo@123).",
                "Go to 'Search & Verify'.",
                "Query clean ID DEMO-APAAR-1001 to see 'Demo record found' state.",
                "Query collision ID DEMO-APAAR-1002 to test cross-institutional collision alert.",
                "Query incomplete ID DEMO-APAAR-1003 to test missing study-end-year warning.",
                "Tap 'Generate Verification-Review Report' to inspect the certificate modal."
            ),
            ctaText = "Switch to Verifier & Open Search",
            onAction = {
                viewModel.switchRole(UserRole.AUTHORIZED_VERIFIER)
                viewModel.navigateTo(AppScreen.SEARCH_VERIFICATION)
            }
        )
    }
}

@Composable
private fun RoleGuideCard(
    roleTitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    badge: String,
    instructions: List<String>,
    ctaText: String,
    onAction: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, LightOutline, RoundedCornerShape(12.dp)),
        color = LightSurface,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = roleTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy900)
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = badge.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = LightOutline)
            Spacer(modifier = Modifier.height(8.dp))

            instructions.forEachIndexed { i, step ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "${i + 1}. ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanDark)
                    Text(text = step, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = ctaText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun KeyConceptsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ConceptCard(
            title = "Verification Status Level Hierarchy",
            icon = Icons.Default.Layers,
            content = "• Level 1: Record Available (Data exists in the demonstration repository)\n• Level 2: Data Validated (Passed structural CSV syntax and format constraints)\n• Level 3: Officially Verified (Requires live government DigiLocker/APAAR identity integration)"
        )

        ConceptCard(
            title = "Zero-Loss Duplicate Detection Rule",
            icon = Icons.Default.Security,
            content = "When an APAAR ID is associated with multiple institutions (e.g. DEMO-APAAR-1002 in Sunrise College and Greenfield Institute), the system alerts coordinators without deleting either record. Candidate records are never automatically removed, and individuals are never labeled fraudulent solely because of a data collision."
        )

        ConceptCard(
            title = "Tamper-Resistant Audit Trail",
            icon = Icons.Default.Lock,
            content = "All platform events (batch CSV uploads, collision reviews, verification queries, demo logins) are written to an append-only activity ledger. Regular users cannot alter or purge audit logs, ensuring accountability."
        )

        ConceptCard(
            title = "Deterministic Demo Reset",
            icon = Icons.Default.RestartAlt,
            content = "At any time, administrators can tap 'Reset Demo Data' in Settings to restore the initial 12 synthetic student records, 3 collision cases, and initial batches without affecting real data."
        )
    }
}

@Composable
private fun ConceptCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, LightOutline, RoundedCornerShape(10.dp)),
        color = LightSurface,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = CyanDark, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy900)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = content, fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun RoleQuickCards(viewModel: ApaarViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RoleQuickItem(
            name = "Student (Rahul Kumar)",
            email = "rahul@demo.apaaroneview.test",
            desc = "DEMO-APAAR-1001 • Academic transcripts, SGPA & credits",
            onClick = {
                viewModel.switchRole(UserRole.STUDENT)
                viewModel.navigateTo(AppScreen.STUDENT_PROFILE)
            }
        )
        RoleQuickItem(
            name = "Institution Administrator",
            email = "admin@demo.apaaroneview.test",
            desc = "ABC Institute of Technology • CSV uploads & collision triage",
            onClick = {
                viewModel.switchRole(UserRole.INSTITUTION_ADMIN)
                viewModel.navigateTo(AppScreen.DASHBOARD)
            }
        )
        RoleQuickItem(
            name = "Authorized Verifier",
            email = "verifier@demo.apaaroneview.test",
            desc = "ID query, collision alerts & verification reports",
            onClick = {
                viewModel.switchRole(UserRole.AUTHORIZED_VERIFIER)
                viewModel.navigateTo(AppScreen.SEARCH_VERIFICATION)
            }
        )
    }
}

@Composable
private fun RoleQuickItem(
    name: String,
    email: String,
    desc: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, LightOutline, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = Navy50
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy900)
                Text(text = email, fontSize = 10.sp, color = CyanDark)
                Text(text = desc, fontSize = 10.sp, color = TextMuted)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
        }
    }
}
