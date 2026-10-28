package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ApaarViewModel
import com.example.ui.theme.*

@Composable
fun ReportsScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val metrics by viewModel.dashboardMetrics.collectAsState()
    val duplicateCases by viewModel.duplicateCases.collectAsState()
    val uploadBatches by viewModel.uploadBatches.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Institutional Reports & Summaries",
                    color = Navy900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Exportable audit statements and student academic certificates",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Statutory Report Notice
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
                    Icon(Icons.Default.Verified, contentDescription = null, tint = Navy700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Report Notice: This report summarizes records available in the APAAR OneView demonstration database. It is not an official APAAR identity verification or government-issued academic verification.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Report 1: Student Verification Review Report
        item {
            ReportCard(
                title = "Student Verification-Review Report",
                subtitle = "Official audit document certifying records retrieved via Demo APAAR ID search.",
                icon = Icons.Default.VerifiedUser,
                details = listOf(
                    "Target ID: DEMO-APAAR-1001 (Rahul Kumar)",
                    "Evaluation Result: Demo record found (3 Semesters Validated)",
                    "Issuing Authority: ABC Institute of Technology",
                    "Status: Level 2 Data Validated"
                ),
                buttonLabel = "Preview Verification Certificate",
                onAction = { viewModel.setShowVerificationDialog(true) },
                buttonTag = "preview_verif_cert_btn"
            )
        }

        // Report 2: Student Academic Record Transcript
        item {
            ReportCard(
                title = "Consolidated Academic Summary",
                subtitle = "Official course transcript and earned credit audit across completed terms.",
                icon = Icons.Default.School,
                details = listOf(
                    "Student: Rahul Kumar (STU-DEMO-001)",
                    "Degree: B.Tech Computer Science and Engineering",
                    "Total Earned Credits: 69 Credits • CGPA: 8.97",
                    "Accreditation: AICTE Demo Institutional Schema"
                ),
                buttonLabel = "Preview Academic Transcript",
                onAction = { viewModel.setShowPrintSummaryDialog(true) },
                buttonTag = "preview_academic_cert_btn"
            )
        }

        // Report 3: Institutional Duplicate & Collision Audit
        item {
            ReportCard(
                title = "Institutional Duplicate & Collision Audit",
                subtitle = "Comprehensive report of cross-institutional collisions and open review cases.",
                icon = Icons.Default.Warning,
                details = listOf(
                    "Total Active Records: ${metrics.totalRecords}",
                    "Total Flagged Cases: ${duplicateCases.size}",
                    "Open Collisions: ${duplicateCases.count { it.status.name == "OPEN" }}",
                    "Resolved / False Positives: ${duplicateCases.count { it.status.name == "RESOLVED" || it.status.name == "FALSE_POSITIVE" }}"
                ),
                buttonLabel = "Review Collision Triage",
                onAction = { viewModel.navigateTo(com.example.ui.AppScreen.DUPLICATE_REVIEW) },
                buttonTag = "preview_collision_audit_btn"
            )
        }
    }
}

@Composable
private fun ReportCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    details: List<String>,
    buttonLabel: String,
    onAction: () -> Unit,
    buttonTag: String
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyanSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = CyanDark, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy900)
                    Text(text = subtitle, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = LightOutline)
            Spacer(modifier = Modifier.height(8.dp))

            details.forEach { d ->
                Text(text = "• $d", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(vertical = 1.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .testTag(buttonTag),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Navy800)
            ) {
                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(buttonLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
