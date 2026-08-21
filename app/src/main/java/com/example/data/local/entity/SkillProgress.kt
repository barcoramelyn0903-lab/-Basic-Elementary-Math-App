package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skill_progress")
data class SkillProgress(
    @PrimaryKey val skillId: String, // ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, FRACTIONS, COMPARISON, COUNTING, WORD_PROBLEMS
    val subjectName: String,
    val totalAttempted: Int = 0,
    val correctCount: Int = 0,
    val hintsUsed: Int = 0,
    val masteryPercentage: Int = 0,
    val currentStreak: Int = 0,
    val lastPracticedTimestamp: Long = System.currentTimeMillis(),
    val statusLabel: String = "Beginning" // Beginning, Practicing, Developing, Mastered
)
