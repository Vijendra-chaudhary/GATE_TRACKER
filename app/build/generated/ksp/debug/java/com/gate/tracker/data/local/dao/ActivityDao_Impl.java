package com.gate.tracker.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gate.tracker.data.local.entity.ActivityType;
import com.gate.tracker.data.local.entity.UserActivityLog;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
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
public final class ActivityDao_Impl implements ActivityDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserActivityLog> __insertionAdapterOfUserActivityLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteActivitiesBefore;

  public ActivityDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserActivityLog = new EntityInsertionAdapter<UserActivityLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `user_activity_log` (`id`,`activityType`,`timestamp`,`hourOfDay`,`dayOfWeek`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserActivityLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, __ActivityType_enumToString(entity.getActivityType()));
        statement.bindLong(3, entity.getTimestamp());
        statement.bindLong(4, entity.getHourOfDay());
        statement.bindLong(5, entity.getDayOfWeek());
      }
    };
    this.__preparedStmtOfDeleteActivitiesBefore = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_activity_log WHERE timestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertActivity(final UserActivityLog activity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserActivityLog.insert(activity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteActivitiesBefore(final long before,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteActivitiesBefore.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, before);
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
          __preparedStmtOfDeleteActivitiesBefore.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getActivitiesInRange(final long startTime, final long endTime,
      final Continuation<? super List<UserActivityLog>> $completion) {
    final String _sql = "SELECT * FROM user_activity_log WHERE timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<UserActivityLog>>() {
      @Override
      @NonNull
      public List<UserActivityLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfActivityType = CursorUtil.getColumnIndexOrThrow(_cursor, "activityType");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfHourOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "hourOfDay");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final List<UserActivityLog> _result = new ArrayList<UserActivityLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserActivityLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final ActivityType _tmpActivityType;
            _tmpActivityType = __ActivityType_stringToEnum(_cursor.getString(_cursorIndexOfActivityType));
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmpHourOfDay;
            _tmpHourOfDay = _cursor.getInt(_cursorIndexOfHourOfDay);
            final int _tmpDayOfWeek;
            _tmpDayOfWeek = _cursor.getInt(_cursorIndexOfDayOfWeek);
            _item = new UserActivityLog(_tmpId,_tmpActivityType,_tmpTimestamp,_tmpHourOfDay,_tmpDayOfWeek);
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
  public Object getActivityCountByHour(final long startTime,
      final Continuation<? super List<HourActivityCount>> $completion) {
    final String _sql = "SELECT hourOfDay, COUNT(*) as count FROM user_activity_log WHERE timestamp >= ? GROUP BY hourOfDay ORDER BY hourOfDay ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HourActivityCount>>() {
      @Override
      @NonNull
      public List<HourActivityCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHourOfDay = 0;
          final int _cursorIndexOfCount = 1;
          final List<HourActivityCount> _result = new ArrayList<HourActivityCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HourActivityCount _item;
            final int _tmpHourOfDay;
            _tmpHourOfDay = _cursor.getInt(_cursorIndexOfHourOfDay);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new HourActivityCount(_tmpHourOfDay,_tmpCount);
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
  public Object getMostActiveHours(final long startTime,
      final Continuation<? super List<HourActivityCount>> $completion) {
    final String _sql = "SELECT hourOfDay, COUNT(*) as count FROM user_activity_log WHERE timestamp >= ? GROUP BY hourOfDay ORDER BY count DESC LIMIT 3";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HourActivityCount>>() {
      @Override
      @NonNull
      public List<HourActivityCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHourOfDay = 0;
          final int _cursorIndexOfCount = 1;
          final List<HourActivityCount> _result = new ArrayList<HourActivityCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HourActivityCount _item;
            final int _tmpHourOfDay;
            _tmpHourOfDay = _cursor.getInt(_cursorIndexOfHourOfDay);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new HourActivityCount(_tmpHourOfDay,_tmpCount);
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
  public Flow<List<UserActivityLog>> getRecentActivities(final int limit) {
    final String _sql = "SELECT * FROM user_activity_log ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_activity_log"}, new Callable<List<UserActivityLog>>() {
      @Override
      @NonNull
      public List<UserActivityLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfActivityType = CursorUtil.getColumnIndexOrThrow(_cursor, "activityType");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfHourOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "hourOfDay");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final List<UserActivityLog> _result = new ArrayList<UserActivityLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserActivityLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final ActivityType _tmpActivityType;
            _tmpActivityType = __ActivityType_stringToEnum(_cursor.getString(_cursorIndexOfActivityType));
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmpHourOfDay;
            _tmpHourOfDay = _cursor.getInt(_cursorIndexOfHourOfDay);
            final int _tmpDayOfWeek;
            _tmpDayOfWeek = _cursor.getInt(_cursorIndexOfDayOfWeek);
            _item = new UserActivityLog(_tmpId,_tmpActivityType,_tmpTimestamp,_tmpHourOfDay,_tmpDayOfWeek);
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
  public Object getActivityCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM user_activity_log";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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

  private String __ActivityType_enumToString(@NonNull final ActivityType _value) {
    switch (_value) {
      case APP_OPEN: return "APP_OPEN";
      case CHAPTER_COMPLETE: return "CHAPTER_COMPLETE";
      case SUBJECT_VIEW: return "SUBJECT_VIEW";
      case MOCK_TEST_START: return "MOCK_TEST_START";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private ActivityType __ActivityType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "APP_OPEN": return ActivityType.APP_OPEN;
      case "CHAPTER_COMPLETE": return ActivityType.CHAPTER_COMPLETE;
      case "SUBJECT_VIEW": return ActivityType.SUBJECT_VIEW;
      case "MOCK_TEST_START": return ActivityType.MOCK_TEST_START;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
