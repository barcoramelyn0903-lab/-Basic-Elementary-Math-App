package com.example.ui.screens.map

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.ChildProfile
import com.example.data.local.entity.WorldLevel
import com.example.ui.components.KidButton
import com.example.ui.components.JungleTopBar
import com.example.ui.theme.StarGold

data class WorldInfo(
    val id: String,
    val name: String,
    val emoji: String,
    val color: Color,
    val description: String
)

@Composable
fun JungleMapScreen(
    profile: ChildProfile,
    levels: List<WorldLevel>,
    onSelectLevel: (WorldLevel) -> Unit,
    onBackClick: () -> Unit,
    onAudioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val worlds = listOf(
        WorldInfo("banana_grove", "1. Banana Grove", "🍌", Color(0xFFFFF59D), "Counting & Addition"),
        WorldInfo("coconut_falls", "2. Coconut Falls", "🥥", Color(0xFFFFCC80), "Jungle Subtraction"),
        WorldInfo("animal_savanna", "3. Animal Savanna", "🐾", Color(0xFFA5D6A7), "Multiplication & Division"),
        WorldInfo("fraction_feast", "4. Fraction Feast", "🍕", Color(0xFFFFAB91), "Visual Fractions"),
        WorldInfo("croc_river", "5. Croc River", "🐊", Color(0xFF80DEEA), "Comparison & Stories"),
        WorldInfo("treasure_temple", "6. Treasure Temple", "🏛️", Color(0xFFCE93D8), "Grand Math Mastery")
    )

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
                title = "Jungle Quest Map 🗺️",
                stars = profile.totalStars,
                gems = profile.totalGems,
                streak = profile.currentStreak,
                showBackButton = true,
                onBackClick = onBackClick,
                onAudioClick = onAudioClick
            )

            // Mini Map Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(vertical = 4.dp)
                    .shadow(3.dp, RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .border(2.dp, Color(0xFFA5D6A7), RoundedCornerShape(18.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_jungle_map),
                    contentDescription = "Jungle Map View",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Black.copy(alpha = 0.55f), Color.Transparent)
                            )
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Text(
                            text = "Explore the Wild Islands!",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Solve math puzzles to unlock new paths",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFE082)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Worlds & Levels List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(worlds) { world ->
                    val worldLevels = levels.filter { it.worldId == world.id }
                    WorldSectionCard(
                        world = world,
                        levels = worldLevels,
                        onLevelClick = onSelectLevel
                    )
                }
            }
        }
    }
}

@Composable
fun WorldSectionCard(
    world: WorldInfo,
    levels: List<WorldLevel>,
    onLevelClick: (WorldLevel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(2.dp, Color(0xFFA5D6A7), RoundedCornerShape(22.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // World Title Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(world.color.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = world.emoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = world.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = world.description,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4E342E)
                    )
                }
            }

            // Stepping Stones Levels in this World
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                levels.forEach { level ->
                    LevelSteppingStone(
                        level = level,
                        onClick = {
                            if (level.isUnlocked) {
                                onLevelClick(level)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LevelSteppingStone(
    level: WorldLevel,
    onClick: () -> Unit
) {
    val isUnlocked = level.isUnlocked
    val isCompleted = level.isCompleted

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(enabled = isUnlocked) { onClick() }
            .testTag("level_stone_${level.levelId}")
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(if (isUnlocked) 3.dp else 1.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    if (!isUnlocked) Color(0xFFECEFF1)
                    else if (isCompleted) Color(0xFFDCEDC8)
                    else Color(0xFFFFF9C4)
                )
                .border(
                    3.dp,
                    if (!isUnlocked) Color(0xFFB0BEC5)
                    else if (isCompleted) Color(0xFF2E7D32)
                    else Color(0xFFFFD54F),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!isUnlocked) {
                Text(text = "🔒", fontSize = 22.sp)
            } else {
                Text(text = level.iconEmoji, fontSize = 26.sp)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Level Title
        Text(
            text = "Lvl ${level.levelNumber}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) Color(0xFF1B5E20) else Color.Gray
        )

        // Star Rating (0 to 3)
        if (isUnlocked) {
            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                for (s in 1..3) {
                    val earned = s <= level.starsEarned
                    Text(
                        text = if (earned) "⭐" else "☆",
                        fontSize = 12.sp,
                        color = if (earned) StarGold else Color.LightGray
                    )
                }
            }
        }
    }
}
