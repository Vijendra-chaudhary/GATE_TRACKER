package com.gate.tracker.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.gate.tracker.MainActivity
import com.gate.tracker.R

/**
 * Widget showing GATE exam progress
 * Updates automatically every hour
 */
class ProgressWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_progress)

            // Set up click intent to open app
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_days_count, pendingIntent)

            // Set default values - widget shows placeholder until app is opened
            //  Widget will auto-update when exam date/progress changes in app
            views.setTextViewText(R.id.widget_days_count, "120")
            views.setTextViewText(R.id.widget_progress_count, "Track Progress")
            views.setTextViewText(R.id.widget_progress_percentage, "Tap to open")
            views.setProgressBar(R.id.widget_progress_bar, 100, 0, false)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
