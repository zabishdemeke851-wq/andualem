package com.example.core.search

import androidx.annotation.Keep

@Keep
data class SearchResultItem(
    val title: String,
    val authors: List<String> = emptyList(),
    val document_type: String = "Research Paper", // "Book", "Research Paper", "Thesis / Dissertation", "Journal Article", "OER Archive"
    val publication_year: Int? = null,
    val publisher: String = "",
    val journal: String = "",
    val university: String = "",
    val isbn10: String = "",
    val isbn13: String = "",
    val doi: String = "",
    val abstract: String = "",
    val language: String = "en",
    val source_name: String = "",
    val source_url: String = "",
    val record_url: String = "",
    val publisher_url: String = "",
    val repository_url: String = "",
    val preview_url: String = "",
    val full_text_url: String = "",
    val download_url: String = "",
    val access_status: String = "Metadata only — no verified full text found.",
    val verification: String = "URL verified",
    val confidence: String = "High",
    val date_checked: String = ""
)

@Keep
data class SearchResponse(
    val query: String,
    val search_date: String,
    val providers_searched: List<String>,
    val providers_unavailable: List<String>,
    val total_results: Int,
    val verified_results: Int,
    val best_match: SearchResultItem? = null,
    val books: List<SearchResultItem> = emptyList(),
    val research: List<SearchResultItem> = emptyList(),
    val theses: List<SearchResultItem> = emptyList(),
    val other_sources: List<SearchResultItem> = emptyList(),
    val limitations: List<String> = emptyList()
)

enum class ConnectionStatus {
    CONNECTED,
    AVAILABLE_WITHOUT_API_KEY,
    API_KEY_REQUIRED,
    MANUAL_SEARCH_ONLY,
    ERROR,
    NOT_CONFIGURED
}

@Keep
data class ProviderStatus(
    val provider_name: String,
    val provider_homepage: String,
    val connection_status: ConnectionStatus,
    val api_status_message: String,
    val last_checked_time: String,
    val results_returned_count: Int = 0,
    val error_message: String? = null,
    val required_env_variable: String = "",
    val search_instructions: String = ""
)
