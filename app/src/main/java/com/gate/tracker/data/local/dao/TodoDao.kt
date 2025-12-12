package com.gate.tracker.data.local.dao

import androidx.room.*
import com.gate.tracker.data.local.entity.TodoEntity
import com.gate.tracker.data.local.entity.TodoWithChapter
import com.gate.tracker.data.local.entity.TodoWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    
    @Transaction
    @Query("""
        SELECT 
            todos.*,
            chapters.id as chapter_id,
            chapters.subjectId as chapter_subjectId,
            chapters.name as chapter_name,
            chapters.orderIndex as chapter_orderIndex,
            chapters.isCompleted as chapter_isCompleted,
            chapters.completedDate as chapter_completedDate,
            chapters.category as chapter_category,
            chapters.isRevised as chapter_isRevised,
            chapters.revisedDate as chapter_revisedDate,
            chapters.revisionCount as chapter_revisionCount,
            subjects.id as subject_id,
            subjects.branchId as subject_branchId,
            subjects.name as subject_name,
            subjects.totalChapters as subject_totalChapters,
            subjects.completedChapters as subject_completedChapters,
            subjects.revisedChapters as subject_revisedChapters
        FROM todos
        INNER JOIN chapters ON todos.chapterId = chapters.id
        INNER JOIN subjects ON chapters.subjectId = subjects.id
        WHERE todos.branchId = :branchId AND todos.isRevisionMode = :isRevisionMode
        ORDER BY todos.createdAt DESC
    """)
    fun getAllTodosByBranch(branchId: Int, isRevisionMode: Boolean): Flow<List<TodoWithDetails>>
    
    @Transaction
    @Query("""
        SELECT 
            todos.*,
            chapters.id as chapter_id,
            chapters.subjectId as chapter_subjectId,
            chapters.name as chapter_name,
            chapters.orderIndex as chapter_orderIndex,
            chapters.isCompleted as chapter_isCompleted,
            chapters.completedDate as chapter_completedDate,
            chapters.category as chapter_category,
            chapters.isRevised as chapter_isRevised,
            chapters.revisedDate as chapter_revisedDate,
            chapters.revisionCount as chapter_revisionCount,
            subjects.id as subject_id,
            subjects.branchId as subject_branchId,
            subjects.name as subject_name,
            subjects.totalChapters as subject_totalChapters,
            subjects.completedChapters as subject_completedChapters,
            subjects.revisedChapters as subject_revisedChapters
        FROM todos
        INNER JOIN chapters ON todos.chapterId = chapters.id
        INNER JOIN subjects ON chapters.subjectId = subjects.id
        WHERE todos.branchId = :branchId AND todos.isCompleted = 0 AND todos.isRevisionMode = :isRevisionMode
        ORDER BY todos.createdAt ASC
        LIMIT 3
    """)
    fun getPendingTodos(branchId: Int, isRevisionMode: Boolean): Flow<List<TodoWithDetails>>
    
    @Query("SELECT COUNT(*) FROM todos WHERE branchId = :branchId AND isCompleted = 0 AND isRevisionMode = :isRevisionMode")
    fun getPendingCount(branchId: Int, isRevisionMode: Boolean): Flow<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity): Long
    
    @Update
    suspend fun updateTodo(todo: TodoEntity)
    
    @Query("UPDATE todos SET isCompleted = :isCompleted WHERE id = :todoId")
    suspend fun toggleTodo(todoId: Int, isCompleted: Boolean)
    
    @Delete
    suspend fun deleteTodo(todo: TodoEntity)
    
    @Query("DELETE FROM todos WHERE id = :todoId")
    suspend fun deleteTodoById(todoId: Int)
    
    @Query("DELETE FROM todos WHERE branchId = :branchId")
    suspend fun deleteAllByBranch(branchId: Int)
    
    @Query("DELETE FROM todos WHERE chapterId = :chapterId")
    suspend fun deleteByChapterId(chapterId: Int)
    
    @Query("SELECT EXISTS(SELECT 1 FROM todos WHERE chapterId = :chapterId AND branchId = :branchId AND isRevisionMode = :isRevisionMode)")
    suspend fun isChapterInTodo(chapterId: Int, branchId: Int, isRevisionMode: Boolean): Boolean
}
