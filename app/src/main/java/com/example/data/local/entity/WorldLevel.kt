package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "world_levels")
data class WorldLevel(
    @PrimaryKey val levelId: Int,
    val worldId: String, // banana_grove, coconut_falls, animal_savanna, fraction_feast, croc_river, treasure_temple
    val worldName: String,
    val levelNumber: Int,
    val title: String,
    val subject: String,
    val targetQuestions: Int = 5,
    val starsEarned: Int = 0, // 0, 1, 2, 3
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val iconEmoji: String = "🌴",
    val description: String = ""
)
