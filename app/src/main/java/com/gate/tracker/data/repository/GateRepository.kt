package com.gate.tracker.data.repository

import android.util.Log

import com.gate.tracker.data.local.GateDatabase
import com.gate.tracker.data.local.SyllabusData
import com.gate.tracker.data.local.entity.BranchEntity
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.data.local.entity.ChapterNoteEntity
import com.gate.tracker.data.local.entity.GoalEntity
import com.gate.tracker.data.local.entity.SubjectEntity
import com.gate.tracker.data.local.entity.UserPreferenceEntity
import com.gate.tracker.data.model.*
import com.gate.tracker.data.drive.DriveManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GateRepository(
    private val database: GateDatabase,
    private val driveManager: DriveManager
) {
    
    private val branchDao = database.branchDao()
    private val subjectDao = database.subjectDao()
    private val chapterDao = database.chapterDao()
    private val examDateDao = database.examDateDao()
    private val userPrefDao = database.userPreferenceDao()
    private val goalDao = database.goalDao()
    private val mockTestDao = database.mockTestDao()
    private val resourceDao = database.resourceDao()
    private val notificationPreferencesDao = database.notificationPreferencesDao()
    private val activityDao = database.activityDao()
    private val todoDao = database.todoDao()
    
    // Branch Operations
    fun getAllBranches(): Flow<List<BranchEntity>> = branchDao.getAllBranches()
    
    suspend fun getBranchById(branchId: Int): BranchEntity? = branchDao.getBranchById(branchId)
    
    // Subject Operations
    fun getSubjectsByBranch(branchId: Int): Flow<List<SubjectEntity>> =
        subjectDao.getSubjectsByBranch(branchId)
    
    suspend fun getSubjectsSync(branchId: Int): List<SubjectEntity> =
        subjectDao.getSubjectsByBranchSync(branchId)
    
    suspend fun getSubjectById(subjectId: Int): SubjectEntity? =
        subjectDao.getSubjectById(subjectId)
    
    // Chapter Operations
    fun getChaptersBySubject(subjectId: Int): Flow<List<ChapterEntity>> =
        chapterDao.getChaptersBySubject(subjectId)
        
    fun getAllChaptersForBranch(branchId: Int): Flow<List<ChapterEntity>> =
        chapterDao.getChaptersByBranch(branchId)

    suspend fun hasAnyProgress(branchId: Int): Boolean {
        return chapterDao.getTotalCompletedChaptersSync(branchId) > 0
    }
    
    suspend fun getChapterById(chapterId: Int): ChapterEntity? =
        chapterDao.getChapterByIdSync(chapterId)
    
    suspend fun toggleChapterStatus(chapterId: Int, currentStatus: Boolean) {
        val newStatus = !currentStatus
        val completedDate = if (newStatus) System.currentTimeMillis() else null
        chapterDao.updateChapterStatus(chapterId, newStatus, completedDate)
        
        // Update subject's completed count
        // Get the chapter to find its subject
        val chapters = chapterDao.getChaptersBySubject(0).toString() // This needs to be improved
        // For now, we'll recalculate in the subject detail screen
    }
    
    suspend fun updateSubjectCompletedCount(subjectId: Int) {
        val completedCount = chapterDao.getCompletedCount(subjectId)
        subjectDao.updateCompletedCount(subjectId, completedCount)
    }
    
    suspend fun toggleRevisionStatus(chapterId: Int, currentStatus: Boolean) {
        val newStatus = !currentStatus
        val revisedDate = if (newStatus) System.currentTimeMillis() else null
        chapterDao.markAsRevised(chapterId, newStatus, revisedDate)
    }
    
    suspend fun incrementRevisionCount(chapterId: Int) {
        // Mark as revised again with current timestamp, which increments the count
        chapterDao.markAsRevised(chapterId, true, System.currentTimeMillis())
    }
    
    suspend fun updateSubjectRevisedCount(subjectId: Int) {
        val revisedCount = chapterDao.getRevisedCount(subjectId)
        subjectDao.updateRevisedCount(subjectId, revisedCount)
    }
    
    fun getTotalCompletedChapters(branchId: Int): Flow<Int> = chapterDao.getTotalCompletedChapters(branchId)
    
    suspend fun getTotalChapterCount(branchId: Int): Int = chapterDao.getTotalChapterCount(branchId)
    
    // User Preference Operations
    fun getUserPreference(): Flow<UserPreferenceEntity?> = userPrefDao.getPreference()
    
    suspend fun selectBranch(branchId: Int) {
        Log.d("GATE_TRACKER", "selectBranch called with branchId: $branchId")
        // Simply update the user preference - do NOT delete/recreate data
        // The syllabus data is already populated during database creation
        userPrefDao.updateBranchSelection(branchId)
        Log.d("GATE_TRACKER", "Branch selection updated to: $branchId")
    }
    
    suspend fun clearSelectedBranch() {
        userPrefDao.resetPreferences()
    }

    suspend fun updateFirstLaunch(isFirstLaunch: Boolean) {
        userPrefDao.updateFirstLaunch(isFirstLaunch)
    }
    
    suspend fun updateLongestStreak(streak: Int) {
        userPrefDao.updateLongestStreak(streak)
    }
    
    // Exam Date Operations
    fun getExamDate(branchId: Int): Flow<Long?> = examDateDao.getExamDate(branchId).map { 
        it?.examDate 
    }
    
    suspend fun updateExamDate(branchId: Int, date: Long) {
        examDateDao.updateExamDate(branchId, date)
    }
    
    // Chapter Completion Date Operations
    suspend fun getLastCompletedChapter(branchId: Int): ChapterEntity? = 
        chapterDao.getLastCompletedChapter(branchId)
    
    suspend fun getLastStudiedSubject(branchId: Int): SubjectEntity? {
        val lastChapter = chapterDao.getLastCompletedChapter(branchId)
        return lastChapter?.let { 
            subjectDao.getSubjectById(it.subjectId)
        }
    }
    
    fun getAllCompletedChaptersWithDates(branchId: Int): Flow<List<ChapterEntity>> =
        chapterDao.getAllCompletedChaptersWithDates(branchId)
    
    // Category Operations
    suspend fun getCategoriesForSubject(subjectId: Int): List<String> =
        chapterDao.getCategoriesForSubject(subjectId)
    
    fun getChaptersByCategory(subjectId: Int, category: String): Flow<List<ChapterEntity>> =
        chapterDao.getChaptersByCategory(subjectId, category)
    
    
    suspend fun getLastCompletionDateForSubject(subjectId: Int): Long? =
        chapterDao.getLastCompletionDateForSubject(subjectId)
    
    suspend fun getLastRevisionDateForSubject(subjectId: Int): Long? =
        chapterDao.getLastRevisionDateForSubject(subjectId)
    
    // Calendar Operations
    fun getChaptersCompletedOnDate(branchId: Int, date: Long): Flow<List<ChapterEntity>> {
        val startOfDay = com.gate.tracker.util.DateUtils.getStartOfDay(date)
        val endOfDay = com.gate.tracker.util.DateUtils.getEndOfDay(date)
        return chapterDao.getChaptersCompletedOnDate(branchId, startOfDay, endOfDay)
    }
    
    fun getCompletedChaptersInRange(branchId: Int, startDate: Long, endDate: Long): Flow<List<ChapterEntity>> =
        chapterDao.getCompletedChaptersInRange(branchId, startDate, endDate)
    
    suspend fun getCompletionCountForDate(branchId: Int, date: Long): Int {
        val startOfDay = com.gate.tracker.util.DateUtils.getStartOfDay(date)
        val endOfDay = com.gate.tracker.util.DateUtils.getEndOfDay(date)
        return chapterDao.getCompletionCountForDate(branchId, startOfDay, endOfDay)
    }
    
    // Reset Operations
    suspend fun resetAllProgress(branchId: Int) {
        // Reset all chapter completions for the branch
        chapterDao.resetAllChapters(branchId)
        
        // Reset all subject completion counts for the branch
        subjectDao.resetAllSubjectCounts(branchId)
        
        // Reset streak data
        userPrefDao.resetStreakData()
    }
    
    // Chapter Note Operations
    suspend fun saveChapterNote(
        chapterId: Int,
        noteText: String,
        isImportant: Boolean,
        needsRevision: Boolean,
        noteId: Int? = null
    ) {
        val note = ChapterNoteEntity(
            id = noteId ?: 0,
            chapterId = chapterId,
            noteText = noteText,
            isImportant = isImportant,
            needsRevision = needsRevision,
            updatedAt = System.currentTimeMillis()
        )
        
        if (noteId != null && noteId > 0) {
            chapterDao.updateNote(note)
        } else {
            chapterDao.insertNote(note)
        }
    }
    
    fun getNoteForChapter(chapterId: Int): Flow<ChapterNoteEntity?> =
        chapterDao.getNoteForChapter(chapterId)
    
    fun getNotesForSubject(subjectId: Int): Flow<List<ChapterNoteEntity>> =
        chapterDao.getNotesForSubject(subjectId)
    
    suspend fun deleteChapterNote(noteId: Int) {
        chapterDao.deleteNote(noteId)
    }
    
    fun getAllRevisionNotes(): Flow<List<ChapterNoteEntity>> =
        chapterDao.getAllRevisionNotes()
    
    // Goal Operations
    fun getAllGoals(): Flow<List<GoalEntity>> = goalDao.getAllGoals()
    
    fun getActiveGoals(): Flow<List<GoalEntity>> = goalDao.getActiveGoals()
    
    fun getCurrentActiveGoal(): Flow<GoalEntity?> = goalDao.getCurrentActiveGoal()
    
    suspend fun getGoalById(goalId: Int): GoalEntity? = goalDao.getGoalById(goalId)
    
    suspend fun createGoal(goal: GoalEntity): Long = goalDao.insertGoal(goal)
    
    suspend fun updateGoal(goal: GoalEntity) = goalDao.updateGoal(goal)
    
    suspend fun updateGoalProgress(goalId: Int, progress: Int) {
        goalDao.updateProgress(goalId, progress)
        
        // Check if goal is completed
        val goal = goalDao.getGoalById(goalId)
        if (goal != null && progress >= goal.targetValue) {
            goalDao.markGoalCompleted(goalId)
        }
    }
    
    suspend fun markGoalCompleted(goalId: Int) = goalDao.markGoalCompleted(goalId)
    
    suspend fun deactivateGoal(goalId: Int) = goalDao.deactivateGoal(goalId)
    
    suspend fun deleteGoal(goalId: Int) = goalDao.deleteGoalById(goalId)
    
    // Todo Operations
    fun getTodosByBranch(branchId: Int, isRevisionMode: Boolean): Flow<List<com.gate.tracker.data.local.entity.TodoWithDetails>> =
        todoDao.getAllTodosByBranch(branchId, isRevisionMode)
    
    fun getPendingTodos(branchId: Int, isRevisionMode: Boolean): Flow<List<com.gate.tracker.data.local.entity.TodoWithDetails>> =
        todoDao.getPendingTodos(branchId, isRevisionMode)
    
    fun getPendingTodoCount(branchId: Int, isRevisionMode: Boolean): Flow<Int> =
        todoDao.getPendingCount(branchId, isRevisionMode)
    
    suspend fun addTodo(chapterId: Int, branchId: Int, isRevisionMode: Boolean): Long {
        val todo = com.gate.tracker.data.local.entity.TodoEntity(
            chapterId = chapterId,
            branchId = branchId,
            isRevisionMode = isRevisionMode
        )
        return todoDao.insertTodo(todo)
    }
    
    suspend fun toggleTodo(todoId: Int, isCompleted: Boolean) {
        todoDao.toggleTodo(todoId, isCompleted)
    }
    
    suspend fun deleteTodo(todoId: Int) {
        todoDao.deleteTodoById(todoId)
    }
    
    suspend fun isChapterInTodo(chapterId: Int, branchId: Int, isRevisionMode: Boolean): Boolean =
        todoDao.isChapterInTodo(chapterId, branchId, isRevisionMode)
    
    fun getTodoChapterIds(branchId: Int, isRevisionMode: Boolean): Flow<Set<Int>> =
        todoDao.getAllTodosByBranch(branchId, isRevisionMode).map { todos ->
            todos.map { it.todo.chapterId }.toSet()
        }
    
    // Mock Test Operations
    fun getMockTestsForBranch(branchId: Int): Flow<List<com.gate.tracker.data.local.entity.MockTestEntity>> =
        mockTestDao.getMockTestsForBranch(branchId)
    
    suspend fun getMockTestById(testId: Int): com.gate.tracker.data.local.entity.MockTestEntity? =
        mockTestDao.getMockTestById(testId)
    
    suspend fun addMockTest(test: com.gate.tracker.data.local.entity.MockTestEntity): Long =
        mockTestDao.insertMockTest(test)
    
    suspend fun updateMockTest(test: com.gate.tracker.data.local.entity.MockTestEntity) =
        mockTestDao.updateMockTest(test)
    
    suspend fun deleteMockTest(test: com.gate.tracker.data.local.entity.MockTestEntity) =
        mockTestDao.deleteMockTest(test)
    
    suspend fun deleteAllMockTestsForBranch(branchId: Int) =
        mockTestDao.deleteAllTestsForBranch(branchId)
    
    suspend fun getMockTestCount(branchId: Int): Int =
        mockTestDao.getTestCount(branchId)
    
    // Resource Operations
    fun getResourcesBySubject(subjectId: Int): Flow<List<com.gate.tracker.data.local.entity.ResourceEntity>> =
        resourceDao.getResourcesBySubject(subjectId)
    
    suspend fun getResourceById(id: Int): com.gate.tracker.data.local.entity.ResourceEntity? =
        resourceDao.getResourceById(id)
    
    suspend fun addResource(resource: com.gate.tracker.data.local.entity.ResourceEntity): Long =
        resourceDao.insertResource(resource)
    
    suspend fun updateResource(resource: com.gate.tracker.data.local.entity.ResourceEntity) =
        resourceDao.updateResource(resource)
    
    suspend fun deleteResource(resource: com.gate.tracker.data.local.entity.ResourceEntity) =
        resourceDao.deleteResource(resource)
    
    suspend fun deleteAllResourcesForSubject(subjectId: Int) =
        resourceDao.deleteAllResourcesForSubject(subjectId)
    
    fun getResourceCount(subjectId: Int): Flow<Int> =
        resourceDao.getResourceCount(subjectId)
    
    // ===== Notification Preferences Operations =====
    
    fun getNotificationPreferences(): Flow<com.gate.tracker.data.local.entity.NotificationPreferencesEntity?> =
        notificationPreferencesDao.getPreferences()
    
    suspend fun getNotificationPreferencesOnce(): com.gate.tracker.data.local.entity.NotificationPreferencesEntity? =
        notificationPreferencesDao.getPreferencesOnce()
    
    suspend fun initializeNotificationPreferences() {
        val existing = notificationPreferencesDao.getPreferencesOnce()
        if (existing == null) {
            notificationPreferencesDao.insertPreferences(com.gate.tracker.data.local.entity.NotificationPreferencesEntity())
        }
    }
    
    suspend fun updateNotificationPreferences(preferences: com.gate.tracker.data.local.entity.NotificationPreferencesEntity) =
        notificationPreferencesDao.updatePreferences(preferences)
    
    suspend fun updateDailyReminder(enabled: Boolean, time: String) =
        notificationPreferencesDao.updateDailyReminder(enabled, time)
    
    suspend fun updateRevisionAlerts(enabled: Boolean, time: String, days: String) =
        notificationPreferencesDao.updateRevisionAlerts(enabled, time, days)
    
    suspend fun updateMockTestReminders(enabled: Boolean, time: String, days: String) =
        notificationPreferencesDao.updateMockTestReminders(enabled, time, days)
    
    suspend fun updateExamCountdown(enabled: Boolean, time: String) =
        notificationPreferencesDao.updateExamCountdown(enabled, time)
    
    suspend fun updateInactivityAlerts(enabled: Boolean, thresholdDays: Int, time: String) =
        notificationPreferencesDao.updateInactivityAlerts(enabled, thresholdDays, time)
    
    suspend fun updateMotivational(enabled: Boolean, time: String) =
        notificationPreferencesDao.updateMotivational(enabled, time)
    
    suspend fun updateAchievementNotifications(enabled: Boolean) =
        notificationPreferencesDao.updateAchievementNotifications(enabled)
    
    
    suspend fun updateQuietHours(enabled: Boolean, start: String, end: String) =
        notificationPreferencesDao.updateQuietHours(enabled, start, end)
    
    // ===== Activity Tracking Operations =====
    
    suspend fun logActivity(activityType: com.gate.tracker.data.local.entity.ActivityType, studyDurationHours: Double = 0.0) {
        val activity = com.gate.tracker.data.local.entity.createActivityLog(activityType, studyDurationHours)
        activityDao.insertActivity(activity)
    }
    
    suspend fun getRecentActivities(limit: Int = 100): List<com.gate.tracker.data.local.entity.UserActivityLog> {
        val currentTime = System.currentTimeMillis()
        val thirtyDaysAgo = currentTime - (30 * 24 * 60 * 60 * 1000L)
        return activityDao.getActivitiesInRange(thirtyDaysAgo, currentTime)
    }
    
    suspend fun getOptimalStudyHour(): Int {
        val activities = getRecentActivities()
        val currentTime = System.currentTimeMillis()
        val thirtyDaysAgo = currentTime - (30 * 24 * 60 * 60 * 1000L)
        val hourCounts = activityDao.getActivityCountByHour(thirtyDaysAgo)
        
        return com.gate.tracker.util.ActivityAnalyzer.getRecommendedHour(activities, hourCounts)
    }
    
    suspend fun getActivityStats(): com.gate.tracker.util.ActivityStats {
        val activities = getRecentActivities()
        return com.gate.tracker.util.ActivityAnalyzer.getActivityStats(activities)
    }
    
    suspend fun cleanupOldActivities() {
        // Keep only last 60 days of activity data
        val sixtyDaysAgo = System.currentTimeMillis() - (60 * 24 * 60 * 60 * 1000L)
        activityDao.deleteActivitiesBefore(sixtyDaysAgo)
    }
    
    // ===== Revision Mode Methods =====
    
    suspend fun setRevisionMode(enabled: Boolean) {
        userPrefDao.setRevisionMode(enabled)
    }
    
    suspend fun setThemeMode(mode: Int) {
        userPrefDao.updateThemeMode(mode)
    }
    
    fun isRevisionMode(): Flow<Boolean> {
        return userPrefDao.getPreference().map { it?.isRevisionMode ?: false }
    }
    
    suspend fun toggleChapterRevision(chapterId: Int, isRevised: Boolean) {
        val date = if (isRevised) System.currentTimeMillis() else null
        chapterDao.markAsRevised(chapterId, isRevised, date)
    }
    
    fun getTotalRevisedChapters(branchId: Int): Flow<Int> {
        return chapterDao.getTotalRevisedChapters(branchId)
    }
    
    fun getChaptersByRevisionPriority(subjectId: Int): Flow<List<ChapterEntity>> {
        return chapterDao.getChaptersByRevisionPriority(subjectId)
    }
    
    suspend fun resetAllRevisions(branchId: Int) {
        chapterDao.resetAllRevisions(branchId)
    }
    
    // ===== Backup and Restore Operations =====
    
    /**
     * Export all data for backup
     */
    suspend fun exportBackupData(branchId: Int): com.gate.tracker.data.model.BackupData {
        val branch = branchDao.getBranchById(branchId)
            ?: throw IllegalArgumentException("Branch not found: $branchId")
        
        val subjects = subjectDao.getSubjectsByBranchSync(branchId)
        val subjectIds = subjects.map { it.id }
        
        // Get all chapters for these subjects
        val allChapters = mutableListOf<com.gate.tracker.data.local.entity.ChapterEntity>()
        val allNotes = mutableListOf<com.gate.tracker.data.local.entity.ChapterNoteEntity>()
        
        subjectIds.forEach { subjectId ->
            val chapters = chapterDao.getChaptersBySubjectSync(subjectId)
            allChapters.addAll(chapters)
            
            chapters.forEach { chapter ->
                val note = chapterDao.getNoteForChapterSync(chapter.id)
                if (note != null) {
                    allNotes.add(note)
                }
            }
        }
        
        // Get exam date
        val examDate = examDateDao.getExamDateSync(branchId)?.examDate
        
        // Get mock tests
        val mockTests = mockTestDao.getMockTestsForBranchSync(branchId)
        
        // Get user preferences
        val userPref = userPrefDao.getPreferenceSync()
            ?: com.gate.tracker.data.local.entity.UserPreferenceEntity()
        
        // Get notification preferences
        val notificationPref = notificationPreferencesDao.getPreferencesOnce()
        
        // Create metadata
        val metadata = com.gate.tracker.data.model.BackupMetadata(
            branchId = branchId,
            branchName = branch.name
        )
        
        // Export resources
        val allResources = database.chapterResourceDao().getAllResourcesSync()
        val subjectResources = database.resourceDao().getAllResourcesSync()
        
        return com.gate.tracker.data.model.BackupData(
            metadata = metadata,
            subjects = subjects.map { it.toBackup() },
            chapters = allChapters.map { it.toBackup() },
            notes = allNotes.map { it.toBackup() },
            mockTests = mockTests.map { it.toBackup() },
            resources = allResources.map { it.toBackup() },
            subjectResources = subjectResources.map { it.toBackup() },
            examDate = examDate,
            preferences = userPref.toBackup(),
            notificationPreferences = notificationPref?.toBackup()
        )
    }
    
    /**
     * Import backup data with smart merge - combines cloud and local data
     */
    suspend fun importBackupData(backupData: com.gate.tracker.data.model.BackupData): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                android.util.Log.d("GateRepository", "Starting smart merge import...")
                
                val branchId = backupData.metadata.branchId
                
                // Smart merge subjects
                android.util.Log.d("GateRepository", "Merging ${backupData.subjects.size} subjects...")
                backupData.subjects.forEach { subjectBackup ->
                    val subject = subjectBackup.toEntity(branchId)
                    val existing = subjectDao.getSubjectByIdSync(subject.id)
                    
                    if (existing == null) {
                        // New subject from cloud
                        subjectDao.insert(subject)
                    } else {
                        // Merge: keep highest completed/revised counts
                        val merged = existing.copy(
                            completedChapters = maxOf(existing.completedChapters, subject.completedChapters),
                            revisedChapters = maxOf(existing.revisedChapters, subject.revisedChapters)
                        )
                        subjectDao.update(merged)
                    }
                }
                
                // Smart merge chapters (OR operation for completion/revision)
                android.util.Log.d("GateRepository", "Merging ${backupData.chapters.size} chapters...")
                backupData.chapters.forEach { chapterBackup ->
                    val chapter = chapterBackup.toEntity()
                    val existing = chapterDao.getChapterByIdSync(chapter.id)
                    
                    if (existing == null) {
                        // New chapter from cloud
                        chapterDao.insert(chapter)
                    } else {
                        // Merge: if either is complete/revised, mark as complete/revised
                        // AND preserve the dates!
                        val isCompleted = existing.isCompleted || chapter.isCompleted
                        val completedDate = if (existing.isCompleted) existing.completedDate else chapter.completedDate
                        
                        val isRevised = existing.isRevised || chapter.isRevised
                        val revisedDate = if (existing.isRevised) existing.revisedDate else chapter.revisedDate
                        
                        val merged = existing.copy(
                            isCompleted = isCompleted,
                            completedDate = completedDate,
                            isRevised = isRevised,
                            revisedDate = revisedDate
                        )
                        chapterDao.update(merged)
                    }
                }
                
                // Smart merge notes (keep most recent by updatedAt)
                android.util.Log.d("GateRepository", "Merging ${backupData.notes.size} notes...")
                backupData.notes.forEach { noteBackup ->
                    val note = noteBackup.toEntity()
                    val existing = chapterDao.getNoteByChapterIdSync(note.chapterId)
                    
                    if (existing == null) {
                        // New note from cloud
                        chapterDao.insertNote(note)
                    } else {
                        // Keep the most recently updated note
                        if (note.updatedAt > existing.updatedAt) {
                            chapterDao.updateNote(note.copy(id = existing.id))
                        }
                    }
                }
                
                // Merge mock tests (combine all unique tests)
                android.util.Log.d("GateRepository", "Merging ${backupData.mockTests.size} mock tests...")
                backupData.mockTests.forEach { testBackup ->
                    val test = testBackup.toEntity(branchId)
                    val existing = mockTestDao.getTestByIdSync(test.id)
                    
                    if (existing == null) {
                        // New test from cloud
                        mockTestDao.insert(test)
                    } else {
                        // Keep higher score
                        if (test.score > existing.score) {
                            mockTestDao.update(test)
                        }
                    }
                }
                
                // Update exam date if from cloud is set (always update to cloud value)
                backupData.examDate?.let { examDateLong ->
                    examDateDao.updateExamDate(branchId, examDateLong)
                }
                
                // Merge notification preferences (keep local if exists, otherwise use cloud)
                backupData.notificationPreferences?.let { prefsBackup ->
                    val prefs = prefsBackup.toEntity()
                    val existing = notificationPreferencesDao.getPreferencesOnce()
                    if (existing == null) {
                        notificationPreferencesDao.insertPreferences(prefs)
                    }
                }
                
                // Merge resources (combine all unique resources)
                android.util.Log.d("GateRepository", "Merging ${backupData.resources.size} resources...")
                backupData.resources.forEach { resourceBackup ->
                    val resource = resourceBackup.toEntity()
                    val existing = database.chapterResourceDao().getResourceById(resource.id)
                    
                    if (existing == null) {
                        // New resource from cloud
                        database.chapterResourceDao().insert(resource)
                    }
                    // If exists, keep local version (don't overwrite)
                }
                
                // Merge subject resources (handle null for old backups)
                val subjectResourcesList = backupData.subjectResources ?: emptyList()
                android.util.Log.d("GateRepository", "Merging ${subjectResourcesList.size} subject resources...")
                subjectResourcesList.forEach { resourceBackup ->
                    val resource = resourceBackup.toEntity()
                    val existing = database.resourceDao().getResourceById(resource.id)
                    
                    if (existing == null) {
                        // New resource from cloud
                        android.util.Log.d("GateRepository", "Importing resource: ${resource.title}, DriveID: ${resource.driveFileId}")
                        database.resourceDao().insertResource(resource)
                    }
                    // If exists, keep local version
                }
                
                android.util.Log.d("GateRepository", "Smart merge completed successfully!")
                Result.success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("GateRepository", "Smart merge failed", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Clear all data for a specific branch (chapters, subjects progress, notes)
     */
    private suspend fun clearBranchData(branchId: Int) {
        // Get all subjects for this branch
        val subjects = subjectDao.getSubjectsByBranchSync(branchId)
        
        subjects.forEach { subject ->
            // Get all chapters for this subject
            val chapters = chapterDao.getChaptersBySubjectSync(subject.id)
            
            // Delete notes for each chapter
            chapters.forEach { chapter ->
                val note = chapterDao.getNoteForChapterSync(chapter.id)
                if (note != null) {
                    chapterDao.deleteNote(note.id)
                }
            }
            
            // Reset chapters
            chapterDao.deleteChaptersForSubject(subject.id)
        }
        
        // Delete subjects
        subjectDao.deleteSubjectsForBranch(branchId)
        
        // Delete mock tests
        mockTestDao.deleteAllTestsForBranch(branchId)
    }

    /**
     * Mark chapters as pre-existing (completed before using ap)
     * Sets isCompleted = true but completedDate = null
     */
    suspend fun markChaptersAsPreExisting(chapterIds: List<Int>) {
        if (chapterIds.isEmpty()) return
        
        // 1. Identify affected subjects BEFORE marking (to know which ones to update)
        // We need to find subject IDs for these chapters. 
        // Since we don't have getChaptersByIds, we can iterate or fetch all.
        // Efficient enough for typical onboarding size (10-50 chapters) to iterate.
        val affectedSubjectIds = mutableSetOf<Int>()
        chapterIds.forEach { id ->
            chapterDao.getChapterByIdSync(id)?.let { 
                affectedSubjectIds.add(it.subjectId) 
            }
        }
        
        // 2. Mark chapters as completed
        chapterDao.markChaptersAsPreExisting(chapterIds)
        
        // 3. Recalculate progress for affected subjects
        affectedSubjectIds.forEach { subjectId ->
            val count = chapterDao.getCompletedCount(subjectId)
            subjectDao.updateCompletedCount(subjectId, count)
        }
    }
    
    /**
     * Validate backup data before import
     */
    fun validateBackupData(backupData: com.gate.tracker.data.model.BackupData): Boolean {
        // Check if branch exists
        val branchExists = branchDao.getBranchByIdSync(backupData.metadata.branchId) != null
        if (!branchExists) return false
        
        // Basic validation
        if (backupData.subjects.isEmpty()) return false
        if (backupData.chapters.isEmpty()) return false
        
        return true
    }
    
    /**
     * Upload resource file to Google Drive
     */
    suspend fun uploadResourceFile(uri: android.net.Uri, mimeType: String): Result<String> {
        return driveManager.uploadFile(uri, mimeType)
    }
    
    /**
     * Ensure Drive service is initialized
     */
    suspend fun ensureDriveService(): Result<Unit> {
        return driveManager.ensureInitialized()
    }

    /**
     * Download resource file from Google Drive
     */
    suspend fun downloadResourceFile(fileId: String, targetFile: java.io.File): Result<Unit> {
        // Ensure service is initialized before download
        driveManager.ensureInitialized()
        return driveManager.downloadFile(fileId, targetFile)
    }
    /**
     * Get metadata for a Drive file
     */
    /**
     * Get metadata for a Drive file
     */
    suspend fun getDriveFileMetadata(fileId: String): Result<com.gate.tracker.data.drive.DriveManager.DriveFileMetadata> {
        return try {
            driveManager.ensureInitialized()
            driveManager.getFileMetadata(fileId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get signed-in Drive user name
     */
    fun getDriveUserName(): String? {
        return driveManager.getUserName()
    }
}

