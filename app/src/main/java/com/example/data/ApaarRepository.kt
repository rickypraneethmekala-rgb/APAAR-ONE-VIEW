package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class ApaarRepository {

    private val _currentRole = MutableStateFlow(UserRole.INSTITUTION_ADMIN)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _students = MutableStateFlow<List<StudentRecord>>(SampleData.getInitialStudents())
    val students: StateFlow<List<StudentRecord>> = _students.asStateFlow()

    private val _duplicateCases = MutableStateFlow<List<DuplicateReviewCase>>(SampleData.getInitialDuplicateCases())
    val duplicateCases: StateFlow<List<DuplicateReviewCase>> = _duplicateCases.asStateFlow()

    private val _uploadBatches = MutableStateFlow<List<UploadBatch>>(SampleData.getInitialUploadBatches())
    val uploadBatches: StateFlow<List<UploadBatch>> = _uploadBatches.asStateFlow()

    private val _activityLogs = MutableStateFlow<List<ActivityLogEntry>>(SampleData.getInitialActivityLogs())
    val activityLogs: StateFlow<List<ActivityLogEntry>> = _activityLogs.asStateFlow()

    val institutions: List<Institution> = SampleData.institutions

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm 'IST'", Locale.getDefault())

    private fun getCurrentTimestamp(): String = dateFormat.format(Date())

    fun switchRole(role: UserRole) {
        _currentRole.value = role
        addActivityLog(
            action = "Demo Role Switch",
            recordId = null,
            outcome = "Success",
            details = "Switched active persona to ${role.label} (${role.email})."
        )
    }

    fun resetDemoData() {
        _students.value = SampleData.getInitialStudents()
        _duplicateCases.value = SampleData.getInitialDuplicateCases()
        _uploadBatches.value = SampleData.getInitialUploadBatches()
        _activityLogs.value = SampleData.getInitialActivityLogs()
        addActivityLog(
            action = "Reset Demo Data",
            recordId = null,
            outcome = "Success",
            details = "Restored original synthetic dataset (12 records, 3 test cases) without affecting real systems."
        )
    }

    fun addActivityLog(action: String, recordId: String?, outcome: String, details: String) {
        val entry = ActivityLogEntry(
            id = "LOG-DEMO-${System.currentTimeMillis().toString().takeLast(6)}",
            timestamp = getCurrentTimestamp(),
            userRole = _currentRole.value.label,
            action = action,
            relatedRecordId = recordId,
            outcome = outcome,
            details = details
        )
        _activityLogs.value = listOf(entry) + _activityLogs.value
    }

    fun getStudentByDemoApaarId(apaarId: String): StudentRecord? {
        return _students.value.firstOrNull { it.demoApaarId.equals(apaarId.trim(), ignoreCase = true) }
    }

    fun getStudentById(studentId: String): StudentRecord? {
        return _students.value.firstOrNull { it.studentId.equals(studentId.trim(), ignoreCase = true) }
    }

    fun searchVerification(query: String): VerificationReport {
        val trimmed = query.trim()
        val allStudents = _students.value

        // Search matches
        val matches = allStudents.filter {
            it.demoApaarId.equals(trimmed, ignoreCase = true) ||
            it.studentId.equals(trimmed, ignoreCase = true) ||
            it.studentName.contains(trimmed, ignoreCase = true)
        }

        val timestamp = getCurrentTimestamp()
        val reportId = "VER-REP-${System.currentTimeMillis().toString().takeLast(6)}"

        if (matches.isEmpty()) {
            addActivityLog(
                action = "Verification Search",
                recordId = trimmed,
                outcome = "Warning",
                details = "Searched for identifier '$trimmed': No matching demo records in prototype database."
            )
            return VerificationReport(
                id = reportId,
                timestamp = timestamp,
                searchInput = trimmed,
                searchType = if (trimmed.startsWith("DEMO-APAAR", ignoreCase = true)) "Demo APAAR ID" else "Student ID / Name",
                studentSummary = null,
                resultState = "No matching demo record",
                matchExplanation = "Query '$trimmed' returned 0 records from the demonstration repository. No AICTE-affiliated demo records match this identifier.",
                duplicateWarnings = emptyList(),
                missingFieldWarnings = emptyList(),
                generatedBy = _currentRole.value.label
            )
        }

        // Check if multiple records share the same demo APAAR ID
        val apaarGroup = matches.groupBy { it.demoApaarId }
        val primaryRecord = matches.first()
        val sameApaarRecords = allStudents.filter { it.demoApaarId.equals(primaryRecord.demoApaarId, ignoreCase = true) }

        val duplicateWarnings = mutableListOf<String>()
        val missingWarnings = mutableListOf<String>()

        if (sameApaarRecords.size > 1) {
            val instNames = sameApaarRecords.map { it.institutionName }.distinct()
            duplicateWarnings.add("Found ${sameApaarRecords.size} records sharing Demo APAAR ID '${primaryRecord.demoApaarId}' across institutions: ${instNames.joinToString(", ")}.")
        }

        if (primaryRecord.studyEndYear.isNullOrBlank()) {
            missingWarnings.add("Study period end year is missing or unverified.")
        }
        if (primaryRecord.flags.isNotEmpty()) {
            missingWarnings.addAll(primaryRecord.flags)
        }

        val resultState = when {
            duplicateWarnings.isNotEmpty() -> "Possible duplicate — review required"
            missingWarnings.isNotEmpty() -> "Incomplete information — review required"
            primaryRecord.recordStatus == RecordStatus.DATA_VALIDATED -> "Data validated"
            else -> "Demo record found"
        }

        addActivityLog(
            action = "Verification Search",
            recordId = primaryRecord.demoApaarId,
            outcome = if (duplicateWarnings.isNotEmpty() || missingWarnings.isNotEmpty()) "Flagged" else "Success",
            details = "Searched '$trimmed' -> Result: $resultState (${matches.size} records evaluated)."
        )

        return VerificationReport(
            id = reportId,
            timestamp = timestamp,
            searchInput = trimmed,
            searchType = if (trimmed.startsWith("DEMO-APAAR", ignoreCase = true)) "Demo APAAR ID" else "Student Identifier",
            studentSummary = primaryRecord,
            resultState = resultState,
            matchExplanation = "Evaluation produced $resultState based on prototype database consistency rules. Record origin: ${primaryRecord.institutionName}.",
            duplicateWarnings = duplicateWarnings,
            missingFieldWarnings = missingWarnings,
            generatedBy = _currentRole.value.label
        )
    }

    data class CsvParseResult(
        val validRows: List<StudentRecord>,
        val issues: List<ValidationIssue>,
        val duplicateCount: Int,
        val missingCount: Int
    )

    fun validateCsvContent(csvContent: String): CsvParseResult {
        val lines = csvContent.lines().map { it.trim() }.filter { it.isNotEmpty() }
        val issues = mutableListOf<ValidationIssue>()
        val validRows = mutableListOf<StudentRecord>()
        var duplicateCount = 0
        var missingCount = 0

        if (lines.isEmpty()) {
            return CsvParseResult(emptyList(), emptyList(), 0, 0)
        }

        val header = lines.first().lowercase()
        val dataLines = lines.drop(1)

        val existingApaarIds = _students.value.map { it.demoApaarId.uppercase() }.toMutableSet()
        val seenBatchApaarIds = mutableSetOf<String>()

        dataLines.forEachIndexed { index, line ->
            val rowNum = index + 2
            val cols = line.split(",").map { it.trim() }

            if (cols.size < 8) {
                issues.add(
                    ValidationIssue(
                        rowNumber = rowNum,
                        studentId = cols.getOrNull(0) ?: "UNKNOWN",
                        apaarId = cols.getOrNull(1) ?: "UNKNOWN",
                        field = "Row Format",
                        severity = "Error",
                        message = "Malformed row: expected at least 8 comma-separated columns, found ${cols.size}.",
                        suggestedFix = "Ensure all 8 required columns are present in CSV format."
                    )
                )
                missingCount++
                return@forEachIndexed
            }

            val studentId = cols.getOrElse(0) { "" }
            val apaarId = cols.getOrElse(1) { "" }
            val studentName = cols.getOrElse(2) { "" }
            val instId = cols.getOrElse(3) { "" }
            val instName = cols.getOrElse(4) { "" }
            val course = cols.getOrElse(5) { "" }
            val startYear = cols.getOrElse(6) { "" }
            val endYear = cols.getOrNull(7)?.takeIf { it.isNotBlank() }
            val statusStr = cols.getOrNull(8) ?: "Demo record available"

            // Validation checks
            if (studentId.isBlank()) {
                issues.add(
                    ValidationIssue(
                        rowNumber = rowNum,
                        studentId = "BLANK",
                        apaarId = apaarId,
                        field = "student_id",
                        severity = "Error",
                        message = "Blank required field: student_id cannot be empty.",
                        suggestedFix = "Supply a unique student registration identifier."
                    )
                )
                missingCount++
            }

            if (apaarId.isBlank()) {
                issues.add(
                    ValidationIssue(
                        rowNumber = rowNum,
                        studentId = studentId,
                        apaarId = "BLANK",
                        field = "demo_apaar_id",
                        severity = "Error",
                        message = "Blank required field: demo_apaar_id is mandatory.",
                        suggestedFix = "Provide a valid placeholder format (e.g. DEMO-APAAR-XXXX)."
                    )
                )
                missingCount++
            } else {
                val upperApaar = apaarId.uppercase()
                if (existingApaarIds.contains(upperApaar) || seenBatchApaarIds.contains(upperApaar)) {
                    issues.add(
                        ValidationIssue(
                            rowNumber = rowNum,
                            studentId = studentId,
                            apaarId = apaarId,
                            field = "demo_apaar_id",
                            severity = "Flagged",
                            message = "Repeated Demo APAAR ID detected in database or batch ($apaarId).",
                            suggestedFix = "Review for cross-institution duplicate enrollment before saving."
                        )
                    )
                    duplicateCount++
                }
                seenBatchApaarIds.add(upperApaar)
            }

            if (studentName.isBlank()) {
                issues.add(
                    ValidationIssue(
                        rowNumber = rowNum,
                        studentId = studentId,
                        apaarId = apaarId,
                        field = "student_name",
                        severity = "Error",
                        message = "Blank student_name field.",
                        suggestedFix = "Provide the candidate's official enrollment name."
                    )
                )
                missingCount++
            }

            if (endYear == null) {
                issues.add(
                    ValidationIssue(
                        rowNumber = rowNum,
                        studentId = studentId,
                        apaarId = apaarId,
                        field = "study_end_year",
                        severity = "Warning",
                        message = "Missing expected completion/graduation year.",
                        suggestedFix = "Specify the projected graduation year (e.g. 2027)."
                    )
                )
                missingCount++
            }

            val validInst = institutions.any { it.id.equals(instId, ignoreCase = true) }
            if (!validInst && instId.isNotBlank()) {
                issues.add(
                    ValidationIssue(
                        rowNumber = rowNum,
                        studentId = studentId,
                        apaarId = apaarId,
                        field = "institution_id",
                        severity = "Warning",
                        message = "Institution ID '$instId' not in AICTE approved demo roster.",
                        suggestedFix = "Map to approved codes: INST-DEMO-001, INST-DEMO-002, or INST-DEMO-003."
                    )
                )
            }

            val recordStatus = when {
                issues.any { it.rowNumber == rowNum && it.severity == "Flagged" } -> RecordStatus.POSSIBLE_DUPLICATE
                endYear == null -> RecordStatus.INCOMPLETE_INFO
                else -> RecordStatus.DEMO_RECORD_AVAILABLE
            }

            if (studentId.isNotBlank() && apaarId.isNotBlank() && studentName.isNotBlank()) {
                validRows.add(
                    StudentRecord(
                        studentId = studentId,
                        demoApaarId = apaarId,
                        studentName = studentName,
                        institutionId = if (instId.isNotBlank()) instId else "INST-DEMO-001",
                        institutionName = if (instName.isNotBlank()) instName else "ABC Institute of Technology",
                        course = course.ifBlank { "B.Tech — General Engineering" },
                        studyStartYear = startYear.ifBlank { "2024" },
                        studyEndYear = endYear,
                        recordStatus = recordStatus,
                        lastUpdated = getCurrentTimestamp(),
                        flags = issues.filter { it.rowNumber == rowNum }.map { it.message }
                    )
                )
            }
        }

        return CsvParseResult(validRows, issues, duplicateCount, missingCount)
    }

    fun confirmBatchUpload(fileName: String, parseResult: CsvParseResult): UploadBatch {
        val timestamp = getCurrentTimestamp()
        val batchId = "BATCH-DEMO-${System.currentTimeMillis().toString().takeLast(4)}"
        val acceptedCount = parseResult.validRows.size
        val flaggedCount = parseResult.issues.count { it.severity == "Flagged" || it.severity == "Warning" }
        val rejectedCount = parseResult.issues.count { it.severity == "Error" }

        val batch = UploadBatch(
            id = batchId,
            timestamp = timestamp,
            fileName = fileName,
            uploaderRole = _currentRole.value.label,
            institutionId = "INST-DEMO-001",
            rowsProcessed = acceptedCount + rejectedCount,
            rowsAccepted = acceptedCount,
            rowsFlagged = flaggedCount,
            rowsRejected = rejectedCount,
            status = if (flaggedCount > 0 || rejectedCount > 0) "Completed with Warnings" else "Completed"
        )

        // Append students
        _students.value = _students.value + parseResult.validRows
        _uploadBatches.value = listOf(batch) + _uploadBatches.value

        // Check for new duplicate cases
        val newlyFlaggedDuplicates = parseResult.issues.filter { it.severity == "Flagged" }
        if (newlyFlaggedDuplicates.isNotEmpty()) {
            newlyFlaggedDuplicates.forEachIndexed { i, issue ->
                val newCase = DuplicateReviewCase(
                    id = "DUP-NEW-${System.currentTimeMillis().toString().takeLast(3)}-$i",
                    apaarId = issue.apaarId,
                    studentName = parseResult.validRows.firstOrNull { it.demoApaarId == issue.apaarId }?.studentName ?: "Student",
                    affectedRecordIds = listOf(issue.studentId),
                    affectedInstitutions = listOf("Uploaded Batch: $fileName"),
                    reason = issue.message,
                    severity = "Possible duplicate",
                    status = ReviewCaseStatus.OPEN,
                    reviewer = null,
                    reviewNotes = "Generated automatically by batch upload validation.",
                    flaggedDate = timestamp,
                    updatedAt = timestamp
                )
                _duplicateCases.value = listOf(newCase) + _duplicateCases.value
            }
        }

        addActivityLog(
            action = "Batch Student Upload",
            recordId = batchId,
            outcome = if (flaggedCount > 0) "Warning" else "Success",
            details = "Uploaded $fileName: $acceptedCount accepted, $flaggedCount flagged, $rejectedCount rejected."
        )

        return batch
    }

    fun updateDuplicateCaseStatus(
        caseId: String,
        newStatus: ReviewCaseStatus,
        notes: String,
        reviewer: String
    ) {
        val timestamp = getCurrentTimestamp()
        _duplicateCases.value = _duplicateCases.value.map { case ->
            if (case.id == caseId) {
                case.copy(
                    status = newStatus,
                    reviewNotes = notes.ifBlank { case.reviewNotes },
                    reviewer = reviewer,
                    updatedAt = timestamp
                )
            } else {
                case
            }
        }

        addActivityLog(
            action = "Duplicate Case Triage",
            recordId = caseId,
            outcome = "Success",
            details = "Updated case $caseId status to '${newStatus.label}' by $reviewer. Notes: ${notes.take(40)}..."
        )
    }
}
