package com.gate.tracker.data.local.dao

import androidx.room.*
import com.gate.tracker.data.local.entity.MockTestEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for mock test operations
 */
@Dao
interface MockTestDao {
    
    /**
     * Get all mock tests for a specific branch, ordered by test date (newest first)
     * Returns a Flow for reactive updates
     */
    @Query("SELECT * FROM mock_tests WHERE branchId = :branchId ORDER BY testDate DESC")
    fun getMockTestsForBranch(branchId: Int): Flow<List<MockTestEntity>>
    
    // Sync methods for backup
    @Query("SELECT * FROM mock_tests")
    suspend fun getAllTestsSync(): List<MockTestEntity>
    
    @Query("SELECT * FROM mock_tests WHERE id = :testId LIMIT 1")
    suspend fun getTestByIdSync(testId: Int): MockTestEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(test: MockTestEntity)
    
    @Update
    suspend fun update(test: MockTestEntity)
    
    /**
     * Get a specific mock test by ID
     */
    @Query("SELECT * FROM mock_tests WHERE id = :testId")
    suspend fun getMockTestById(testId: Int): MockTestEntity?
    
    /**
     * Insert a new mock test
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMockTest(test: MockTestEntity): Long
    
    /**
     * Update an existing mock test
     */
    @Update
    suspend fun updateMockTest(test: MockTestEntity)
    
    /**
     * Delete a specific mock test
     */
    @Delete
    suspend fun deleteMockTest(test: MockTestEntity)
    
    /**
     * Delete all mock tests for a specific branch
     */
    @Query("DELETE FROM mock_tests WHERE branchId = :branchId")
    suspend fun deleteAllTestsForBranch(branchId: Int)
    
    /**
     * Get all mock tests for a branch synchronously (for backup)
     */
    @Query("SELECT * FROM mock_tests WHERE branchId = :branchId")
    suspend fun getMockTestsForBranchSync(branchId: Int): List<MockTestEntity>
    
    /**
     * Get count of tests for a branch
     */
    @Query("SELECT COUNT(*) FROM mock_tests WHERE branchId = :branchId")
    suspend fun getTestCount(branchId: Int): Int
}
