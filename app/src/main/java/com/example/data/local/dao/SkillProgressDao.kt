package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.SkillProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillProgressDao {
    @Query("SELECT * FROM skill_progress")
    fun getAllSkillProgress(): Flow<List<SkillProgress>>

    @Query("SELECT * FROM skill_progress WHERE skillId = :skillId")
    suspend fun getSkillProgress(skillId: String): SkillProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: SkillProgress)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<SkillProgress>)

    @Update
    suspend fun update(progress: SkillProgress)
}
