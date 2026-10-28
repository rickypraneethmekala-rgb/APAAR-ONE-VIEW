package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RecordStatus
import com.example.model.ReviewCaseStatus
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun ApaarLogo(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Emblem Icon Box
        Box(
            modifier = Modifier
                .size(if (compact) 36.dp else 44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Navy900)
                .border(1.dp, CyanBright.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = "APAAR OneView Emblem",
                tint = CyanBright,
                modifier = Modifier.size(if (compact) 22.dp else 26.dp)
            )
        }

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "APAAR ",
                    color = Navy900,
                    fontWeight = FontWeight.Black,
                    fontSize = if (compact) 16.sp else 19.sp,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "OneView",
                    color = CyanPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (compact) 16.sp else 19.sp
                )
            }
            if (!compact) {
                Text(
                    text = "AICTE Student Record Platform",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SyntheticDataBanner(
    currentRole: UserRole,
    onRoleSelect: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("synthetic_data_banner"),
        color = Navy900,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(StatusWarning)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "SYNTHETIC DEMO DATA",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = "Demonstration Platform — Non-Government Environment",
                        color = Navy100,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Text(
                    text = "Role: ${currentRole.label.split(" ").first()}",
                    color = CyanBright,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Quick Role Switcher Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                UserRole.values().forEach { role ->
                    val isSelected = role == currentRole
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onRoleSelect(role) }
                            .testTag("role_chip_${role.name.lowercase()}"),
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) CyanPrimary else Navy800,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Navy700)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 5.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = when (role) {
                                    UserRole.INSTITUTION_ADMIN -> Icons.Default.AdminPanelSettings
                                    UserRole.STUDENT -> Icons.Default.Person
                                    UserRole.AUTHORIZED_VERIFIER -> Icons.Default.VerifiedUser
                                },
                                contentDescription = role.label,
                                tint = if (isSelected) Color.White else Navy100,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (role) {
                                    UserRole.INSTITUTION_ADMIN -> "Admin"
                                    UserRole.STUDENT -> "Student"
                                    UserRole.AUTHORIZED_VERIFIER -> "Verifier"
                                },
                                color = if (isSelected) Color.White else Navy100,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: RecordStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (status) {
        RecordStatus.DEMO_RECORD_AVAILABLE -> Triple(StatusInfoBg, StatusInfo, Icons.Default.CheckCircle)
        RecordStatus.DATA_VALIDATED -> Triple(StatusSuccessBg, StatusSuccess, Icons.Default.Verified)
        RecordStatus.POSSIBLE_DUPLICATE -> Triple(StatusWarningBg, StatusWarning, Icons.Default.Warning)
        RecordStatus.INCOMPLETE_INFO -> Triple(StatusErrorBg, StatusError, Icons.Default.ErrorOutline)
    }

    Surface(
        modifier = modifier.testTag("status_badge_${status.name.lowercase()}"),
        shape = RoundedCornerShape(6.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = status.label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CaseStatusBadge(
    status: ReviewCaseStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        ReviewCaseStatus.OPEN -> Pair(StatusErrorBg, StatusError)
        ReviewCaseStatus.UNDER_REVIEW -> Pair(StatusWarningBg, StatusWarning)
        ReviewCaseStatus.RESOLVED -> Pair(StatusSuccessBg, StatusSuccess)
        ReviewCaseStatus.FALSE_POSITIVE -> Pair(Navy50, TextSecondary)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = bgColor
    ) {
        Text(
            text = status.label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun MetricStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    bgColor: Color = LightSurface,
    subText: String? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .border(1.dp, LightOutline, RoundedCornerShape(12.dp)),
        color = bgColor,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = Navy900,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )

            if (subText != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subText,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}
