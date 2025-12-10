package com.gate.tracker.data.drive

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Represents a backup file stored in Google Drive
 */
data class DriveBackupFile(
    val id: String,
    val name: String,
    val createdTime: Long,
    val size: Long
)

/**
 * Manages Google Drive operations for backup/restore
 */
class DriveManager(private val context: Context) {
    
    companion object {
        private const val APP_FOLDER_NAME = "GATE_Tracker_Backups"
        private const val BACKUP_MIME_TYPE = "application/json"
    }
    
    private var driveService: Drive? = null
    private var signInClient: GoogleSignInClient? = null
    
    /**
     * Get Google Sign-In client
     */
    fun getSignInClient(): GoogleSignInClient {
        if (signInClient == null) {
            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .build()
            
            signInClient = GoogleSignIn.getClient(context, signInOptions)
        }
        return signInClient!!
    }
    
    /**
     * Get sign-in intent for activity result launcher
     */
    fun getSignInIntent(): Intent {
        return getSignInClient().signInIntent
    }
    
    /**
     * Initialize Drive service with signed-in account
     */
    fun initializeDriveService(account: GoogleSignInAccount): Result<Unit> {
        return try {
            val credential = GoogleAccountCredential.usingOAuth2(
                context,
                listOf(DriveScopes.DRIVE_FILE)
            )
            credential.selectedAccount = account.account
            
            driveService = Drive.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            )
                .setApplicationName("GATE Tracker")
                .build()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to initialize Drive service: ${e.message}"))
        }
    }
    
    /**
     * Check if user is currently signed in
     */
    fun isSignedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account != null && driveService != null
    }
    
    /**
     * Get currently signed-in account
     */
    fun getSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
    
    /**
     * Sign out from Google account
     */
    suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            getSignInClient().signOut().await()
            driveService = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to sign out: ${e.message}"))
        }
    }
    
    /**
     * Get or create app folder in Google Drive
     */
    private suspend fun getOrCreateAppFolder(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                Exception("Drive service not initialized. Please sign in first.")
            )
            
            // Search for existing folder
            val query = "name='$APP_FOLDER_NAME' and mimeType='application/vnd.google-apps.folder' and trashed=false"
            val result = service.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute()
            
            if (result.files.isNotEmpty()) {
                return@withContext Result.success(result.files[0].id)
            }
            
            // Create folder if it doesn't exist
            val folderMetadata = com.google.api.services.drive.model.File()
            folderMetadata.name = APP_FOLDER_NAME
            folderMetadata.mimeType = "application/vnd.google-apps.folder"
            
            val folder = service.files().create(folderMetadata)
                .setFields("id")
                .execute()
            
            Result.success(folder.id)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to access app folder: ${e.message}"))
        }
    }
    
    /**
     * Upload backup file to Google Drive
     */
    suspend fun uploadBackup(fileName: String, content: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                Exception("Drive service not initialized. Please sign in first.")
            )
            
            val folderIdResult = getOrCreateAppFolder()
            if (folderIdResult.isFailure) {
                return@withContext Result.failure(folderIdResult.exceptionOrNull()!!)
            }
            val folderId = folderIdResult.getOrNull()!!
            
            val fileMetadata = com.google.api.services.drive.model.File()
            fileMetadata.name = fileName
            fileMetadata.parents = listOf(folderId)
            fileMetadata.mimeType = BACKUP_MIME_TYPE
            
            val contentBytes = content.toByteArray(Charsets.UTF_8)
            val mediaContent = ByteArrayContent(BACKUP_MIME_TYPE, contentBytes)
            
            val file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute()
            
            Result.success(file.id)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to upload backup: ${e.message}"))
        }
    }
    
    /**
     * List all backup files from Google Drive
     */
    suspend fun listBackups(): Result<List<DriveBackupFile>> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                Exception("Drive service not initialized. Please sign in first.")
            )
            
            val folderIdResult = getOrCreateAppFolder()
            if (folderIdResult.isFailure) {
                return@withContext Result.success(emptyList())
            }
            val folderId = folderIdResult.getOrNull()!!
            
            val query = "'$folderId' in parents and mimeType='$BACKUP_MIME_TYPE' and trashed=false"
            val result = service.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name, createdTime, size)")
                .setOrderBy("createdTime desc")
                .execute()
            
            val backupFiles = result.files.map { file ->
                DriveBackupFile(
                    id = file.id,
                    name = file.name,
                    createdTime = file.createdTime?.value ?: 0L,
                    size = file.getSize() ?: 0L
                )
            }
            
            Result.success(backupFiles)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to list backups: ${e.message}"))
        }
    }
    
    /**
     * Download backup file content from Google Drive
     */
    suspend fun downloadBackup(fileId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                Exception("Drive service not initialized. Please sign in first.")
            )
            
            val outputStream = ByteArrayOutputStream()
            service.files().get(fileId).executeMediaAndDownloadTo(outputStream)
            
            val content = outputStream.toString(Charsets.UTF_8.name())
            Result.success(content)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to download backup: ${e.message}"))
        }
    }
    
    /**
     * Delete backup file from Google Drive
     */
    suspend fun deleteBackup(fileId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                Exception("Drive service not initialized. Please sign in first.")
            )
            
            service.files().delete(fileId).execute()
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to delete backup: ${e.message}"))
        }
    }
}
