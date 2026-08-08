package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val titleAmharic: String,
    val author: String,
    val category: String, // Orthodox Books, History, Science & Astronomy, Ge'ez Language, University Research
    val language: String, // Amharic, Ge'ez, English
    val year: Int,
    val pageCount: Int,
    val description: String,
    val content: String,
    val tags: String,
    val isFeatured: Boolean = false,
    val coverColorHex: String = "#800020",
    val sourceUrl: String = "",
    val downloadUrl: String = ""
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val documentId: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val documentId: Long,
    val pageNumber: Int,
    val note: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
    @PrimaryKey val documentId: Long,
    val currentPage: Int,
    val totalPages: Int,
    val lastReadAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_notes")
data class UserNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val documentId: Long,
    val pageNumber: Int,
    val noteText: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saints")
data class SaintEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ethiopianMonth: Int,
    val ethiopianDay: Int,
    val nameEn: String,
    val nameAm: String,
    val geezName: String,
    val description: String,
    val biography: String
)

@Entity(tableName = "feasts")
data class FeastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameEn: String,
    val nameAm: String,
    val ethiopianMonth: Int,
    val ethiopianDay: Int,
    val isMovable: Boolean,
    val description: String
)
