package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.StudentRecord
import com.example.model.VerificationReport
import com.example.ui.theme.*

@Composable
fun VerificationReportModal(
    report: VerificationReport?,
    onDismiss: () -> Unit
) {
    if (report == null) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, LightOutline, RoundedCornerShape(14.dp))
                .testTag("verification_report_modal"),
            color = LightSurface
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(scrollState)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ApaarLogo(compact = true)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = CyanSurface
                    ) {
                        Text(
                            text = "REPORT ${report.id}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = LightOutline)
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "STUDENT RECORD VERIFICATION SUMMARY",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Navy900,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Search Query: ${report.searchInput} (${report.searchType})", fontSize = 11.sp, color = TextSecondary)
                Text(text = "Timestamp: ${report.timestamp} • Evaluator: ${report.generatedBy}", fontSize = 10.sp, color = TextMuted)
                Text(text = "Result Evaluation: ${report.resultState}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanDark)

                Spacer(modifier = Modifier.height(10.dp))

                report.studentSummary?.let { student ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = Navy50
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "Candidate Name: ${student.studentName}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy900)
                            Text(text = "Student Roll / ID: ${student.studentId}", fontSize = 11.sp, color = TextSecondary)
                            Text(text = "Demo APAAR ID: ${student.demoApaarId}", fontSize = 11.sp, color = CyanDark, fontWeight = FontWeight.SemiBold)
                            Text(text = "Institution: ${student.institutionName}", fontSize = 11.sp, color = TextSecondary)
                            Text(text = "Course: ${student.course}", fontSize = 11.sp, color = TextSecondary)
                            Text(text = "Study Period: ${student.studyStartYear} – ${student.studyEndYear ?: "[INCOMPLETE]"}", fontSize = 11.sp, color = TextSecondary)
                            Text(text = "Status: ${student.recordStatus.label}", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }

                if (report.duplicateWarnings.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        color = StatusWarningBg
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "Collision Flags:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = StatusWarning)
                            report.duplicateWarnings.forEach { w ->
                                Text(text = "• $w", fontSize = 10.sp, color = Navy900)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // MANDATORY DISCLAIMER
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Navy900
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "STATUTORY DISCLAIMER",
                            color = CyanBright,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "This report summarizes records available in the APAAR OneView demonstration database. It is not an official APAAR identity verification or government-issued academic verification.",
                            color = Color.White,
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                    ) {
                        Text("Close Report", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AcademicSummaryModal(
    student: StudentRecord?,
    onDismiss: () -> Unit
) {
    if (student == null) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, LightOutline, RoundedCornerShape(14.dp))
                .testTag("academic_summary_modal"),
            color = LightSurface
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(scrollState)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ApaarLogo(compact = true)
                    Text(
                        text = "ACADEMIC SUMMARY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = LightOutline)
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = student.studentName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Navy900
                )
                Text(
                    text = "${student.course} • ${student.institutionName}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Demo APAAR ID: ${student.demoApaarId} • Student ID: ${student.studentId}",
                    fontSize = 11.sp,
                    color = CyanDark,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Validated Semester Transcripts",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )
                Spacer(modifier = Modifier.height(6.dp))

                student.semesters.forEach { semester ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = Navy50
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = semester.academicTerm, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Navy900)
                                Text(text = "SGPA: ${semester.gpa}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = StatusSuccess)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            semester.subjects.forEach { s ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "${s.code} ${s.title}", fontSize = 10.sp, color = TextSecondary, modifier = Modifier.weight(1f))
                                    Text(text = "${s.grade} (${s.credits} cr)", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Navy900)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    color = Navy900
                ) {
                    Text(
                        text = "DEMO SAMPLE DATA — Fictional student records for demonstration and verification review. Not an official academic degree or transcript.",
                        color = Color.White,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                ) {
                    Text("Close Academic Summary", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
