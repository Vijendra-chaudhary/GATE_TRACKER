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
import com.gate.tracker.data.local.entity.ChapterResourceEntity;
import com.gate.tracker.data.local.entity.ResourceType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
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
public final class ChapterResourceDao_Impl implements ChapterResourceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChapterResourceEntity> __insertionAdapterOfChapterResourceEntity;

  private final EntityDeletionOrUpdateAdapter<ChapterResourceEntity> __deletionAdapterOfChapterResourceEntity;

  private final EntityDeletionOrUpdateAdapter<ChapterResourceEntity> __updateAdapterOfChapterResourceEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllForChapter;

  public ChapterResourceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChapterResourceEntity = new EntityInsertionAdapter<ChapterResourceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chapter_resources` (`id`,`chapterId`,`type`,`title`,`uri`,`driveFileId`,`fileSize`,`mimeType`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChapterResourceEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChapterId());
        statement.bindString(3, __ResourceType_enumToString(entity.getType()));
        statement.bindString(4, entity.getTitle());
        statement.bindString(5, entity.getUri());
        if (entity.getDriveFileId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDriveFileId());
        }
        statement.bindLong(7, entity.getFileSize());
        if (entity.getMimeType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMimeType());
        }
        statement.bindLong(9, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfChapterResourceEntity = new EntityDeletionOrUpdateAdapter<ChapterResourceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `chapter_resources` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChapterResourceEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfChapterResourceEntity = new EntityDeletionOrUpdateAdapter<ChapterResourceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `chapter_resources` SET `id` = ?,`chapterId` = ?,`type` = ?,`title` = ?,`uri` = ?,`driveFileId` = ?,`fileSize` = ?,`mimeType` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChapterResourceEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChapterId());
        statement.bindString(3, __ResourceType_enumToString(entity.getType()));
        statement.bindString(4, entity.getTitle());
        statement.bindString(5, entity.getUri());
        if (entity.getDriveFileId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDriveFileId());
        }
        statement.bindLong(7, entity.getFileSize());
        if (entity.getMimeType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMimeType());
        }
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM chapter_resources WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllForChapter = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM chapter_resources WHERE chapterId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ChapterResourceEntity resource,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfChapterResourceEntity.insertAndReturnId(resource);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ChapterResourceEntity resource,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfChapterResourceEntity.handle(resource);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ChapterResourceEntity resource,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChapterResourceEntity.handle(resource);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final int resourceId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, resourceId);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllForChapter(final int chapterId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllForChapter.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, chapterId);
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
          __preparedStmtOfDeleteAllForChapter.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ChapterResourceEntity>> getResourcesForChapter(final int chapterId) {
    final String _sql = "SELECT * FROM chapter_resources WHERE chapterId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapter_resources"}, new Callable<List<ChapterResourceEntity>>() {
      @Override
      @NonNull
      public List<ChapterResourceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "driveFileId");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ChapterResourceEntity> _result = new ArrayList<ChapterResourceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterResourceEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final ResourceType _tmpType;
            _tmpType = __ResourceType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ChapterResourceEntity(_tmpId,_tmpChapterId,_tmpType,_tmpTitle,_tmpUri,_tmpDriveFileId,_tmpFileSize,_tmpMimeType,_tmpCreatedAt);
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
  public Object getResourcesForChapterSync(final int chapterId,
      final Continuation<? super List<ChapterResourceEntity>> $completion) {
    final String _sql = "SELECT * FROM chapter_resources WHERE chapterId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ChapterResourceEntity>>() {
      @Override
      @NonNull
      public List<ChapterResourceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "driveFileId");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ChapterResourceEntity> _result = new ArrayList<ChapterResourceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterResourceEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final ResourceType _tmpType;
            _tmpType = __ResourceType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ChapterResourceEntity(_tmpId,_tmpChapterId,_tmpType,_tmpTitle,_tmpUri,_tmpDriveFileId,_tmpFileSize,_tmpMimeType,_tmpCreatedAt);
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
  public Object getResourceById(final int resourceId,
      final Continuation<? super ChapterResourceEntity> $completion) {
    final String _sql = "SELECT * FROM chapter_resources WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, resourceId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ChapterResourceEntity>() {
      @Override
      @Nullable
      public ChapterResourceEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "driveFileId");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final ChapterResourceEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final ResourceType _tmpType;
            _tmpType = __ResourceType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new ChapterResourceEntity(_tmpId,_tmpChapterId,_tmpType,_tmpTitle,_tmpUri,_tmpDriveFileId,_tmpFileSize,_tmpMimeType,_tmpCreatedAt);
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
  public Object getAllResourcesSync(
      final Continuation<? super List<ChapterResourceEntity>> $completion) {
    final String _sql = "SELECT * FROM chapter_resources";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ChapterResourceEntity>>() {
      @Override
      @NonNull
      public List<ChapterResourceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "driveFileId");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ChapterResourceEntity> _result = new ArrayList<ChapterResourceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterResourceEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final ResourceType _tmpType;
            _tmpType = __ResourceType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ChapterResourceEntity(_tmpId,_tmpChapterId,_tmpType,_tmpTitle,_tmpUri,_tmpDriveFileId,_tmpFileSize,_tmpMimeType,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __ResourceType_enumToString(@NonNull final ResourceType _value) {
    switch (_value) {
      case PDF: return "PDF";
      case URL: return "URL";
      case IMAGE: return "IMAGE";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private ResourceType __ResourceType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "PDF": return ResourceType.PDF;
      case "URL": return ResourceType.URL;
      case "IMAGE": return ResourceType.IMAGE;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
