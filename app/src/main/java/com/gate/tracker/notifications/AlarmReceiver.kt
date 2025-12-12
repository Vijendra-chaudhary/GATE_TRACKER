package com.gate.tracker.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule all alarms on boot
            NotificationScheduler.scheduleAll(context)
            return
        }

        val type = intent.getStringExtra("TYPE") ?: return


        Log.d("AlarmReceiver", "Received alarm for type: $type")

        when (type) {
            NotificationScheduler.TAG_DAILY_REMINDER -> {
                // Determine pending chapters logic if possible, or just show generic
                // For exact data, we might need a Worker to fetch DB, 
                // but for speed/reliability we can show a general message or 
                // launch a OneTimeWorker to fetch data and notify.
                // Given "pending chapters" requires DB, let's use OneTimeWorker to handle the data fetching part
                // immediately. Or simpler: direct notification if we pass data in intent? 
                // Data changes dynamically, so fetching is better.
                NotificationScheduler.triggerImmediateWorker(context, type)
            }
            NotificationScheduler.TAG_REVISION_ALERT -> {
                NotificationScheduler.triggerImmediateWorker(context, type)
            }
            NotificationScheduler.TAG_MOCK_TEST_REMINDER -> {
                NotificationScheduler.triggerImmediateWorker(context, type)
            }
            NotificationScheduler.TAG_EXAM_COUNTDOWN -> {
                NotificationScheduler.triggerImmediateWorker(context, type)
            }
            NotificationScheduler.TAG_INACTIVITY_ALERT -> {
                 NotificationScheduler.triggerImmediateWorker(context, type)
            }
            NotificationScheduler.TAG_MOTIVATIONAL -> {
                 NotificationScheduler.triggerImmediateWorker(context, type)
            }
        }
    }
}
