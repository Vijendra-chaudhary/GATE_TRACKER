package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "branches")
data class BranchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String,
    val name: String,
    val description: String,
    val icon: String,
    val colorHex: String
)
