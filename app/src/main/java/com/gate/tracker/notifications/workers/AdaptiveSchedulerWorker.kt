package com.gate.tracker.notifications.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gate.tracker.data.local.GateDatabase
import com.gate.tracker.data.repository.GateRepository
import com.gate.tracker.notifications.NotificationScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker that calculates optimal notification time based on user activity
 * and schedules the daily reminder accordingly
 */
class AdaptiveSchedulerWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.Default) {
            try {
                val database = GateDatabase.getInstance(applicationContext)
                val repository = GateRepository(database)
                
                // Get optimal study hour from activity analysis
                val optimalHour = repository.getOptimalStudyHour()
                
                Log.d("AdaptiveScheduler", "Calculated optimal study hour: $optimalHour")
                
                // Schedule daily reminder at optimal hour
                NotificationScheduler.scheduleDailyReminderAtHour(applicationContext, optimalHour)
                
                // Reschedule this worker to run weekly to readapt
                scheduleWeeklyCheck(applicationContext)
                
                Result.success()
            } catch (e: Exception) {
                Log .e("AdaptiveScheduler", "Failed to calculate optimal hour", e)
                
                // Fall back to default hour (9 AM) on error
                NotificationScheduler.scheduleDailyReminderAtHour(applicationContext, 9)
                Result.failure()
            }
        }
    }
    
    companion object {
        private const val TAG_WEEKLY_ADAPTIVE_CHECK = "weekly_adaptive_check"
        
        fun scheduleWeeklyCheck(context: Context) {
            val weeklyCheckRequest = androidx.work.PeriodicWorkRequestBuilder<AdaptiveSchedulerWorker>(
                7, java.util.concurrent.TimeUnit.DAYS
            )
                .setConstraints(
                    androidx.work.Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .addTag(TAG_WEEKLY_ADAPTIVE_CHECK)
                .build()
            
            androidx.work.WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                TAG_WEEKLY_ADAPTIVE_CHECK,
                androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
                weeklyCheckRequest
            )
        }
    }
}
