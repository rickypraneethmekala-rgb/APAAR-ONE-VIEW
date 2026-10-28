package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ApaarViewModel
import com.example.ui.theme.*

data class OverlayTourStep(
    val stepIndex: Int,
    val title: String,
    val targetAreaName: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val description: String,
    val tip: String,
    val targetActionLabel: String? = null
)

@Composable
fun DashboardTourOverlay(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val showTour by viewModel.showDashboardTourOverlay.collectAsState()
    val activeStep by viewModel.dashboardTourStep.collectAsState()

    val tourSteps = listOf(
        OverlayTourStep(
            stepIndex = 0,
            title = "Instant Role Persona Switcher",
            targetAreaName = "Top Persona Bar (Admin • Student • Verifier)",
            icon = Icons.Default.SwitchAccount,
            description = "Switch between Institution Admin, Student, and Authorized Verifier roles anytime with a single tap to explore different perspectives without re-authenticating.",
            tip = "Quick Tip: Tap 'Student' to experience Rahul Kumar's view, or 'Verifier' for identity search.",
            targetActionLabel = "Next: Live Metrics"
        ),
        OverlayTourStep(
            stepIndex = 1,
            title = "Dynamic Repository Metrics",
            targetAreaName = "Institutional Overview Dashboard Cards",
            icon = Icons.Default.Analytics,
            description = "Every metric is calculated directly from the synthetic demo database in real-time: track 12 total records, missing study-end years, and cross-institutional collision cases.",
            tip = "Notice: Numbers dynamically update when you upload new batches or triage duplicate cases.",
            targetActionLabel = "Next: Action Center"
        ),
        OverlayTourStep(
            stepIndex = 2,
            title = "Direct Action Center",
            targetAreaName = "Upload Records & Search Student Buttons",
            icon = Icons.Default.FlashOn,
            description = "Stage structured CSV admissions batches with automated rule validation (blank fields, duplicate APAAR IDs) or perform instant verification checks on candidate IDs.",
            tip = "Try this: Tap 'Upload Records' to test the CSV collision validator using pre-built batches.",
            targetActionLabel = "Next: Duplicate Triage"
        ),
        OverlayTourStep(
            stepIndex = 3,
            title = "Rule-Based Collision Detection & Triage",
            targetAreaName = "Flagged Cases Requiring Review",
            icon = Icons.Default.Warning,
            description = "When an APAAR ID is shared across colleges (e.g. DEMO-APAAR-1002 in Sunrise College & Greenfield Institute), the system alerts coordinators without deleting records.",
            tip = "Coordinators can update triage statuses (Open, Under review, Resolved, False positive) with audit notes.",
            targetActionLabel = "Next: Audit Trail"
        ),
        OverlayTourStep(
            stepIndex = 4,
            title = "Tamper-Resistant Audit Trail",
            targetAreaName = "System Activity & Audit Logs",
            icon = Icons.Default.History,
            description = "Every single demo event — CSV upload, validation error, collision review, and verification search — is recorded chronologically in an immutable audit ledger.",
            tip = "Security rule: Normal users cannot edit or delete audit logs, preserving full traceability.",
            targetActionLabel = "Finish Tour & Explore"
        )
    )

    AnimatedVisibility(
        visible = showTour,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(250))
    ) {
        val currentStep = tourSteps.getOrElse(activeStep) { tourSteps.first() }

        // Pulsing border animation for spotlight effect
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val glowAlpha by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 0.95f,
            animationSpec = infiniteRepeatable(
                animation = tween(900, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glowAlpha"
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xDD050D1A)) // Dark semi-transparent scrim
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    // Prevent accidental background clicks from dismissing without intention
                }
                .testTag("dashboard_overlay_tour_scrim")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Indicator Banner
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Navy900.copy(alpha = 0.9f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanBright.copy(alpha = glowAlpha))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(CyanBright)
                        )
                        Text(
                            text = "INTERACTIVE DASHBOARD TOUR • STEP ${activeStep + 1} OF ${tourSteps.size}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Spotlight Preview / Target Area Focus Callout
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 2.dp,
                            color = CyanBright.copy(alpha = glowAlpha),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    color = Navy800.copy(alpha = 0.85f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Adjust,
                            contentDescription = null,
                            tint = CyanBright,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "FOCUSING ON DASHBOARD ELEMENT:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanBright
                            )
                            Text(
                                text = currentStep.targetAreaName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }

                // Main Interactive Coachmark Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, LightOutline, RoundedCornerShape(16.dp))
                        .testTag("dashboard_tour_card_step_$activeStep"),
                    color = LightSurface,
                    tonalElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        // Card Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(CyanSurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = currentStep.icon,
                                        contentDescription = null,
                                        tint = CyanDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = currentStep.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Navy900
                                    )
                                    Text(
                                        text = "Interactive Feature Guide",
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.dismissDashboardTour() },
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("dismiss_tour_overlay_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Tour",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = LightOutline)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Body Description
                        Text(
                            text = currentStep.description,
                            fontSize = 12.sp,
                            color = Navy900,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Pro-Tip Box
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = CyanSurface.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.25f))
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = CyanDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentStep.tip,
                                    fontSize = 11.sp,
                                    color = CyanDark,
                                    lineHeight = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress Dots
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            tourSteps.forEachIndexed { index, _ ->
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 3.dp)
                                        .size(if (index == activeStep) 16.dp else 7.dp, 7.dp)
                                        .clip(CircleShape)
                                        .background(if (index == activeStep) CyanPrimary else LightOutline)
                                        .clickable { viewModel.setDashboardTourStep(index) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Tour Action Controls
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { viewModel.prevDashboardTourStep() },
                                enabled = activeStep > 0,
                                modifier = Modifier.testTag("tour_prev_btn")
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Back", fontSize = 12.sp)
                            }

                            TextButton(
                                onClick = { viewModel.dismissDashboardTour() },
                                modifier = Modifier.testTag("tour_skip_btn")
                            ) {
                                Text("Skip Tour", fontSize = 12.sp, color = TextMuted)
                            }

                            Button(
                                onClick = { viewModel.nextDashboardTourStep() },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                                modifier = Modifier.testTag("tour_next_btn")
                            ) {
                                Text(
                                    text = if (activeStep < tourSteps.size - 1) "Next" else "Done",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = if (activeStep < tourSteps.size - 1) Icons.Default.ArrowForward else Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
