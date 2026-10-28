package com.example.data

import com.example.model.*

object SampleData {

    val institutions = listOf(
        Institution(
            id = "INST-DEMO-001",
            code = "ABC-TECH",
            name = "ABC Institute of Technology",
            location = "Bengaluru, Karnataka",
            coordinatorEmail = "dean.academic@abctech.demo.test",
            activeBatches = 6
        ),
        Institution(
            id = "INST-DEMO-002",
            code = "SUNRISE-ENGG",
            name = "Sunrise College of Engineering",
            location = "Pune, Maharashtra",
            coordinatorEmail = "registrar@sunrise.demo.test",
            activeBatches = 4
        ),
        Institution(
            id = "INST-DEMO-003",
            code = "GREENFIELD-TECH",
            name = "Greenfield Institute of Technology",
            location = "Hyderabad, Telangana",
            coordinatorEmail = "controller.exams@greenfield.demo.test",
            activeBatches = 5
        )
    )

    fun getInitialStudents(): List<StudentRecord> = listOf(
        // 1. Rahul Kumar (Primary student profile)
        StudentRecord(
            studentId = "STU-DEMO-001",
            demoApaarId = "DEMO-APAAR-1001",
            studentName = "Rahul Kumar",
            institutionId = "INST-DEMO-001",
            institutionName = "ABC Institute of Technology",
            course = "B.Tech — Computer Science and Engineering",
            studyStartYear = "2023",
            studyEndYear = "2027",
            recordStatus = RecordStatus.DEMO_RECORD_AVAILABLE,
            lastUpdated = "2026-10-24 14:30 IST",
            flags = emptyList(),
            semesters = listOf(
                AcademicSemester(
                    semesterNumber = 1,
                    academicTerm = "Fall 2023 (Semester I)",
                    gpa = 8.85,
                    creditsEarned = 22,
                    validatedTimestamp = "2024-01-15 10:20 IST",
                    subjects = listOf(
                        SubjectGrade("CS101", "Introduction to Computing", 4, "A+", 92),
                        SubjectGrade("MA101", "Engineering Mathematics I", 4, "A", 86),
                        SubjectGrade("PH101", "Engineering Physics", 4, "A-", 81),
                        SubjectGrade("EC101", "Basic Electronics", 3, "A", 85),
                        SubjectGrade("HS101", "Technical Communication", 3, "A+", 94),
                        SubjectGrade("CS102", "Computing Laboratory", 4, "O", 98)
                    )
                ),
                AcademicSemester(
                    semesterNumber = 2,
                    academicTerm = "Spring 2024 (Semester II)",
                    gpa = 9.12,
                    creditsEarned = 24,
                    validatedTimestamp = "2024-06-20 11:45 IST",
                    subjects = listOf(
                        SubjectGrade("CS201", "Data Structures & Algorithms", 4, "O", 96),
                        SubjectGrade("CS202", "Digital Logic Design", 4, "A+", 91),
                        SubjectGrade("MA201", "Discrete Mathematics", 4, "A", 88),
                        SubjectGrade("CS203", "Object-Oriented Programming", 4, "O", 95),
                        SubjectGrade("EE101", "Electrical Science", 4, "A", 84),
                        SubjectGrade("CS204", "Data Structures Lab", 4, "O", 97)
                    )
                ),
                AcademicSemester(
                    semesterNumber = 3,
                    academicTerm = "Fall 2024 (Semester III)",
                    gpa = 8.94,
                    creditsEarned = 23,
                    validatedTimestamp = "2025-01-12 16:10 IST",
                    subjects = listOf(
                        SubjectGrade("CS301", "Database Management Systems", 4, "A+", 93),
                        SubjectGrade("CS302", "Computer Organization & Architecture", 4, "A", 87),
                        SubjectGrade("CS303", "Design & Analysis of Algorithms", 4, "A+", 90),
                        SubjectGrade("CS304", "Theory of Computation", 4, "B+", 78),
                        SubjectGrade("HS301", "Professional Ethics", 3, "A+", 92),
                        SubjectGrade("CS305", "RDBMS Laboratory", 4, "O", 95)
                    )
                )
            )
        ),

        // 2 & 3. Deliberate Duplicate Test Case (DEMO-APAAR-1002 across two institutions)
        StudentRecord(
            studentId = "STU-DEMO-002",
            demoApaarId = "DEMO-APAAR-1002",
            studentName = "Priya Sharma",
            institutionId = "INST-DEMO-002",
            institutionName = "Sunrise College of Engineering",
            course = "B.Tech — Electronics & Communication",
            studyStartYear = "2022",
            studyEndYear = "2026",
            recordStatus = RecordStatus.POSSIBLE_DUPLICATE,
            lastUpdated = "2026-10-22 09:15 IST",
            flags = listOf("Shared demo APAAR ID with STU-DEMO-003 at different institution")
        ),
        StudentRecord(
            studentId = "STU-DEMO-003",
            demoApaarId = "DEMO-APAAR-1002",
            studentName = "Priya Sharma",
            institutionId = "INST-DEMO-003",
            institutionName = "Greenfield Institute of Technology",
            course = "B.Tech — Electrical Engineering",
            studyStartYear = "2023",
            studyEndYear = "2027",
            recordStatus = RecordStatus.POSSIBLE_DUPLICATE,
            lastUpdated = "2026-10-23 11:40 IST",
            flags = listOf("Shared demo APAAR ID with STU-DEMO-002 at different institution")
        ),

        // 4. Deliberate Incomplete Test Case (Missing studyEndYear)
        StudentRecord(
            studentId = "STU-DEMO-004",
            demoApaarId = "DEMO-APAAR-1003",
            studentName = "Amit Patel",
            institutionId = "INST-DEMO-001",
            institutionName = "ABC Institute of Technology",
            course = "B.Tech — Mechanical Engineering",
            studyStartYear = "2021",
            studyEndYear = null, // Missing end year
            recordStatus = RecordStatus.INCOMPLETE_INFO,
            lastUpdated = "2026-10-18 16:00 IST",
            flags = listOf("Missing study period end year")
        ),

        // 5. Valid Student
        StudentRecord(
            studentId = "STU-DEMO-005",
            demoApaarId = "DEMO-APAAR-1004",
            studentName = "Ananya Roy",
            institutionId = "INST-DEMO-002",
            institutionName = "Sunrise College of Engineering",
            course = "B.Tech — Information Technology",
            studyStartYear = "2023",
            studyEndYear = "2027",
            recordStatus = RecordStatus.DATA_VALIDATED,
            lastUpdated = "2026-10-25 15:00 IST",
            flags = emptyList()
        ),

        // 6. Valid Student
        StudentRecord(
            studentId = "STU-DEMO-006",
            demoApaarId = "DEMO-APAAR-1005",
            studentName = "Vikram Singh",
            institutionId = "INST-DEMO-003",
            institutionName = "Greenfield Institute of Technology",
            course = "B.Tech — Civil Engineering",
            studyStartYear = "2022",
            studyEndYear = "2026",
            recordStatus = RecordStatus.DEMO_RECORD_AVAILABLE,
            lastUpdated = "2026-10-19 12:30 IST",
            flags = emptyList()
        ),

        // 7. Valid Postgraduate Student
        StudentRecord(
            studentId = "STU-DEMO-007",
            demoApaarId = "DEMO-APAAR-1006",
            studentName = "Sneha Reddy",
            institutionId = "INST-DEMO-001",
            institutionName = "ABC Institute of Technology",
            course = "M.Tech — Artificial Intelligence",
            studyStartYear = "2023",
            studyEndYear = "2025",
            recordStatus = RecordStatus.DATA_VALIDATED,
            lastUpdated = "2026-10-20 18:00 IST",
            flags = emptyList()
        ),

        // 8. Completed Student
        StudentRecord(
            studentId = "STU-DEMO-008",
            demoApaarId = "DEMO-APAAR-1007",
            studentName = "Rohan Verma",
            institutionId = "INST-DEMO-002",
            institutionName = "Sunrise College of Engineering",
            course = "B.Tech — Computer Science",
            studyStartYear = "2020",
            studyEndYear = "2024",
            recordStatus = RecordStatus.DEMO_RECORD_AVAILABLE,
            lastUpdated = "2026-10-15 10:10 IST",
            flags = emptyList()
        ),

        // 9. Deliberate Inconsistent Field Test Case
        StudentRecord(
            studentId = "STU-DEMO-009",
            demoApaarId = "DEMO-APAAR-1008",
            studentName = "Neha Gupta",
            institutionId = "INST-DEMO-001",
            institutionName = "ABC Institute of Technology",
            course = "B.Tech — Biotechnology",
            studyStartYear = "2022",
            studyEndYear = "2026",
            recordStatus = RecordStatus.POSSIBLE_DUPLICATE,
            lastUpdated = "2026-10-21 17:20 IST",
            flags = listOf("Institution code in legacy system (INST-DEMO-001X) differed from normalized ID")
        ),

        // 10. Valid Student
        StudentRecord(
            studentId = "STU-DEMO-010",
            demoApaarId = "DEMO-APAAR-1009",
            studentName = "Karthik Iyer",
            institutionId = "INST-DEMO-003",
            institutionName = "Greenfield Institute of Technology",
            course = "B.Tech — Data Science",
            studyStartYear = "2023",
            studyEndYear = "2027",
            recordStatus = RecordStatus.DEMO_RECORD_AVAILABLE,
            lastUpdated = "2026-10-22 13:45 IST",
            flags = emptyList()
        ),

        // 11. Valid Student
        StudentRecord(
            studentId = "STU-DEMO-011",
            demoApaarId = "DEMO-APAAR-1010",
            studentName = "Pooja Nair",
            institutionId = "INST-DEMO-002",
            institutionName = "Sunrise College of Engineering",
            course = "B.Tech — Chemical Engineering",
            studyStartYear = "2021",
            studyEndYear = "2025",
            recordStatus = RecordStatus.DATA_VALIDATED,
            lastUpdated = "2026-10-17 14:15 IST",
            flags = emptyList()
        ),

        // 12. Valid Student
        StudentRecord(
            studentId = "STU-DEMO-012",
            demoApaarId = "DEMO-APAAR-1011",
            studentName = "Devendra Joshi",
            institutionId = "INST-DEMO-001",
            institutionName = "ABC Institute of Technology",
            course = "B.Tech — Electronics & Instrumentation",
            studyStartYear = "2022",
            studyEndYear = "2026",
            recordStatus = RecordStatus.DEMO_RECORD_AVAILABLE,
            lastUpdated = "2026-10-26 11:00 IST",
            flags = emptyList()
        )
    )

    fun getInitialDuplicateCases(): List<DuplicateReviewCase> = listOf(
        DuplicateReviewCase(
            id = "DUP-DEMO-001",
            apaarId = "DEMO-APAAR-1002",
            studentName = "Priya Sharma",
            affectedRecordIds = listOf("STU-DEMO-002", "STU-DEMO-003"),
            affectedInstitutions = listOf("Sunrise College of Engineering", "Greenfield Institute of Technology"),
            reason = "Two uploaded rows share DEMO-APAAR-1002 but contain different institution details and conflicting study periods (2022–2026 vs 2023–2027).",
            severity = "Possible duplicate",
            status = ReviewCaseStatus.OPEN,
            reviewer = null,
            reviewNotes = "Automated cross-institutional collision alert triggered during batch synchronization. Both records preserved pending coordinator verification.",
            flaggedDate = "2026-10-23 11:42 IST",
            updatedAt = "2026-10-23 11:42 IST"
        ),
        DuplicateReviewCase(
            id = "DUP-DEMO-002",
            apaarId = "DEMO-APAAR-1003",
            studentName = "Amit Patel",
            affectedRecordIds = listOf("STU-DEMO-004"),
            affectedInstitutions = listOf("ABC Institute of Technology"),
            reason = "Study period end year is missing from the submitted academic record.",
            severity = "Incomplete",
            status = ReviewCaseStatus.UNDER_REVIEW,
            reviewer = "admin@demo.apaaroneview.test",
            reviewNotes = "Requested official course completion schedule from mechanical engineering department.",
            flaggedDate = "2026-10-18 16:05 IST",
            updatedAt = "2026-10-20 09:30 IST"
        ),
        DuplicateReviewCase(
            id = "DUP-DEMO-003",
            apaarId = "DEMO-APAAR-1008",
            studentName = "Neha Gupta",
            affectedRecordIds = listOf("STU-DEMO-009"),
            affectedInstitutions = listOf("ABC Institute of Technology"),
            reason = "Institution code syntax in legacy ERP import was INST-DEMO-001X instead of standard INST-DEMO-001.",
            severity = "Inconsistent details",
            status = ReviewCaseStatus.FALSE_POSITIVE,
            reviewer = "admin@demo.apaaroneview.test",
            reviewNotes = "Confirmed legacy department code mapping error; student is properly enrolled in ABC Institute of Technology Biotechnology division.",
            flaggedDate = "2026-10-21 17:25 IST",
            updatedAt = "2026-10-24 10:15 IST"
        )
    )

    fun getInitialUploadBatches(): List<UploadBatch> = listOf(
        UploadBatch(
            id = "BATCH-DEMO-004",
            timestamp = "2026-10-26 11:30 IST",
            fileName = "aicte_ug_enrolments_2026.csv",
            uploaderRole = "Institution Administrator",
            institutionId = "INST-DEMO-001",
            rowsProcessed = 15,
            rowsAccepted = 14,
            rowsFlagged = 1,
            rowsRejected = 0,
            status = "Completed with Warnings"
        ),
        UploadBatch(
            id = "BATCH-DEMO-003",
            timestamp = "2026-10-23 11:40 IST",
            fileName = "greenfield_fresh_admissions.csv",
            uploaderRole = "Institution Administrator",
            institutionId = "INST-DEMO-003",
            rowsProcessed = 20,
            rowsAccepted = 18,
            rowsFlagged = 2,
            rowsRejected = 0,
            status = "Completed with Warnings"
        ),
        UploadBatch(
            id = "BATCH-DEMO-002",
            timestamp = "2026-10-20 14:10 IST",
            fileName = "sunrise_ec_dept_records.csv",
            uploaderRole = "Institution Administrator",
            institutionId = "INST-DEMO-002",
            rowsProcessed = 25,
            rowsAccepted = 25,
            rowsFlagged = 0,
            rowsRejected = 0,
            status = "Completed"
        ),
        UploadBatch(
            id = "BATCH-DEMO-001",
            timestamp = "2026-10-15 09:00 IST",
            fileName = "initial_aicte_pilot_data.csv",
            uploaderRole = "System Seeder",
            institutionId = "INST-DEMO-001",
            rowsProcessed = 50,
            rowsAccepted = 48,
            rowsFlagged = 2,
            rowsRejected = 0,
            status = "Completed with Warnings"
        )
    )

    fun getInitialActivityLogs(): List<ActivityLogEntry> = listOf(
        ActivityLogEntry(
            id = "LOG-DEMO-001",
            timestamp = "2026-10-28 08:30 IST",
            userRole = "Authorized Verifier",
            action = "Student Verification Search",
            relatedRecordId = "DEMO-APAAR-1001",
            outcome = "Success",
            details = "Searched for Rahul Kumar (STU-DEMO-001); demo record verified with 3 validated semesters."
        ),
        ActivityLogEntry(
            id = "LOG-DEMO-002",
            timestamp = "2026-10-27 16:45 IST",
            userRole = "Institution Administrator",
            action = "Duplicate Review Status Update",
            relatedRecordId = "DUP-DEMO-003",
            outcome = "Flagged",
            details = "Marked case DUP-DEMO-003 (Neha Gupta) as False Positive after legacy institution code audit."
        ),
        ActivityLogEntry(
            id = "LOG-DEMO-003",
            timestamp = "2026-10-26 11:32 IST",
            userRole = "Institution Administrator",
            action = "Batch CSV Upload & Validation",
            relatedRecordId = "BATCH-DEMO-004",
            outcome = "Warning",
            details = "Uploaded aicte_ug_enrolments_2026.csv: 15 rows processed, 14 accepted, 1 flagged for incomplete end date."
        ),
        ActivityLogEntry(
            id = "LOG-DEMO-004",
            timestamp = "2026-10-25 10:15 IST",
            userRole = "Authorized Verifier",
            action = "Verification Report Export",
            relatedRecordId = "DEMO-APAAR-1001",
            outcome = "Success",
            details = "Generated printable Verification-Review Report with official demonstration disclaimer."
        ),
        ActivityLogEntry(
            id = "LOG-DEMO-005",
            timestamp = "2026-10-23 11:45 IST",
            userRole = "System Validator",
            action = "Automated Duplicate Detection",
            relatedRecordId = "DEMO-APAAR-1002",
            outcome = "Flagged",
            details = "Detected duplicate demo APAAR ID across Sunrise College (STU-DEMO-002) and Greenfield Institute (STU-DEMO-003)."
        ),
        ActivityLogEntry(
            id = "LOG-DEMO-006",
            timestamp = "2026-10-22 09:00 IST",
            userRole = "Student",
            action = "Demo Profile Access",
            relatedRecordId = "STU-DEMO-001",
            outcome = "Success",
            details = "Rahul Kumar accessed student academic timeline and verified credits summary."
        )
    )

    const val SAMPLE_CSV_TEMPLATE = """student_id,demo_apaar_id,student_name,institution_id,institution_name,course,study_start_year,study_end_year,record_status
STU-DEMO-021,DEMO-APAAR-1021,Aarav Nair,INST-DEMO-001,ABC Institute of Technology,B.Tech — Civil Engineering,2023,2027,Demo record available
STU-DEMO-022,DEMO-APAAR-1022,Meera Krishnan,INST-DEMO-002,Sunrise College of Engineering,B.Tech — Computer Science,2024,2028,Data validated
STU-DEMO-023,DEMO-APAAR-1023,Tanya Sengupta,INST-DEMO-003,Greenfield Institute of Technology,B.Tech — Chemical Engineering,2022,2026,Demo record available"""

    const val SAMPLE_CSV_WITH_ERRORS = """student_id,demo_apaar_id,student_name,institution_id,institution_name,course,study_start_year,study_end_year,record_status
STU-DEMO-031,DEMO-APAAR-1001,Impostor Test,INST-DEMO-002,Sunrise College of Engineering,B.Tech — Robotics,2023,2027,Demo record available
STU-DEMO-032,,Harsh Vardhan,INST-DEMO-001,ABC Institute of Technology,B.Tech — Civil Engineering,2022,2026,Demo record available
STU-DEMO-033,DEMO-APAAR-1033,Sunita Rao,INST-DEMO-999,Unknown Institute of Science,B.Tech — Data Science,2023,,Demo record available
STU-DEMO-034,DEMO-APAAR-1034,Siddharth Jain,INST-DEMO-003,Greenfield Institute of Technology,B.Tech — AI & ML,2023,2027,Data validated"""
}
