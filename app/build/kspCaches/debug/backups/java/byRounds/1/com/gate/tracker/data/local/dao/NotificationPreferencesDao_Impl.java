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
import com.gate.tracker.data.local.entity.NotificationPreferencesEntity;
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
public final class NotificationPreferencesDao_Impl implements NotificationPreferencesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NotificationPreferencesEntity> __insertionAdapterOfNotificationPreferencesEntity;

  private final EntityDeletionOrUpdateAdapter<NotificationPreferencesEntity> __updateAdapterOfNotificationPreferencesEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDailyReminder;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRevisionAlerts;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMockTestReminders;

  private final SharedSQLiteStatement __preparedStmtOfUpdateExamCountdown;

  private final SharedSQLiteStatement __preparedStmtOfUpdateInactivityAlerts;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMotivational;

  private final SharedSQLiteStatement __preparedStmtOfUpdateAchievementNotifications;

  private final SharedSQLiteStatement __preparedStmtOfUpdateQuietHours;

  public NotificationPreferencesDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNotificationPreferencesEntity = new EntityInsertionAdapter<NotificationPreferencesEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `notification_preferences` (`id`,`dailyReminderEnabled`,`dailyReminderTime`,`revisionAlertsEnabled`,`revisionAlertsTime`,`revisionAlertsDays`,`revisionThresholdDays`,`mockTestRemindersEnabled`,`mockTestRemindersTime`,`mockTestRemindersDays`,`mockTestReminderFrequency`,`examCountdownEnabled`,`examCountdownTime`,`inactivityAlertsEnabled`,`inactivityAlertsTime`,`inactivityThresholdDays`,`motivationalEnabled`,`motivationalTime`,`achievementNotificationsEnabled`,`quietHoursEnabled`,`quietHoursStart`,`quietHoursEnd`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NotificationPreferencesEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.getDailyReminderEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindString(3, entity.getDailyReminderTime());
        final int _tmp_1 = entity.getRevisionAlertsEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
        statement.bindString(5, entity.getRevisionAlertsTime());
        statement.bindString(6, entity.getRevisionAlertsDays());
        statement.bindLong(7, entity.getRevisionThresholdDays());
        final int _tmp_2 = entity.getMockTestRemindersEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        statement.bindString(9, entity.getMockTestRemindersTime());
        statement.bindString(10, entity.getMockTestRemindersDays());
        statement.bindLong(11, entity.getMockTestReminderFrequency());
        final int _tmp_3 = entity.getExamCountdownEnabled() ? 1 : 0;
        statement.bindLong(12, _tmp_3);
        statement.bindString(13, entity.getExamCountdownTime());
        final int _tmp_4 = entity.getInactivityAlertsEnabled() ? 1 : 0;
        statement.bindLong(14, _tmp_4);
        statement.bindString(15, entity.getInactivityAlertsTime());
        statement.bindLong(16, entity.getInactivityThresholdDays());
        final int _tmp_5 = entity.getMotivationalEnabled() ? 1 : 0;
        statement.bindLong(17, _tmp_5);
        statement.bindString(18, entity.getMotivationalTime());
        final int _tmp_6 = entity.getAchievementNotificationsEnabled() ? 1 : 0;
        statement.bindLong(19, _tmp_6);
        final int _tmp_7 = entity.getQuietHoursEnabled() ? 1 : 0;
        statement.bindLong(20, _tmp_7);
        statement.bindString(21, entity.getQuietHoursStart());
        statement.bindString(22, entity.getQuietHoursEnd());
      }
    };
    this.__updateAdapterOfNotificationPreferencesEntity = new EntityDeletionOrUpdateAdapter<NotificationPreferencesEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `notification_preferences` SET `id` = ?,`dailyReminderEnabled` = ?,`dailyReminderTime` = ?,`revisionAlertsEnabled` = ?,`revisionAlertsTime` = ?,`revisionAlertsDays` = ?,`revisionThresholdDays` = ?,`mockTestRemindersEnabled` = ?,`mockTestRemindersTime` = ?,`mockTestRemindersDays` = ?,`mockTestReminderFrequency` = ?,`examCountdownEnabled` = ?,`examCountdownTime` = ?,`inactivityAlertsEnabled` = ?,`inactivityAlertsTime` = ?,`inactivityThresholdDays` = ?,`motivationalEnabled` = ?,`motivationalTime` = ?,`achievementNotificationsEnabled` = ?,`quietHoursEnabled` = ?,`quietHoursStart` = ?,`quietHoursEnd` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NotificationPreferencesEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.getDailyReminderEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindString(3, entity.getDailyReminderTime());
        final int _tmp_1 = entity.getRevisionAlertsEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
        statement.bindString(5, entity.getRevisionAlertsTime());
        statement.bindString(6, entity.getRevisionAlertsDays());
        statement.bindLong(7, entity.getRevisionThresholdDays());
        final int _tmp_2 = entity.getMockTestRemindersEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        statement.bindString(9, entity.getMockTestRemindersTime());
        statement.bindString(10, entity.getMockTestRemindersDays());
        statement.bindLong(11, entity.getMockTestReminderFrequency());
        final int _tmp_3 = entity.getExamCountdownEnabled() ? 1 : 0;
        statement.bindLong(12, _tmp_3);
        statement.bindString(13, entity.getExamCountdownTime());
        final int _tmp_4 = entity.getInactivityAlertsEnabled() ? 1 : 0;
        statement.bindLong(14, _tmp_4);
        statement.bindString(15, entity.getInactivityAlertsTime());
        statement.bindLong(16, entity.getInactivityThresholdDays());
        final int _tmp_5 = entity.getMotivationalEnabled() ? 1 : 0;
        statement.bindLong(17, _tmp_5);
        statement.bindString(18, entity.getMotivationalTime());
        final int _tmp_6 = entity.getAchievementNotificationsEnabled() ? 1 : 0;
        statement.bindLong(19, _tmp_6);
        final int _tmp_7 = entity.getQuietHoursEnabled() ? 1 : 0;
        statement.bindLong(20, _tmp_7);
        statement.bindString(21, entity.getQuietHoursStart());
        statement.bindString(22, entity.getQuietHoursEnd());
        statement.bindLong(23, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateDailyReminder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notification_preferences SET dailyReminderEnabled = ?, dailyReminderTime = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRevisionAlerts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notification_preferences SET revisionAlertsEnabled = ?, revisionAlertsTime = ?, revisionAlertsDays = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMockTestReminders = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notification_preferences SET mockTestRemindersEnabled = ?, mockTestRemindersTime = ?, mockTestRemindersDays = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateExamCountdown = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notification_preferences SET examCountdownEnabled = ?, examCountdownTime = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateInactivityAlerts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notification_preferences SET inactivityAlertsEnabled = ?, inactivityThresholdDays = ?, inactivityAlertsTime = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMotivational = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notification_preferences SET motivationalEnabled = ?, motivationalTime = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateAchievementNotifications = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notification_preferences SET achievementNotificationsEnabled = ? WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateQuietHours = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notification_preferences SET quietHoursEnabled = ?, quietHoursStart = ?, quietHoursEnd = ? WHERE id = 1";
        return _query;
      }
    };
  }

  @Override
  public Object insertPreferences(final NotificationPreferencesEntity preferences,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNotificationPreferencesEntity.insert(preferences);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePreferences(final NotificationPreferencesEntity preferences,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfNotificationPreferencesEntity.handle(preferences);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDailyReminder(final boolean enabled, final String time,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDailyReminder.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, time);
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
          __preparedStmtOfUpdateDailyReminder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRevisionAlerts(final boolean enabled, final String time, final String days,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRevisionAlerts.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, time);
        _argIndex = 3;
        _stmt.bindString(_argIndex, days);
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
          __preparedStmtOfUpdateRevisionAlerts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMockTestReminders(final boolean enabled, final String time, final String days,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMockTestReminders.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, time);
        _argIndex = 3;
        _stmt.bindString(_argIndex, days);
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
          __preparedStmtOfUpdateMockTestReminders.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateExamCountdown(final boolean enabled, final String time,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateExamCountdown.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, time);
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
          __preparedStmtOfUpdateExamCountdown.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateInactivityAlerts(final boolean enabled, final int thresholdDays,
      final String time, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateInactivityAlerts.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, thresholdDays);
        _argIndex = 3;
        _stmt.bindString(_argIndex, time);
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
          __preparedStmtOfUpdateInactivityAlerts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMotivational(final boolean enabled, final String time,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMotivational.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, time);
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
          __preparedStmtOfUpdateMotivational.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAchievementNotifications(final boolean enabled,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateAchievementNotifications.acquire();
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
          __preparedStmtOfUpdateAchievementNotifications.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateQuietHours(final boolean enabled, final String start, final String end,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateQuietHours.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, start);
        _argIndex = 3;
        _stmt.bindString(_argIndex, end);
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
          __preparedStmtOfUpdateQuietHours.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<NotificationPreferencesEntity> getPreferences() {
    final String _sql = "SELECT * FROM notification_preferences WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notification_preferences"}, new Callable<NotificationPreferencesEntity>() {
      @Override
      @Nullable
      public NotificationPreferencesEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDailyReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyReminderEnabled");
          final int _cursorIndexOfDailyReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyReminderTime");
          final int _cursorIndexOfRevisionAlertsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionAlertsEnabled");
          final int _cursorIndexOfRevisionAlertsTime = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionAlertsTime");
          final int _cursorIndexOfRevisionAlertsDays = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionAlertsDays");
          final int _cursorIndexOfRevisionThresholdDays = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionThresholdDays");
          final int _cursorIndexOfMockTestRemindersEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "mockTestRemindersEnabled");
          final int _cursorIndexOfMockTestRemindersTime = CursorUtil.getColumnIndexOrThrow(_cursor, "mockTestRemindersTime");
          final int _cursorIndexOfMockTestRemindersDays = CursorUtil.getColumnIndexOrThrow(_cursor, "mockTestRemindersDays");
          final int _cursorIndexOfMockTestReminderFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "mockTestReminderFrequency");
          final int _cursorIndexOfExamCountdownEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "examCountdownEnabled");
          final int _cursorIndexOfExamCountdownTime = CursorUtil.getColumnIndexOrThrow(_cursor, "examCountdownTime");
          final int _cursorIndexOfInactivityAlertsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "inactivityAlertsEnabled");
          final int _cursorIndexOfInactivityAlertsTime = CursorUtil.getColumnIndexOrThrow(_cursor, "inactivityAlertsTime");
          final int _cursorIndexOfInactivityThresholdDays = CursorUtil.getColumnIndexOrThrow(_cursor, "inactivityThresholdDays");
          final int _cursorIndexOfMotivationalEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "motivationalEnabled");
          final int _cursorIndexOfMotivationalTime = CursorUtil.getColumnIndexOrThrow(_cursor, "motivationalTime");
          final int _cursorIndexOfAchievementNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "achievementNotificationsEnabled");
          final int _cursorIndexOfQuietHoursEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "quietHoursEnabled");
          final int _cursorIndexOfQuietHoursStart = CursorUtil.getColumnIndexOrThrow(_cursor, "quietHoursStart");
          final int _cursorIndexOfQuietHoursEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "quietHoursEnd");
          final NotificationPreferencesEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpDailyReminderEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDailyReminderEnabled);
            _tmpDailyReminderEnabled = _tmp != 0;
            final String _tmpDailyReminderTime;
            _tmpDailyReminderTime = _cursor.getString(_cursorIndexOfDailyReminderTime);
            final boolean _tmpRevisionAlertsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRevisionAlertsEnabled);
            _tmpRevisionAlertsEnabled = _tmp_1 != 0;
            final String _tmpRevisionAlertsTime;
            _tmpRevisionAlertsTime = _cursor.getString(_cursorIndexOfRevisionAlertsTime);
            final String _tmpRevisionAlertsDays;
            _tmpRevisionAlertsDays = _cursor.getString(_cursorIndexOfRevisionAlertsDays);
            final int _tmpRevisionThresholdDays;
            _tmpRevisionThresholdDays = _cursor.getInt(_cursorIndexOfRevisionThresholdDays);
            final boolean _tmpMockTestRemindersEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMockTestRemindersEnabled);
            _tmpMockTestRemindersEnabled = _tmp_2 != 0;
            final String _tmpMockTestRemindersTime;
            _tmpMockTestRemindersTime = _cursor.getString(_cursorIndexOfMockTestRemindersTime);
            final String _tmpMockTestRemindersDays;
            _tmpMockTestRemindersDays = _cursor.getString(_cursorIndexOfMockTestRemindersDays);
            final int _tmpMockTestReminderFrequency;
            _tmpMockTestReminderFrequency = _cursor.getInt(_cursorIndexOfMockTestReminderFrequency);
            final boolean _tmpExamCountdownEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfExamCountdownEnabled);
            _tmpExamCountdownEnabled = _tmp_3 != 0;
            final String _tmpExamCountdownTime;
            _tmpExamCountdownTime = _cursor.getString(_cursorIndexOfExamCountdownTime);
            final boolean _tmpInactivityAlertsEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfInactivityAlertsEnabled);
            _tmpInactivityAlertsEnabled = _tmp_4 != 0;
            final String _tmpInactivityAlertsTime;
            _tmpInactivityAlertsTime = _cursor.getString(_cursorIndexOfInactivityAlertsTime);
            final int _tmpInactivityThresholdDays;
            _tmpInactivityThresholdDays = _cursor.getInt(_cursorIndexOfInactivityThresholdDays);
            final boolean _tmpMotivationalEnabled;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfMotivationalEnabled);
            _tmpMotivationalEnabled = _tmp_5 != 0;
            final String _tmpMotivationalTime;
            _tmpMotivationalTime = _cursor.getString(_cursorIndexOfMotivationalTime);
            final boolean _tmpAchievementNotificationsEnabled;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfAchievementNotificationsEnabled);
            _tmpAchievementNotificationsEnabled = _tmp_6 != 0;
            final boolean _tmpQuietHoursEnabled;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfQuietHoursEnabled);
            _tmpQuietHoursEnabled = _tmp_7 != 0;
            final String _tmpQuietHoursStart;
            _tmpQuietHoursStart = _cursor.getString(_cursorIndexOfQuietHoursStart);
            final String _tmpQuietHoursEnd;
            _tmpQuietHoursEnd = _cursor.getString(_cursorIndexOfQuietHoursEnd);
            _result = new NotificationPreferencesEntity(_tmpId,_tmpDailyReminderEnabled,_tmpDailyReminderTime,_tmpRevisionAlertsEnabled,_tmpRevisionAlertsTime,_tmpRevisionAlertsDays,_tmpRevisionThresholdDays,_tmpMockTestRemindersEnabled,_tmpMockTestRemindersTime,_tmpMockTestRemindersDays,_tmpMockTestReminderFrequency,_tmpExamCountdownEnabled,_tmpExamCountdownTime,_tmpInactivityAlertsEnabled,_tmpInactivityAlertsTime,_tmpInactivityThresholdDays,_tmpMotivationalEnabled,_tmpMotivationalTime,_tmpAchievementNotificationsEnabled,_tmpQuietHoursEnabled,_tmpQuietHoursStart,_tmpQuietHoursEnd);
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
  public Object getPreferencesOnce(
      final Continuation<? super NotificationPreferencesEntity> $completion) {
    final String _sql = "SELECT * FROM notification_preferences WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<NotificationPreferencesEntity>() {
      @Override
      @Nullable
      public NotificationPreferencesEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDailyReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyReminderEnabled");
          final int _cursorIndexOfDailyReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyReminderTime");
          final int _cursorIndexOfRevisionAlertsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionAlertsEnabled");
          final int _cursorIndexOfRevisionAlertsTime = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionAlertsTime");
          final int _cursorIndexOfRevisionAlertsDays = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionAlertsDays");
          final int _cursorIndexOfRevisionThresholdDays = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionThresholdDays");
          final int _cursorIndexOfMockTestRemindersEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "mockTestRemindersEnabled");
          final int _cursorIndexOfMockTestRemindersTime = CursorUtil.getColumnIndexOrThrow(_cursor, "mockTestRemindersTime");
          final int _cursorIndexOfMockTestRemindersDays = CursorUtil.getColumnIndexOrThrow(_cursor, "mockTestRemindersDays");
          final int _cursorIndexOfMockTestReminderFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "mockTestReminderFrequency");
          final int _cursorIndexOfExamCountdownEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "examCountdownEnabled");
          final int _cursorIndexOfExamCountdownTime = CursorUtil.getColumnIndexOrThrow(_cursor, "examCountdownTime");
          final int _cursorIndexOfInactivityAlertsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "inactivityAlertsEnabled");
          final int _cursorIndexOfInactivityAlertsTime = CursorUtil.getColumnIndexOrThrow(_cursor, "inactivityAlertsTime");
          final int _cursorIndexOfInactivityThresholdDays = CursorUtil.getColumnIndexOrThrow(_cursor, "inactivityThresholdDays");
          final int _cursorIndexOfMotivationalEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "motivationalEnabled");
          final int _cursorIndexOfMotivationalTime = CursorUtil.getColumnIndexOrThrow(_cursor, "motivationalTime");
          final int _cursorIndexOfAchievementNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "achievementNotificationsEnabled");
          final int _cursorIndexOfQuietHoursEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "quietHoursEnabled");
          final int _cursorIndexOfQuietHoursStart = CursorUtil.getColumnIndexOrThrow(_cursor, "quietHoursStart");
          final int _cursorIndexOfQuietHoursEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "quietHoursEnd");
          final NotificationPreferencesEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpDailyReminderEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDailyReminderEnabled);
            _tmpDailyReminderEnabled = _tmp != 0;
            final String _tmpDailyReminderTime;
            _tmpDailyReminderTime = _cursor.getString(_cursorIndexOfDailyReminderTime);
            final boolean _tmpRevisionAlertsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRevisionAlertsEnabled);
            _tmpRevisionAlertsEnabled = _tmp_1 != 0;
            final String _tmpRevisionAlertsTime;
            _tmpRevisionAlertsTime = _cursor.getString(_cursorIndexOfRevisionAlertsTime);
            final String _tmpRevisionAlertsDays;
            _tmpRevisionAlertsDays = _cursor.getString(_cursorIndexOfRevisionAlertsDays);
            final int _tmpRevisionThresholdDays;
            _tmpRevisionThresholdDays = _cursor.getInt(_cursorIndexOfRevisionThresholdDays);
            final boolean _tmpMockTestRemindersEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMockTestRemindersEnabled);
            _tmpMockTestRemindersEnabled = _tmp_2 != 0;
            final String _tmpMockTestRemindersTime;
            _tmpMockTestRemindersTime = _cursor.getString(_cursorIndexOfMockTestRemindersTime);
            final String _tmpMockTestRemindersDays;
            _tmpMockTestRemindersDays = _cursor.getString(_cursorIndexOfMockTestRemindersDays);
            final int _tmpMockTestReminderFrequency;
            _tmpMockTestReminderFrequency = _cursor.getInt(_cursorIndexOfMockTestReminderFrequency);
            final boolean _tmpExamCountdownEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfExamCountdownEnabled);
            _tmpExamCountdownEnabled = _tmp_3 != 0;
            final String _tmpExamCountdownTime;
            _tmpExamCountdownTime = _cursor.getString(_cursorIndexOfExamCountdownTime);
            final boolean _tmpInactivityAlertsEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfInactivityAlertsEnabled);
            _tmpInactivityAlertsEnabled = _tmp_4 != 0;
            final String _tmpInactivityAlertsTime;
            _tmpInactivityAlertsTime = _cursor.getString(_cursorIndexOfInactivityAlertsTime);
            final int _tmpInactivityThresholdDays;
            _tmpInactivityThresholdDays = _cursor.getInt(_cursorIndexOfInactivityThresholdDays);
            final boolean _tmpMotivationalEnabled;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfMotivationalEnabled);
            _tmpMotivationalEnabled = _tmp_5 != 0;
            final String _tmpMotivationalTime;
            _tmpMotivationalTime = _cursor.getString(_cursorIndexOfMotivationalTime);
            final boolean _tmpAchievementNotificationsEnabled;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfAchievementNotificationsEnabled);
            _tmpAchievementNotificationsEnabled = _tmp_6 != 0;
            final boolean _tmpQuietHoursEnabled;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfQuietHoursEnabled);
            _tmpQuietHoursEnabled = _tmp_7 != 0;
            final String _tmpQuietHoursStart;
            _tmpQuietHoursStart = _cursor.getString(_cursorIndexOfQuietHoursStart);
            final String _tmpQuietHoursEnd;
            _tmpQuietHoursEnd = _cursor.getString(_cursorIndexOfQuietHoursEnd);
            _result = new NotificationPreferencesEntity(_tmpId,_tmpDailyReminderEnabled,_tmpDailyReminderTime,_tmpRevisionAlertsEnabled,_tmpRevisionAlertsTime,_tmpRevisionAlertsDays,_tmpRevisionThresholdDays,_tmpMockTestRemindersEnabled,_tmpMockTestRemindersTime,_tmpMockTestRemindersDays,_tmpMockTestReminderFrequency,_tmpExamCountdownEnabled,_tmpExamCountdownTime,_tmpInactivityAlertsEnabled,_tmpInactivityAlertsTime,_tmpInactivityThresholdDays,_tmpMotivationalEnabled,_tmpMotivationalTime,_tmpAchievementNotificationsEnabled,_tmpQuietHoursEnabled,_tmpQuietHoursStart,_tmpQuietHoursEnd);
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
