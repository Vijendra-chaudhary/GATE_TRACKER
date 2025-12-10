package com.gate.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gate.tracker.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjects WHERE branchId = :branchId")
    fun getSubjectsByBranch(branchId: Int): Flow<List<SubjectEntity>>
    
    @Query("SELECT * FROM subjects WHERE branchId = :branchId")
    suspend fun getSubjectsByBranchSync(branchId: Int): List<SubjectEntity>

    @Query("SELECT * FROM subjects WHERE id = :subjectId")
    suspend fun getSubjectById(subjectId: Int): SubjectEntity?

    @Query("UPDATE subjects SET completedChapters = :count WHERE id = :subjectId")
    suspend fun updateCompletedCount(subjectId: Int, count: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Query("DELETE FROM subjects WHERE branchId = :branchId")
    suspend fun deleteSubjectsByBranch(branchId: Int)
    
    @Query("DELETE FROM subjects WHERE branchId = :branchId")
    suspend fun deleteSubjectsForBranch(branchId: Int)
    
    
    @Query("UPDATE subjects SET completedChapters = 0 WHERE branchId = :branchId")
    suspend fun resetAllSubjectCounts(branchId: Int)
    
    @Query("UPDATE subjects SET revisedChapters = :count WHERE id = :subjectId")
    suspend fun updateRevisedCount(subjectId: Int, count: Int)
}
