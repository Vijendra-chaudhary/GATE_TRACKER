package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "exam_date",
    foreignKeys = [ForeignKey(
        entity = BranchEntity::class,
        parentColumns = ["id"],
        childColumns = ["branchId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExamDateEntity(
    @PrimaryKey
    val branchId: Int,
    val examDate: Long // Timestamp in milliseconds
)
