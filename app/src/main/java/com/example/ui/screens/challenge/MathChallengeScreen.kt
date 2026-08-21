package com.example.ui.screens.challenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.local.entity.ChildProfile
import com.example.domain.model.InteractionType
import com.example.domain.model.MathProblem
import com.example.domain.model.MathSubject
import com.example.ui.components.ConfettiRewardEffect
import com.example.ui.components.InteractiveKeypad
import com.example.ui.components.KidButton
import com.example.ui.components.JungleTopBar
import com.example.ui.components.VisualMathContainer
import com.example.ui.theme.GemTeal
import com.example.ui.theme.JunglePrimary
import com.example.ui.theme.StarGold
import com.example.ui.viewmodel.ChallengeSessionState

@Composable
fun MathChallengeScreen(
    profile: ChildProfile,
    sessionState: ChallengeSessionState,
    onAnswerSelected: (String) -> Unit,
    onObjectTapped: () -> Unit,
    onRequestHint: () -> Unit,
    onHearAgain: () -> Unit,
    onNextQuestion: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val problem = sessionState.currentProblem

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
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            JungleTopBar(
                title = sessionState.currentLevel?.title ?: sessionState.subject.displayName,
                stars = profile.totalStars,
                gems = profile.totalGems,
                streak = profile.currentStreak,
                showBackButton = true,
                onBackClick = onBackClick,
                onAudioClick = onHearAgain
            )

            // Quest Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Q ${sessionState.currentQuestionIndex + 1}/${sessionState.totalQuestions}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = Color(0xFF1B5E20)
                )

                val progressFrac = (sessionState.currentQuestionIndex.toFloat() / sessionState.totalQuestions)
                LinearProgressIndicator(
                    progress = { progressFrac },
                    modifier = Modifier
                        .weight(1f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = Color(0xFF2E7D32),
                    trackColor = Color(0xFFDCEDC8)
                )

                // Hear Again Quick Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9))
                        .border(1.5.dp, Color(0xFFA5D6A7), CircleShape)
                        .clickable { onHearAgain() }
                        .testTag("hear_again_circle"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🔊", fontSize = 18.sp)
                }

                // Hint Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF9C4))
                        .border(1.5.dp, Color(0xFFFFD54F), CircleShape)
                        .clickable { onRequestHint() }
                        .testTag("hint_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💡", fontSize = 18.sp)
                }
            }

            // Challenge Question Area (Scrollable if needed on compact screens)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (problem != null) {
                    // Question Prompt Bubble
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color(0xFFA5D6A7), RoundedCornerShape(22.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🐒", fontSize = 34.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = problem.questionText,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1B5E20),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Visual Items Display
                    VisualMathContainer(
                        problem = problem,
                        onObjectTapped = onObjectTapped
                    )

                    // Hint Box (if active)
                    if (sessionState.showingHintStep > 0) {
                        val hintMsg = if (sessionState.showingHintStep == 1) problem.hintStep1 else problem.hintStep2
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFF9C4))
                                .border(2.dp, Color(0xFFFFD54F), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "💡 Hint:", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color(0xFFE65100))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = hintMsg, fontSize = 14.sp, color = Color(0xFF5D4037), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    // Interactive Choices / Buttons
                    if (problem.interactionType == InteractionType.NUMBER_KEYPAD) {
                        var enteredNum by remember(problem.id) { mutableStateOf("") }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(width = 140.dp, height = 50.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                                    .border(2.dp, Color(0xFFA5D6A7), RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (enteredNum.isEmpty()) "?" else enteredNum,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                            InteractiveKeypad(
                                onDigitClick = { if (enteredNum.length < 3) enteredNum += it },
                                onDeleteClick = { if (enteredNum.isNotEmpty()) enteredNum = enteredNum.dropLast(1) },
                                onSubmitClick = {
                                    if (enteredNum.isNotEmpty()) onAnswerSelected(enteredNum)
                                }
                            )
                        }
                    } else {
                        // Big Multiple Choice Grid
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val rows = problem.options.chunked(2)
                            rows.forEach { rowOptions ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowOptions.forEach { option ->
                                        val isSelected = sessionState.selectedAnswer == option
                                        val isCorrectOption = if (problem.subject == MathSubject.FRACTIONS || problem.subject == MathSubject.COMPARISON) {
                                            option == problem.correctAnswerString
                                        } else {
                                            option == problem.correctAnswer.toString()
                                        }

                                        val bgColor = when {
                                            !sessionState.isAnswerChecked -> Color.White
                                            isSelected && sessionState.isCorrect -> Color(0xFFDCEDC8)
                                            isSelected && !sessionState.isCorrect -> Color(0xFFFFCDD2)
                                            isCorrectOption && sessionState.isAnswerChecked -> Color(0xFFDCEDC8)
                                            else -> Color.White
                                        }

                                        val borderColor = when {
                                            !sessionState.isAnswerChecked -> Color(0xFFFFD54F)
                                            isSelected && sessionState.isCorrect -> Color(0xFF2E7D32)
                                            isSelected && !sessionState.isCorrect -> Color(0xFFE53935)
                                            isCorrectOption && sessionState.isAnswerChecked -> Color(0xFF2E7D32)
                                            else -> Color(0xFFE0E0E0)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .shadow(2.dp, RoundedCornerShape(20.dp))
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(bgColor)
                                                .border(2.5.dp, borderColor, RoundedCornerShape(20.dp))
                                                .clickable(enabled = !sessionState.isAnswerChecked) {
                                                    onAnswerSelected(option)
                                                }
                                                .padding(vertical = 16.dp)
                                                .testTag("option_$option"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = option,
                                                fontSize = 26.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color(0xFF1B5E20)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Immediate Feedback Bar
                    if (sessionState.isAnswerChecked) {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + scaleIn()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(3.dp, RoundedCornerShape(20.dp))
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (sessionState.isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFF3E0))
                                    .border(
                                        2.dp,
                                        if (sessionState.isCorrect) Color(0xFFA5D6A7) else Color(0xFFFFB74D),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (sessionState.isCorrect) "🌟 FANTASTIC!" else "🌱 ALMOST!",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 17.sp,
                                            color = if (sessionState.isCorrect) Color(0xFF1B5E20) else Color(0xFFE65100)
                                        )
                                        Text(
                                            text = sessionState.feedbackMessage,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF33691E)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    KidButton(
                                        text = "NEXT ➔",
                                        onClick = onNextQuestion,
                                        gradientColors = listOf(Color(0xFF81C784), Color(0xFF2E7D32)),
                                        textColor = Color.White,
                                        fontSize = 17,
                                        testTag = "next_question_button"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Level / Quest Complete Dialog
        if (sessionState.isSessionComplete) {
            ConfettiRewardEffect()

            Dialog(onDismissRequest = {}) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(28.dp))
                        .clip(RoundedCornerShape(28.dp))
                        .border(3.dp, Color(0xFFA5D6A7), RoundedCornerShape(28.dp)),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "🎉 QUEST COMPLETE! 🎉",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1B5E20),
                            textAlign = TextAlign.Center
                        )

                        // Stars Earned Row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (s in 1..3) {
                                val earned = s <= sessionState.starsEarned
                                Text(
                                    text = if (earned) "⭐" else "☆",
                                    fontSize = 42.sp,
                                    color = if (earned) StarGold else Color.LightGray
                                )
                            }
                        }

                        // Rewards earned
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFFFF9C4))
                                    .border(1.5.dp, Color(0xFFFFD54F), RoundedCornerShape(14.dp))
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "+${sessionState.starsEarned} ⭐ Stars",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFFE65100)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFE0F2F1))
                                    .border(1.5.dp, Color(0xFF80CBC4), RoundedCornerShape(14.dp))
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "+${sessionState.gemsEarned} 💎 Gems",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF00695C)
                                )
                            }
                        }

                        Text(
                            text = "Great math learning today! You answered ${sessionState.correctInSession} of ${sessionState.totalQuestions} questions correctly!",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color(0xFF1B5E20)
                        )

                        KidButton(
                            text = "CONTINUE ADVENTURE! 🚀",
                            onClick = onBackClick,
                            gradientColors = listOf(Color(0xFF81C784), Color(0xFF2E7D32)),
                            textColor = Color.White,
                            fontSize = 19,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "quest_complete_continue"
                        )
                    }
                }
            }
        }
    }
}
