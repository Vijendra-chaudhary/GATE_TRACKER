package com.gate.tracker.widget

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class to store and retrieve widget data using SharedPreferences
 * This avoids coroutine issues in widget updates
 */
object WidgetDataStore {
    private const val PREFS_NAME = "widget_data"
    private const val KEY_DAYS_REMAINING = "days_remaining"
    private const val KEY_COMPLETED_CHAPTERS = "completed_chapters"
    private const val KEY_TOTAL_CHAPTERS = "total_chapters"
    private const val KEY_COMPLETED_SUBJECTS = "completed_subjects"
    private const val KEY_IN_PROGRESS_SUBJECTS = "in_progress_subjects"
    private const val KEY_NOT_STARTED_SUBJECTS = "not_started_subjects"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Save overall progress data
    fun saveProgressData(
        context: Context,
        completedChapters: Int,
        totalChapters: Int
    ) {
        getPrefs(context).edit().apply {
            putInt(KEY_COMPLETED_CHAPTERS, completedChapters)
            putInt(KEY_TOTAL_CHAPTERS, totalChapters)
            apply()
        }
    }

    // Save exam countdown
    fun saveDaysRemaining(context: Context, days: Int) {
        getPrefs(context).edit().apply {
            putInt(KEY_DAYS_REMAINING, days)
            apply()
        }
    }

    // Save subject counts
    fun saveSubjectCounts(
        context: Context,
        completed: Int,
        inProgress: Int,
        notStarted: Int
    ) {
        getPrefs(context).edit().apply {
            putInt(KEY_COMPLETED_SUBJECTS, completed)
            putInt(KEY_IN_PROGRESS_SUBJECTS, inProgress)
            putInt(KEY_NOT_STARTED_SUBJECTS, notStarted)
            apply()
        }
    }

    // Get progress data
    fun getCompletedChapters(context: Context): Int {
        return getPrefs(context).getInt(KEY_COMPLETED_CHAPTERS, 0)
    }

    fun getTotalChapters(context: Context): Int {
        return getPrefs(context).getInt(KEY_TOTAL_CHAPTERS, 152)
    }

    fun getProgressPercentage(context: Context): Int {
        val completed = getCompletedChapters(context)
        val total = getTotalChapters(context)
        return if (total > 0) (completed * 100 / total) else 0
    }

    // Get exam countdown
    fun getDaysRemaining(context: Context): Int {
        return getPrefs(context).getInt(KEY_DAYS_REMAINING, 0)
    }

    // Get subject counts
    fun getCompletedSubjects(context: Context): Int {
        return getPrefs(context).getInt(KEY_COMPLETED_SUBJECTS, 0)
    }

    fun getInProgressSubjects(context: Context): Int {
        return getPrefs(context).getInt(KEY_IN_PROGRESS_SUBJECTS, 0)
    }

    fun getNotStartedSubjects(context: Context): Int {
        return getPrefs(context).getInt(KEY_NOT_STARTED_SUBJECTS, 10)
    }
}
