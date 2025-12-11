package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chapter_resources",
    foreignKeys = [
        ForeignKey(
            entity = ChapterEntity::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("chapterId")]
)
data class ChapterResourceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val chapterId: Int,
    val type: ResourceType,
    val title: String,
    val uri: String, // Local file path or URL
    val driveFileId: String? = null, // Google Drive file ID
    val fileSize: Long = 0,
    val mimeType: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
