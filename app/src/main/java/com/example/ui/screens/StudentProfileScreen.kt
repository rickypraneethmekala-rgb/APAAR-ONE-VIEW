package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.model.AcademicSemester
import com.example.model.StudentRecord
import com.example.ui.ApaarViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun StudentProfileScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val selectedStudent by viewModel.selectedStudent.collectAsState()
    val allStudents by viewModel.students.collectAsState()

    val student = selectedStudent ?: allStudents.first()

    // Related records sharing same demo APAAR ID (for duplicate test cases)
    val relatedRecords = allStudents.filter {
        it.demoApaarId.equals(student.demoApaarId, ignoreCase = true) && it.studentId != student.studentId
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Profile Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, LightOutline, RoundedCornerShape(14.dp)),
                color = Navy900
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(CyanPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = student.studentName.split(" ").map { it.take(1) }.joinToString(""),
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = student.studentName,
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Roll / Student ID: ${student.studentId}",
                                color = Navy100,
                                fontSize = 12.sp
                            )
                        }

                        StatusBadge(status = student.recordStatus)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Navy700)
                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileRow(label = "Demo APAAR ID", value = student.demoApaarId, isCyan = true)
                    ProfileRow(label = "Affiliated Institution", value = student.institutionName)
                    ProfileRow(label = "Academic Degree / Course", value = student.course)
                    ProfileRow(label = "Study Period", value = "${student.studyStartYear} – ${student.studyEndYear ?: "[INCOMPLETE - MISSING END YEAR]"}")
                    ProfileRow(label = "Record Last Synchronized", value = student.lastUpdated)

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.setShowPrintSummaryDialog(true) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .testTag("open_print_summary_dialog_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("View Print-Friendly Academic Summary", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Warnings / Collisions Card (if applicable)
        if (student.flags.isNotEmpty() || relatedRecords.isNotEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = StatusWarningBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, StatusWarning.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Attention: Inconsistency / Collision Flagged",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = StatusWarning
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        student.flags.forEach { flag ->
                            Text(text = "• $flag", fontSize = 11.sp, color = Navy900, lineHeight = 15.sp)
                        }
                        if (relatedRecords.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Collision: Another row (${relatedRecords.first().studentId}) shares DEMO-APAAR ID '${student.demoApaarId}' at '${relatedRecords.first().institutionName}'.",
                                fontSize = 11.sp,
                                color = Navy900,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Distinction callout: Record available vs Officially verified
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Navy50,
                border = androidx.compose.foundation.BorderStroke(1.dp, LightOutline)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Verification Status Level Distinction:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Level 1: Record Available (Data exists in local AICTE demo repository)\n• Level 2: Data Validated (Passed structural CSV syntax and format constraints)\n• Level 3: Officially Verified (Requires future integration with live government DigiLocker/APAAR identity gateways)",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Academic Semesters & Subjects Timeline
        item {
            Text(
                text = "Academic History & Coursework Timeline",
                color = Navy900,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (student.semesters.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = LightSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightOutline)
                ) {
                    Text(
                        text = "No detailed semester transcript uploaded for this demo student yet. Basic enrollment metadata validated.",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        } else {
            items(student.semesters) { semester ->
                SemesterDetailCard(semester = semester)
            }
        }
    }
}

@Composable
private fun SemesterDetailCard(semester: AcademicSemester) {
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
                Column {
                    Text(
                        text = semester.academicTerm,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Navy900
                    )
                    Text(
                        text = "Credits: ${semester.creditsEarned} • Validated: ${semester.validatedTimestamp}",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = StatusSuccessBg
                ) {
                    Text(
                        text = "SGPA: ${semester.gpa}",
                        color = StatusSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = LightOutline)
            Spacer(modifier = Modifier.height(8.dp))

            // Subjects table
            semester.subjects.forEach { subject ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = subject.title, fontSize = 11.sp, color = Navy900, fontWeight = FontWeight.Medium)
                        Text(text = "Code: ${subject.code} • ${subject.credits} Credits", fontSize = 9.sp, color = TextMuted)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Grade: ${subject.grade} (${subject.score}%)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String, isCyan: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = Navy100)
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isCyan) CyanBright else Color.White
        )
    }
}
