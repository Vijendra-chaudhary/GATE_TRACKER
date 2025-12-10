package com.gate.tracker.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

/**
 * Utilities for capturing composables as bitmaps and sharing them
 */
object BitmapUtils {
    
    /**
     * Save bitmap to cache directory and return URI
     */
    fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        
        val file = File(cachePath, "progress_share_${System.currentTimeMillis()}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
    
    /**
     * Share image using Android share intent
     */
    fun shareImage(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, "Check out my GATE exam progress! 📚\n\n#GATEPreparation #StudyProgress")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        context.startActivity(
            Intent.createChooser(intent, "Share Progress")
        )
    }
}
