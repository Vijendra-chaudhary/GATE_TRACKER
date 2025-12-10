package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subjects",
    foreignKeys = [
        ForeignKey(
            entity = BranchEntity::class,
            parentColumns = ["id"],
            childColumns = ["branchId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("branchId")]
)
data class SubjectEntity(
    @PrimaryKey
    val id: Int,
    val branchId: Int,
    val name: String,
    val totalChapters: Int,
    val completedChapters: Int = 0,
    val revisedChapters: Int = 0 // For revision mode tracking
)
