package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GoalType {
    WEEKLY_CHAPTERS,
    DAILY_CHAPTERS,
    SUBJECT_COMPLETION
}

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val goalType: String, // Stored as String for Room compatibility
    val targetValue: Int,
    val currentProgress: Int = 0,
    val startDate: Long,
    val endDate: Long,
    val isActive: Boolean = true,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
