package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.abushakir.BahreHasab
import com.example.core.abushakir.BahreHasabResult
import com.example.core.ai.AiResponse
import com.example.core.ai.GeminiAiService
import com.example.core.calendar.EthiopianCalendar
import com.example.core.calendar.EthiopianDate
import com.example.core.search.ProviderStatus
import com.example.core.search.SearchManager
import com.example.core.search.SearchResponse
import com.example.core.time.EthiopianTime
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
    DASHBOARD,
    CALENDAR,
    CONVERTER,
    ABUSHAKIR,
    TIME,
    LIBRARY,
    READER,
    AI_RESEARCH,
    SAINTS_FEASTS,
    FAVORITES,
    SETTINGS
}

data class AiChatMessage(
    val sender: String, // "USER" or "ANDUALEM_AI"
    val text: String,
    val citations: List<com.example.core.ai.AiSourceCitation> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = AndualemRepository(
        db.documentDao(),
        db.userDataDao(),
        db.saintDao(),
        db.feastDao()
    )

    // UI Navigation State
    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Language Preference (true = Amharic, false = English)
    private val _isAmharic = MutableStateFlow(true)
    val isAmharic: StateFlow<Boolean> = _isAmharic.asStateFlow()

    // Calendar & Today State
    private val _todayEthiopian = MutableStateFlow(EthiopianCalendar.getTodayEthiopian())
    val todayEthiopian: StateFlow<EthiopianDate> = _todayEthiopian.asStateFlow()

    private val _selectedCalendarMonth = MutableStateFlow(_todayEthiopian.value.month)
    val selectedCalendarMonth: StateFlow<Int> = _selectedCalendarMonth.asStateFlow()

    private val _selectedCalendarYear = MutableStateFlow(_todayEthiopian.value.year)
    val selectedCalendarYear: StateFlow<Int> = _selectedCalendarYear.asStateFlow()

    // Converter State
    val converterGregorianYear = MutableStateFlow(2026)
    val converterGregorianMonth = MutableStateFlow(9)
    val converterGregorianDay = MutableStateFlow(11)

    val converterEthYear = MutableStateFlow(2019)
    val converterEthMonth = MutableStateFlow(1)
    val converterEthDay = MutableStateFlow(1)

    // Abushakir Bahre Hasab State
    private val _bahreHasabYear = MutableStateFlow(_todayEthiopian.value.year)
    val bahreHasabYear: StateFlow<Int> = _bahreHasabYear.asStateFlow()

    private val _bahreHasabResult = MutableStateFlow(BahreHasab.calculate(_bahreHasabYear.value))
    val bahreHasabResult: StateFlow<BahreHasabResult> = _bahreHasabResult.asStateFlow()

    // Real-time Ethiopian Clock
    val ethiopianClockTime = MutableStateFlow(EthiopianTime.getCurrentEthiopianClockTime())

    // Real Search & Provider Integration State
    private val _realSearchResults = MutableStateFlow<SearchResponse?>(null)
    val realSearchResults: StateFlow<SearchResponse?> = _realSearchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _providerStatuses = MutableStateFlow<List<ProviderStatus>>(emptyList())
    val providerStatuses: StateFlow<List<ProviderStatus>> = _providerStatuses.asStateFlow()

    // Library Filter & Search State
    val librarySearchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("ALL")

    val documentsList: StateFlow<List<DocumentEntity>> = librarySearchQuery
        .combine(selectedCategory) { query, category -> Pair(query, category) }
        .flatMapLatest { (query, category) ->
            if (query.isNotBlank()) {
                repository.searchDocuments(query)
            } else if (category != "ALL") {
                repository.getDocumentsByCategory(category)
            } else {
                repository.allDocuments
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredDocuments: StateFlow<List<DocumentEntity>> = repository.featuredDocuments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Document for Reader
    private val _selectedDocument = MutableStateFlow<DocumentEntity?>(null)
    val selectedDocument: StateFlow<DocumentEntity?> = _selectedDocument.asStateFlow()

    val currentDocumentBookmarks: StateFlow<List<BookmarkEntity>> = _selectedDocument
        .flatMapLatest { doc ->
            if (doc != null) repository.getBookmarks(doc.id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentDocumentNotes: StateFlow<List<UserNoteEntity>> = _selectedDocument
        .flatMapLatest { doc ->
            if (doc != null) repository.getNotes(doc.id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isCurrentDocFavorite: StateFlow<Boolean> = _selectedDocument
        .flatMapLatest { doc ->
            if (doc != null) repository.isFavorite(doc.id) else flowOf(false)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // AI Research Chat State
    private val _aiChatMessages = MutableStateFlow<List<AiChatMessage>>(
        listOf(
            AiChatMessage(
                sender = "ANDUALEM_AI",
                text = "በሰላም መጡ! I am Andualem AI, your Ethiopian Knowledge & Research Assistant. Ask me anything about the Ethiopian calendar, Bahre Hasab, Ge'ez literature, Orthodox feasts, or academic research."
            )
        )
    )
    val aiChatMessages: StateFlow<List<AiChatMessage>> = _aiChatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    init {
        refreshProviderStatuses()
        // Run ticker for live clock
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(1000)
                ethiopianClockTime.value = EthiopianTime.getCurrentEthiopianClockTime()
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun toggleLanguage() {
        _isAmharic.value = !_isAmharic.value
    }

    fun selectDocumentForReader(doc: DocumentEntity) {
        _selectedDocument.value = doc
        _currentScreen.value = AppScreen.READER
    }

    fun setBahreHasabYear(year: Int) {
        _bahreHasabYear.value = year
        _bahreHasabResult.value = BahreHasab.calculate(year)
    }

    fun setCalendarMonthYear(month: Int, year: Int) {
        var m = month
        var y = year
        if (m > 13) {
            m = 1
            y += 1
        } else if (m < 1) {
            m = 13
            y -= 1
        }
        _selectedCalendarMonth.value = m
        _selectedCalendarYear.value = y
    }

    fun toggleFavorite(docId: Long, currentIsFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(docId, currentIsFav)
        }
    }

    fun addBookmark(docId: Long, pageNum: Int, note: String) {
        viewModelScope.launch {
            repository.addBookmark(BookmarkEntity(documentId = docId, pageNumber = pageNum, note = note))
        }
    }

    fun addNote(docId: Long, pageNum: Int, noteText: String) {
        viewModelScope.launch {
            repository.addNote(UserNoteEntity(documentId = docId, pageNumber = pageNum, noteText = noteText))
        }
    }

    fun sendAiQuestion(userQuery: String, docContext: String? = null, docTitle: String? = null) {
        if (userQuery.isBlank()) return

        val userMsg = AiChatMessage(sender = "USER", text = userQuery)
        _aiChatMessages.update { it + userMsg }
        _isAiThinking.value = true

        viewModelScope.launch {
            val response = GeminiAiService.askAi(
                question = userQuery,
                documentContext = docContext,
                documentTitle = docTitle
            )

            val aiMsg = AiChatMessage(
                sender = "ANDUALEM_AI",
                text = response.answer,
                citations = response.citations
            )
            _aiChatMessages.update { it + aiMsg }
            _isAiThinking.value = false
        }
    }

    fun refreshProviderStatuses() {
        viewModelScope.launch {
            _providerStatuses.value = SearchManager.checkAllStatuses()
        }
    }

    fun performRealSearch(query: String) {
        if (query.isBlank()) return
        _isSearching.value = true
        viewModelScope.launch {
            try {
                val response = SearchManager.executeSearch(query)
                _realSearchResults.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isSearching.value = false
            }
        }
    }
}
