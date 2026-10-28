package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RecordStatus
import com.example.model.StudentRecord
import com.example.ui.ApaarViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun StudentDirectoryScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val students by viewModel.students.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<RecordStatus?>(null) }
    var selectedInstitutionId by remember { mutableStateOf<String?>(null) }

    val filteredList = students.filter { student ->
        val matchesSearch = searchQuery.isBlank() ||
                student.studentName.contains(searchQuery, ignoreCase = true) ||
                student.studentId.contains(searchQuery, ignoreCase = true) ||
                student.demoApaarId.contains(searchQuery, ignoreCase = true) ||
                student.course.contains(searchQuery, ignoreCase = true)

        val matchesStatus = selectedStatus == null || student.recordStatus == selectedStatus
        val matchesInst = selectedInstitutionId == null || student.institutionId == selectedInstitutionId

        matchesSearch && matchesStatus && matchesInst
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Student Academic Directory",
                    color = Navy900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Consolidated demo student roster across AICTE institutions (${students.size} records)",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("directory_search_bar"),
                placeholder = { Text("Filter by student name, ID, course, or APAAR ID...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CyanDark) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
        }

        // Status Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("All Statuses", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedStatus == RecordStatus.DATA_VALIDATED,
                    onClick = { selectedStatus = RecordStatus.DATA_VALIDATED },
                    label = { Text("Validated", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedStatus == RecordStatus.POSSIBLE_DUPLICATE,
                    onClick = { selectedStatus = RecordStatus.POSSIBLE_DUPLICATE },
                    label = { Text("Duplicates", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedStatus == RecordStatus.INCOMPLETE_INFO,
                    onClick = { selectedStatus = RecordStatus.INCOMPLETE_INFO },
                    label = { Text("Incomplete", fontSize = 11.sp) }
                )
            }
        }

        // Institution Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedInstitutionId == null,
                    onClick = { selectedInstitutionId = null },
                    label = { Text("All Colleges", fontSize = 10.sp) }
                )
                FilterChip(
                    selected = selectedInstitutionId == "INST-DEMO-001",
                    onClick = { selectedInstitutionId = "INST-DEMO-001" },
                    label = { Text("ABC Tech", fontSize = 10.sp) }
                )
                FilterChip(
                    selected = selectedInstitutionId == "INST-DEMO-002",
                    onClick = { selectedInstitutionId = "INST-DEMO-002" },
                    label = { Text("Sunrise", fontSize = 10.sp) }
                )
                FilterChip(
                    selected = selectedInstitutionId == "INST-DEMO-003",
                    onClick = { selectedInstitutionId = "INST-DEMO-003" },
                    label = { Text("Greenfield", fontSize = 10.sp) }
                )
            }
        }

        // List count label
        item {
            Text(
                text = "Showing ${filteredList.size} of ${students.size} records",
                fontSize = 11.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
        }

        // Student Card Items
        items(filteredList) { student ->
            StudentDirectoryCard(
                student = student,
                onClick = { viewModel.selectStudent(student) }
            )
        }
    }
}

@Composable
private fun StudentDirectoryCard(
    student: StudentRecord,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, LightOutline, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .testTag("student_card_${student.studentId.lowercase()}"),
        color = LightSurface,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = student.studentName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Navy900
                    )
                    Text(
                        text = "ID: ${student.studentId} • ${student.demoApaarId}",
                        fontSize = 11.sp,
                        color = CyanDark,
                        fontWeight = FontWeight.Medium
                    )
                }

                StatusBadge(status = student.recordStatus)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = student.course,
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${student.institutionName} • Study Period: ${student.studyStartYear}–${student.studyEndYear ?: "[MISSING]"}",
                fontSize = 10.sp,
                color = TextMuted
            )

            if (student.flags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = StatusWarningBg.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "Flag: ${student.flags.first()}",
                        fontSize = 10.sp,
                        color = StatusWarning,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
