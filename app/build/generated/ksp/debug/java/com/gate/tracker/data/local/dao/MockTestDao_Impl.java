package com.gate.tracker.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gate.tracker.data.local.entity.MockTestConverters;
import com.gate.tracker.data.local.entity.MockTestEntity;
import com.gate.tracker.data.local.entity.TestType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MockTestDao_Impl implements MockTestDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MockTestEntity> __insertionAdapterOfMockTestEntity;

  private final MockTestConverters __mockTestConverters = new MockTestConverters();

  private final EntityDeletionOrUpdateAdapter<MockTestEntity> __deletionAdapterOfMockTestEntity;

  private final EntityDeletionOrUpdateAdapter<MockTestEntity> __updateAdapterOfMockTestEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllTestsForBranch;

  public MockTestDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMockTestEntity = new EntityInsertionAdapter<MockTestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `mock_tests` (`id`,`branchId`,`testName`,`score`,`maxScore`,`testDate`,`testType`,`selectedSubjects`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MockTestEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBranchId());
        statement.bindString(3, entity.getTestName());
        statement.bindDouble(4, entity.getScore());
        statement.bindDouble(5, entity.getMaxScore());
        statement.bindLong(6, entity.getTestDate());
        final String _tmp = __mockTestConverters.fromTestType(entity.getTestType());
        statement.bindString(7, _tmp);
        final String _tmp_1 = __mockTestConverters.fromSubjectList(entity.getSelectedSubjects());
        statement.bindString(8, _tmp_1);
        statement.bindLong(9, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfMockTestEntity = new EntityDeletionOrUpdateAdapter<MockTestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `mock_tests` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MockTestEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMockTestEntity = new EntityDeletionOrUpdateAdapter<MockTestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `mock_tests` SET `id` = ?,`branchId` = ?,`testName` = ?,`score` = ?,`maxScore` = ?,`testDate` = ?,`testType` = ?,`selectedSubjects` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MockTestEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBranchId());
        statement.bindString(3, entity.getTestName());
        statement.bindDouble(4, entity.getScore());
        statement.bindDouble(5, entity.getMaxScore());
        statement.bindLong(6, entity.getTestDate());
        final String _tmp = __mockTestConverters.fromTestType(entity.getTestType());
        statement.bindString(7, _tmp);
        final String _tmp_1 = __mockTestConverters.fromSubjectList(entity.getSelectedSubjects());
        statement.bindString(8, _tmp_1);
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllTestsForBranch = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM mock_tests WHERE branchId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MockTestEntity test, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMockTestEntity.insert(test);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMockTest(final MockTestEntity test,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMockTestEntity.insertAndReturnId(test);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMockTest(final MockTestEntity test,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMockTestEntity.handle(test);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final MockTestEntity test, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMockTestEntity.handle(test);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMockTest(final MockTestEntity test,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMockTestEntity.handle(test);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllTestsForBranch(final int branchId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllTestsForBranch.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, branchId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllTestsForBranch.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MockTestEntity>> getMockTestsForBranch(final int branchId) {
    final String _sql = "SELECT * FROM mock_tests WHERE branchId = ? ORDER BY testDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"mock_tests"}, new Callable<List<MockTestEntity>>() {
      @Override
      @NonNull
      public List<MockTestEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfTestName = CursorUtil.getColumnIndexOrThrow(_cursor, "testName");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfMaxScore = CursorUtil.getColumnIndexOrThrow(_cursor, "maxScore");
          final int _cursorIndexOfTestDate = CursorUtil.getColumnIndexOrThrow(_cursor, "testDate");
          final int _cursorIndexOfTestType = CursorUtil.getColumnIndexOrThrow(_cursor, "testType");
          final int _cursorIndexOfSelectedSubjects = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedSubjects");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MockTestEntity> _result = new ArrayList<MockTestEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MockTestEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final String _tmpTestName;
            _tmpTestName = _cursor.getString(_cursorIndexOfTestName);
            final float _tmpScore;
            _tmpScore = _cursor.getFloat(_cursorIndexOfScore);
            final float _tmpMaxScore;
            _tmpMaxScore = _cursor.getFloat(_cursorIndexOfMaxScore);
            final long _tmpTestDate;
            _tmpTestDate = _cursor.getLong(_cursorIndexOfTestDate);
            final TestType _tmpTestType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTestType);
            _tmpTestType = __mockTestConverters.toTestType(_tmp);
            final List<Integer> _tmpSelectedSubjects;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSelectedSubjects);
            _tmpSelectedSubjects = __mockTestConverters.toSubjectList(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MockTestEntity(_tmpId,_tmpBranchId,_tmpTestName,_tmpScore,_tmpMaxScore,_tmpTestDate,_tmpTestType,_tmpSelectedSubjects,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllTestsSync(final Continuation<? super List<MockTestEntity>> $completion) {
    final String _sql = "SELECT * FROM mock_tests";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MockTestEntity>>() {
      @Override
      @NonNull
      public List<MockTestEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfTestName = CursorUtil.getColumnIndexOrThrow(_cursor, "testName");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfMaxScore = CursorUtil.getColumnIndexOrThrow(_cursor, "maxScore");
          final int _cursorIndexOfTestDate = CursorUtil.getColumnIndexOrThrow(_cursor, "testDate");
          final int _cursorIndexOfTestType = CursorUtil.getColumnIndexOrThrow(_cursor, "testType");
          final int _cursorIndexOfSelectedSubjects = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedSubjects");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MockTestEntity> _result = new ArrayList<MockTestEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MockTestEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final String _tmpTestName;
            _tmpTestName = _cursor.getString(_cursorIndexOfTestName);
            final float _tmpScore;
            _tmpScore = _cursor.getFloat(_cursorIndexOfScore);
            final float _tmpMaxScore;
            _tmpMaxScore = _cursor.getFloat(_cursorIndexOfMaxScore);
            final long _tmpTestDate;
            _tmpTestDate = _cursor.getLong(_cursorIndexOfTestDate);
            final TestType _tmpTestType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTestType);
            _tmpTestType = __mockTestConverters.toTestType(_tmp);
            final List<Integer> _tmpSelectedSubjects;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSelectedSubjects);
            _tmpSelectedSubjects = __mockTestConverters.toSubjectList(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MockTestEntity(_tmpId,_tmpBranchId,_tmpTestName,_tmpScore,_tmpMaxScore,_tmpTestDate,_tmpTestType,_tmpSelectedSubjects,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTestByIdSync(final int testId,
      final Continuation<? super MockTestEntity> $completion) {
    final String _sql = "SELECT * FROM mock_tests WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, testId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MockTestEntity>() {
      @Override
      @Nullable
      public MockTestEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfTestName = CursorUtil.getColumnIndexOrThrow(_cursor, "testName");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfMaxScore = CursorUtil.getColumnIndexOrThrow(_cursor, "maxScore");
          final int _cursorIndexOfTestDate = CursorUtil.getColumnIndexOrThrow(_cursor, "testDate");
          final int _cursorIndexOfTestType = CursorUtil.getColumnIndexOrThrow(_cursor, "testType");
          final int _cursorIndexOfSelectedSubjects = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedSubjects");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final MockTestEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final String _tmpTestName;
            _tmpTestName = _cursor.getString(_cursorIndexOfTestName);
            final float _tmpScore;
            _tmpScore = _cursor.getFloat(_cursorIndexOfScore);
            final float _tmpMaxScore;
            _tmpMaxScore = _cursor.getFloat(_cursorIndexOfMaxScore);
            final long _tmpTestDate;
            _tmpTestDate = _cursor.getLong(_cursorIndexOfTestDate);
            final TestType _tmpTestType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTestType);
            _tmpTestType = __mockTestConverters.toTestType(_tmp);
            final List<Integer> _tmpSelectedSubjects;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSelectedSubjects);
            _tmpSelectedSubjects = __mockTestConverters.toSubjectList(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new MockTestEntity(_tmpId,_tmpBranchId,_tmpTestName,_tmpScore,_tmpMaxScore,_tmpTestDate,_tmpTestType,_tmpSelectedSubjects,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMockTestById(final int testId,
      final Continuation<? super MockTestEntity> $completion) {
    final String _sql = "SELECT * FROM mock_tests WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, testId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MockTestEntity>() {
      @Override
      @Nullable
      public MockTestEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfTestName = CursorUtil.getColumnIndexOrThrow(_cursor, "testName");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfMaxScore = CursorUtil.getColumnIndexOrThrow(_cursor, "maxScore");
          final int _cursorIndexOfTestDate = CursorUtil.getColumnIndexOrThrow(_cursor, "testDate");
          final int _cursorIndexOfTestType = CursorUtil.getColumnIndexOrThrow(_cursor, "testType");
          final int _cursorIndexOfSelectedSubjects = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedSubjects");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final MockTestEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final String _tmpTestName;
            _tmpTestName = _cursor.getString(_cursorIndexOfTestName);
            final float _tmpScore;
            _tmpScore = _cursor.getFloat(_cursorIndexOfScore);
            final float _tmpMaxScore;
            _tmpMaxScore = _cursor.getFloat(_cursorIndexOfMaxScore);
            final long _tmpTestDate;
            _tmpTestDate = _cursor.getLong(_cursorIndexOfTestDate);
            final TestType _tmpTestType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTestType);
            _tmpTestType = __mockTestConverters.toTestType(_tmp);
            final List<Integer> _tmpSelectedSubjects;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSelectedSubjects);
            _tmpSelectedSubjects = __mockTestConverters.toSubjectList(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new MockTestEntity(_tmpId,_tmpBranchId,_tmpTestName,_tmpScore,_tmpMaxScore,_tmpTestDate,_tmpTestType,_tmpSelectedSubjects,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMockTestsForBranchSync(final int branchId,
      final Continuation<? super List<MockTestEntity>> $completion) {
    final String _sql = "SELECT * FROM mock_tests WHERE branchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MockTestEntity>>() {
      @Override
      @NonNull
      public List<MockTestEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfTestName = CursorUtil.getColumnIndexOrThrow(_cursor, "testName");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfMaxScore = CursorUtil.getColumnIndexOrThrow(_cursor, "maxScore");
          final int _cursorIndexOfTestDate = CursorUtil.getColumnIndexOrThrow(_cursor, "testDate");
          final int _cursorIndexOfTestType = CursorUtil.getColumnIndexOrThrow(_cursor, "testType");
          final int _cursorIndexOfSelectedSubjects = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedSubjects");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MockTestEntity> _result = new ArrayList<MockTestEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MockTestEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final String _tmpTestName;
            _tmpTestName = _cursor.getString(_cursorIndexOfTestName);
            final float _tmpScore;
            _tmpScore = _cursor.getFloat(_cursorIndexOfScore);
            final float _tmpMaxScore;
            _tmpMaxScore = _cursor.getFloat(_cursorIndexOfMaxScore);
            final long _tmpTestDate;
            _tmpTestDate = _cursor.getLong(_cursorIndexOfTestDate);
            final TestType _tmpTestType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTestType);
            _tmpTestType = __mockTestConverters.toTestType(_tmp);
            final List<Integer> _tmpSelectedSubjects;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfSelectedSubjects);
            _tmpSelectedSubjects = __mockTestConverters.toSubjectList(_tmp_1);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MockTestEntity(_tmpId,_tmpBranchId,_tmpTestName,_tmpScore,_tmpMaxScore,_tmpTestDate,_tmpTestType,_tmpSelectedSubjects,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTestCount(final int branchId, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM mock_tests WHERE branchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
