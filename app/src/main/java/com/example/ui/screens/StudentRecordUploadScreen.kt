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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ApaarViewModel
import com.example.ui.theme.*

@Composable
fun StudentRecordUploadScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val csvContent by viewModel.csvContentInput.collectAsState()
    val fileName by viewModel.csvFileName.collectAsState()
    val parseResult by viewModel.csvParseResult.collectAsState()
    val lastBatch by viewModel.lastConfirmedBatch.collectAsState()
    val uploadBatches by viewModel.uploadBatches.collectAsState()

    var showTemplateDialog by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf(0) } // 0: Batch Staging & Validation, 1: Upload History

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Institution Record Upload",
                        color = Navy900,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Batch synchronization & validation for AICTE institutions",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                OutlinedButton(
                    onClick = { showTemplateDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CSV Schema", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Tab Row
        item {
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = LightSurface,
                contentColor = CyanPrimary
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = { Text("Upload & Validate", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = { Text("Upload History (${uploadBatches.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }
        }

        if (activeTab == 0) {
            // Success Banner if recently confirmed
            item {
                AnimatedVisibility(visible = lastBatch != null) {
                    lastBatch?.let { batch ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = StatusSuccessBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusSuccess)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Batch ${batch.id} Imported Successfully!",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = StatusSuccess
                                    )
                                    Text(
                                        text = "${batch.rowsAccepted} accepted into repository, ${batch.rowsFlagged} flagged for review.",
                                        fontSize = 11.sp,
                                        color = Navy900
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Staging Card
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
                            text = "Stage CSV Student Dataset",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Navy900
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Choose a sample batch or paste CSV text directly. Expected columns: student_id, demo_apaar_id, student_name, institution_id, institution_name, course, study_start_year, study_end_year, record_status.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick pre-fill buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(
                                onClick = { viewModel.loadPrebuiltBatch(withErrors = false) },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.testTag("load_valid_batch_btn")
                            ) {
                                Text("Load Valid Batch", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }

                            FilledTonalButton(
                                onClick = { viewModel.loadPrebuiltBatch(withErrors = true) },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.testTag("load_error_batch_btn")
                            ) {
                                Text("Load Batch with Collisions", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = csvContent,
                            onValueChange = { viewModel.updateCsvContent(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .testTag("csv_input_field"),
                            textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 11.sp),
                            shape = RoundedCornerShape(8.dp),
                            placeholder = { Text("Paste CSV rows here...") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Staging File: $fileName",
                            fontSize = 11.sp,
                            color = CyanDark,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Validation Results Preview
            parseResult?.let { result ->
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
                                Text(
                                    text = "Validation & Collision Summary",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Navy900
                                )

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (result.issues.isEmpty()) StatusSuccessBg else StatusWarningBg
                                ) {
                                    Text(
                                        text = if (result.issues.isEmpty()) "PASSED VALIDATION" else "${result.issues.size} ISSUES FLAGGED",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = if (result.issues.isEmpty()) StatusSuccess else StatusWarning,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // 4-stat chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                MiniStat(label = "Valid Rows", value = "${result.validRows.size}", color = StatusSuccess, modifier = Modifier.weight(1f))
                                MiniStat(label = "Duplicates", value = "${result.duplicateCount}", color = StatusWarning, modifier = Modifier.weight(1f))
                                MiniStat(label = "Missing Data", value = "${result.missingCount}", color = StatusError, modifier = Modifier.weight(1f))
                                MiniStat(label = "Total Rows", value = "${result.validRows.size + result.issues.count { it.severity == "Error" }}", color = Navy800, modifier = Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action buttons: Confirm vs Cancel
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.confirmUpload() },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .testTag("confirm_upload_button"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                                    enabled = result.validRows.isNotEmpty()
                                ) {
                                    Icon(Icons.Default.CloudDone, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Confirm & Commit Rows (${result.validRows.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { viewModel.updateCsvContent("") },
                                    modifier = Modifier.height(42.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Clear", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                // Row-by-Row Issue Details
                if (result.issues.isNotEmpty()) {
                    item {
                        Text(
                            text = "Validation Flags & Guidance (${result.issues.size})",
                            color = Navy900,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(result.issues) { issue ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (issue.severity == "Error") StatusError.copy(alpha = 0.5f) else StatusWarning.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                ),
                            color = LightSurface
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Row ${issue.rowNumber} • ${issue.field}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (issue.severity == "Error") StatusError else StatusWarning
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (issue.severity == "Error") StatusErrorBg else StatusWarningBg
                                    ) {
                                        Text(
                                            text = issue.severity.uppercase(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (issue.severity == "Error") StatusError else StatusWarning,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(text = issue.message, fontSize = 11.sp, color = Navy900)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Guidance: ${issue.suggestedFix}", fontSize = 10.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        } else {
            // Upload History Tab
            items(uploadBatches) { batch ->
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
                            Text(text = batch.id, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy900)
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "File: ${batch.fileName}", fontSize = 11.sp, color = CyanDark, fontWeight = FontWeight.Medium)
                        Text(
                            text = "Timestamp: ${batch.timestamp} • Uploader: ${batch.uploaderRole}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(color = LightOutline)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Processed: ${batch.rowsProcessed}", fontSize = 11.sp, color = TextSecondary)
                            Text(text = "Accepted: ${batch.rowsAccepted}", fontSize = 11.sp, color = StatusSuccess, fontWeight = FontWeight.Bold)
                            Text(text = "Flagged: ${batch.rowsFlagged}", fontSize = 11.sp, color = StatusWarning, fontWeight = FontWeight.Bold)
                            Text(text = "Rejected: ${batch.rowsRejected}", fontSize = 11.sp, color = StatusError, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Persistent Disclaimer Footer
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
                    Icon(Icons.Default.Security, contentDescription = null, tint = Navy700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Statutory Protocol: Successful CSV batch import updates local records only. Official identity accreditation requires authorized government integration.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }

    // CSV Schema Modal Dialog
    if (showTemplateDialog) {
        AlertDialog(
            onDismissRequest = { showTemplateDialog = false },
            title = {
                Text("Expected CSV Specification", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Navy900)
            },
            text = {
                Column {
                    Text(
                        text = "AICTE Student Record Schema Columns:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val columns = listOf(
                        "1. student_id (e.g. STU-DEMO-021)",
                        "2. demo_apaar_id (e.g. DEMO-APAAR-1021)",
                        "3. student_name (Full official name)",
                        "4. institution_id (e.g. INST-DEMO-001)",
                        "5. institution_name (Institution title)",
                        "6. course (e.g. B.Tech — Computer Science)",
                        "7. study_start_year (e.g. 2023)",
                        "8. study_end_year (e.g. 2027)",
                        "9. record_status (Optional status label)"
                    )
                    columns.forEach { col ->
                        Text(text = col, fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(vertical = 1.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTemplateDialog = false }) {
                    Text("Close Schema", fontWeight = FontWeight.Bold, color = CyanDark)
                }
            }
        )
    }
}

@Composable
private fun MiniStat(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 15.sp, color = color)
            Text(text = label, fontSize = 9.sp, color = TextSecondary, maxLines = 1)
        }
    }
}
