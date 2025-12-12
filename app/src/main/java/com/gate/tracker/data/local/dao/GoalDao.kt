package com.gate.tracker.data.local.dao

import androidx.room.*
import com.gate.tracker.data.local.entity.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    
    @Query("SELECT * FROM goals WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getActiveGoals(): Flow<List<GoalEntity>>
    
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<GoalEntity>>
    
    @Query("SELECT * FROM goals WHERE id = :goalId")
    suspend fun getGoalById(goalId: Int): GoalEntity?
    
    @Query("SELECT * FROM goals WHERE isActive = 1 OR (isCompleted = 1 AND isActive = 0) ORDER BY createdAt DESC LIMIT 1")
    fun getCurrentActiveGoal(): Flow<GoalEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long
    
    @Update
    suspend fun updateGoal(goal: GoalEntity)
    
    @Query("UPDATE goals SET currentProgress = :progress WHERE id = :goalId")
    suspend fun updateProgress(goalId: Int, progress: Int)
    
    @Query("UPDATE goals SET isCompleted = 1 WHERE id = :goalId")
    suspend fun markGoalCompleted(goalId: Int)
    
    @Query("UPDATE goals SET isActive = 0 WHERE id = :goalId")
    suspend fun deactivateGoal(goalId: Int)
    
    @Delete
    suspend fun deleteGoal(goal: GoalEntity)
    
    @Query("DELETE FROM goals WHERE id = :goalId")
    suspend fun deleteGoalById(goalId: Int)
}
