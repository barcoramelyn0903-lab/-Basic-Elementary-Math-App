package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.RewardItem
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardItemDao {
    @Query("SELECT * FROM reward_items")
    fun getAllRewards(): Flow<List<RewardItem>>

    @Query("SELECT * FROM reward_items WHERE category = :category")
    fun getRewardsByCategory(category: String): Flow<List<RewardItem>>

    @Query("SELECT * FROM reward_items WHERE itemId = :itemId")
    suspend fun getRewardById(itemId: String): RewardItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RewardItem>)

    @Update
    suspend fun updateReward(item: RewardItem)

    @Query("UPDATE reward_items SET isUnlocked = 1 WHERE itemId = :itemId")
    suspend fun unlockReward(itemId: String)

    @Query("UPDATE reward_items SET isEquipped = 0 WHERE category = :category")
    suspend fun unequipCategory(category: String)

    @Query("UPDATE reward_items SET isEquipped = 1 WHERE itemId = :itemId")
    suspend fun equipItem(itemId: String)
}
