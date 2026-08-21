package com.example.ui.screens.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ChildProfile
import com.example.data.local.entity.SkillProgress
import com.example.ui.components.KidButton
import com.example.ui.theme.JunglePrimary

@Composable
fun ParentDashboardScreen(
    profile: ChildProfile,
    skills: List<SkillProgress>,
    onOpenSettings: () -> Unit,
    onBackToKidHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalAttempted = skills.sumOf { it.totalAttempted }.coerceAtLeast(1)
    val totalCorrect = skills.sumOf { it.correctCount }
    val overallAccuracy = ((totalCorrect.toDouble() / totalAttempted) * 100).toInt()

    val masteredSkills = skills.filter { it.statusLabel == "Mastered" }
    val developingSkills = skills.filter { it.statusLabel == "Developing" || it.statusLabel == "Practicing" }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Parent Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(2.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.5.dp, Color(0xFFA5D6A7), CircleShape)
                            .clickable { onBackToKidHome() }
                            .testTag("parent_back_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to child area",
                            tint = Color(0xFF1B5E20)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Parent Learning Hub",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "Insights for ${profile.name} (Age ${profile.age})",
                            fontSize = 12.sp,
                            color = Color(0xFF558B2F)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.5.dp, Color(0xFFA5D6A7), CircleShape)
                        .clickable { onOpenSettings() }
                        .testTag("parent_settings_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFF1B5E20)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Quick KPI Metric Cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ParentKpiCard(
                            title = "Accuracy Rate",
                            value = "$overallAccuracy%",
                            subtitle = "$totalCorrect of $totalAttempted correct",
                            color = Color(0xFF1B5E20),
                            bgColor = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        ParentKpiCard(
                            title = "Total Time",
                            value = "${profile.totalTimeMinutes}m",
                            subtitle = "${profile.currentStreak} day streak",
                            color = Color(0xFF00897B),
                            bgColor = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Actionable Recommendation Box
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, Color(0xFFFFD54F), RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "💡 Learning Recommendation",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFFE65100)
                            )
                            val recommendation = if (developingSkills.isNotEmpty()) {
                                "${profile.name} is excelling with ${masteredSkills.firstOrNull()?.subjectName ?: "basic counting"} (${masteredSkills.firstOrNull()?.masteryPercentage ?: 85}%), but could benefit from a bit more practice with ${developingSkills.first().subjectName}."
                            } else {
                                "${profile.name} has demonstrated fantastic proficiency across all introductory math fundamentals! Try advancing to higher difficulty in Settings."
                            }
                            Text(
                                text = recommendation,
                                fontSize = 13.sp,
                                color = Color(0xFF4E342E),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Subject-by-Subject Mastery Breakdown
                item {
                    Text(
                        text = "Subject Breakdown",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                }

                items(skills) { skill ->
                    SubjectParentProgressItem(skill = skill)
                }

                // Child Safety Statement
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .border(1.5.dp, Color(0xFFA5D6A7), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "🛡️ COPPA Compliant & Privacy Safe",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = "This application operates 100% on-device with zero advertisements, no tracking, and no external data sharing.",
                                fontSize = 11.sp,
                                color = Color(0xFF558B2F)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParentKpiCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.5.dp, Color(0xFFA5D6A7), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = title, fontSize = 12.sp, color = Color(0xFF558B2F), fontWeight = FontWeight.SemiBold)
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF4E342E))
        }
    }
}

@Composable
fun SubjectParentProgressItem(skill: SkillProgress) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.5.dp, Color(0xFFA5D6A7), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = skill.subjectName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1B5E20)
                )
                Text(
                    text = "${skill.masteryPercentage}% (${skill.statusLabel})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = when (skill.statusLabel) {
                        "Mastered" -> Color(0xFF2E7D32)
                        "Developing" -> Color(0xFF00897B)
                        else -> Color(0xFFE65100)
                    }
                )
            }

            val fraction = (skill.masteryPercentage / 100f).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { fraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = when (skill.statusLabel) {
                    "Mastered" -> Color(0xFF2E7D32)
                    "Developing" -> Color(0xFF00897B)
                    else -> Color(0xFFE65100)
                },
                trackColor = Color(0xFFDCEDC8)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${skill.correctCount} correct / ${skill.totalAttempted} attempts",
                    fontSize = 11.sp,
                    color = Color(0xFF558B2F)
                )
                Text(
                    text = "Hints used: ${skill.hintsUsed}",
                    fontSize = 11.sp,
                    color = Color(0xFF4E342E)
                )
            }
        }
    }
}
