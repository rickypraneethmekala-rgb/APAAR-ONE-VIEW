package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Institution
import com.example.ui.ApaarViewModel
import com.example.ui.theme.*

@Composable
fun InstitutionManagementScreen(
    viewModel: ApaarViewModel,
    modifier: Modifier = Modifier
) {
    val institutions = viewModel.institutions
    val students by viewModel.students.collectAsState()

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
                    text = "AICTE Institutions Registry",
                    color = Navy900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Approved institutional partners participating in APAAR OneView data exchange",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Fictional Disclaimer
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
                    Icon(Icons.Default.Info, contentDescription = null, tint = Navy700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Demonstration Entities: These institutions and credentials are sample records for platform demonstration and testing. No representation of actual AICTE affiliation is made.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        items(institutions) { inst ->
            val recordCount = students.count { it.institutionId == inst.id }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, LightOutline, RoundedCornerShape(12.dp))
                    .testTag("institution_card_${inst.id.lowercase()}"),
                color = LightSurface,
                tonalElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = inst.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Navy900
                            )
                            Text(
                                text = "Code: ${inst.code} • ID: ${inst.id}",
                                fontSize = 11.sp,
                                color = CyanDark,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = CyanSurface
                        ) {
                            Text(
                                text = "$recordCount RECORDS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = CyanDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = LightOutline)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Campus Location", fontSize = 11.sp, color = TextMuted)
                        Text(text = inst.location, fontSize = 11.sp, color = Navy900, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Academic Coordinator", fontSize = 11.sp, color = TextMuted)
                        Text(text = inst.coordinatorEmail, fontSize = 11.sp, color = Navy900, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Active Upload Batches", fontSize = 11.sp, color = TextMuted)
                        Text(text = "${inst.activeBatches} Batches Processed", fontSize = 11.sp, color = StatusSuccess, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
