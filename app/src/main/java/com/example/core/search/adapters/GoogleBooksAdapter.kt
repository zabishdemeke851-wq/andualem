package com.example.core.search.adapters

import com.example.BuildConfig
import com.example.core.search.ConnectionStatus
import com.example.core.search.ProviderStatus
import com.example.core.search.SearchResultItem
import com.example.core.search.UrlValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

object GoogleBooksAdapter : BaseSourceAdapter {
    override val providerName: String = "Google Books"
    override val providerHomepage: String = "https://books.google.com/"
    override val requiredEnvVariable: String = "GOOGLE_BOOKS_API_KEY"

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .build()
    }

    private fun getApiKey(): String {
        return try { BuildConfig.GOOGLE_BOOKS_API_KEY } catch (e: Exception) { "" }
    }

    override suspend fun checkStatus(currentDate: String): ProviderStatus = withContext(Dispatchers.IO) {
        val key = getApiKey()
        val apiKeyProvided = key.isNotBlank() && key != "MY_GOOGLE_BOOKS_API_KEY" && key != "UNSET"
        try {
            val testQuery = URLEncoder.encode("Ethiopia", "UTF-8")
            val url = if (apiKeyProvided) {
                "https://www.googleapis.com/books/v1/volumes?q=$testQuery&maxResults=1&key=$key"
            } else {
                "https://www.googleapis.com/books/v1/volumes?q=$testQuery&maxResults=1"
            }
            val request = Request.Builder().url(url).build()
            httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    ProviderStatus(
                        provider_name = providerName,
                        provider_homepage = providerHomepage,
                        connection_status = if (apiKeyProvided) ConnectionStatus.CONNECTED else ConnectionStatus.AVAILABLE_WITHOUT_API_KEY,
                        api_status_message = if (apiKeyProvided) "Connected with API Key" else "Available without API Key (Rate limited)",
                        last_checked_time = currentDate,
                        results_returned_count = 1,
                        required_env_variable = requiredEnvVariable
                    )
                } else {
                    ProviderStatus(
                        provider_name = providerName,
                        provider_homepage = providerHomepage,
                        connection_status = ConnectionStatus.ERROR,
                        api_status_message = "HTTP ${response.code}",
                        last_checked_time = currentDate,
                        error_message = response.message,
                        required_env_variable = requiredEnvVariable
                    )
                }
            }
        } catch (e: Exception) {
            ProviderStatus(
                provider_name = providerName,
                provider_homepage = providerHomepage,
                connection_status = ConnectionStatus.ERROR,
                api_status_message = "Connection failed",
                last_checked_time = currentDate,
                error_message = e.localizedMessage ?: "Network error",
                required_env_variable = requiredEnvVariable
            )
        }
    }

    override suspend fun search(query: String, currentDate: String): List<SearchResultItem> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        val results = mutableListOf<SearchResultItem>()
        try {
            val key = getApiKey()
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = if (key.isNotBlank() && key != "MY_GOOGLE_BOOKS_API_KEY" && key != "UNSET") {
                "https://www.googleapis.com/books/v1/volumes?q=$encodedQuery&maxResults=15&key=$key"
            } else {
                "https://www.googleapis.com/books/v1/volumes?q=$encodedQuery&maxResults=15"
            }

            val request = Request.Builder().url(url).build()
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val bodyStr = response.body?.string() ?: return@withContext emptyList()
                val root = JSONObject(bodyStr)
                val items = root.optJSONArray("items") ?: return@withContext emptyList()

                for (i in 0 until items.length()) {
                    val item = items.optJSONObject(i) ?: continue
                    val volumeInfo = item.optJSONObject("volumeInfo") ?: continue
                    val accessInfo = item.optJSONObject("accessInfo")

                    val title = volumeInfo.optString("title", "").trim()
                    if (title.isBlank()) continue

                    val authorsList = mutableListOf<String>()
                    val authorsArray = volumeInfo.optJSONArray("authors")
                    if (authorsArray != null) {
                        for (j in 0 until authorsArray.length()) {
                            val a = authorsArray.optString(j, "").trim()
                            if (a.isNotBlank()) authorsList.add(a)
                        }
                    }

                    val publishedDate = volumeInfo.optString("publishedDate", "")
                    val pubYear = publishedDate.take(4).toIntOrNull()

                    val publisher = volumeInfo.optString("publisher", "")
                    val description = volumeInfo.optString("description", "")
                    val language = volumeInfo.optString("language", "en")

                    var isbn10 = ""
                    var isbn13 = ""
                    val identifiers = volumeInfo.optJSONArray("industryIdentifiers")
                    if (identifiers != null) {
                        for (k in 0 until identifiers.length()) {
                            val idObj = identifiers.optJSONObject(k) ?: continue
                            val type = idObj.optString("type", "")
                            val identifier = idObj.optString("identifier", "")
                            if (type == "ISBN_10") isbn10 = identifier
                            if (type == "ISBN_13") isbn13 = identifier
                        }
                    }

                    val infoLink = UrlValidator.sanitizeUrl(volumeInfo.optString("infoLink", ""))
                    val previewLink = UrlValidator.sanitizeUrl(volumeInfo.optString("previewLink", ""))

                    var fullTextUrl = ""
                    var downloadUrl = ""
                    var isFullText = false

                    if (accessInfo != null) {
                        val pdfObj = accessInfo.optJSONObject("pdf")
                        val isPdfAvailable = pdfObj?.optBoolean("isAvailable", false) ?: false
                        val pdfDownloadLink = UrlValidator.sanitizeUrl(pdfObj?.optString("downloadLink", ""))
                        val webReaderLink = UrlValidator.sanitizeUrl(accessInfo.optString("webReaderLink", ""))
                        val accessViewStatus = accessInfo.optString("accessViewStatus", "")

                        if (isPdfAvailable && pdfDownloadLink.isNotBlank()) {
                            fullTextUrl = pdfDownloadLink
                            downloadUrl = pdfDownloadLink
                            isFullText = true
                        } else if (accessViewStatus == "FULL_PUBLIC_DOMAIN" && webReaderLink.isNotBlank()) {
                            fullTextUrl = webReaderLink
                            isFullText = true
                        }
                    }

                    val accessStatus = when {
                        isFullText -> "Verified legal full text."
                        previewLink.isNotBlank() -> "Free preview — not full text."
                        else -> "Metadata only — no verified full text found."
                    }

                    val recordUrl = if (infoLink.isNotBlank()) infoLink else previewLink
                    if (!UrlValidator.isValidUrl(recordUrl)) continue

                    results.add(
                        SearchResultItem(
                            title = title,
                            authors = if (authorsList.isEmpty()) listOf("Google Books Record") else authorsList,
                            document_type = "Book",
                            publication_year = pubYear,
                            publisher = publisher,
                            isbn10 = isbn10,
                            isbn13 = isbn13,
                            abstract = description.take(300),
                            language = language,
                            source_name = providerName,
                            source_url = recordUrl,
                            record_url = recordUrl,
                            publisher_url = providerHomepage,
                            preview_url = previewLink,
                            full_text_url = fullTextUrl,
                            download_url = downloadUrl,
                            access_status = accessStatus,
                            verification = "URL verified",
                            confidence = "High",
                            date_checked = currentDate
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext results
    }
}
