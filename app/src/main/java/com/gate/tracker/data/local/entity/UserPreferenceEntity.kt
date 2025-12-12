package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preference")
data class UserPreferenceEntity(
    @PrimaryKey
    val id: Int = 1,
    val selectedBranchId: Int? = null,
    val isFirstLaunch: Boolean = true,
    val longestStreak: Int = 0,
    val isRevisionMode: Boolean = false, // Toggle for revision mode
    val themeMode: Int = THEME_SYSTEM // 0=System, 1=Light, 2=Dark
) {
    companion object {
        const val THEME_SYSTEM = 0
        const val THEME_LIGHT = 1
        const val THEME_DARK = 2
    }
}
