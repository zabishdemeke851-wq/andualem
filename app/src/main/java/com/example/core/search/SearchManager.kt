package com.example.core.search

import com.example.core.search.adapters.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SearchManager {

    private val adapters: List<BaseSourceAdapter> = listOf(
        GoogleBooksAdapter,
        OpenAlexAdapter,
        CrossrefAdapter,
        InternetArchiveAdapter,
        DoajAdapter,
        AauEtdAdapter,
        NadreAdapter,
        EjolAdapter,
        EthernetLibraryAdapter,
        InstitutionalRepositoryAdapter
    )

    private fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        return sdf.format(Date())
    }

    suspend fun checkAllStatuses(): List<ProviderStatus> = coroutineScope {
        val currentDate = getCurrentDateString()
        adapters.map { adapter ->
            async {
                adapter.checkStatus(currentDate)
            }
        }.map { it.await() }
    }

    suspend fun executeSearch(query: String): SearchResponse = coroutineScope {
        val currentDate = getCurrentDateString()
        val trimmedQuery = query.trim()

        if (trimmedQuery.isBlank()) {
            return@coroutineScope SearchResponse(
                query = "",
                search_date = currentDate,
                providers_searched = emptyList(),
                providers_unavailable = adapters.map { it.providerName },
                total_results = 0,
                verified_results = 0
            )
        }

        val providersSearched = mutableListOf<String>()
        val providersUnavailable = mutableListOf<String>()
        val allRawResults = mutableListOf<SearchResultItem>()

        // Concurrently query active adapters
        val deferreds = adapters.map { adapter ->
            async {
                val status = adapter.checkStatus(currentDate)
                if (status.connection_status == ConnectionStatus.CONNECTED || status.connection_status == ConnectionStatus.AVAILABLE_WITHOUT_API_KEY) {
                    providersSearched.add(adapter.providerName)
                    try {
                        adapter.search(trimmedQuery, currentDate)
                    } catch (e: Exception) {
                        providersUnavailable.add(adapter.providerName)
                        emptyList()
                    }
                } else {
                    providersUnavailable.add(adapter.providerName)
                    emptyList()
                }
            }
        }

        deferreds.forEach { deferred ->
            allRawResults.addAll(deferred.await())
        }

        // Deduplicate and Validate
        val validatedAndDeduplicated = mutableListOf<SearchResultItem>()
        val seenKeys = mutableSetOf<String>()

        for (item in allRawResults) {
            // Must have a valid URL
            val primaryUrl = when {
                UrlValidator.isValidUrl(item.full_text_url) -> item.full_text_url
                UrlValidator.isValidUrl(item.source_url) -> item.source_url
                UrlValidator.isValidUrl(item.record_url) -> item.record_url
                else -> ""
            }

            if (primaryUrl.isBlank()) continue

            // Deduplication key
            val titleNorm = item.title.lowercase().replace(Regex("[^a-z0-9]"), "")
            val doiNorm = item.doi.lowercase().trim()
            val dedupeKey = if (doiNorm.isNotBlank()) doiNorm else titleNorm

            if (dedupeKey.isNotBlank() && !seenKeys.contains(dedupeKey)) {
                seenKeys.add(dedupeKey)
                validatedAndDeduplicated.add(
                    item.copy(
                        verification = if (UrlValidator.isValidUrl(primaryUrl)) "URL verified" else "URL not verified"
                    )
                )
            }
        }

        // Categorize into Books, Research, Theses, Other
        val books = mutableListOf<SearchResultItem>()
        val research = mutableListOf<SearchResultItem>()
        val theses = mutableListOf<SearchResultItem>()
        val other = mutableListOf<SearchResultItem>()

        for (item in validatedAndDeduplicated) {
            when (item.document_type.lowercase()) {
                "book" -> books.add(item)
                "thesis / dissertation", "thesis", "dissertation" -> theses.add(item)
                "research paper", "journal article" -> research.add(item)
                else -> other.add(item)
            }
        }

        val bestMatch = validatedAndDeduplicated.firstOrNull()

        val limitations = mutableListOf<String>()
        if (providersUnavailable.isNotEmpty()) {
            limitations.add("The following Ethiopian digital repositories require direct portal access and offer manual search only: AAU ETD, NADRE, EJOL, NDL, Jimma & Hawassa Repositories.")
        }

        SearchResponse(
            query = trimmedQuery,
            search_date = currentDate,
            providers_searched = providersSearched.distinct(),
            providers_unavailable = providersUnavailable.distinct(),
            total_results = validatedAndDeduplicated.size,
            verified_results = validatedAndDeduplicated.count { it.verification == "URL verified" },
            best_match = bestMatch,
            books = books,
            research = research,
            theses = theses,
            other_sources = other,
            limitations = limitations
        )
    }
}
