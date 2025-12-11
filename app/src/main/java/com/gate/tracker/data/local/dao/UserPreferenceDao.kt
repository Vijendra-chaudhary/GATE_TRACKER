package com.gate.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gate.tracker.data.local.entity.UserPreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferenceDao {
    @Query("SELECT * FROM user_preference WHERE id = 1")
    fun getPreference(): Flow<UserPreferenceEntity?>
    
    @Query("SELECT * FROM user_preference WHERE id = 1")
    suspend fun getPreferenceSync(): UserPreferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreference(preference: UserPreferenceEntity)

    @Query("UPDATE user_preference SET selectedBranchId = :branchId WHERE id = 1")
    suspend fun updateBranchSelection(branchId: Int)

    @Query("UPDATE user_preference SET isFirstLaunch = :isFirstLaunch WHERE id = 1")
    suspend fun updateFirstLaunch(isFirstLaunch: Boolean)
    
    @Query("UPDATE user_preference SET selectedBranchId = 0, isFirstLaunch = 1 WHERE id = 1")
    suspend fun resetPreferences()
    
    @Query("UPDATE user_preference SET longestStreak = :streak WHERE id = 1")
    suspend fun updateLongestStreak(streak: Int)
    
    
    @Query("UPDATE user_preference SET longestStreak = 0 WHERE id = 1")
    suspend fun resetStreakData()
    
    @Query("UPDATE user_preference SET isRevisionMode = :enabled WHERE id = 1")
    suspend fun setRevisionMode(enabled: Boolean)
    
    @Query("UPDATE user_preference SET themeMode = :mode WHERE id = 1")
    suspend fun updateThemeMode(mode: Int)
}
