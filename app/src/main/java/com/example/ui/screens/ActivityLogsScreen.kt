package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ApaarViewModel
import com.example.ui.theme.*

@Composable
fun ActivityLogsScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.activityLogs.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedOutcome by remember { mutableStateOf<String?>(null) }

    val filteredLogs = logs.filter { log ->
        val matchesSearch = searchQuery.isBlank() ||
                log.action.contains(searchQuery, ignoreCase = true) ||
                log.details.contains(searchQuery, ignoreCase = true) ||
                log.userRole.contains(searchQuery, ignoreCase = true) ||
                (log.relatedRecordId?.contains(searchQuery, ignoreCase = true) ?: false)

        val matchesOutcome = selectedOutcome == null || log.outcome.equals(selectedOutcome, ignoreCase = true)

        matchesSearch && matchesOutcome
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = "System Activity & Audit Logs",
                    color = Navy900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Immutable chronological audit trail of all institutional platform events",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Integrity Notice
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Navy50,
                border = androidx.compose.foundation.BorderStroke(1.dp, LightOutline)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Navy700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tamper-Resistant Security Rule: Audit log entries cannot be modified or deleted through the user interface. Production environments enforce write-once append-only server-side ledger controls.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Search & Filter
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("audit_logs_search_bar"),
                placeholder = { Text("Filter audit trail by action, role, or ID...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CyanDark) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedOutcome == null,
                    onClick = { selectedOutcome = null },
                    label = { Text("All (${logs.size})", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedOutcome == "Success",
                    onClick = { selectedOutcome = "Success" },
                    label = { Text("Success", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedOutcome == "Flagged",
                    onClick = { selectedOutcome = "Flagged" },
                    label = { Text("Flagged", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedOutcome == "Warning",
                    onClick = { selectedOutcome = "Warning" },
                    label = { Text("Warnings", fontSize = 11.sp) }
                )
            }
        }

        item {
            Text(
                text = "Showing ${filteredLogs.size} audit events",
                fontSize = 11.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
        }

        items(filteredLogs) { log ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, LightOutline, RoundedCornerShape(10.dp)),
                color = LightSurface
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
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
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (log.outcome) {
                                            "Success" -> StatusSuccess
                                            "Flagged" -> StatusError
                                            "Warning" -> StatusWarning
                                            else -> TextMuted
                                        }
                                    )
                            )
                            Text(
                                text = log.action,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Navy900
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (log.outcome) {
                                "Success" -> StatusSuccessBg
                                "Flagged" -> StatusErrorBg
                                "Warning" -> StatusWarningBg
                                else -> Navy50
                            }
                        ) {
                            Text(
                                text = log.outcome.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (log.outcome) {
                                    "Success" -> StatusSuccess
                                    "Flagged" -> StatusError
                                    "Warning" -> StatusWarning
                                    else -> TextSecondary
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = log.details,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(color = LightOutline)
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Actor: ${log.userRole}",
                            fontSize = 10.sp,
                            color = CyanDark,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${log.timestamp} • ${log.id}",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}
