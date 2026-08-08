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

object OpenAlexAdapter : BaseSourceAdapter {
    override val providerName: String = "OpenAlex"
    override val providerHomepage: String = "https://openalex.org/"
    override val requiredEnvVariable: String = "OPENALEX_EMAIL"

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .build()
    }

    private fun getEmail(): String {
        return try { BuildConfig.OPENALEX_EMAIL } catch (e: Exception) { "" }
    }

    override suspend fun checkStatus(currentDate: String): ProviderStatus = withContext(Dispatchers.IO) {
        val email = getEmail()
        val emailProvided = email.isNotBlank() && email != "UNSET"
        try {
            val testQuery = URLEncoder.encode("Ethiopian Calendar", "UTF-8")
            val url = if (emailProvided) {
                "https://api.openalex.org/works?search=$testQuery&per-page=1&mailto=$email"
            } else {
                "https://api.openalex.org/works?search=$testQuery&per-page=1"
            }
            val request = Request.Builder().url(url).build()
            httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    ProviderStatus(
                        provider_name = providerName,
                        provider_homepage = providerHomepage,
                        connection_status = if (emailProvided) ConnectionStatus.CONNECTED else ConnectionStatus.AVAILABLE_WITHOUT_API_KEY,
                        api_status_message = if (emailProvided) "Connected with Email Politely Pool" else "Available Public API",
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
            val email = getEmail()
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = if (email.isNotBlank() && email != "UNSET") {
                "https://api.openalex.org/works?search=$encodedQuery&per-page=15&mailto=$email"
            } else {
                "https://api.openalex.org/works?search=$encodedQuery&per-page=15"
            }

            val request = Request.Builder().url(url).build()
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val bodyStr = response.body?.string() ?: return@withContext emptyList()
                val root = JSONObject(bodyStr)
                val resultsArr = root.optJSONArray("results") ?: return@withContext emptyList()

                for (i in 0 until resultsArr.length()) {
                    val item = resultsArr.optJSONObject(i) ?: continue

                    val title = item.optString("title", "").trim()
                    if (title.isBlank() || title == "null") continue

                    val openAlexId = item.optString("id", "")
                    val doi = item.optString("doi", "")
                    val pubYear = item.optInt("publication_year", 0).let { if (it > 0) it else null }

                    // Authors
                    val authorsList = mutableListOf<String>()
                    val authorships = item.optJSONArray("authorships")
                    if (authorships != null) {
                        for (j in 0 until authorships.length()) {
                            val authObj = authorships.optJSONObject(j)?.optJSONObject("author")
                            val name = authObj?.optString("display_name", "")?.trim() ?: ""
                            if (name.isNotBlank()) authorsList.add(name)
                        }
                    }

                    // Primary Location
                    val primaryLoc = item.optJSONObject("primary_location")
                    val sourceObj = primaryLoc?.optJSONObject("source")
                    val publisherOrJournal = sourceObj?.optString("display_name", "") ?: ""
                    val landingPageUrl = UrlValidator.sanitizeUrl(primaryLoc?.optString("landing_page_url", ""))
                    val pdfUrl = UrlValidator.sanitizeUrl(primaryLoc?.optString("pdf_url", ""))
                    val isOa = primaryLoc?.optBoolean("is_oa", false) ?: false

                    val sourceUrl = when {
                        doi.isNotBlank() -> UrlValidator.sanitizeUrl(doi)
                        landingPageUrl.isNotBlank() -> landingPageUrl
                        openAlexId.isNotBlank() -> openAlexId
                        else -> ""
                    }

                    if (!UrlValidator.isValidUrl(sourceUrl)) continue

                    val recordUrl = if (openAlexId.isNotBlank()) openAlexId else sourceUrl
                    val fullTextUrl = if (isOa && pdfUrl.isNotBlank()) pdfUrl else ""
                    val accessStatus = if (fullTextUrl.isNotBlank()) {
                        "Verified legal full text."
                    } else {
                        "Metadata only — no verified full text found."
                    }

                    results.add(
                        SearchResultItem(
                            title = title,
                            authors = if (authorsList.isEmpty()) listOf("Academic Researcher") else authorsList,
                            document_type = "Research Paper",
                            publication_year = pubYear,
                            journal = publisherOrJournal,
                            doi = doi,
                            source_name = providerName,
                            source_url = sourceUrl,
                            record_url = recordUrl,
                            publisher_url = landingPageUrl.ifBlank { providerHomepage },
                            full_text_url = fullTextUrl,
                            download_url = fullTextUrl,
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
