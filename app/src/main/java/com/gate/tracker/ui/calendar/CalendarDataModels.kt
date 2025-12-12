package com.gate.tracker.ui.calendar

import androidx.compose.ui.graphics.Color

data class SubjectProgress(
    val subjectId: Int,
    val subjectName: String,
    val completedCount: Int,
    val percentage: Float,
    val color: Color
)

data class WeekdayActivity(
    val dayOfWeek: Int, // 1 = Sunday, 7 = Saturday (Calendar.DAY_OF_WEEK)
    val dayName: String,
    val count: Int,
    val percentage: Float
)
