package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reward_items")
data class RewardItem(
    @PrimaryKey val itemId: String,
    val title: String,
    val category: String, // HAT, OUTFIT, PET, BADGE, LOCATION
    val iconEmoji: String,
    val costGems: Int = 0,
    val isUnlocked: Boolean = false,
    val isEquipped: Boolean = false,
    val description: String = "",
    val unlockCriteria: String = ""
)
