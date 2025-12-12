package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chapters",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("subjectId")]
)
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subjectId: Int,
    val name: String,
    val orderIndex: Int,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null, // Timestamp when chapter was completed, null if not completed
    val category: String? = null, // Category/group for organizing chapters (e.g., "Discrete Mathematics", "Linear Algebra")
    
    // Revision mode tracking
    val isRevised: Boolean = false, // Separate from isCompleted - for revision mode
    val revisedDate: Long? = null, // Last time chapter was revised
    val revisionCount: Int = 0 // Number of times this chapter has been revised
)
