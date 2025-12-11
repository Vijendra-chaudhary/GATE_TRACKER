package com.gate.tracker.ui.subject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.data.local.entity.ChapterNoteEntity
import com.gate.tracker.data.local.entity.SubjectEntity
import com.gate.tracker.data.repository.GateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class ChapterFilter {
    ALL,
    IMPORTANT,
    NEEDS_REVISION,
    WITH_NOTES,
    COMPLETED,
    PENDING
}

class SubjectDetailViewModel(
    private val repository: GateRepository,
    private val context: android.content.Context
) : ViewModel() {
    
    private val _subject = MutableStateFlow<SubjectEntity?>(null)
    val subject: StateFlow<SubjectEntity?> = _subject.asStateFlow()
    
    private val _chapters = MutableStateFlow<List<ChapterEntity>>(emptyList())
    val chapters: StateFlow<List<ChapterEntity>> = _chapters.asStateFlow()
    
    private val _chapterNotes = MutableStateFlow<Map<Int, ChapterNoteEntity>>(emptyMap())
    val chapterNotes: StateFlow<Map<Int, ChapterNoteEntity>> = _chapterNotes.asStateFlow()
    
    private val _currentFilter = MutableStateFlow(ChapterFilter.ALL)
    val currentFilter: StateFlow<ChapterFilter> = _currentFilter.asStateFlow()
    
    // Track current revision mode
    private val _isRevisionMode = MutableStateFlow(false)
    val isRevisionMode: StateFlow<Boolean> = _isRevisionMode.asStateFlow()
    
    // Track which chapters are in to-do for current mode
    private val _todoChapterIds = MutableStateFlow<Set<Int>>(emptySet())
    val todoChapterIds: StateFlow<Set<Int>> = _todoChapterIds.asStateFlow()
    
    fun loadSubject(subjectId: Int) {
        viewModelScope.launch {
            //Load subject details
            _subject.value = repository.getSubjectById(subjectId)
            
            // Load revision mode status
            _isRevisionMode.value = repository.isRevisionMode().first()
            
            // Load to-do chapter IDs for current mode
            _subject.value?.let { subject ->
                repository.getTodoChapterIds(subject.branchId, _isRevisionMode.value).collect { todoIds ->
                    _todoChapterIds.value = todoIds
                }
            }
        }
        
        // Load chapters in separate coroutine
        viewModelScope.launch {
            repository.getChaptersBySubject(subjectId).collect { chapterList ->
                _chapters.value = chapterList
            }
        }
        
        // Load notes for all chapters in this subject in separate coroutine
        viewModelScope.launch {
            repository.getNotesForSubject(subjectId).collect { notesList ->
                _chapterNotes.value = notesList.associateBy { it.chapterId }
            }
        }
    }
    
    fun toggleChapter(chapter: ChapterEntity) {
        viewModelScope.launch {
            // Check if in revision mode (get single value, don't collect forever)
            val isRevisionMode = repository.isRevisionMode().first()
            
            if (isRevisionMode) {
                // Revision mode: toggle isRevised field
                repository.toggleChapterRevision(chapter.id, !chapter.isRevised)
                
                // Update subject revised count
                _subject.value?.let { subject ->
                    repository.updateSubjectRevisedCount(subject.id)
                    // Reload the subject
                    _subject.value = repository.getSubjectById(subject.id)
                }
            } else {
                // Normal mode: toggle isCompleted field  
                repository.toggleChapterStatus(chapter.id, chapter.isCompleted)
                
                // Log activity when chapter is completed
                if (!chapter.isCompleted) {
                    repository.logActivity(
                        com.gate.tracker.data.local.entity.ActivityType.CHAPTER_COMPLETE,
                        studyDurationHours = 2.5
                    )
                }
                
                // Update subject completed count
                _subject.value?.let { subject ->
                    repository.updateSubjectCompletedCount(subject.id)
                    _subject.value = repository.getSubjectById(subject.id)
                }
            }
            
            // Trigger auto-backup after ANY chapter status change
            _subject.value?.let { subject ->
                android.util.Log.d("GATE_TRACKER", "Chapter toggled, scheduling auto-backup for branch ${subject.branchId}")
                com.gate.tracker.data.backup.AutoBackupWorker.scheduleBackup(
                    context = context,
                    branchId = subject.branchId,
                    branchName = getBranchName(subject.branchId)
                )
            }
        }
    }
    
    fun saveNote(
        chapterId: Int,
        noteText: String,
        isImportant: Boolean,
        needsRevision: Boolean
    ) {
        viewModelScope.launch {
            val existingNote = _chapterNotes.value[chapterId]
            
            // Delete the note only if text is blank AND no flags are set
            if (noteText.isBlank() && !isImportant && !needsRevision) {
                existingNote?.let {
                    repository.deleteChapterNote(it.id)
                }
            } else {
                // Save if there's text OR if any flags are set
                repository.saveChapterNote(
                    chapterId = chapterId,
                    noteText = noteText,
                    isImportant = isImportant,
                    needsRevision = needsRevision,
                    noteId = existingNote?.id
                )
            }
        }
    }
    
    fun deleteNote(chapterId: Int) {
        viewModelScope.launch {
            val note = _chapterNotes.value[chapterId]
            note?.let {
                repository.deleteChapterNote(it.id)
            }
        }
    }
    
    fun setFilter(filter: ChapterFilter) {
        _currentFilter.value = filter
    }
    
    fun toggleChapterInTodo(chapterId: Int, onLimitReached: () -> Unit = {}) {
        viewModelScope.launch {
            _subject.value?.let { subject ->
                val isInTodo = _todoChapterIds.value.contains(chapterId)
                if (isInTodo) {
                    // Remove from to-do - find the todo ID and delete it
                    val todos = repository.getTodosByBranch(subject.branchId, _isRevisionMode.value).first()
                    val todoToRemove = todos.find { it.todo.chapterId == chapterId }
                    todoToRemove?.let {
                        repository.deleteTodo(it.todo.id)
                    }
                } else {
                    // Check pending count before adding (for current mode)
                    val allTodos = repository.getPendingTodos(subject.branchId, _isRevisionMode.value).first()
                    if (allTodos.size >= 3) {
                        // Limit reached, notify user
                        onLimitReached()
                    } else {
                        // Add to to-do with current mode
                        repository.addTodo(chapterId, subject.branchId, _isRevisionMode.value)
                    }
                }
            }
        }
    }
    
    private fun getBranchName(branchId: Int): String {
        return when (branchId) {
            1 -> "CS"
            2 -> "EC"
            3 -> "EE"
            4 -> "ME"
            5 -> "CE"
            6 -> "DA"
            else -> "CS"
        }
    }
}
