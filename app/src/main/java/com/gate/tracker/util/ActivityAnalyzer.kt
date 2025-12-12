package com.gate.tracker.util

import com.gate.tracker.data.local.dao.HourActivityCount
import com.gate.tracker.data.local.entity.UserActivityLog
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.exp

/**
 * Analyzes user activity patterns to determine optimal notification times
 */
object ActivityAnalyzer {
    
    private const val MIN_DAYS_FOR_ANALYSIS = 7
    private const val ANALYSIS_WINDOW_DAYS = 30
    private const val DEFAULT_HOUR = 9 // 9 AM default
    private const val MIN_CONFIDENCE_THRESHOLD = 0.7
    
    /**
     * Calculate optimal notification hour based on user activity history
     * Returns pair of (hour, confidence_score)
     */
    suspend fun calculateOptimalHour(
        activities: List<UserActivityLog>,
        hourCounts: List<HourActivityCount>
    ): Pair<Int, Double> {
        // Check if we have enough data
        if (activities.isEmpty() || hourCounts.isEmpty()) {
            return Pair(DEFAULT_HOUR, 0.0)
        }
        
        // Check if data spans at least MIN_DAYS_FOR_ANALYSIS
        val oldestActivity = activities.minByOrNull { it.timestamp }?.timestamp ?: 0L
        val daysSinceOldest = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - oldestActivity)
        
        if (daysSinceOldest < MIN_DAYS_FOR_ANALYSIS) {
            return Pair(DEFAULT_HOUR, 0.0)
        }
        
        // Apply exponential decay weighting (recent activities weighted higher)
        val weightedHours = applyExponentialDecay(activities)
        
        // Find peak activity hour
        val peakHour = weightedHours.maxByOrNull { it.value }?.key ?: DEFAULT_HOUR
        
        // Calculate 30 minutes before peak for notification
        val notificationHour = if (peakHour > 0) peakHour - 1 else 23
        
        // Calculate confidence score
        val confidence = calculateConfidence(weightedHours, activities.size, daysSinceOldest.toInt())
        
        return Pair(notificationHour, confidence)
    }
    
    /**
     * Apply exponential decay to weight recent activities higher
     * Returns map of hour -> weighted count
     */
    private fun applyExponentialDecay(activities: List<UserActivityLog>): Map<Int, Double> {
        val currentTime = System.currentTimeMillis()
        val decayRate = 0.1 // Decay rate per day
        
        val weightedCounts = mutableMapOf<Int, Double>()
        
        activities.forEach { activity ->
            val daysSince = TimeUnit.MILLISECONDS.toDays(currentTime - activity.timestamp)
            val weight = exp(-decayRate * daysSince)
            
            weightedCounts[activity.hourOfDay] = 
                (weightedCounts[activity.hourOfDay] ?: 0.0) + weight
        }
        
        return weightedCounts
    }
    
    /**
     * Calculate confidence score for the prediction
     * Factors: data points, distribution uniformity, time span
     */
    private fun calculateConfidence(
        weightedHours: Map<Int, Double>,
        totalActivities: Int,
        daysSinceFirst: Int
    ): Double {
        if (totalActivities < 10) return 0.0
        
        // Factor 1: More data = higher confidence (max 0.4)
        val dataFactor = minOf(totalActivities / 100.0, 0.4)
        
        // Factor 2: Longer time span = higher confidence (max 0.3)
        val timeFactor = minOf(daysSinceFirst / 30.0,  0.3)
        
        // Factor 3: Clear peak = higher confidence (max 0.3)
        val maxCount = weightedHours.values.maxOrNull() ?: 0.0
        val avgCount = weightedHours.values.average()
        val peakRatio = if (avgCount > 0) maxCount / avgCount else 0.0
        val peakFactor = minOf((peakRatio - 1.0) / 3.0, 0.3)
        
        return dataFactor + timeFactor + peakFactor
    }
    
    /**
     * Get recommended notification time
     * Falls back to default hour if confidence is too low
     */
    suspend fun getRecommendedHour(
        activities: List<UserActivityLog>,
        hourCounts: List<HourActivityCount>
    ): Int {
        val (optimalHour, confidence) = calculateOptimalHour(activities, hourCounts)
        
        return if (confidence >= MIN_CONFIDENCE_THRESHOLD) {
            optimalHour
        } else {
            DEFAULT_HOUR
        }
    }
    
    /**
     * Get activity statistics for debugging/display
     */
    fun getActivityStats(activities: List<UserActivityLog>): ActivityStats {
        val daysSinceFirst = if (activities.isNotEmpty()) {
            val oldest = activities.minByOrNull { it.timestamp }?.timestamp ?: 0L
            TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - oldest).toInt()
        } else 0
        
        val mostCommonHour = activities
            .groupBy { it.hourOfDay }
            .maxByOrNull { it.value.size }
            ?.key ?: DEFAULT_HOUR
        
        return ActivityStats(
            totalActivities = activities.size,
            daysSinceFirst = daysSinceFirst,
            mostCommonHour = mostCommonHour,
            uniqueDays = activities.map { 
                TimeUnit.MILLISECONDS.toDays(it.timestamp) 
            }.distinct().size
        )
    }
}

/**
 * Statistics about user activity for debugging/display
 */
data class ActivityStats(
    val totalActivities: Int,
    val daysSinceFirst: Int,
    val mostCommonHour: Int,
    val uniqueDays: Int
)
