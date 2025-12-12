package com.gate.tracker.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.gate.tracker.data.local.entity.ChapterEntity;
import com.gate.tracker.data.local.entity.SubjectEntity;
import com.gate.tracker.data.local.entity.TodoEntity;
import com.gate.tracker.data.local.entity.TodoWithDetails;
import java.lang.Boolean;
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
public final class TodoDao_Impl implements TodoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TodoEntity> __insertionAdapterOfTodoEntity;

  private final EntityDeletionOrUpdateAdapter<TodoEntity> __deletionAdapterOfTodoEntity;

  private final EntityDeletionOrUpdateAdapter<TodoEntity> __updateAdapterOfTodoEntity;

  private final SharedSQLiteStatement __preparedStmtOfToggleTodo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTodoById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllByBranch;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByChapterId;

  public TodoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTodoEntity = new EntityInsertionAdapter<TodoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `todos` (`id`,`chapterId`,`branchId`,`isCompleted`,`isRevisionMode`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TodoEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChapterId());
        statement.bindLong(3, entity.getBranchId());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isRevisionMode() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfTodoEntity = new EntityDeletionOrUpdateAdapter<TodoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `todos` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TodoEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTodoEntity = new EntityDeletionOrUpdateAdapter<TodoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `todos` SET `id` = ?,`chapterId` = ?,`branchId` = ?,`isCompleted` = ?,`isRevisionMode` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TodoEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChapterId());
        statement.bindLong(3, entity.getBranchId());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isRevisionMode() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfToggleTodo = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE todos SET isCompleted = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTodoById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM todos WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllByBranch = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM todos WHERE branchId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByChapterId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM todos WHERE chapterId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTodo(final TodoEntity todo, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTodoEntity.insertAndReturnId(todo);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTodo(final TodoEntity todo, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTodoEntity.handle(todo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTodo(final TodoEntity todo, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTodoEntity.handle(todo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object toggleTodo(final int todoId, final boolean isCompleted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfToggleTodo.acquire();
        int _argIndex = 1;
        final int _tmp = isCompleted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, todoId);
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
          __preparedStmtOfToggleTodo.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTodoById(final int todoId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTodoById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, todoId);
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
          __preparedStmtOfDeleteTodoById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllByBranch(final int branchId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllByBranch.acquire();
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
          __preparedStmtOfDeleteAllByBranch.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByChapterId(final int chapterId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByChapterId.acquire();
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
          __preparedStmtOfDeleteByChapterId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TodoWithDetails>> getAllTodosByBranch(final int branchId,
      final boolean isRevisionMode) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            todos.*,\n"
            + "            chapters.id as chapter_id,\n"
            + "            chapters.subjectId as chapter_subjectId,\n"
            + "            chapters.name as chapter_name,\n"
            + "            chapters.orderIndex as chapter_orderIndex,\n"
            + "            chapters.isCompleted as chapter_isCompleted,\n"
            + "            chapters.completedDate as chapter_completedDate,\n"
            + "            chapters.category as chapter_category,\n"
            + "            chapters.isRevised as chapter_isRevised,\n"
            + "            chapters.revisedDate as chapter_revisedDate,\n"
            + "            chapters.revisionCount as chapter_revisionCount,\n"
            + "            subjects.id as subject_id,\n"
            + "            subjects.branchId as subject_branchId,\n"
            + "            subjects.name as subject_name,\n"
            + "            subjects.totalChapters as subject_totalChapters,\n"
            + "            subjects.completedChapters as subject_completedChapters,\n"
            + "            subjects.revisedChapters as subject_revisedChapters\n"
            + "        FROM todos\n"
            + "        INNER JOIN chapters ON todos.chapterId = chapters.id\n"
            + "        INNER JOIN subjects ON chapters.subjectId = subjects.id\n"
            + "        WHERE todos.branchId = ? AND todos.isRevisionMode = ?\n"
            + "        ORDER BY todos.createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    _argIndex = 2;
    final int _tmp = isRevisionMode ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"todos", "chapters",
        "subjects"}, new Callable<List<TodoWithDetails>>() {
      @Override
      @NonNull
      public List<TodoWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
            final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
            final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
            final int _cursorIndexOfIsRevisionMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevisionMode");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_id");
            final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_subjectId");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_name");
            final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_orderIndex");
            final int _cursorIndexOfIsCompleted_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_isCompleted");
            final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_completedDate");
            final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_category");
            final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_isRevised");
            final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_revisedDate");
            final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_revisionCount");
            final int _cursorIndexOfId_2 = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_id");
            final int _cursorIndexOfBranchId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_branchId");
            final int _cursorIndexOfName_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_name");
            final int _cursorIndexOfTotalChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_totalChapters");
            final int _cursorIndexOfCompletedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_completedChapters");
            final int _cursorIndexOfRevisedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_revisedChapters");
            final List<TodoWithDetails> _result = new ArrayList<TodoWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TodoWithDetails _item;
              final TodoEntity _tmpTodo;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpChapterId;
              _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
              final int _tmpBranchId;
              _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
              final boolean _tmpIsCompleted;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsCompleted);
              _tmpIsCompleted = _tmp_1 != 0;
              final boolean _tmpIsRevisionMode;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsRevisionMode);
              _tmpIsRevisionMode = _tmp_2 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              _tmpTodo = new TodoEntity(_tmpId,_tmpChapterId,_tmpBranchId,_tmpIsCompleted,_tmpIsRevisionMode,_tmpCreatedAt);
              final ChapterEntity _tmpChapter;
              final int _tmpId_1;
              _tmpId_1 = _cursor.getInt(_cursorIndexOfId_1);
              final int _tmpSubjectId;
              _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final int _tmpOrderIndex;
              _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
              final boolean _tmpIsCompleted_1;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfIsCompleted_1);
              _tmpIsCompleted_1 = _tmp_3 != 0;
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
              final int _tmp_4;
              _tmp_4 = _cursor.getInt(_cursorIndexOfIsRevised);
              _tmpIsRevised = _tmp_4 != 0;
              final Long _tmpRevisedDate;
              if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
                _tmpRevisedDate = null;
              } else {
                _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
              }
              final int _tmpRevisionCount;
              _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
              _tmpChapter = new ChapterEntity(_tmpId_1,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted_1,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
              final SubjectEntity _tmpSubject;
              final int _tmpId_2;
              _tmpId_2 = _cursor.getInt(_cursorIndexOfId_2);
              final int _tmpBranchId_1;
              _tmpBranchId_1 = _cursor.getInt(_cursorIndexOfBranchId_1);
              final String _tmpName_1;
              _tmpName_1 = _cursor.getString(_cursorIndexOfName_1);
              final int _tmpTotalChapters;
              _tmpTotalChapters = _cursor.getInt(_cursorIndexOfTotalChapters);
              final int _tmpCompletedChapters;
              _tmpCompletedChapters = _cursor.getInt(_cursorIndexOfCompletedChapters);
              final int _tmpRevisedChapters;
              _tmpRevisedChapters = _cursor.getInt(_cursorIndexOfRevisedChapters);
              _tmpSubject = new SubjectEntity(_tmpId_2,_tmpBranchId_1,_tmpName_1,_tmpTotalChapters,_tmpCompletedChapters,_tmpRevisedChapters);
              _item = new TodoWithDetails(_tmpTodo,_tmpChapter,_tmpSubject);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TodoWithDetails>> getPendingTodos(final int branchId,
      final boolean isRevisionMode) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            todos.*,\n"
            + "            chapters.id as chapter_id,\n"
            + "            chapters.subjectId as chapter_subjectId,\n"
            + "            chapters.name as chapter_name,\n"
            + "            chapters.orderIndex as chapter_orderIndex,\n"
            + "            chapters.isCompleted as chapter_isCompleted,\n"
            + "            chapters.completedDate as chapter_completedDate,\n"
            + "            chapters.category as chapter_category,\n"
            + "            chapters.isRevised as chapter_isRevised,\n"
            + "            chapters.revisedDate as chapter_revisedDate,\n"
            + "            chapters.revisionCount as chapter_revisionCount,\n"
            + "            subjects.id as subject_id,\n"
            + "            subjects.branchId as subject_branchId,\n"
            + "            subjects.name as subject_name,\n"
            + "            subjects.totalChapters as subject_totalChapters,\n"
            + "            subjects.completedChapters as subject_completedChapters,\n"
            + "            subjects.revisedChapters as subject_revisedChapters\n"
            + "        FROM todos\n"
            + "        INNER JOIN chapters ON todos.chapterId = chapters.id\n"
            + "        INNER JOIN subjects ON chapters.subjectId = subjects.id\n"
            + "        WHERE todos.branchId = ? AND todos.isCompleted = 0 AND todos.isRevisionMode = ?\n"
            + "        ORDER BY todos.createdAt ASC\n"
            + "        LIMIT 3\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    _argIndex = 2;
    final int _tmp = isRevisionMode ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"todos", "chapters",
        "subjects"}, new Callable<List<TodoWithDetails>>() {
      @Override
      @NonNull
      public List<TodoWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
            final int _cursorIndexOfBranchId = CursorUtil.getColumnIndexOrThrow(_cursor, "branchId");
            final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
            final int _cursorIndexOfIsRevisionMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isRevisionMode");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_id");
            final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_subjectId");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_name");
            final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_orderIndex");
            final int _cursorIndexOfIsCompleted_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_isCompleted");
            final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_completedDate");
            final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_category");
            final int _cursorIndexOfIsRevised = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_isRevised");
            final int _cursorIndexOfRevisedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_revisedDate");
            final int _cursorIndexOfRevisionCount = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter_revisionCount");
            final int _cursorIndexOfId_2 = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_id");
            final int _cursorIndexOfBranchId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_branchId");
            final int _cursorIndexOfName_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_name");
            final int _cursorIndexOfTotalChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_totalChapters");
            final int _cursorIndexOfCompletedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_completedChapters");
            final int _cursorIndexOfRevisedChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "subject_revisedChapters");
            final List<TodoWithDetails> _result = new ArrayList<TodoWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TodoWithDetails _item;
              final TodoEntity _tmpTodo;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpChapterId;
              _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
              final int _tmpBranchId;
              _tmpBranchId = _cursor.getInt(_cursorIndexOfBranchId);
              final boolean _tmpIsCompleted;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsCompleted);
              _tmpIsCompleted = _tmp_1 != 0;
              final boolean _tmpIsRevisionMode;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsRevisionMode);
              _tmpIsRevisionMode = _tmp_2 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              _tmpTodo = new TodoEntity(_tmpId,_tmpChapterId,_tmpBranchId,_tmpIsCompleted,_tmpIsRevisionMode,_tmpCreatedAt);
              final ChapterEntity _tmpChapter;
              final int _tmpId_1;
              _tmpId_1 = _cursor.getInt(_cursorIndexOfId_1);
              final int _tmpSubjectId;
              _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final int _tmpOrderIndex;
              _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
              final boolean _tmpIsCompleted_1;
              final int _tmp_3;
              _tmp_3 = _cursor.getInt(_cursorIndexOfIsCompleted_1);
              _tmpIsCompleted_1 = _tmp_3 != 0;
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
              final int _tmp_4;
              _tmp_4 = _cursor.getInt(_cursorIndexOfIsRevised);
              _tmpIsRevised = _tmp_4 != 0;
              final Long _tmpRevisedDate;
              if (_cursor.isNull(_cursorIndexOfRevisedDate)) {
                _tmpRevisedDate = null;
              } else {
                _tmpRevisedDate = _cursor.getLong(_cursorIndexOfRevisedDate);
              }
              final int _tmpRevisionCount;
              _tmpRevisionCount = _cursor.getInt(_cursorIndexOfRevisionCount);
              _tmpChapter = new ChapterEntity(_tmpId_1,_tmpSubjectId,_tmpName,_tmpOrderIndex,_tmpIsCompleted_1,_tmpCompletedDate,_tmpCategory,_tmpIsRevised,_tmpRevisedDate,_tmpRevisionCount);
              final SubjectEntity _tmpSubject;
              final int _tmpId_2;
              _tmpId_2 = _cursor.getInt(_cursorIndexOfId_2);
              final int _tmpBranchId_1;
              _tmpBranchId_1 = _cursor.getInt(_cursorIndexOfBranchId_1);
              final String _tmpName_1;
              _tmpName_1 = _cursor.getString(_cursorIndexOfName_1);
              final int _tmpTotalChapters;
              _tmpTotalChapters = _cursor.getInt(_cursorIndexOfTotalChapters);
              final int _tmpCompletedChapters;
              _tmpCompletedChapters = _cursor.getInt(_cursorIndexOfCompletedChapters);
              final int _tmpRevisedChapters;
              _tmpRevisedChapters = _cursor.getInt(_cursorIndexOfRevisedChapters);
              _tmpSubject = new SubjectEntity(_tmpId_2,_tmpBranchId_1,_tmpName_1,_tmpTotalChapters,_tmpCompletedChapters,_tmpRevisedChapters);
              _item = new TodoWithDetails(_tmpTodo,_tmpChapter,_tmpSubject);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getPendingCount(final int branchId, final boolean isRevisionMode) {
    final String _sql = "SELECT COUNT(*) FROM todos WHERE branchId = ? AND isCompleted = 0 AND isRevisionMode = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, branchId);
    _argIndex = 2;
    final int _tmp = isRevisionMode ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"todos"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(0);
            _result = _tmp_1;
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
  public Object isChapterInTodo(final int chapterId, final int branchId,
      final boolean isRevisionMode, final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM todos WHERE chapterId = ? AND branchId = ? AND isRevisionMode = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, branchId);
    _argIndex = 3;
    final int _tmp = isRevisionMode ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(0);
            _result = _tmp_1 != 0;
          } else {
            _result = false;
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
