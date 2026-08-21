package com.example.ui.screens.rewards

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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.ChildProfile
import com.example.data.local.entity.RewardItem
import com.example.ui.components.KidButton
import com.example.ui.components.JungleTopBar
import com.example.ui.theme.GemTeal
import com.example.ui.theme.JunglePrimary
import com.example.ui.theme.StarGold

@Composable
fun TreasureRoomScreen(
    profile: ChildProfile,
    rewards: List<RewardItem>,
    onEquipReward: (RewardItem) -> Unit,
    onBackClick: () -> Unit,
    onAudioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val categories = listOf("ALL", "HAT", "OUTFIT", "PET", "BADGE")
    val categoryLabels = listOf("All Loot 🎁", "Hats 🤠", "Outfits 🦺", "Pets 🐾", "Badges 🏆")

    val filteredRewards = remember(selectedCategoryIndex, rewards) {
        val cat = categories[selectedCategoryIndex]
        if (cat == "ALL") rewards else rewards.filter { it.category == cat }
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
                .padding(12.dp)
        ) {
            JungleTopBar(
                title = "Treasure Room 💎",
                stars = profile.totalStars,
                gems = profile.totalGems,
                streak = profile.currentStreak,
                showBackButton = true,
                onBackClick = onBackClick,
                onAudioClick = onAudioClick
            )

            // Treasure Chest Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .padding(vertical = 4.dp)
                    .shadow(3.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, Color(0xFFA5D6A7), RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_treasure_room),
                    contentDescription = "Treasure Room Chest",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                            )
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Text(
                            text = "Jungle Vault of Trophies!",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Earned 100% through math learning 🌟",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD54F)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Category Filter Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                containerColor = Color.Transparent,
                edgePadding = 0.dp,
                divider = {}
            ) {
                categories.forEachIndexed { index, _ ->
                    val isSelected = selectedCategoryIndex == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedCategoryIndex = index },
                        text = {
                            Text(
                                text = categoryLabels[index],
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                color = if (isSelected) Color(0xFF1B5E20) else Color(0xFF558B2F),
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rewards List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredRewards) { item ->
                    RewardCard(
                        item = item,
                        playerGems = profile.totalGems,
                        onEquip = { onEquipReward(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun RewardCard(
    item: RewardItem,
    playerGems: Int,
    onEquip: () -> Unit
) {
    val canAfford = playerGems >= item.costGems
    val isBadge = item.category == "BADGE"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(
                2.dp,
                if (item.isEquipped) Color(0xFF2E7D32) else Color(0xFFA5D6A7),
                RoundedCornerShape(18.dp)
            )
            .padding(12.dp)
            .testTag("reward_item_${item.itemId}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (item.isUnlocked) Color(0xFFFFF9C4) else Color(0xFFECEFF1))
                    .border(2.dp, if (item.isUnlocked) Color(0xFFFFD54F) else Color(0xFFB0BEC5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.iconEmoji, fontSize = 30.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1B5E20)
                    )
                    if (item.isEquipped) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "EQUIPPED", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E7D32))
                        }
                    }
                }
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    color = Color(0xFF558B2F)
                )
                Text(
                    text = if (item.isUnlocked) "Unlocked! ✨" else "Requirement: ${item.unlockCriteria}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (item.isUnlocked) Color(0xFF2E7D32) else Color(0xFFE65100)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Button
            if (!isBadge) {
                if (item.isUnlocked) {
                    KidButton(
                        text = if (item.isEquipped) "Equipped" else "Wear",
                        onClick = onEquip,
                        gradientColors = if (item.isEquipped) {
                            listOf(Color(0xFF81C784), Color(0xFF2E7D32))
                        } else {
                            listOf(Color(0xFFFFD54F), Color(0xFFE65100))
                        },
                        textColor = Color.White,
                        fontSize = 13,
                        testTag = "equip_${item.itemId}"
                    )
                } else {
                    KidButton(
                        text = "${item.costGems} 💎",
                        onClick = onEquip,
                        gradientColors = if (canAfford) {
                            listOf(Color(0xFF00897B), Color(0xFF004D40))
                        } else {
                            listOf(Color(0xFFECEFF1), Color(0xFFB0BEC5))
                        },
                        textColor = Color.White,
                        fontSize = 13,
                        testTag = "unlock_${item.itemId}"
                    )
                }
            } else {
                if (item.isUnlocked) {
                    Text(text = "🏆 Earned", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                } else {
                    Text(text = "🔒 Locked", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                }
            }
        }
    }
}
