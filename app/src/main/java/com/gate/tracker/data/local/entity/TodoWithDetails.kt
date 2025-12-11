package com.gate.tracker.data.local.entity

import androidx.room.Embedded

/**
 * Complete todo data with chapter and subject information
 */
data class TodoWithDetails(
    @Embedded
    val todo: TodoEntity,
    
    @Embedded(prefix = "chapter_")
    val chapter: ChapterEntity,
    
    @Embedded(prefix = "subject_")
    val subject: SubjectEntity
)
