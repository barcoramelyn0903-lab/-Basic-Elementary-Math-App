package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profile")
data class ChildProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Leo",
    val age: Int = 7,
    val selectedAvatar: String = "monkey_pip", // monkey_pip, lion_leo, toucan_tiki, elephant_ella, tiger_toby
    val hatId: String = "safari_hat",
    val outfitId: String = "explorer_vest",
    val companionPetId: String = "baby_sloth",
    val currentLevel: Int = 1,
    val totalStars: Int = 12,
    val totalGems: Int = 45,
    val currentStreak: Int = 3,
    val lastPlayedDate: Long = System.currentTimeMillis(),
    val totalTimeMinutes: Int = 24,
    val soundEffectsEnabled: Boolean = true,
    val voiceNarrationEnabled: Boolean = true,
    val ttsSpeed: Float = 0.9f,
    val parentPin: String = "1234"
)
