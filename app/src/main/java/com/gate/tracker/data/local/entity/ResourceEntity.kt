package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Resource type enum
 */
enum class ResourceType {
    PDF,
    LINK,
    IMAGE
}

/**
 * Entity representing a study resource (PDF, link, or image)
 * Resources are scoped to a specific subject
 */
@Entity(tableName = "resources")
data class ResourceEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    
    /** Subject ID this resource belongs to */
    val subjectId: Int,
    
    /** Type of resource */
    val resourceType: ResourceType,
    
    /** Title/name of the resource */
    val title: String,
    
    /** URI string - file URI for PDFs/images, web URL for links */
    val uri: String,
    
    /** Optional description */
    val description: String = "",
    
    /** File size in bytes (for PDFs and images) */
    val fileSize: Long? = null,
    
    /** Timestamp when resource was added */
    val createdAt: Long = System.currentTimeMillis()
)
