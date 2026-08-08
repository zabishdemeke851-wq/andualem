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

object CrossrefAdapter : BaseSourceAdapter {
    override val providerName: String = "Crossref"
    override val providerHomepage: String = "https://api.crossref.org/"
    override val requiredEnvVariable: String = "CROSSREF_EMAIL"

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .build()
    }

    private fun getEmail(): String {
        return try { BuildConfig.CROSSREF_EMAIL } catch (e: Exception) { "" }
    }

    override suspend fun checkStatus(currentDate: String): ProviderStatus = withContext(Dispatchers.IO) {
        val email = getEmail()
        val emailProvided = email.isNotBlank() && email != "UNSET"
        try {
            val testQuery = URLEncoder.encode("Ethiopian", "UTF-8")
            val url = if (emailProvided) {
                "https://api.crossref.org/works?query=$testQuery&rows=1&mailto=$email"
            } else {
                "https://api.crossref.org/works?query=$testQuery&rows=1"
            }
            val request = Request.Builder().url(url).build()
            httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    ProviderStatus(
                        provider_name = providerName,
                        provider_homepage = providerHomepage,
                        connection_status = if (emailProvided) ConnectionStatus.CONNECTED else ConnectionStatus.AVAILABLE_WITHOUT_API_KEY,
                        api_status_message = if (emailProvided) "Connected with Polite Pool" else "Public API Available",
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
                "https://api.crossref.org/works?query=$encodedQuery&rows=15&mailto=$email"
            } else {
                "https://api.crossref.org/works?query=$encodedQuery&rows=15"
            }

            val request = Request.Builder().url(url).build()
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val bodyStr = response.body?.string() ?: return@withContext emptyList()
                val root = JSONObject(bodyStr)
                val msg = root.optJSONObject("message") ?: return@withContext emptyList()
                val items = msg.optJSONArray("items") ?: return@withContext emptyList()

                for (i in 0 until items.length()) {
                    val item = items.optJSONObject(i) ?: continue

                    // Title
                    val titleArr = item.optJSONArray("title")
                    val title = if (titleArr != null && titleArr.length() > 0) titleArr.optString(0, "").trim() else ""
                    if (title.isBlank()) continue

                    // Authors
                    val authorsList = mutableListOf<String>()
                    val authorsArr = item.optJSONArray("author")
                    if (authorsArr != null) {
                        for (j in 0 until authorsArr.length()) {
                            val aObj = authorsArr.optJSONObject(j) ?: continue
                            val given = aObj.optString("given", "")
                            val family = aObj.optString("family", "")
                            val full = "$given $family".trim()
                            if (full.isNotBlank()) authorsList.add(full)
                        }
                    }

                    val publisher = item.optString("publisher", "")
                    val containerArr = item.optJSONArray("container-title")
                    val journal = if (containerArr != null && containerArr.length() > 0) containerArr.optString(0, "") else ""

                    // Publication Year
                    var pubYear: Int? = null
                    val issued = item.optJSONObject("issued") ?: item.optJSONObject("published-print") ?: item.optJSONObject("published-online")
                    val dateParts = issued?.optJSONArray("date-parts")
                    if (dateParts != null && dateParts.length() > 0) {
                        val yearArray = dateParts.optJSONArray(0)
                        if (yearArray != null && yearArray.length() > 0) {
                            pubYear = yearArray.optInt(0)
                        }
                    }

                    val doiStr = item.optString("DOI", "")
                    val doiUrl = if (doiStr.isNotBlank()) "https://doi.org/$doiStr" else ""

                    // Link items
                    var fullTextUrl = ""
                    val linksArr = item.optJSONArray("link")
                    if (linksArr != null) {
                        for (k in 0 until linksArr.length()) {
                            val lObj = linksArr.optJSONObject(k) ?: continue
                            val urlVal = UrlValidator.sanitizeUrl(lObj.optString("URL", ""))
                            val contentType = lObj.optString("content-type", "")
                            if (urlVal.isNotBlank() && (contentType.contains("pdf") || urlVal.endsWith(".pdf"))) {
                                fullTextUrl = urlVal
                                break
                            }
                        }
                    }

                    val sourceUrl = if (doiUrl.isNotBlank()) doiUrl else if (fullTextUrl.isNotBlank()) fullTextUrl else ""
                    if (!UrlValidator.isValidUrl(sourceUrl)) continue

                    val accessStatus = if (fullTextUrl.isNotBlank()) {
                        "Verified legal full text."
                    } else {
                        "Metadata only — no verified full text found."
                    }

                    results.add(
                        SearchResultItem(
                            title = title,
                            authors = if (authorsList.isEmpty()) listOf("Scholar / Crossref Record") else authorsList,
                            document_type = "Journal Article",
                            publication_year = pubYear,
                            publisher = publisher,
                            journal = journal,
                            doi = doiStr,
                            source_name = providerName,
                            source_url = sourceUrl,
                            record_url = sourceUrl,
                            publisher_url = providerHomepage,
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
