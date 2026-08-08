package com.example.data

import kotlinx.coroutines.flow.Flow

class AndualemRepository(
    private val documentDao: DocumentDao,
    private val userDataDao: UserDataDao,
    private val saintDao: SaintDao,
    private val feastDao: FeastDao
) {
    val allDocuments: Flow<List<DocumentEntity>> = documentDao.getAllDocuments()
    val featuredDocuments: Flow<List<DocumentEntity>> = documentDao.getFeaturedDocuments()
    val allFavorites: Flow<List<FavoriteEntity>> = userDataDao.getFavorites()
    val allReadingProgress: Flow<List<ReadingProgressEntity>> = userDataDao.getAllReadingProgress()
    val allFeasts: Flow<List<FeastEntity>> = feastDao.getAllFeasts()

    fun getDocumentsByCategory(category: String): Flow<List<DocumentEntity>> =
        documentDao.getDocumentsByCategory(category)

    fun searchDocuments(query: String): Flow<List<DocumentEntity>> =
        documentDao.searchDocuments(query)

    suspend fun getDocumentById(id: Long): DocumentEntity? =
        documentDao.getDocumentById(id)

    fun isFavorite(docId: Long): Flow<Boolean> = userDataDao.isFavorite(docId)

    suspend fun toggleFavorite(docId: Long, isFav: Boolean) {
        if (isFav) {
            userDataDao.removeFavorite(docId)
        } else {
            userDataDao.addFavorite(FavoriteEntity(documentId = docId))
        }
    }

    fun getBookmarks(docId: Long): Flow<List<BookmarkEntity>> =
        userDataDao.getBookmarksForDocument(docId)

    suspend fun addBookmark(bookmark: BookmarkEntity) =
        userDataDao.addBookmark(bookmark)

    suspend fun deleteBookmark(id: Long) =
        userDataDao.deleteBookmark(id)

    fun getNotes(docId: Long): Flow<List<UserNoteEntity>> =
        userDataDao.getNotesForDocument(docId)

    suspend fun addNote(note: UserNoteEntity) =
        userDataDao.addNote(note)

    suspend fun deleteNote(id: Long) =
        userDataDao.deleteNote(id)

    suspend fun getReadingProgress(docId: Long) =
        userDataDao.getReadingProgress(docId)

    suspend fun saveReadingProgress(progress: ReadingProgressEntity) =
        userDataDao.saveReadingProgress(progress)

    fun getSaintsForDate(month: Int, day: Int): Flow<List<SaintEntity>> =
        saintDao.getSaintsForDate(month, day)

    fun getSaintsForMonth(month: Int): Flow<List<SaintEntity>> =
        saintDao.getSaintsForMonth(month)

    fun searchSaints(query: String): Flow<List<SaintEntity>> =
        saintDao.searchSaints(query)
}
