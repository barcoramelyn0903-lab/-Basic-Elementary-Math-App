package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.ChildProfile
import com.example.data.local.entity.RewardItem
import com.example.data.local.entity.SkillProgress
import com.example.data.local.entity.WorldLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MathRepository(private val database: AppDatabase) {

    val profile: Flow<ChildProfile?> = database.childProfileDao().getProfile()
    val allSkills: Flow<List<SkillProgress>> = database.skillProgressDao().getAllSkillProgress()
    val allLevels: Flow<List<WorldLevel>> = database.worldLevelDao().getAllLevels()
    val allRewards: Flow<List<RewardItem>> = database.rewardItemDao().getAllRewards()

    suspend fun getProfileSync(): ChildProfile {
        return database.childProfileDao().getProfileSync() ?: ChildProfile()
    }

    suspend fun saveProfile(profile: ChildProfile) {
        database.childProfileDao().insertProfile(profile)
    }

    suspend fun updateCustomization(avatar: String, hat: String, outfit: String, pet: String) {
        database.childProfileDao().updateCustomization(avatar, hat, outfit, pet)
    }

    suspend fun recordQuestionResult(
        skillId: String,
        isCorrect: Boolean,
        usedHint: Boolean
    ) {
        val existing = database.skillProgressDao().getSkillProgress(skillId)
        val currentAttempted = (existing?.totalAttempted ?: 0) + 1
        val currentCorrect = (existing?.correctCount ?: 0) + (if (isCorrect) 1 else 0)
        val currentHints = (existing?.hintsUsed ?: 0) + (if (usedHint) 1 else 0)
        val streak = if (isCorrect) (existing?.currentStreak ?: 0) + 1 else 0
        val mastery = if (currentAttempted > 0) ((currentCorrect.toDouble() / currentAttempted) * 100).toInt() else 0

        val label = when {
            mastery >= 85 && currentAttempted >= 10 -> "Mastered"
            mastery >= 65 -> "Developing"
            mastery >= 40 -> "Practicing"
            else -> "Beginning"
        }

        val updated = SkillProgress(
            skillId = skillId,
            subjectName = existing?.subjectName ?: skillId,
            totalAttempted = currentAttempted,
            correctCount = currentCorrect,
            hintsUsed = currentHints,
            masteryPercentage = mastery,
            currentStreak = streak,
            lastPracticedTimestamp = System.currentTimeMillis(),
            statusLabel = label
        )

        database.skillProgressDao().insertOrUpdate(updated)

        // Award stars & gems on correct answers
        if (isCorrect) {
            val starBonus = if (usedHint) 1 else 2
            val gemBonus = if (usedHint) 2 else 5
            database.childProfileDao().addCurrency(starBonus, gemBonus)
        }
    }

    suspend fun completeLevel(levelId: Int, stars: Int, gemsEarned: Int) {
        val level = database.worldLevelDao().getLevelById(levelId)
        if (level != null) {
            val bestStars = maxOf(level.starsEarned, stars)
            val updated = level.copy(
                starsEarned = bestStars,
                isCompleted = true
            )
            database.worldLevelDao().updateLevel(updated)

            // Unlock next level
            database.worldLevelDao().unlockLevel(levelId + 1)

            // Add currency & update level
            database.childProfileDao().addCurrency(stars, gemsEarned)
        }
    }

    suspend fun unlockAndEquipReward(item: RewardItem) {
        val currentProfile = getProfileSync()
        if (currentProfile.totalGems >= item.costGems || item.isUnlocked) {
            if (!item.isUnlocked) {
                database.childProfileDao().addCurrency(0, -item.costGems)
                database.rewardItemDao().unlockReward(item.itemId)
            }
            // Equip
            database.rewardItemDao().unequipCategory(item.category)
            database.rewardItemDao().equipItem(item.itemId)

            // Update child profile representation
            when (item.category) {
                "HAT" -> saveProfile(currentProfile.copy(hatId = item.itemId))
                "OUTFIT" -> saveProfile(currentProfile.copy(outfitId = item.itemId))
                "PET" -> saveProfile(currentProfile.copy(companionPetId = item.itemId))
            }
        }
    }

    suspend fun updateSettings(
        soundEnabled: Boolean,
        voiceEnabled: Boolean,
        ttsSpeed: Float,
        parentPin: String
    ) {
        val p = getProfileSync()
        saveProfile(
            p.copy(
                soundEffectsEnabled = soundEnabled,
                voiceNarrationEnabled = voiceEnabled,
                ttsSpeed = ttsSpeed,
                parentPin = parentPin
            )
        )
    }

    suspend fun resetAllProgress() {
        AppDatabase.populateInitialData(database)
    }
}
