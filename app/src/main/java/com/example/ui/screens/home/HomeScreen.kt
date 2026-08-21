package com.example.ui.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.R
import com.example.data.local.entity.ChildProfile
import com.example.domain.model.MathSubject
import com.example.ui.components.KidButton
import com.example.ui.components.JungleTopBar
import com.example.ui.theme.GemTeal
import com.example.ui.theme.JunglePrimary
import com.example.ui.theme.StarGold

@Composable
fun HomeScreen(
    profile: ChildProfile,
    onPlayTodayAdventure: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenTreasureRoom: () -> Unit,
    onOpenAvatarCustomize: () -> Unit,
    onOpenProgress: () -> Unit,
    onOpenParentGate: () -> Unit,
    onSubjectQuickPlay: (MathSubject) -> Unit,
    onAudioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "play_btn_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val avatarEmoji = when (profile.selectedAvatar) {
        "monkey_pip" -> "🐒"
        "lion_leo" -> "🦁"
        "toucan_tiki" -> "🦜"
        "elephant_ella" -> "🐘"
        "tiger_toby" -> "🐅"
        else -> "🐒"
    }

    val petEmoji = when (profile.companionPetId) {
        "baby_sloth" -> "🦥"
        "parrot_pip" -> "🦜"
        "baby_dino" -> "🦖"
        "chameleon" -> "🦎"
        else -> "🦥"
    }

    val hatEmoji = when (profile.hatId) {
        "safari_hat" -> "🤠"
        "flower_crown" -> "🌸"
        "pirate_bandana" -> "🏴‍☠️"
        "golden_crown" -> "👑"
        "astronaut_helmet" -> "🚀"
        else -> "🤠"
    }

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
            // Top Bar with Stats & Parent Gate
            JungleTopBar(
                title = "Hi, ${profile.name}! 👋",
                stars = profile.totalStars,
                gems = profile.totalGems,
                streak = profile.currentStreak,
                showBackButton = false,
                onParentGateClick = onOpenParentGate,
                onAudioClick = onAudioClick
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Child Avatar & Pet Showcase Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(2.5.dp, Color(0xFFA5D6A7), RoundedCornerShape(24.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Avatar Circle
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .shadow(3.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF9C4))
                                .border(3.dp, Color(0xFFFFD54F), CircleShape)
                                .clickable { onOpenAvatarCustomize() }
                                .testTag("home_avatar_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = avatarEmoji, fontSize = 48.sp)
                            // Hat overlay
                            Text(
                                text = hatEmoji,
                                fontSize = 28.sp,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 2.dp)
                            )
                            // Pet overlay
                            Text(
                                text = petEmoji,
                                fontSize = 26.sp,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 2.dp, end = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Next Reward Progress
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Level ${profile.currentLevel}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF1B5E20)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "🎁 Next: Crown",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE65100)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            val progressValue = ((profile.totalGems % 60) / 60f).coerceIn(0.1f, 1f)
                            LinearProgressIndicator(
                                progress = { progressValue },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = Color(0xFF2E7D32),
                                trackColor = Color(0xFFDCEDC8),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${profile.totalGems}/60 💎 to unlock next item!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF558B2F)
                            )
                        }
                    }
                }

                // Big Main "PLAY TODAY'S QUEST" Button
                Box(
                    modifier = Modifier
                        .scale(pulseScale)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    KidButton(
                        text = "▶ PLAY TODAY'S QUEST! 🌟",
                        onClick = onPlayTodayAdventure,
                        gradientColors = listOf(Color(0xFF81C784), Color(0xFF2E7D32)),
                        textColor = Color.White,
                        fontSize = 22,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "play_today_quest_button"
                    )
                }

                // Quick Navigation Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HomeNavCard(
                        title = "Jungle Map",
                        emoji = "🗺️",
                        bgColor = Color(0xFFE8F5E9),
                        borderColor = Color(0xFFA5D6A7),
                        onClick = onOpenMap,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_map"
                    )
                    HomeNavCard(
                        title = "Treasure",
                        emoji = "💎",
                        bgColor = Color(0xFFE0F2F1),
                        borderColor = Color(0xFF80CBC4),
                        onClick = onOpenTreasureRoom,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_treasure"
                    )
                    HomeNavCard(
                        title = "Dress Up",
                        emoji = "🎨",
                        bgColor = Color(0xFFFFF8E1),
                        borderColor = Color(0xFFFFD54F),
                        onClick = onOpenAvatarCustomize,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_avatar"
                    )
                    HomeNavCard(
                        title = "Badges",
                        emoji = "🏆",
                        bgColor = Color(0xFFEDE7F6),
                        borderColor = Color(0xFFB39DDB),
                        onClick = onOpenProgress,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_badges"
                    )
                }

                // Math Subject Fast Practice Carousel / Cards
                SectionHeader(title = "Choose a Subject to Play 🌴")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SubjectQuickButton(
                        emoji = "➕",
                        name = "Addition",
                        bgColor = Color(0xFFFFF9C4),
                        onClick = { onSubjectQuickPlay(MathSubject.ADDITION) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_add"
                    )
                    SubjectQuickButton(
                        emoji = "➖",
                        name = "Subtract",
                        bgColor = Color(0xFFFFE0B2),
                        onClick = { onSubjectQuickPlay(MathSubject.SUBTRACTION) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_sub"
                    )
                    SubjectQuickButton(
                        emoji = "✖️",
                        name = "Multiply",
                        bgColor = Color(0xFFE8F5E9),
                        onClick = { onSubjectQuickPlay(MathSubject.MULTIPLICATION) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_mult"
                    )
                    SubjectQuickButton(
                        emoji = "🍕",
                        name = "Fractions",
                        bgColor = Color(0xFFE0F2F1),
                        onClick = { onSubjectQuickPlay(MathSubject.FRACTIONS) },
                        modifier = Modifier.weight(1f),
                        testTag = "quick_frac"
                    )
                }
            }
        }
    }
}

@Composable
fun HomeNavCard(
    title: String,
    emoji: String,
    bgColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .shadow(3.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF37474F),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SubjectQuickButton(
    emoji: String,
    name: String,
    bgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723)
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1B5E20)
        )
    }
}
