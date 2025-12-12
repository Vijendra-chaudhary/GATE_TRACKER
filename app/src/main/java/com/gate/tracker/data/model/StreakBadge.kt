package com.gate.tracker.data.model

import androidx.compose.ui.graphics.Color

enum class StreakBadge(
    val title: String,
    val emoji: String,
    val daysRequired: Int,
    val color: Color,
    val gradientStart: Color,
    val gradientEnd: Color
) {
    NONE(
        "Beginner",
        "📅",
        0,
        Color(0xFF94A3B8),
        Color(0xFF94A3B8),
        Color(0xFFCBD5E1)
    ),
    BRONZE(
        "Bronze Warrior",
        "🥉",
        7,
        Color(0xFFCD7F32),
        Color(0xFFCD7F32),
        Color(0xFFE59866)
    ),
    SILVER(
        "Silver Champion",
        "🥈",
        14,
        Color(0xFFC0C0C0),
        Color(0xFFC0C0C0),
        Color(0xFFE8E8E8)
    ),
    GOLD(
        "Gold Legend",
        "🥇",
        30,
        Color(0xFFFFD700),
        Color(0xFFFFD700),
        Color(0xFFFFF4B8)
    ),
    DIAMOND(
        "Diamond Master",
        "💎",
        100,
        Color(0xFFB9F2FF),
        Color(0xFF4FC3F7),
        Color(0xFFB9F2FF)
    );
    
    companion object {
        fun fromStreak(streak: Int): StreakBadge {
            return values()
                .filter { it.daysRequired <= streak }
                .maxByOrNull { it.daysRequired }
                ?: NONE
        }
        
        fun getNext(current: StreakBadge): StreakBadge? {
            val currentIndex = values().indexOf(current)
            return if (currentIndex < values().size - 1) {
                values()[currentIndex + 1]
            } else null
        }
    }
}
