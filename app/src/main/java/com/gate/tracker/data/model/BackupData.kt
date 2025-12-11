package com.gate.tracker.data.model

import com.gate.tracker.data.local.entity.*

/**
 * Metadata for backup file
 */
data class BackupMetadata(
    val version: Int = CURRENT_BACKUP_VERSION,
    val timestamp: Long = System.currentTimeMillis(),
    val branchId: Int,
    val branchName: String,
    val deviceName: String = android.os.Build.MODEL,
    val appVersion: String = "1.0"
) {
    companion object {
        const val CURRENT_BACKUP_VERSION = 1
    }
}

/**
 * Complete backup data structure
 */
data class BackupData(
    val metadata: BackupMetadata,
    val subjects: List<SubjectBackup>,
    val chapters: List<ChapterBackup>,
    val notes: List<ChapterNoteBackup>,
    val mockTests: List<MockTestBackup>,
    val resources: List<ChapterResourceBackup>,
    val subjectResources: List<SubjectResourceBackup>? = null,
    val examDate: Long?,
    val preferences: UserPreferenceBackup,
    val notificationPreferences: NotificationPreferencesBackup?
)

/**
 * Serializable backup versions of entities
 */
data class SubjectBackup(
    val id: Int,
    val name: String,
    val totalChapters: Int,
    val completedChapters: Int,
    val revisedChapters: Int
)

data class ChapterBackup(
    val id: Int,
    val subjectId: Int,
    val name: String,
    val orderIndex: Int,
    val category: String?,
    val isCompleted: Boolean,
    val completedDate: Long?,
    val isRevised: Boolean,
    val revisedDate: Long?,
    val revisionCount: Int
)

data class ChapterNoteBackup(
    val id: Int,
    val chapterId: Int,
    val noteText: String,
    val isImportant: Boolean,
    val needsRevision: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

data class UserPreferenceBackup(
    val selectedBranchId: Int?,
    val longestStreak: Int,
    val isRevisionMode: Boolean,
    val themeMode: Int
)

data class NotificationPreferencesBackup(
    val dailyReminderTime: String,
    val dailyReminderEnabled: Boolean,
    val motivationalTime: String,
    val motivationalEnabled: Boolean,
    val revisionAlertsTime: String,
    val revisionAlertsDays: String,
    val mockTestRemindersTime: String,
    val mockTestRemindersDays: String,
    val examCountdownTime: String,
    val inactivityAlertsEnabled: Boolean
)

data class MockTestBackup(
    val id: Int,
    val testName: String,
    val score: Float,
    val maxScore: Float,
    val testDate: Long,
    val testType: String,
    val selectedSubjects: List<Int>,
    val createdAt: Long
)

data class ChapterResourceBackup(
    val id: Int,
    val chapterId: Int,
    val type: String, // PDF, IMAGE, URL
    val title: String,
    val uri: String,
    val driveFileId: String?,
    val fileSize: Long,
    val mimeType: String?,
    val createdAt: Long
)

data class SubjectResourceBackup(
    val id: Int,
    val subjectId: Int,
    val type: String,
    val title: String,
    val uri: String,
    val description: String,
    val fileSize: Long?,
    val createdAt: Long,
    val driveFileId: String?
)

/**
 * Conversion extensions from entities to backup models
 */
fun SubjectEntity.toBackup() = SubjectBackup(
    id = id,
    name = name,
    totalChapters = totalChapters,
    completedChapters = completedChapters,
    revisedChapters = revisedChapters
)

fun ChapterEntity.toBackup() = ChapterBackup(
    id = id,
    subjectId = subjectId,
    name = name,
    orderIndex = orderIndex,
    category = category,
    isCompleted = isCompleted,
    completedDate = completedDate,
    isRevised = isRevised,
    revisedDate = revisedDate,
    revisionCount = revisionCount
)

fun ChapterNoteEntity.toBackup() = ChapterNoteBackup(
    id = id,
    chapterId = chapterId,
    noteText = noteText,
    isImportant = isImportant,
    needsRevision = needsRevision,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun UserPreferenceEntity.toBackup() = UserPreferenceBackup(
    selectedBranchId = selectedBranchId,
    longestStreak = longestStreak,
    isRevisionMode = isRevisionMode,
    themeMode = themeMode
)

fun NotificationPreferencesEntity.toBackup() = NotificationPreferencesBackup(
    dailyReminderTime = dailyReminderTime,
    dailyReminderEnabled = dailyReminderEnabled,
    motivationalTime = motivationalTime,
    motivationalEnabled = motivationalEnabled,
    revisionAlertsTime = revisionAlertsTime,
    revisionAlertsDays = revisionAlertsDays,
    mockTestRemindersTime = mockTestRemindersTime,
    mockTestRemindersDays = mockTestRemindersDays,
    examCountdownTime = examCountdownTime,
    inactivityAlertsEnabled = inactivityAlertsEnabled
)

fun MockTestEntity.toBackup() = MockTestBackup(
    id = id,
    testName = testName,
    score = score,
    maxScore = maxScore,
    testDate = testDate,
    testType = testType.name,
    selectedSubjects = selectedSubjects,
    createdAt = createdAt
)

/**
 * Conversion extensions from backup models to entities
 */
fun SubjectBackup.toEntity(branchId: Int) = SubjectEntity(
    id = id,
    branchId = branchId,
    name = name,
    totalChapters = totalChapters,
    completedChapters = completedChapters,
    revisedChapters = revisedChapters
)

fun ChapterBackup.toEntity() = ChapterEntity(
    id = id,
    subjectId = subjectId,
    name = name,
    orderIndex = orderIndex,
    category = category,
    isCompleted = isCompleted,
    completedDate = completedDate,
    isRevised = isRevised,
    revisedDate = revisedDate,
    revisionCount = revisionCount
)

fun ChapterNoteBackup.toEntity() = ChapterNoteEntity(
    id = id,
    chapterId = chapterId,
    noteText = noteText,
    isImportant = isImportant,
    needsRevision = needsRevision,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun UserPreferenceBackup.toEntity() = UserPreferenceEntity(
    id = 1,
    selectedBranchId = selectedBranchId,
    longestStreak = longestStreak,
    isRevisionMode = isRevisionMode,
    themeMode = themeMode
)

fun NotificationPreferencesBackup.toEntity() = NotificationPreferencesEntity(
    id = 1,
    dailyReminderTime = dailyReminderTime,
    dailyReminderEnabled = dailyReminderEnabled,
    motivationalTime = motivationalTime,
    motivationalEnabled = motivationalEnabled,
    revisionAlertsTime = revisionAlertsTime,
    revisionAlertsDays = revisionAlertsDays,
    mockTestRemindersTime = mockTestRemindersTime,
    mockTestRemindersDays = mockTestRemindersDays,
    examCountdownTime = examCountdownTime,
    inactivityAlertsEnabled = inactivityAlertsEnabled
)

fun ChapterResourceEntity.toBackup() = ChapterResourceBackup(
    id = id,
    chapterId = chapterId,
    type = type.name,
    title = title,
    uri = uri,
    driveFileId = driveFileId,
    fileSize = fileSize,
    mimeType = mimeType,
    createdAt = createdAt
)

fun ChapterResourceBackup.toEntity() = ChapterResourceEntity(
    id = id,
    chapterId = chapterId,
    type = ResourceType.valueOf(type),
    title = title,
    uri = uri,
    driveFileId = driveFileId,
    fileSize = fileSize,
    mimeType = mimeType,
    createdAt = createdAt
)

fun SubjectResourceBackup.toEntity() = ResourceEntity(
    id = id,
    subjectId = subjectId,
    resourceType = ResourceType.valueOf(type),
    title = title,
    uri = uri,
    description = description,
    fileSize = fileSize,
    createdAt = createdAt,
    driveFileId = driveFileId
)

fun ResourceEntity.toBackup() = SubjectResourceBackup(
    id = id,
    subjectId = subjectId,
    type = resourceType.name,
    title = title,
    uri = uri,
    description = description,
    fileSize = fileSize,
    createdAt = createdAt,
    driveFileId = driveFileId
)

fun MockTestBackup.toEntity(branchId: Int) = MockTestEntity(
    id = id,
    branchId = branchId,
    testName = testName,
    score = score,
    maxScore = maxScore,
    testDate = testDate,
    testType = TestType.valueOf(testType),
    selectedSubjects = selectedSubjects,
    createdAt = createdAt
)
