package com.gate.tracker.data.local

import android.util.Log

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gate.tracker.data.local.dao.*
import com.gate.tracker.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Database(
    entities = [
        BranchEntity::class,
        SubjectEntity::class,
        ChapterEntity::class,
        ExamDateEntity::class,
        UserPreferenceEntity::class,
        ChapterNoteEntity::class,
        GoalEntity::class,
        MockTestEntity::class,
        ResourceEntity::class,
        NotificationPreferencesEntity::class,
        UserActivityLog::class
    ],
    version = 20,
    exportSchema = false
)
abstract class GateDatabase : RoomDatabase() {
    
    abstract fun branchDao(): BranchDao
    abstract fun subjectDao(): SubjectDao
    abstract fun chapterDao(): ChapterDao
    abstract fun examDateDao(): ExamDateDao
    abstract fun userPreferenceDao(): UserPreferenceDao
    abstract fun goalDao(): GoalDao
    abstract fun mockTestDao(): MockTestDao
    abstract fun resourceDao(): ResourceDao
    abstract fun notificationPreferencesDao(): NotificationPreferencesDao
    abstract fun activityDao(): ActivityDao
    
    companion object {
        @Volatile
        private var INSTANCE: GateDatabase? = null
        
        fun getInstance(context: Context): GateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GateDatabase::class.java,
                    "gate_tracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Pre-populate database on first creation
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }
        
        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            // Re-populate database after destructive migration
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }
        
        private suspend fun populateDatabase(database: GateDatabase) {
            val branchDao = database.branchDao()
            val subjectDao = database.subjectDao()
            val chapterDao = database.chapterDao()
            val examDateDao = database.examDateDao()
            val userPrefDao = database.userPreferenceDao()
            
            // Insert all branches
            val branches = SyllabusData.getBranches()
            Log.d("GATE_TRACKER", "Populating database with ${branches.size} branches")
            branchDao.insertBranches(branches)
            Log.d("GATE_TRACKER", "Branches inserted successfully")
            
            // Insert subjects and chapters for each branch
            branches.forEach { branch ->
                Log.d("GATE_TRACKER", "Populating subjects for branch: ${branch.name}")
                val subjects = SyllabusData.getSubjectsForBranch(branch.id)
                subjectDao.insertSubjects(subjects)
                Log.d("GATE_TRACKER", "Inserted ${subjects.size} subjects for ${branch.name}")
                
                // Insert chapters for these subjects
                val chapters = SyllabusData.getChaptersForSubjects(subjects)
                chapterDao.insertChapters(chapters)
                Log.d("GATE_TRACKER", "Inserted ${chapters.size} chapters for ${branch.name}")
            }
            
            
            // Set branch-specific exam dates
            // CS (Computer Science & IT) - February 7, 2026
            val csExamDate = Calendar.getInstance().apply {
                set(2026, Calendar.FEBRUARY, 7, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            
            // CE (Civil Engineering) - February 14, 2026
            val ceExamDate = Calendar.getInstance().apply {
                set(2026, Calendar.FEBRUARY, 14, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            
            // DA (Data Science & AI) - February 15, 2026
            val daExamDate = Calendar.getInstance().apply {
                set(2026, Calendar.FEBRUARY, 15, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            
            // Insert exam dates for each branch with correct branch IDs
            examDateDao.insertExamDate(ExamDateEntity(branchId = 1, examDate = csExamDate))  // CS
            examDateDao.insertExamDate(ExamDateEntity(branchId = 5, examDate = ceExamDate))  // CE
            examDateDao.insertExamDate(ExamDateEntity(branchId = 6, examDate = daExamDate))  // DA
            
            Log.d("GATE_TRACKER", "Inserted exam dates - CS: $csExamDate, CE: $ceExamDate, DA: $daExamDate")
            
            // Set user preference (first launch = true)
            userPrefDao.insertPreference(
                UserPreferenceEntity(id = 1, selectedBranchId = null, isFirstLaunch = true)
            )
        }
    }
}
