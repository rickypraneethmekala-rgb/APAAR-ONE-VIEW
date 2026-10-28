package com.example

import com.example.data.ApaarRepository
import com.example.data.SampleData
import com.example.model.RecordStatus
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: ApaarRepository

    @Before
    fun setUp() {
        repository = ApaarRepository()
    }

    @Test
    fun sampleData_containsRequiredSyntheticEntities() {
        val students = repository.students.value
        val institutions = repository.institutions

        assertTrue("Expected at least 12 students, found ${students.size}", students.size >= 12)
        assertEquals(3, institutions.size)

        val rahul = students.firstOrNull { it.demoApaarId == "DEMO-APAAR-1001" }
        assertNotNull(rahul)
        assertEquals("Rahul Kumar", rahul?.studentName)
        assertEquals("INST-DEMO-001", rahul?.institutionId)
        assertTrue("Rahul should have academic semesters", (rahul?.semesters?.size ?: 0) >= 3)
    }

    @Test
    fun searchVerification_detectsDuplicates() {
        val report = repository.searchVerification("DEMO-APAAR-1002")
        assertEquals("Possible duplicate — review required", report.resultState)
        assertTrue(report.duplicateWarnings.isNotEmpty())
    }

    @Test
    fun searchVerification_detectsIncompleteInformation() {
        val report = repository.searchVerification("DEMO-APAAR-1003")
        assertEquals("Incomplete information — review required", report.resultState)
        assertTrue(report.missingFieldWarnings.isNotEmpty())
    }

    @Test
    fun searchVerification_handlesNonExistentRecord() {
        val report = repository.searchVerification("DEMO-APAAR-9999")
        assertEquals("No matching demo record", report.resultState)
        assertNull(report.studentSummary)
    }

    @Test
    fun csvValidation_identifiesMalformedRowsAndDuplicates() {
        val parseResult = repository.validateCsvContent(SampleData.SAMPLE_CSV_WITH_ERRORS)
        assertTrue("Expected issues in error CSV", parseResult.issues.isNotEmpty())
        assertTrue("Expected duplicate or collision flag", parseResult.duplicateCount > 0)
    }

    @Test
    fun resetDemoData_restoresInitialState() {
        repository.resetDemoData()
        assertEquals(12, repository.students.value.size)
        assertEquals(3, repository.duplicateCases.value.size)
    }

    @Test
    fun dashboardTourOverlay_progressionAndDismiss() {
        val viewModel = com.example.ui.ApaarViewModel(repository)
        assertTrue("Tour overlay should be active initially for new users", viewModel.showDashboardTourOverlay.value)
        assertEquals(0, viewModel.dashboardTourStep.value)

        viewModel.nextDashboardTourStep()
        assertEquals(1, viewModel.dashboardTourStep.value)

        viewModel.prevDashboardTourStep()
        assertEquals(0, viewModel.dashboardTourStep.value)

        viewModel.setDashboardTourStep(4)
        assertEquals(4, viewModel.dashboardTourStep.value)

        // Advancing past final step dismisses tour
        viewModel.nextDashboardTourStep()
        assertFalse(viewModel.showDashboardTourOverlay.value)

        // Can be restarted
        viewModel.startDashboardTour()
        assertTrue(viewModel.showDashboardTourOverlay.value)
        assertEquals(0, viewModel.dashboardTourStep.value)

        viewModel.dismissDashboardTour()
        assertFalse(viewModel.showDashboardTourOverlay.value)
    }
}
