package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.ApaarViewModel
import com.example.ui.components.ApaarLogo
import com.example.ui.theme.*

@Composable
fun WelcomeLoginScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Header Hero
        ApaarLogo(modifier = Modifier.padding(bottom = 12.dp))

        // Platform Badge
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CyanSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = CyanPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "AICTE Student Data Management & Verification",
                    color = CyanDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "APAAR ID-Based Student Data Management\n& Verification Platform",
            color = Navy900,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Centralized student academic-record review, structured batch uploads, duplicate detection, and verification for AICTE-approved institutions.",
            color = TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 17.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Prominent "Explore Demo / Continue as Demo" CTA
        Button(
            onClick = {
                viewModel.switchRole(UserRole.INSTITUTION_ADMIN)
                viewModel.navigateTo(AppScreen.DASHBOARD)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("explore_demo_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
        ) {
            Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Continue as Demo — Instant Access",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Unified App Tour & Guide Button
        OutlinedButton(
            onClick = {
                viewModel.navigateTo(AppScreen.GUIDED_TOUR)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("guided_tour_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy800)
        ) {
            Icon(Icons.Default.OndemandVideo, contentDescription = null, modifier = Modifier.size(18.dp), tint = CyanDark)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Watch App Tour & Complete Interactive Guide",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Role Selection Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = LightOutline)
            Text(
                text = "  OR SELECT DEMO ROLE  ",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = LightOutline)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Role Card: Institution Administrator
        RoleLoginCard(
            role = UserRole.INSTITUTION_ADMIN,
            icon = Icons.Default.AdminPanelSettings,
            accentColor = Navy800,
            tag = "role_admin_card",
            onSelect = {
                viewModel.switchRole(UserRole.INSTITUTION_ADMIN)
                viewModel.navigateTo(AppScreen.DASHBOARD)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Role Card: Student
        RoleLoginCard(
            role = UserRole.STUDENT,
            icon = Icons.Default.Person,
            accentColor = CyanPrimary,
            tag = "role_student_card",
            onSelect = {
                viewModel.switchRole(UserRole.STUDENT)
                viewModel.navigateTo(AppScreen.DASHBOARD)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Role Card: Authorized Verifier
        RoleLoginCard(
            role = UserRole.AUTHORIZED_VERIFIER,
            icon = Icons.Default.VerifiedUser,
            accentColor = StatusSuccess,
            tag = "role_verifier_card",
            onSelect = {
                viewModel.switchRole(UserRole.AUTHORIZED_VERIFIER)
                viewModel.navigateTo(AppScreen.SEARCH_VERIFICATION)
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Environment Notice Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Navy50,
            border = androidx.compose.foundation.BorderStroke(1.dp, LightOutline)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Demo Environment Notice",
                        color = Navy900,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Demo environment — use sample data only. Preloaded with 12 synthetic student records, deliberate collision test cases, and audit logs. No actual government database connections are established.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Problem -> Solution -> Outcome Summary
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = LightSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, LightOutline)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Platform Architecture & Workflow Synopsis",
                    color = Navy900,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                PsoRow(label = "Problem", desc = "Academic records can be fragmented, difficult to verify, and prone to duplicate entries across AICTE institutions.")
                Spacer(modifier = Modifier.height(6.dp))
                PsoRow(label = "Solution", desc = "Structured CSV batch uploads, automated duplicate detection, searchable academic history, and verifiable audit trails.")
                Spacer(modifier = Modifier.height(6.dp))
                PsoRow(label = "Outcome", desc = "A transparent, traceable, institution-friendly workflow preventing bogus enrollments while safeguarding student privacy.")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RoleLoginCard(
    role: UserRole,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    tag: String,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .border(1.dp, LightOutline, RoundedCornerShape(12.dp))
            .testTag(tag),
        color = LightSurface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = role.label,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = role.label,
                    color = Navy900,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = role.roleDescription,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Demo ID: ${role.email} • Password: Demo@123",
                    color = CyanDark,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Log in as ${role.label}",
                tint = TextMuted,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun PsoRow(label: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(
                    when (label) {
                        "Problem" -> StatusErrorBg
                        "Solution" -> StatusInfoBg
                        else -> StatusSuccessBg
                    }
                )
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = label,
                color = when (label) {
                    "Problem" -> StatusError
                    "Solution" -> StatusInfo
                    else -> StatusSuccess
                },
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = desc,
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
