package com.example.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.local.entity.WorldLevel
import com.example.ui.components.KidButton
import com.example.ui.components.JungleTopBar
import com.example.ui.theme.GemTeal
import com.example.ui.theme.StarGold

@Composable
fun ProgressScreen(
    profile: ChildProfile,
    skills: List<SkillProgress>,
    levels: List<WorldLevel>,
    onBackClick: () -> Unit,
    onAudioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedLevelsCount = levels.count { it.isCompleted }
    val totalLevelsCount = levels.size.coerceAtLeast(1)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF1F8E9),
                        Color(0xFFE8F5E9),
                        Color(0xFFDCEDC8)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            JungleTopBar(
                title = "Math Journey 🏆",
                stars = profile.totalStars,
                gems = profile.totalGems,
                streak = profile.currentStreak,
                showBackButton = true,
                onBackClick = onBackClick,
                onAudioClick = onAudioClick
            )

            // Overall Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(22.dp))
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White)
                    .border(2.dp, Color(0xFFA5D6A7), RoundedCornerShape(22.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jungle Exploration Progress",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "$completedLevelsCount / $totalLevelsCount Islands",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFE65100)
                        )
                    }

                    val overallProgress = (completedLevelsCount.toFloat() / totalLevelsCount)
                    LinearProgressIndicator(
                        progress = { overallProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFF2E7D32),
                        trackColor = Color(0xFFDCEDC8)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProgressMiniStat(emoji = "⭐", value = "${profile.totalStars}", label = "Stars Earned")
                        ProgressMiniStat(emoji = "💎", value = "${profile.totalGems}", label = "Gems Found")
                        ProgressMiniStat(emoji = "🔥", value = "${profile.currentStreak} Days", label = "Streak")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Skill Badges & Power Levels 🌿",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Skills List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(skills) { skill ->
                    SkillBadgeCard(skill = skill)
                }
            }
        }
    }
}

@Composable
fun ProgressMiniStat(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$emoji $value", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF1B5E20))
        Text(text = label, fontSize = 11.sp, color = Color(0xFF558B2F))
    }
}

@Composable
fun SkillBadgeCard(skill: SkillProgress) {
    val statusColor = when (skill.statusLabel) {
        "Mastered" -> Color(0xFF2E7D32)
        "Developing" -> Color(0xFF00897B)
        "Practicing" -> Color(0xFFE65100)
        else -> Color(0xFF7E57C2)
    }

    val statusBg = when (skill.statusLabel) {
        "Mastered" -> Color(0xFFE8F5E9)
        "Developing" -> Color(0xFFE0F2F1)
        "Practicing" -> Color(0xFFFFF3E0)
        else -> Color(0xFFEDE7F6)
    }

    val emoji = when (skill.skillId) {
        "COUNTING" -> "🎯"
        "ADDITION" -> "➕"
        "SUBTRACTION" -> "➖"
        "MULTIPLICATION" -> "✖️"
        "DIVISION" -> "➗"
        "FRACTIONS" -> "🍕"
        "COMPARISON" -> "🐊"
        "WORD_PROBLEMS" -> "📜"
        else -> "⭐"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.5.dp, Color(0xFFA5D6A7), RoundedCornerShape(18.dp))
            .padding(12.dp)
            .testTag("skill_${skill.skillId}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(statusBg),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = skill.subjectName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1B5E20)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(statusBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = skill.statusLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = statusColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                val fraction = (skill.masteryPercentage / 100f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { fraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = statusColor,
                    trackColor = Color(0xFFECEFF1)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${skill.masteryPercentage}% Mastery • ${skill.correctCount}/${skill.totalAttempted} Correct",
                    fontSize = 11.sp,
                    color = Color(0xFF558B2F)
                )
            }
        }
    }
}
