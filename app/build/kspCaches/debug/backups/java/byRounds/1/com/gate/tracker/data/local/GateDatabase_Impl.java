package com.gate.tracker.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.gate.tracker.data.local.dao.ActivityDao;
import com.gate.tracker.data.local.dao.ActivityDao_Impl;
import com.gate.tracker.data.local.dao.BranchDao;
import com.gate.tracker.data.local.dao.BranchDao_Impl;
import com.gate.tracker.data.local.dao.ChapterDao;
import com.gate.tracker.data.local.dao.ChapterDao_Impl;
import com.gate.tracker.data.local.dao.ChapterResourceDao;
import com.gate.tracker.data.local.dao.ChapterResourceDao_Impl;
import com.gate.tracker.data.local.dao.ExamDateDao;
import com.gate.tracker.data.local.dao.ExamDateDao_Impl;
import com.gate.tracker.data.local.dao.GoalDao;
import com.gate.tracker.data.local.dao.GoalDao_Impl;
import com.gate.tracker.data.local.dao.MockTestDao;
import com.gate.tracker.data.local.dao.MockTestDao_Impl;
import com.gate.tracker.data.local.dao.NotificationPreferencesDao;
import com.gate.tracker.data.local.dao.NotificationPreferencesDao_Impl;
import com.gate.tracker.data.local.dao.ResourceDao;
import com.gate.tracker.data.local.dao.ResourceDao_Impl;
import com.gate.tracker.data.local.dao.SubjectDao;
import com.gate.tracker.data.local.dao.SubjectDao_Impl;
import com.gate.tracker.data.local.dao.TodoDao;
import com.gate.tracker.data.local.dao.TodoDao_Impl;
import com.gate.tracker.data.local.dao.UserPreferenceDao;
import com.gate.tracker.data.local.dao.UserPreferenceDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GateDatabase_Impl extends GateDatabase {
  private volatile BranchDao _branchDao;

  private volatile SubjectDao _subjectDao;

  private volatile ChapterDao _chapterDao;

  private volatile ExamDateDao _examDateDao;

  private volatile UserPreferenceDao _userPreferenceDao;

  private volatile GoalDao _goalDao;

  private volatile MockTestDao _mockTestDao;

  private volatile ResourceDao _resourceDao;

  private volatile ChapterResourceDao _chapterResourceDao;

  private volatile NotificationPreferencesDao _notificationPreferencesDao;

  private volatile ActivityDao _activityDao;

  private volatile TodoDao _todoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(25) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `branches` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `code` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `icon` TEXT NOT NULL, `colorHex` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `subjects` (`id` INTEGER NOT NULL, `branchId` INTEGER NOT NULL, `name` TEXT NOT NULL, `totalChapters` INTEGER NOT NULL, `completedChapters` INTEGER NOT NULL, `revisedChapters` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`branchId`) REFERENCES `branches`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_subjects_branchId` ON `subjects` (`branchId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chapters` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `subjectId` INTEGER NOT NULL, `name` TEXT NOT NULL, `orderIndex` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `completedDate` INTEGER, `category` TEXT, `isRevised` INTEGER NOT NULL, `revisedDate` INTEGER, `revisionCount` INTEGER NOT NULL, FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chapters_subjectId` ON `chapters` (`subjectId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exam_date` (`branchId` INTEGER NOT NULL, `examDate` INTEGER NOT NULL, PRIMARY KEY(`branchId`), FOREIGN KEY(`branchId`) REFERENCES `branches`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_preference` (`id` INTEGER NOT NULL, `selectedBranchId` INTEGER, `isFirstLaunch` INTEGER NOT NULL, `longestStreak` INTEGER NOT NULL, `isRevisionMode` INTEGER NOT NULL, `themeMode` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chapter_notes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `chapterId` INTEGER NOT NULL, `noteText` TEXT NOT NULL, `isImportant` INTEGER NOT NULL, `needsRevision` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`chapterId`) REFERENCES `chapters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chapter_notes_chapterId` ON `chapter_notes` (`chapterId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `goals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `goalType` TEXT NOT NULL, `targetValue` INTEGER NOT NULL, `currentProgress` INTEGER NOT NULL, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `mock_tests` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `branchId` INTEGER NOT NULL, `testName` TEXT NOT NULL, `score` REAL NOT NULL, `maxScore` REAL NOT NULL, `testDate` INTEGER NOT NULL, `testType` TEXT NOT NULL, `selectedSubjects` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `resources` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `subjectId` INTEGER NOT NULL, `resourceType` TEXT NOT NULL, `title` TEXT NOT NULL, `uri` TEXT NOT NULL, `description` TEXT NOT NULL, `fileSize` INTEGER, `createdAt` INTEGER NOT NULL, `driveFileId` TEXT, `thumbnailUrl` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chapter_resources` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `chapterId` INTEGER NOT NULL, `type` TEXT NOT NULL, `title` TEXT NOT NULL, `uri` TEXT NOT NULL, `driveFileId` TEXT, `fileSize` INTEGER NOT NULL, `mimeType` TEXT, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`chapterId`) REFERENCES `chapters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chapter_resources_chapterId` ON `chapter_resources` (`chapterId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notification_preferences` (`id` INTEGER NOT NULL, `dailyReminderEnabled` INTEGER NOT NULL, `dailyReminderTime` TEXT NOT NULL, `revisionAlertsEnabled` INTEGER NOT NULL, `revisionAlertsTime` TEXT NOT NULL, `revisionAlertsDays` TEXT NOT NULL, `revisionThresholdDays` INTEGER NOT NULL, `mockTestRemindersEnabled` INTEGER NOT NULL, `mockTestRemindersTime` TEXT NOT NULL, `mockTestRemindersDays` TEXT NOT NULL, `mockTestReminderFrequency` INTEGER NOT NULL, `examCountdownEnabled` INTEGER NOT NULL, `examCountdownTime` TEXT NOT NULL, `inactivityAlertsEnabled` INTEGER NOT NULL, `inactivityAlertsTime` TEXT NOT NULL, `inactivityThresholdDays` INTEGER NOT NULL, `motivationalEnabled` INTEGER NOT NULL, `motivationalTime` TEXT NOT NULL, `achievementNotificationsEnabled` INTEGER NOT NULL, `quietHoursEnabled` INTEGER NOT NULL, `quietHoursStart` TEXT NOT NULL, `quietHoursEnd` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_activity_log` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `activityType` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `hourOfDay` INTEGER NOT NULL, `dayOfWeek` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `todos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `chapterId` INTEGER NOT NULL, `branchId` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `isRevisionMode` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`chapterId`) REFERENCES `chapters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`branchId`) REFERENCES `branches`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_todos_chapterId` ON `todos` (`chapterId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_todos_branchId` ON `todos` (`branchId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '90419505499c84ccfbaa16471612f2c3')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `branches`");
        db.execSQL("DROP TABLE IF EXISTS `subjects`");
        db.execSQL("DROP TABLE IF EXISTS `chapters`");
        db.execSQL("DROP TABLE IF EXISTS `exam_date`");
        db.execSQL("DROP TABLE IF EXISTS `user_preference`");
        db.execSQL("DROP TABLE IF EXISTS `chapter_notes`");
        db.execSQL("DROP TABLE IF EXISTS `goals`");
        db.execSQL("DROP TABLE IF EXISTS `mock_tests`");
        db.execSQL("DROP TABLE IF EXISTS `resources`");
        db.execSQL("DROP TABLE IF EXISTS `chapter_resources`");
        db.execSQL("DROP TABLE IF EXISTS `notification_preferences`");
        db.execSQL("DROP TABLE IF EXISTS `user_activity_log`");
        db.execSQL("DROP TABLE IF EXISTS `todos`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBranches = new HashMap<String, TableInfo.Column>(6);
        _columnsBranches.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBranches.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBranches.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBranches.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBranches.put("icon", new TableInfo.Column("icon", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBranches.put("colorHex", new TableInfo.Column("colorHex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBranches = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBranches = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBranches = new TableInfo("branches", _columnsBranches, _foreignKeysBranches, _indicesBranches);
        final TableInfo _existingBranches = TableInfo.read(db, "branches");
        if (!_infoBranches.equals(_existingBranches)) {
          return new RoomOpenHelper.ValidationResult(false, "branches(com.gate.tracker.data.local.entity.BranchEntity).\n"
                  + " Expected:\n" + _infoBranches + "\n"
                  + " Found:\n" + _existingBranches);
        }
        final HashMap<String, TableInfo.Column> _columnsSubjects = new HashMap<String, TableInfo.Column>(6);
        _columnsSubjects.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("branchId", new TableInfo.Column("branchId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("totalChapters", new TableInfo.Column("totalChapters", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("completedChapters", new TableInfo.Column("completedChapters", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("revisedChapters", new TableInfo.Column("revisedChapters", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSubjects = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSubjects.add(new TableInfo.ForeignKey("branches", "CASCADE", "NO ACTION", Arrays.asList("branchId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSubjects = new HashSet<TableInfo.Index>(1);
        _indicesSubjects.add(new TableInfo.Index("index_subjects_branchId", false, Arrays.asList("branchId"), Arrays.asList("ASC")));
        final TableInfo _infoSubjects = new TableInfo("subjects", _columnsSubjects, _foreignKeysSubjects, _indicesSubjects);
        final TableInfo _existingSubjects = TableInfo.read(db, "subjects");
        if (!_infoSubjects.equals(_existingSubjects)) {
          return new RoomOpenHelper.ValidationResult(false, "subjects(com.gate.tracker.data.local.entity.SubjectEntity).\n"
                  + " Expected:\n" + _infoSubjects + "\n"
                  + " Found:\n" + _existingSubjects);
        }
        final HashMap<String, TableInfo.Column> _columnsChapters = new HashMap<String, TableInfo.Column>(10);
        _columnsChapters.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("subjectId", new TableInfo.Column("subjectId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("orderIndex", new TableInfo.Column("orderIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("completedDate", new TableInfo.Column("completedDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("isRevised", new TableInfo.Column("isRevised", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("revisedDate", new TableInfo.Column("revisedDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("revisionCount", new TableInfo.Column("revisionCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChapters = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysChapters.add(new TableInfo.ForeignKey("subjects", "CASCADE", "NO ACTION", Arrays.asList("subjectId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesChapters = new HashSet<TableInfo.Index>(1);
        _indicesChapters.add(new TableInfo.Index("index_chapters_subjectId", false, Arrays.asList("subjectId"), Arrays.asList("ASC")));
        final TableInfo _infoChapters = new TableInfo("chapters", _columnsChapters, _foreignKeysChapters, _indicesChapters);
        final TableInfo _existingChapters = TableInfo.read(db, "chapters");
        if (!_infoChapters.equals(_existingChapters)) {
          return new RoomOpenHelper.ValidationResult(false, "chapters(com.gate.tracker.data.local.entity.ChapterEntity).\n"
                  + " Expected:\n" + _infoChapters + "\n"
                  + " Found:\n" + _existingChapters);
        }
        final HashMap<String, TableInfo.Column> _columnsExamDate = new HashMap<String, TableInfo.Column>(2);
        _columnsExamDate.put("branchId", new TableInfo.Column("branchId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExamDate.put("examDate", new TableInfo.Column("examDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExamDate = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExamDate.add(new TableInfo.ForeignKey("branches", "CASCADE", "NO ACTION", Arrays.asList("branchId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExamDate = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExamDate = new TableInfo("exam_date", _columnsExamDate, _foreignKeysExamDate, _indicesExamDate);
        final TableInfo _existingExamDate = TableInfo.read(db, "exam_date");
        if (!_infoExamDate.equals(_existingExamDate)) {
          return new RoomOpenHelper.ValidationResult(false, "exam_date(com.gate.tracker.data.local.entity.ExamDateEntity).\n"
                  + " Expected:\n" + _infoExamDate + "\n"
                  + " Found:\n" + _existingExamDate);
        }
        final HashMap<String, TableInfo.Column> _columnsUserPreference = new HashMap<String, TableInfo.Column>(6);
        _columnsUserPreference.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserPreference.put("selectedBranchId", new TableInfo.Column("selectedBranchId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserPreference.put("isFirstLaunch", new TableInfo.Column("isFirstLaunch", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserPreference.put("longestStreak", new TableInfo.Column("longestStreak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserPreference.put("isRevisionMode", new TableInfo.Column("isRevisionMode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserPreference.put("themeMode", new TableInfo.Column("themeMode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserPreference = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserPreference = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserPreference = new TableInfo("user_preference", _columnsUserPreference, _foreignKeysUserPreference, _indicesUserPreference);
        final TableInfo _existingUserPreference = TableInfo.read(db, "user_preference");
        if (!_infoUserPreference.equals(_existingUserPreference)) {
          return new RoomOpenHelper.ValidationResult(false, "user_preference(com.gate.tracker.data.local.entity.UserPreferenceEntity).\n"
                  + " Expected:\n" + _infoUserPreference + "\n"
                  + " Found:\n" + _existingUserPreference);
        }
        final HashMap<String, TableInfo.Column> _columnsChapterNotes = new HashMap<String, TableInfo.Column>(7);
        _columnsChapterNotes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterNotes.put("chapterId", new TableInfo.Column("chapterId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterNotes.put("noteText", new TableInfo.Column("noteText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterNotes.put("isImportant", new TableInfo.Column("isImportant", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterNotes.put("needsRevision", new TableInfo.Column("needsRevision", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterNotes.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterNotes.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChapterNotes = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysChapterNotes.add(new TableInfo.ForeignKey("chapters", "CASCADE", "NO ACTION", Arrays.asList("chapterId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesChapterNotes = new HashSet<TableInfo.Index>(1);
        _indicesChapterNotes.add(new TableInfo.Index("index_chapter_notes_chapterId", false, Arrays.asList("chapterId"), Arrays.asList("ASC")));
        final TableInfo _infoChapterNotes = new TableInfo("chapter_notes", _columnsChapterNotes, _foreignKeysChapterNotes, _indicesChapterNotes);
        final TableInfo _existingChapterNotes = TableInfo.read(db, "chapter_notes");
        if (!_infoChapterNotes.equals(_existingChapterNotes)) {
          return new RoomOpenHelper.ValidationResult(false, "chapter_notes(com.gate.tracker.data.local.entity.ChapterNoteEntity).\n"
                  + " Expected:\n" + _infoChapterNotes + "\n"
                  + " Found:\n" + _existingChapterNotes);
        }
        final HashMap<String, TableInfo.Column> _columnsGoals = new HashMap<String, TableInfo.Column>(9);
        _columnsGoals.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("goalType", new TableInfo.Column("goalType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("targetValue", new TableInfo.Column("targetValue", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("currentProgress", new TableInfo.Column("currentProgress", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("endDate", new TableInfo.Column("endDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoals.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGoals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGoals = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGoals = new TableInfo("goals", _columnsGoals, _foreignKeysGoals, _indicesGoals);
        final TableInfo _existingGoals = TableInfo.read(db, "goals");
        if (!_infoGoals.equals(_existingGoals)) {
          return new RoomOpenHelper.ValidationResult(false, "goals(com.gate.tracker.data.local.entity.GoalEntity).\n"
                  + " Expected:\n" + _infoGoals + "\n"
                  + " Found:\n" + _existingGoals);
        }
        final HashMap<String, TableInfo.Column> _columnsMockTests = new HashMap<String, TableInfo.Column>(9);
        _columnsMockTests.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMockTests.put("branchId", new TableInfo.Column("branchId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMockTests.put("testName", new TableInfo.Column("testName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMockTests.put("score", new TableInfo.Column("score", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMockTests.put("maxScore", new TableInfo.Column("maxScore", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMockTests.put("testDate", new TableInfo.Column("testDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMockTests.put("testType", new TableInfo.Column("testType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMockTests.put("selectedSubjects", new TableInfo.Column("selectedSubjects", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMockTests.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMockTests = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMockTests = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMockTests = new TableInfo("mock_tests", _columnsMockTests, _foreignKeysMockTests, _indicesMockTests);
        final TableInfo _existingMockTests = TableInfo.read(db, "mock_tests");
        if (!_infoMockTests.equals(_existingMockTests)) {
          return new RoomOpenHelper.ValidationResult(false, "mock_tests(com.gate.tracker.data.local.entity.MockTestEntity).\n"
                  + " Expected:\n" + _infoMockTests + "\n"
                  + " Found:\n" + _existingMockTests);
        }
        final HashMap<String, TableInfo.Column> _columnsResources = new HashMap<String, TableInfo.Column>(10);
        _columnsResources.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("subjectId", new TableInfo.Column("subjectId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("resourceType", new TableInfo.Column("resourceType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("fileSize", new TableInfo.Column("fileSize", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("driveFileId", new TableInfo.Column("driveFileId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResources.put("thumbnailUrl", new TableInfo.Column("thumbnailUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysResources = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesResources = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoResources = new TableInfo("resources", _columnsResources, _foreignKeysResources, _indicesResources);
        final TableInfo _existingResources = TableInfo.read(db, "resources");
        if (!_infoResources.equals(_existingResources)) {
          return new RoomOpenHelper.ValidationResult(false, "resources(com.gate.tracker.data.local.entity.ResourceEntity).\n"
                  + " Expected:\n" + _infoResources + "\n"
                  + " Found:\n" + _existingResources);
        }
        final HashMap<String, TableInfo.Column> _columnsChapterResources = new HashMap<String, TableInfo.Column>(9);
        _columnsChapterResources.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterResources.put("chapterId", new TableInfo.Column("chapterId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterResources.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterResources.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterResources.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterResources.put("driveFileId", new TableInfo.Column("driveFileId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterResources.put("fileSize", new TableInfo.Column("fileSize", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterResources.put("mimeType", new TableInfo.Column("mimeType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapterResources.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChapterResources = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysChapterResources.add(new TableInfo.ForeignKey("chapters", "CASCADE", "NO ACTION", Arrays.asList("chapterId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesChapterResources = new HashSet<TableInfo.Index>(1);
        _indicesChapterResources.add(new TableInfo.Index("index_chapter_resources_chapterId", false, Arrays.asList("chapterId"), Arrays.asList("ASC")));
        final TableInfo _infoChapterResources = new TableInfo("chapter_resources", _columnsChapterResources, _foreignKeysChapterResources, _indicesChapterResources);
        final TableInfo _existingChapterResources = TableInfo.read(db, "chapter_resources");
        if (!_infoChapterResources.equals(_existingChapterResources)) {
          return new RoomOpenHelper.ValidationResult(false, "chapter_resources(com.gate.tracker.data.local.entity.ChapterResourceEntity).\n"
                  + " Expected:\n" + _infoChapterResources + "\n"
                  + " Found:\n" + _existingChapterResources);
        }
        final HashMap<String, TableInfo.Column> _columnsNotificationPreferences = new HashMap<String, TableInfo.Column>(22);
        _columnsNotificationPreferences.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("dailyReminderEnabled", new TableInfo.Column("dailyReminderEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("dailyReminderTime", new TableInfo.Column("dailyReminderTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("revisionAlertsEnabled", new TableInfo.Column("revisionAlertsEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("revisionAlertsTime", new TableInfo.Column("revisionAlertsTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("revisionAlertsDays", new TableInfo.Column("revisionAlertsDays", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("revisionThresholdDays", new TableInfo.Column("revisionThresholdDays", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("mockTestRemindersEnabled", new TableInfo.Column("mockTestRemindersEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("mockTestRemindersTime", new TableInfo.Column("mockTestRemindersTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("mockTestRemindersDays", new TableInfo.Column("mockTestRemindersDays", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("mockTestReminderFrequency", new TableInfo.Column("mockTestReminderFrequency", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("examCountdownEnabled", new TableInfo.Column("examCountdownEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("examCountdownTime", new TableInfo.Column("examCountdownTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("inactivityAlertsEnabled", new TableInfo.Column("inactivityAlertsEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("inactivityAlertsTime", new TableInfo.Column("inactivityAlertsTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("inactivityThresholdDays", new TableInfo.Column("inactivityThresholdDays", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("motivationalEnabled", new TableInfo.Column("motivationalEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("motivationalTime", new TableInfo.Column("motivationalTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("achievementNotificationsEnabled", new TableInfo.Column("achievementNotificationsEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("quietHoursEnabled", new TableInfo.Column("quietHoursEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("quietHoursStart", new TableInfo.Column("quietHoursStart", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationPreferences.put("quietHoursEnd", new TableInfo.Column("quietHoursEnd", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotificationPreferences = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotificationPreferences = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotificationPreferences = new TableInfo("notification_preferences", _columnsNotificationPreferences, _foreignKeysNotificationPreferences, _indicesNotificationPreferences);
        final TableInfo _existingNotificationPreferences = TableInfo.read(db, "notification_preferences");
        if (!_infoNotificationPreferences.equals(_existingNotificationPreferences)) {
          return new RoomOpenHelper.ValidationResult(false, "notification_preferences(com.gate.tracker.data.local.entity.NotificationPreferencesEntity).\n"
                  + " Expected:\n" + _infoNotificationPreferences + "\n"
                  + " Found:\n" + _existingNotificationPreferences);
        }
        final HashMap<String, TableInfo.Column> _columnsUserActivityLog = new HashMap<String, TableInfo.Column>(5);
        _columnsUserActivityLog.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserActivityLog.put("activityType", new TableInfo.Column("activityType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserActivityLog.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserActivityLog.put("hourOfDay", new TableInfo.Column("hourOfDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserActivityLog.put("dayOfWeek", new TableInfo.Column("dayOfWeek", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserActivityLog = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserActivityLog = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserActivityLog = new TableInfo("user_activity_log", _columnsUserActivityLog, _foreignKeysUserActivityLog, _indicesUserActivityLog);
        final TableInfo _existingUserActivityLog = TableInfo.read(db, "user_activity_log");
        if (!_infoUserActivityLog.equals(_existingUserActivityLog)) {
          return new RoomOpenHelper.ValidationResult(false, "user_activity_log(com.gate.tracker.data.local.entity.UserActivityLog).\n"
                  + " Expected:\n" + _infoUserActivityLog + "\n"
                  + " Found:\n" + _existingUserActivityLog);
        }
        final HashMap<String, TableInfo.Column> _columnsTodos = new HashMap<String, TableInfo.Column>(6);
        _columnsTodos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("chapterId", new TableInfo.Column("chapterId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("branchId", new TableInfo.Column("branchId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("isRevisionMode", new TableInfo.Column("isRevisionMode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTodos.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTodos = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysTodos.add(new TableInfo.ForeignKey("chapters", "CASCADE", "NO ACTION", Arrays.asList("chapterId"), Arrays.asList("id")));
        _foreignKeysTodos.add(new TableInfo.ForeignKey("branches", "CASCADE", "NO ACTION", Arrays.asList("branchId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTodos = new HashSet<TableInfo.Index>(2);
        _indicesTodos.add(new TableInfo.Index("index_todos_chapterId", false, Arrays.asList("chapterId"), Arrays.asList("ASC")));
        _indicesTodos.add(new TableInfo.Index("index_todos_branchId", false, Arrays.asList("branchId"), Arrays.asList("ASC")));
        final TableInfo _infoTodos = new TableInfo("todos", _columnsTodos, _foreignKeysTodos, _indicesTodos);
        final TableInfo _existingTodos = TableInfo.read(db, "todos");
        if (!_infoTodos.equals(_existingTodos)) {
          return new RoomOpenHelper.ValidationResult(false, "todos(com.gate.tracker.data.local.entity.TodoEntity).\n"
                  + " Expected:\n" + _infoTodos + "\n"
                  + " Found:\n" + _existingTodos);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "90419505499c84ccfbaa16471612f2c3", "3f687602e8fbc78af9a4b219ed56f578");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "branches","subjects","chapters","exam_date","user_preference","chapter_notes","goals","mock_tests","resources","chapter_resources","notification_preferences","user_activity_log","todos");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `branches`");
      _db.execSQL("DELETE FROM `subjects`");
      _db.execSQL("DELETE FROM `chapters`");
      _db.execSQL("DELETE FROM `exam_date`");
      _db.execSQL("DELETE FROM `user_preference`");
      _db.execSQL("DELETE FROM `chapter_notes`");
      _db.execSQL("DELETE FROM `goals`");
      _db.execSQL("DELETE FROM `mock_tests`");
      _db.execSQL("DELETE FROM `resources`");
      _db.execSQL("DELETE FROM `chapter_resources`");
      _db.execSQL("DELETE FROM `notification_preferences`");
      _db.execSQL("DELETE FROM `user_activity_log`");
      _db.execSQL("DELETE FROM `todos`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BranchDao.class, BranchDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SubjectDao.class, SubjectDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChapterDao.class, ChapterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExamDateDao.class, ExamDateDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserPreferenceDao.class, UserPreferenceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GoalDao.class, GoalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MockTestDao.class, MockTestDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ResourceDao.class, ResourceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChapterResourceDao.class, ChapterResourceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NotificationPreferencesDao.class, NotificationPreferencesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ActivityDao.class, ActivityDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TodoDao.class, TodoDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BranchDao branchDao() {
    if (_branchDao != null) {
      return _branchDao;
    } else {
      synchronized(this) {
        if(_branchDao == null) {
          _branchDao = new BranchDao_Impl(this);
        }
        return _branchDao;
      }
    }
  }

  @Override
  public SubjectDao subjectDao() {
    if (_subjectDao != null) {
      return _subjectDao;
    } else {
      synchronized(this) {
        if(_subjectDao == null) {
          _subjectDao = new SubjectDao_Impl(this);
        }
        return _subjectDao;
      }
    }
  }

  @Override
  public ChapterDao chapterDao() {
    if (_chapterDao != null) {
      return _chapterDao;
    } else {
      synchronized(this) {
        if(_chapterDao == null) {
          _chapterDao = new ChapterDao_Impl(this);
        }
        return _chapterDao;
      }
    }
  }

  @Override
  public ExamDateDao examDateDao() {
    if (_examDateDao != null) {
      return _examDateDao;
    } else {
      synchronized(this) {
        if(_examDateDao == null) {
          _examDateDao = new ExamDateDao_Impl(this);
        }
        return _examDateDao;
      }
    }
  }

  @Override
  public UserPreferenceDao userPreferenceDao() {
    if (_userPreferenceDao != null) {
      return _userPreferenceDao;
    } else {
      synchronized(this) {
        if(_userPreferenceDao == null) {
          _userPreferenceDao = new UserPreferenceDao_Impl(this);
        }
        return _userPreferenceDao;
      }
    }
  }

  @Override
  public GoalDao goalDao() {
    if (_goalDao != null) {
      return _goalDao;
    } else {
      synchronized(this) {
        if(_goalDao == null) {
          _goalDao = new GoalDao_Impl(this);
        }
        return _goalDao;
      }
    }
  }

  @Override
  public MockTestDao mockTestDao() {
    if (_mockTestDao != null) {
      return _mockTestDao;
    } else {
      synchronized(this) {
        if(_mockTestDao == null) {
          _mockTestDao = new MockTestDao_Impl(this);
        }
        return _mockTestDao;
      }
    }
  }

  @Override
  public ResourceDao resourceDao() {
    if (_resourceDao != null) {
      return _resourceDao;
    } else {
      synchronized(this) {
        if(_resourceDao == null) {
          _resourceDao = new ResourceDao_Impl(this);
        }
        return _resourceDao;
      }
    }
  }

  @Override
  public ChapterResourceDao chapterResourceDao() {
    if (_chapterResourceDao != null) {
      return _chapterResourceDao;
    } else {
      synchronized(this) {
        if(_chapterResourceDao == null) {
          _chapterResourceDao = new ChapterResourceDao_Impl(this);
        }
        return _chapterResourceDao;
      }
    }
  }

  @Override
  public NotificationPreferencesDao notificationPreferencesDao() {
    if (_notificationPreferencesDao != null) {
      return _notificationPreferencesDao;
    } else {
      synchronized(this) {
        if(_notificationPreferencesDao == null) {
          _notificationPreferencesDao = new NotificationPreferencesDao_Impl(this);
        }
        return _notificationPreferencesDao;
      }
    }
  }

  @Override
  public ActivityDao activityDao() {
    if (_activityDao != null) {
      return _activityDao;
    } else {
      synchronized(this) {
        if(_activityDao == null) {
          _activityDao = new ActivityDao_Impl(this);
        }
        return _activityDao;
      }
    }
  }

  @Override
  public TodoDao todoDao() {
    if (_todoDao != null) {
      return _todoDao;
    } else {
      synchronized(this) {
        if(_todoDao == null) {
          _todoDao = new TodoDao_Impl(this);
        }
        return _todoDao;
      }
    }
  }
}
