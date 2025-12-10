package com.gate.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gate.tracker.data.local.entity.UserActivityLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    
    @Insert
    suspend fun insertActivity(activity: UserActivityLog)
    
    @Query("SELECT * FROM user_activity_log WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    suspend fun getActivitiesInRange(startTime: Long, endTime: Long): List<UserActivityLog>
    
    @Query("SELECT hourOfDay, COUNT(*) as count FROM user_activity_log WHERE timestamp >= :startTime GROUP BY hourOfDay ORDER BY hourOfDay ASC")
    suspend fun getActivityCountByHour(startTime: Long): List<HourActivityCount>
    
    @Query("SELECT hourOfDay, COUNT(*) as count FROM user_activity_log WHERE timestamp >= :startTime GROUP BY hourOfDay ORDER BY count DESC LIMIT 3")
    suspend fun getMostActiveHours(startTime: Long): List<HourActivityCount>
    
    @Query("SELECT * FROM user_activity_log ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentActivities(limit: Int): Flow<List<UserActivityLog>>
    
    @Query("DELETE FROM user_activity_log WHERE timestamp < :before")
    suspend fun deleteActivitiesBefore(before: Long)
    
    @Query("SELECT COUNT(*) FROM user_activity_log")
    suspend fun getActivityCount(): Int
}

// Data class for hour-based activity count
data class HourActivityCount(
    val hourOfDay: Int,
    val count: Int
)
