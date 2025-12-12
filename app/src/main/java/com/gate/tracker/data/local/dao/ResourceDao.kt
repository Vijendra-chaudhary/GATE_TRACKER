package com.gate.tracker.data.local.dao

import androidx.room.*
import com.gate.tracker.data.local.entity.ResourceEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for resource operations
 */
@Dao
interface ResourceDao {
    
    /**
     * Get all resources for a specific subject
     */
    @Query("SELECT * FROM resources WHERE subjectId = :subjectId ORDER BY createdAt DESC")
    fun getResourcesBySubject(subjectId: Int): Flow<List<ResourceEntity>>
    
    /**
     * Get a specific resource by ID
     */
    @Query("SELECT * FROM resources WHERE id = :id")
    suspend fun getResourceById(id: Int): ResourceEntity?
    
    /**
     * Insert a new resource
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResource(resource: ResourceEntity): Long
    
    /**
     * Update an existing resource
     */
    @Update
    suspend fun updateResource(resource: ResourceEntity)
    
    /**
     * Delete a resource
     */
    @Delete
    suspend fun deleteResource(resource: ResourceEntity)
    
    /**
     * Delete all resources for a subject
     */
    @Query("DELETE FROM resources WHERE subjectId = :subjectId")
    suspend fun deleteAllResourcesForSubject(subjectId: Int)
    
    /**
     * Get count of resources for a subject
     */
    @Query("SELECT COUNT(*) FROM resources WHERE subjectId = :subjectId")
    fun getResourceCount(subjectId: Int): Flow<Int>
    
    /**
     * Get all resources for backup
     */
    @Query("SELECT * FROM resources")
    suspend fun getAllResourcesSync(): List<ResourceEntity>
}
