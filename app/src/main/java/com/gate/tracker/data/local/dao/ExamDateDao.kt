package com.gate.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gate.tracker.data.local.entity.ExamDateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDateDao {
    @Query("SELECT * FROM exam_date WHERE branchId = :branchId")
    fun getExamDate(branchId: Int): Flow<ExamDateEntity?>
    
    @Query("SELECT * FROM exam_date WHERE branchId = :branchId")
    suspend fun getExamDateSync(branchId: Int): ExamDateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamDate(examDate: ExamDateEntity)

    @Query("UPDATE exam_date SET examDate = :date WHERE branchId = :branchId")
    suspend fun updateExamDate(branchId: Int, date: Long)
}
