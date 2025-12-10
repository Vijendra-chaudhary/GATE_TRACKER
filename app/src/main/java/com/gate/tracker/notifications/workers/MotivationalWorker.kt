package com.gate.tracker.notifications.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gate.tracker.data.local.GateDatabase
import com.gate.tracker.data.repository.GateRepository
import com.gate.tracker.notifications.NotificationHelper

/**
 * Worker for motivational quote notifications
 */
class MotivationalWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val motivationalQuotes = listOf(
        "Success is the sum of small efforts repeated daily 💪",
        "You're one step closer to your goal 🎯",
        "Every chapter completed is progress made! 📚",
        "Consistency beats intensity. Keep going! 🔥",
        "Believe in yourself. You can do this! ✨",
        "Hard work always pays off 💎",
        "Stay focused on your dreams 🌟",
        "One day or day one. You decide! ⚡",
        "Your future is created by what you do today 🚀",
        "Great things take time. Keep pushing! 💫",
        "The only way out is through 🛤️",
        "You're stronger than you think 💪",
        "Small progress is still progress 📈",
        "Don't stop until you're proud! 🏆",
        "Success doesn't come easy, but it's worth it 🎖️",
        // Hindi quotes
        "कड़ी मेहनत का फल मीठा होता है 🍯",
        "हर दिन एक नया मौका है 🌅",
        "सफलता उन्हीं को मिलती है जो हार नहीं मानते 🏅",
        "आज की मेहनत, कल की सफलता 💯",
        "अपने सपनों को हकीकत बनाओ ✨",
        "मंजिल उन्हीं को मिलती है, जिनके सपनों में जान होती है 🎯",
        "धैर्य और मेहनत से सब कुछ संभव है 🙏",
        "हिम्मत मत हारो, तुम कर सकते हो! 💪",
        "छोटे-छोटे कदम भी मंजिल तक ले जाते हैं 👣",
        "सफलता का कोई शॉर्टकट नहीं होता 🛤️",
        "आज का संघर्ष, कल का गौरव 🌟",
        "तुम्हारी मेहनत रंग लाएगी 🌈"
    )
    
    override suspend fun doWork(): Result {
        return try {
            val database = GateDatabase.getInstance(applicationContext)
            val repository = GateRepository(database)
            val notificationHelper = NotificationHelper(applicationContext)
            
            // Check if enabled
            val prefs = repository.getNotificationPreferencesOnce()
            if (prefs?.motivationalEnabled != true) {
                return Result.success()
            }
            
            // Pick random quote
            val quote = motivationalQuotes.random()
            notificationHelper.showMotivationalQuote(quote)
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
