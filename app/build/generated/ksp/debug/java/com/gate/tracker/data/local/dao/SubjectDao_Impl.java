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
import com.gate.tracker.data.local.entity.SubjectEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class SubjectDao_Impl implements SubjectDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SubjectEntity> __insertionAdapterOfSubjectEntity;

  private final EntityDeletionOrUpdateAdapter<SubjectEntity> __updateAdapterOfSubjectEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCompletedCount;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSubjectsByBranch;

  private final SharedSQLiteStatement __preparedStmtOfResetAllSubjectCounts;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRevisedCount;

  public SubjectDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSubjectEntity = new EntityInsertionAdapter<SubjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `subjects` (`id`,`branchId`,`name`,`totalChapters`,`completedChapters`,`revisedChapters`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubjectEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBranchId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getTotalChapters());
        statement.bindLong(5, entity.getCompletedChapters());
        statement.bindLong(6, entity.getRevisedChapters());
      }
    };
    this.__updateAdapterOfSubjectEntity = new EntityDeletionOrUpdateAdapter<SubjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `subjects` SET `id` = ?,`branchId` = ?,`name` = ?,`totalChapters` = ?,`completedChapters` = ?,`revisedChapters` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubjectEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBranchId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getTotalChapters());
        statement.bindLong(5, entity.getCompletedChapters());
        statement.bindLong(6, entity.getRevisedChapters());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateCompletedCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE subjects SET completedChapters = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSubjectsByBranch = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM subjects WHERE branchId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetAllSubjectCounts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE subjects SET completedChapters = 0 WHERE branchId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRevisedCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE subjects SET revisedChapters = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSubjects(final List<SubjectEntity> subjects,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSubjectEntity.insert(subjects);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insert(final SubjectEntity subject, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSubjectEntity.insert(subject);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SubjectEntity subject, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSubjectEntity.handle(subject);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCompletedCount(final int subjectId, final int count,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCompletedCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, count);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, subjectId);
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
          __preparedStmtOfUpdateCompletedCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSubjectsByBranch(final int branchId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSubjectsByBranch.acquire();
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
          __preparedStmtOfDeleteSubjectsByBranch.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSubjectsForBranch(final int branchId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSubjectsByBranch.acquire();
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
          __preparedStmtOfDeleteSubjectsByBranch.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetAllSubjectCounts(final int branchId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetAllSubjectCounts.acquire();
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
          __preparedStmtOfResetAllSubjectCounts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRevisedCount(final int subjectId, final int count,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRevisedCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, count);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, subjectId);
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
          __preparedStmtOfUpdateRevisedCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SubjectEntity>> getSubjectsByBranch(final int branchId) {
    final String _sql = "SELECT * FROM subjects WHERE branchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subjects"}, new Callable<List<SubjectEntity>>() {
      @Override
      @NonNull
      public List<SubjectEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTotalChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalChapters");
          final int _cursorIndexOfCompletedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "completedChapters");
          final int _cursorIndexOfRevisedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedChapters");
          final List<SubjectEntity> _result = new ArrayList<SubjectEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubjectEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpTotalChapters;
            _tmpTotalChapters = _cursor.getInt(_cursorIndexOfTotalChapters);
            final int _tmpCompletedChapters;
            _tmpCompletedChapters = _cursor.getInt(_cursorIndexOfCompletedChapters);
            final int _tmpRevisedChapters;
            _tmpRevisedChapters = _cursor.getInt(_cursorIndexOfRevisedChapters);
            _item = new SubjectEntity(_tmpId,_tmpBranchId,_tmpName,_tmpTotalChapters,_tmpCompletedChapters,_tmpRevisedChapters);
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
  public Object getSubjectsByBranchSync(final int branchId,
      final Continuation<? super List<SubjectEntity>> $completion) {
    final String _sql = "SELECT * FROM subjects WHERE branchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SubjectEntity>>() {
      @Override
      @NonNull
      public List<SubjectEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTotalChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalChapters");
          final int _cursorIndexOfCompletedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "completedChapters");
          final int _cursorIndexOfRevisedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedChapters");
          final List<SubjectEntity> _result = new ArrayList<SubjectEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubjectEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpTotalChapters;
            _tmpTotalChapters = _cursor.getInt(_cursorIndexOfTotalChapters);
            final int _tmpCompletedChapters;
            _tmpCompletedChapters = _cursor.getInt(_cursorIndexOfCompletedChapters);
            final int _tmpRevisedChapters;
            _tmpRevisedChapters = _cursor.getInt(_cursorIndexOfRevisedChapters);
            _item = new SubjectEntity(_tmpId,_tmpBranchId,_tmpName,_tmpTotalChapters,_tmpCompletedChapters,_tmpRevisedChapters);
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
  public Object getSubjectById(final int subjectId,
      final Continuation<? super SubjectEntity> $completion) {
    final String _sql = "SELECT * FROM subjects WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SubjectEntity>() {
      @Override
      @Nullable
      public SubjectEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTotalChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalChapters");
          final int _cursorIndexOfCompletedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "completedChapters");
          final int _cursorIndexOfRevisedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedChapters");
          final SubjectEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpTotalChapters;
            _tmpTotalChapters = _cursor.getInt(_cursorIndexOfTotalChapters);
            final int _tmpCompletedChapters;
            _tmpCompletedChapters = _cursor.getInt(_cursorIndexOfCompletedChapters);
            final int _tmpRevisedChapters;
            _tmpRevisedChapters = _cursor.getInt(_cursorIndexOfRevisedChapters);
            _result = new SubjectEntity(_tmpId,_tmpBranchId,_tmpName,_tmpTotalChapters,_tmpCompletedChapters,_tmpRevisedChapters);
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
  public Object getSubjectByIdSync(final int subjectId,
      final Continuation<? super SubjectEntity> $completion) {
    final String _sql = "SELECT * FROM subjects WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SubjectEntity>() {
      @Override
      @Nullable
      public SubjectEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTotalChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalChapters");
          final int _cursorIndexOfCompletedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "completedChapters");
          final int _cursorIndexOfRevisedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedChapters");
          final SubjectEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpTotalChapters;
            _tmpTotalChapters = _cursor.getInt(_cursorIndexOfTotalChapters);
            final int _tmpCompletedChapters;
            _tmpCompletedChapters = _cursor.getInt(_cursorIndexOfCompletedChapters);
            final int _tmpRevisedChapters;
            _tmpRevisedChapters = _cursor.getInt(_cursorIndexOfRevisedChapters);
            _result = new SubjectEntity(_tmpId,_tmpBranchId,_tmpName,_tmpTotalChapters,_tmpCompletedChapters,_tmpRevisedChapters);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
