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
import com.gate.tracker.data.local.entity.ResourceEntity;
import com.gate.tracker.data.local.entity.ResourceType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
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
public final class ResourceDao_Impl implements ResourceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ResourceEntity> __insertionAdapterOfResourceEntity;

  private final EntityDeletionOrUpdateAdapter<ResourceEntity> __deletionAdapterOfResourceEntity;

  private final EntityDeletionOrUpdateAdapter<ResourceEntity> __updateAdapterOfResourceEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllResourcesForSubject;

  public ResourceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfResourceEntity = new EntityInsertionAdapter<ResourceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `resources` (`id`,`subjectId`,`resourceType`,`title`,`uri`,`description`,`fileSize`,`createdAt`,`driveFileId`,`thumbnailUrl`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ResourceEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindString(3, __ResourceType_enumToString(entity.getResourceType()));
        statement.bindString(4, entity.getTitle());
        statement.bindString(5, entity.getUri());
        statement.bindString(6, entity.getDescription());
        if (entity.getFileSize() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getFileSize());
        }
        statement.bindLong(8, entity.getCreatedAt());
        if (entity.getDriveFileId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDriveFileId());
        }
        if (entity.getThumbnailUrl() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getThumbnailUrl());
        }
      }
    };
    this.__deletionAdapterOfResourceEntity = new EntityDeletionOrUpdateAdapter<ResourceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `resources` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ResourceEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfResourceEntity = new EntityDeletionOrUpdateAdapter<ResourceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `resources` SET `id` = ?,`subjectId` = ?,`resourceType` = ?,`title` = ?,`uri` = ?,`description` = ?,`fileSize` = ?,`createdAt` = ?,`driveFileId` = ?,`thumbnailUrl` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ResourceEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindString(3, __ResourceType_enumToString(entity.getResourceType()));
        statement.bindString(4, entity.getTitle());
        statement.bindString(5, entity.getUri());
        statement.bindString(6, entity.getDescription());
        if (entity.getFileSize() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getFileSize());
        }
        statement.bindLong(8, entity.getCreatedAt());
        if (entity.getDriveFileId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDriveFileId());
        }
        if (entity.getThumbnailUrl() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getThumbnailUrl());
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllResourcesForSubject = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM resources WHERE subjectId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertResource(final ResourceEntity resource,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfResourceEntity.insertAndReturnId(resource);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteResource(final ResourceEntity resource,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfResourceEntity.handle(resource);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateResource(final ResourceEntity resource,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfResourceEntity.handle(resource);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllResourcesForSubject(final int subjectId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllResourcesForSubject.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfDeleteAllResourcesForSubject.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ResourceEntity>> getResourcesBySubject(final int subjectId) {
    final String _sql = "SELECT * FROM resources WHERE subjectId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"resources"}, new Callable<List<ResourceEntity>>() {
      @Override
      @NonNull
      public List<ResourceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfResourceType = CursorUtil.getColumnIndexOrThrow(_cursor, "resourceType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "driveFileId");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final List<ResourceEntity> _result = new ArrayList<ResourceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ResourceEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final ResourceType _tmpResourceType;
            _tmpResourceType = __ResourceType_stringToEnum(_cursor.getString(_cursorIndexOfResourceType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Long _tmpFileSize;
            if (_cursor.isNull(_cursorIndexOfFileSize)) {
              _tmpFileSize = null;
            } else {
              _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            _item = new ResourceEntity(_tmpId,_tmpSubjectId,_tmpResourceType,_tmpTitle,_tmpUri,_tmpDescription,_tmpFileSize,_tmpCreatedAt,_tmpDriveFileId,_tmpThumbnailUrl);
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
  public Object getResourceById(final int id,
      final Continuation<? super ResourceEntity> $completion) {
    final String _sql = "SELECT * FROM resources WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ResourceEntity>() {
      @Override
      @Nullable
      public ResourceEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfResourceType = CursorUtil.getColumnIndexOrThrow(_cursor, "resourceType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "driveFileId");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final ResourceEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final ResourceType _tmpResourceType;
            _tmpResourceType = __ResourceType_stringToEnum(_cursor.getString(_cursorIndexOfResourceType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Long _tmpFileSize;
            if (_cursor.isNull(_cursorIndexOfFileSize)) {
              _tmpFileSize = null;
            } else {
              _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            _result = new ResourceEntity(_tmpId,_tmpSubjectId,_tmpResourceType,_tmpTitle,_tmpUri,_tmpDescription,_tmpFileSize,_tmpCreatedAt,_tmpDriveFileId,_tmpThumbnailUrl);
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
  public Flow<Integer> getResourceCount(final int subjectId) {
    final String _sql = "SELECT COUNT(*) FROM resources WHERE subjectId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"resources"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllResourcesSync(final Continuation<? super List<ResourceEntity>> $completion) {
    final String _sql = "SELECT * FROM resources";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ResourceEntity>>() {
      @Override
      @NonNull
      public List<ResourceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfResourceType = CursorUtil.getColumnIndexOrThrow(_cursor, "resourceType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDriveFileId = CursorUtil.getColumnIndexOrThrow(_cursor, "driveFileId");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final List<ResourceEntity> _result = new ArrayList<ResourceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ResourceEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final ResourceType _tmpResourceType;
            _tmpResourceType = __ResourceType_stringToEnum(_cursor.getString(_cursorIndexOfResourceType));
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Long _tmpFileSize;
            if (_cursor.isNull(_cursorIndexOfFileSize)) {
              _tmpFileSize = null;
            } else {
              _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpDriveFileId;
            if (_cursor.isNull(_cursorIndexOfDriveFileId)) {
              _tmpDriveFileId = null;
            } else {
              _tmpDriveFileId = _cursor.getString(_cursorIndexOfDriveFileId);
            }
            final String _tmpThumbnailUrl;
            if (_cursor.isNull(_cursorIndexOfThumbnailUrl)) {
              _tmpThumbnailUrl = null;
            } else {
              _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            }
            _item = new ResourceEntity(_tmpId,_tmpSubjectId,_tmpResourceType,_tmpTitle,_tmpUri,_tmpDescription,_tmpFileSize,_tmpCreatedAt,_tmpDriveFileId,_tmpThumbnailUrl);
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
