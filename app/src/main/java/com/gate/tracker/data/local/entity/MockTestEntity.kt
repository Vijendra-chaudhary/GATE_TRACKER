package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * Test type enum
 */
enum class TestType {
    FULL_SYLLABUS,
    SELECTED_SUBJECT
}

/**
 * Type converters for Room
 */
class MockTestConverters {
    @TypeConverter
    fun fromTestType(value: TestType): String {
        return value.name
    }
    
    @TypeConverter
    fun toTestType(value: String): TestType {
        return TestType.valueOf(value)
    }
    
    @TypeConverter
    fun fromSubjectList(value: List<Int>?): String {
        return value?.joinToString(",") ?: ""
    }
    
    @TypeConverter
    fun toSubjectList(value: String): List<Int> {
        return if (value.isEmpty()) emptyList() else value.split(",").map { it.toInt() }
    }
}

/**
 * Entity representing a mock test record
 * Tests are scoped to a specific branch (CS, CE, DA, etc.)
 */
@Entity(tableName = "mock_tests")
@TypeConverters(MockTestConverters::class)
data class MockTestEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    
    /** Branch ID this test belongs to (links to BranchEntity) */
    val branchId: Int,
    
    /** Name/title of the test (e.g., "GATE 2025 Mock Test 3") */
    val testName: String,
    
    /** Score obtained in the test */
    val score: Float,
    
    /** Maximum possible score for the test */
    val maxScore: Float,
    
    /** Date when the test was taken (timestamp in milliseconds) */
    val testDate: Long,
    
    /** Type of test - Full Syllabus or Selected Subject */
    val testType: TestType = TestType.FULL_SYLLABUS,
    
    /** List of subject IDs if testType is SELECTED_SUBJECT, empty otherwise */
    val selectedSubjects: List<Int> = emptyList(),
    
    /** Timestamp when this record was created (for audit trail) */
    val createdAt: Long = System.currentTimeMillis()
) {
    /** Calculate percentage score */
    val percentage: Float
        get() = if (maxScore > 0) (score / maxScore) * 100 else 0f
}
