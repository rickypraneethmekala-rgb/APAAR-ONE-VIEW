package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.UserRole
import com.example.ui.ApaarViewModel
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val currentRole by viewModel.currentRole.collectAsState()
    val showResetDialog by viewModel.showResetDialog.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "System Settings & Demo Reset",
                    color = Navy900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Platform configuration, persona switcher, and sample data restoration",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Active Role Switcher Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, LightOutline, RoundedCornerShape(12.dp)),
                color = LightSurface
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Active Demonstration Persona",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Navy900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Switch between the three predefined demonstration roles:",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    UserRole.values().forEach { role ->
                        val isSelected = role == currentRole
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.switchRole(role) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = role.label,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) CyanDark else Navy900
                                )
                                Text(
                                    text = "${role.email} • Password: Demo@123",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // Reset Demo Data Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, StatusError.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                color = LightSurface
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, tint = StatusError, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reset Synthetic Demo Data",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = StatusError
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Restores the original 12 fictional student records, deliberate collision test cases (DEMO-APAAR-1002, DEMO-APAAR-1003, DEMO-APAAR-1008), 4 upload batches, and initial audit logs without affecting any real user data.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.setShowResetDialog(true) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("reset_demo_data_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                    ) {
                        Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset Demo Data to Initial State", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Platform Architecture & Features Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, LightOutline, RoundedCornerShape(12.dp)),
                color = LightSurface
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Platform Architecture & Specifications",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Navy900
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• System: APAAR ID-Based Student Data Management & Verification Platform for AICTE-Approved Institutions\n• Architecture: Native Android Jetpack Compose M3, MVVM Architecture, StateFlow Reactive Streams\n• Data Layer: Deterministic in-memory Repository with batch validation engine and rule-based collision triage\n• Security: Zero-trust input validation, role-based boundary enforcement, and immutable audit logs",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }

    // Confirmation Dialog for Reset Demo Data
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowResetDialog(false) },
            title = {
                Text("Confirm Demo Data Reset?", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy900)
            },
            text = {
                Text(
                    text = "This will restore the original 12 fictional student records and clear any newly uploaded batches. The initial test cases (including duplicate DEMO-APAAR-1002 and missing-field DEMO-APAAR-1003) will be restored.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.resetDemoData() },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError),
                    modifier = Modifier.testTag("confirm_reset_dialog_btn")
                ) {
                    Text("Confirm Reset", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowResetDialog(false) }) {
                    Text("Cancel")
                }
            }
        )
    }
}
