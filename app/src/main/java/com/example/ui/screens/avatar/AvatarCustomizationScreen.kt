package com.example.ui.screens.avatar

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ChildProfile
import com.example.data.local.entity.RewardItem
import com.example.ui.components.KidButton
import com.example.ui.components.JungleTopBar
import com.example.ui.theme.StarGold

@Composable
fun AvatarCustomizationScreen(
    profile: ChildProfile,
    rewards: List<RewardItem>,
    onEquipItem: (RewardItem) -> Unit,
    onBackClick: () -> Unit,
    onAudioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hats = rewards.filter { it.category == "HAT" }
    val outfits = rewards.filter { it.category == "OUTFIT" }
    val pets = rewards.filter { it.category == "PET" }

    val avatarEmoji = when (profile.selectedAvatar) {
        "monkey_pip" -> "🐒"
        "lion_leo" -> "🦁"
        "toucan_tiki" -> "🦜"
        "elephant_ella" -> "🐘"
        "tiger_toby" -> "🐅"
        else -> "🐒"
    }

    val currentHatEmoji = hats.find { it.itemId == profile.hatId }?.iconEmoji ?: "🤠"
    val currentOutfitEmoji = outfits.find { it.itemId == profile.outfitId }?.iconEmoji ?: "🦺"
    val currentPetEmoji = pets.find { it.itemId == profile.companionPetId }?.iconEmoji ?: "🦥"

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
                title = "Dress Up Avatar 🎨",
                stars = profile.totalStars,
                gems = profile.totalGems,
                streak = profile.currentStreak,
                showBackButton = true,
                onBackClick = onBackClick,
                onAudioClick = onAudioClick
            )

            // Main Dressing Room Stage Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .shadow(4.dp, RoundedCornerShape(26.dp))
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color.White)
                    .border(2.5.dp, Color(0xFFA5D6A7), RoundedCornerShape(26.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .shadow(3.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF9C4))
                            .border(3.dp, StarGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = avatarEmoji, fontSize = 56.sp)
                        // Hat
                        Text(
                            text = currentHatEmoji,
                            fontSize = 32.sp,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 2.dp)
                        )
                        // Outfit badge
                        Text(
                            text = currentOutfitEmoji,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 4.dp, bottom = 4.dp)
                        )
                        // Companion pet
                        Text(
                            text = currentPetEmoji,
                            fontSize = 30.sp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 4.dp, bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${profile.name}'s Explorer Look",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color(0xFF1B5E20)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Wardrobe Sections (Scrollable)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                WardrobePickerRow(
                    sectionTitle = "Choose Hat 🤠",
                    items = hats,
                    currentEquippedId = profile.hatId,
                    onItemSelect = onEquipItem
                )

                WardrobePickerRow(
                    sectionTitle = "Choose Outfit 🦺",
                    items = outfits,
                    currentEquippedId = profile.outfitId,
                    onItemSelect = onEquipItem
                )

                WardrobePickerRow(
                    sectionTitle = "Choose Companion Pet 🐾",
                    items = pets,
                    currentEquippedId = profile.companionPetId,
                    onItemSelect = onEquipItem
                )
            }

            KidButton(
                text = "LOOKS GREAT! DONE 👍",
                onClick = onBackClick,
                gradientColors = listOf(Color(0xFF81C784), Color(0xFF2E7D32)),
                textColor = Color.White,
                fontSize = 18,
                modifier = Modifier.fillMaxWidth(),
                testTag = "avatar_done_button"
            )
        }
    }
}

@Composable
fun WardrobePickerRow(
    sectionTitle: String,
    items: List<RewardItem>,
    currentEquippedId: String,
    onItemSelect: (RewardItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(2.dp, Color(0xFFA5D6A7), RoundedCornerShape(20.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = sectionTitle,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1B5E20)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { item ->
                val isEquipped = item.itemId == currentEquippedId
                val isUnlocked = item.isUnlocked

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onItemSelect(item) }
                        .padding(4.dp)
                        .testTag("wardrobe_${item.itemId}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .shadow(if (isEquipped) 4.dp else 1.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                if (!isUnlocked) Color(0xFFECEFF1)
                                else if (isEquipped) Color(0xFFFFF9C4)
                                else Color(0xFFE8F5E9)
                            )
                            .border(
                                3.dp,
                                if (isEquipped) StarGold
                                else if (isUnlocked) Color(0xFF81C784)
                                else Color(0xFFB0BEC5),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isUnlocked) item.iconEmoji else "🔒",
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isEquipped) "Equipped" else if (isUnlocked) item.title.split(" ").firstOrNull() ?: "" else "${item.costGems}💎",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEquipped) Color(0xFFE65100) else Color(0xFF4E342E),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
