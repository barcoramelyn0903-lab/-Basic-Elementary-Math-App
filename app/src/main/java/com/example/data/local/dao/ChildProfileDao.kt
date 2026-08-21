package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ChildProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildProfileDao {
    @Query("SELECT * FROM child_profile WHERE id = 1")
    fun getProfile(): Flow<ChildProfile?>

    @Query("SELECT * FROM child_profile WHERE id = 1")
    suspend fun getProfileSync(): ChildProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ChildProfile)

    @Update
    suspend fun updateProfile(profile: ChildProfile)

    @Query("UPDATE child_profile SET totalStars = totalStars + :stars, totalGems = totalGems + :gems WHERE id = 1")
    suspend fun addCurrency(stars: Int, gems: Int)

    @Query("UPDATE child_profile SET selectedAvatar = :avatar, hatId = :hat, outfitId = :outfit, companionPetId = :pet WHERE id = 1")
    suspend fun updateCustomization(avatar: String, hat: String, outfit: String, pet: String)
}
