package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.AppScreen
import com.example.ui.ApaarViewModel
import com.example.ui.components.CaseStatusBadge
import com.example.ui.components.MetricStatCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val currentRole by viewModel.currentRole.collectAsState()
    val metrics by viewModel.dashboardMetrics.collectAsState()
    val students by viewModel.students.collectAsState()
    val duplicateCases by viewModel.duplicateCases.collectAsState()
    val uploadBatches by viewModel.uploadBatches.collectAsState()
    val activityLogs by viewModel.activityLogs.collectAsState()
    val selectedStudent by viewModel.selectedStudent.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome Header Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, LightOutline, RoundedCornerShape(12.dp)),
                color = Navy900
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = when (currentRole) {
                                    UserRole.INSTITUTION_ADMIN -> "Institution Console"
                                    UserRole.STUDENT -> "Student Academic Portal"
                                    UserRole.AUTHORIZED_VERIFIER -> "Verification Workspace"
                                },
                                color = Navy100,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = when (currentRole) {
                                    UserRole.INSTITUTION_ADMIN -> "ABC Institute of Technology"
                                    UserRole.STUDENT -> "Welcome, Rahul Kumar"
                                    UserRole.AUTHORIZED_VERIFIER -> "Authorized Verifier Portal"
                                },
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(CyanPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (currentRole) {
                                    UserRole.INSTITUTION_ADMIN -> Icons.Default.Apartment
                                    UserRole.STUDENT -> Icons.Default.School
                                    UserRole.AUTHORIZED_VERIFIER -> Icons.Default.VerifiedUser
                                },
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Navy700)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = when (currentRole) {
                            UserRole.INSTITUTION_ADMIN -> "AICTE Affiliation ID: INST-DEMO-001 • Status: Active Batch Reviewer"
                            UserRole.STUDENT -> "DEMO-APAAR-1001 • STU-DEMO-001 • B.Tech CSE (2023–2027)"
                            UserRole.AUTHORIZED_VERIFIER -> "Credentials: verifier@demo.apaaroneview.test • AICTE Validation Protocol"
                        },
                        color = CyanBright,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { viewModel.startDashboardTour() },
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanBright),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyanBright.copy(alpha = 0.6f)),
                        modifier = Modifier.testTag("launch_dashboard_tour_pill")
                    ) {
                        Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(13.dp), tint = CyanBright)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Take Interactive Dashboard Tour", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Interactive Guide & Demo Video CTA Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.navigateTo(AppScreen.GUIDED_TOUR) }
                    .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .testTag("dashboard_user_guide_banner"),
                color = CyanSurface
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CyanDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleFilled,
                            contentDescription = "Demo Video Guide",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Need a Guide? Watch Demo Video & Handbooks",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CyanDark
                        )
                        Text(
                            text = "Step-by-step navigation for Students, Institutions, and Verifiers",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = "Open Guide",
                        tint = CyanDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // ROLE-SPECIFIC CONTENT
        when (currentRole) {
            UserRole.INSTITUTION_ADMIN -> {
                // Section 1: Administrator Metric Cards
                item {
                    Text(
                        text = "Institutional Overview",
                        color = Navy900,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricStatCard(
                            title = "Total Demo Records",
                            value = "${metrics.totalRecords}",
                            icon = Icons.Default.Group,
                            iconTint = CyanPrimary,
                            subText = "Across 3 institutions",
                            onClick = { viewModel.navigateTo(AppScreen.STUDENT_DIRECTORY) },
                            modifier = Modifier.weight(1f)
                        )
                        MetricStatCard(
                            title = "Missing Fields",
                            value = "${metrics.missingFieldsCount}",
                            icon = Icons.Default.WarningAmber,
                            iconTint = StatusWarning,
                            subText = "Require completion",
                            onClick = { viewModel.navigateTo(AppScreen.STUDENT_DIRECTORY) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricStatCard(
                            title = "Possible Duplicates",
                            value = "${metrics.possibleDuplicatesCount}",
                            icon = Icons.Default.ContentCopy,
                            iconTint = StatusError,
                            subText = "Pending review",
                            onClick = { viewModel.navigateTo(AppScreen.DUPLICATE_REVIEW) },
                            modifier = Modifier.weight(1f)
                        )
                        MetricStatCard(
                            title = "Recent Batches",
                            value = "${metrics.recentUploadsCount}",
                            icon = Icons.Default.CloudDone,
                            iconTint = StatusSuccess,
                            subText = "Latest: BATCH-004",
                            onClick = { viewModel.navigateTo(AppScreen.RECORD_UPLOAD) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Section 2: Quick Action CTAs
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.RECORD_UPLOAD) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("upload_student_records_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload Records", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.SEARCH_VERIFICATION) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("search_student_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Navy800)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Search Student", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                // Section 3: Duplicate Cases Requiring Attention
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Flagged Cases Requiring Review",
                            color = Navy900,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { viewModel.navigateTo(AppScreen.DUPLICATE_REVIEW) }) {
                            Text("View All (${duplicateCases.size})", fontSize = 12.sp, color = CyanDark)
                        }
                    }
                }

                items(duplicateCases.take(2)) { case ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, LightOutline, RoundedCornerShape(10.dp))
                            .clickable {
                                viewModel.selectDuplicateCase(case)
                                viewModel.navigateTo(AppScreen.DUPLICATE_REVIEW)
                            },
                        color = LightSurface
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${case.studentName} (${case.apaarId})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Navy900
                                )
                                CaseStatusBadge(status = case.status)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = case.reason,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                // Section 4: Recent Upload Batches
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Recent Upload History",
                            color = Navy900,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { viewModel.navigateTo(AppScreen.RECORD_UPLOAD) }) {
                            Text("New Upload", fontSize = 12.sp, color = CyanDark)
                        }
                    }
                }

                items(uploadBatches.take(2)) { batch ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, LightOutline, RoundedCornerShape(10.dp)),
                        color = LightSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyanSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = CyanDark, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = batch.fileName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy900)
                                Text(
                                    text = "${batch.timestamp} • ${batch.rowsProcessed} processed (${batch.rowsAccepted} accepted, ${batch.rowsFlagged} flagged)",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (batch.rowsFlagged > 0) StatusWarningBg else StatusSuccessBg
                            ) {
                                Text(
                                    text = batch.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (batch.rowsFlagged > 0) StatusWarning else StatusSuccess,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            UserRole.STUDENT -> {
                // STUDENT DASHBOARD: Rahul Kumar
                val rahul = selectedStudent ?: students.first()

                item {
                    Text(
                        text = "Student Profile Summary",
                        color = Navy900,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, LightOutline, RoundedCornerShape(12.dp)),
                        color = LightSurface
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = rahul.studentName,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        color = Navy900
                                    )
                                    Text(
                                        text = "Roll / Student ID: ${rahul.studentId}",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                                StatusBadge(status = rahul.recordStatus)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = LightOutline)
                            Spacer(modifier = Modifier.height(10.dp))

                            InfoGridRow(label = "Demo APAAR ID", value = rahul.demoApaarId, isHighlight = true)
                            InfoGridRow(label = "Institution", value = rahul.institutionName)
                            InfoGridRow(label = "Course & Branch", value = rahul.course)
                            InfoGridRow(label = "Study Period", value = "${rahul.studyStartYear} – ${rahul.studyEndYear ?: "Present"}")
                            InfoGridRow(label = "Last Record Update", value = rahul.lastUpdated)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.setShowPrintSummaryDialog(true) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .testTag("student_print_summary_button"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                                ) {
                                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Academic Summary", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { viewModel.navigateTo(AppScreen.STUDENT_PROFILE) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Timeline, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Full Timeline", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Academic Credits & Semesters Summary
                item {
                    Text(
                        text = "Academic Milestones & Semesters",
                        color = Navy900,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(rahul.semesters) { semester ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, LightOutline, RoundedCornerShape(10.dp)),
                        color = LightSurface
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = semester.academicTerm,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Navy900
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = StatusSuccessBg
                                ) {
                                    Text(
                                        text = "SGPA: ${semester.gpa}",
                                        color = StatusSuccess,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Credits: ${semester.creditsEarned} • Subjects Validated: ${semester.subjects.size} • Verified: ${semester.validatedTimestamp}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Student Privacy Notice
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = Navy50,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LightOutline)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Navy700, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Role Security Enforcement: In Student Mode, private records of other enrolled students are restricted per role-based access rules.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            UserRole.AUTHORIZED_VERIFIER -> {
                // VERIFIER DASHBOARD
                item {
                    Text(
                        text = "Instant Identifier Search",
                        color = Navy900,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, LightOutline, RoundedCornerShape(12.dp)),
                        color = LightSurface
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Search Demo APAAR ID or Student ID",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            var tempQuery by remember { mutableStateOf("DEMO-APAAR-1001") }

                            OutlinedTextField(
                                value = tempQuery,
                                onValueChange = { tempQuery = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("verifier_search_input"),
                                placeholder = { Text("e.g. DEMO-APAAR-1001, DEMO-APAAR-1002") },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        viewModel.updateSearchQuery(tempQuery)
                                        viewModel.performSearch()
                                        viewModel.navigateTo(AppScreen.SEARCH_VERIFICATION)
                                    }) {
                                        Icon(Icons.Default.Search, contentDescription = "Search", tint = CyanPrimary)
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Quick test query pills
                            Text(text = "Quick Test Cases:", fontSize = 11.sp, color = TextMuted)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilterChip(
                                    selected = tempQuery == "DEMO-APAAR-1001",
                                    onClick = { tempQuery = "DEMO-APAAR-1001" },
                                    label = { Text("Rahul Kumar (Valid)", fontSize = 10.sp) }
                                )
                                FilterChip(
                                    selected = tempQuery == "DEMO-APAAR-1002",
                                    onClick = { tempQuery = "DEMO-APAAR-1002" },
                                    label = { Text("Duplicate Case", fontSize = 10.sp) }
                                )
                                FilterChip(
                                    selected = tempQuery == "DEMO-APAAR-1003",
                                    onClick = { tempQuery = "DEMO-APAAR-1003" },
                                    label = { Text("Incomplete", fontSize = 10.sp) }
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    viewModel.updateSearchQuery(tempQuery)
                                    viewModel.performSearch()
                                    viewModel.navigateTo(AppScreen.SEARCH_VERIFICATION)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("verifier_execute_search_btn"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Execute Verification Check", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Recent Verification Activities",
                        color = Navy900,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(activityLogs.filter { it.action.contains("Verification", ignoreCase = true) }.take(3)) { log ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, LightOutline, RoundedCornerShape(10.dp)),
                        color = LightSurface
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = log.action, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy900)
                                Text(text = log.timestamp, fontSize = 10.sp, color = TextMuted)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = log.details, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
                        }
                    }
                }
            }
        }

        // Section: System Audit Trail Preview (All Roles)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recent System Activity",
                    color = Navy900,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { viewModel.navigateTo(AppScreen.ACTIVITY_LOGS) }) {
                    Text("View Audit Logs", fontSize = 12.sp, color = CyanDark)
                }
            }
        }

        items(activityLogs.take(2)) { log ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, LightOutline, RoundedCornerShape(8.dp)),
                color = LightSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (log.outcome == "Success") StatusSuccess else StatusWarning)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${log.action} • ${log.userRole}", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Navy900)
                        Text(text = log.details, fontSize = 10.sp, color = TextMuted, maxLines = 1)
                    }
                    Text(text = log.timestamp.split(" ").firstOrNull() ?: "", fontSize = 9.sp, color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun InfoGridRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, color = TextMuted)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) CyanDark else Navy900
        )
    }
}
