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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gate.tracker.data.local.entity.ChapterEntity;
import com.gate.tracker.data.local.entity.ChapterNoteEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class ChapterDao_Impl implements ChapterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChapterEntity> __insertionAdapterOfChapterEntity;

  private final EntityInsertionAdapter<ChapterNoteEntity> __insertionAdapterOfChapterNoteEntity;

  private final EntityDeletionOrUpdateAdapter<ChapterNoteEntity> __updateAdapterOfChapterNoteEntity;

  private final EntityDeletionOrUpdateAdapter<ChapterEntity> __updateAdapterOfChapterEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateChapterStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteChaptersByBranch;

  private final SharedSQLiteStatement __preparedStmtOfDeleteChaptersForSubject;

  private final SharedSQLiteStatement __preparedStmtOfResetAllChapters;

  private final SharedSQLiteStatement __preparedStmtOfDeleteNote;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsRevised;

  private final SharedSQLiteStatement __preparedStmtOfResetAllRevisions;

  public ChapterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChapterEntity = new EntityInsertionAdapter<ChapterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chapters` (`id`,`subjectId`,`name`,`orderIndex`,`isCompleted`,`completedDate`,`category`,`isRevised`,`revisedDate`,`revisionCount`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChapterEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getOrderIndex());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(5, _tmp);
        if (entity.getCompletedDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCompletedDate());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCategory());
        }
        final int _tmp_1 = entity.isRevised() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getRevisedDate() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getRevisedDate());
        }
        statement.bindLong(10, entity.getRevisionCount());
      }
    };
    this.__insertionAdapterOfChapterNoteEntity = new EntityInsertionAdapter<ChapterNoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chapter_notes` (`id`,`chapterId`,`noteText`,`isImportant`,`needsRevision`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChapterNoteEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChapterId());
        statement.bindString(3, entity.getNoteText());
        final int _tmp = entity.isImportant() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getNeedsRevision() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfChapterNoteEntity = new EntityDeletionOrUpdateAdapter<ChapterNoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `chapter_notes` SET `id` = ?,`chapterId` = ?,`noteText` = ?,`isImportant` = ?,`needsRevision` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChapterNoteEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChapterId());
        statement.bindString(3, entity.getNoteText());
        final int _tmp = entity.isImportant() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getNeedsRevision() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__updateAdapterOfChapterEntity = new EntityDeletionOrUpdateAdapter<ChapterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `chapters` SET `id` = ?,`subjectId` = ?,`name` = ?,`orderIndex` = ?,`isCompleted` = ?,`completedDate` = ?,`category` = ?,`isRevised` = ?,`revisedDate` = ?,`revisionCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChapterEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getOrderIndex());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(5, _tmp);
        if (entity.getCompletedDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCompletedDate());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCategory());
        }
        final int _tmp_1 = entity.isRevised() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getRevisedDate() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getRevisedDate());
        }
        statement.bindLong(10, entity.getRevisionCount());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateChapterStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chapters SET isCompleted = ?, completedDate = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteChaptersByBranch = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM chapters WHERE subjectId IN (SELECT id FROM subjects WHERE branchId = ?)";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteChaptersForSubject = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM chapters WHERE subjectId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetAllChapters = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chapters SET isCompleted = 0, completedDate = NULL WHERE subjectId IN (SELECT id FROM subjects WHERE branchId = ?)";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM chapter_notes WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkAsRevised = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chapters SET isRevised = ?, revisedDate = ?, revisionCount = revisionCount + 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetAllRevisions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chapters SET isRevised = 0, revisedDate = NULL WHERE subjectId IN (SELECT id FROM subjects WHERE branchId = ?)";
        return _query;
      }
    };
  }

  @Override
  public Object insertChapters(final List<ChapterEntity> chapters,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChapterEntity.insert(chapters);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertNote(final ChapterNoteEntity note,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfChapterNoteEntity.insertAndReturnId(note);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insert(final ChapterEntity chapter, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChapterEntity.insert(chapter);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateNote(final ChapterNoteEntity note,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChapterNoteEntity.handle(note);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ChapterEntity chapter, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChapterEntity.handle(chapter);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateChapterStatus(final int chapterId, final boolean isCompleted,
      final Long completedDate, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateChapterStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isCompleted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (completedDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, completedDate);
        }
        _argIndex = 3;
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
          __preparedStmtOfUpdateChapterStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteChaptersByBranch(final int branchId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteChaptersByBranch.acquire();
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
          __preparedStmtOfDeleteChaptersByBranch.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteChaptersForSubject(final int subjectId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteChaptersForSubject.acquire();
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
          __preparedStmtOfDeleteChaptersForSubject.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetAllChapters(final int branchId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetAllChapters.acquire();
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
          __preparedStmtOfResetAllChapters.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteNote(final int noteId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteNote.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, noteId);
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
          __preparedStmtOfDeleteNote.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsRevised(final int chapterId, final boolean isRevised, final Long date,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsRevised.acquire();
        int _argIndex = 1;
        final int _tmp = isRevised ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (date == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, date);
        }
        _argIndex = 3;
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
          __preparedStmtOfMarkAsRevised.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetAllRevisions(final int branchId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetAllRevisions.acquire();
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
          __preparedStmtOfResetAllRevisions.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ChapterEntity>> getChaptersBySubject(final int subjectId) {
    final String _sql = "SELECT * FROM chapters WHERE subjectId = ? ORDER BY orderIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters"}, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Object getChaptersBySubjectSync(final int subjectId,
      final Continuation<? super List<ChapterEntity>> $completion) {
    final String _sql = "SELECT * FROM chapters WHERE subjectId = ? ORDER BY orderIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Object getCompletedCount(final int subjectId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM chapters WHERE subjectId = ? AND isCompleted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
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

  @Override
  public Flow<List<ChapterEntity>> getChaptersByBranch(final int branchId) {
    final String _sql = "SELECT * FROM chapters WHERE subjectId IN (SELECT id FROM subjects WHERE branchId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters",
        "subjects"}, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Flow<Integer> getTotalCompletedChapters(final int branchId) {
    final String _sql = "SELECT COUNT(*) FROM chapters WHERE isCompleted = 1 AND subjectId IN (SELECT id FROM subjects WHERE branchId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters",
        "subjects"}, new Callable<Integer>() {
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
  public Object getTotalCompletedChaptersSync(final int branchId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM chapters WHERE isCompleted = 1 AND subjectId IN (SELECT id FROM subjects WHERE branchId = ?)";
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

  @Override
  public Object getLastCompletedChapter(final int branchId,
      final Continuation<? super ChapterEntity> $completion) {
    final String _sql = "SELECT * FROM chapters WHERE isCompleted = 1 AND completedDate IS NOT NULL AND subjectId IN (SELECT id FROM subjects WHERE branchId = ?) ORDER BY completedDate DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ChapterEntity>() {
      @Override
      @Nullable
      public ChapterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final ChapterEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _result = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Flow<List<ChapterEntity>> getAllCompletedChaptersWithDates(final int branchId) {
    final String _sql = "SELECT * FROM chapters WHERE isCompleted = 1 AND completedDate IS NOT NULL AND subjectId IN (SELECT id FROM subjects WHERE branchId = ?) ORDER BY completedDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters",
        "subjects"}, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Object getCategoriesForSubject(final int subjectId,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT DISTINCT category FROM chapters WHERE subjectId = ? AND category IS NOT NULL ORDER BY orderIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Flow<List<ChapterEntity>> getChaptersByCategory(final int subjectId,
      final String category) {
    final String _sql = "SELECT * FROM chapters WHERE subjectId = ? AND category = ? ORDER BY orderIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    _argIndex = 2;
    _statement.bindString(_argIndex, category);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters"}, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Object getLastCompletionDateForSubject(final int subjectId,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(completedDate) FROM chapters WHERE subjectId = ? AND completedDate IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
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
  public Object getLastRevisionDateForSubject(final int subjectId,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(revisedDate) FROM chapters WHERE subjectId = ? AND revisedDate IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
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
  public Flow<List<ChapterEntity>> getChaptersCompletedOnDate(final int branchId,
      final long startOfDay, final long endOfDay) {
    final String _sql = "SELECT * FROM chapters WHERE isCompleted = 1 AND completedDate >= ? AND completedDate <= ? AND subjectId IN (SELECT id FROM subjects WHERE branchId = ?) ORDER BY completedDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    _argIndex = 3;
    _statement.bindLong(_argIndex, branchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters",
        "subjects"}, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Flow<List<ChapterEntity>> getCompletedChaptersInRange(final int branchId,
      final long startDate, final long endDate) {
    final String _sql = "SELECT * FROM chapters WHERE isCompleted = 1 AND completedDate >= ? AND completedDate <= ? AND subjectId IN (SELECT id FROM subjects WHERE branchId = ?) ORDER BY completedDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    _argIndex = 3;
    _statement.bindLong(_argIndex, branchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters",
        "subjects"}, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Object getCompletionCountForDate(final int branchId, final long startOfDay,
      final long endOfDay, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM chapters WHERE isCompleted = 1 AND completedDate >= ? AND completedDate <= ? AND subjectId IN (SELECT id FROM subjects WHERE branchId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    _argIndex = 3;
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

  @Override
  public Object getTotalChapterCount(final int branchId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM chapters WHERE subjectId IN (SELECT id FROM subjects WHERE branchId = ?)";
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

  @Override
  public Flow<ChapterNoteEntity> getNoteForChapter(final int chapterId) {
    final String _sql = "SELECT * FROM chapter_notes WHERE chapterId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapter_notes"}, new Callable<ChapterNoteEntity>() {
      @Override
      @Nullable
      public ChapterNoteEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "noteText");
          final int _cursorIndexOfIsImportant = CursorUtil.getColumnIndexOrThrow(_cursor, "isImportant");
          final int _cursorIndexOfNeedsRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "needsRevision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ChapterNoteEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final String _tmpNoteText;
            _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
            final boolean _tmpIsImportant;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsImportant);
            _tmpIsImportant = _tmp != 0;
            final boolean _tmpNeedsRevision;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfNeedsRevision);
            _tmpNeedsRevision = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ChapterNoteEntity(_tmpId,_tmpChapterId,_tmpNoteText,_tmpIsImportant,_tmpNeedsRevision,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getNoteForChapterSync(final int chapterId,
      final Continuation<? super ChapterNoteEntity> $completion) {
    final String _sql = "SELECT * FROM chapter_notes WHERE chapterId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ChapterNoteEntity>() {
      @Override
      @Nullable
      public ChapterNoteEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "noteText");
          final int _cursorIndexOfIsImportant = CursorUtil.getColumnIndexOrThrow(_cursor, "isImportant");
          final int _cursorIndexOfNeedsRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "needsRevision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ChapterNoteEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final String _tmpNoteText;
            _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
            final boolean _tmpIsImportant;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsImportant);
            _tmpIsImportant = _tmp != 0;
            final boolean _tmpNeedsRevision;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfNeedsRevision);
            _tmpNeedsRevision = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ChapterNoteEntity(_tmpId,_tmpChapterId,_tmpNoteText,_tmpIsImportant,_tmpNeedsRevision,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<ChapterNoteEntity>> getNotesForSubject(final int subjectId) {
    final String _sql = "SELECT * FROM chapter_notes WHERE chapterId IN (SELECT id FROM chapters WHERE subjectId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapter_notes",
        "chapters"}, new Callable<List<ChapterNoteEntity>>() {
      @Override
      @NonNull
      public List<ChapterNoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "noteText");
          final int _cursorIndexOfIsImportant = CursorUtil.getColumnIndexOrThrow(_cursor, "isImportant");
          final int _cursorIndexOfNeedsRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "needsRevision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ChapterNoteEntity> _result = new ArrayList<ChapterNoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterNoteEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final String _tmpNoteText;
            _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
            final boolean _tmpIsImportant;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsImportant);
            _tmpIsImportant = _tmp != 0;
            final boolean _tmpNeedsRevision;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfNeedsRevision);
            _tmpNeedsRevision = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ChapterNoteEntity(_tmpId,_tmpChapterId,_tmpNoteText,_tmpIsImportant,_tmpNeedsRevision,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<ChapterNoteEntity>> getAllRevisionNotes() {
    final String _sql = "SELECT * FROM chapter_notes WHERE needsRevision = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapter_notes"}, new Callable<List<ChapterNoteEntity>>() {
      @Override
      @NonNull
      public List<ChapterNoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "noteText");
          final int _cursorIndexOfIsImportant = CursorUtil.getColumnIndexOrThrow(_cursor, "isImportant");
          final int _cursorIndexOfNeedsRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "needsRevision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ChapterNoteEntity> _result = new ArrayList<ChapterNoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterNoteEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final String _tmpNoteText;
            _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
            final boolean _tmpIsImportant;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsImportant);
            _tmpIsImportant = _tmp != 0;
            final boolean _tmpNeedsRevision;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfNeedsRevision);
            _tmpNeedsRevision = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ChapterNoteEntity(_tmpId,_tmpChapterId,_tmpNoteText,_tmpIsImportant,_tmpNeedsRevision,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getRevisedCount(final int subjectId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM chapters WHERE subjectId = ? AND isRevised = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
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

  @Override
  public Flow<Integer> getTotalRevisedChapters(final int branchId) {
    final String _sql = "SELECT COUNT(*) FROM chapters WHERE isRevised = 1 AND subjectId IN (SELECT id FROM subjects WHERE branchId = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters",
        "subjects"}, new Callable<Integer>() {
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
  public Flow<List<ChapterEntity>> getChaptersByRevisionPriority(final int subjectId) {
    final String _sql = "SELECT * FROM chapters WHERE subjectId = ? ORDER BY revisionCount ASC, revisedDate ASC, orderIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters"}, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Object getChapterByIdSync(final int chapterId,
      final Continuation<? super ChapterEntity> $completion) {
    final String _sql = "SELECT * FROM chapters WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ChapterEntity>() {
      @Override
      @Nullable
      public ChapterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "orderIndex");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevised");
          final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "revisedDate");
          final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "revisionCount");
          final ChapterEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final boolean _tmpIsRevised;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRevised);
            _tmpIsRevised = _tmp_1 != 0;
            final Long _tmpRevisedDate;
            if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
              _tmpRevisedDate = null;
            } else {
              _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
            }
            final int _tmpRevisionCount;
            _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
            _result = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
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
  public Object getNoteByChapterIdSync(final int chapterId,
      final Continuation<? super ChapterNoteEntity> $completion) {
    final String _sql = "SELECT * FROM chapter_notes WHERE chapterId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ChapterNoteEntity>() {
      @Override
      @Nullable
      public ChapterNoteEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "noteText");
          final int _cursorIndexOfIsImportant = CursorUtil.getColumnIndexOrThrow(_cursor, "isImportant");
          final int _cursorIndexOfNeedsRevision = CursorUtil.getColumnIndexOrThrow(_cursor, "needsRevision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ChapterNoteEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final String _tmpNoteText;
            _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
            final boolean _tmpIsImportant;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsImportant);
            _tmpIsImportant = _tmp != 0;
            final boolean _tmpNeedsRevision;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfNeedsRevision);
            _tmpNeedsRevision = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ChapterNoteEntity(_tmpId,_tmpChapterId,_tmpNoteText,_tmpIsImportant,_tmpNeedsRevision,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object markChaptersAsPreExisting(final List<Integer> chapterIds,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE chapters SET isCompleted = 1, completedDate = NULL WHERE id IN (");
        final int _inputSize = chapterIds.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (int _item : chapterIds) {
          _stmt.bindLong(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
