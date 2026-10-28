package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ApaarRepository
import com.example.data.SampleData
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class AppScreen(val title: String, val iconName: String) {
    DASHBOARD("Dashboard", "Dashboard"),
    STUDENT_DIRECTORY("Student Directory", "FolderShared"),
    STUDENT_PROFILE("Academic Profile", "School"),
    RECORD_UPLOAD("Upload Records", "CloudUpload"),
    SEARCH_VERIFICATION("Search & Verify", "VerifiedUser"),
    DUPLICATE_REVIEW("Duplicate Review", "Warning"),
    ACTIVITY_LOGS("Activity Logs", "History"),
    INSTITUTION_MANAGEMENT("Institutions", "Apartment"),
    REPORTS("Reports", "Description"),
    GUIDED_TOUR("App Tour & Guide", "OndemandVideo"),
    SETTINGS("Settings & Reset", "Settings")
}

data class DashboardMetrics(
    val totalRecords: Int,
    val missingFieldsCount: Int,
    val possibleDuplicatesCount: Int,
    val validatedCount: Int,
    val totalInstitutions: Int,
    val recentUploadsCount: Int
)

class ApaarViewModel(
    val repository: ApaarRepository = ApaarRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("DEMO-APAAR-1001")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchReport = MutableStateFlow<VerificationReport?>(null)
    val searchReport: StateFlow<VerificationReport?> = _searchReport.asStateFlow()

    private val _selectedStudent = MutableStateFlow<StudentRecord?>(null)
    val selectedStudent: StateFlow<StudentRecord?> = _selectedStudent.asStateFlow()

    private val _guidedTourStep = MutableStateFlow(1)
    val guidedTourStep: StateFlow<Int> = _guidedTourStep.asStateFlow()

    private val _showResetDialog = MutableStateFlow(false)
    val showResetDialog: StateFlow<Boolean> = _showResetDialog.asStateFlow()

    private val _showVerificationDialog = MutableStateFlow(false)
    val showVerificationDialog: StateFlow<Boolean> = _showVerificationDialog.asStateFlow()

    private val _showPrintSummaryDialog = MutableStateFlow(false)
    val showPrintSummaryDialog: StateFlow<Boolean> = _showPrintSummaryDialog.asStateFlow()

    // Interactive Dashboard Overlay Tour (Shown for new users)
    private val _showDashboardTourOverlay = MutableStateFlow(true)
    val showDashboardTourOverlay: StateFlow<Boolean> = _showDashboardTourOverlay.asStateFlow()

    private val _dashboardTourStep = MutableStateFlow(0)
    val dashboardTourStep: StateFlow<Int> = _dashboardTourStep.asStateFlow()

    // CSV upload staging state
    private val _csvContentInput = MutableStateFlow(SampleData.SAMPLE_CSV_TEMPLATE)
    val csvContentInput: StateFlow<String> = _csvContentInput.asStateFlow()

    private val _csvFileName = MutableStateFlow("student_admissions_batch.csv")
    val csvFileName: StateFlow<String> = _csvFileName.asStateFlow()

    private val _csvParseResult = MutableStateFlow<ApaarRepository.CsvParseResult?>(null)
    val csvParseResult: StateFlow<ApaarRepository.CsvParseResult?> = _csvParseResult.asStateFlow()

    private val _lastConfirmedBatch = MutableStateFlow<UploadBatch?>(null)
    val lastConfirmedBatch: StateFlow<UploadBatch?> = _lastConfirmedBatch.asStateFlow()

    // Selected Duplicate Case for edit
    private val _selectedDuplicateCase = MutableStateFlow<DuplicateReviewCase?>(null)
    val selectedDuplicateCase: StateFlow<DuplicateReviewCase?> = _selectedDuplicateCase.asStateFlow()

    // Flows from repository
    val currentRole: StateFlow<UserRole> = repository.currentRole
    val students: StateFlow<List<StudentRecord>> = repository.students
    val duplicateCases: StateFlow<List<DuplicateReviewCase>> = repository.duplicateCases
    val uploadBatches: StateFlow<List<UploadBatch>> = repository.uploadBatches
    val activityLogs: StateFlow<List<ActivityLogEntry>> = repository.activityLogs
    val institutions: List<Institution> = repository.institutions

    // Dynamically calculated dashboard metrics based on real database state
    val dashboardMetrics: StateFlow<DashboardMetrics> = combine(
        students,
        duplicateCases,
        uploadBatches
    ) { studentList, dupCases, batches ->
        val missingCount = studentList.count { it.studyEndYear.isNullOrBlank() || it.recordStatus == RecordStatus.INCOMPLETE_INFO }
        val duplicatesCount = dupCases.count { it.status == ReviewCaseStatus.OPEN || it.status == ReviewCaseStatus.UNDER_REVIEW }
        val validatedCount = studentList.count { it.recordStatus == RecordStatus.DATA_VALIDATED }

        DashboardMetrics(
            totalRecords = studentList.size,
            missingFieldsCount = missingCount,
            possibleDuplicatesCount = duplicatesCount,
            validatedCount = validatedCount,
            totalInstitutions = institutions.size,
            recentUploadsCount = batches.size
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DashboardMetrics(12, 1, 2, 3, 3, 4)
    )

    init {
        // Pre-select Rahul Kumar for Student mode preview
        _selectedStudent.value = repository.getStudentByDemoApaarId("DEMO-APAAR-1001")
        // Initial parse of default template
        validateStagedCsv()
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun switchRole(role: UserRole) {
        repository.switchRole(role)
        if (role == UserRole.STUDENT) {
            _selectedStudent.value = repository.getStudentByDemoApaarId("DEMO-APAAR-1001")
            _currentScreen.value = AppScreen.DASHBOARD
        } else if (role == UserRole.AUTHORIZED_VERIFIER) {
            _currentScreen.value = AppScreen.SEARCH_VERIFICATION
        } else {
            _currentScreen.value = AppScreen.DASHBOARD
        }
    }

    fun setTourStep(step: Int) {
        _guidedTourStep.value = step.coerceIn(1, 9)
        when (step) {
            1 -> {
                switchRole(UserRole.INSTITUTION_ADMIN)
                _currentScreen.value = AppScreen.DASHBOARD
            }
            2 -> {
                switchRole(UserRole.INSTITUTION_ADMIN)
                _currentScreen.value = AppScreen.RECORD_UPLOAD
            }
            3 -> {
                loadPrebuiltBatch(withErrors = true)
                _currentScreen.value = AppScreen.RECORD_UPLOAD
            }
            4 -> {
                switchRole(UserRole.AUTHORIZED_VERIFIER)
                _searchQuery.value = "DEMO-APAAR-1001"
                performSearch()
                _currentScreen.value = AppScreen.SEARCH_VERIFICATION
            }
            5 -> {
                _selectedStudent.value = repository.getStudentByDemoApaarId("DEMO-APAAR-1001")
                _currentScreen.value = AppScreen.STUDENT_PROFILE
            }
            6 -> {
                switchRole(UserRole.AUTHORIZED_VERIFIER)
                _searchQuery.value = "DEMO-APAAR-1002"
                performSearch()
                _currentScreen.value = AppScreen.SEARCH_VERIFICATION
            }
            7 -> {
                switchRole(UserRole.INSTITUTION_ADMIN)
                _currentScreen.value = AppScreen.DUPLICATE_REVIEW
            }
            8 -> {
                _currentScreen.value = AppScreen.ACTIVITY_LOGS
            }
            9 -> {
                switchRole(UserRole.STUDENT)
                _currentScreen.value = AppScreen.DASHBOARD
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun performSearch() {
        if (_searchQuery.value.isNotBlank()) {
            _searchReport.value = repository.searchVerification(_searchQuery.value)
        }
    }

    fun selectStudent(student: StudentRecord) {
        _selectedStudent.value = student
        _currentScreen.value = AppScreen.STUDENT_PROFILE
    }

    fun updateCsvContent(text: String) {
        _csvContentInput.value = text
        validateStagedCsv()
    }

    fun loadPrebuiltBatch(withErrors: Boolean) {
        if (withErrors) {
            _csvFileName.value = "admissions_batch_with_collisions.csv"
            _csvContentInput.value = SampleData.SAMPLE_CSV_WITH_ERRORS
        } else {
            _csvFileName.value = "aicte_enrolments_verified.csv"
            _csvContentInput.value = SampleData.SAMPLE_CSV_TEMPLATE
        }
        validateStagedCsv()
    }

    fun validateStagedCsv() {
        val result = repository.validateCsvContent(_csvContentInput.value)
        _csvParseResult.value = result
    }

    fun confirmUpload() {
        val result = _csvParseResult.value ?: return
        if (result.validRows.isNotEmpty() || result.issues.isNotEmpty()) {
            val batch = repository.confirmBatchUpload(_csvFileName.value, result)
            _lastConfirmedBatch.value = batch
            // Reset staging
            _csvContentInput.value = SampleData.SAMPLE_CSV_TEMPLATE
            validateStagedCsv()
        }
    }

    fun selectDuplicateCase(case: DuplicateReviewCase?) {
        _selectedDuplicateCase.value = case
    }

    fun updateCaseStatus(caseId: String, status: ReviewCaseStatus, notes: String) {
        repository.updateDuplicateCaseStatus(
            caseId = caseId,
            newStatus = status,
            notes = notes,
            reviewer = repository.currentRole.value.email
        )
        _selectedDuplicateCase.value = null
    }

    fun setShowResetDialog(show: Boolean) {
        _showResetDialog.value = show
    }

    fun setShowVerificationDialog(show: Boolean) {
        _showVerificationDialog.value = show
    }

    fun setShowPrintSummaryDialog(show: Boolean) {
        _showPrintSummaryDialog.value = show
    }

    fun startDashboardTour() {
        _dashboardTourStep.value = 0
        _showDashboardTourOverlay.value = true
        _currentScreen.value = AppScreen.DASHBOARD
    }

    fun dismissDashboardTour() {
        _showDashboardTourOverlay.value = false
    }

    fun nextDashboardTourStep() {
        if (_dashboardTourStep.value < 4) {
            _dashboardTourStep.value += 1
        } else {
            _showDashboardTourOverlay.value = false
        }
    }

    fun prevDashboardTourStep() {
        if (_dashboardTourStep.value > 0) {
            _dashboardTourStep.value -= 1
        }
    }

    fun setDashboardTourStep(step: Int) {
        _dashboardTourStep.value = step.coerceIn(0, 4)
    }

    fun resetDemoData() {
        repository.resetDemoData()
        _selectedStudent.value = repository.getStudentByDemoApaarId("DEMO-APAAR-1001")
        _searchQuery.value = "DEMO-APAAR-1001"
        _searchReport.value = null
        _showResetDialog.value = false
        _csvContentInput.value = SampleData.SAMPLE_CSV_TEMPLATE
        validateStagedCsv()
    }
}
