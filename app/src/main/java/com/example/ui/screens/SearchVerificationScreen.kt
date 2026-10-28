package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.VerificationReport
import com.example.ui.ApaarViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun SearchVerificationScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchReport by viewModel.searchReport.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()

    var inputQuery by remember { mutableStateOf(searchQuery) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Header
        item {
            Column {
                Text(
                    text = "Search & Verification Review",
                    color = Navy900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "AICTE institutional verification protocol & duplicate triage",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Search Input Box
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
                        text = "Query by Demo APAAR ID, Student ID, or Name",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = inputQuery,
                        onValueChange = { inputQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_verification_input"),
                        placeholder = { Text("e.g. DEMO-APAAR-1001, DEMO-APAAR-1002, Rahul Kumar") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.updateSearchQuery(inputQuery)
                                viewModel.performSearch()
                            }) {
                                Icon(Icons.Default.Search, contentDescription = "Search", tint = CyanPrimary)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Preset Demonstration Test Queries:", fontSize = 11.sp, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = inputQuery == "DEMO-APAAR-1001",
                            onClick = {
                                inputQuery = "DEMO-APAAR-1001"
                                viewModel.updateSearchQuery("DEMO-APAAR-1001")
                                viewModel.performSearch()
                            },
                            label = { Text("Rahul Kumar (Clean)", fontSize = 10.sp) },
                            modifier = Modifier.testTag("chip_rahul_kumar")
                        )
                        FilterChip(
                            selected = inputQuery == "DEMO-APAAR-1002",
                            onClick = {
                                inputQuery = "DEMO-APAAR-1002"
                                viewModel.updateSearchQuery("DEMO-APAAR-1002")
                                viewModel.performSearch()
                            },
                            label = { Text("Duplicate Collision", fontSize = 10.sp) },
                            modifier = Modifier.testTag("chip_duplicate_case")
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = inputQuery == "DEMO-APAAR-1003",
                            onClick = {
                                inputQuery = "DEMO-APAAR-1003"
                                viewModel.updateSearchQuery("DEMO-APAAR-1003")
                                viewModel.performSearch()
                            },
                            label = { Text("Missing End Date", fontSize = 10.sp) },
                            modifier = Modifier.testTag("chip_incomplete_case")
                        )
                        FilterChip(
                            selected = inputQuery == "DEMO-APAAR-9999",
                            onClick = {
                                inputQuery = "DEMO-APAAR-9999"
                                viewModel.updateSearchQuery("DEMO-APAAR-9999")
                                viewModel.performSearch()
                            },
                            label = { Text("Non-Existent ID", fontSize = 10.sp) },
                            modifier = Modifier.testTag("chip_unknown_case")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.updateSearchQuery(inputQuery)
                            viewModel.performSearch()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("execute_verification_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Search & Verify Prototype Records", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Verification Report Card
        searchReport?.let { report ->
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, LightOutline, RoundedCornerShape(12.dp))
                        .testTag("verification_result_card"),
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
                                    text = "Verification Evaluation Result",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Navy900
                                )
                                Text(
                                    text = "Generated: ${report.timestamp} • By: ${report.generatedBy}",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }

                            // Distinct result state badge
                            ResultStateBadge(state = report.resultState)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = LightOutline)
                        Spacer(modifier = Modifier.height(10.dp))

                        // How the result was produced
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(6.dp),
                            color = Navy50
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "Rule-Based Evaluation Explanation:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Navy900
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = report.matchExplanation,
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        // Duplicate Warnings if any
                        if (report.duplicateWarnings.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(6.dp),
                                color = StatusWarningBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, StatusWarning.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Warning, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "Possible Duplicate Warning", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = StatusWarning)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    report.duplicateWarnings.forEach { w ->
                                        Text(text = "• $w", fontSize = 11.sp, color = Navy900, lineHeight = 15.sp)
                                    }
                                }
                            }
                        }

                        // Missing Field Warnings if any
                        if (report.missingFieldWarnings.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(6.dp),
                                color = StatusErrorBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, StatusError.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = StatusError, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "Incomplete Information Warning", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = StatusError)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    report.missingFieldWarnings.forEach { w ->
                                        Text(text = "• $w", fontSize = 11.sp, color = Navy900, lineHeight = 15.sp)
                                    }
                                }
                            }
                        }

                        // Student Profile summary if record found
                        report.studentSummary?.let { student ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Matched Demo Candidate Record",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Navy900
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            RecordRow(label = "Student Name", value = student.studentName)
                            RecordRow(label = "Student Roll / ID", value = student.studentId)
                            RecordRow(label = "Demo APAAR ID", value = student.demoApaarId)
                            RecordRow(label = "Affiliated Institution", value = student.institutionName)
                            RecordRow(label = "Academic Course", value = student.course)
                            RecordRow(label = "Study Period", value = "${student.studyStartYear} – ${student.studyEndYear ?: "[MISSING DATA]"}")
                            RecordRow(label = "Record Availability", value = student.recordStatus.label)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Button to export / view verification review report
                        Button(
                            onClick = { viewModel.setShowVerificationDialog(true) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("export_verification_report_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Navy800)
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Generate Verification-Review Report", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Statutory Verification Disclaimer Box
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Navy50,
                border = androidx.compose.foundation.BorderStroke(1.dp, LightOutline)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = Navy700, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Statutory Demonstration Disclaimer",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "This report summarizes records available in the APAAR OneView demonstration database. It is not an official APAAR identity verification or government-issued academic verification. Does not access government databases.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultStateBadge(state: String) {
    val (bgColor, textColor) = when (state) {
        "Demo record found" -> Pair(StatusSuccessBg, StatusSuccess)
        "Data validated" -> Pair(StatusSuccessBg, StatusSuccess)
        "Possible duplicate — review required" -> Pair(StatusWarningBg, StatusWarning)
        "Incomplete information — review required" -> Pair(StatusErrorBg, StatusError)
        else -> Pair(Navy50, TextMuted)
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = bgColor
    ) {
        Text(
            text = state,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
private fun RecordRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = TextMuted)
        Text(text = value, fontSize = 11.sp, color = Navy900, fontWeight = FontWeight.SemiBold)
    }
}
