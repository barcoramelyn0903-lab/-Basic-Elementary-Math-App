package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.WorldLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldLevelDao {
    @Query("SELECT * FROM world_levels ORDER BY levelId ASC")
    fun getAllLevels(): Flow<List<WorldLevel>>

    @Query("SELECT * FROM world_levels WHERE levelId = :levelId")
    suspend fun getLevelById(levelId: Int): WorldLevel?

    @Query("SELECT * FROM world_levels WHERE worldId = :worldId ORDER BY levelNumber ASC")
    fun getLevelsForWorld(worldId: String): Flow<List<WorldLevel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(levels: List<WorldLevel>)

    @Update
    suspend fun updateLevel(level: WorldLevel)

    @Query("UPDATE world_levels SET isUnlocked = 1 WHERE levelId = :levelId")
    suspend fun unlockLevel(levelId: Int)
}
