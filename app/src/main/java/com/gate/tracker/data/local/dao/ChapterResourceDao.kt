package com.gate.tracker.data.local.dao

import androidx.room.*
import com.gate.tracker.data.local.entity.ChapterResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterResourceDao {
    
    @Query("SELECT * FROM chapter_resources WHERE chapterId = :chapterId ORDER BY createdAt DESC")
    fun getResourcesForChapter(chapterId: Int): Flow<List<ChapterResourceEntity>>
    
    @Query("SELECT * FROM chapter_resources WHERE chapterId = :chapterId ORDER BY createdAt DESC")
    suspend fun getResourcesForChapterSync(chapterId: Int): List<ChapterResourceEntity>
    
    @Query("SELECT * FROM chapter_resources WHERE id = :resourceId")
    suspend fun getResourceById(resourceId: Int): ChapterResourceEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resource: ChapterResourceEntity): Long
    
    @Update
    suspend fun update(resource: ChapterResourceEntity)
    
    @Delete
    suspend fun delete(resource: ChapterResourceEntity)
    
    @Query("DELETE FROM chapter_resources WHERE id = :resourceId")
    suspend fun deleteById(resourceId: Int)
    
    @Query("DELETE FROM chapter_resources WHERE chapterId = :chapterId")
    suspend fun deleteAllForChapter(chapterId: Int)
    
    @Query("SELECT * FROM chapter_resources")
    suspend fun getAllResourcesSync(): List<ChapterResourceEntity>
}
