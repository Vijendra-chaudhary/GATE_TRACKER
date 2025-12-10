package com.gate.tracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gate.tracker.MainActivity
import com.gate.tracker.R

/**
 * Helper class for creating and showing notifications
 */
class NotificationHelper(private val context: Context) {
    
    private val notificationManager = NotificationManagerCompat.from(context)
    
    companion object {
        // Notification IDs
        const val NOTIFICATION_ID_DAILY_REMINDER = 1001
        const val NOTIFICATION_ID_REVISION = 1002
        const val NOTIFICATION_ID_MOCK_TEST = 1003
        const val NOTIFICATION_ID_EXAM_COUNTDOWN = 1004
        const val NOTIFICATION_ID_INACTIVITY = 1005
        const val NOTIFICATION_ID_MOTIVATIONAL = 1006
        const val NOTIFICATION_ID_ACHIEVEMENT = 1007
        
        // Deep link actions
        const val ACTION_DASHBOARD = "dashboard"
        const val ACTION_SUBJECTS = "subjects"
        const val ACTION_MOCK_TESTS = "mock_tests"
        const val ACTION_SUBJECT = "subject"
        const val ACTION_SETTINGS = "settings"
    }
    
    /**
     * Create all notification channels
     */
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                createChannel(
                    NotificationChannels.STUDY_REMINDERS,
                    NotificationChannels.STUDY_REMINDERS_NAME,
                    NotificationChannels.STUDY_REMINDERS_DESC,
                    NotificationManager.IMPORTANCE_HIGH
                ),
                createChannel(
                    NotificationChannels.REVISION_ALERTS,
                    NotificationChannels.REVISION_ALERTS_NAME,
                    NotificationChannels.REVISION_ALERTS_DESC,
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                createChannel(
                    NotificationChannels.MOCK_TEST_REMINDERS,
                    NotificationChannels.MOCK_TEST_REMINDERS_NAME,
                    NotificationChannels.MOCK_TEST_REMINDERS_DESC,
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                createChannel(
                    NotificationChannels.EXAM_COUNTDOWN,
                    NotificationChannels.EXAM_COUNTDOWN_NAME,
                    NotificationChannels.EXAM_COUNTDOWN_DESC,
                    NotificationManager.IMPORTANCE_LOW
                ),
                createChannel(
                    NotificationChannels.INACTIVITY_ALERTS,
                    NotificationChannels.INACTIVITY_ALERTS_NAME,
                    NotificationChannels.INACTIVITY_ALERTS_DESC,
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                createChannel(
                    NotificationChannels.MOTIVATIONAL,
                    NotificationChannels.MOTIVATIONAL_NAME,
                    NotificationChannels.MOTIVATIONAL_DESC,
                    NotificationManager.IMPORTANCE_LOW
                ),
                createChannel(
                    NotificationChannels.ACHIEVEMENTS,
                    NotificationChannels.ACHIEVEMENTS_NAME,
                    NotificationChannels.ACHIEVEMENTS_DESC,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
            
            val systemNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channels.forEach { systemNotificationManager.createNotificationChannel(it) }
        }
    }
    
    /**
     * Create a notification channel (Android O+)
     */
    private fun createChannel(
        id: String,
        name: String,
        description: String,
        importance: Int
    ): NotificationChannel {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(id, name, importance).apply {
                this.description = description
                enableVibration(true)
                enableLights(true)
            }
        } else {
            throw IllegalStateException("NotificationChannel requires API 26+")
        }
    }
    
    /**
     * Show daily study reminder notification
     */
    fun showDailyReminder(pendingChapters: Int) {
        val intent = createDeepLinkIntent(ACTION_DASHBOARD)
        val pendingIntent = createPendingIntent(intent, NOTIFICATION_ID_DAILY_REMINDER)
        
        val notification = NotificationCompat.Builder(context, NotificationChannels.STUDY_REMINDERS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Time to Study! 📚")
            .setContentText("You have $pendingChapters pending chapters")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER, notification)
    }
    
    /**
     * Show revision alert notification
     */
    fun showRevisionAlert(chaptersCount: Int) {
        val intent = createDeepLinkIntent(ACTION_SUBJECTS)
        val pendingIntent = createPendingIntent(intent, NOTIFICATION_ID_REVISION)
        
        val notification = NotificationCompat.Builder(context, NotificationChannels.REVISION_ALERTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Revision Reminder 🔄")
            .setContentText("$chaptersCount ${if (chaptersCount == 1) "chapter" else "chapters"} need${if (chaptersCount == 1) "s" else ""} revision")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_REVISION, notification)
    }
    
    /**
     * Show mock test reminder notification
     */
    fun showMockTestReminder(daysSinceLastTest: Int) {
        val intent = createDeepLinkIntent(ACTION_MOCK_TESTS)
        val pendingIntent = createPendingIntent(intent, NOTIFICATION_ID_MOCK_TEST)
        
        val notification = NotificationCompat.Builder(context, NotificationChannels.MOCK_TEST_REMINDERS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Mock Test Time! 📝")
            .setContentText("It's been $daysSinceLastTest days since your last test")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_MOCK_TEST, notification)
    }
    
    /**
     * Show exam countdown notification
     */
    fun showExamCountdown(daysRemaining: Int, message: String) {
        val intent = createDeepLinkIntent(ACTION_DASHBOARD)
        val pendingIntent = createPendingIntent(intent, NOTIFICATION_ID_EXAM_COUNTDOWN)
        
        val notification = NotificationCompat.Builder(context, NotificationChannels.EXAM_COUNTDOWN)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Exam in $daysRemaining Days! 📅")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_EXAM_COUNTDOWN, notification)
    }
    
    /**
     * Show inactivity alert notification
     */
    fun showInactivityAlert(daysSinceActivity: Int) {
        val intent = createDeepLinkIntent(ACTION_DASHBOARD)
        val pendingIntent = createPendingIntent(intent, NOTIFICATION_ID_INACTIVITY)
        
        val notification = NotificationCompat.Builder(context, NotificationChannels.INACTIVITY_ALERTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("We Miss You! 😴")
            .setContentText("It's been $daysSinceActivity days since you studied")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_INACTIVITY, notification)
    }
    
    /**
     * Show motivational notification
     */
    fun showMotivationalQuote(quote: String) {
        val intent = createDeepLinkIntent(ACTION_DASHBOARD)
        val pendingIntent = createPendingIntent(intent, NOTIFICATION_ID_MOTIVATIONAL)
        
        val notification = NotificationCompat.Builder(context, NotificationChannels.MOTIVATIONAL)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Stay Motivated! ✨")
            .setContentText(quote)
            .setStyle(NotificationCompat.BigTextStyle().bigText(quote))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_MOTIVATIONAL, notification)
    }
    
    /**
     * Show achievement notification
     */
    fun showAchievement(title: String, message: String) {
        val intent = createDeepLinkIntent(ACTION_DASHBOARD)
        val pendingIntent = createPendingIntent(intent, NOTIFICATION_ID_ACHIEVEMENT)
        
        val notification = NotificationCompat.Builder(context, NotificationChannels.ACHIEVEMENTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_ACHIEVEMENT, notification)
    }
    
    /**
     * Create deep link intent
     */
    private fun createDeepLinkIntent(action: String, extraData: String? = null): Intent {
        return Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("notification_action", action)
            extraData?.let { putExtra("extra_data", it) }
        }
    }
    
    /**
     * Create pending intent
     */
    private fun createPendingIntent(intent: Intent, requestCode: Int): PendingIntent {
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    /**
     * Cancel a specific notification
     */
    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }
    
    /**
     * Show backup notification
     */
    fun showBackupNotification(title: String, message: String) {
        val intent = createDeepLinkIntent(ACTION_SETTINGS)
        val pendingIntent = createPendingIntent(intent, 2001)
        
        val notification = NotificationCompat.Builder(context, NotificationChannels.ACHIEVEMENTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(2001, notification)
    }
    
    /**
     * Cancel all notifications
     */
    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }
}
