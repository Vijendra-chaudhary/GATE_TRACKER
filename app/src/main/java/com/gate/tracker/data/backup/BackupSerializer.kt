package com.gate.tracker.data.backup

import com.gate.tracker.data.model.BackupData
import com.gate.tracker.data.model.BackupMetadata
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException

/**
 * Handles serialization and deserialization of backup data
 */
class BackupSerializer {
    
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
    
    /**
     * Serialize backup data to JSON string
     */
    fun serialize(backupData: BackupData): String {
        return gson.toJson(backupData)
    }
    
    /**
     * Deserialize JSON string to backup data
     * @return Result with BackupData or error message
     */
    fun deserialize(json: String): Result<BackupData> {
        return try {
            val backupData = gson.fromJson(json, BackupData::class.java)
            
            // Validate the backup data
            validateBackupData(backupData)?.let { error ->
                return Result.failure(Exception(error))
            }
            
            Result.success(backupData)
        } catch (e: JsonSyntaxException) {
            Result.failure(Exception("Invalid backup file format: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to read backup: ${e.message}"))
        }
    }
    
    /**
     * Validate backup data structure
     * @return Error message if invalid, null if valid
     */
    private fun validateBackupData(backupData: BackupData): String? {
        // Check metadata
        if (backupData.metadata.version > BackupMetadata.CURRENT_BACKUP_VERSION) {
            return "Backup version ${backupData.metadata.version} is not supported. Please update the app."
        }
        
        // Check basic data integrity
        if (backupData.subjects.isEmpty()) {
            return "Backup contains no subject data"
        }
        
        if (backupData.chapters.isEmpty()) {
            return "Backup contains no chapter data"
        }
        
        // Validate subject-chapter relationships
        val subjectIds = backupData.subjects.map { it.id }.toSet()
        val orphanedChapters = backupData.chapters.filter { it.subjectId !in subjectIds }
        if (orphanedChapters.isNotEmpty()) {
            return "Backup contains chapters without valid subjects"
        }
        
        // Validate chapter-note relationships
        val chapterIds = backupData.chapters.map { it.id }.toSet()
        val orphanedNotes = backupData.notes.filter { it.chapterId !in chapterIds }
        if (orphanedNotes.isNotEmpty()) {
            return "Backup contains notes without valid chapters"
        }
        
        return null
    }
    
    /**
     * Check if backup is compatible with current branch
     */
    fun isCompatibleBranch(backupData: BackupData, currentBranchId: Int): Boolean {
        return backupData.metadata.branchId == currentBranchId
    }
    
    /**
     * Get human-readable backup summary
     */
    fun getBackupSummary(backupData: BackupData): String {
        val subjectCount = backupData.subjects.size
        val completedChapters = backupData.chapters.count { it.isCompleted }
        val totalChapters = backupData.chapters.size
        val noteCount = backupData.notes.size
        
        return buildString {
            appendLine("Branch: ${backupData.metadata.branchName}")
            appendLine("Subjects: $subjectCount")
            appendLine("Progress: $completedChapters/$totalChapters chapters")
            appendLine("Notes: $noteCount")
            appendLine("Created: ${formatTimestamp(backupData.metadata.timestamp)}")
        }
    }
    
    private fun formatTimestamp(timestamp: Long): String {
        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
        return dateFormat.format(java.util.Date(timestamp))
    }
}
