package com.gate.tracker.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.gate.tracker.MainActivity
import com.gate.tracker.R

class OverallProgressWidgetProvider : AppWidgetProvider() {

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
            val views = RemoteViews(context.packageName, R.layout.widget_overall_progress)

            // Set up click intent to open app
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_percentage, pendingIntent)

            // Load data from SharedPreferences
            val completed = WidgetDataStore.getCompletedChapters(context)
            val total = WidgetDataStore.getTotalChapters(context)
            val percentage = WidgetDataStore.getProgressPercentage(context)

            // Update widget views
            views.setTextViewText(R.id.widget_percentage, "$percentage%")
            views.setTextViewText(R.id.widget_chapters, "$completed / $total chapters")
            views.setProgressBar(R.id.widget_progress, 100, percentage, false)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
