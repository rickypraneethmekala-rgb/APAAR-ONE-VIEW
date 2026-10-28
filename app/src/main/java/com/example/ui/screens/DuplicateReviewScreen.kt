package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.model.DuplicateReviewCase
import com.example.model.ReviewCaseStatus
import com.example.ui.ApaarViewModel
import com.example.ui.components.CaseStatusBadge
import com.example.ui.theme.*

@Composable
fun DuplicateReviewScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val cases by viewModel.duplicateCases.collectAsState()
    val selectedCase by viewModel.selectedDuplicateCase.collectAsState()

    var statusFilter by remember { mutableStateOf<ReviewCaseStatus?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var activeEditingCase by remember { mutableStateOf<DuplicateReviewCase?>(null) }

    val filteredCases = if (statusFilter == null) cases else cases.filter { it.status == statusFilter }

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
                    text = "Duplicate & Mismatch Triage",
                    color = Navy900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Rule-based collision resolution for AICTE institution administrators",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Triage Guidelines Notice
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
                    Icon(Icons.Default.Balance, contentDescription = null, tint = Navy700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Due-Process Protocol: Duplicates are flagged for coordinator review. Records are never automatically deleted and candidates are never marked fraudulent solely due to data collisions.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Status Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = statusFilter == null,
                    onClick = { statusFilter = null },
                    label = { Text("All (${cases.size})", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = statusFilter == ReviewCaseStatus.OPEN,
                    onClick = { statusFilter = ReviewCaseStatus.OPEN },
                    label = { Text("Open (${cases.count { it.status == ReviewCaseStatus.OPEN }})", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = statusFilter == ReviewCaseStatus.UNDER_REVIEW,
                    onClick = { statusFilter = ReviewCaseStatus.UNDER_REVIEW },
                    label = { Text("Reviewing", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = statusFilter == ReviewCaseStatus.RESOLVED,
                    onClick = { statusFilter = ReviewCaseStatus.RESOLVED },
                    label = { Text("Resolved", fontSize = 11.sp) }
                )
            }
        }

        // Case Cards List
        items(filteredCases) { case ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, LightOutline, RoundedCornerShape(12.dp))
                    .testTag("duplicate_case_card_${case.id.lowercase()}"),
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
                                text = case.studentName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Navy900
                            )
                            Text(
                                text = "Case ID: ${case.id} • ${case.apaarId}",
                                fontSize = 11.sp,
                                color = CyanDark,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        CaseStatusBadge(status = case.status)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Collision explanation
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        color = StatusWarningBg.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StatusWarning.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "Collision Reason:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusWarning
                            )
                            Text(
                                text = case.reason,
                                fontSize = 11.sp,
                                color = Navy900,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Affected Records: ${case.affectedRecordIds.joinToString(", ")}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "Involved Institutions: ${case.affectedInstitutions.joinToString(", ")}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "Flagged: ${case.flaggedDate} • Updated: ${case.updatedAt}",
                        fontSize = 10.sp,
                        color = TextMuted
                    )

                    case.reviewNotes?.let { notes ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coordinator Notes: $notes (Reviewer: ${case.reviewer ?: "Admin"})",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Triage action button
                    OutlinedButton(
                        onClick = {
                            activeEditingCase = case
                            showEditDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("triage_case_btn_${case.id.lowercase()}"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Update Case Triage Status", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    // Triage Status Update Dialog
    if (showEditDialog && activeEditingCase != null) {
        val currentCase = activeEditingCase!!
        var selectedStatus by remember { mutableStateOf(currentCase.status) }
        var reviewNoteText by remember { mutableStateOf(currentCase.reviewNotes ?: "") }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = "Update Case ${currentCase.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Navy900
                )
            },
            text = {
                Column {
                    Text(
                        text = "${currentCase.studentName} (${currentCase.apaarId})",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Select Triage Status:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Navy900)
                    Spacer(modifier = Modifier.height(4.dp))

                    ReviewCaseStatus.values().forEach { status ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedStatus = status }
                                .padding(vertical = 3.dp)
                        ) {
                            RadioButton(
                                selected = selectedStatus == status,
                                onClick = { selectedStatus = status }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = status.label, fontSize = 12.sp, color = Navy900)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Coordinator Explanation / Action Note:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Navy900)
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = reviewNoteText,
                        onValueChange = { reviewNoteText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Document reason for status change...") },
                        maxLines = 3,
                        shape = RoundedCornerShape(6.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateCaseStatus(currentCase.id, selectedStatus, reviewNoteText)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                ) {
                    Text("Save Status", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
