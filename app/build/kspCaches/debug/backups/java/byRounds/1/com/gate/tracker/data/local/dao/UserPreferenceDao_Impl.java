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
import com.gate.tracker.data.local.entity.UserPreferenceEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class UserPreferenceDao_Impl implements UserPreferenceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserPreferenceEntity> __insertionAdapterOfUserPreferenceEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBranchSelection;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFirstLaunch;

  private final SharedSQLiteStatement __preparedStmtOfResetPreferences;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLongestStreak;

  private final SharedSQLiteStatement __preparedStmtOfResetStreakData;

  private final SharedSQLiteStatement __preparedStmtOfSetRevisionMode;

  private final SharedSQLiteStatement __preparedStmtOfUpdateThemeMode;

  public UserPreferenceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserPreferenceEntity = new EntityInsertionAdapter<UserPreferenceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_preference` (`id`,`selectedBranchId`,`isFirstLaunch`,`longestStreak`,`isRevisionMode`,`themeMode`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserPreferenceEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getSelectedBranchId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getSelectedBranchId());
        }
        final int _tmp = entity.isFirstLaunch() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getLongestStreak());
        final int _tmp_1 = entity.isRevisionMode() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getThemeMode());
      }
    };
    this.__preparedStmtOfUpdateBranchSelection = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_preference SET selectedBranchId = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateFirstLaunch = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_preference SET isFirstLaunch = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfResetPreferences = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_preference SET selectedBranchId = 0, isFirstLaunch = 1 WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLongestStreak = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_preference SET longestStreak = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfResetStreakData = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_preference SET longestStreak = 0 WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfSetRevisionMode = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_preference SET isRevisionMode = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateThemeMode = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_preference SET themeMode = ? WHERE id = 1";
        return _query;
      }
    };
  }

  @Override
  public Object insertPreference(final UserPreferenceEntity preference,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserPreferenceEntity.insert(preference);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBranchSelection(final int branchId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBranchSelection.acquire();
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
          __preparedStmtOfUpdateBranchSelection.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFirstLaunch(final boolean isFirstLaunch,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFirstLaunch.acquire();
        int _argIndex = 1;
        final int _tmp = isFirstLaunch ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
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
          __preparedStmtOfUpdateFirstLaunch.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetPreferences(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetPreferences.acquire();
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
          __preparedStmtOfResetPreferences.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLongestStreak(final int streak,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLongestStreak.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, streak);
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
          __preparedStmtOfUpdateLongestStreak.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetStreakData(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetStreakData.acquire();
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
          __preparedStmtOfResetStreakData.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setRevisionMode(final boolean enabled,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetRevisionMode.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
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
          __preparedStmtOfSetRevisionMode.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateThemeMode(final int mode, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateThemeMode.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, mode);
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
          __preparedStmtOfUpdateThemeMode.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserPreferenceEntity> getPreference() {
    final String _sql = "SELECT * FROM user_preference WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_preference"}, new Callable<UserPreferenceEntity>() {
      @Override
      @Nullable
      public UserPreferenceEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSelectedBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedBranchId");
          final int _cursorIndexOfIsFirstLaunch = CursorUtil.getColumnIndexOrThrow(_cursor, "isFirstLaunch");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final int _cursorIndexOfIsRevisionMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevisionMode");
          final int _cursorIndexOfThemeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "themeMode");
          final UserPreferenceEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpSelectedBranchId;
            if (_cursor.isNull(_cursorIndexOfSelectedBranchId)) {
              _tmpSelectedBranchId = null;
            } else {
              _tmpSelectedBranchId = _cursor.getInt(_cursorIndexOfSelectedBranchId);
            }
            final boolean _tmpIsFirstLaunch;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFirstLaunch);
            _tmpIsFirstLaunch = _tmp != 0;
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            final boolean _tmpIsRevisionMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevisionMode);
            _tmpIsRevisionMode = _tmp_1 != 0;
            final int _tmpThemeMode;
            _tmpThemeMode = _cursor.getInt(_cursorIndexOfThemeMode);
            _result = new UserPreferenceEntity(_tmpId,_tmpSelectedBranchId,_tmpIsFirstLaunch,_tmpLongestStreak,_tmpIsRevisionMode,_tmpThemeMode);
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
  public Object getPreferenceSync(final Continuation<? super UserPreferenceEntity> $completion) {
    final String _sql = "SELECT * FROM user_preference WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserPreferenceEntity>() {
      @Override
      @Nullable
      public UserPreferenceEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSelectedBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedBranchId");
          final int _cursorIndexOfIsFirstLaunch = CursorUtil.getColumnIndexOrThrow(_cursor, "isFirstLaunch");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longestStreak");
          final int _cursorIndexOfIsRevisionMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevisionMode");
          final int _cursorIndexOfThemeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "themeMode");
          final UserPreferenceEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpSelectedBranchId;
            if (_cursor.isNull(_cursorIndexOfSelectedBranchId)) {
              _tmpSelectedBranchId = null;
            } else {
              _tmpSelectedBranchId = _cursor.getInt(_cursorIndexOfSelectedBranchId);
            }
            final boolean _tmpIsFirstLaunch;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFirstLaunch);
            _tmpIsFirstLaunch = _tmp != 0;
            final int _tmpLongestStreak;
            _tmpLongestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
            final boolean _tmpIsRevisionMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevisionMode);
            _tmpIsRevisionMode = _tmp_1 != 0;
            final int _tmpThemeMode;
            _tmpThemeMode = _cursor.getInt(_cursorIndexOfThemeMode);
            _result = new UserPreferenceEntity(_tmpId,_tmpSelectedBranchId,_tmpIsFirstLaunch,_tmpLongestStreak,_tmpIsRevisionMode,_tmpThemeMode);
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
