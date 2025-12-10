package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "user_activity_log")
data class UserActivityLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val activityType: ActivityType,
    val timestamp: Long,
    val hourOfDay: Int, // 0-23
    val dayOfWeek: Int  // 1-7 (Calendar.SUNDAY to Calendar.SATURDAY)
)

enum class ActivityType {
    APP_OPEN,
    CHAPTER_COMPLETE,
    SUBJECT_VIEW,
    MOCK_TEST_START
}

// Helper function to create activity log from current time
fun createActivityLog(type: ActivityType, studyDurationHours: Double = 0.0): UserActivityLog {
    val calendar = Calendar.getInstance()
    
    // For chapter completions, adjust timestamp backwards to reflect actual study time
    // If someone completes at 9 PM, they likely started at 6:30 PM (2.5 hours ago)
    if (type == ActivityType.CHAPTER_COMPLETE && studyDurationHours > 0) {
        val adjustmentMillis = (studyDurationHours * 60 * 60 * 1000).toLong()
        calendar.timeInMillis = calendar.timeInMillis - adjustmentMillis
    }
    
    return UserActivityLog(
        activityType = type,
        timestamp = calendar.timeInMillis,
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY),
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    )
}
