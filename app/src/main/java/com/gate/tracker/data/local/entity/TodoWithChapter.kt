package com.gate.tracker.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class representing a Todo item with its associated Chapter
 * Note: We only embed todo and chapter. Subject must be fetched separately or via a query.
 */
data class TodoWithChapter(
    @Embedded
    val todo: TodoEntity,
    
    @Relation(
        parentColumn = "chapterId",
        entityColumn = "id"
    )
    val chapter: ChapterEntity
)
