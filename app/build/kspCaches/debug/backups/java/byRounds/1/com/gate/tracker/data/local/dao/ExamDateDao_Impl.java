package com.gate.tracker.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gate.tracker.data.local.entity.ExamDateEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ExamDateDao_Impl implements ExamDateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExamDateEntity> __insertionAdapterOfExamDateEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateExamDate;

  public ExamDateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExamDateEntity = new EntityInsertionAdapter<ExamDateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exam_date` (`branchId`,`examDate`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamDateEntity entity) {
        statement.bindLong(1, entity.getBranchId());
        statement.bindLong(2, entity.getExamDate());
      }
    };
    this.__preparedStmtOfUpdateExamDate = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE exam_date SET examDate = ? WHERE branchId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertExamDate(final ExamDateEntity examDate,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExamDateEntity.insert(examDate);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateExamDate(final int branchId, final long date,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateExamDate.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, date);
        _argIndex = 2;
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
          __preparedStmtOfUpdateExamDate.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ExamDateEntity> getExamDate(final int branchId) {
    final String _sql = "SELECT * FROM exam_date WHERE branchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exam_date"}, new Callable<ExamDateEntity>() {
      @Override
      @Nullable
      public ExamDateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfExamDate = CursorUtil.getColumnIndexOrThrow(_cursor, "examDate");
          final ExamDateEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final long _tmpExamDate;
            _tmpExamDate = _cursor.getLong(_cursorIndexOfExamDate);
            _result = new ExamDateEntity(_tmpBranchId,_tmpExamDate);
          } else {
            _result = null;
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
  public Object getExamDateSync(final int branchId,
      final Continuation<? super ExamDateEntity> $completion) {
    final String _sql = "SELECT * FROM exam_date WHERE branchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExamDateEntity>() {
      @Override
      @Nullable
      public ExamDateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
          final int _cursorIndexOfExamDate = CursorUtil.getColumnIndexOrThrow(_cursor, "examDate");
          final ExamDateEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpBranchId;
            _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
            final long _tmpExamDate;
            _tmpExamDate = _cursor.getLong(_cursorIndexOfExamDate);
            _result = new ExamDateEntity(_tmpBranchId,_tmpExamDate);
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
