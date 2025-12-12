package com.gate.tracker.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {
    
    /**
     * Format a timestamp to a readable date string
     * @param timestamp The timestamp in milliseconds
     * @param format The desired format (default: "dd MMM yyyy")
     * @return Formatted date string
     */
    fun formatDate(timestamp: Long?, format: String = "dd MMM yyyy"): String {
        if (timestamp == null) return "Not completed"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * Format a timestamp to a readable date and time string
     * @param timestamp The timestamp in milliseconds
     * @return Formatted date-time string
     */
    fun formatDateTime(timestamp: Long?): String {
        return formatDate(timestamp, "dd MMM yyyy, hh:mm a")
    }
    
    /**
     * Get a relative time string (e.g., "2 days ago", "Today", "Yesterday")
     * @param timestamp The timestamp in milliseconds
     * @return Relative time string
     */
    fun getRelativeTime(timestamp: Long?): String {
        if (timestamp == null) return "Not completed"
        
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 0 -> "Just now"
            diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
            diff < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                "$minutes minute${if (minutes != 1L) "s" else ""} ago"
            }
            diff < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                if (hours == 1L) "1 hour ago" else "$hours hours ago"
            }
            diff < TimeUnit.DAYS.toMillis(2) -> "Yesterday"
            diff < TimeUnit.DAYS.toMillis(7) -> {
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                "$days days ago"
            }
            diff < TimeUnit.DAYS.toMillis(30) -> {
                val weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7
                if (weeks == 1L) "1 week ago" else "$weeks weeks ago"
            }
            diff < TimeUnit.DAYS.toMillis(365) -> {
                val months = TimeUnit.MILLISECONDS.toDays(diff) / 30
                if (months == 1L) "1 month ago" else "$months months ago"
            }
            else -> {
                val years = TimeUnit.MILLISECONDS.toDays(diff) / 365
                if (years == 1L) "1 year ago" else "$years years ago"
            }
        }
    }
    
    /**
     * Check if two timestamps are on the same day
     */
    fun isSameDay(timestamp1: Long?, timestamp2: Long?): Boolean {
        if (timestamp1 == null || timestamp2 == null) return false
        
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date(timestamp1)) == sdf.format(Date(timestamp2))
    }
    
    /**
     * Get the start of day (00:00:00) for a given timestamp
     */
    fun getStartOfDay(timestamp: Long): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    /**
     * Get the end of day (23:59:59) for a given timestamp
     */
    fun getEndOfDay(timestamp: Long): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
        calendar.set(java.util.Calendar.MINUTE, 59)
        calendar.set(java.util.Calendar.SECOND, 59)
        calendar.set(java.util.Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
    
    /**
     * Get the number of days in a specific month
     */
    fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(year, month, 1)
        return calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
    }
    
    /**
     * Get month name from month number (0-11)
     */
    fun getMonthName(month: Int): String {
        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.MONTH, month)
        return sdf.format(calendar.time)
    }
    
    /**
     * Get the day of week for the first day of the month (1=Sunday, 7=Saturday)
     */
    fun getFirstDayOfWeek(year: Int, month: Int): Int {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(year, month, 1)
        return calendar.get(java.util.Calendar.DAY_OF_WEEK)
    }
    
    /**
     * Get timestamp for a specific date
     */
    fun getTimestampForDate(year: Int, month: Int, day: Int): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(year, month, day, 0, 0, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    /**
     * Get day of month from timestamp
     */
    fun getDayOfMonth(timestamp: Long): Int {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(java.util.Calendar.DAY_OF_MONTH)
    }
    
    /**
     * Format date as simple string for grouping (yyyyMMdd)
     */
    fun getDateKey(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
