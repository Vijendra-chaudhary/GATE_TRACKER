package com.gate.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.data.local.entity.ChapterNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId ORDER BY orderIndex ASC")
    fun getChaptersBySubject(subjectId: Int): Flow<List<ChapterEntity>>
    
    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId ORDER BY orderIndex ASC")
    suspend fun getChaptersBySubjectSync(subjectId: Int): List<ChapterEntity>

    @Query("UPDATE chapters SET isCompleted = :isCompleted, completedDate = :completedDate WHERE id = :chapterId")
    suspend fun updateChapterStatus(chapterId: Int, isCompleted: Boolean, completedDate: Long?)

    @Query("SELECT COUNT(*) FROM chapters WHERE subjectId = :subjectId AND isCompleted = 1")
    suspend fun getCompletedCount(subjectId: Int): Int

    @Query("SELECT COUNT(*) FROM chapters WHERE isCompleted = 1 AND subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId)")
    fun getTotalCompletedChapters(branchId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Query("DELETE FROM chapters WHERE subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId)")
    suspend fun deleteChaptersByBranch(branchId: Int)
    
    @Query("DELETE FROM chapters WHERE subjectId = :subjectId")
    suspend fun deleteChaptersForSubject(subjectId: Int)
    
    @Query("SELECT * FROM chapters WHERE isCompleted = 1 AND completedDate IS NOT NULL AND subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId) ORDER BY completedDate DESC LIMIT 1")
    suspend fun getLastCompletedChapter(branchId: Int): ChapterEntity?
    
    @Query("SELECT * FROM chapters WHERE isCompleted = 1 AND completedDate IS NOT NULL AND subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId) ORDER BY completedDate DESC")
    fun getAllCompletedChaptersWithDates(branchId: Int): Flow<List<ChapterEntity>>
    
    @Query("SELECT DISTINCT category FROM chapters WHERE subjectId = :subjectId AND category IS NOT NULL ORDER BY orderIndex ASC")
    suspend fun getCategoriesForSubject(subjectId: Int): List<String>
    
    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId AND category = :category ORDER BY orderIndex ASC")
    fun getChaptersByCategory(subjectId: Int, category: String): Flow<List<ChapterEntity>>

    @Query("SELECT MAX(completedDate) FROM chapters WHERE subjectId = :subjectId AND completedDate IS NOT NULL")
    suspend fun getLastCompletionDateForSubject(subjectId: Int): Long?
    
    @Query("SELECT MAX(revisedDate) FROM chapters WHERE subjectId = :subjectId AND revisedDate IS NOT NULL")
    suspend fun getLastRevisionDateForSubject(subjectId: Int): Long?
    
    // Calendar-specific queries
    @Query("SELECT * FROM chapters WHERE isCompleted = 1 AND completedDate >= :startOfDay AND completedDate <= :endOfDay AND subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId) ORDER BY completedDate DESC")
    fun getChaptersCompletedOnDate(branchId: Int, startOfDay: Long, endOfDay: Long): Flow<List<ChapterEntity>>
    
    @Query("SELECT * FROM chapters WHERE isCompleted = 1 AND completedDate >= :startDate AND completedDate <= :endDate AND subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId) ORDER BY completedDate DESC")
    fun getCompletedChaptersInRange(branchId: Int, startDate: Long, endDate: Long): Flow<List<ChapterEntity>>
    
    @Query("SELECT COUNT(*) FROM chapters WHERE isCompleted = 1 AND completedDate >= :startOfDay AND completedDate <= :endOfDay AND subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId)")
    suspend fun getCompletionCountForDate(branchId: Int, startOfDay: Long, endOfDay: Long): Int
    
    @Query("SELECT COUNT(*) FROM chapters WHERE subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId)")
    suspend fun getTotalChapterCount(branchId: Int): Int
    
    @Query("UPDATE chapters SET isCompleted = 0, completedDate = NULL WHERE subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId)")
    suspend fun resetAllChapters(branchId: Int)
    
    // Chapter Note operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: ChapterNoteEntity): Long
    
    @Update
    suspend fun updateNote(note: ChapterNoteEntity)
    
    @Query("DELETE FROM chapter_notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int)
    
    @Query("SELECT * FROM chapter_notes WHERE chapterId = :chapterId LIMIT 1")
    fun getNoteForChapter(chapterId: Int): Flow<ChapterNoteEntity?>
    
    @Query("SELECT * FROM chapter_notes WHERE chapterId = :chapterId LIMIT 1")
    suspend fun getNoteForChapterSync(chapterId: Int): ChapterNoteEntity?
    
    @Query("SELECT * FROM chapter_notes WHERE chapterId IN (SELECT id FROM chapters WHERE subjectId = :subjectId)")
    fun getNotesForSubject(subjectId: Int): Flow<List<ChapterNoteEntity>>
    
    
    @Query("SELECT * FROM chapter_notes WHERE needsRevision = 1")
    fun getAllRevisionNotes(): Flow<List<ChapterNoteEntity>>
    
    // ===== Revision Mode Methods =====
    
    @Query("UPDATE chapters SET isRevised = :isRevised, revisedDate = :date, revisionCount = revisionCount + 1 WHERE id = :chapterId")
    suspend fun markAsRevised(chapterId: Int, isRevised: Boolean, date: Long?)
    
    @Query("SELECT COUNT(*) FROM chapters WHERE subjectId = :subjectId AND isRevised = 1")
    suspend fun getRevisedCount(subjectId: Int): Int
    
    @Query("SELECT COUNT(*) FROM chapters WHERE isRevised = 1 AND subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId)")
    fun getTotalRevisedChapters(branchId: Int): Flow<Int>
    
    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId ORDER BY revisionCount ASC, revisedDate ASC, orderIndex ASC")
    fun getChaptersByRevisionPriority(subjectId: Int): Flow<List<ChapterEntity>>
    
    @Query("UPDATE chapters SET isRevised = 0, revisedDate = NULL WHERE subjectId IN (SELECT id FROM subjects WHERE branchId = :branchId)")
    suspend fun resetAllRevisions(branchId: Int)
}
