package com.gate.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gate.tracker.data.local.entity.BranchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BranchDao {
    @Query("SELECT * FROM branches")
    fun getAllBranches(): Flow<List<BranchEntity>>

    @Query("SELECT * FROM branches WHERE id = :branchId")
    suspend fun getBranchById(branchId: Int): BranchEntity?
    
    @Query("SELECT * FROM branches WHERE id = :branchId")
    fun getBranchByIdSync(branchId: Int): BranchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBranches(branches: List<BranchEntity>)
}
