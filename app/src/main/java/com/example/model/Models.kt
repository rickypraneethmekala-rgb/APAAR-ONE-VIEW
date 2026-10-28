package com.example.model

enum class UserRole(val label: String, val email: String, val roleDescription: String) {
    INSTITUTION_ADMIN(
        label = "Institution Administrator",
        email = "admin@demo.apaaroneview.test",
        roleDescription = "Uploads student records, monitors validation errors & duplicate alerts, manages institutional student datasets."
    ),
    STUDENT(
        label = "Student",
        email = "rahul@demo.apaaroneview.test",
        roleDescription = "Views personal academic history, credit timeline, status notices, and reviews data consistency."
    ),
    AUTHORIZED_VERIFIER(
        label = "Authorized Verifier",
        email = "verifier@demo.apaaroneview.test",
        roleDescription = "Searches by APAAR / Student ID, inspects validation flags, and generates verification-review reports."
    )
}

enum class RecordStatus(val label: String) {
    DEMO_RECORD_AVAILABLE("Demo record available"),
    DATA_VALIDATED("Data validated"),
    POSSIBLE_DUPLICATE("Possible duplicate — review required"),
    INCOMPLETE_INFO("Incomplete information — review required")
}

data class Institution(
    val id: String,
    val code: String,
    val name: String,
    val location: String,
    val coordinatorEmail: String,
    val activeBatches: Int = 4,
    val isDemoOnly: Boolean = true
)

data class SubjectGrade(
    val code: String,
    val title: String,
    val credits: Int,
    val grade: String,
    val score: Int
)

data class AcademicSemester(
    val semesterNumber: Int,
    val academicTerm: String,
    val gpa: Double,
    val creditsEarned: Int,
    val subjects: List<SubjectGrade>,
    val validatedTimestamp: String
)

data class StudentRecord(
    val studentId: String,
    val demoApaarId: String,
    val studentName: String,
    val institutionId: String,
    val institutionName: String,
    val course: String,
    val studyStartYear: String,
    val studyEndYear: String?, // Nullable for deliberate test case 2
    val recordStatus: RecordStatus,
    val lastUpdated: String,
    val semesters: List<AcademicSemester> = emptyList(),
    val flags: List<String> = emptyList()
)

enum class ReviewCaseStatus(val label: String) {
    OPEN("Open"),
    UNDER_REVIEW("Under review"),
    RESOLVED("Resolved"),
    FALSE_POSITIVE("False positive")
}

data class DuplicateReviewCase(
    val id: String,
    val apaarId: String,
    val studentName: String,
    val affectedRecordIds: List<String>,
    val affectedInstitutions: List<String>,
    val reason: String,
    val severity: String, // "Possible duplicate", "Inconsistent details", "Incomplete"
    var status: ReviewCaseStatus,
    var reviewer: String?,
    var reviewNotes: String?,
    val flaggedDate: String,
    var updatedAt: String
)

data class ValidationIssue(
    val rowNumber: Int,
    val studentId: String,
    val apaarId: String,
    val field: String,
    val severity: String, // "Error", "Warning", "Flagged"
    val message: String,
    val suggestedFix: String
)

data class UploadBatch(
    val id: String,
    val timestamp: String,
    val fileName: String,
    val uploaderRole: String,
    val institutionId: String,
    val rowsProcessed: Int,
    val rowsAccepted: Int,
    val rowsFlagged: Int,
    val rowsRejected: Int,
    val status: String // "Completed", "Completed with Warnings", "Failed"
)

data class ActivityLogEntry(
    val id: String,
    val timestamp: String,
    val userRole: String,
    val action: String,
    val relatedRecordId: String?,
    val outcome: String, // "Success", "Flagged", "Warning", "Error"
    val details: String
)

data class VerificationReport(
    val id: String,
    val timestamp: String,
    val searchInput: String,
    val searchType: String,
    val studentSummary: StudentRecord?,
    val resultState: String, // "Demo record found", "No matching demo record", "Possible duplicate — review required", "Incomplete information — review required"
    val matchExplanation: String,
    val duplicateWarnings: List<String>,
    val missingFieldWarnings: List<String>,
    val generatedBy: String
)
