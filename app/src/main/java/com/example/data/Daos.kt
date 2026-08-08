package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY isFeatured DESC, title ASC")
    fun getAllDocuments(): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE id = :id")
    suspend fun getDocumentById(id: Long): DocumentEntity?

    @Query("SELECT * FROM documents WHERE category = :category ORDER BY title ASC")
    fun getDocumentsByCategory(category: String): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE title LIKE '%' || :query || '%' OR titleAmharic LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%'")
    fun searchDocuments(query: String): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE isFeatured = 1")
    fun getFeaturedDocuments(): Flow<List<DocumentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(documents: List<DocumentEntity>)

    @Query("DELETE FROM documents")
    suspend fun clearAll()
}

@Dao
interface UserDataDao {
    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE documentId = :docId)")
    fun isFavorite(docId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE documentId = :docId")
    suspend fun removeFavorite(docId: Long)

    @Query("SELECT * FROM bookmarks WHERE documentId = :docId ORDER BY pageNumber ASC")
    fun getBookmarksForDocument(docId: Long): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :bookmarkId")
    suspend fun deleteBookmark(bookmarkId: Long)

    @Query("SELECT * FROM reading_progress WHERE documentId = :docId")
    suspend fun getReadingProgress(docId: Long): ReadingProgressEntity?

    @Query("SELECT * FROM reading_progress ORDER BY lastReadAt DESC")
    fun getAllReadingProgress(): Flow<List<ReadingProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReadingProgress(progress: ReadingProgressEntity)

    @Query("SELECT * FROM user_notes WHERE documentId = :docId ORDER BY pageNumber ASC")
    fun getNotesForDocument(docId: Long): Flow<List<UserNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: UserNoteEntity)

    @Query("DELETE FROM user_notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Long)
}

@Dao
interface SaintDao {
    @Query("SELECT * FROM saints WHERE ethiopianMonth = :month AND ethiopianDay = :day")
    fun getSaintsForDate(month: Int, day: Int): Flow<List<SaintEntity>>

    @Query("SELECT * FROM saints WHERE ethiopianMonth = :month ORDER BY ethiopianDay ASC")
    fun getSaintsForMonth(month: Int): Flow<List<SaintEntity>>

    @Query("SELECT * FROM saints WHERE nameEn LIKE '%' || :query || '%' OR nameAm LIKE '%' || :query || '%'")
    fun searchSaints(query: String): Flow<List<SaintEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaints(saints: List<SaintEntity>)
}

@Dao
interface FeastDao {
    @Query("SELECT * FROM feasts ORDER BY ethiopianMonth ASC, ethiopianDay ASC")
    fun getAllFeasts(): Flow<List<FeastEntity>>

    @Query("SELECT * FROM feasts WHERE ethiopianMonth = :month AND ethiopianDay = :day")
    fun getFeastsForDate(month: Int, day: Int): Flow<List<FeastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeasts(feasts: List<FeastEntity>)
}
